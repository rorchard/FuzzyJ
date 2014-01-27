#! /bin/bash
#  Create an executable jar file for the FuzzyPendulum demo program that has
#  only the required class files from the demo and the required supporting libraries
#  and other files
#
#  Supporting libraries are:
#
#			nrc.fuzzy
#			com.klg.jclass (2 libraries -- jctable451.jar, jcchart451.jar)
#			javacup.runtime
#
#  Other files need for the execution are:
#
#			none
#
#  Following directory assignments will need to be customized ... assumes we are in directory
#  with the FuzzyPendulum example
#  
#  NOTE: all directories must be specified relative to the current working directory ... i.e. where
#        this .sh file is located and run from (normally examples/fuzzypendulum)

FuzzyPendulumExampleDirectory=../../  # expects 'examples' directory to be here with the class files, shower.gif and in examples/fuzzypendulum
JavacupDirectory=../../               # expects be where the java_cup directory is with class files in sub-directories
FuzzyPendulumUtilsDirectory=          # expects com.klg.jclass.chart and com.klg.jclass.table and the com.sun.java.utils.collections 
                                      # classes to be here in file FuzzyPendulumUtil.jar file 
fuzzyJDirectory=../../                # expects the 'nrc' directory to be here with all fuzzyJ/fuzzyJess classes in subdirectories

BFD=`pwd`    # remember the batch file directory --- where this was executed from
cd $BFD/$FuzzyPendulumExampleDirectory

# put the FuzzyPendulum classes and files into the executable jar file
jar -cfe $BFD/FuzzyPendulumJAppletNew.jar examples.fuzzypendulum.FuzzyPendulumJApplet \
        examples/fuzzypendulum/*.class

#add java cup runtime classes
cd $BFD/$JavacupDirectory
jar -uf $BFD/FuzzyPendulumJAppletNew.jar java_cup/runtime/*.class

#add the FuzzyJ and FuzzyJess classes that are needed for the example
cd $BFD/$fuzzyJDirectory
jar -uf $BFD/FuzzyPendulumJAppletNew.jar \
		 nrc/fuzzy/Antecedent*.class \
		 nrc/fuzzy/MinimumAntecedent*.class \
		 nrc/fuzzy/InvalidFuzzyVariable*.class \
         nrc/fuzzy/CUP*.class \
         nrc/fuzzy/FuzzyException.class \
         nrc/fuzzy/FuzzyParser.class \
         nrc/fuzzy/FuzzyRule.class \
         nrc/fuzzy/FuzzyRuleException.class \
         nrc/fuzzy/FuzzyRuleException.class \
         nrc/fuzzy/FuzzyRuleExecutor.class \
         nrc/fuzzy/FuzzyRuleExecutorInterface.class \
         nrc/fuzzy/FuzzyScanner.class \
         nrc/fuzzy/FuzzySet*MomentAndArea.class \
         nrc/fuzzy/FuzzySet*UITools.class \
         nrc/fuzzy/FuzzySet*.class \
         nrc/fuzzy/FuzzySetException.class \
         nrc/fuzzy/FuzzySetFunction.class \
         nrc/fuzzy/FuzzySetFunction.class \
         nrc/fuzzy/FuzzyValue.class \
         nrc/fuzzy/FuzzyValueException.class \
         nrc/fuzzy/FuzzyValueVector.class \
         nrc/fuzzy/FuzzyVariable.class \
         nrc/fuzzy/FuzzyVariableException.class \
         nrc/fuzzy/IncompatibleFuzzyValuesException.class \
         nrc/fuzzy/IncompatibleRuleInputsException.class \
         nrc/fuzzy/Interval.class \
         nrc/fuzzy/IntervalVector.class \
         nrc/fuzzy/InvalidDefuzzifyException.class \
         nrc/fuzzy/InvalidFuzzyVariableNameException.class \
         nrc/fuzzy/InvalidLinguisticExpressionException.class \
         nrc/fuzzy/InvalidUODRangeException.class \
         nrc/fuzzy/InvalidUODRangeException.class \
         nrc/fuzzy/LeftLinearFunction.class \
         nrc/fuzzy/LFuzzySet.class \
         nrc/fuzzy/LRFuzzySet.class \
         nrc/fuzzy/MamdaniMinMaxMinRuleExecutor.class \
         nrc/fuzzy/ModifierFunction.class \
         nrc/fuzzy/Modifiers.class \
         nrc/fuzzy/NoXValueForMembershipException.class \
         nrc/fuzzy/Parameters.class \
         nrc/fuzzy/PIFuzzySet.class \
         nrc/fuzzy/RightLinearFunction.class \
         nrc/fuzzy/RFuzzySet.class \
         nrc/fuzzy/SFunction.class \
         nrc/fuzzy/SFuzzySet.class \
         nrc/fuzzy/SetPoint.class \
         nrc/fuzzy/TrapezoidFuzzySet.class \
         nrc/fuzzy/Similarity*.class \
         nrc/fuzzy/TriangleFuzzySet.class \
         nrc/fuzzy/XValueOutsideUODException.class \
         nrc/fuzzy/XValuesOutOfOrderException.class \
         nrc/fuzzy/YValueOutOfRangeException.class \
         nrc/fuzzy/ZFunction.class 

# add the JClass (jcchart, jctable) classes and Sun collections classes required for this example
cd $BFD/$FuzzyPendulumUtilsDirectory
if [ -e com/klg ]; then echo ; else jar -xf FuzzyPendulumUtil.jar; fi

jar -uf $BFD/FuzzyPendulumJAppletNew.jar \
         com/klg/jclass/beans/resources/*.class \
         com/klg/jclass/beans/resources/*.class \
         com/klg/jclass/chart/*.class \
         com/klg/jclass/chart/beans/*.class \
         com/klg/jclass/chart/beans/*.gif \
         com/klg/jclass/chart/beans/resources/*.class \
         com/klg/jclass/chart/data/*.class \
         com/klg/jclass/chart/resources/*.class \
         com/klg/jclass/util/*.class \
         com/klg/jclass/util/internal/*.class \
         com/klg/jclass/util/legend/*.class \
         com/klg/jclass/util/resources/*.class \
         com/klg/jclass/util/legend/*.class \
         com/klg/jclass/util/swing/*.class \
         com/klg/jclass/util/swing/icons/*.gif \
         com/sun/java/util/collections/*.class

if [ -e com/klg ] & [ -e FuzzyPendulumUtil.jar ]; then rm -fr com; fi

