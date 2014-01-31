package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.gui.panels.SquareMatrixPanel;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 *
 */
public class PinkWallMonitor extends JFrame implements MonitorFrame {
    private SquareMatrixPanel pinkWall = new SquareMatrixPanel(5, 3, TriggerType.PINK_CUBE.getMaxInstances(), new Color(254, 65, 209));

    public PinkWallMonitor() {
        super("Pink Wall");
        setContentPane(pinkWall);
        setSize(200, 150);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        pinkWall.clear();
        Set<PinkCube> pinkCubes = status.getPinkCubes();
        for (PinkCube pink : pinkCubes) {
            pinkWall.add(pink.ordinal());
        }
    }
}
