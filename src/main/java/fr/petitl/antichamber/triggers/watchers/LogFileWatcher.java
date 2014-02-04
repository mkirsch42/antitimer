/*
 * Copyright 2014 Loic Petit
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

package fr.petitl.antichamber.triggers.watchers;

import java.io.IOException;

import fr.petitl.antichamber.triggers.logger.LogChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;

/**
 *
 */
public class LogFileWatcher extends FileWatcher<LogFile> {
    private LogChangeListener listener;
    private long oldLastFiringGhost = 0;
    private long oldLastMapClick = 0;
    private long oldLastDyingWorld = 0;

    public LogFileWatcher(LogFile logFile, LogChangeListener listener) throws IOException {
        super(logFile);
        this.listener = listener;
    }

    protected void fileUpdated(long l) {
        double dyingWorldTime;
        long lastFiringGhost;
        long lastMapClick;
        long lastDyingWorld;
        synchronized (save) {
            dyingWorldTime = save.getDyingWorldTime();
            lastFiringGhost = save.getLastFiringGhost();
            lastMapClick = save.getLastMapClick();
            lastDyingWorld = save.getLastDyingWorld();
        }
        if(lastMapClick > oldLastMapClick) {
            listener.mapClicked(l);
        }
        if(lastFiringGhost > oldLastFiringGhost)
            listener.ghostFired(l);
        if(lastDyingWorld > oldLastDyingWorld)
            listener.theEnd((long) (l-dyingWorldTime*1000));
        oldLastMapClick = lastMapClick;
        oldLastFiringGhost = lastFiringGhost;
        oldLastDyingWorld = lastDyingWorld;
    }
}
