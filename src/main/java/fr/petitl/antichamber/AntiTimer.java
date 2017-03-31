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

package fr.petitl.antichamber;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import fr.petitl.antichamber.gui.AntiTimerFrame;
import fr.petitl.antichamber.timer.TimerControl;
import fr.petitl.antichamber.timer.TimerFactory;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.SplitEngine;
import fr.petitl.antichamber.triggers.StatusChangeListener;
import fr.petitl.antichamber.triggers.logger.LogFile;
import fr.petitl.antichamber.triggers.save.AntichamberSave;

/**
 *
 */
public class AntiTimer implements StatusChangeListener, SplitEngine {
    public final static String VERSION = "0.1";
    private TimerControl control;
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

	control = TimerFactory.notimer();

	AntichamberSave save = new AntichamberSave(cfg.getAntichamberSave());
	LogFile logFile = new LogFile(cfg.getAntichamberLog());
	gameStatus = new GameStatus(save, logFile, KeyEvent.VK_P, this, this);

	frame = new AntiTimerFrame(control, gameStatus, cfg);
	frame.getFrame().setVisible(true);
	frame.update(gameStatus);

	setTimer(cfg.getTimer(), false);
    }

    @Override
    public void gameStatusHasChanged() {
	if (frame != null)
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

    public void setTimer(TimerControl t, boolean set) {
	control.exit();
	control = t;
	frame.refreshTimer(control.toString());
	if (set) {
	    Configuration.read().setTimer(control);
	}
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
