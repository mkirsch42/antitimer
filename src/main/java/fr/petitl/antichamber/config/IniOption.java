package fr.petitl.antichamber.config;

public class IniOption {

    public final String file, section, key, value;
    
    public IniOption(String file, String section, String key, String value) {
	this.file = file;
	this.section = section;
	this.key = key;
	this.value = value;
    }
    
}
