package fr.petitl.antichamber.llanfair;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.util.List;

import fr.petitl.antichamber.triggers.TriggerInfo;
import org.fenix.llanfair.Llanfair;
import org.fenix.llanfair.Run;
import org.fenix.llanfair.Segment;
import org.fenix.llanfair.Time;

import javax.swing.*;

/**
 *
 */
public class LlanfairControl {
    private Run run;
    private Llanfair llanfair;

    public LlanfairControl(Llanfair llanfair) {
        this.llanfair = llanfair;
    }

    public Run getRun() {
        ensureRun();
        return run;
    }

    public void buildAndInjectRun(String name, List<TriggerInfo> splits) {
        Run r = new Run("Antichamber ("+name+")");
        for (TriggerInfo trigger : splits) {
            String segmentName = trigger.getType().toString() + ": ";
            segmentName += trigger.mustBeComplete() ? "Complete" : trigger.getArgumentToMatch().toString();
            r.addSegment(new Segment(segmentName));
        }
        r.addSegment(new Segment("Complete "+name));
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

        //if ((run.isSegmented()) && (run.getState() == Run.State.ONGOING) && (run.getCurrent() > -1))
//            internalPause(stopTime);
    }


    public void start(final long stopTime) {
        final LlanfairControl c = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                c.internalStart(stopTime);
            }
        });
    }

    private void internalStart(long startTime) {
        ensureRun();
        if ((run.getState() == null) || (run.getState() == Run.State.ONGOING)) {
            throw new IllegalStateException("illegal Run.State to start");
        }

        setPrivateField(run, "startTime", startTime);
        setPrivateField(run, "current", 0);
        setPrivateField(run, "state", Run.State.ONGOING);
        setPrivateField(run.getSegment(run.getCurrent()), "startTime", startTime);

        getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.state", Run.State.READY,
                run.getState());
        getPrivateField(run, "pcSupport", PropertyChangeSupport.class).firePropertyChange("run.currentSegment", -1, 0);
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
        ensureRun();
        run.unsplit();
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
        ensureRun();
        run.stop();
    }

    public void reset() {
        final LlanfairControl c = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                c.internalReset();
            }
        });
    }

    private void internalReset() {
        ensureRun();
        run.reset();
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
        ensureRun();
        run.skip();
    }

}
