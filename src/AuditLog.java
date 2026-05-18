package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Singleton Pattern - AuditLog
public class AuditLog {

    private static volatile AuditLog instance = null;
    private final List<String> logs = new ArrayList<>();

    private AuditLog() {}

    public void printLogHistory() {
        System.out.println("-Audit Log History-");
        for (String log : getLogs()) {
            System.out.println(log);
        }
    }

    //Double-Checking Locking
    public static AuditLog getAuditLog() {
        if (instance == null) {
            synchronized (AuditLog.class) {
                if (instance == null) instance = new AuditLog();
            }
        }
        return instance;
    }

    public synchronized void sendLog(String message) {
        String logEntry = String.format("[%s] %s", getTimestamp(), message);
        logs.add(logEntry);
    }

    public synchronized List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
