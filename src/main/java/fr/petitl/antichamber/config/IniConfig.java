package fr.petitl.antichamber.config;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

import fr.petitl.antichamber.Configuration;

public class IniConfig implements Configurable {

    private static final String configPath = Configuration.read().getAntichamberPath() + "/UDKGame/Config/";;
    IniTweak tweak;

    public IniConfig(IniTweak tweak) {
	this.tweak = tweak;
    }

    private static void remove(IniOption o) throws InvalidFileFormatException, IOException {
	Ini ini = new Ini(new File(configPath + o.file));
	Section sec = ini.get(o.section);
	int index = sec.getAll(o.key).indexOf(o.value);
	if (index != -1) {
	    sec.remove(o.key, index);
	    ini.store();
	}
    }

    private static void add(IniOption o) throws InvalidFileFormatException, IOException {
	Ini ini = new Ini(new File(configPath + o.file));
	Section sec = ini.get(o.section);
	sec.add(o.key, o.value);
	ini.store();
    }

    @Override
    public void config() throws InvalidFileFormatException, IOException {
	for (IniOption o : tweak.removes) {
	    remove(o);
	}
	for (IniOption o : tweak.adds) {
	    remove(o);
	}
	for (IniOption o : tweak.adds) {
	    add(o);
	}
    }

    @Override
    public void unconfig() throws InvalidFileFormatException, IOException {
	for (IniOption o : tweak.adds) {
	    remove(o);
	}
	for (IniOption o : tweak.removes) {
	    remove(o);
	}
	for (IniOption o : tweak.removes) {
	    add(o);
	}
    }

    @Override
    public String desc() {
	return tweak.name;
    }

    @Override
    public boolean isEnabled() throws InvalidFileFormatException, IOException {
	for(IniOption o : tweak.adds) {
	    Ini ini = new Ini(new File(configPath + o.file));
	    Section sec = ini.get(o.section);
	    if(sec == null) {
		return false;
	    }
	    if(!sec.getAll(o.key).contains(o.value)) {
		return false;
	    }
	}
	for(IniOption o : tweak.removes) {
	    Ini ini = new Ini(new File(configPath + o.file));
	    Section sec = ini.get(o.section);
	    if(sec != null && sec.getAll(o.key).contains(o.value)) {
		return false;
	    }
	}
	return true;
    }

}
