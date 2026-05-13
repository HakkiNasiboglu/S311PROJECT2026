package src;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuditLog {

    private static AuditLog instance = null;
    private final List<String> logs = new ArrayList<String>();
    private static final Lock lock = new ReentrantLock();

    //If we need to reset AuditLog
    //public static void initInstance() { instance = null;}

    private AuditLog() {}

    //AuditLog singleton structure is thread safe, but
    //we have to make sendLog also thread safe to prevent
    //multi threads send log at the same time
    public synchronized void sendLog(String message) {
        String logEntry = String.format("[%s] %s", getTimestamp(), message);
        logs.add(logEntry);
        System.out.println("An audit log added to the list. " + logEntry);
    }

    public void printLogHistory() {
        System.out.println("-Audit Log History-");
        for(String log : logs){
            System.out.println(log);
        }
    }

    public static AuditLog GetAuditLog() {
        if(instance == null){
            lock.lock();
            try{
                if(instance == null){
                    instance = new AuditLog();
                }
            }finally {lock.unlock();}
        }
        return instance;
    }

    //Returns a new collection instance not return logs
    //to prevent manipulation in the list just send the info in it
    public List<String> getLogs() {return new ArrayList<>(logs);}

    private String getTimestamp(){return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));}

}

