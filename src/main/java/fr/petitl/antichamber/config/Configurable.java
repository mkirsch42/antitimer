package fr.petitl.antichamber.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ini4j.InvalidFileFormatException;

public interface Configurable {

    void config() throws InvalidFileFormatException, IOException;
    void unconfig() throws InvalidFileFormatException, IOException;
    boolean isEnabled() throws InvalidFileFormatException, IOException;
    default void toggle() throws InvalidFileFormatException, IOException {
	if(isEnabled()) {
	    unconfig();
	} else {
	    config();
	}
    }
    String desc();
    
    static List<Configurable> allConfigs() {
	List<Configurable> configs = Stream.of(IniTweak.values()).map(x->new IniConfig(x)).collect(Collectors.toList());
	configs.add(new MovieConfig());
	return configs;
    }
    
}
