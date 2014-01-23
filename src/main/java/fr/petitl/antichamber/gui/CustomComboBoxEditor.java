package fr.petitl.antichamber.gui;

import fr.petitl.antichamber.triggers.save.data.Gun;
import fr.petitl.antichamber.triggers.save.data.PinkCube;
import fr.petitl.antichamber.triggers.save.data.Sign;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class CustomComboBoxEditor extends DefaultCellEditor {
    // Declare a model that is used for adding the elements to the `ComboBox`
    private DefaultComboBoxModel model;

    public CustomComboBoxEditor() {
        super(new JComboBox());
        //noinspection unchecked
        this.model = (DefaultComboBoxModel)((JComboBox)getComponent()).getModel();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (column != 0) {
            // Remove previous elements every time.
            // So that we can populate the elements based on the selection.
            model.removeAllElements();

            // getValueAt(..) method will give you the selection that is set for column one.
            TriggerType selectedItem = (TriggerType) table.getValueAt(row, 0);

            switch (selectedItem) {
                case FINISH_GAME:
                    model.addElement(SplitTableModel.COMPLETE_VALUE);
                    break;
                case MAP_UPDATE:
                    model.addElement(SplitTableModel.COMPLETE_VALUE);
                    break;
                case SIGN:
                    model.addElement(SplitTableModel.COMPLETE_VALUE);
                    for(Sign s : Sign.values())
                        model.addElement(s);
                    break;
                case PINK_CUBE:
                    model.addElement(SplitTableModel.COMPLETE_VALUE);
                    for(PinkCube s : PinkCube.values())
                        model.addElement(s);
                    break;
                case GUN:
                    model.addElement(SplitTableModel.COMPLETE_VALUE);
                    for(Gun g : Gun.values())
                        model.addElement(g);
                    break;
            }
           // model.setSelectedItem(SplitTableModel.COMPLETE_VALUE);
        }
        // finally return the component.
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}

