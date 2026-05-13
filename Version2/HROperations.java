import java.util.ArrayList;
import java.util.List;

interface HRCommand {
    void execute();
    boolean validate();
    void undo();
}

class HireCommand implements HRCommand {
    private WorkElement targetTeam;
    private WorkElement employee;

    public HireCommand(WorkElement targetTeam, WorkElement employee) {
        this.targetTeam = targetTeam;
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return targetTeam != null && employee != null && employee.getParent() == null;
    }

    @Override
    public void execute() {
        targetTeam.addElement(employee);
        employee.setParent(targetTeam);
        AuditLog.GetAuditLog().sendLog("Recruitment: " + employee.getName() + " was added.");
    }

    @Override
    public void undo() {
        targetTeam.removeElement(employee);
        employee.setParent(null);
        AuditLog.GetAuditLog().sendLog("Retracted: Recruitment cancelled - " + employee.getName());
    }
}

class PromoteCommand implements HRCommand {
    private WorkElement parent;
    private WorkElement employee;
    private WorkElement promotedEmployee;

    public PromoteCommand(WorkElement employee, String newPosition) {
        this.employee = employee;
        Employee old = (Employee) employee;
        this.promotedEmployee = new Employee(old.getName(), newPosition, old.getYearsOfService(), old.getGender());
    }

    @Override
    public boolean validate() {
        return employee != null && employee.getParent() != null;
    }

    @Override
    public void execute() {
        this.parent = employee.getParent();

        parent.removeElement(employee);
        parent.addElement(promotedEmployee);

        AuditLog.GetAuditLog().sendLog("Promotion: " + employee.getName() + " promoted to new role.");
    }

    @Override
    public void undo() {
        parent.removeElement(promotedEmployee);
        parent.addElement(employee);
        AuditLog.GetAuditLog().sendLog("Reversed: Promotion cancelled for " + employee.getName());
    }
}

class LayoffCommand implements HRCommand {
    private WorkElement employee;
    private WorkElement savedParent;

    public LayoffCommand(WorkElement employee) {
        this.employee = employee;
    }

    @Override
    public boolean validate() {
        return employee != null && employee.getParent() != null;
    }

    @Override
    public void execute() {
        this.savedParent = employee.getParent();
        savedParent.removeElement(employee);
        AuditLog.GetAuditLog().sendLog("Dismissal: " + employee.getName() + " has been dismissed.");
    }

    @Override
    public void undo() {
        if (savedParent != null) savedParent.addElement(employee);
        AuditLog.GetAuditLog().sendLog("Retracted: Dismissal cancelled - " + employee.getName());
    }
}

class MergeDeptCommand implements HRCommand {
    private WorkElement mergingDepartment1;
    private WorkElement mergingDepartment2;
    private WorkElement mergedDepartment;
    private WorkElement commonParent;

    public MergeDeptCommand(WorkElement d1, WorkElement d2, String newName) {
        this.mergingDepartment1 = d1;
        this.mergingDepartment2 = d2;
        this.mergedDepartment = new Department(newName);
    }

    public WorkElement getMergedDepartment() {
        return mergedDepartment;
    }

    @Override
    public boolean validate() {
        return mergingDepartment1 != null && mergingDepartment2 != null &&
                mergingDepartment1 instanceof Department && mergingDepartment2 instanceof Department &&
                mergingDepartment1.getParent() != null && mergingDepartment1.getParent() == mergingDepartment2.getParent();
    }

    @Override
    public void execute() {
        this.commonParent = mergingDepartment1.getParent();

        List<WorkElement> sources = new ArrayList<>();
        sources.add(mergingDepartment1);
        sources.add(mergingDepartment2);
        ((Department) mergedDepartment).setOrigins(sources);

        mergedDepartment.getList().addAll(mergingDepartment1.getList());
        mergedDepartment.getList().addAll(mergingDepartment2.getList());

        commonParent.removeElement(mergingDepartment1);
        commonParent.removeElement(mergingDepartment2);
        commonParent.addElement(mergedDepartment);

        AuditLog.GetAuditLog().sendLog("Department merged: " + mergedDepartment.getName());
    }

    @Override
    public void undo() {
        commonParent.removeElement(mergedDepartment);
        commonParent.addElement(mergingDepartment1);
        commonParent.addElement(mergingDepartment2);

        AuditLog.GetAuditLog().sendLog("Reversed: Department merge cancelled - " + mergedDepartment.getName());
    }
}

class SplitBackDeptCommand implements HRCommand {
    private WorkElement splittingDepartment;
    private WorkElement parent;

    public SplitBackDeptCommand(WorkElement targetDept) {
        this.splittingDepartment = targetDept;
    }

    @Override
    public boolean validate() {
        return splittingDepartment instanceof Department &&
                splittingDepartment.getParent() != null &&
                !((Department) splittingDepartment).getOrigins().isEmpty();
    }

    @Override
    public void execute() {
        parent = splittingDepartment.getParent();
        List<WorkElement> originalParts = ((Department) splittingDepartment).getOrigins();

        parent.removeElement(splittingDepartment);

        for (WorkElement original : originalParts) parent.addElement(original);

        AuditLog.GetAuditLog().sendLog(splittingDepartment.getName() + " split back into its original parts.");
    }

    public void undo() {
        List<WorkElement> origins = ((Department) splittingDepartment).getOrigins();

        for (WorkElement origin : origins) parent.removeElement(origin);

        parent.addElement(splittingDepartment);

        AuditLog.GetAuditLog().sendLog("Reversed: Split back cancelled - " + splittingDepartment.getName());
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