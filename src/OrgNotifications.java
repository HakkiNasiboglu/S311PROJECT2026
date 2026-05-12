import java.util.ArrayList;

interface OrgSubject {
    void subscribe(OrgObserver observer);

    void unsubscribe(OrgObserver observer);

    void notifyObservers(String message);
}

interface OrgObserver {
    void onOrgChange(String message);
}

class OrganizationNotifier implements OrgSubject {
    private ArrayList<OrgObserver> observers = new ArrayList<>();

    public void subscribe(OrgObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(OrgObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (OrgObserver observer : observers) {
            observer.onOrgChange(message);
        }
    }
}

class CorporateHead implements OrgObserver {
    private String name;
    private ArrayList<String> notifications = new ArrayList<>();

    public CorporateHead(String name) {
        this.name = name;
    }

    public void onOrgChange(String message) {
        notifications.add(message);
        System.out.println("[CEO - " + name + "] Notification: " + message);
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public String getName() {
        return name;
    }
}

class HRSystemMain {
    public static void main(String[] args) {
        Department company = new Department("Global Tech");
        Department engineering = new Department("Engineering");
        Department sales = new Department("Sales");
        Department operations = new Department("Operations");

        company.add(engineering);
        company.add(sales);
        company.add(operations);

        Employee alice = new Employee("Alice Carter", "Female", 6, "Software Engineer");
        Employee brian = new Employee("Brian Stone", "Male", 4, "Sales Associate");
        Employee clara = new Employee("Clara Reed", "Female", 22, "Operations Manager");
        Employee david = new Employee("David Lane", "Male", 2, "Operations Specialist");
        Employee emma = new Employee("Emma Brooks", "Female", 21, "Principal Engineer");

        OrganizationNotifier notifier = new OrganizationNotifier();
        CorporateHead ceo = new CorporateHead("Morgan Blake");
        notifier.subscribe(ceo);

        HRInvoker invoker = new HRInvoker();
        invoker.executeCommand(new HireCommand(engineering, alice));
        invoker.executeCommand(new HireCommand(engineering, emma));
        invoker.executeCommand(new HireCommand(sales, brian));
        invoker.executeCommand(new HireCommand(operations, clara));
        invoker.executeCommand(new HireCommand(operations, david));
        invoker.executeCommand(new PromoteCommand(alice, "Senior Software Engineer"));

        System.out.println();
        System.out.println("Initial Organization Structure:");
        company.display(0);

        System.out.println();
        invoker.executeCommand(new MergeDeptCommand(sales, engineering, company, notifier));

        System.out.println();
        invoker.executeCommand(new SplitDeptCommand(engineering, company, notifier));

        System.out.println();
        System.out.println("Final Organization Structure:");
        company.display(0);

        System.out.println();
        printVisitorReport(company, new HeadcountVisitor());

        System.out.println();
        printVisitorReport(company, new DiversityVisitor());

        System.out.println();
        printVisitorReport(company, new SeniorityVisitor());

        System.out.println();
        printVisitorReport(company, new RoleDistributionVisitor());

        System.out.println();
        invoker.printHistory();

        System.out.println();
        AuditLog.getInstance().printAllLogs();
    }

    private static void printVisitorReport(Department rootDepartment, OrgVisitor visitor) {
        rootDepartment.accept(visitor);
        System.out.println(visitor.getReport());
    }
}
