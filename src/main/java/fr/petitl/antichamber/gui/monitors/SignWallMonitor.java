package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.gui.panels.SquareMatrixPanel;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 *
 */
public class SignWallMonitor extends JFrame implements MonitorFrame {
    private SquareMatrixPanel signWall = new SquareMatrixPanel(15, 8, TriggerType.SIGN.getMaxInstances(), new Color(51, 102, 255));

    public SignWallMonitor() {
        super("Sign Wall");
        add(signWall, BorderLayout.CENTER);
        setSize(650, 370);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        signWall.clear();
        Set<Sign> signs = status.getSigns();
        for (Sign sign : signs) {
            signWall.add(sign.ordinal());
        }
    }
}
