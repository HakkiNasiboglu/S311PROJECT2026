package src;

import java.util.ArrayList;
import java.util.List;

interface HRCommand {
    void execute();

    boolean validate();

    void undo();
}

class HireCommand implements HRCommand {
    private CompositeElement target; // dept or team
    private Employee employee;

    public HireCommand(CompositeElement targetTeam, Employee employee) {
        this.target = targetTeam;
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return target != null && employee != null && employee.getParent() == null;
    }

    @Override
    public void execute() {
        target.addElement(employee);
        AuditLog.getAuditLog().sendLog("Recruitment: " + employee.getName() + " was added.");
    }

    @Override
    public void undo() {
        target.removeElement(employee);
        AuditLog.getAuditLog().sendLog("Retracted: Recruitment cancelled - " + employee.getName());
    }
}

class PromoteCommand implements HRCommand {
    private Employee employee;
    private String newPosition;
    private String currentPosition;

    public PromoteCommand(Employee employee, String newPosition) {
        this.employee = employee;
        this.newPosition = newPosition;
    }

    @Override
    public boolean validate() {
        return employee != null && employee.getParent() != null;
    }

    @Override
    public void execute() {
        this.currentPosition = employee.getPosition();
        employee.setPosition(newPosition);
        AuditLog.getAuditLog().sendLog("Promotion: " + employee.getName() + " promoted to new role.");
    }

    @Override
    public void undo() {
        employee.setPosition(currentPosition);
        AuditLog.getAuditLog().sendLog("Reversed: Promotion cancelled for " + employee.getName());
    }
}

class LayoffCommand implements HRCommand {
    private Employee employee;
    private CompositeElement savedParent;

    public LayoffCommand(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return employee != null && employee.getParent() instanceof CompositeElement;
    }

    @Override
    public void execute() {
        this.savedParent = (CompositeElement) employee.getParent(); // dept or team
        savedParent.removeElement(employee);
        AuditLog.getAuditLog().sendLog("Dismissal: " + employee.getName() + " has been dismissed.");
    }

    @Override
    public void undo() {
        if (savedParent != null)
            savedParent.addElement(employee);
        AuditLog.getAuditLog().sendLog("Retracted: Dismissal cancelled - " + employee.getName());
    }
}

class MergeDeptCommand implements HRCommand {
    private Department mergingDepartment1;
    private Department mergingDepartment2;
    private Department mergedDepartment;
    private CompositeElement commonParent;

    public MergeDeptCommand(Department d1, Department d2, Department targetDept) {
        this.mergingDepartment1 = d1;
        this.mergingDepartment2 = d2;
        this.mergedDepartment = targetDept;
    }

    @Override
    public boolean validate() {
        return mergingDepartment1 != null && mergingDepartment2 != null &&
                mergingDepartment1.getParent() != null &&
                mergingDepartment1.getParent() == mergingDepartment2.getParent();
    }

    @Override
    public void execute() {
        this.commonParent = (CompositeElement) mergingDepartment1.getParent();

        mergedDepartment.addElement(mergingDepartment1);
        mergedDepartment.addElement(mergingDepartment2);

        commonParent.removeElement(mergingDepartment1);
        commonParent.removeElement(mergingDepartment2);
        commonParent.addElement(mergedDepartment);

        AuditLog.getAuditLog().sendLog("Department merged: " + mergedDepartment.getName());
        OrganizationNotifier.getInstance().notifyObservers("Departments merged into: " + mergedDepartment.getName());
    }

    @Override
    public void undo() {
        commonParent.removeElement(mergedDepartment);
        commonParent.addElement(mergingDepartment1);
        commonParent.addElement(mergingDepartment2);

        AuditLog.getAuditLog().sendLog("Reversed: Department merge cancelled - " + mergedDepartment.getName());
    }
}

class SplitBackDeptCommand implements HRCommand {
    private Department targetDepartment;
    private CompositeElement parent;

    public SplitBackDeptCommand(Department targetDept) {
        this.targetDepartment = targetDept;
    }

    private List<WorkElement> subDepartments;

    @Override
    public boolean validate() {
        return targetDepartment.getParent() != null && !targetDepartment.getChild().isEmpty();
    }

    @Override
    public void execute() {
        this.parent = (CompositeElement) targetDepartment.getParent();

        this.subDepartments = new ArrayList<>(targetDepartment.getChild());

        parent.removeElement(targetDepartment);

        for (WorkElement subDept : subDepartments) {
            targetDepartment.removeElement(subDept);
            parent.addElement(subDept);
        }

        AuditLog.getAuditLog().sendLog(targetDepartment.getName() + " split back into its original parts.");
        OrganizationNotifier.getInstance()
                .notifyObservers(targetDepartment.getName() + " split back into its original parts.");
    }

    @Override
    public void undo() {
        for (WorkElement subDept : subDepartments) {
            parent.removeElement(subDept);
            targetDepartment.addElement(subDept);
        }
        parent.addElement(targetDepartment);

        AuditLog.getAuditLog().sendLog("Reversed: Split back cancelled.");
    }

    public List<WorkElement> getRecoveredDepartments() {
        if (subDepartments != null) {
            return new ArrayList<>(subDepartments);
        } else {
            return new ArrayList<>();
        }
    }
}

class HRInvoker {
    private List<HRCommand> commandHistory = new ArrayList<>();

    public void executeCommand(HRCommand command) {
        if (command == null)
            return;

        if (command.validate()) {
            command.execute();
            commandHistory.add(command);
        } else {
            System.out.println("Command rejected: Verification failed.");
        }
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            HRCommand lastCommand = commandHistory.removeLast();
            lastCommand.undo();
        } else {
            System.out.println("No action could be undone.");
        }
    }
}