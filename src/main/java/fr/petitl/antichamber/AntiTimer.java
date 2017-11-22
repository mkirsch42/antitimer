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

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.json.JSONObject;

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
    public final static String VERSION = "0.2.5";
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

	if (cfg.getCheckUpdates()) {
		checkVersion(frame);
	}
	
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

    public static void checkVersion(JFrame frame) {
	// Start a thread ot determine the most recent verison that has been
	// uploaded
	Thread newerVersionThread = new Thread(() -> {
	    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
		    new URL("https://api.github.com/repos/mkirsch42/antitimer/releases/latest").openStream()))) {
		
		StringBuffer buffer  = new StringBuffer();
		int read;
		char[] chars = new char[1024];
		while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
		String jsonStr = buffer.toString();
		
		JSONObject json = new JSONObject(jsonStr);
		String latest = json.getString("tag_name");
		System.out.println("Latest version: " + latest);
		if(!latest.equals(VERSION)) {
		    System.out.println("Update available");
		    updateAvailable(json, frame);
		}
		
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(frame, "Failed to check most recent version.", "Antitimer updates",
			JOptionPane.WARNING_MESSAGE);
		e.printStackTrace();
	    }
	}, "Newer Version Thread");
	
	newerVersionThread.setDaemon(true);
	newerVersionThread.start();
    }
    
    public static void updateAvailable(JSONObject json, JFrame frame) {
	int reply = JOptionPane.showConfirmDialog(frame, "New version available: "+json.getString("tag_name")+"\n\n"
		+ json.getString("body") + "\n\nDownload?", 
		"Antitimer updates", JOptionPane.YES_NO_OPTION);
	if(reply == JOptionPane.YES_OPTION) {
	    Desktop desktop = Desktop.getDesktop();
            if(desktop == null)
                return;
            try {
                desktop.browse(URI.create(json.getString("html_url")));
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame, "Weird... cannot open browser... go to " + json.getString("html_url"));
            }
	}
    }

    @Override
    public TimerControl getTimer() {
	return control;
    }
}
