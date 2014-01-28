package fr.petitl.antichamber.gui.monitors;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerType;
import fr.petitl.antichamber.triggers.save.data.Sign;

import javax.swing.*;

/**
 *
 */
public class SignMonitor extends JFrame implements MonitorFrame {
    private static final float FONT_SIZE = 32;
    private JLabel signLabel = new JLabel();

    public SignMonitor() {
        super("Sign");
        add(signLabel);
        setSize(800, 150);
        signLabel.setFont(signLabel.getFont().deriveFont(FONT_SIZE));
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
        Sign latestSign = status.getLatestSign();
        String s = latestSign.getLabel();
        signLabel.setText("<html><b>" + status.getSigns().size() + "/" + (TriggerType.SIGN.getMaxInstances()) + "</b> " +
                "#<b>" + (latestSign.ordinal() + 1) + "</b> " + s + "...</html>");
    }
}
