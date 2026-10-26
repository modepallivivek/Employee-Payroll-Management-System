# Employee Payroll Management System

A console-based, menu-driven Java application for maintaining employee
records and calculating salaries with fixed, transparent rules. No
external libraries are required.

## Features (actually implemented)

- **Employee records** — add, display, search (by ID and by name),
  update and delete employees, stored in an `ArrayList`.
- **Salary calculation** — HRA 20% of basic, DA 10% of basic,
  gross = basic + HRA + DA, PF 12% of basic, professional tax
  Rs. 200 fixed, net = gross − PF − professional tax.
  Hence net = 1.18 × basic − 200, so every payslip can be checked by hand.
- **Payslip** — complete payslip for any employee selected by ID.
- **Department-wise summary** — employee count and total net payroll
  per department, plus the overall total.
- **Validation and error handling** — empty names and salaries of zero
  or less are rejected; non-numeric salary/ID input is re-prompted
  (`InputMismatchException`); invalid menu choices and missing IDs
  are reported without stopping the program.
- **Object-oriented design** — `Person` (base: name, phone) is extended
  by `Employee` (ID, department, designation, basic salary, salary
  calculation); `EmployeePayrollSystem` manages the menu and the
  employee collection.

## Project structure

```
src/com/payroll/
  Person.java                  Base class: name, phone
  Employee.java                Employee + salary calculation
  EmployeePayrollSystem.java   Menu + operations + main
tests/com/payroll/
  PayrollTest.java             Dependency-free test runner
docs/report/
  main.tex                     LaTeX report source (Overleaf-ready)
  main.pdf                     Compiled report (≤ 15 pages)
  college_logo.jpg             Title-page logo
  payslip_and_summary.png      Report figure (also in screenshots/)
screenshots/
  demo_run.txt                 Full scripted demo output (real run)
  payslip_and_summary.png      Rendered excerpt of that run
compile.sh, run.sh, test.sh, .gitignore
```

## Requirements

- JDK 17 or later (tested with OpenJDK 17, Temurin 17.0.21)
- No other dependencies

## Build and run

```sh
./compile.sh
./run.sh
```

Or manually:

```sh
javac --release 17 -d out $(find src -name '*.java')
java -cp out com.payroll.EmployeePayrollSystem
```

The program starts with three sample employees (IDs 1001–1003) and
shows a nine-option menu:

1. Display all employees
2. Add employee
3. Search by ID
4. Search by name
5. Update employee
6. Delete employee
7. Generate payslip
8. Department-wise summary
9. Exit

## Sample data and hand-checked values

| ID | Name | Department | Basic (Rs.) | Net (Rs.) |
|----|------|------------|-------------|-----------|
| 1001 | Rahul Sharma | IT | 50,000 | 58,800 |
| 1002 | Priya Reddy | HR | 40,000 | 47,000 |
| 1003 | Amit Verma | Finance | 35,000 | 41,100 |

Department summary for the samples: IT Rs. 58,800, HR Rs. 47,000,
Finance Rs. 41,100, total Rs. 1,46,900.

Note: the supplied report named Rahul Sharma (1001) and Priya Reddy
(1002, by name search) but did not name the third sample employee;
Amit Verma (Finance, Rs. 35,000) was chosen for this implementation
to reproduce the stated Finance total of Rs. 41,100.

## Testing

```sh
./test.sh
```

Result on the verified run: **21 checks, 0 failures**. The checks
mirror the 16 test cases in the supplied report, plus validation and
edge cases (empty name, zero/negative salary, missing-ID update/delete,
salary formula).

One correction to the supplied report's test table is recorded in the
report and in `PayrollTest.java`: after adding one employee and then
deleting one, the count is 3 (3 + 1 − 1), not 2 as printed in the
supplied table.

A scripted run through the real menu is saved in
`screenshots/demo_run.txt`; it covers display, payslip, summary,
name search, adding (including a non-numeric salary that is
re-prompted), updating, deleting, an invalid menu choice (99) and a
not-found name search.

## Report

`docs/report/main.tex` is the complete, editable LaTeX source for the
course-end report. Compile it with pdfLaTeX (twice, for the table of
contents) — for example, from `docs/report/`:

```sh
pdflatex main.tex
pdflatex main.tex
```

The compiled `main.pdf` is 11 pages (verified 2026-10-26, pdfLaTeX,
no LaTeX errors), within the 15-page limit including the title page,
contents, figures and bibliography.

## Known limitations

- Records are held in memory only and are lost when the program exits;
  the three sample employees are reloaded on the next start.
- Salary rules are fixed in code; income-tax slabs are not included.
- The interface is console-based; there is no GUI and no PDF payslip
  export yet.

## Future improvements

File or database persistence, income-tax slab calculation, a graphical
interface, and payslip export as a printable PDF.
