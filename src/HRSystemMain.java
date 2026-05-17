package src;

import java.util.ArrayList;
import java.util.List;

interface OrgObserver {
    void onOrgChange(String message);
}

class CorporateHead implements OrgObserver {
    private String name;
    private List<String> notifications = new ArrayList<>();

    public CorporateHead(String name) {this.name = name;}

    @Override
    public void onOrgChange(String message) {
        notifications.add(message);
        System.out.println("[Corporate Head - " + name + "] Notification received: " + message);
    }

    public List<String> getNotifications() {return new ArrayList<>(notifications);}

    public String getName() {return name;}
}

public class HRSystemMain {
    public static void main(String[] args) {
        CorporateHead ceo = new CorporateHead("Utku");
        OrganizationNotifier.getInstance().subscribe(ceo);

        HRInvoker invoker = new HRInvoker();
        OrgVisitor visitor = new DiversityVisitor();
        Department bigDept = new Department("BigDept");
        Department techDept = new Department("Tech Dept");
        Department cleanDept = new Department("Cleaning Dept");

        bigDept.addElement(techDept);
        bigDept.addElement(cleanDept);

        Employee dev1 = new Employee("Alice", "Developer", 3, "Female");
        Employee dev2 = new Employee("a", "Developer", 1, "Female");
        Employee dev3 = new Employee("n", "Developer", 21, "Female");
        Employee dev4 = new Employee("Ac", "Developer", 20, "Male");

        HRCommand hire1 = new HireCommand(bigDept, dev1);
        HRCommand hire2 = new HireCommand(techDept, dev2);
        HRCommand hire3 = new HireCommand(cleanDept, dev3);
        HRCommand hire4 = new HireCommand(techDept, dev4);
        invoker.executeCommand(hire1);
        invoker.executeCommand(hire2);
        invoker.executeCommand(hire3);
        invoker.executeCommand(hire4);

        printReport(techDept, visitor);
        printReport(cleanDept, visitor);
        printReport(bigDept, visitor);
        Department mergedDept = new Department("mergedDept");
        HRCommand merge = new MergeDeptCommand(techDept, cleanDept, mergedDept);
        invoker.executeCommand(merge);

        printReport(bigDept, visitor);
        printReport(mergedDept, visitor);
        printReport(bigDept.getChild().get(1), visitor);

        HRCommand split = new SplitBackDeptCommand(mergedDept);
        invoker.executeCommand(split);

        printReport(mergedDept, visitor);
        printReport(bigDept.getChild().get(1), visitor);
    }

    private static void printReport(WorkElement root, OrgVisitor visitor) {
        root.accept(visitor);
        System.out.println(visitor.getReport());
        visitor.reset();
    }
}
