#!/bin/sh
# Run the console application. Compile first with ./compile.sh
set -e
cd "$(dirname "$0")"
java -cp out com.payroll.EmployeePayrollSystem
