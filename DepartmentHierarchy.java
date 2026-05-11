import java.util.ArrayList;
import java.util.List;

interface WorkElement{
    void add(WorkElement element);
    void remove(WorkElement element);
    void display(int indent);
    public String getName();
}

 class Department implements  WorkElement{
    private String name;
    private List<WorkElement> departmentList = new ArrayList<>();

    public Department(String name){
        this.name = name;
    }
    @Override
    public void add(WorkElement element) {
        departmentList.add(element);
    }

    @Override
    public void remove(WorkElement element) {
        departmentList.remove(element);
    }

    @Override
    public void display(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("-");
        }
            System.out.println(" " + name);

        for (WorkElement element : departmentList) {
            element.display(indent + 1);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

class Team implements WorkElement{
    private String name;
    private  List<WorkElement> teamList = new ArrayList<>();

    public Team(String name){
        this.name=name;
    }
    @Override
    public void add(WorkElement element) {
        teamList.add(element);
    }

    @Override
    public void remove(WorkElement element) {
        teamList.remove(element);
    }

    @Override
    public void display(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("-");
        }
            System.out.println(" " + name);

        for (WorkElement element : teamList) {
            element.display(indent + 1);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

class Employee implements  WorkElement{
    private String name;
    private  String position;

    public  Employee(String name, String position){
        this.name = name;
        this.position = position;
    }

    @Override
    public void add(WorkElement element) {
        System.out.println("Wrong command");
    }

    @Override
    public void remove(WorkElement element) {
        System.out.println("Wrong command");
    }

    @Override
    public void display(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("-");
        }
            System.out.println(" " + name);
    }

    @Override
    public String getName() {
        return name;
    }
}

