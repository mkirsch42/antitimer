package fr.petitl.antichamber.triggers;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.triggers.logger.LogChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.*;
import fr.petitl.antichamber.triggers.watchers.LogFileWatcher;
import fr.petitl.antichamber.triggers.watchers.SaveFileWatcher;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class GameStatus {
    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SS");
    private static Logger log = Logger.getLogger(GameStatus.class);

    private SaveFileWatcher saveFileWatcher;
    private LogFileWatcher logFileWatcher;
    private final int theEndSpammerKeyCode;
    private SplitEngine splitEngine;
    private StatusChangeListener scl;
    private boolean isCurrentlyRunning;
    private Sign latestSign;
    private Set<TriggerInfo> splitIndex = new HashSet<>();
    private long startTimestamp;
    private List<TriggerInfo> endingConditions;
    private boolean creditsFounds;

    public GameStatus(AntichamberSave save, LogFile log, int theEndSpammerKeyCode, SplitEngine splitEngine, StatusChangeListener scl) throws IOException {
        this.theEndSpammerKeyCode = theEndSpammerKeyCode;
        this.splitEngine = splitEngine;
        this.scl = scl;
        Watcher w = new Watcher();
        saveFileWatcher = new SaveFileWatcher(save, w);
        logFileWatcher = new LogFileWatcher(log, w);
        splitIndex.clear();
        latestSign = Sign.SIGN_0;
        isCurrentlyRunning = false;
        creditsFounds = false;
        startTimestamp = 0;
    }

    public void reset() {
        scl.gameStatusHasChanged();
        latestSign = Sign.SIGN_0;
        isCurrentlyRunning = false;
        creditsFounds = false;
        startTimestamp = 0;
        splitEngine.fireReset();
        log.info("Reset!");
    }

    public Set<Sign> getSigns() {
        return saveFileWatcher.getSave().getSigns();
    }

    public Set<Gun> getGuns() {
        return saveFileWatcher.getSave().getGuns();
    }

    public Set<String> getMapEntries() {
        return saveFileWatcher.getSave().getMapEntries();
    }

    public Set<PinkCube> getPinkCubes() {
        return saveFileWatcher.getSave().getPinkCubes();
    }

    public void setSplits(List<TriggerInfo> splits) {
        splitIndex.clear();
        log.debug("Setting new conditions");
        for (TriggerInfo split : splits) {
            splitIndex.add(split);
        }
    }

    public void setEndingConditions(List<TriggerInfo> endingConditions) {
        log.debug("Setting new ending conditions");
        this.endingConditions = endingConditions;
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
        log.info("Triggered " + type.toString() + " (" + count + "/" + type.getMaxInstances() + "): " + g);
        if(isCurrentlyRunning) {
            TriggerInfo<E> trigger = new TriggerInfo<>(type, g, false);
            if (splitIndex.contains(trigger)) {
                log.info("\t" + sdf.format(new Date(timestamp - startTimestamp)) + " Split (selected)");
                // damn I don't like that
                splitEngine.fireSplit(timestamp);
            }
            if (count == type.getMaxInstances()) {
                trigger.setMustBeComplete(true);
                if (splitIndex.contains(trigger)) {
                    log.info("\t" + sdf.format(new Date(timestamp - startTimestamp)) + " Split (complete)");
                    // damn I don't like that
                    splitEngine.fireSplit(timestamp);
                }
            }
        }
        checkEndingConditions(timestamp);
        scl.gameStatusHasChanged();
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
        public void newMapEntry(String m, int count, long timestamp) {
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
            checkEndingConditions(timestamp);
            scl.gameStatusHasChanged();
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
}
