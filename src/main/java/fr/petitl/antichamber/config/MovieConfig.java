package fr.petitl.antichamber.config;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.ini4j.InvalidFileFormatException;

import fr.petitl.antichamber.Configuration;

public class MovieConfig implements Configurable {

    @Override
    public void config() {
	unconfig();
	File movieDir = new File(Configuration.read().getAntichamberPath() + "/UDKGame/Movies");
	Stream.of(movieDir.listFiles()).forEach(x -> {
	    x.renameTo(new File(x.getAbsolutePath() + ".bak"));
	});
    }

    @Override
    public void unconfig() {
	File movieDir = new File(Configuration.read().getAntichamberPath() + "/UDKGame/Movies");
	Stream.of(movieDir.listFiles()).forEach(x -> {
	    x.renameTo(new File(x.getAbsolutePath().replace(".bak", "")));
	});
    }

    @Override
    public String desc() {
	return "Skip intro movies";
    }

    @Override
    public boolean isEnabled() throws InvalidFileFormatException, IOException {
	File movieDir = new File(Configuration.read().getAntichamberPath() + "/UDKGame/Movies");
	return Stream.of(movieDir.listFiles()).allMatch(f -> f.getAbsolutePath().contains(".bak"));
    }
    
}
