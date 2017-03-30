package fr.petitl.antichamber.timer;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.fenix.llanfair.Llanfair;

public class TimerFactory {
    
    public static TimerControl fromString(String str) {
	switch(str) {
	case "llanfair":
	    return llanfair();
	case "livesplit":
	    return livesplit();
	case "notimer":
	    return notimer();
	}
	return notimer();
    }

    public static TimerControl llanfair() {
	return new LlanfairControl(new Llanfair());
    }
    
    public static TimerControl livesplit() {
	try {
	    return new LiveSplitControl();
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Couldn't connect to LiveSplit Server. Are you sure it is on (Control->Start Server)?\nRunning in timerless mode!", "LiveSplit Connection", JOptionPane.ERROR_MESSAGE);
	    return notimer();
	}
    }
    
    public static TimerControl notimer() {
	return new NoTimer();
    }    
}
