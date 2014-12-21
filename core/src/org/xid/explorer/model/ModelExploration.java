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

package org.xid.explorer.model;

/**
 * ModelExploration stores the complete result for an exploration. It contains all the explored ModelState, the
 * ModelTransition and some meta-information (for instance duration, source model, etc...).
 */
public class ModelExploration {

    public static enum CompletionStatus { complete, stoppedByUser, stoppedByHeuristic, reachedMemoryLimit, errorOccurred}

    private final CompletionStatus status;

    private final int stateCount;

    private final int transitionCount;

    /** Exploration duration in milliseconds. */
    private final long duration;

    public ModelExploration(CompletionStatus status, long duration, int stateCount, int transitionCount) {
        this.status = status;
        this.duration = duration;
        this.stateCount = stateCount;
        this.transitionCount = transitionCount;
    }

    public CompletionStatus getStatus() {
        return status;
    }

    public long getDuration() {
        return duration;
    }

    public int getStateCount() {
        return stateCount;
    }

    public int getTransitionCount() {
        return transitionCount;
    }

}
