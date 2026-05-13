package src;

import java.util.ArrayList;
import java.util.List;

public interface WorkElement {
    void addElement(WorkElement element);
    void removeElement(WorkElement element);
    void display(int indent);
    String getName();
    List<WorkElement> getList();
    void setParent(WorkElement parent);
    WorkElement getParent();
    void accept(OrgVisitor visitor);
}
abstract class CompositeElement implements WorkElement {
    protected WorkElement parent;

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitDepartment(this);
        for (WorkElement element : getList()) {
            element.accept(visitor);
        }
    }

    @Override
    public void setParent(WorkElement parent) {this.parent = parent;}

    @Override
    public WorkElement getParent() {return parent;}
}

class Department extends CompositeElement {
    private String departmentName;
    private List<WorkElement> list = new ArrayList<>();
    private List<WorkElement> origins = new ArrayList<>();

    public Department(String name) {
        this.departmentName = name;
    }

    @Override
    public void addElement(WorkElement team) {
        list.add(team);
        team.setParent(this);
    }

    @Override
    public void removeElement(WorkElement team) {
        list.remove(team);
        team.setParent(null);
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "DEPARTMENT: " + this.departmentName.toUpperCase());
        for (WorkElement element : list) {
            element.display(indent + 1);
        }
    }

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitDepartment(this);
        for (WorkElement element : list) {
            element.accept(visitor);
        }
    }

    public void setOrigins(List<WorkElement> originalParts) {this.origins = new ArrayList<>(originalParts);}
    public List<WorkElement> getOrigins() {return origins;}
    @Override public String getName() { return departmentName; }
    @Override public List<WorkElement> getList() { return list; }
}

class Team extends CompositeElement {
    private String teamName;
    private List<WorkElement> list = new ArrayList<>();

    public Team(String name) {
        this.teamName = name;
    }

    @Override
    public void addElement(WorkElement element) {
        list.add(element);
        element.setParent(this);
    }

    @Override
    public void removeElement(WorkElement element) {
        list.remove(element);
        element.setParent(null);
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "Team: " + this.teamName);
        for (WorkElement member : list) {
            member.display(indent + 1);
        }
    }

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitTeam(this);
        for (WorkElement element : list) {
            element.accept(visitor);
        }
    }

    @Override public String getName() { return teamName; }
    @Override public List<WorkElement> getList() { return list; }
}

class Employee implements WorkElement {
    private String employeeName;
    private String position;
    private WorkElement parent;

    public Employee(String name, String position) {
        this.employeeName = name;
        this.position = position;
    }

    @Override
    public void addElement(WorkElement element) {
        System.out.println("Wrong command: Employee cannot have children.");
    }

    @Override
    public void removeElement(WorkElement element) {
        System.out.println("Wrong command: Employee has no children to remove.");
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "Employee: " + this.employeeName + " [" + this.position + "]");
    }

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitEmployee(this);
    }

    @Override public String getName() { return employeeName; }
    @Override public List<WorkElement> getList() { return null; }
    @Override public void setParent(WorkElement parent) { this.parent = parent; }
    @Override public WorkElement getParent() { return parent; }
}