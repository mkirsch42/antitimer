package fr.petitl.antichamber.gui.panels;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 *
 */
public class TableCellRendererAsEditor extends JComboBox implements TableCellRenderer {

    public TableCellRendererAsEditor() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultComboBoxModel currentModel = (DefaultComboBoxModel) getModel();
        currentModel.removeAllElements();
        currentModel.addElement(value);
        currentModel.setSelectedItem(value);
        return this;
    }
}
