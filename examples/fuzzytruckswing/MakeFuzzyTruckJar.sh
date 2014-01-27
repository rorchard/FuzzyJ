#! /bin/bash
#  Create an executable jar file for the FuzzyTruck demo program that has
#  only the required class files from the demo and the required supporting libraries
#  and other files
#
#  Supporting libraries are:
#
#			nrc.fuzzy
#			javacup.runtime
#
#  Other files need for the execution are:
#
#			none
#
#  Following directory assignments will need to be customized ... assumes we are in directory
#  with the FuzzyTruck example
#  
#  NOTE: all directories must be specified relative to the current working directory ... i.e. where
#        this .sh file is located and run from (normally examples/fuzzytruckswing)

FuzzyTruckExampleDirectory=../../  # expects 'examples' directory to be here with the class files in examples/fuzzytruckswing
JavacupDirectory=../../             # expects be where the java_cup directory is with class files in sub-directories
fuzzyJDirectory=../../              # expects the 'nrc' directory to be here with all fuzzyF/fuzzyJess classes in subdirectories

BFD=`pwd`    # remember the batch file directory --- where this was executed from
cd $BFD/$FuzzyTruckExampleDirectory

# put the FuzzyTruck classes and files into the executable jar file
jar -cfe $BFD/FuzzyTruckJAppletNew.jar examples.fuzzytruckswing.FuzzyTruckJApplet \
        examples/fuzzytruckswing/*.class 

#add java cup runtime classes
cd $BFD/$JavacupDirectory
jar -uf $BFD/FuzzyTruckJAppletNew.jar java_cup/runtime/*.class

#add the FuzzyJ and FuzzyJess classes that are needed for the example
cd $BFD/$fuzzyJDirectory
jar -uf $BFD/FuzzyTruckJAppletNew.jar \
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
         nrc/fuzzy/LarsenProductMaxMinRuleExecutor.class \
         nrc/fuzzy/LeftLinearFunction.class \
         nrc/fuzzy/LFuzzySet.class \
         nrc/fuzzy/LRFuzzySet.class \
         nrc/fuzzy/MamdaniMinMaxMinRuleExecutor.class \
         nrc/fuzzy/ModifierFunction.class \
         nrc/fuzzy/Modifiers.class \
         nrc/fuzzy/NoXValueForMembershipException.class \
         nrc/fuzzy/ProductAntecedentCombineOperator.class \
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
