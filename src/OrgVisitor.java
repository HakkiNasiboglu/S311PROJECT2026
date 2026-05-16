package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrgVisitor {
    void visitEmployee(WorkElement employee);
    void visitDepartment(WorkElement department);
    void visitTeam(WorkElement team);
    String getReport();
}

class HeadcountVisitor implements OrgVisitor {
    private int employeeCount   = 0;
    private int departmentCount = 0;
    private int teamCount       = 0;

    @Override
    public void visitEmployee(WorkElement employee) {employeeCount++;}

    @Override
    public void visitDepartment(WorkElement department) {departmentCount++;}

    @Override
    public void visitTeam(WorkElement team) {teamCount++;}

    @Override
    public String getReport() {
        return "--- Headcount Report ---\n"
                + "Departments : " + departmentCount + "\n"
                + "Teams       : " + teamCount + "\n"
                + "Employees   : " + employeeCount;
    }
}

class DiversityVisitor implements OrgVisitor {
    private int femaleCount = 0;
    private int maleCount   = 0;
    private int otherCount  = 0;

    @Override
    public void visitEmployee(WorkElement employee) {
        String gender = ((Employee) employee).getGender();
        if ("Female".equalsIgnoreCase(gender)) femaleCount++;
        else if ("Male".equalsIgnoreCase(gender)) maleCount++;
        else otherCount++;
    }

    @Override
    public void visitDepartment(WorkElement department) {}

    @Override
    public void visitTeam(WorkElement team) {}

    @Override
    public String getReport() {
        int total = femaleCount + maleCount + otherCount;
        String femaleRatio = total > 0
                ? String.format("%.1f%%", (femaleCount * 100.0) / total) : "N/A";
        String maleRatio   = total > 0
                ? String.format("%.1f%%", (maleCount   * 100.0) / total) : "N/A";

        return "--- Diversity Report ---\n"
                + "Female : " + femaleCount + " (" + femaleRatio + ")\n"
                + "Male   : " + maleCount   + " (" + maleRatio   + ")\n"
                + "Other  : " + otherCount;
    }
}

class SeniorityVisitor implements OrgVisitor {
    private List<WorkElement> seniorEmployees = new ArrayList<>();
    private static final int THRESHOLD = 20;

    @Override
    public void visitEmployee(WorkElement employee) {
        if (((Employee) employee).getYearsOfService() >= THRESHOLD) {
            seniorEmployees.add(employee);
        }
    }

    @Override
    public void visitDepartment(WorkElement department) {}

    @Override
    public void visitTeam(WorkElement team) {}

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder("--- Seniority Report (20+ years) ---\n");

        if (seniorEmployees.isEmpty()) {
            sb.append("No employees with 20+ years of service found.");
            return sb.toString();
        }

        for (WorkElement e : seniorEmployees) {
            sb.append("- ").append(e.getName())
                    .append(" | ").append(((Employee) e).getYearsOfService()).append(" years")
                    .append(" | ").append(((Employee) e).getPosition())
                    .append("\n");
        }
        return sb.toString().trim();
    }
}

class RoleDistributionVisitor implements OrgVisitor {
    private Map<String, Integer> positionCounts = new HashMap<>();

    @Override
    public void visitEmployee(WorkElement employee) {
        String pos = ((Employee) employee).getPosition();
        positionCounts.put(pos, positionCounts.getOrDefault(pos, 0) + 1);
    }

    @Override
    public void visitDepartment(WorkElement department) {}

    @Override
    public void visitTeam(WorkElement team) {}

    @Override
    public String getReport() {
        StringBuilder sb = new StringBuilder("--- Role Distribution Report ---\n");

        if (positionCounts.isEmpty()) {
            sb.append("No roles found.");
            return sb.toString();
        }

        for (Map.Entry<String, Integer> entry : positionCounts.entrySet()) {
            sb.append("- ").append(entry.getKey())
                    .append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString().trim();
    }
}
