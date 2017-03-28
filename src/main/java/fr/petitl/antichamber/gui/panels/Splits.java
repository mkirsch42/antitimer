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

import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerInfo;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 */
public class Splits {
    private JTable splitTable;
    private JPanel content;
    private JTextField titleTextField;
    private JTable completionTable;
    private JButton addSplitButton;
    private JButton addCompletionConditionButton;
    private SplitTableModel completionModel = new SplitTableModel();
    private SplitTableModel splitModel = new SplitTableModel();

    private File saveFile;

    public Splits(final GameStatus status) {
        completionModel = new SplitTableModel();
        completionTable.setModel(completionModel);
        splitModel = new SplitTableModel();
        splitTable.setModel(splitModel);
        addSplitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splitModel.add(new TriggerInfo(TriggerType.GUN, null, true));
            }
        });
        addCompletionConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                completionModel.add(new TriggerInfo(TriggerType.GUN, null, true));
            }
        });

        JComboBox<TriggerType> comboBox = new JComboBox<>();
        for (TriggerType triggerType : TriggerType.values()) {
            if (triggerType.isDisplayable())
                comboBox.addItem(triggerType);
        }
        DefaultCellEditor triggerEdit = new DefaultCellEditor(comboBox);
        CustomComboBoxEditor valueEditor = new CustomComboBoxEditor();
        initTable(triggerEdit, valueEditor, splitTable);
        initTable(triggerEdit, valueEditor, completionTable);


        if (status != null) {
            splitModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    status.setSplits(splitModel.getTriggers());
                }
            });
            completionModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    status.setEndingConditions(completionModel.getTriggers());
                }
            });
        }
    }

    private void initTable(DefaultCellEditor triggerEdit, CustomComboBoxEditor valueEditor, final JTable table) {
        table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(0).setCellEditor(triggerEdit);
        table.getColumnModel().getColumn(0).setCellRenderer(new TableCellRendererAsEditor());
        table.getColumnModel().getColumn(1).setCellEditor(valueEditor);
        table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRendererAsEditor());
        table.setRowHeight(26);
        new ButtonColumn(table, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((SplitTableModel) table.getModel()).remove(modelRow);
            }
        }, 2);
        new ButtonColumn(table, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((SplitTableModel) table.getModel()).shiftUp(modelRow);
            }
        }, 3);
        new ButtonColumn(table, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((SplitTableModel) table.getModel()).shiftDown(modelRow);
            }
        }, 4);
    }

    public SplitTableModel getCompletionModel() {
        return completionModel;
    }

    public SplitTableModel getSplitModel() {
        return splitModel;
    }

    public JPanel getContent() {
        return content;
    }

    public JTextField getTitleTextField() {
        return titleTextField;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        content = new JPanel();
        content.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        content.add(panel1, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, BorderLayout.EAST);
        final JLabel label1 = new JLabel();
        label1.setText("Splits");
        panel2.add(label1);
        addSplitButton = new JButton();
        addSplitButton.setText("+");
        panel2.add(addSplitButton);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        panel1.add(panel3, BorderLayout.CENTER);
        titleTextField = new JTextField();
        titleTextField.setMargin(new Insets(0, 10, 0, 10));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        panel3.add(titleTextField, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        content.add(panel4, BorderLayout.SOUTH);
        completionTable = new JTable();
        panel4.add(completionTable, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel4.add(panel5, BorderLayout.NORTH);
        final JLabel label2 = new JLabel();
        label2.setText("Completion conditions");
        panel5.add(label2);
        addCompletionConditionButton = new JButton();
        addCompletionConditionButton.setText("+");
        panel5.add(addCompletionConditionButton);
        final JScrollPane scrollPane1 = new JScrollPane();
        content.add(scrollPane1, BorderLayout.CENTER);
        splitTable = new JTable();
        scrollPane1.setViewportView(splitTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return content;
    }
}
