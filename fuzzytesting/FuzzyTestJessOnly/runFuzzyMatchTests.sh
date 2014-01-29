#!/bin/bash
# *******************************************************************
# Run a set of Jess-based tests to test fuzzy-match
# *******************************************************************

# 
# Run the tests, and make sure we get the right output.
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

echo "Running the fuzzy-match test programs ..."

echo ""
echo $JESS_JAR
echo $FUZZYJ_JAR
echo ""


java -cp "$JESS_JAR:$FUZZYJ_JAR" nrc.fuzzy.jess.FuzzyMain -nologo TestFuzzy-match1.clp >TestFuzzy-match1.out 2>&1

# Diff the output with the expected outout.
if diff --ignore-space-change   TestFuzzy-match1.out TestFuzzy-match1.ref > /dev/null ; then
    # files are the same
    echo "TestFuzzy-match1 succeeded"
else
    # files are different
    echo "TestFuzzy-match1 Test failed.  Try:"
    echo "  diff --ignore-space-change TestFuzzy-match1.out TestFuzzy-match1.ref"
fi

java -cp "$JESS_JAR:$FUZZYJ_JAR" nrc.fuzzy.jess.FuzzyMain -nologo TestFuzzy-match2.clp >TestFuzzy-match2.out 2>&1

# Diff the output with the expected outout.
if diff --ignore-space-change   TestFuzzy-match2.out TestFuzzy-match2.ref > /dev/null ; then
    # files are the same
    echo "TestFuzzy-match2 succeeded"
else
    # files are different
    echo "TestFuzzy-match2 Test failed.  Try:"
    echo "  diff --ignore-space-change TestFuzzy-match1.out TestFuzzy-match1.ref"
fi

java -cp "$JESS_JAR:$FUZZYJ_JAR" nrc.fuzzy.jess.FuzzyMain -nologo TestFuzzy-match3.clp >TestFuzzy-match3.out 2>&1

# Diff the output with the expected outout.
if diff --ignore-space-change   TestFuzzy-match3.out TestFuzzy-match3.ref > /dev/null ; then
    # files are the same
    echo "TestFuzzy-match3 succeeded"
else
    # files are different
    echo "TestFuzzy-match3 Test failed.  Try:"
    echo "  diff --ignore-space-change TestFuzzy-match1.out TestFuzzy-match1.ref"
fi

java -cp "$JESS_JAR:$FUZZYJ_JAR" nrc.fuzzy.jess.FuzzyMain -nologo TestFuzzy-match4.clp >TestFuzzy-match4.out 2>&1

# Diff the output with the expected outout.
if diff --ignore-space-change   TestFuzzy-match4.out TestFuzzy-match4.ref > /dev/null ; then
    # files are the same
    echo "TestFuzzy-match4 succeeded"
else
    # files are different
    echo "TestFuzzy-match4 Test failed.  Try:"
    echo "  diff --ignore-space-change TestFuzzy-match1.out TestFuzzy-match1.ref"
fi

echo "Completed the fuzzy-match tests"
