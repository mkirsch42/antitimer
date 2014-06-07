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
import java.util.EnumSet;
import java.util.Set;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.*;
import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.MapEntry;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

/**
 *
 */
public class SaveFileWatcher extends FileWatcher<AntichamberSave> {
    private static final Logger log = Logger.getLogger(SaveFileWatcher.class);
    private final AntichamberSave save;
    private SaveChangeListener listener;
    private final LogFile logFile;
    private Set<PinkCube> oldPinkCubes = EnumSet.noneOf(PinkCube.class);
    private Set<Sign> oldSigns = EnumSet.noneOf(Sign.class);
    private Set<Gun> oldGuns = EnumSet.noneOf(Gun.class);
    private Set<MapEntry> oldMapEntries = EnumSet.noneOf(MapEntry.class);

    public SaveFileWatcher(AntichamberSave save, SaveChangeListener listener, LogFile logFile) throws IOException {
        super(save);
        this.save = save;
        this.listener = listener;
        this.logFile = logFile;
    }

    protected void fileUpdated(long l) {
        // ensure that logFile is updated
        try {
            logFile.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(logFile.isLoadingSave()) {
            log.debug("Game is currently loading his save so... I'm preventing this update!");
            return;
        }
        Set<Sign> signs;
        Set<PinkCube> pinkCubes;
        Set<Gun> guns;
        EnumSet<MapEntry> mapEntries;

        synchronized (save) {
            if (save.getNbTriggers() == 0 && save.getPlayTime() == 0) {
                listener.saveReset(l);
                saveOldState();
                return;
            }

            signs = EnumSet.copyOf(save.getSigns());
            signs.removeAll(oldSigns);
            pinkCubes = EnumSet.copyOf(save.getPinkCubes());
            pinkCubes.removeAll(oldPinkCubes);
            guns = EnumSet.copyOf(save.getGuns());
            guns.removeAll(oldGuns);
            mapEntries = EnumSet.copyOf(save.getMapEntries());
            mapEntries.removeAll(oldMapEntries);

            saveOldState();
        }
        // release the lock asap
        for (Sign sign : signs) {
            listener.newSign(sign, oldSigns.size(), l);
        }

        int pinkSize = oldPinkCubes.size();
        // fix the pink completion problem
        if(oldPinkCubes.contains(PinkCube.OOB))
            pinkSize--;
        if(oldPinkCubes.contains(PinkCube.GLITCHED_GALLERY))
            pinkSize--;

        for (PinkCube pinkCube : pinkCubes) {
            listener.newSecret(pinkCube, pinkSize, l);
        }
        for (Gun gun : guns) {
            listener.newGun(gun, oldGuns.size(), l);
        }
        for (MapEntry mapEntry : mapEntries) {
            listener.newMapEntry(mapEntry, oldMapEntries.size(), l);
        }
    }

    private void saveOldState() {
        oldPinkCubes.clear();
        oldPinkCubes.addAll(save.getPinkCubes());
        oldSigns.clear();
        oldSigns.addAll(save.getSigns());
        oldGuns.clear();
        oldGuns.addAll(save.getGuns());
        oldMapEntries.clear();
        oldMapEntries.addAll(save.getMapEntries());
    }

}
