rem
rem  I can't figure out the format of these file specs to get
rem  the classes to be stored in the jar file with just the
rem  directories of its package. The -C option seems to fail
rem  for the 1st file after it and then works correctly????
rem  So I just added the 1st one twice!!!???
rem  Also I broke into several pieces since I was getting 'buffer
rem  overflow' or some such message with 1 
rem           -C f:\fuzzyjtoolkit\bin\

d:\javasoft\jdk1.3\bin\jar -cf FuzzyShowerApplet.jar ^
         -C f:\fuzzyjtoolkit\bin\ ^
         f:\fuzzyjtoolkit\bin\examples\fuzzyshower\*.class ^
         f:\fuzzyjtoolkit\bin\examples\fuzzyshower\*.class ^
         f:\fuzzyjtoolkit\bin\java_cup\runtime\*.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\CUP$FuzzyParser$actions.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyParser.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRule.class ^
         -C f:\fuzzyjtoolkit\bin\ ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleExecutor.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleExecutorInterface.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyScanner.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet$MomentAndArea.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet$UITools.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySetException.class 

d:\javasoft\jdk1.3\bin\jar -uf FuzzyShowerApplet.jar ^
         -C f:\fuzzyjtoolkit\bin\ ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySetFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySetFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyValue.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyValueException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyValueVector.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyVariable.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyVariableException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\IncompatibleFuzzyValuesException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\IncompatibleRuleInputsException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\Interval.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\IntervalVector.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidDefuzzifyException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidFuzzyVariableNameException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidLinguisticExpressionException.class ^
         -C f:\fuzzyjtoolkit\bin\ ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidUODRangeException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidUODRangeException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LeftLinearFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LRFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\MamdaniMinMaxMinRuleExecutor.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\ModifierFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\Modifiers.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\Parameters.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\PIFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\RightLinearFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\RFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\SFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\SFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\SetPoint.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\TrapezoidFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\TriangleFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\XValueOutsideUODException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\XValuesOutOfOrderException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\YValueOutOfRangeException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\ZFunction.class 

d:\javasoft\jdk1.3\bin\jar -uf FuzzyShowerApplet.jar ^
         -C f:\SymantecClasses ^
         f:\SymantecClasses\symantec\itools\awt\AlignStyle.class ^
         f:\SymantecClasses\symantec\itools\awt\AlignStyle.class ^
         f:\SymantecClasses\symantec\itools\awt\BevelStyle.class ^
         f:\SymantecClasses\symantec\itools\awt\ButtonBase.class ^
         f:\SymantecClasses\symantec\itools\awt\ButtonBase$*.class ^
         f:\SymantecClasses\symantec\itools\awt\DirectionButton.class ^
         f:\SymantecClasses\symantec\itools\awt\DirectionButton$*.class ^
         f:\SymantecClasses\symantec\itools\awt\HorizontalSlider.class ^
         f:\SymantecClasses\symantec\itools\awt\HorizontalSlider$*.class ^
         f:\SymantecClasses\symantec\itools\awt\HorizontalSliderThumb*.class ^
         f:\SymantecClasses\symantec\itools\awt\ImagePanel.class ^
         f:\SymantecClasses\symantec\itools\awt\Label3D.class ^
         f:\SymantecClasses\symantec\itools\awt\Orientation.class ^
         f:\SymantecClasses\symantec\itools\awt\Slider.class ^
         f:\SymantecClasses\symantec\itools\awt\Slider$*.class ^
         f:\SymantecClasses\symantec\itools\awt\SliderTick.class ^
         f:\SymantecClasses\symantec\itools\awt\shape\Line.class ^
         f:\SymantecClasses\symantec\itools\awt\shape\Rect.class ^
         f:\SymantecClasses\symantec\itools\awt\shape\Shape.class ^
         f:\SymantecClasses\symantec\itools\awt\shape\VerticalLine.class ^
         f:\SymantecClasses\symantec\itools\awt\util\ColorUtils.class ^
         f:\SymantecClasses\symantec\itools\awt\util\spinner\NumericSpinner.class ^
         f:\SymantecClasses\symantec\itools\awt\util\spinner\SpinButtonPanel.class ^
         f:\SymantecClasses\symantec\itools\awt\util\spinner\SpinButtonPanel$Action.class ^
         f:\SymantecClasses\symantec\itools\awt\util\spinner\Spinner.class ^
         f:\SymantecClasses\symantec\itools\awt\util\spinner\Spinner$*.class ^
         f:\SymantecClasses\symantec\itools\beans\PropertyChangeSupport.class ^
         f:\SymantecClasses\symantec\itools\beans\VetoableChangeSupport.class ^
         f:\SymantecClasses\symantec\itools\lang\Context.class ^
         f:\SymantecClasses\symantec\itools\lang\OS.class ^
         f:\SymantecClasses\symantec\itools\net\RelativeURL.class ^
         f:\SymantecClasses\symantec\itools\resources\ErrorsBundle.class ^
         f:\SymantecClasses\symantec\itools\util\Timer.class ^
         f:\SymantecClasses\symantec\itools\util\GeneralUtils.class 


