package src;

import java.util.ArrayList;
import java.util.List;

interface HRCommand {
    void execute();
    boolean validate();
    void undo();
}

class HireCommand implements HRCommand {
    private WorkElement targetGroup;
    private WorkElement employee;

    public HireCommand(WorkElement targetGroup, WorkElement employee) {
        this.targetGroup = targetGroup;
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return targetGroup != null && employee != null;
    }

    @Override
    public void execute() {
        targetGroup.addElement(employee);
        AuditLog.GetAuditLog().sendLog("Recruitment: " + employee.getName() + " was added.");
    }

    @Override
    public void undo() {
        targetGroup.removeElement(employee);
        AuditLog.GetAuditLog().sendLog("Retracted: Recruitment cancelled - " + employee.getName());
    }
}

class PromoteCommand implements HRCommand {

    private WorkElement parentGroup;
    private Employee oldEmployee;
    private Employee promotedEmployee;

    public PromoteCommand(WorkElement parentGroup, Employee oldEmployee, Employee promotedEmployee) {
        this.parentGroup = parentGroup;
        this.oldEmployee = oldEmployee;
        this.promotedEmployee = promotedEmployee;
    }

    @Override
    public boolean validate() {
        return parentGroup != null && oldEmployee != null && promotedEmployee != null;
    }

    @Override
    public void execute() {
        parentGroup.removeElement(oldEmployee);
        parentGroup.addElement(promotedEmployee);
        AuditLog.GetAuditLog().sendLog("Promotion: " + oldEmployee.getName() + " promoted.");
    }

    @Override
    public void undo() {
        parentGroup.removeElement(promotedEmployee);
        parentGroup.addElement(oldEmployee);
        AuditLog.GetAuditLog().sendLog("Reversed: Promotion cancelled - " + oldEmployee.getName());
    }
}

class LayoffCommand implements HRCommand {
    private WorkElement targetGroup;
    private WorkElement employee;

    public LayoffCommand(WorkElement targetGroup, WorkElement employee) {
        this.targetGroup = targetGroup;
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return targetGroup != null && employee != null;
    }

    @Override
    public void execute() {
        targetGroup.removeElement(employee);
        AuditLog.GetAuditLog().sendLog("Dismissal: " + employee.getName() + " left.");
    }

    @Override
    public void undo() {
        targetGroup.addElement(employee);
        AuditLog.GetAuditLog().sendLog("Reversed: The dismissal has been cancelled - " + employee.getName());
    }
}

class MergeDeptCommand implements HRCommand {
    private WorkElement parentDepartment;
    private WorkElement sourceDepartment;
    private WorkElement targetDepartment;

    public MergeDeptCommand(WorkElement parentDepartment, WorkElement sourceDepartment, WorkElement targetDepartment) {
        this.parentDepartment = parentDepartment;
        this.sourceDepartment = sourceDepartment;
        this.targetDepartment = targetDepartment;
    }

    @Override
    public boolean validate() {
        return  sourceDepartment != null && targetDepartment != null && parentDepartment != null && sourceDepartment != targetDepartment;
    }

    @Override
    public void execute() {
        parentDepartment.removeElement(sourceDepartment);
        targetDepartment.addElement(sourceDepartment);
        AuditLog.GetAuditLog().sendLog("Department Merger: " + sourceDepartment.getName() + " -> " + targetDepartment.getName() + " added into.");
    }

    @Override
    public void undo() {
        targetDepartment.removeElement(sourceDepartment);
        parentDepartment.addElement(sourceDepartment);
        AuditLog.GetAuditLog().sendLog("Split Back: " + sourceDepartment.getName() + " left and returned to her old place.");
    }
}

class HRInvoker {
    private List<HRCommand> commandHistory = new ArrayList<>();

    public void executeCommand(HRCommand command) {
        if (command == null) return;

        if (command.validate()) {
            command.execute();
            commandHistory.add(command);
        }else {
            System.out.println("Command rejected: Verification failed.");
        }
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            HRCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
            lastCommand.undo();
        }else {
            System.out.println("No action could be undone.");
        }
    }
}
