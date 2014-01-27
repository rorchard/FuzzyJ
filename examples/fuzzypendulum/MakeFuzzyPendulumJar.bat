rem
rem  I can't figure out the format of these file specs to get
rem  the classes to be stored in the jar file with just the
rem  directories of its package. The -C option seems to fail
rem  for the 1st file after it and then works correctly????
rem  So I just added the 1st one twice!!!???
rem  Also I broke into 2 pieces since I was getting 'buffer
rem  overflow' or some such message with 1 
rem           -C f:\fuzzyjtoolkit\bin\

d:\javasoft\jdk1.3\bin\jar -cf FuzzyPendulum.jar ^
         -C f:\fuzzyjtoolkit\bin\ ^
         f:\fuzzyjtoolkit\bin\examples\fuzzypendulum\*.class ^
         f:\fuzzyjtoolkit\bin\examples\fuzzypendulum\*.class ^
         f:\fuzzyjtoolkit\bin\java_cup\runtime\*.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\CUP$FuzzyParser$actions.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyParser.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyParserSym.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRule.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleExecutor.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyRuleExecutorInterface.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzyScanner.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet$MomentAndArea.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet$UITools.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\FuzzySetException.class ^
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
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\InvalidUODRangeException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LeftLinearFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\LRFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\MamdaniMinMaxMinRuleExecutor.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\ModifierFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\Modifiers.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\Parameters.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\RFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\RightLinearFunction.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\SetPoint.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\TrapezoidFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\TriangleFuzzySet.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\XValueOutsideUODException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\XValuesOutOfOrderException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\YValueOutOfRangeException.class ^
         f:\fuzzyjtoolkit\bin\nrc\fuzzy\ZFunction.class 

d:\javasoft\jdk1.3\bin\jar -uf FuzzyPendulum.jar ^
         -C f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\ ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\beans\resources\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\beans\resources\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\beans\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\beans\*.gif ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\beans\resources\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\data\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\chart\resources\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\internal\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\legend\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\resources\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\legend\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\swing\*.class ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\klg\jclass\util\swing\icons\*.gif ^
         f:\fuzzyjtoolkit\examples\fuzzypendulum\fuzzypendulumutils\com\sun\java\util\collections\*.class

