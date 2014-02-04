/*
 * Copyright 2014 Loic Petit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.petitl.antichamber.gui.panels;

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

