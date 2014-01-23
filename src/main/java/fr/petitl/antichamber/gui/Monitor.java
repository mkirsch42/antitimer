package fr.petitl.antichamber.gui;


import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;
import fr.petitl.antichamber.triggers.save.Gun;
import fr.petitl.antichamber.triggers.save.PinkCube;
import fr.petitl.antichamber.triggers.save.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class Monitor {
    private static final String TITLE = "Antimonitor";
    private JPanel content;
    private JLabel signLabel;
    private JLabel gunLabel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JLabel mapPercent;

    private SquareMatrixPanel signWall = new SquareMatrixPanel(15, 8, TriggerType.SIGN.getMaxInstances(), new Color(51, 102, 255));
    private SquareMatrixPanel pinkWall = new SquareMatrixPanel(3, 5, TriggerType.PINK_CUBE.getMaxInstances(), new Color(254, 65, 209));
    private final JFrame frame;

    public Monitor() {
        mainPanel.add(signWall, BorderLayout.CENTER);
        rightPanel.add(pinkWall, BorderLayout.CENTER);
        frame = new JFrame(TITLE);
        frame.setContentPane(content);
        frame.setSize(650, 360);
        frame.setVisible(true);
    }

    public void update(GameStatus gameStatus) {
        signWall.clear();
        Set<Sign> signs = gameStatus.getSigns();
        for (Sign sign : signs) {
            signWall.add(sign.ordinal());
        }
        pinkWall.clear();
        for (PinkCube p : gameStatus.getPinkCubes()) {
            pinkWall.add(p.ordinal());
        }
        Sign latestSign = gameStatus.getLatestSign();
        String s = latestSign.getLabel();
        signLabel.setText("<html> <b>" + signs.size() + "/" + (TriggerType.SIGN.getMaxInstances()) + "</b> " +
                "#<b>" + (latestSign.ordinal() + 1) + "</b> " + s.substring(0, Math.min(s.length()-1, 40)) + "...</html>");
        Set<Gun> guns = gameStatus.getGuns();
        if(guns.isEmpty())
            gunLabel.setText("None");
        else {
            Gun g = Collections.max(guns);
            gunLabel.setText(g.toString());
        }
        mapPercent.setText(Math.floor(gameStatus.getMapEntries().size() * 1000.0 / TriggerType.MAP_UPDATE.getMaxInstances())/10 + "%");
        frame.setTitle(TITLE+" ["+(gameStatus.isCurrentlyRunning() ? "Running" : "Stopped")+"]");
    }
}
