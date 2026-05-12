import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface OrgVisitor {
    void visitEmployee(Employee employee);

    void visitDepartment(Department department);

    String getReport();
}

class HeadcountVisitor implements OrgVisitor {
    private int employeeCount;
    private int departmentCount;

    public void visitEmployee(Employee employee) {
        employeeCount++;
    }

    public void visitDepartment(Department department) {
        departmentCount++;
    }

    public String getReport() {
        return "Headcount Report:\n"
                + "Departments: " + departmentCount + "\n"
                + "Employees: " + employeeCount;
    }
}

class DiversityVisitor implements OrgVisitor {
    private int femaleCount;
    private int maleCount;
    private int otherCount;

    public void visitEmployee(Employee employee) {
        String gender = employee.getGender();
        if ("Female".equalsIgnoreCase(gender)) {
            femaleCount++;
        } else if ("Male".equalsIgnoreCase(gender)) {
            maleCount++;
        } else {
            otherCount++;
        }
    }

    public void visitDepartment(Department department) {
    }

    public String getReport() {
        return "Diversity Report:\n"
                + "Female employees: " + femaleCount + "\n"
                + "Male employees: " + maleCount + "\n"
                + "Other employees: " + otherCount;
    }
}

class SeniorityVisitor implements OrgVisitor {
    private ArrayList<Employee> seniorEmployees = new ArrayList<>();

    public void visitEmployee(Employee employee) {
        if (employee.getYearsOfService() >= 20) {
            seniorEmployees.add(employee);
        }
    }

    public void visitDepartment(Department department) {
    }

    public String getReport() {
        StringBuilder report = new StringBuilder("Seniority Report:\n");
        report.append("Employees with 20+ years of service:");

        if (seniorEmployees.isEmpty()) {
            report.append("\nNo employees found.");
            return report.toString();
        }

        for (Employee employee : seniorEmployees) {
            report.append("\n- ")
                    .append(employee.getName())
                    .append(" (")
                    .append(employee.getYearsOfService())
                    .append(" years, ")
                    .append(employee.getRole())
                    .append(")");
        }
        return report.toString();
    }
}

class RoleDistributionVisitor implements OrgVisitor {
    private Map<String, Integer> roleCounts = new HashMap<>();

    public void visitEmployee(Employee employee) {
        String role = employee.getRole();
        roleCounts.put(role, roleCounts.getOrDefault(role, 0) + 1);
    }

    public void visitDepartment(Department department) {
    }

    public String getReport() {
        StringBuilder report = new StringBuilder("Role Distribution Report:");

        if (roleCounts.isEmpty()) {
            report.append("\nNo roles found.");
            return report.toString();
        }

        for (Map.Entry<String, Integer> entry : roleCounts.entrySet()) {
            report.append("\n- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue());
        }
        return report.toString();
    }
}
