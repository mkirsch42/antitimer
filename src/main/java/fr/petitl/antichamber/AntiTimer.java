package fr.petitl.antichamber;

import fr.petitl.antichamber.gui.MapMonitor;
import fr.petitl.antichamber.gui.Monitor;
import fr.petitl.antichamber.gui.Splits;
import fr.petitl.antichamber.llanfair.LlanfairControl;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.SplitEngine;
import fr.petitl.antichamber.triggers.StatusChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.AntichamberSave;
import org.fenix.llanfair.Llanfair;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class AntiTimer implements StatusChangeListener, SplitEngine {

    private final Splits splitsFrame;
    private final Monitor monitorFrame;
    private final GameStatus gameStatus;
    private final LlanfairControl control;
    private final MapMonitor mapMonitor;

    public AntiTimer() throws IOException {
        Llanfair llanfair = new Llanfair();
        control = new LlanfairControl(llanfair);

        splitsFrame = new Splits(control);
        monitorFrame = new Monitor();
        mapMonitor = new MapMonitor();
        AntichamberSave save = new AntichamberSave(new File("Binaries/Win32/SavedGame.bin"));
        LogFile logFile = new LogFile(new File("UDKGame/Logs/Launch.log"));
        gameStatus = new GameStatus(save, logFile, KeyEvent.VK_P, this, this);

        splitsFrame.getSplitModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                gameStatus.setSplits(splitsFrame.getSplitModel().getTriggers());
            }
        });
        splitsFrame.getCompletionModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                gameStatus.setEndingConditions(splitsFrame.getCompletionModel().getTriggers());
            }
        });
    }

    @Override
    public void gameStatusHasChanged() {
        monitorFrame.update(gameStatus);
        mapMonitor.setEntries(gameStatus.getMapEntries());
    }

    @Override
    public void fireStart(long timestamp) {
        control.start(timestamp);
    }

    @Override
    public void fireSplit(long timestamp) {
        control.split(timestamp);
    }

    @Override
    public void fireEnd(long timestamp) {
        control.split(timestamp);
    }

    @Override
    public void fireReset() {
        control.reset();
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    new AntiTimer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
