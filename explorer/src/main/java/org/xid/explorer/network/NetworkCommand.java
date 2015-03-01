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

import io.airlift.airline.Option;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import org.xid.explorer.ExplorerCommand;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import static io.undertow.Handlers.*;

public abstract class NetworkCommand extends ExplorerCommand {

    @Option(name = {"-p", "--port"}, description = "Port for service")
    public int port = 9091;

    protected Path getStaticFilePath() {
        return null;
    }

    protected Undertow createServer(Map<String,HttpHandler> handlers) {
        // Registers handlers to corresponding paths
        PathTemplateHandler pathHandler = pathTemplate();
        for (Entry<String,HttpHandler> entry : handlers.entrySet()) {
            pathHandler.add(entry.getKey(), entry.getValue());
        }

        // Adds static file
        Path modelPath = getStaticFilePath();
        if (modelPath != null) {
            FileResourceManager resourceManager = new FileResourceManager(modelPath.toFile(), 4*1024);
            pathHandler.add("/files/*", resource(resourceManager));
        }

        ExceptionHandler exceptionHandler = exceptionHandler(pathHandler);
        exceptionHandler.addExceptionHandler(ServerException.class, this::handleError);

        Undertow.Builder builder = Undertow.builder();
        builder.addHttpListener(port, "localhost");
        builder.setHandler(gracefulShutdown(urlDecodingHandler("UTF-8", exceptionHandler)));
        return builder.build();
    }

    protected boolean isAskingForJson(HttpServerExchange exchange) {
        HeaderValues values = exchange.getRequestHeaders().get(Headers.ACCEPT);
        return values != null && values.getFirst().contains("application/json");
    }

    protected String createMessage(String message, boolean json) {
        return json ? "{ \"message\": \"" + message +"\"}" : message;
    }

    protected void handleError(HttpServerExchange exchange) {
        Throwable exception = exchange.getAttachment(ExceptionHandler.THROWABLE);
        int code = exception instanceof ServerException ? ((ServerException) exception).getCode() : 500;
        exchange.setResponseCode(code);
        exchange.getResponseSender().send(createMessage(exception.getMessage(), isAskingForJson(exchange)));
    }
}
