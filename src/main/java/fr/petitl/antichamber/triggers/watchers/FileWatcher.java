package fr.petitl.antichamber.triggers.watchers;

import java.io.File;

/**
 *
 */
public abstract class FileWatcher<E extends FileReader> implements Runnable {
    protected final Thread thread;
    protected final E save;
    protected final File file;
    private long lastCheckedTime = 0;
    private long lastCheckedSize = 0;

    public FileWatcher(E save) {
        thread = new Thread(this, "File saver watcher");
        this.save = save;
        file = save.getFile();
        thread.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1);
                long t = file.lastModified();
                long s = file.length();
                long l = System.nanoTime() / 1000000L;
                if (lastCheckedTime >= t && lastCheckedSize >= s)
                    continue;
                lastCheckedTime = t;
                lastCheckedSize = s;
                // we have detected a change... wait for the writer to finish
                Thread.sleep(10);

                save.read();
                fileUpdated(l);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getSave() {
        return save;
    }

    protected abstract void fileUpdated(long l);
}
