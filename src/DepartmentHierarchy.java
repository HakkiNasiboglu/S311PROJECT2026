package src;

import java.util.ArrayList;
import java.util.List;

interface WorkElement{
    void addElement(WorkElement element);
    void removeElement(WorkElement element);
    void display(int indent);
    String getName();
}

//abstract class CompositeElement implements WorkElement{} May be we could add CompositeElement class for both department and team classes

 class Department implements  WorkElement{
    private String name;
    private List<WorkElement> teams = new ArrayList<>();

    public Department(String name){
        this.name = name;
    }

    @Override
    public void addElement(WorkElement team) {
        teams.add(team);
    }

    @Override
    public void removeElement(WorkElement team) {teams.remove(team);}

     @Override
     public void display(int indent) {
         String indentation = "    ".repeat(indent);

         System.out.println(indentation + "DEPARTMENT: " + this.name.toUpperCase());

         for (WorkElement element : teams) {
             element.display(indent + 1);
         }
     }

    @Override
    public String getName() {
        return name;
    }
}

class Team implements WorkElement{
    private String teamName;
    private  List<WorkElement> members = new ArrayList<>();

    public Team(String name){
        this.teamName = name;
    }

    @Override
    public void addElement(WorkElement element) {
        members.add(element);
    }

    @Override
    public void removeElement(WorkElement element) {
        members.remove(element);
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);

        System.out.println(indentation + "👥 Team: " + this.teamName);

        for (WorkElement member : members) {
            member.display(indent + 1);
        }
    }

    @Override
    public String getName() {
        return teamName;
    }
}

class Employee implements  WorkElement{
    private String memberName;
    private  String position;

    public  Employee(String name, String position){
        this.memberName = name;
        this.position = position;
    }

    @Override
    public void addElement(WorkElement element) {
        System.out.println("Wrong command");
    }

    @Override
    public void removeElement(WorkElement element) {
        System.out.println("Wrong command");
    }

    @Override
    public void display(int indent) {
        String indentation = "    ".repeat(indent);

        System.out.println(indentation + "Employee: " + this.memberName + " [" + this.position + "]");
    }

    @Override
    public String getName() {
        return memberName;
    }
}

