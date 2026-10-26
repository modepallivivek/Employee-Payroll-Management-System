package com.payroll;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menu-driven console application for employee payroll.
 *
 * The program starts with three sample employees already loaded, so
 * that display, search and payslip generation can be used immediately.
 * Every employee is given an automatically generated ID starting
 * from 1001. Records are held in an ArrayList of Employee objects
 * while the program runs; they are not persisted to a file or a
 * database, which is a stated limitation of this version.
 *
 * Menu (nine options):
 *   1 Display all employees
 *   2 Add employee
 *   3 Search by ID
 *   4 Search by name
 *   5 Update employee
 *   6 Delete employee
 *   7 Generate payslip
 *   8 Department-wise summary
 *   9 Exit
 *
 * Business methods (add, search, update, delete, summary) are kept
 * separate from the menu input code so that the automated tests can
 * exercise them directly without driving the console menu.
 */
public class EmployeePayrollSystem {
    private final List<Employee> employees = new ArrayList<>();
    private int nextId = 1001;

    public List<Employee> getEmployees() {
        return employees;
    }

    /** Loads the three sample employees used by the report's test run. */
    public void loadSampleEmployees() {
        addEmployee("Rahul Sharma", "9876543210", "IT", "Software Engineer", 50000);
        addEmployee("Priya Reddy", "9876543211", "HR", "HR Manager", 40000);
        addEmployee("Amit Verma", "9876543212", "Finance", "Accountant", 35000);
    }

    /**
     * Adds an employee after validation.
     *
     * @return the new employee, or null if the record was rejected
     *         (empty name, empty phone/department/designation, or a
     *         basic salary of zero or less).
     */
    public Employee addEmployee(String name, String phone, String department,
                                String designation, double basicSalary) {
        if (name == null || name.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || department == null || department.trim().isEmpty()
                || designation == null || designation.trim().isEmpty()
                || basicSalary <= 0) {
            return null;
        }
        Employee employee = new Employee(nextId++, name.trim(), phone.trim(),
                department.trim(), designation.trim(), basicSalary);
        employees.add(employee);
        return employee;
    }

    public Employee findById(int empId) {
        for (Employee employee : employees) {
            if (employee.getEmpId() == empId) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> findByName(String name) {
        List<Employee> result = new ArrayList<>();
        if (name == null) {
            return result;
        }
        String wanted = name.trim().toLowerCase();
        for (Employee employee : employees) {
            if (employee.getName().toLowerCase().contains(wanted)) {
                result.add(employee);
            }
        }
        return result;
    }

    /**
     * Updates an employee's basic salary.
     *
     * @return true if the employee was found and the new salary was
     *         valid, false otherwise.
     */
    public boolean updateBasicSalary(int empId, double newBasicSalary) {
        Employee employee = findById(empId);
        if (employee == null || newBasicSalary <= 0) {
            return false;
        }
        employee.setBasicSalary(newBasicSalary);
        return true;
    }

    /** Deletes an employee by ID; returns true only if one was removed. */
    public boolean deleteById(int empId) {
        Employee employee = findById(empId);
        if (employee == null) {
            return false;
        }
        return employees.remove(employee);
    }

    /**
     * Department-wise summary: for each department, the number of
     * employees and the total net payroll, in first-seen order.
     */
    public Map<String, double[]> departmentSummary() {
        Map<String, double[]> summary = new LinkedHashMap<>();
        for (Employee employee : employees) {
            double[] row = summary.computeIfAbsent(employee.getDepartment(),
                    k -> new double[2]);
            row[0] += 1; // count
            row[1] += employee.calculateNet(); // total net payroll
        }
        return summary;
    }

    public double totalNetPayroll() {
        double total = 0;
        for (Employee employee : employees) {
            total += employee.calculateNet();
        }
        return total;
    }

    // ------------------------------------------------------------------
    // Console menu
    // ------------------------------------------------------------------

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static double readSalary(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid salary. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    private void displayAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee employee : employees) {
            employee.displayEmployee();
        }
    }

    private void addFromMenu(Scanner scanner) {
        String name = readLine(scanner, "Enter name: ");
        String phone = readLine(scanner, "Enter phone: ");
        String department = readLine(scanner, "Enter department: ");
        String designation = readLine(scanner, "Enter designation: ");
        double basicSalary = readSalary(scanner, "Enter basic salary: ");
        Employee added = addEmployee(name, phone, department, designation, basicSalary);
        if (added == null) {
            System.out.println("Invalid employee details. Employee not added.");
        } else {
            System.out.println("Employee added with ID " + added.getEmpId());
        }
    }

    private void runMenu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("===== Employee Payroll Management System =====");
            System.out.println("1. Display all employees");
            System.out.println("2. Add employee");
            System.out.println("3. Search by ID");
            System.out.println("4. Search by name");
            System.out.println("5. Update employee");
            System.out.println("6. Delete employee");
            System.out.println("7. Generate payslip");
            System.out.println("8. Department-wise summary");
            System.out.println("9. Exit");
            int choice = readInt(scanner, "Enter your choice: ");
            switch (choice) {
                case 1:
                    displayAll();
                    break;
                case 2:
                    addFromMenu(scanner);
                    break;
                case 3: {
                    Employee found = findById(readInt(scanner, "Enter employee ID: "));
                    if (found == null) {
                        System.out.println("Employee not found.");
                    } else {
                        found.displayEmployee();
                    }
                    break;
                }
                case 4: {
                    List<Employee> found = findByName(readLine(scanner, "Enter name to search: "));
                    if (found.isEmpty()) {
                        System.out.println("No employee found.");
                    } else {
                        for (Employee employee : found) {
                            employee.displayEmployee();
                        }
                    }
                    break;
                }
                case 5: {
                    int id = readInt(scanner, "Enter employee ID to update: ");
                    double salary = readSalary(scanner, "Enter new basic salary: ");
                    if (updateBasicSalary(id, salary)) {
                        System.out.println("Employee updated.");
                    } else {
                        System.out.println("Employee not found or invalid salary.");
                    }
                    break;
                }
                case 6: {
                    int id = readInt(scanner, "Enter employee ID to delete: ");
                    if (deleteById(id)) {
                        System.out.println("Employee deleted.");
                    } else {
                        System.out.println("Employee not found.");
                    }
                    break;
                }
                case 7: {
                    Employee found = findById(readInt(scanner, "Enter employee ID for payslip: "));
                    if (found == null) {
                        System.out.println("Employee not found.");
                    } else {
                        found.displayPayslip();
                    }
                    break;
                }
                case 8: {
                    for (Map.Entry<String, double[]> entry : departmentSummary().entrySet()) {
                        System.out.println(entry.getKey() + " : " + (int) entry.getValue()[0]
                                + " employee(s), Total Net Payroll: Rs. "
                                + String.format("%.2f", entry.getValue()[1]));
                    }
                    System.out.println("Total Employees: " + employees.size()
                            + ", Total Net Payroll: Rs. "
                            + String.format("%.2f", totalNetPayroll()));
                    break;
                }
                case 9:
                    System.out.println("Exiting. Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        EmployeePayrollSystem system = new EmployeePayrollSystem();
        system.loadSampleEmployees();
        try (Scanner scanner = new Scanner(System.in)) {
            system.runMenu(scanner);
        }
    }
}
