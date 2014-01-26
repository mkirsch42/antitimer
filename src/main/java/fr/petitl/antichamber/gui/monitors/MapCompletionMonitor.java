package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class MapCompletionMonitor extends JFrame implements MonitorFrame {
    private static final float FONT_SIZE = 48;
    private JLabel label = new JLabel();

    public MapCompletionMonitor() {
        super("Map Completion");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(FONT_SIZE));
        add(label, BorderLayout.CENTER);
        setSize(200, 120);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        label.setText(Math.floor(status.getMapEntries().size() * 1000.0 / TriggerType.MAP_UPDATE.getMaxInstances()) / 10 + "%");
    }
}
