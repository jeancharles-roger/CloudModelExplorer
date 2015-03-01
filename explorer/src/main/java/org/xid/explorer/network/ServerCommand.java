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
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.xid.explorer.network.data.FileModelDataBase;
import org.xid.explorer.network.data.ModelDataBase;
import org.xid.explorer.network.data.User;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, HttpHandler> handlers = new HashMap<>();
        handlers.put("/rest/{user}", this::handleUserRequest);
        handlers.put("/rest/{user}/model/{model}", this::handleModelRequest);
        handlers.put("/rest/{user}/model/{model}/exploration/{exploration}", this::handleExplorationRequest);
        handlers.put("/rest/{user}/model/{model}/exploration/{exploration}/state/{state}", this::handleStateRequest);
        Undertow server = createServer(handlers);
        server.start();
    }


    private User getUser(final HttpServerExchange exchange) throws Exception {
        String userLogin = exchange.getQueryParameters().get("user").element();
        User user = modelDataBase.getUser(userLogin);
        if (user == null) throw ServerException.NOT_FOUND;
        return user;
    }

    private void handleUserRequest(final HttpServerExchange exchange) throws Exception {
        User user = getUser(exchange);
        exchange.getResponseSender().send(user.getName());
    }

    private void handleModelRequest(final HttpServerExchange exchange) throws Exception {
        System.out.println("Model: " + exchange.getQueryParameters());
    }

    private void handleExplorationRequest(final HttpServerExchange exchange) throws Exception {
        System.out.println("Exploration: " + exchange.getQueryParameters());
    }

    private void handleStateRequest(final HttpServerExchange exchange) throws Exception {
        // How to randomly access state for read and write in constant time ?
        System.out.println("State: " + exchange.getQueryParameters());
    }

}
