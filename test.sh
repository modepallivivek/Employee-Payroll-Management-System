#!/bin/sh
# Compile and run the dependency-free test suite.
set -e
cd "$(dirname "$0")"
rm -rf out-test
mkdir -p out-test
find src tests -name '*.java' > /tmp/payroll-test-sources.txt
javac --release 17 -d out-test @/tmp/payroll-test-sources.txt
java -cp out-test com.payroll.PayrollTest
