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

package fr.petitl.antichamber.gui;

import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

import fr.petitl.antichamber.AntiTimer;
import fr.petitl.antichamber.Configuration;
import fr.petitl.antichamber.gui.monitors.*;
import fr.petitl.antichamber.gui.panels.Splits;
import fr.petitl.antichamber.timer.TimerControl;
import fr.petitl.antichamber.timer.TimerFactory;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerInfo;

/**
 *
 */
public class AntiTimerFrame extends JFrame implements MonitorFrame {
    private static final String TITLE = "AntiTimer";
    private static final String BITBUCKET_LINK = "https://github.com/mkirsch42/antitimer";
    private static final String ISSUES_LINK = "https://gitreports.com/issue/mkirsch42/antitimer";
    private static final long serialVersionUID = -2200305139211369118L;
    private final GunMonitor gun;
    private final MapCompletionMonitor mapCompletion;
    private final MapMonitor map;
    private final PinkWallMonitor pinkWall;
    private final SignWallMonitor signWall;
    private final TriggerMonitor trigger;
    private final LatestSignMonitor latestSign;
    private final VideoMonitor videoMonitor;
    private List<MonitorFrame> frames = new ArrayList<>();
    private TimerControl control;
    private Splits splits;
    private File saveFile;
    private GameStatus status;
    private Configuration cfg;

