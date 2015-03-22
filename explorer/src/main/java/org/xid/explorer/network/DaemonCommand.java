/*
 * Copyright 2015 to CloudModelExplorer authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xid.explorer.network;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import io.undertow.Undertow;
import io.undertow.io.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.boon.Boon;
import org.xid.explorer.ActionMonitor;
import org.xid.explorer.BFSExplorer;
import org.xid.explorer.ResourceResolver;
import org.xid.explorer.model.ModelDescription;
import org.xid.explorer.network.data.DataPresenter;
import org.xid.explorer.result.ModelExploration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "daemon", description = "Starts an explorer daemon, it handles distributed exploration")
public class DaemonCommand extends NetworkCommand {

    @Option(name = {"-m", "--master"}, description = "Master for daemon, if none the daemon is a master")
    public String master = null;

    private final Map<String, DataPresenter> completedExplorations = new ConcurrentHashMap<>();

    private final Set<String> runningExploration = Collections.synchronizedSet(new HashSet<>());

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void run() {
        // TODO Commented handlers are to be implemented
        Map<String, HttpHandler> handlers = new HashMap<>();

        handlers.put("/explorations", this::handleAllExplorationsRequest);
        handlers.put("/explorations/{exploration}", this::handleExplorationRequest);

        //handlers.put("/status", this::handleStatusRequest);

        Undertow server = createServer(handlers);
        server.start();
    }

    private void handleExplorationRequest(HttpServerExchange exchange) throws Exception {
        String exploration = getParameter(exchange, "exploration");
        String resourcesPath = getParameter(exchange, "resources");

        // TODO for all return HTML or JSON depending on client preference
        Sender responseSender = exchange.getResponseSender();
        if (completedExplorations.containsKey(exploration)) {
            // Exploration is completed, presents the result
            DataPresenter presenter = completedExplorations.get(exploration);
            responseSender.send(presenter.toJson());

        } else if (runningExploration.contains(exploration)) {
            // Exploration is running, presents a status
            responseSender.send("Running");

        } else {
            runningExploration.add(exploration);

            // Exploration doesn't exist, start it.
            executorService.submit(() -> {
                try {
                    ResourceResolver resourceResolver = new ServerResourceResolver(resourcesPath);
                    ModelDescription description = ModelDescription.loadDescription(resourceResolver.readEntry("model.json"));

                    // prepares the explorer
                    BFSExplorer explorer = new BFSExplorer(description, resourceResolver);
                    explorer.initialize(null);

                    // explores the model and stores the result
                    ModelExploration modelExploration = explorer.explore(ActionMonitor.EMPTY);
                    completedExplorations.put(exploration, createModelExplorationPresenter(modelExploration));

                } catch (Exception e) {
                    error("Exploration of '" + exploration + "' failed", e);
                    completedExplorations.put(exploration, createExceptionPresenter(e));
                } finally {
                    runningExploration.remove(exploration);
                }
            });

            // TODO factorize with running
            responseSender.send("Running");
        }
    }

    private void handleAllExplorationsRequest(HttpServerExchange exchange) {

    }

    private DataPresenter createModelExplorationPresenter(ModelExploration modelExploration) {
        return new DataPresenter() {
            @Override
            public String toJson() throws Exception{
                return Boon.toJson(modelExploration);
            }

            @Override
            public String toHtml() throws Exception {
                // TODO implement a HTML print
                return Boon.toJson(modelExploration);
            }
        };
    }

    private DataPresenter createExceptionPresenter(Exception exception) {
        return new DataPresenter() {
            @Override
            public String toJson() throws Exception{
                return Boon.toJson(exception);
            }

            @Override
            public String toHtml() throws Exception {
                // TODO implement a HTML print
                return Boon.toJson(exception);
            }
        };
    }

}
