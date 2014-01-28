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
import javax.swing.filechooser.FileFilter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class AntiTimer implements StatusChangeListener, SplitEngine {
    public final static String VERSION = "0.1";
    private LlanfairControl control;
    private AntiTimerFrame frame;
    private GameStatus gameStatus;

    public AntiTimer() throws IOException {
        Configuration cfg = Configuration.read();

        File exe = cfg.getAntichamberExe();
        if (!exe.exists()) {
            JFileChooser fc = new JFileChooser(cfg.getAntichamberPath());
            fc.setDialogTitle("Select the Antichamber executable");
            fc.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getAbsolutePath().contains("Win32") && f.getName().equals("UDK.exe");
                }

                @Override
                public String getDescription() {
                    return "UDK.exe";
                }
            });
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setDialogType(JFileChooser.OPEN_DIALOG);
            if (fc.showDialog(null, "Select") != JFileChooser.APPROVE_OPTION) {
                return;
            }
            exe = fc.getSelectedFile();
            String exeAbsolutePath = exe.getParentFile().getParentFile().getParentFile().getAbsolutePath();
            cfg.setAntichamberPath(exeAbsolutePath);
        }

        Llanfair llanfair = new Llanfair();
        control = new LlanfairControl(llanfair);

        AntichamberSave save = new AntichamberSave(cfg.getAntichamberSave());
        LogFile logFile = new LogFile(cfg.getAntichamberLog());
        gameStatus = new GameStatus(save, logFile, KeyEvent.VK_P, this, this);

        frame = new AntiTimerFrame(control, gameStatus, cfg);
        frame.getFrame().setVisible(true);
        frame.update(gameStatus);
    }

    @Override
    public void gameStatusHasChanged() {
        if(frame != null)
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
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new AntiTimer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
