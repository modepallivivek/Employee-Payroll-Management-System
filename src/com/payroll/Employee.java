package com.payroll;

/**
 * Represents one employee and knows how to calculate its own salary.
 *
 * Salary rules used in this project (kept simple so that every
 * payslip can be verified by hand):
 *   HRA             = 20% of Basic Salary
 *   DA              = 10% of Basic Salary
 *   Gross Salary    = Basic + HRA + DA
 *   Provident Fund  = 12% of Basic Salary
 *   Professional Tax = Rs. 200 (fixed)
 *   Net Salary      = Gross - PF - Professional Tax
 *
 * Hence, for any basic salary B, net = 1.18 * B - 200.
 */
public class Employee extends Person {
    private int empId;
    private String department;
    private String designation;
    private double basicSalary;

    public Employee(int empId, String name, String phone,
                    String department, String designation, double basicSalary) {
        super(name, phone);
        this.empId = empId;
        this.department = department;
        this.designation = designation;
        this.basicSalary = basicSalary;
    }

    public int getEmpId() {
        return empId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double calculateHRA() {
        return 0.20 * basicSalary;
    }

    public double calculateDA() {
        return 0.10 * basicSalary;
    }

    public double calculateGross() {
        return basicSalary + calculateHRA() + calculateDA();
    }

    public double calculatePF() {
        return 0.12 * basicSalary;
    }

    public double calculateProfessionalTax() {
        return 200.0;
    }

    public double calculateNet() {
        return calculateGross() - calculatePF() - calculateProfessionalTax();
    }

    public void displayEmployee() {
        System.out.println("ID : " + empId
                + " | Name : " + getName()
                + " | Dept : " + department
                + " | Designation : " + designation
                + " | Basic : Rs. " + String.format("%.2f", basicSalary)
                + " | Net : Rs. " + String.format("%.2f", calculateNet()));
    }

    public void displayPayslip() {
        System.out.println("----------PAYSLIP----------");
        System.out.println("Employee ID  : " + empId);
        System.out.println("Name         : " + getName());
        System.out.println("Department   : " + department);
        System.out.println("Designation  : " + designation);
        System.out.println("Basic Salary : Rs. " + String.format("%.2f", basicSalary));
        System.out.println("HRA (20%)    : Rs. " + String.format("%.2f", calculateHRA()));
        System.out.println("DA (10%)     : Rs. " + String.format("%.2f", calculateDA()));
        System.out.println("Gross Salary : Rs. " + String.format("%.2f", calculateGross()));
        System.out.println("PF (12%)     : Rs. " + String.format("%.2f", calculatePF()));
        System.out.println("Prof. Tax    : Rs. " + String.format("%.2f", calculateProfessionalTax()));
        System.out.println("Net Salary   : Rs. " + String.format("%.2f", calculateNet()));
        System.out.println("-----------------------------");
    }
}
