/*
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
package org.xid.explorer;

import io.airlift.airline.Cli;
import io.airlift.airline.Cli.CliBuilder;
import io.airlift.airline.Help;
import org.xid.explorer.network.DaemonCommand;
import org.xid.explorer.network.ServerCommand;

/**
 * CloudModelExplorer command line entry point.
 */
public class Explorer {

    public static void main(String[] args) {
        CliBuilder<Runnable> builder = Cli.<Runnable>builder("Explorer");
        builder.withDescription("Explorer startup");
        builder.withDefaultCommand(Help.class);
        builder.withCommand(Help.class);
        builder.withCommand(ExploreCommand.class);
        builder.withCommand(DaemonCommand.class);
        builder.withCommand(ServerCommand.class);

        Cli<Runnable> explorerParser = builder.build();
        explorerParser.parse(args).run();
    }

}
