package fr.petitl.antichamber.gui;

import fr.petitl.antichamber.triggers.TriggerInfo;
import fr.petitl.antichamber.triggers.TriggerType;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class SplitTableModel extends AbstractTableModel {
    public static String COMPLETE_VALUE = "Complete";
    private List<TriggerInfo> triggers = new LinkedList<>();

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Trigger Type";
            case 1:
                return "Objective";
            case 2:
                return "";
            case 3:
                return "";
            case 4:
                return "";
        }
        throw new IllegalArgumentException("Unknown column index " + column);
    }


    @Override
    public int getRowCount() {
        return triggers.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    public List<TriggerInfo> getTriggers() {
        return triggers;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o = doGet(rowIndex, columnIndex);
        return o;
    }

    private Object doGet(int rowIndex, int columnIndex) {
        TriggerInfo trigger = triggers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return trigger.getType();
            case 1:
                return trigger.mustBeComplete() ? COMPLETE_VALUE : trigger.getArgumentToMatch();
            case 2:
                return "x";
            case 3:
                return "^";
            case 4:
                return "v";
        }
        throw new IllegalArgumentException("Unknown column index " + columnIndex);
    }

    public void add(TriggerInfo t) {
        int rowCount = getRowCount();
        triggers.add(t);
        fireTableRowsInserted(rowCount, rowCount);
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, final int row, int col) {
        TriggerInfo trigger = triggers.get(row);
        switch (col) {
            case 0:
                trigger.setType((TriggerType) value);
                trigger.setMustBeComplete(true);
                break;
            case 1:
                if (COMPLETE_VALUE.equals(value))
                    trigger.setMustBeComplete(true);
                else {
                    trigger.setMustBeComplete(false);
                    trigger.setArgumentToMatch(value);
                }
                break;
        }
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return TriggerType.class;
            case 1:
                return Object.class;
            default:
                return String.class;
        }
    }

    public void remove(int selectedRow) {
        triggers.remove(selectedRow);
        fireTableRowsDeleted(selectedRow, selectedRow);
    }

    public void shiftUp(int selectedRow) {
        if(selectedRow == 0) {
            return;
        }
        TriggerInfo elt = triggers.remove(selectedRow);
        triggers.add(selectedRow-1, elt);
        fireTableRowsUpdated(selectedRow-1, selectedRow);
    }

    public void shiftDown(int selectedRow) {
        if(selectedRow == triggers.size()-1)
            return;
        TriggerInfo elt = triggers.remove(selectedRow);
        triggers.add(selectedRow+1, elt);
        fireTableRowsUpdated(selectedRow, selectedRow+1);
    }
}
