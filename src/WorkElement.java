package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class WorkElement {
    protected String name;
    protected WorkElement parent;

    public WorkElement(String name) {this.name = name;}

    public String getName() {return name;}

    public void setParent(WorkElement parent) {this.parent = parent;}
    public WorkElement getParent() {return parent;}

    //for leafs. composite abstract class will override them
    public void addElement(WorkElement element) {
        throw new UnsupportedOperationException(" Unsupported command for " + this.name );
    }
    public void removeElement(WorkElement element) {
        throw new UnsupportedOperationException(" Unsupported command for " + this.name);
    }
    public List<WorkElement> getChild() {return Collections.emptyList();}

    public abstract void display(int indent);
    public abstract void accept(OrgVisitor visitor);
}

// COMPOSITE ELEMENT (ABSTRACT)
abstract class CompositeElement extends WorkElement {
    protected List<WorkElement> children = new ArrayList<>();

    public CompositeElement(String name) {super(name);}

    @Override
    public void addElement(WorkElement element) {
        children.add(element);
        element.setParent(this);
    }

    @Override
    public void removeElement(WorkElement element) {
        children.remove(element);
        element.setParent(null);
    }

    @Override
    public List<WorkElement> getChild() {return children;}
}

// ELEMENTS
class Department extends CompositeElement {

    public Department(String name) {super(name);}

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "DEPARTMENT: " + getName().toUpperCase());
        for (WorkElement element : children) element.display(indent + 1);
    }

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitDepartment(this);
        for (WorkElement element : children) element.accept(visitor);
    }
}

// COMPOSITE: Team
class Team extends CompositeElement {

    public Team(String name) {super(name);}

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "Team: " + getName());
        for (WorkElement member : children) member.display(indent + 1);
    }

    @Override
    public void accept(OrgVisitor visitor) {
        visitor.visitTeam(this);
        for (WorkElement element : children) element.accept(visitor);
    }
}

// LEAF: Employee
class Employee extends WorkElement {
    private String position;
    private int yearsOfService;
    private String gender;

    public Employee(String name, String position, int yearsOfService, String gender) {
        super(name);
        this.position = position;
        this.yearsOfService = yearsOfService;
        this.gender = gender;
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);
        System.out.println(indentation + "Employee: " + getName() + " [" + this.position + "]");
    }

    @Override
    public void accept(OrgVisitor visitor) {visitor.visitEmployee(this);}

    public int getYearsOfService() { return yearsOfService;}
    public String getGender() { return gender;}
    public String getPosition() { return position;}
    public void setPosition(String position){this.position = position;}
}