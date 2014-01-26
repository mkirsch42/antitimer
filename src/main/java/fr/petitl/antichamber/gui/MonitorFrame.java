package fr.petitl.antichamber.gui;

import fr.petitl.antichamber.triggers.GameStatus;

import javax.swing.*;

/**
 *
 */
public interface MonitorFrame {
    JFrame getFrame();
    void update(GameStatus status);
}
