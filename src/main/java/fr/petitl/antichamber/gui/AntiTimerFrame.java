package fr.petitl.antichamber.gui;

import fr.petitl.antichamber.gui.monitors.*;
import fr.petitl.antichamber.gui.panels.Splits;
import fr.petitl.antichamber.llanfair.LlanfairControl;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AntiTimerFrame extends JFrame implements MonitorFrame {
    private static final String TITLE = "AntiTimer";
    private final GunMonitor gun;
    private final MapCompletionMonitor mapCompletion;
    private final MapMonitor map;
    private final PinkWallMonitor pinkWall;
    private final SignWallMonitor signWall;
    private final SignMonitor sign;
    private List<MonitorFrame> frames = new ArrayList<>();
    private LlanfairControl control;
    private Splits splits;
    private File saveFile;
    private GameStatus status;
    private Configuration conf;

    public AntiTimerFrame(LlanfairControl control, GameStatus status) {
        super(TITLE);

        this.control = control;
        this.status = status;
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
        sign = new SignMonitor();
        frames.add(sign);

        JMenuBar m = new JMenuBar();
        antiTimerMenu(m);
        monitorMenu(m);
        helpMenu(m);
        setJMenuBar(m);

        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void helpMenu(JMenuBar m) {
        JMenu help = new JMenu("Help");
        JMenuItem instructions = new JMenuItem("Instructions");
        help.add(instructions);
        JMenuItem about = new JMenuItem("About");
        help.add(about);
    }

    private void antiTimerMenu(JMenuBar m) {
        final JFrame frame = this;
        JMenu help = new JMenu("AntiTimer");
        JMenuItem load = new JMenuItem("Load");
        help.add(load);
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
        help.add(save);
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
        help.add(saveAs);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile = null;
                saveAction.actionPerformed(e);
            }
        });
        help.add(new JSeparator());
        JMenuItem configure = new JMenuItem("Configure");
        help.add(configure);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Not implemented yet", "Configure", JOptionPane.ERROR_MESSAGE);
            }
        });
        help.add(new JSeparator());
        JMenuItem clear = new JMenuItem("Clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splits.getCompletionModel().clear();
                splits.getSplitModel().clear();
            }
        });
        help.add(clear);
        JMenuItem initLlanfair = new JMenuItem("Init Llanfair");
        help.add(initLlanfair);
        initLlanfair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.buildAndInjectRun(splits.getTitleTextField().getText(), splits.getSplitModel().getTriggers());
            }
        });
        help.add(new JSeparator());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.exit();
            }
        });
        help.add(exit);
        m.add(help);
    }

    private void monitorMenu(JMenuBar m) {
        JMenu monitors = new JMenu("Monitors");
        monitors.add(new MonitorMenuItem(gun, "Current Gun"));
        monitors.add(new MonitorMenuItem(map, "Map"));
        monitors.add(new MonitorMenuItem(mapCompletion, "Map Completion"));
        monitors.add(new MonitorMenuItem(signWall, "Moral Wall"));
        monitors.add(new MonitorMenuItem(sign, "Latest Sign"));
        monitors.add(new MonitorMenuItem(pinkWall, "Pink Wall"));
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

    private class MonitorMenuItem extends JMenuItem implements ActionListener {
        private final MonitorFrame frame;

        private MonitorMenuItem(MonitorFrame frame, String label) {
            super(label);
            this.frame = frame;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (status != null) {
                frame.update(status);
            }
            frame.getFrame().setVisible(true);
        }
    }
}
