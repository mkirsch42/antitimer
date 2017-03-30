package fr.petitl.antichamber.config;

import java.io.IOException;

import org.ini4j.InvalidFileFormatException;

public interface Configurable {

    void config() throws InvalidFileFormatException, IOException;
    void unconfig() throws InvalidFileFormatException, IOException;
    String desc();
    
}
