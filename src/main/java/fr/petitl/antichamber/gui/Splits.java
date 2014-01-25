package fr.petitl.antichamber.gui;

import fr.petitl.antichamber.llanfair.LlanfairControl;
import fr.petitl.antichamber.triggers.TriggerInfo;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.*;
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
    private JButton saveButton;
    private JButton loadButton;
    private JTextField titleTextField;
    private JTable completionTable;
    private JButton addSplitButton;
    private JButton addCompletionConditionButton;
    private JButton initLlanfairButton;
    private SplitTableModel completionModel = new SplitTableModel();
    private SplitTableModel splitModel = new SplitTableModel();
    private final JFrame frame;

    private File saveFile;

    public Splits(final LlanfairControl control) {
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
        initLlanfairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                control.buildAndInjectRun(titleTextField.getText(), splitModel.getTriggers());
            }
        });

        initTable(triggerEdit, valueEditor, splitTable);
        initTable(triggerEdit, valueEditor, completionTable);

        frame = new JFrame("Antitimer");
        frame.setContentPane(content);
        frame.setSize(500, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveFile == null) {
                    JFileChooser fc = new JFileChooser();
                    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fc.setDialogType(JFileChooser.SAVE_DIALOG);
                    if (fc.showDialog(frame, "Save") != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    saveFile = fc.getSelectedFile();
                    try {
                        saveFile.createNewFile();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(frame, "Cannot create file " + saveFile.getName() + "\n" + e1.getMessage());
                        return;
                    }
                }
                try (FileOutputStream fos = new FileOutputStream(saveFile, false)) {
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(titleTextField.getText());
                    oos.writeObject(splitModel.getTriggers());
                    oos.writeObject(completionModel.getTriggers());
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(frame, "Cannot write to " + saveFile.getName() + "\n" + e1.getMessage());
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setDialogType(JFileChooser.OPEN_DIALOG);
                if (fc.showDialog(frame, "Open") != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                saveFile = fc.getSelectedFile();
                try {
                    FileInputStream fis = new FileInputStream(saveFile);
                    ObjectInputStream oos = new ObjectInputStream(fis);
                    titleTextField.setText((String) oos.readObject());
                    splitModel.setTriggers((List<TriggerInfo>) oos.readObject());
                    completionModel.setTriggers((List<TriggerInfo>) oos.readObject());
                } catch (IOException | ClassNotFoundException e1) {
                    JOptionPane.showMessageDialog(frame, "Cannot read " + saveFile.getName() + "\n" + e1.getMessage());
                }
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

    public JFrame getFrame() {
        return frame;
    }

    public SplitTableModel getCompletionModel() {
        return completionModel;
    }

    public SplitTableModel getSplitModel() {
        return splitModel;
    }
}
