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

package fr.petitl.antichamber.triggers.logger;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.triggers.watchers.FileReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class LogFile implements FileReader {
    private static final Logger log = Logger.getLogger(LogFile.class);
    private static final String LOADING_SAVED_GAME = "ScriptLog: Loading saved game";
    private static final String FINISHED_LOADING_SAVED_GAME = "Kismet: Music 0_0 start";
    private long lastMapClick = 0;
    private long lastFiringGhost = 0;
    private double dyingWorldTime = 0;
    private long lastDyingWorld = 0;
    private final File file;

    private long offset = 0;

    private static final String FIRING_GHOST = "ScriptLog: Weapon = HazardWeap_TileGun_0";
    private static final Pattern DYING_WORLD_PATTERN = Pattern
                    .compile("Yes\\s+([0-9.]+)\\s+0.75\\s+HazardMusic_Ending.DyingWorldv3");
    private static final String MAP_CLICK = "ScriptWarning: Accessed array 'HazardGame_0.MapArray' out of bounds (-1/0)";
    private boolean loadingSave = false;

    public LogFile(File file) throws IOException {
        this.file = file;
        if (!file.exists())
            throw new IllegalStateException("Log file doesn't seem to exist - is the path correct?");
        offset = file.lastModified();
    }

    @Override
    public void read() throws IOException {
        long len = file.length();
        if (len < offset) {
            // Log must have been jibbled or deleted.
            log.debug("Log file was reset. Restarting logging from start of file.");
            offset = 0;
            lastMapClick = 0;
            lastFiringGhost = 0;
            dyingWorldTime = 0;
            lastDyingWorld = 0;
            loadingSave = false;
        }
        if (len > offset) {
            log.debug("LogFile updated");
            // File must have had something added to it!
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(offset);
            String line;
            while ((line = raf.readLine()) != null) {
                this.processLine(line, raf.getFilePointer());
            }
            offset = raf.getFilePointer();
            raf.close();
        }
    }

    private void processLine(String line, long filePointer) {
        log.trace(line);
        if(line.contains(FIRING_GHOST)) {
            log.debug("Found Firing Ghost");
            lastFiringGhost = filePointer;
        } else if(line.contains(MAP_CLICK)) {
            log.debug("Found Map Click");
            lastMapClick = filePointer;
        } else if(line.contains(LOADING_SAVED_GAME)) {
            log.debug("Game is loading its file");
            loadingSave = true;
        } else if(line.contains(FINISHED_LOADING_SAVED_GAME)) {
            log.debug("Game has finished loading its file");
            loadingSave = false;
        } else {
            Matcher m = DYING_WORLD_PATTERN.matcher(line);
            if(m.find()) {
                log.debug("Found Dying World!");
                dyingWorldTime = Double.parseDouble(m.group(1));
                lastDyingWorld = filePointer;
            }
        }
    }

    @Override
    public File getFile() {
        return file;
    }

    public long getLastMapClick() {
        return lastMapClick;
    }

    public long getLastFiringGhost() {
        return lastFiringGhost;
    }

    public double getDyingWorldTime() {
        return dyingWorldTime;
    }

    public long getLastDyingWorld() {
        return lastDyingWorld;
    }

    public boolean isLoadingSave() {
        return loadingSave;
    }
}
