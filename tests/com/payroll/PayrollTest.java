package com.payroll;

import java.util.List;
import java.util.Map;

/**
 * Dependency-free test runner for the payroll logic.
 *
 * Mirrors the 16 test cases in the supplied report, plus validation
 * and edge cases. Each check prints PASS or FAIL; the process exits
 * with a non-zero status if any check fails. Run with test.sh.
 *
 * Note on the supplied report's Table 2: its test 14 says "2 records
 * displayed" after adding one employee (test 8) and then deleting one
 * (test 13). Arithmetically that sequence gives 3 records
 * (3 + 1 - 1), not 2, so this runner asserts 3 and the report records
 * that discrepancy instead of repeating the supplied figure.
 */
public class PayrollTest {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static boolean approx(double actual, double expected) {
        return Math.abs(actual - expected) < 0.01;
    }

    public static void main(String[] args) {
        EmployeePayrollSystem system = new EmployeePayrollSystem();
        system.loadSampleEmployees();

        // 1. Display all employees (3 sample records)
        check("1. Three sample employees loaded", system.getEmployees().size() == 3);

        // 2. Payslip for ID 1001 (basic Rs. 50,000) -> net Rs. 58,800
        Employee e1001 = system.findById(1001);
        check("2. Payslip ID 1001 net Rs. 58,800",
                e1001 != null && approx(e1001.calculateNet(), 58800));

        // 3. Search by ID 1002 (existing)
        check("3. Search by ID 1002 found", system.findById(1002) != null);

        // 4. Search by ID 9999 (not existing)
        check("4. Search by ID 9999 not found", system.findById(9999) == null);

        // 5. Search by name "Priya Reddy"
        List<Employee> priya = system.findByName("Priya Reddy");
        check("5. Search by name Priya Reddy found",
                priya.size() == 1 && priya.get(0).getEmpId() == 1002);

        // 6. Search by name "Nobody"
        check("6. Search by name Nobody not found",
                system.findByName("Nobody").isEmpty());

        // 7. Department-wise summary total Rs. 1,46,900
        check("7. Department summary total Rs. 146,900",
                approx(system.totalNetPayroll(), 146900));
        Map<String, double[]> summary = system.departmentSummary();
        check("7b. Summary has IT, HR and Finance",
                summary.containsKey("IT") && summary.containsKey("HR")
                        && summary.containsKey("Finance"));

        // 8. Add employee Sneha Patel (basic Rs. 45,000) -> ID 1004
        Employee sneha = system.addEmployee("Sneha Patel", "9876543213",
                "IT", "Developer", 45000);
        check("8. Add Sneha Patel with ID 1004",
                sneha != null && sneha.getEmpId() == 1004);

        // 9. Display after adding: 4 records
        check("9. Four records after adding", system.getEmployees().size() == 4);

        // 10. Payslip for ID 1004 -> net Rs. 52,900
        check("10. Payslip ID 1004 net Rs. 52,900",
                sneha != null && approx(sneha.calculateNet(), 52900));

        // 11. Update ID 1003 basic salary to Rs. 38,000
        check("11. Update ID 1003 basic to Rs. 38,000",
                system.updateBasicSalary(1003, 38000));

        // 12. Payslip for updated ID 1003 -> net Rs. 44,640
        Employee e1003 = system.findById(1003);
        check("12. Payslip updated ID 1003 net Rs. 44,640",
                e1003 != null && approx(e1003.calculateNet(), 44640));

        // 13. Delete employee ID 1002
        check("13. Delete ID 1002", system.deleteById(1002)
                && system.findById(1002) == null);

        // 14. Display after deletion: 3 records (3 + 1 - 1), see class note
        check("14. Three records after deletion", system.getEmployees().size() == 3);

        // Validation / edge cases (menu-level invalid choice and
        // non-numeric salary are exercised in the scripted demo run)
        check("15. Empty name rejected",
                system.addEmployee("", "9876543210", "IT", "Developer", 50000) == null);
        check("16. Zero salary rejected",
                system.addEmployee("Test User", "9876543210", "IT", "Developer", 0) == null);
        check("17. Negative salary rejected",
                system.addEmployee("Test User", "9876543210", "IT", "Developer", -100) == null);
        check("18. Update of missing ID rejected",
                !system.updateBasicSalary(9999, 50000));
        check("19. Delete of missing ID rejected",
                !system.deleteById(9999));
        check("20. Salary formula: net = 1.18 * basic - 200",
                approx(new Employee(9999, "Formula", "0", "IT", "X", 10000).calculateNet(),
                        11600));

        System.out.println();
        System.out.println("Tests passed: " + passed + ", failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}
