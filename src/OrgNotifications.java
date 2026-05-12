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

        Department company   = new Department("Global Tech");
        Department engineering = new Department("Engineering");
        Department sales       = new Department("Sales");
        Department operations  = new Department("Operations");

        Team backendTeam  = new Team("Backend Team");
        Team frontendTeam = new Team("Frontend Team");
        Team salesTeam    = new Team("Sales Team");
        Team opsTeam      = new Team("Ops Team");

        company.addElement(engineering);
        company.addElement(sales);
        company.addElement(operations);

        engineering.addElement(backendTeam);
        engineering.addElement(frontendTeam);
        sales.addElement(salesTeam);
        operations.addElement(opsTeam);
        
        Employee alice = new Employee("Alice Carter",  "Software Engineer",   "Female", 6);
        Employee emma  = new Employee("Emma Brooks",   "Principal Engineer",  "Female", 21);
        Employee brian = new Employee("Brian Stone",   "Sales Associate",     "Male",   4);
        Employee clara = new Employee("Clara Reed",    "Operations Manager",  "Female", 22);
        Employee david = new Employee("David Lane",    "Operations Analyst",  "Male",   2);

        OrganizationNotifier notifier = new OrganizationNotifier();
        CorporateHead ceo = new CorporateHead("Morgan Blake");
        notifier.subscribe(ceo);

        HRInvoker invoker = new HRInvoker();
        invoker.executeCommand(new HireCommand(backendTeam,  alice));
        invoker.executeCommand(new HireCommand(frontendTeam, emma));
        invoker.executeCommand(new HireCommand(salesTeam,    brian));
        invoker.executeCommand(new HireCommand(opsTeam,      clara));
        invoker.executeCommand(new HireCommand(opsTeam,      david));

        Employee aliceSenior = new Employee("Alice Carter", "Senior Software Engineer", "Female", 6);
        invoker.executeCommand(new PromoteCommand(backendTeam, alice, aliceSenior));

        System.out.println("\n--- Initial Organization Structure ---");
        company.display(0);

        System.out.println();
        invoker.executeCommand(new MergeDeptCommand(company, sales, engineering, notifier));

        System.out.println("\n--- Organization After Merge ---");
        company.display(0);

        System.out.println("\n--- Undoing Last Command (Merge) ---");
        invoker.undoLastCommand();

        System.out.println("\n--- Organization After Undo ---");
        company.display(0);

        System.out.println();
        printReport(company, new HeadcountVisitor());

        System.out.println();
        printReport(company, new DiversityVisitor());

        System.out.println();
        printReport(company, new SeniorityVisitor());

        System.out.println();
        printReport(company, new RoleDistributionVisitor());

        System.out.println();
        AuditLog.GetAuditLog().printLogHistory();
    }

    private static void printReport(Department root, OrgVisitor visitor) {
        root.accept(visitor);
        System.out.println(visitor.getReport());
    }
}
