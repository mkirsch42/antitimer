package fr.petitl.antichamber.log;

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
    private static TreeMap<String, LogLevel> levels = new TreeMap<String, LogLevel>();
    private static LogLevel defaultLevel = LogLevel.INFO;

    static {
        // init levels goes here
        //levels.put("fr.petitl.antichamber.triggers.logger", LogLevel.DEBUG);
    }

    private final String simpleName;
    private final LogLevel level;

    public Logger(String simpleName, LogLevel level) {
        this.simpleName = simpleName;
        this.level = level;
    }

    public static Logger getLogger(Class z) {
        String name = z.getName();
        Map.Entry<String, LogLevel> potentialLevel = levels.floorEntry(name);
        LogLevel level = defaultLevel;
        if (potentialLevel != null && name.startsWith(potentialLevel.getKey()))
            level = potentialLevel.getValue();

        return new Logger(z.getSimpleName(), level);
    }

    private PrintStream out = System.out;

    private void print(String message, LogLevel l) {
        if (l.compareTo(level) <= 0)
            out.println("[" + sdf.format(new Date(System.currentTimeMillis())) + "] [" + l.name() + "] [" + simpleName + "] " + message);
    }

    private void printStackTrace(Exception e) {
        e.printStackTrace(out);
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
}
