package fr.petitl.antichamber.gui.panels;

import fr.petitl.antichamber.gui.panels.ButtonColumn;
import fr.petitl.antichamber.gui.panels.CustomComboBoxEditor;
import fr.petitl.antichamber.gui.panels.SplitTableModel;
import fr.petitl.antichamber.gui.panels.TableCellRendererAsEditor;
import fr.petitl.antichamber.llanfair.LlanfairControl;
import fr.petitl.antichamber.triggers.GameStatus;
import fr.petitl.antichamber.triggers.TriggerInfo;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

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
        // assume JTable is named "table"
        /*TableButton buttonEditor = new TableButton("x");
        buttonEditor.addTableButtonListener(new TableButtonListener() {
            @Override
            public void tableButtonClicked(final int row, int col) {
            }
        });
        TableColumn col = table.getColumnModel().getColumn(2);
        col.setCellRenderer(buttonEditor);
        col.setCellEditor(buttonEditor);  */
    }

    private void createUIComponents() {
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
}
