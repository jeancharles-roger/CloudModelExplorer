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
import io.undertow.io.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.PathTemplateHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import org.xid.explorer.ExplorerCommand;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import static io.undertow.Handlers.exceptionHandler;
import static io.undertow.Handlers.gracefulShutdown;
import static io.undertow.Handlers.pathTemplate;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.urlDecodingHandler;

public abstract class NetworkCommand extends ExplorerCommand {

    @Option(name = {"-p", "--port"}, description = "Port for service")
    public int port = 9091;

    protected Path getStaticFilePath() {
        return null;
    }

    protected Undertow createServer(Map<String, HttpHandler> handlers) {
        // Registers handlers to corresponding paths
        PathTemplateHandler pathHandler = pathTemplate();
        for (Entry<String, HttpHandler> entry : handlers.entrySet()) {
            pathHandler.add(entry.getKey(), entry.getValue());
        }
        // TODO a default handler to NOT FOUND

        // Adds static file
        Path modelPath = getStaticFilePath();
        if (modelPath != null) {
            FileResourceManager resourceManager = new FileResourceManager(modelPath.toFile(), 4 * 1024);
            pathHandler.add("/files/*", resource(resourceManager));
        }

        // Handles exceptions (including ServerException)
        ExceptionHandler exceptionHandler = exceptionHandler(pathHandler);
        exceptionHandler.addExceptionHandler(ServerException.class, (exchange) -> {
            Throwable exception = exchange.getAttachment(ExceptionHandler.THROWABLE);
            int code = exception instanceof ServerException ? ((ServerException) exception).getCode() : StatusCodes.INTERNAL_SERVER_ERROR;
            String message = exception.getMessage();
            exchange.setResponseCode(code);
            exchange.getResponseSender().send(createMessage(message, isAskingForJson(exchange)));
        });

        // Logs all requests
        HttpHandler logHandler = (HttpServerExchange exchange) -> {
            long start = System.currentTimeMillis();
            exceptionHandler.handleRequest(exchange);
            long end = System.currentTimeMillis();
            long time = end - start < 0 ? 0 : end - start;
            info("[" + exchange.getRequestMethod() + "]" + exchange.getRequestPath() + " in " + time + " ms");
        };

        Undertow.Builder builder = Undertow.builder();
        builder.addHttpListener(port, "localhost");
        builder.setHandler(gracefulShutdown(urlDecodingHandler("UTF-8", logHandler)));
        return builder.build();
    }

    protected boolean isAskingForJson(HttpServerExchange exchange) {
        HeaderValues values = exchange.getRequestHeaders().get(Headers.ACCEPT);
        return values != null && values.getFirst().contains("application/json");
    }

    protected String createMessage(String message, boolean json) {
        return json ? "{ \"message\": \"" + message + "\"}" : message;
    }

    protected void dispatchGetPost(HttpServerExchange exchange, Runnable get, Runnable post) {
        if (Methods.GET.equals(exchange.getRequestMethod())) get.run();
        if (Methods.POST.equals(exchange.getRequestMethod())) post.run();
    }

    protected void dispatchGetPutDelete(HttpServerExchange exchange, Runnable get, Runnable put, Runnable delete) {
        if (Methods.GET.equals(exchange.getRequestMethod())) get.run();
        if (Methods.PUT.equals(exchange.getRequestMethod())) put.run();
        if (Methods.DELETE.equals(exchange.getRequestMethod())) delete.run();
    }

    protected void send(HttpServerExchange exchange, Supplier<ReadableByteChannel> readSupplier) {
        Sender sender = exchange.getResponseSender();
        try (ReadableByteChannel modelChannel = readSupplier.get()) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            int bytesCount = modelChannel.read(buffer);
            while (bytesCount > 0) {
                buffer.flip();
                sender.send(buffer);
                buffer.clear();

                bytesCount = modelChannel.read(buffer);
            }
        } catch (IOException e) {
            error("Can't read '" + exchange.getRequestPath() + "'", e);
        } finally {
            sender.close();
        }
    }

    protected void write(HttpServerExchange exchange, Supplier<WritableByteChannel> writeSupplier) {
        ReadableByteChannel requestChannel = exchange.getRequestChannel();
        try (WritableByteChannel modelChannel = writeSupplier.get()) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            int bytesCount = requestChannel.read(buffer);
            while (bytesCount > 0) {
                buffer.flip();
                modelChannel.write(buffer);
                buffer.clear();

                bytesCount = requestChannel.read(buffer);
            }
        } catch (IOException e) {
            error("Can't write '/" + exchange.getRequestPath() + "'", e);
        } finally {
            try {
                requestChannel.close();
            } catch (IOException e) {
                error("Can't write '/" + exchange.getRequestPath() + "'", e);
            }
        }
    }

    protected String getParameter(HttpServerExchange exchange, String parameter) {
        return exchange.getQueryParameters().get(parameter).element();
    }
}
