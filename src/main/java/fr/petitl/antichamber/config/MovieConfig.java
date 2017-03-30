package fr.petitl.antichamber.config;

import java.io.File;
import java.util.stream.Stream;

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
	return "Remove intro movies";
    }
    
}
