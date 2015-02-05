package org.xid.explorer;/*
 * Copyright 2015 to org.xid.explorer.CloudModelExplorer authors
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

import io.airlift.airline.Option;

/**
* Created by j5r on 01/02/2015.
*/
public abstract class ExplorerCommand implements Runnable {
    @Option(name = "-v", description = "Verbose mode")
    public boolean verbose;

    public void log(String message) {
        if (verbose) System.out.println("[Explorer] " + message);
    }

    public void info(String message) {
        System.out.println("[Explorer] " + message + ".");
    }

    public void error(String message) {
        error(message, null);
    }
    public void error(String message, Throwable e) {
        System.err.println("[Explorer] Error " + message + ".");
        if (e != null && verbose) e.printStackTrace();
    }

    public void fatalError(String message, int status, Throwable e) {
        System.err.println("[Explorer] Fatal Error " + message + ".");
        if (e != null && verbose) e.printStackTrace();
        System.err.println("Exiting Explorer");
        System.exit(status);
    }
}
