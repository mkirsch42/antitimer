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

package fr.petitl.antichamber.log;

import fr.petitl.antichamber.triggers.watchers.FileReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class Logger {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static TreeMap<String, LogLevel> levels = new TreeMap<String, LogLevel>();
    private static LogLevel defaultLevel = LogLevel.INFO;
    private static LogLevel fileDefaultLevel = LogLevel.DEBUG;

    static {
        // init levels goes here
        //levels.put("fr.petitl.antichamber.triggers.logger", LogLevel.DEBUG);
        File file = new File("debug.log");
        try {
            file.createNewFile();
            fileOut = new PrintStream(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private final String simpleName;
    private LogLevel level;
    private LogLevel fileLevel;

    public Logger(String simpleName, LogLevel level, LogLevel fileLevel) {
        this.simpleName = simpleName;
        this.level = level;
        this.fileLevel = fileLevel;
    }

    public static Logger getLogger(Class z) {
        String name = z.getName();
        Map.Entry<String, LogLevel> potentialLevel = levels.floorEntry(name);
        LogLevel level = defaultLevel;
        if (potentialLevel != null && name.startsWith(potentialLevel.getKey()))
            level = potentialLevel.getValue();
        LogLevel fileLevel = fileDefaultLevel;
        if (potentialLevel != null && name.startsWith(potentialLevel.getKey()))
            fileLevel = potentialLevel.getValue();


        return new Logger(z.getSimpleName(), level, fileLevel);
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    private static final PrintStream out = System.out;
    private static final PrintStream fileOut;

    private void print(String message, LogLevel l) {
        String msg;
        synchronized(out) {
            msg = "[" + sdf.format(new Date(System.currentTimeMillis())) + "] [" + l.name() + "] [" + simpleName + "] " + message;
            if (l.compareTo(level) <= 0) {
                out.println(msg);
                out.flush();
            }
        }
        synchronized(fileOut) {
            if (l.compareTo(fileLevel) <= 0) {
                fileOut.println(msg);
                fileOut.flush();
            }
        }
    }

    private void printStackTrace(Exception e) {
        e.printStackTrace(out);
    }

    public void error(String message) {
        print(message, LogLevel.ERROR);
    }

    public void error(String message, Exception e) {
        print(message, LogLevel.ERROR);
        if (e != null)
            printStackTrace(e);
    }

    public void warn(String message, Exception e) {
        print(message, LogLevel.WARN);
        if (e != null)
            printStackTrace(e);
    }

    public void info(String message) {
        print(message, LogLevel.INFO);
    }

    public void debug(String message) {
        print(message, LogLevel.DEBUG);
    }

    public void trace(String message) {
        print(message, LogLevel.TRACE);
    }

    public boolean isDebugEnabled() {
        return (LogLevel.DEBUG.compareTo(level) <= 0);
    }
}
