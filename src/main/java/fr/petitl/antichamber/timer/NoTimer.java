package fr.petitl.antichamber.timer;

import java.util.List;

import fr.petitl.antichamber.triggers.TriggerInfo;

public class NoTimer implements TimerControl {

    @Override
    public void exit() {}

    @Override
    public void buildAndInjectRun(String name, List<TriggerInfo> splits) {}

    @Override
    public void split(long stopTime) {}

    @Override
    public void start(long stopTime) {}

    @Override
    public void reset() {}
    
    @Override
    public String toString() {
	return "notimer";
    }

}
