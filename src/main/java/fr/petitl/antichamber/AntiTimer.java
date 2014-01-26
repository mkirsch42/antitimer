package fr.petitl.antichamber;

import fr.petitl.antichamber.gui.AntiTimerFrame;
import fr.petitl.antichamber.llanfair.LlanfairControl;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.SplitEngine;
import fr.petitl.antichamber.triggers.StatusChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.AntichamberSave;
import org.fenix.llanfair.Llanfair;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class AntiTimer implements StatusChangeListener, SplitEngine {

    private final LlanfairControl control;
    private final AntiTimerFrame frame;
    private final GameStatus gameStatus;

    public AntiTimer() throws IOException {
        Llanfair llanfair = new Llanfair();
        control = new LlanfairControl(llanfair);

        AntichamberSave save = new AntichamberSave(new File("Binaries/Win32/SavedGame.bin"));
        LogFile logFile = new LogFile(new File("UDKGame/Logs/Launch.log"));
        gameStatus = new GameStatus(save, logFile, KeyEvent.VK_P, this, this);

        frame = new AntiTimerFrame(control, gameStatus);
        frame.getFrame().setVisible(true);
    }

    @Override
    public void gameStatusHasChanged() {
        frame.update(gameStatus);
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
