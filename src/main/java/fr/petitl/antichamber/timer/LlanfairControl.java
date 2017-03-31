/*
 * Copyright 2014 Loic Petit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.petitl.antichamber.timer;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.fenix.llanfair.Language;
import org.fenix.llanfair.Llanfair;
import org.fenix.llanfair.Run;
import org.fenix.llanfair.Segment;
import org.fenix.llanfair.Settings;
import org.fenix.llanfair.Time;
import org.jnativehook.GlobalScreen;

import fr.petitl.antichamber.log.Logger;
import fr.petitl.antichamber.triggers.TriggerInfo;

/**
 *
 */
public class LlanfairControl implements TimerControl {
    private static Logger log = Logger.getLogger(LlanfairControl.class);
    private Run run;
    private Llanfair llanfair;

    public LlanfairControl(Llanfair llanfair) {
	this.llanfair = llanfair;
    }

    public Run getRun() {
	ensureRun();
	return run;
    }

    @Override
    public void exit() {
	reset();
        GlobalScreen.unregisterNativeHook();
	llanfair.setVisible(false);
	llanfair = null;
    }

    @Override
    public void buildAndInjectRun(String name, List<TriggerInfo> splits) {
	Run r = new Run("Antichamber (" + name + ")");
	for (TriggerInfo trigger : splits) {
	    String segmentName = trigger.getType().toString() + ": ";
	    segmentName += trigger.mustBeComplete() ? "Complete" : trigger.getArgumentToMatch().toString();
	    r.addSegment(new Segment(segmentName));
	}
	r.addSegment(new Segment("Complete " + name));
	llanfair.setRun(r);
    }

    private void ensureRun() {
	run = getPrivateField(llanfair, "run", Run.class);
    }

    private void setPrivateField(Object obj, String field, Object current) {
	try {
	    Field currentField = obj.getClass().getDeclaredField(field);
	    currentField.setAccessible(true);
	    currentField.set(obj, current);
	} catch (Exception e) {
	    throw new IllegalStateException(e);
	}
    }

    @SuppressWarnings("unchecked")
    private <E> E getPrivateField(Object obj, String field, Class<E> clazz) {
	try {
	    Field currentField = obj.getClass().getDeclaredField(field);
	    currentField.setAccessible(true);
	    return (E) currentField.get(obj);
	} catch (Exception e) {
	    throw new IllegalStateException(e);
	}
    }

    @SuppressWarnings("unchecked")
    private <E> E callPrivateMethod(Object obj, String method, Class<E> clazz) {
	try {
	    Method m = obj.getClass().getDeclaredMethod(method);
	    m.setAccessible(true);
	    return (E) m.invoke(obj);
	} catch (Exception e) {
	    throw new IllegalStateException(e);
	}
    }

