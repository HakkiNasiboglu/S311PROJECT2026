package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuditLog {

    private static volatile AuditLog instance = null;
    private final List<String> logs = new ArrayList<>();
    private static final Lock lock = new ReentrantLock();

    //If we need to reset AuditLog
    //public static void initInstance() { instance = null;}

    private AuditLog() {}

    public void printLogHistory() {
        System.out.println("-Audit Log History-");
        for(String log : getLogs()){
            System.out.println(log);
        }
    }

    public static AuditLog getAuditLog() {
        if(instance == null){
            lock.lock();
            try{
                if(instance == null) instance = new AuditLog();
            }finally {lock.unlock();}
        }
        return instance;
    }

    public synchronized void sendLog(String message) {
        String logEntry = String.format("[%s] %s", getTimestamp(), message);
        logs.add(logEntry);
        System.out.println("An audit log added to the list. " + logEntry);
    }
    public synchronized List<String> getLogs() {return new ArrayList<>(logs);}

    private String getTimestamp(){return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));}

}

