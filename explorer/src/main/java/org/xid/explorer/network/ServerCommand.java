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
import org.xid.explorer.network.data.FileModelDataBase;
import org.xid.explorer.network.data.ModelDataBase;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
* Created by j5r on 01/02/2015.
*/
@Command(name = "server", description = "Starts a web server")
public class ServerCommand extends NetworkCommand {

    @Option(name = {"--master", "-m"}, description = "Master to connect to", required = true)
    public String master;

    @Option(name = {"--data", "-d"}, description = "Data path")
    public String data = ".";

    private ModelDataBase modelDataBase;

    @Override
    public void run() {

        modelDataBase = new FileModelDataBase(Paths.get(data));

        // TODO Commented handlers are to be implemented
        Map<String, HttpHandler> handlers = new HashMap<>();
        handlers.put("/{user}", this::handleUserRequest);
        handlers.put("/{user}/models", this::handleAllModelsRequest);
        handlers.put("/{user}/models/{model}", this::handleModelRequest);
        //handlers.put("/{user}/models/{model}/resources", this::handleModelAllResourcesRequest);
        handlers.put("/{user}/models/{model}/resources/{resource}", this::handleModelResourceRequest);
        //handlers.put("/{user}/models/{model}/explorations", this::handleModelAllExplorationsRequest);
        handlers.put("/{user}/models/{model}/explorations/{exploration}", this::handleModelExplorationRequest);
        //handlers.put("/{user}/models/{model}/explorations/{exploration}/states", this::handleExplorationAllStatesRequest);
        handlers.put("/{user}/models/{model}/explorations/{exploration}/states/{state}", this::handleExplorationStateRequest);
        Undertow server = createServer(handlers);
        server.start();
    }


    private void handleUserRequest(final HttpServerExchange exchange) throws Exception {
        dispatchGetPutDelete(exchange,
                // GET
                () -> send(exchange, () -> modelDataBase.getReadUserChannel(getParameter(exchange, "user"))),

                // PUT
                () -> {
                },

                // DELETE
                () -> {
                }
        );
    }

    private void handleAllModelsRequest(final HttpServerExchange exchange) throws Exception {
        String userId = getParameter(exchange, "user");
        Sender sender = exchange.getResponseSender();
        dispatchGetPost(exchange,
                // GET
                () -> {
                    sender.send("{" + modelDataBase.getAllModelIds(userId).map(id -> '"' + id + '"').collect(Collectors.joining(",")) + "}");
                },

                // POST
                () -> {
                    /* TODO Implements new model creation */
                }
        );
    }

    private void handleModelRequest(final HttpServerExchange exchange) throws Exception {
        exchange.dispatch(() -> {
            String userId = getParameter(exchange, "user");
            String modelId = getParameter(exchange, "model");

            dispatchGetPutDelete(exchange,
                    // GET
                    () -> send(exchange, () -> modelDataBase.getReadModelChannel(userId, modelId)),

                    // PUT
                    () -> write(exchange, () -> modelDataBase.getWriteModelChannel(userId, modelId, false)),

                    // DELETE
                    () -> {
                    }
            );
        });
    }

    private void handleModelResourceRequest(final HttpServerExchange exchange) throws Exception {
        // dispatch send to IO thread.
        exchange.dispatch(() -> {
            String userId = getParameter(exchange, "user");
            String modelId = getParameter(exchange, "model");
            String resourceId = getParameter(exchange, "resource");

            dispatchGetPutDelete(exchange,
                    // GET
                    () -> send(exchange, () -> modelDataBase.getReadModelResourceChannel(userId, modelId, resourceId)),

                    // PUT
                    () -> write(exchange, () -> modelDataBase.getWriteModelResourceChannel(userId, modelId, resourceId, false)),

                    // DELETE
                    () -> {
                    }
            );
        });
    }

    private void handleModelExplorationRequest(final HttpServerExchange exchange) throws Exception {
        System.out.println("Exploration: " + exchange.getQueryParameters());
    }

    private void handleExplorationStateRequest(final HttpServerExchange exchange) throws Exception {
        // How to randomly access state for read and write in constant time ?
        // By using a cache for loaded exploration results
        System.out.println("State: " + exchange.getQueryParameters());
    }

}
