package fr.petitl.antichamber.gui.panels;

import java.util.EventListener;

/**
 *
 */
public interface TableButtonListener extends EventListener {
    public void tableButtonClicked( int row, int col );
}
