#!/bin/sh
# *******************************************************************
# Run a complete set of Jess-based tests 
# *******************************************************************

# 
# Run the tests
# 
# This shell script was run under the cygwin system so some changes 
# may be needed to run uner other unix variants. 
#

echo "Running All FuzzyJess test programs ..."


JESS_JAR=../../jess.jar
FUZZYJ_JAR=../../fuzzyJ-OpenSource-2.0.jar
export JESS_JAR
export FUZZYJ_JAR

echo ""
echo $JESS_JAR
echo $FUZZYJ_JAR
echo ""


./runFuzzyMatchTests.sh
./runFuzzyCompilerTest.sh

echo "Testing Completed"



