import src.AuditLog;
import src.Department;

import java.util.ArrayList;
import java.util.List;

interface OrgSubject {
    void subscribe(OrgObserver observer);
    void unsubscribe(OrgObserver observer);
    void notifyObservers(String message);
}

interface OrgObserver {
    void onOrgChange(String message);
}

class OrganizationNotifier implements OrgSubject {
    private List<OrgObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(OrgObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unsubscribe(OrgObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (OrgObserver observer : observers) {
            observer.onOrgChange(message);
        }
    }
}

class CorporateHead implements OrgObserver {
    private String name;
    private List<String> notifications = new ArrayList<>();

    public CorporateHead(String name) {
        this.name = name;
    }

    @Override
    public void onOrgChange(String message) {
        notifications.add(message);
        System.out.println("[CEO - " + name + "] Notification received: " + message);
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public String getName() {
        return name;
    }
}

class HRSystemMain {
    public static void main(String[] args) {

    }

    private static void printReport(Department root, OrgVisitor visitor) {
        root.accept(visitor);
        System.out.println(visitor.getReport());
    }
}
