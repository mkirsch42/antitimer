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

package fr.petitl.antichamber.triggers;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.timer.TimerControl;
import fr.petitl.antichamber.triggers.logger.LogChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.AntichamberSave;
import fr.petitl.antichamber.triggers.save.SaveChangeListener;
import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.MapEntry;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;
import fr.petitl.antichamber.triggers.watchers.LogFileWatcher;
import fr.petitl.antichamber.triggers.watchers.SaveFileWatcher;

/**
 *
 */
public class GameStatus {
    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SS");
    private static Logger log = Logger.getLogger(GameStatus.class);

    private SaveFileWatcher saveFileWatcher;
    private final int theEndSpammerKeyCode;
    private SplitEngine splitEngine;
    private StatusChangeListener scl;
    private boolean isCurrentlyRunning;
    private Sign latestSign;
    private List<TriggerInfo> splits = new ArrayList<>();
    private int splitIdx = 0;

    private long startTimestamp =  System.nanoTime() / 1000000L;
    private List<TriggerInfo> endingConditions;
    private Set<TriggerType> triggerComplete = EnumSet.noneOf(TriggerType.class);
    private List<DetectedTriggerInfo> detectedTriggers = new ArrayList<>();
    private boolean creditsFounds;

    public GameStatus(AntichamberSave save, LogFile log, int theEndSpammerKeyCode, SplitEngine splitEngine, StatusChangeListener scl) throws IOException {
        this.theEndSpammerKeyCode = theEndSpammerKeyCode;
        this.splitEngine = splitEngine;
        this.scl = scl;
        Watcher w = new Watcher();
        saveFileWatcher = new SaveFileWatcher(save, w, log);
        new LogFileWatcher(log, w);
        splits.clear();
        latestSign = Sign.SIGN_0;
        triggerComplete.clear();
        isCurrentlyRunning = false;
        creditsFounds = false;
        startTimestamp = 0;
    }

    public void reset() {
        scl.gameStatusHasChanged();
        latestSign = Sign.SIGN_0;
        triggerComplete.clear();
        detectedTriggers.clear();
        isCurrentlyRunning = false;
        creditsFounds = false;
        startTimestamp = 0;
        splitIdx = 0;
        splitEngine.fireReset();
        log.info("Reset!");
    }

    public Set<Sign> getSigns() {
        return saveFileWatcher.getSave().getSigns();
    }

    public Set<Gun> getGuns() {
        return saveFileWatcher.getSave().getGuns();
    }

    public Set<MapEntry> getMapEntries() {
        return saveFileWatcher.getSave().getMapEntries();
    }

    public Set<PinkCube> getPinkCubes() {
        return saveFileWatcher.getSave().getPinkCubes();
    }

    public void setSplits(List<TriggerInfo> splits) {
        log.debug("Setting new conditions");
        this.splits = new ArrayList<>(splits);
    }

    public void setEndingConditions(List<TriggerInfo> endingConditions) {
        log.debug("Setting new ending conditions");
        this.endingConditions = endingConditions;
    }
    
    public void setTimer(TimerControl t, boolean set) {
	splitEngine.setTimer(t, set);
    }

    public TimerControl getTimer() {
	return splitEngine.getTimer();
    }
    
    private void checkEndingConditions(long timestamp) {
        if(endingConditions == null || endingConditions.size() == 0)
            return;
        for (TriggerInfo trigger : endingConditions) {
            boolean valid;
            if(trigger.mustBeComplete()) {
                valid = isTriggerValidAsComplete(trigger);
            } else {
                valid = isTriggerValidAsSpecific(trigger);
            }
            if(!valid)
                return;
        }
        // we made it this far, ending conditions are valid !
        log.info("Finished! "+ sdf.format(new Date(timestamp - startTimestamp)));
        if(isCurrentlyRunning) {
            isCurrentlyRunning = false;
            splitEngine.fireEnd(timestamp);
        }
    }

    private boolean isTriggerValidAsSpecific(TriggerInfo trigger) {
        Set collection = null;
        switch (trigger.getType()) {
            case GUN:
                collection = getGuns();
                break;
            case SIGN:
                collection = getSigns();
                break;
            case PINK_CUBE:
                collection = getPinkCubes();
                break;
            case MAP_UPDATE:
                collection = getMapEntries();
                break;
            case FINISH_GAME:
                return creditsFounds;
        }
        if(collection == null)
            throw new IllegalStateException("Wut? Collection is null?");
        return collection.contains(trigger.getArgumentToMatch());
    }

