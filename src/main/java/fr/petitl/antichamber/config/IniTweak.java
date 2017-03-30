package fr.petitl.antichamber.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum IniTweak {

    Q_ESC("Rebind Esc to Q",
	    new IniOption[]{
		    new IniOption("UDKInput.ini", "Engine.PlayerInput", "Bindings", "(Name=\"Q\",Command=\"ShowMenu | onrelease CancelExit\",Control=False,Shift=False,Alt=False,bIgnoreCtrl=False,bIgnoreShift=False,bIgnoreAlt=False)")
	    },
	    new IniOption[]{
		    new IniOption("UDKInput.ini", "Engine.PlayerInput", "Bindings", "(Name=\"Escape\",Command=\"ShowMenu | onrelease CancelExit\",Control=False,Shift=False,Alt=False,bIgnoreCtrl=False,bIgnoreShift=False,bIgnoreAlt=False)")
	    }),
    SCRL_FIRE("Bind Fire to Scroll Wheel",
	    new IniOption[]{
		    new IniOption("UDKInput.ini", "Engine.PlayerInput", "Bindings", "(Name=\"MouseScrollUp\",Command=\"GBA_Fire\",Control=False,Shift=False,Alt=False,bIgnoreCtrl=False,bIgnoreShift=False,bIgnoreAlt=False)"),
		    new IniOption("UDKInput.ini", "Engine.PlayerInput", "Bindings", "(Name=\"MouseScrollDown\",Command=\"GBA_Fire\",Control=False,Shift=False,Alt=False,bIgnoreCtrl=False,bIgnoreShift=False,bIgnoreAlt=False)")
	    },
	    new IniOption[]{}),
    CONSOLE("Enable Console",
	    new IniOption[]{},
	    new IniOption[]{
		    new IniOption("DefaultInput.ini", "Engine.Console", "ConsoleKey", "None"),
		    new IniOption("DefaultInput.ini", "Engine.Console", "TypeKey", "None")
	    }),
    NO_CAP("Remove FPS Cap (Disallowed in 100% and Pink Cubes)",
	    new IniOption[]{},
	    new IniOption[]{}),
    END_HACK("Allow tracking in The End",
	    new IniOption[]{
		    new IniOption("UDKInput.ini", "Engine.PlayerInput", "Bindings", "(Name=\"P\",Command=\"ListWaves\",Control=False,Shift=False,Alt=False,bIgnoreCtrl=False,bIgnoreShift=False,bIgnoreAlt=False)")
	    },
	    new IniOption[]{});
    
    public final Set<IniOption> adds, removes;
    public final String name;
    
    private IniTweak(String name, IniOption[] adds, IniOption[] removes) {
	this.adds = new HashSet<IniOption>(Arrays.asList(adds));
	this.removes = new HashSet<IniOption>(Arrays.asList(removes));
	this.name= name;
    }
    
}
