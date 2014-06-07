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
                Thread.sleep(5);
                long t = file.lastModified();
                long s = file.length();
                long l = System.nanoTime() / 1000000L;
                if (lastCheckedTime >= t && lastCheckedSize >= s)
                    continue;
                lastCheckedTime = t;
                lastCheckedSize = s;
                // we have detected a change... wait for the writer to finish
                Thread.sleep(80);

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