    private boolean isTriggerValidAsComplete(TriggerInfo trigger) {
        Set collection = null;
        switch (trigger.getType()) {
            case GUN:
                collection = getGuns();
                break;
            case SIGN:
                collection = getSigns();
                break;
            case PINK_CUBE:
                collection = getPinkCubes();
                break;
            case MAP_UPDATE:
                collection = getMapEntries();
                break;
            case FINISH_GAME:
                return creditsFounds;
        }
        if(collection == null)
            throw new IllegalStateException("Wut? Collection is null?");
        return collection.size() == trigger.getType().getMaxInstances();
    }

    public Sign getLatestSign() {
        return latestSign;
    }

    public boolean isCurrentlyRunning() {
        return isCurrentlyRunning;
    }

    private <E> void genericTrigger(TriggerType type, E g, int count, long timestamp) {
        TriggerInfo trigger = new TriggerInfo(type, g, false);
        detectedTriggers.add(new DetectedTriggerInfo(timestamp, count, trigger));
        if (count == type.getMaxInstances() && !triggerComplete.contains(type)) {
            TriggerInfo completeTrigger = new TriggerInfo(type, null, true);
            triggerComplete.add(type);
            detectedTriggers.add(new DetectedTriggerInfo(timestamp, count, completeTrigger));
        }
        trigger = new TriggerInfo(type, g, false);
        log.info("Triggered " + type.toString() + " (" + count + "/" + type.getMaxInstances() + "): " + g);
        if(isCurrentlyRunning && splitIdx < splits.size()) {
            TriggerInfo toMatch = splits.get(splitIdx);
            if (trigger.equals(toMatch)) {
                log.info("\t" + sdf.format(new Date(timestamp - startTimestamp)) + " Split (selected)");
                // damn I don't like that
                splitEngine.fireSplit(timestamp);
                splitIdx++;
                if(splitIdx < splits.size())
                    toMatch = splits.get(splitIdx); // useless but security first
            }
            if (count == type.getMaxInstances()) {
                trigger.setMustBeComplete(true);
                if (trigger.equals(toMatch)) {
                    log.info("\t" + sdf.format(new Date(timestamp - startTimestamp)) + " Split (complete)");
                    // damn I don't like that
                    splitEngine.fireSplit(timestamp);
                    splitIdx++;
                }
            }
        }
        checkEndingConditions(timestamp);
        scl.gameStatusHasChanged();
    }

    public List<DetectedTriggerInfo> getDetectedTriggers() {
        return detectedTriggers;
    }



    private class Watcher implements SaveChangeListener, LogChangeListener {
        private long spamUntil = System.currentTimeMillis();
        private final Object lock = new Object();

        @Override
        public void newSign(final Sign s, final int count, final long timestamp) {
            latestSign = s;
            genericTrigger(TriggerType.SIGN, s, count, timestamp);
        }

        @Override
        public void newGun(Gun g, int count, long timestamp) {
            genericTrigger(TriggerType.GUN, g, count, timestamp);
        }


        @Override
        public void newMapEntry(MapEntry m, int count, long timestamp) {
            genericTrigger(TriggerType.MAP_UPDATE, m, count, timestamp);
        }

        @Override
        public void newSecret(PinkCube p, int count, long timestamp) {
            genericTrigger(TriggerType.PINK_CUBE, p, count, timestamp);
        }

        @Override
        public void saveReset(long timestamp) {
            reset();
        }

        @Override
        public void mapClicked(long timestamp) {
            log.info("Map clicked");
            if (isCurrentlyRunning || getSigns().size() > 1)
                return;
            isCurrentlyRunning = true;
            startTimestamp = timestamp;
            splitEngine.fireStart(timestamp);
            scl.gameStatusHasChanged();
        }

        @Override
        public void theEnd(long timestamp) {
            spamUntil = 0;
            log.info("The end!");
            creditsFounds = true;
            genericTrigger(TriggerType.FINISH_GAME, null, 1, timestamp);
        }

        @Override
        public void ghostFired(long timestamp) {
            log.debug("Ghost fired!");
            synchronized (lock) {
                if (System.currentTimeMillis() < spamUntil) {
                    log.debug("Thread should be still spamming so: no new thread, just update!");
                    spamUntil = System.currentTimeMillis() + 6000;
                    return;
                }
                spamUntil = System.currentTimeMillis() + 6000;
            }
            try {
                final Robot robot = new Robot();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            synchronized (lock) {
                                if (System.currentTimeMillis() > spamUntil)
                                    return; // end thread
                            }
                            log.debug("Pressing P!");
                            robot.keyPress(theEndSpammerKeyCode);
                            robot.delay(50);
                            robot.keyRelease(theEndSpammerKeyCode);
                            // this should eventually trigger theEnd if ghost cube reaches the final hole
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                spamUntil = 0;
                                return; // end process if interrupted
                            }
                        }
                    }
                }).start();
            } catch (AWTException e) {
                throw new IllegalStateException("Could not create robot?", e);
            }
        }
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }
}
