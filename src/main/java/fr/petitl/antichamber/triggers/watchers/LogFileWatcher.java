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
