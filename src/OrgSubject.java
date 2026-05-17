package src;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

interface OrgSubject {
    void subscribe(OrgObserver observer);
    void unsubscribe(OrgObserver observer);
    void notifyObservers(String message);
}

class OrganizationNotifier implements OrgSubject {
    private List<OrgObserver> observers = new CopyOnWriteArrayList<>();
    private static volatile OrganizationNotifier instance;

    private OrganizationNotifier() {}

    public static OrganizationNotifier getInstance() {
        if (instance == null) {
            synchronized (OrganizationNotifier.class) {
                if (instance == null) {
                    instance = new OrganizationNotifier();
                }
            }
        }
        return instance;
    }

    @Override
    public void subscribe(OrgObserver observer) {
        if (observer != null) {
            ((CopyOnWriteArrayList<OrgObserver>) observers).addIfAbsent(observer);
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