package fr.petitl.antichamber.triggers.watchers;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public interface FileReader {
    void read() throws IOException;

    File getFile();
}
