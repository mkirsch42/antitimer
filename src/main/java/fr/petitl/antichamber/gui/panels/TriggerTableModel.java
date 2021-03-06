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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import fr.petitl.antichamber.triggers.DetectedTriggerInfo;

/**
 *
 */
public class TriggerTableModel extends AbstractTableModel {

    private boolean guns;
    private boolean signs;
    private boolean pinks;
    private boolean map;
    private boolean credits;
    private List<DetectedTriggerInfo> detectedTriggerInfos = new ArrayList<>();

    public void filter(boolean guns, boolean signs, boolean pinks, boolean map, boolean credits) {
        this.guns = guns;
        this.signs = signs;
        this.pinks = pinks;
        this.map = map;
        this.credits = credits;
        setTriggers(detectedTriggerInfos);
    }
    
    public void save() {
    	JFileChooser fc = new JFileChooser();
	    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV (Comma delimited) (*.csv)", "csv"));
	    fc.setFileFilter(fc.getChoosableFileFilters()[1]);
	    if(fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	String fileName = fc.getSelectedFile().getAbsolutePath();
	    	if(!fileName.endsWith(".csv")) {
	    		fileName += ".csv";
	    	}
	    	try (CSVPrinter printer = new CSVPrinter(new FileWriter(new File(fileName)), CSVFormat.DEFAULT)) {
				String[][] tbl = toTable();
				for(String[] row : tbl) {
					printer.printRecord(row);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
    
    private String[][] toTable() {
		Stream<String[]> head = Stream.of(IntStream.range(0, getColumnCount()).mapToObj(c -> getColumnName(c)).toArray(String[]::new), null).limit(1);
		Stream<String[]> data = IntStream.range(0, getRowCount()).mapToObj(r -> IntStream.range(0, getColumnCount())
				.mapToObj(c -> getValueAt(r, c).toString().trim()).toArray(String[]::new));
		return Stream.concat(head, data).toArray(String[][]::new);
    }

    public TriggerTableModel() {
    }

    private List<DetectedTriggerInfo> triggers = new LinkedList<>();
    private long startTimestamp;

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Time";
            case 1:
                return "Type";
            case 2:
                return "Count";
            case 3:
                return "Detail";
        }
        throw new IllegalArgumentException("Unknown column index " + column);
    }


    @Override
    public int getRowCount() {
        return triggers.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            DetectedTriggerInfo trigger = triggers.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return " " + sdf.format(new Date(trigger.getTimestamp() - startTimestamp)) + " ";
                case 1:
                    return " " + trigger.getTriggerInfo().getType().toString() + " ";
                case 2:
                    return " " + trigger.getCount() + " ";
                case 3:
                    return " " + (trigger.getTriggerInfo().mustBeComplete() ? "Complete!" : trigger.getTriggerInfo().getArgumentToMatch()) + " ";
            }
            throw new IllegalArgumentException("Unknown column index " + columnIndex);
        } catch (Exception e) {
            return "";
        }
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");

    public void setTriggers(List<DetectedTriggerInfo> detectedTriggerInfos) {
        this.detectedTriggerInfos = detectedTriggerInfos;
        this.triggers.clear();
        for (DetectedTriggerInfo detectedTriggerInfo : detectedTriggerInfos) {
            switch (detectedTriggerInfo.getTriggerInfo().getType()) {
                case GUN:
                    if (guns)
                        triggers.add(0, detectedTriggerInfo);
                    break;
                case SIGN:
                    if (signs)
                        triggers.add(0, detectedTriggerInfo);
                    break;
                case PINK_CUBE:
                    if (pinks)
                        triggers.add(0, detectedTriggerInfo);
                    break;
                case MAP_UPDATE:
                    if (map)
                        triggers.add(0, detectedTriggerInfo);
                    break;
                case FINISH_GAME:
                    if (credits)
                        triggers.add(0, detectedTriggerInfo);
                    break;
            }
        }
        fireTableDataChanged();
    }

    public void setStart(long start) {
        this.startTimestamp = start;
    }
}
