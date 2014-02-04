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

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class TriggerTrackerPanel {
    private final TriggerTableModel model;
    private JTable triggerTable;
    private JPanel content;
    private JCheckBox gunsCheckBox;
    private JCheckBox signsCheckBox;
    private JCheckBox pinkCubesCheckBox;
    private JCheckBox mapCheckBox;
    private JCheckBox creditsCheckBox;
    private JScrollPane scroll;

    public TriggerTrackerPanel() {
        TableModelListener l = new TableModelListener() {
            @Override
            public void tableChanged(final TableModelEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        triggerTable.scrollRectToVisible(triggerTable.getCellRect(triggerTable.getRowCount() - 1, 0, true));
                    }
                });
            }
        };
        model = new TriggerTableModel();
        triggerTable.setModel(model);
        triggerTable.getModel().addTableModelListener(l);
        ActionListener onCheckbox = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.filter(gunsCheckBox.isSelected(), signsCheckBox.isSelected(), pinkCubesCheckBox.isSelected(), mapCheckBox.isSelected(), creditsCheckBox.isSelected());
            }
        };
        gunsCheckBox.addActionListener(onCheckbox);
        signsCheckBox.addActionListener(onCheckbox);
        pinkCubesCheckBox.addActionListener(onCheckbox);
        mapCheckBox.addActionListener(onCheckbox);
        creditsCheckBox.addActionListener(onCheckbox);
        onCheckbox.actionPerformed(null);
        scroll.getViewport().setBackground(Color.BLACK);
        triggerTable.getColumnModel().getColumn(0).setMaxWidth(200);
        triggerTable.getColumnModel().getColumn(1).setMaxWidth(200);
    }

    public JPanel getContent() {
        return content;
    }

    public TriggerTableModel getModel() {
        return model;
    }
}
