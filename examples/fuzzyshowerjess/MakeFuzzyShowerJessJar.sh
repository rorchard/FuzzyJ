#! /bin/bash
#  Create an executable jar file for the FuzzyShower demo program that has
#  only the required class files from the demo and the required supporting libraries
#  and other files
#
#  Supporting libraries are:
#
#			nrc.fuzzy
#			nrc.fuzzy.jess
#			symantec.itools
#			jess
#			javacup.runtime
#
#  Other files need for the execution are:
#
#			FuzzyShowerJess.clp
#			shower.gif
#
#  In this example we actually include all of the jess classes, but an analysis could reduce 
#  the size of this jar file by including only the required jes classes ... might be a
#  difficult analysis
#
#  Following directory assignments will need to be customized ... assumes we are in directory
#  with the FuzzyShowerJess example
#  
#  NOTE: all directories must be specified relative to the current working directory ... i.e. where
#        this .bat file is located and run from

FuzzyShowerExampleDirectory=../../  # expects 'examples' directory to be here with the class files, shower.gif and FuzzyShowerJess.clp in examples/fuzzyshowerjess
jessDirectory=../../                # expects jess.jar file to be here
JavacupDirectory=../../             # expects be where the java_cup directory is with class files in sub-directories
SymantecItoolsDirectory=../../      # expects symbeans.jar file to be here
fuzzyJDirectory=../../              # expects the 'nrc' directory to be here with all fuzzyF/fuzzyJess classes in subdirectories

BFD=`pwd`    # remember the batch file directory --- where this was executed from
cd $BFD/$FuzzyShowerExampleDirectory

# put the FuzzyShowerJess classes and files into the executable jar file
jar -cfe $BFD/FuzzyShowerJessAppletNew.jar examples.fuzzyshowerjess.ShowerFrameJess \
        examples/fuzzyshowerjess/*.clp \
        examples/fuzzyshowerjess/*.class \
        examples/fuzzyshowerjess/*.gif 
        
jar -uf $BFD/FuzzyShowerJessAppletNew.jar \
        -C examples/fuzzyshowerjess/ FuzzyShowerJess.clp
        
# add the Jess classes required for this example
cd $BFD/$jessDirectory
if [ -e jess ]; then echo ; else jar -xf jess.jar; fi
jar -uf $BFD/FuzzyShowerJessAppletNew.jar \
         jess/*.class \
	 jess/*.clp \
         jess/*/*.class
         
if [ -e jess ] & [ -e jess.jar ]; then rm -fr jess; fi
         

#add java cup runtime classes
cd $BFD/$JavacupDirectory
jar -uf $BFD/FuzzyShowerJessAppletNew.jar java_cup/runtime/*.class

#add the FuzzyJ and FuzzyJess classes that are needed for the example
cd $BFD/$fuzzyJDirectory
jar -uf $BFD/FuzzyShowerJessAppletNew.jar \
         nrc/fuzzy/CUP*.class \
	 nrc/fuzzy/AntecedentCombineOperator.class \
	 nrc/fuzzy/AntecedentCombineOperatorInterface.class \
	 nrc/fuzzy/CompensatoryAndAntecedentCombineOperator.class \
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
         nrc/fuzzy/FuzzySet.class \
         nrc/fuzzy/FuzzySetException.class \
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
         nrc/fuzzy/InvalidFuzzyVariableTermNameException.class \
         nrc/fuzzy/InvalidLinguisticExpressionException.class \
         nrc/fuzzy/InvalidUODRangeException.class \
         nrc/fuzzy/LarsenProductMaxMinRuleExecutor.class \
         nrc/fuzzy/LeftLinearFunction.class \
         nrc/fuzzy/LFuzzySet.class \
         nrc/fuzzy/LRFuzzySet.class \
         nrc/fuzzy/MamdaniMinMaxMinRuleExecutor.class \
	 nrc/fuzzy/MinimumAntecedentCombineOperator.class \
         nrc/fuzzy/ModifierFunction.class \
         nrc/fuzzy/Modifiers.class \
         nrc/fuzzy/NoXValueForMembershipException.class \
         nrc/fuzzy/Parameters.class \
         nrc/fuzzy/PIFuzzySet.class \
	 nrc/fuzzy/ProductAntecedentCombineOperator.class \
         nrc/fuzzy/RightLinearFunction.class \
         nrc/fuzzy/RFuzzySet.class \
         nrc/fuzzy/SFunction.class \
         nrc/fuzzy/SFuzzySet.class \
         nrc/fuzzy/SetPoint.class \
         nrc/fuzzy/TrapezoidFuzzySet.class \
	 nrc/fuzzy/TsukamotoRuleExecutor.class \
         nrc/fuzzy/Similarity*.class \
         nrc/fuzzy/TriangleFuzzySet.class \
         nrc/fuzzy/XValueOutsideUODException.class \
         nrc/fuzzy/XValuesOutOfOrderException.class \
         nrc/fuzzy/YValueOutOfRangeException.class \
         nrc/fuzzy/ZFunction.class \
         nrc/fuzzy/jess/*.class

# add the Symantec itools classes required for this example
cd $BFD/$SymantecItoolsDirectory
if [ -e symantec ]; then echo ; else jar -xf symbeans.jar; fi
jar -uf $BFD/FuzzyShowerJessAppletNew.jar \
         symantec/itools/awt/AlignStyle.class \
         symantec/itools/awt/AlignStyle.class \
         symantec/itools/awt/BevelStyle.class \
         symantec/itools/awt/ButtonBase.class \
         symantec/itools/awt/ButtonBase*.class \
         symantec/itools/awt/DirectionButton.class \
         symantec/itools/awt/DirectionButton*.class \
         symantec/itools/awt/HorizontalSlider.class \
         symantec/itools/awt/HorizontalSlider*.class \
         symantec/itools/awt/HorizontalSliderThumb*.class \
         symantec/itools/awt/ImagePanel.class \
         symantec/itools/awt/Label3D.class \
         symantec/itools/awt/Orientation.class \
         symantec/itools/awt/Slider.class \
         symantec/itools/awt/Slider*.class \
         symantec/itools/awt/SliderTick.class \
         symantec/itools/awt/shape/Line.class \
         symantec/itools/awt/shape/Rect.class \
         symantec/itools/awt/shape/Shape.class \
         symantec/itools/awt/shape/VerticalLine.class \
         symantec/itools/awt/util/ColorUtils.class \
         symantec/itools/awt/util/spinner/NumericSpinner.class \
         symantec/itools/awt/util/spinner/SpinButtonPanel.class \
         symantec/itools/awt/util/spinner/SpinButtonPanel*Action.class \
         symantec/itools/awt/util/spinner/Spinner.class \
         symantec/itools/awt/util/spinner/Spinner*.class \
         symantec/itools/beans/PropertyChangeSupport.class \
         symantec/itools/beans/VetoableChangeSupport.class \
         symantec/itools/lang/Context.class \
         symantec/itools/lang/OS.class \
         symantec/itools/net/RelativeURL.class \
         symantec/itools/resources/ErrorsBundle.class \
         symantec/itools/util/Timer.class \
         symantec/itools/util/GeneralUtils.class 

if [ -e symantec ] & [ -e symbeans.jar ]; then rm -fr symantec; fi
