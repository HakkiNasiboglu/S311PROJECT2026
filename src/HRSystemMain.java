//Ataberk Yayla
//Elif Göksu Sümer
//Hakkı Nasiboğlu
//Utku Sertkaya

package src;

import java.util.ArrayList;
import java.util.List;

interface OrgObserver {
    void onOrgChange(String message);
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
        System.out.println("[Corporate Head - " + name + "] Notification received: " + message);
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public String getName() {
        return name;
    }
}

interface OrgSubject {
    void subscribe(OrgObserver observer);

    void unsubscribe(OrgObserver observer);

    void notifyObservers(String message);
}

class OrganizationNotifier implements OrgSubject {
    private List<OrgObserver> observers = new java.util.concurrent.CopyOnWriteArrayList<>();
    private static volatile OrganizationNotifier instance;

    private OrganizationNotifier() {
    }

    public static OrganizationNotifier getNotifier() {
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
            ((java.util.concurrent.CopyOnWriteArrayList<OrgObserver>) observers).addIfAbsent(observer);
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

public class HRSystemMain {
    public static void main(String[] args) {

        CorporateHead ceo = new CorporateHead("Ufuk");
        OrganizationNotifier.getNotifier().subscribe(ceo);

        HRInvoker invoker = new HRInvoker();
        OrgVisitor headcount = new HeadcountVisitor();
        OrgVisitor diversity = new DiversityVisitor();
        OrgVisitor seniority = new SeniorityVisitor();
        OrgVisitor roles = new RoleDistributionVisitor();

        Department pixelAgency = new Department("Pixel Agency");
        Department creativeDept = new Department("Creative Dept");
        Department performanceDept = new Department("Performance Dept");

        Team seoTeam = new Team("SEO Team");
        Team socialMediaTeam = new Team("Social Media Team");
        Team designTeam = new Team("Design Team");
        Team adTeam = new Team("Ad Team");
        Team analyticsTeam = new Team("Analytics Team");

        creativeDept.addElement(seoTeam);
        creativeDept.addElement(socialMediaTeam);
        creativeDept.addElement(designTeam);

        performanceDept.addElement(adTeam);
        performanceDept.addElement(analyticsTeam);

        pixelAgency.addElement(creativeDept);
        pixelAgency.addElement(performanceDept);

        System.out.println("========== HIRING ==========");

        Employee ataberk = new Employee("Ataberk", "SEO Specialist", 25, "Male");
        Employee hakki = new Employee("Hakkı", "SEO Specialist", 22, "Male");
        Employee baris = new Employee("Barış", "SEO Specialist", 2, "Male");

        Employee pelin = new Employee("Pelin", "Social Media Specialist", 5, "Female");
        Employee sude = new Employee("Sude", "Social Media Specialist", 3, "Female");
        Employee mert = new Employee("Mert", "Social Media Specialist", 1, "Male");

        Employee basar = new Employee("Başar", "Web Designer", 8, "Male");
        Employee burak = new Employee("Burak", "Web Designer", 4, "Male");

        Employee elif = new Employee("Elif", "Digital Ad Specialist", 3, "Female");
        Employee utku = new Employee("Utku", "Digital Ad Specialist", 1, "Male");
        Employee tolga = new Employee("Tolga", "Digital Ad Specialist", 6, "Male");

        Employee mehmet = new Employee("Mehmet", "Analytics Specialist", 21, "Male");
        Employee sirac = new Employee("Siraç", "Analytics Specialist", 2, "Male");

        invoker.executeCommand(new HireCommand(seoTeam, ataberk));
        invoker.executeCommand(new HireCommand(seoTeam, hakki));
        invoker.executeCommand(new HireCommand(seoTeam, baris));
        invoker.executeCommand(new HireCommand(socialMediaTeam, pelin));
        invoker.executeCommand(new HireCommand(socialMediaTeam, sude));
        invoker.executeCommand(new HireCommand(socialMediaTeam, mert));
        invoker.executeCommand(new HireCommand(designTeam, basar));
        invoker.executeCommand(new HireCommand(designTeam, burak));
        invoker.executeCommand(new HireCommand(adTeam, elif));
        invoker.executeCommand(new HireCommand(adTeam, utku));
        invoker.executeCommand(new HireCommand(adTeam, tolga));
        invoker.executeCommand(new HireCommand(analyticsTeam, mehmet));
        invoker.executeCommand(new HireCommand(analyticsTeam, sirac));

        System.out.println();
        pixelAgency.display(0);

        System.out.println("\n========== INITIAL REPORTS: Creative Dept ==========");
        printReport(creativeDept, headcount);
        printReport(creativeDept, diversity);
        printReport(creativeDept, seniority);
        printReport(creativeDept, roles);

        System.out.println("\n========== INITIAL REPORTS: Performance Dept ==========");
        printReport(performanceDept, headcount);
        printReport(performanceDept, diversity);
        printReport(performanceDept, seniority);
        printReport(performanceDept, roles);

        System.out.println("\n========== PROMOTIONS ==========");

        invoker.executeCommand(new PromoteCommand(ataberk, "SEO Lead"));
        System.out.println("Ataberk's new position : " + ataberk.getPosition());

        invoker.executeCommand(new PromoteCommand(pelin, "Social Media Lead"));
        System.out.println("Pelin's new position : " + pelin.getPosition());

        invoker.executeCommand(new PromoteCommand(elif, "Digital Ad Lead"));
        System.out.println("Elif's new position : " + elif.getPosition());

        System.out.println("\n-- Undoing Elif's promotion --");
        invoker.undoLastCommand();
        System.out.println("Elif's position after undo : " + elif.getPosition());

        System.out.println("\n-- Re-promoting Elif --");
        invoker.executeCommand(new PromoteCommand(elif, "Digital Ad Lead"));
        System.out.println("Elif's current position : " + elif.getPosition());

        System.out.println("\n========== LAYOFF ==========");
        invoker.executeCommand(new LayoffCommand(utku));

        System.out.println("\n========== REPORTS AFTER PROMOTIONS & LAYOFF ==========");
        printReport(pixelAgency, headcount);
        printReport(pixelAgency, roles);
        printReport(pixelAgency, seniority);

        System.out.println("\n========== DEPARTMENT MERGE ==========");
        Department integratedDept = new Department("Integrated Marketing Dept");
        invoker.executeCommand(new MergeDeptCommand(creativeDept, performanceDept, integratedDept));

        System.out.println("\n-- Pixel Agency after merge --");
        pixelAgency.display(0);

        System.out.println();
        printReport(integratedDept, headcount);
        printReport(integratedDept, diversity);

        System.out.println("\n========== SPLIT BACK ==========");
        invoker.executeCommand(new SplitBackDeptCommand(integratedDept));

        System.out.println("\n-- Pixel Agency after split --");
        pixelAgency.display(0);

        System.out.println("\n========== UNDO SPLIT BACK ==========");
        invoker.undoLastCommand();

        System.out.println("\n-- Pixel Agency after undo (back to merged state) --");
        pixelAgency.display(0);

        System.out.println("\n========== AUDIT LOG ==========");
        AuditLog.getAuditLog().printLogHistory();
    }

    private static void printReport(WorkElement root, OrgVisitor visitor) {
        root.accept(visitor);
        System.out.println(visitor.getReport());
        visitor.reset();
    }
}