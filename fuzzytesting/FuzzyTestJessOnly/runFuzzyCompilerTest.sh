#!/bin/bash
# *******************************************************************
# Run a Jess-based test (fuzzy compiler exmaple) to test various
# FuzzyJess features (fuzzy rules, moment-defuzzify, the various
# types of global contribution, antecedent combining and inference
# methods
# *******************************************************************

# 
# Run the test, and make sure we get the right output.
# 
# The variables JAVA, JESS_JAR and FUZZYJ_JAR must have been defined
# See the file runAllFuzzyJessTests.sh for these definitions
#
# This shell script was run under the cygwin system so some changes 
# may be needed to run uner other unix variants. In particular one
# might note that the -cp option for running java programs uses
# the ; to separate the list of directories and class/jar files 
# and may need to be changed to a :
#

echo "Running the fuzzy compiler test program ..."

echo ""
echo $JESS_JAR
echo $FUZZYJ_JAR
echo ""


java -cp "$JESS_JAR:$FUZZYJ_JAR" nrc.fuzzy.jess.FuzzyMain -nologo fuzzyCompiler.clp >fuzzyCompiler.out 2>&1

# Diff the output with the expected outout.
if diff --ignore-space-change  fuzzyCompiler.out fuzzyCompiler.ref > /dev/null ; then
    # files are the same
    echo "Fuzzy Compiler Test succeeded"
else
    # files are different
    echo "Fuzzy Compiler Test failed.  Try:"
    echo "  diff --ignore-space-change fuzzyCompiler.out fuzzyCompiler.ref"
fi


echo "Completed the fuzzy compiler test"
