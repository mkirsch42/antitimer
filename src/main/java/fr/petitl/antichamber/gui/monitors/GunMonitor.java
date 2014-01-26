package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.save.data.Gun;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class GunMonitor extends JFrame implements MonitorFrame {
    private static final float FONT_SIZE = 48;
    private JLabel label = new JLabel();

    public GunMonitor() {
        super("Gun");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(FONT_SIZE));
        add(label, BorderLayout.CENTER);
        label.setText("Yellow");
        setSize(200, 100);
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        Set<Gun> guns = status.getGuns();
        if(guns.isEmpty())
            label.setText("None");
        else {
            Gun g = Collections.max(guns);
            label.setText(g.toString());
        }
    }
}
