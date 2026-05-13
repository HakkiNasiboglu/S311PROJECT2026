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

        OrganizationNotifier notifier = new OrganizationNotifier();
        CorporateHead ceo = new CorporateHead("Alice");
        notifier.subscribe(ceo);

        Department hrDept = new Department("HR");
        Department itDept = new Department("IT");

        Team team1 = new Team("HR Team");
        Team team2 = new Team("IT Team");
        Team team3 = new Team(" IT Team 2");

        hrDept.addElement(team1);
        itDept.addElement(team2);
        itDept.addElement(team3);

        Employee e1 = new Employee("Ali", "Manager", 3, "Male");
        Employee e2 = new Employee("Sara", "Developer", 6, "Female");
        Employee e3 = new Employee("John", "Developer", 22, "Male");
        Employee e4 = new Employee("Lina", "Designer", 28, "Female");
        Employee e5 = new Employee("Johanna", "Designer", 4, "Female");

        HRInvoker invoker = new HRInvoker();

        invoker.executeCommand(new HireCommand(team1, e1));
        invoker.executeCommand(new HireCommand(team2, e2));
        invoker.executeCommand(new HireCommand(team2, e3));
        invoker.executeCommand(new HireCommand(team3, e4));
        invoker.executeCommand(new HireCommand(team3, e5));

        notifier.notifyObservers("Employees hired.");

        System.out.println("\n--- IT DEPARTMENT ---");
        itDept.display(0);

        System.out.println("\n--- HR DEPARTMENT ---");
        hrDept.display(0);

        invoker.executeCommand(new PromoteCommand(e5, "Senior Designer"));
        notifier.notifyObservers("Promotion happened.");

        MergeDeptCommand merge =
                new MergeDeptCommand(itDept, hrDept, "Tech+HR");

        if (merge.validate()) {
            merge.execute();
            notifier.notifyObservers("Departments merged.");

            WorkElement merged = merge.getMergedDepartment();

            System.out.println("\n--- MERGED DEPARTMENT ---");
            merged.display(0);

            merge.undo();
            notifier.notifyObservers("Merge undone.");

            System.out.println("\n--- AFTER UNDO ---");
            itDept.display(0);
            hrDept.display(0);
        }

        System.out.println("\n--- REPORTS (IT) ---");
        printReport(itDept, new HeadcountVisitor());
        printReport(itDept, new DiversityVisitor());
        printReport(itDept, new SeniorityVisitor());
        printReport(itDept, new RoleDistributionVisitor());

        System.out.println("\n--- REPORTS (HR) ---");
        printReport(hrDept, new HeadcountVisitor());
        printReport(hrDept, new DiversityVisitor());
        printReport(hrDept, new SeniorityVisitor());
        printReport(hrDept, new RoleDistributionVisitor());

        System.out.println("\n--- AUDIT LOG ---");
        AuditLog.GetAuditLog().printLogHistory();

        System.out.println("\n--- CEO NOTIFICATIONS ---");
        for (String note : ceo.getNotifications()) {
            System.out.println(note);
        }



    }

    private static void printReport(Department root, OrgVisitor visitor) {
        root.accept(visitor);
        System.out.println(visitor.getReport());
    }
}