    @Override
    public void split(final long stopTime) {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalSplit(stopTime);
	    }
	});
    }

    private void internalSplit(long stopTime) {
	ensureRun();
	if (run.getState() != Run.State.ONGOING) {
	    throw new IllegalStateException("run is not on-going");
	}
	long segmentTime = stopTime - run.getSegment(run.getCurrent()).getStartTime();
	setPrivateField(run, "current", run.getCurrent() + 1);

	Time time = new Time(segmentTime);
	run.getSegment(run.getCurrent() - 1).setTime(time, 3);

	if (run.getCurrent() == run.getRowCount())
	    internalStop();
	else {
	    setPrivateField(run.getSegment(run.getCurrent()), "startTime", stopTime);
	}
	getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.currentSegment",
		run.getCurrent() - 1, run.getCurrent());

	// if ((run.isSegmented()) && (run.getState() == Run.State.ONGOING) &&
	// (run.getCurrent() > -1))
	// internalPause(stopTime);
    }

    @Override
    public void start(final long stopTime) {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    c.internalStart(stopTime);
		} catch (Exception e) {
		    log.error("Reset failed: " + e.getMessage());
		}
	    }
	});
    }

    private void internalStart(long startTime) {
	try {
	    ensureRun();
	    if ((run.getState() == null) || (run.getState() == Run.State.ONGOING)) {
		throw new IllegalStateException("illegal Run.State to start");
	    }

	    setPrivateField(run, "startTime", startTime);
	    setPrivateField(run, "current", 0);
	    setPrivateField(run, "state", Run.State.ONGOING);
	    setPrivateField(run.getSegment(run.getCurrent()), "startTime", startTime);

	    getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.state",
		    Run.State.READY, run.getState());
	    getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.currentSegment", -1,
		    0);
	} catch (Exception e) {
	    log.error("Start failed: " + e.getMessage());
	}
    }

    private void unsplit() {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalUnsplit();
	    }
	});
    }

    public void internalUnsplit() {
	try {
	    ensureRun();
	    run.unsplit();
	} catch (Exception e) {
	    log.error("Unsplit failed: " + e.getMessage());
	}
    }

    public void pause(final long stop) {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalPause(stop);
	    }
	});
    }

    private void internalPause(long stopTime) {
	try {
	    ensureRun();
	    if (run.getState() != Run.State.ONGOING) {
		throw new IllegalStateException("run is not on-going");
	    }
	    setPrivateField(run, "state", Run.State.PAUSED);
	    long segmentTime = stopTime - run.getSegment(run.getCurrent()).getStartTime();
	    Time time = new Time(segmentTime);
	    run.getSegment(run.getCurrent()).setTime(time, 3, true);
	    getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.state",
		    Run.State.ONGOING, run.getState());

	} catch (Exception e) {
	    log.error("Pause failed: " + e.getMessage());
	}
    }

    public void resume(final long stop) {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalResume(stop);
	    }
	});
    }

    private void internalResume(long stop) {
	try {
	    ensureRun();
	    if (run.getState() != Run.State.PAUSED) {
		throw new IllegalStateException("run is not paused");
	    }
	    setPrivateField(run, "state", Run.State.ONGOING);
	    long startTime = stop - run.getTime(run.getCurrent(), 3, false).getMilliseconds();
	    setPrivateField(run, "startTime", startTime);

	    Segment crt = run.getSegment(run.getCurrent());
	    setPrivateField(crt, "startTime", stop - crt.getTime(3).getMilliseconds());

	    long cumulative = 0L;
	    for (int i = 0; i < run.getCurrent(); i++) {
		Segment iSeg = run.getSegment(i);
		setPrivateField(iSeg, "startTime", startTime + cumulative);
		cumulative += iSeg.getTime(3).getMilliseconds();
	    }
	    getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.state",
		    Run.State.PAUSED, run.getState());

	} catch (Exception e) {
	    log.error("Resume failed: " + e.getMessage());
	}
    }

    public void stop() {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalStop();
	    }
	});
    }

    private void internalStop() {
	try {
	    ensureRun();
	    run.stop();
	} catch (Exception e) {
	    log.error("Stop failed: " + e.getMessage());
	}
    }

    private boolean proceedWithOverwrite() {
	llanfair.setIgnoreNativeInputs(true);
	boolean betterRun = this.run.isPersonalBest();
	boolean betterSgt = this.run.hasSegmentsBest();

	if ((betterRun) || (betterSgt)) {
	    String message = betterRun ? Language.WARN_BETTER_RUN.value() : Language.WARN_BETTER_TIMES.value();

	    int option = JOptionPane.showConfirmDialog(llanfair, message, Language.WARNING.value(), 1, 2);

	    if (option == 2) {
		llanfair.setIgnoreNativeInputs(false);
		return false;
	    }
	    if (option == 0) {
		this.run.saveLiveTimes(!betterRun);
		this.run.reset();
		callPrivateMethod(llanfair, "writeFile", Void.class);
	    }
	}
	llanfair.setIgnoreNativeInputs(false);
	return true;
    }

    @Override
    public void reset() {
	try {
	    final LlanfairControl c = this;
	    ensureRun();
	    Run.State state = run.getState();
	    if (state != Run.State.NULL && (!Settings.WARN_ON_RESET.value() || proceedWithOverwrite()))
		SwingUtilities.invokeLater(new Runnable() {
		    @Override
		    public void run() {
			c.internalReset();
		    }
		});
	} catch (Exception e) {
	    log.error("Reset failed: " + e.getMessage());
	}
    }

    private void internalReset() {
	try {
	    ensureRun();
	    run.reset();
	} catch (Exception e) {
	    log.error("Reset failed: " + e.getMessage());
	}
    }

    public void skip() {
	final LlanfairControl c = this;
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		c.internalSkip();
	    }
	});
    }

    private void internalSkip() {
	try {
	    ensureRun();
	    run.skip();
	} catch (Exception e) {
	    log.error("Reset failed: " + e.getMessage());
	}
    }

    @Override
    public String toString() {
	return "llanfair";
    }
}
