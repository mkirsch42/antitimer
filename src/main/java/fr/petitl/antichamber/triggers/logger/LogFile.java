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

    public LogFile(File file) throws IOException {
        this.file = file;
        if (!file.exists())
            throw new IllegalStateException("Log file doesn't seem to exist - is the path correct?");
        offset = file.lastModified();
    }

    @Override
    public void read() throws IOException {
        long len = file.length();
        log.debug("LogFile updated");
        if (len < offset) {
            // Log must have been jibbled or deleted.
            log.debug("Log file was reset. Restarting logging from start of file.");
            offset = 0;
            lastMapClick = 0;
            lastFiringGhost = 0;
            dyingWorldTime = 0;
            lastDyingWorld = 0;
        } else if (len > offset) {
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
        if(line.contains(FIRING_GHOST)) {
            log.debug("Found Firing Ghost");
            lastFiringGhost = filePointer;
        } else if(line.contains(MAP_CLICK)) {
            log.debug("Found Map Click");
            lastMapClick = filePointer;
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
}
