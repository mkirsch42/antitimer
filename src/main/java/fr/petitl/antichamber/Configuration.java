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

package fr.petitl.antichamber;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import fr.petitl.antichamber.timer.TimerControl;
import fr.petitl.antichamber.timer.TimerFactory;

/**
 *
 */
public class Configuration implements Serializable {
    private static final transient File CFG_FILE = new File("antitimer.cfg");
    private static final long serialVersionUID = -8314817758671312803L;

    private String antichamberPath;
    private String timer = "notimer";
    private Map<String, Point> windowLocations = new HashMap<>();
    private Map<String, Dimension> windowSize = new HashMap<>();

    public String getAntichamberPath() {
        return antichamberPath;
    }

    public void setAntichamberPath(String antichamberPath) {
        this.antichamberPath = antichamberPath;
        write();
    }

    public File getAntichamberExe() {
        return new File(antichamberPath + "/Binaries/Win32/UDK.exe");
    }

    public File getAntichamberSave() {
        return new File(antichamberPath + "/Binaries/Win32/SavedGame.bin");
    }

    public File getAntichamberLog() {
        return new File(antichamberPath + "/UDKGame/Logs/Launch.log");
    }

    public Point getWindowLocation(String window) {
        Point point = windowLocations.get(window);
        if(point == null)
            return new Point(0,0);
        return point;
    }

    public void setWindowLocation(String window, Point location) {
        windowLocations.put(window, location);
        write();
    }

    public Dimension getWindowSize(String window) {
        return windowSize.get(window);
    }

    public void setWindowSize(String window, Dimension dimension) {
        windowSize.put(window, dimension);
        write();
    }
    
    public TimerControl getTimer() {
	return TimerFactory.fromString(timer);
    }
    
    public void setTimer(TimerControl t) {
	this.timer = t.toString();
	write();
    }

    public void write() {
        Configuration.write(this);
    }

    public static void write(Configuration cfg) {
        try {
            //noinspection ResultOfMethodCallIgnored
            CFG_FILE.createNewFile();

            try(FileOutputStream out = new FileOutputStream(CFG_FILE, false)) {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(cfg);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not write configuration: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Configuration read() {
        if (!CFG_FILE.exists())
            return new Configuration();

        try(FileInputStream in = new FileInputStream(CFG_FILE)) {
            ObjectInputStream ois = new ObjectInputStream(in);
            return (Configuration) ois.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not read configuration: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new Configuration();
        }
    }
}