    public AntiTimerFrame(TimerControl control, GameStatus status, Configuration cfg) {
        super(TITLE);

        this.control = control;
        this.status = status;
        this.cfg = cfg;
        splits = new Splits(status);
        setContentPane(splits.getContent());
        gun = new GunMonitor();
        frames.add(gun);
        mapCompletion = new MapCompletionMonitor();
        frames.add(mapCompletion);
        map = new MapMonitor();
        frames.add(map);
        pinkWall = new PinkWallMonitor();
        frames.add(pinkWall);
        signWall = new SignWallMonitor();
        frames.add(signWall);
        trigger = new TriggerMonitor();
        frames.add(trigger);
        latestSign = new LatestSignMonitor();
        frames.add(latestSign);
        videoMonitor = new VideoMonitor();
        frames.add(videoMonitor);

        JMenuBar m = new JMenuBar();
        antiTimerMenu(m);
        monitorMenu(m);
        timerMenu(m);
        helpMenu(m);
        setJMenuBar(m);

        initWindows();

        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initWindows() {
        for (MonitorFrame frame : frames) {
            initWindows(frame);
        }
        initWindows(this);
    }

    private void initWindows(MonitorFrame frame) {
        final String windowName = frame.getClass().getSimpleName();
        Point w = cfg.getWindowLocation(windowName);
        final JFrame frameComponent = frame.getFrame();
        frameComponent.setLocation(w);
        frameComponent.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                cfg.setWindowSize(windowName, frameComponent.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                cfg.setWindowLocation(windowName, frameComponent.getLocation());
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }
    
    private void timerMenu(JMenuBar m) {
	JMenu timers = new JMenu("Timers");
	Stream.of(new String[]{"No Timer", "Llanfair", "LiveSplit"}).forEach(t -> {
	    JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(t);
	    jmi.addActionListener(event -> {
		status.setTimer(TimerFactory.fromString(t.replaceAll(" ", "").toLowerCase()), true);
	    });
	    timers.add(jmi);
	});
	m.add(timers);
    }
    
    public void refreshTimer(String id) {
	JMenu menu = getJMenuBar().getMenu(2);
	for(int i = 0; i < menu.getItemCount(); i++) {
	    JCheckBoxMenuItem jmi = (JCheckBoxMenuItem)menu.getItem(i);
	    if(id.equals(jmi.getText().replaceAll(" ", "").toLowerCase())){
		jmi.setState(true);
	    } else {
		jmi.setState(false);
	    }
	}
    }

    private void helpMenu(JMenuBar m) {
        final JFrame frame = this;
        JMenu help = new JMenu("Help");
        JMenuItem instructions = new JMenuItem("Instructions");
        help.add(instructions);
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                if(desktop == null)
                    return;
                try {
                    desktop.browse(URI.create(BITBUCKET_LINK));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Weird... cannot open browser... go to " + BITBUCKET_LINK);
                }
            }
        });
        JMenuItem bugreport = new JMenuItem("Report Bug/Feature");
        help.add(bugreport);
        bugreport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop desktop = Desktop.getDesktop();
                if(desktop == null)
                    return;
                try {
                    desktop.browse(URI.create(ISSUES_LINK));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Weird... cannot open browser... go to " + ISSUES_LINK);
                }
            }
        });
        JMenuItem update = new JMenuItem("Check for Updates");
        help.add(update);
        update.addActionListener((e)->AntiTimer.checkVersion(this));
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "AntiTimer " + AntiTimer.VERSION + "\n\n" +
                        "Developed by: WydD and mkirsch42\n\n" +
                        "Special thanks to:\n" +
                        "\tCrehl (for his early work on the sign tracker)\n" +
                        "\tCali and Pallidus (for their testing and feedbacks)\n" +
                        "\tAll the Antichamber Speedrunning Community\n\n" +
                        "AntiTimer embeds Llanfair 1.4.3 (“Dante”)\n" +
                        "\tdeveloped by Xunkar\n" +
                        "\treleased under CC-BY-SA\n" +
                        "\tembedded without changes\n");
            }
        });
        help.add(about);
        m.add(help);
    }

    private void antiTimerMenu(JMenuBar m) {
        final JFrame frame = this;
        JMenu anti = new JMenu("AntiTimer");
        JMenuItem launch = new JMenuItem("Launch Antichamber");
        anti.add(launch);
        launch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(new String[]{cfg.getAntichamberExe().getAbsolutePath(), "-FORCELOGFLUSH"});
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Error while trying to launch Antichamber: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JMenuItem load = new JMenuItem("Load");
        anti.add(load);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setDialogType(JFileChooser.OPEN_DIALOG);
                if (fc.showDialog(frame, "Open") != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                saveFile = fc.getSelectedFile();
                try {
                    FileInputStream fis = new FileInputStream(saveFile);
                    ObjectInputStream oos = new ObjectInputStream(fis);
                    splits.getTitleTextField().setText((String) oos.readObject());
                    splits.getSplitModel().setTriggers((List<TriggerInfo>) oos.readObject());
                    splits.getCompletionModel().setTriggers((List<TriggerInfo>) oos.readObject());
                } catch (IOException | ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(frame, "Cannot read " + saveFile.getName() + "\n" + e1.getMessage());
                }
            }
        });
        JMenuItem save = new JMenuItem("Save");
        anti.add(save);
        final ActionListener saveAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveFile == null) {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fc.setDialogType(JFileChooser.SAVE_DIALOG);
                    if (fc.showDialog(frame, "Save") != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    saveFile = fc.getSelectedFile();
                    try {
                        saveFile.createNewFile();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame, "Cannot create file " + saveFile.getName() + "\n" + e1.getMessage());
                        return;
                    }
                }
                try (FileOutputStream fos = new FileOutputStream(saveFile, false)) {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(splits.getTitleTextField().getText());
                    oos.writeObject(splits.getSplitModel().getTriggers());
                    oos.writeObject(splits.getCompletionModel().getTriggers());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Cannot write to " + saveFile.getName() + "\n" + e1.getMessage());
                }
            }
        };
        save.addActionListener(saveAction);
        JMenuItem saveAs = new JMenuItem("Save as...");
        anti.add(saveAs);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile = null;
                saveAction.actionPerformed(e);
            }
        });
        anti.add(new JSeparator());
        JMenuItem configure = new JMenuItem("Configure");
        anti.add(configure);
        configure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        	new ConfigFrame().setVisible(true);
            }
        });
        anti.add(new JSeparator());
        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splits.getCompletionModel().clear();
                splits.getSplitModel().clear();
            }
        });
        anti.add(clear);
        JMenuItem initLlanfair = new JMenuItem("Init Llanfair");
        anti.add(initLlanfair);
        initLlanfair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.getTimer().buildAndInjectRun(splits.getTitleTextField().getText(), splits.getSplitModel().getTriggers());
            }
        });
        anti.add(new JSeparator());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.exit();
            }
        });
        anti.add(exit);
        m.add(anti);
    }

    private void monitorMenu(JMenuBar m) {
        JMenu monitors = new JMenu("Monitors");
        monitors.add(new MonitorMenuItem(map, "Map"));
        monitors.add(new MonitorMenuItem(signWall, "Moral Wall"));
        monitors.add(new MonitorMenuItem(pinkWall, "Pink Wall"));
        monitors.add(new MonitorMenuItem(trigger, "Triggers"));
        monitors.add(new MonitorMenuItem(latestSign, "Latest Sign"));
        monitors.add(new MonitorMenuItem(gun, "Current Gun"));
        monitors.add(new MonitorMenuItem(mapCompletion, "Map Completion"));
        monitors.add(new MonitorMenuItem(videoMonitor, "Video"));
        m.add(monitors);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        boolean running = status.isCurrentlyRunning();
        setTitle(TITLE + " [" + (running ? "Running" : "Stopped") + "]");
        for (MonitorFrame frame : frames) {
            if (frame.getFrame().isVisible())
                frame.update(status);
        }
    }

    private class MonitorMenuItem extends JCheckBoxMenuItem implements ActionListener, WindowListener {
        private static final long serialVersionUID = -7209267553708481233L;
        private final MonitorFrame frame;

        private MonitorMenuItem(MonitorFrame frame, String label) {
            super(label);
            this.frame = frame;
            addActionListener(this);
            frame.getFrame().setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            frame.getFrame().addWindowListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (status != null && isSelected()) {
                frame.update(status);
            }
            frame.getFrame().setVisible(isSelected());
        }

        @Override
        public void windowOpened(WindowEvent e) {
            setSelected(true);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            setSelected(false);
            frame.getFrame().setVisible(false);
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}
