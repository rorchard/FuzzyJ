/*
	Test some FuzzyJ objects and methods ...
 */
package fuzzytesting.fuzzytest;

import nrc.fuzzy.*;
import java.util.*;


public class FuzzyTest 
{
	public FuzzyTest()
	{
	}

	static public void main(String args[])
	{
		double xHot[] = {25, 35};
		double yHot[] = {0, 1};
		double xCold[] = {5, 15};
		double yCold[] = {1, 0};
		double x[] = {1, 2, 3};
		double y[] = {0, 1, 0};
		String mods[];
		int i;
	    FuzzyValue fval = null;
	    FuzzyValue fval3 = null;

		try
		  { 
		    FuzzySet fsHot = new FuzzySet(xHot, yHot, 2);
		    FuzzySet fsCold = new FuzzySet(xCold, yCold, 2);
		    FuzzySet fSets[] = {fsHot, fsCold};
		    String fTerms[] = {"hot", "cold"};
            FuzzyValue fvals[] = new FuzzyValue[2];
            FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C", fTerms, fSets, 2);
		    FuzzyVariable temp2 = new FuzzyVariable("temp2", 0, 100, "C");
		    FuzzyVariable temp3 = new FuzzyVariable("temp3", 5, 65, "C");
		    System.out.println("3 FuzzyVariables before any terms added using 'addTerm method'");
		    System.out.println(temp);
		    System.out.println(temp2);
		    System.out.println(temp3);
		    
		    /******/
		    double t1y[] = {0.0, 1.0, 0.0 };
		    double t1x[] = {41.294, 41.344, 41.394 };
		    double t2y[] = {0.0, 0.033546, 0.1233, 0.28 };
		    double t2x[] = {37.0, 39.884, 42.7549, 45.62 };
		    FuzzyValue t1 = new FuzzyValue(temp3, t1x, t1y, 3);
		    FuzzyValue t2 = new FuzzyValue(temp3, t2x, t2y, 4);
		    double max1 = t1.maximumOfIntersection(t2);
		    double max2 = t2.maximumOfIntersection(t1);
		    System.out.println("");
		    System.out.println("Find the max of intersection of the following 2 FuzzyValues, t1 and t2");
		    System.out.println("t1 = " + t1);
		    System.out.println("t2 = " + t2);
		    System.out.println("t1.max.t2 = " + max1);
		    System.out.println("t2.max.t1 = " + max2);
		    if (Math.abs(max1-max2) > 0.000000000001)
		    {	System.out.println("******************");
		        System.out.println("Expecting max1 and max2 to be equal within some tolerance");
		        System.out.println("******************");
		    }
		    System.out.println("");
		    /******/
			
		   // temp.addTerm("hot", xHot, yHot, 2);
		   // temp.addTerm("cold", xCold, yCold, 2);
		    temp.addTerm("veryHot", "very hot");
		    temp.addTerm("medium", "(not hot and (not cold))");
		    temp2.addTerm("hot", xHot, yHot, 2);
		    temp2.addTerm("cold", xCold, yCold, 2);
		    temp2.addTerm("veryHot", "very hot");
		    temp2.addTerm("medium", "(not hot and (not cold))");
		    System.out.println("3 FuzzyVariables after terms added for 2 of them");
		    System.out.println(temp);
		    System.out.println(temp2);
		    System.out.println(temp3);
		    System.out.println("");
		    		    
            FuzzyVariable pressure = new FuzzyVariable("pressure", 0, 10, "kilo-pascals");
		    pressure.addTerm("low", new ZFuzzySet(2.0, 5.0));
		    pressure.addTerm("medium", new PIFuzzySet(5.0, 2.5));
		    pressure.addTerm("high", new SFuzzySet(5.0, 8.0));
		    System.out.println("");
		    System.out.println("FuzzyVariable pressure");
		    System.out.println(pressure);
		    mods = Modifiers.getModifierNames();
		    System.out.println("");
		    System.out.println("All of the defined Modifiers:");
		    for (i=0; i < mods.length; i++)
		       System.out.println("\t" + mods[i]);

		    System.out.println("");
		    fval = new FuzzyValue(temp, x, y, 3);
		    System.out.println("Use these modifiers on the FuzzyVariable: \n" + fval);
		    System.out.println("");
		    System.out.println(fval.toString());
		    System.out.println("(no modifier)Value at x=1.25 is :" + fval.getMembership(1.25));
		    FuzzyValue fval2 = Modifiers.not(fval);
		    System.out.println(fval2);
		    System.out.println("(NOT modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));
		    fval2 = Modifiers.very(fval);
		    System.out.println(fval2);
		    System.out.println("(VERY modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));
		    fval2 = Modifiers.extremely(fval);
		    System.out.println(fval2);
		    System.out.println("(EXTREMELY modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.somewhat(fval);
		    System.out.println(fval2);
		    System.out.println("(SOMEWHAT modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.more_or_less(fval);
		    System.out.println(fval2);
		    System.out.println("(MORE_OR_LESS modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.call("More_or_Less", fval);
		    System.out.println(fval2);
		    System.out.println("(MORE_OR_LESS modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.plus(fval);
		    System.out.println(fval2);
		    System.out.println("(PLUS modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.norm(fval);
		    System.out.println(fval2);
		    System.out.println("(NORM modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = Modifiers.slightly(fval);
		    System.out.println(fval2);
		    System.out.println("(SLIGHTLY modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fvals[0] = fval;
		    fvals[1] = fval2;
		    System.out.println();
		    System.out.println(FuzzyValue.plotFuzzyValues(".+", 1, 3, fvals));
		    System.out.println("maxmin of slightly fval and fval = " + fval.maximumOfIntersection(fval2));
		    System.out.println("match of slightly fval and fval at threshold 0.8 = " + fval.fuzzyMatch(fval2, 0.8));
		    System.out.println("match of slightly fval and fval at threshold 0.75 = " + fval.fuzzyMatch(fval2, 0.75));
		    Interval.setToStringPrecision(5);
		    System.out.println();
		    System.out.println("Support of 'slightly' fval is: " + fval2.getSupport());
		    System.out.println("Weak Alpha cut at 0.0 is: " + fval2.getAlphaCut(Parameters.WEAK, 0.0));
		    System.out.println("Weak Alpha cut at 0.52 is: " + fval2.getAlphaCut(Parameters.WEAK, 0.52));
		    System.out.println("Strong Alpha cut at 0.52 is: " + fval2.getAlphaCut(Parameters.STRONG, 0.52));

		    fval2 = Modifiers.intensify(fval);
		    System.out.println();
		    System.out.println(fval2);
		    System.out.println("(INTENSIFY modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fvals[0] = fval;
		    fvals[1] = fval2;
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 5, fvals));

		    fval2 = Modifiers.above(fval);
		    System.out.println();
		    System.out.println(fval2);
		    System.out.println("(ABOVE modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));
		    fval2 = Modifiers.below(fval);
		    System.out.println(fval2);
		    System.out.println("(BELOW modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval2 = (new VeryModifier()).call(fval);
		    System.out.println(fval2);
		    System.out.println("(VERY modifier) Value at x=1.25 is :" + fval2.getMembership(1.25));

		    fval = new FuzzyValue(temp, "very hot or cold");
		    fval3 = new FuzzyValue(temp, "medium");
		    System.out.println("Membership value at 27.5 of 'very hot or cold' is " + fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+", 0, 40));
		    fvals[0] = fval;
		    fvals[1] = fval3;
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 40, fvals));

		    fval = fval.fuzzyUnion(fval2);
		    System.out.println(fval);
		    System.out.println("Membership value at 27.5 of union of 'very hot or cold' and 'medium' is " + fval.getMembership(27.5));

		    fval = new FuzzyValue(temp, new PIFuzzySet(50, 10, 11));
		    System.out.println();
		    System.out.println();
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval.plotFuzzyValue("+", 30, 70));

		    fval = new FuzzyValue(temp, new ZFuzzySet(10.0, 50.0, 11));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("Membership value at 27.5 is " + fval.getMembership(27.5));
		    System.out.println(fval.plotFuzzyValue("+",0,60));

		    fval = new FuzzyValue(temp, new RectangleFuzzySet(10.0, 50.0));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("Membership value at 9.999 is " + fval.getMembership(9.999));
		    System.out.println("Membership value at 10 is " + fval.getMembership(10));
		    System.out.println("Membership value at 27.5 is " + fval.getMembership(27.5));
		    System.out.println("Membership value at 50 is " + fval.getMembership(50));
		    System.out.println(fval.plotFuzzyValue("+",5,55));
		    System.out.println("Moment defuzzify is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment defuzzify is " + fval.momentDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify());

		    fval = new FuzzyValue(temp, new TrapezoidFuzzySet(10.0, 15, 30, 50.0));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval.plotFuzzyValue("+",5,55));
		    System.out.println("Moment defuzzify is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment defuzzify is " + fval.momentDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify());

		    fval = new FuzzyValue(temp, new LRFuzzySet(10.0, 15, 30, 50.0,
		                          new SFunction(), new ZFunction()));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("Membership at 27.5 is " + fval.getMembership(27.5));
		    System.out.println(fval.plotFuzzyValue("+",10,50));

		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    fval = new FuzzyValue(temp, new PIFuzzySet(1, 5));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println(new PIFuzzySet(1, 5));
		    System.out.println(fval.plotFuzzyValue("+",0,10));
		    FuzzyValue.setConfineFuzzySetsToUOD(false);
		    
		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    fval = new FuzzyValue(temp, new PIFuzzySet(99, 5));
		    System.out.println();
		    System.out.println(fval);
		    System.out.println(new PIFuzzySet(99, 5));
		    System.out.println(fval.plotFuzzyValue("+",90,100));
		    System.out.println("Moment is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment is " + fval.momentDefuzzify());
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());
		    FuzzyValue.setConfineFuzzySetsToUOD(false);
		    System.out.println();
		    System.out.println("Plot horizontalIntersction, horizontalUnion and fuzzyScale at 0.75");
		    System.out.println(fval.horizontalIntersection(.75).plotFuzzyValue("+",90,100));
		    System.out.println(fval.horizontalUnion(.75).plotFuzzyValue("+",90,100));
		    System.out.println(fval.fuzzyScale(.75).plotFuzzyValue("+",90,100));

		    Interval.setToStringPrecision(5);
		    System.out.println();
		    System.out.println("Support is: " + fval.getSupport());
		    System.out.println("Weak Alpha cut at 0.0 is: " + fval.getAlphaCut(Parameters.WEAK, 0.0));
		    System.out.println("Weak Alpha cut at 0.52 is: " + fval.getAlphaCut(Parameters.WEAK, 0.52));

            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "cold");
		    System.out.println();
            System.out.println("non intersection of hot and cold is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "cold");
		    System.out.println();
            System.out.println("NO intersection test of hot and cold is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "medium");
		    System.out.println();
            System.out.println("non intersection test of hot and medium is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "medium");
		    System.out.println();
            System.out.println("NO intersection test of hot and medium is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            Thread.sleep(500);
		    System.out.println();
            System.out.println("Starting loop 1"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 2"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 3"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.maximumOfIntersection(fval2);
            System.out.println("Ending all loops");System.out.flush();

            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "not hot");
		    System.out.println();
            System.out.println("non intersection test of hot and not hot is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "not hot");
            System.out.println("NO intersection test of hot and not hot is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, new SFuzzySet(30.0, 40.0));
            fval2 = new FuzzyValue(temp, new ZFuzzySet(10.0, 30.0));
            System.out.println("non intersection of S30-40 and Z10-30 is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, new SFuzzySet(30.0, 40.0));
            fval2 = new FuzzyValue(temp, new ZFuzzySet(10.0, 30.0));
            System.out.println("NO intersection of S30-40 and Z10-30 is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, new RectangleFuzzySet(30.0, 40.0));
            fval2 = new FuzzyValue(temp, new RectangleFuzzySet(10.0, 30.0));
            System.out.println("non intersection of RectangleFuzzySet30-40 and RectangleFuzzySet10-30 is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, new RectangleFuzzySet(30.0, 40.0));
            fval2 = new FuzzyValue(temp, new RectangleFuzzySet(10.0, 30.0));
            System.out.println("NO intersection of RectangleFuzzySet30-40 and RectangleFuzzySet10-30 is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            Thread.sleep(500);
            System.out.println("Starting loop 1"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 2"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 3"); System.out.flush();
            for (int jj=0; jj<1000; jj++)
               fval.maximumOfIntersection(fval2);
            System.out.println("Ending all loops");System.out.flush();
            
            // let's try some rules --- 
            FuzzyRule rule1 = new FuzzyRule();
            fval = new FuzzyValue( temp, "hot");
            fval2 = new FuzzyValue( pressure, "low or medium");
            fval3 = new FuzzyValue( temp, "medium");
		    System.out.println();
		    System.out.println("Medium is ... " + fval3);
            fval3 = new FuzzyValue( temp, "very medium");
		    System.out.println("Very medium is ... " + fval3);
		    fvals[0] = fval;
		    fvals[1] = fval3;
            rule1.addAntecedent(fval);
            rule1.addConclusion(fval2);
            rule1.addInput(fval3);
		    System.out.println("Linguistic value of temp term \"medium\" is: " + temp.findTerm("medium").getLinguisticExpression());
		    System.out.println("Linguistic value of Rule antecedent 0 is: " + rule1.antecedentAt(0).getLinguisticExpression());
            FuzzyValueVector fvv = rule1.execute();
		    System.out.println("Linguistic value of Rule antecedent 0 (after rule fires) is: " + rule1.antecedentAt(0).getLinguisticExpression());
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 50, fvals));
		    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
		    System.out.println(fval2.plotFuzzyValue("*", 0, 10));
		    System.out.println("Start firing rule many times with LarsenProductMaxMinRuleExecutor ... ");
		    for (i=0; i<500; i++)
              fvv = rule1.execute(new LarsenProductMaxMinRuleExecutor());
		    System.out.println("Completed firing of rule many times with LarsenProductMaxMinRuleExecutor ... ");
		    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
		    
		    System.out.println("Firing rule with Tsukamoto rule executor ... ");
            fvv = rule1.execute(new TsukamotoRuleExecutor());
		    System.out.println("Output FuzzySet result with Tsukamoto rule executor ... " + fvv.fuzzyValueAt(0));
		    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
		    
		    System.out.println("\n******** SimilarityByPossibility *****************");
		    FuzzyValue.setDefaultSimilarityOperator(new SimilarityByPossibilityOperator());
		    System.out.println("\nSimilarity of 'temp very medium' and 'temp hot' is: " +
                    fval.similarity(fval3));
		    System.out.println("\nSimilarity of 'temp very medium' and 'temp very medium' is (should be 1.0): " +
                    fval3.similarity(fval3));
		    System.out.println("\nSimilarity of 'temp hot' and 'temp hot' is (should be 1.0): " +
                    fval.similarity(fval));
		    System.out.println("\nSimilarity of 'pressure low or medium' and 'pressure low or medium' is (should be 1.0): " +
                    fval2.similarity(fval2));
		    
	    	FuzzyVariable fVarX = new FuzzyVariable("Temporal Association", 0, 1);          
		    FuzzyValue fValX = new FuzzyValue(fVarX, new TriangleFuzzySet(0,0,0.5));

		    System.out.println();
		    System.out.println(fValX.plotFuzzyValue("*", 0, 1));
	    	FuzzyValue fValXcomp = fValX.fuzzyComplement();
		    System.out.println(fValXcomp.plotFuzzyValue("*", 0, 1));
	    	System.out.println("\n Similarity between two fuzzy values (should be 1.0): " + fValX.similarity(fValX));
	    	System.out.println("\npossibility = " + fValX.maximumOfIntersection(fValX));
	    	System.out.println("\nnecessity = " + (1.0 - fValXcomp.maximumOfIntersection(fValX)));
	    	
		    System.out.println();
	    	fVarX = new FuzzyVariable("Bug test2", 0, 1);          
		    fValX = new FuzzyValue(fVarX, new RectangleFuzzySet(0.1,0.3));

		    System.out.println(fValX.plotFuzzyValue("*", 0, 1));
	    	fValXcomp = fValX.fuzzyComplement();
		    System.out.println(fValXcomp.plotFuzzyValue("*", 0, 1));
		    System.out.println(fValXcomp.fuzzyIntersection(fValX).plotFuzzyValue("*", 0, 1));
	    	System.out.println("\n Similarity between two fuzzy values (should be 1.0): " + fValX.similarity(fValX));
	    	System.out.println("\npossibility = " + fValX.maximumOfIntersection(fValX));
	    	System.out.println("\nnecessity = " + (1.0 - fValXcomp.maximumOfIntersection(fValX)));
	    	
	    	FuzzyValue fval11 = new FuzzyValue(fVarX, new LeftLinearFuzzySet(0.499999, 0.500001)), 
	    			   fval22 = new FuzzyValue(fVarX, new RightLinearFuzzySet(0.499999, 0.500001));
	    	System.out.println("\n Similarity between two fuzzy values (should be close to 0.0??): " + fval11.similarity(fval22));
	    	fval11 = new FuzzyValue(fVarX, new LeftLinearFuzzySet(0.5, 0.5)); 
	    	fval22 = new FuzzyValue(fVarX, new RightLinearFuzzySet(0.5, 0.5));
	    	System.out.println("\n Similarity between two fuzzy values (should be exactly 0.0): " + fval11.similarity(fval22));
		    System.out.println("\n****************************************");
		  		        
		    System.out.println("\n\n******repeat with similarityByArea************");
		    FuzzyValue.setDefaultSimilarityOperator(new SimilarityByAreaOperator());
		    System.out.println("\nSimilarity of 'temp very medium' and 'temp hot' is: " +
                    fval.similarity(fval3));
		    System.out.println("\nSimilarity of 'temp very medium' and 'temp very medium' is (should be 1.0): " +
                    fval3.similarity(fval3));
		    System.out.println("\nSimilarity of 'temp hot' and 'temp hot' is (should be 1.0): " +
                    fval.similarity(fval));
		    System.out.println("\nSimilarity of 'pressure low or medium' and 'pressure low or medium' is (should be 1.0): " +
                    fval2.similarity(fval2));
		    
	    	fVarX = new FuzzyVariable("Temporal Association", 0, 1);          
		    fValX = new FuzzyValue(fVarX, new TriangleFuzzySet(0,0,0.5));

		    System.out.println(fValX.plotFuzzyValue("*", 0, 1));
	    	fValXcomp = fValX.fuzzyComplement();
		    System.out.println(fValXcomp.plotFuzzyValue("*", 0, 1));
	    	System.out.println("\n Similarity between two fuzzy values (should be 1.0): " + fValX.similarity(fValX));
	    	
	    	fVarX = new FuzzyVariable("Bug test2", 0, 1);          
		    fValX = new FuzzyValue(fVarX, new RectangleFuzzySet(0.1,0.3));

		    System.out.println(fValX.plotFuzzyValue("*", 0, 1));
	    	fValXcomp = fValX.fuzzyComplement();
		    System.out.println(fValXcomp.plotFuzzyValue("*", 0, 1));
		    System.out.println(fValXcomp.fuzzyIntersection(fValX).plotFuzzyValue("*", 0, 1));
	    	System.out.println("\n Similarity between two fuzzy values (should be 1.0): " + fValX.similarity(fValX));
	    	
	    	fval11 = new FuzzyValue(fVarX, new LeftLinearFuzzySet(0.499999, 0.500001)); 
	    	fval22 = new FuzzyValue(fVarX, new RightLinearFuzzySet(0.499999, 0.500001));
	    	System.out.println("\n Similarity between two fuzzy values (should be close to 0.0??): " + fval11.similarity(fval22));
	    	fval11 = new FuzzyValue(fVarX, new LeftLinearFuzzySet(0.5, 0.5)); 
	    	fval22 = new FuzzyValue(fVarX, new RightLinearFuzzySet(0.5, 0.5));
	    	System.out.println("\n Similarity between two fuzzy values (should be exactly 0.0): " + fval11.similarity(fval22));

	    	System.out.println("\n\n********* Odd/unexpected behaviour of simiarity by possibility/necessity *****************");
	    	fVarX = new FuzzyVariable("TestFVar", 0, 1);          
	    	fval11 = new FuzzyValue(fVarX, new RightLinearFuzzySet(0.25, 0.26));
	    	fval22 = new FuzzyValue(fVarX, new LeftLinearFuzzySet(0.24, 0.25)); 
	    	FuzzyValue fvalArray[] = {fval11, fval22};
		    System.out.println(FuzzyValue.plotFuzzyValues("*.", 0.2, 0.3, fvalArray));   
		    FuzzyValue.setDefaultSimilarityOperator(new SimilarityByPossibilityOperator());
	    	System.out.println("\n Similarity using possibility/necessity between two fuzzy values shown above(expect to be close to 0.0): " + fval11.similarity(fval22));
		    FuzzyValue.setDefaultSimilarityOperator(new SimilarityByAreaOperator());
	    	System.out.println("\n Similarity using area between two fuzzy values shown above(expect to be close to 0.0): " + fval11.similarity(fval22));
		    System.out.println("\n****************************************");
		  		        
		    FuzzyVariable outEnergy = new FuzzyVariable("Energy", 0.0, 100.0);
		    System.out.println();
		    outEnergy.addTerm("Low", new RightGaussianFuzzySet(0.0 , 15.0));
		    outEnergy.addTerm("Medium", new  GaussianFuzzySet(50.0 , 10.0));
		    outEnergy.addTerm("High", new  LeftGaussianFuzzySet(100.0 , 15.0));
		    FuzzyValue fvals1[] = new FuzzyValue[3];
		    fvals1[0] = outEnergy.findTerm("Low");
		    fvals1[1] = outEnergy.findTerm("Medium");
		    fvals1[2] = outEnergy.findTerm("High");
		    System.out.println(FuzzyValue.plotFuzzyValues("*+.", 0, 100, fvals1));
		    
		    // test the 'new' defuzzify method ... weightedAverageDefuzzify with
		    // FuzzyValues and a FuzzyValueVector with several FuzzyValues in it
		    System.out.println("test the 'new' defuzzify method 'weightedAverageDefuzzify' with FuzzyValues and a FuzzyValueVector with several FuzzyValues in it");
		    FuzzyValueVector fvv2 = new FuzzyValueVector();
		    FuzzyValueVector fvv3 = new FuzzyValueVector();
		    double xx[] = { 10, 10, 10, 20, 20, 20, 30, 30, 30 };
		    double yy[] = {  0, .5,  0,  0,  1,  0,  0,.25,  0 };
		    fval = new FuzzyValue(temp, xx, yy, xx.length);
		    System.out.println("\nweightedAvg defuzzify is " + 
		                       fval.weightedAverageDefuzzify() + ", expected 18.57...");
		    fvv2.addFuzzyValue(fval);
		    double xx1[] = { 10, 15, 20, 25, 30, 35, 40 };
		    double yy1[] = {  0, .5, 1,  0,  1,   1,  0 };
		    fval = new FuzzyValue(temp, xx1, yy1, xx1.length);
		    fvv3.addFuzzyValue(fval);
		    System.out.println(fval);
		    System.out.println("weightedAvg defuzzify is " + 
		                       fval.weightedAverageDefuzzify() + ", expected 26.42857... or 28.3333");
		    System.out.println(fval.toString() + "\n(depends whether the fuzzy set was reduced by 1 point - [15, .5] is unnecessary)");
		    System.out.println("moment defuzzify is " + 
		                       fval.momentDefuzzify() + ", expected 26.428571428571427...");
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify());
		    System.out.println("Maximum defuzzify is " + 
		                       fval.maximumDefuzzify() + ", expected 28.333333");
		    double xx2[] = { 10, 10, 10, 20, 25, 30, 40, 45 };
		    double yy2[] = {  0, .5,  0,  0, 1,   1, .5,  0 };
		    fval = new FuzzyValue(temp, xx2, yy2, xx2.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("weightedAvg defuzzify is " + 
		                       fval.weightedAverageDefuzzify() + ", expected 26.6666...");
		    System.out.println("moment defuzzify is " + 
		                       fval.momentDefuzzify() + ", expected 31.15384615384615...");
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify());
		    fvv2.addFuzzyValue(fval);
		    fvv3.addFuzzyValue(fval);
		    System.out.println("Maximum defuzzify is " + 
		                       fval.maximumDefuzzify() + ", expected 27.5");
		    System.out.println("weightedAvg defuzzify of FuzzyValueVector is " + 
		                       fvv2.weightedAverageDefuzzify() + ", expected 23.68421052631579...");
		    System.out.println("maximum defuzzify of FuzzyValueVector is " + 
		                       fvv2.maximumDefuzzify() + ", expected 25.0");
		    for (i=0; i<fvv3.size(); i++)
		    	System.out.println("fvv3["+i+"]is:" + fvv3.fuzzyValueAt(i));
		    System.out.println("moment defuzzify of FuzzyValueVector is " + 
		                       fvv3.momentDefuzzify() + ", expected 28.703703703703702...\n");
		    System.out.println("COA defuzzify of FuzzyValueVector is " + fvv3.centerOfAreaDefuzzify());
System.out.println("fvv3 size is: " + fvv3.size());
			FuzzyValue fvSum = fvv3.fuzzyValueAt(0).fuzzySum(fvv3.fuzzyValueAt(1));
		    System.out.println("FuzzySum of fvv3 is: " + fvSum);
		    System.out.println(fvSum.plotFuzzyValue("*", 0, 50));
		    System.out.println("COA defuzzify of fvSum is: " + fvSum.centerOfAreaDefuzzify());
		    
/*
		    fval3 = new FuzzyValue(temp2, x, y, 3);
		    fval3 = fval3.fuzzyUnion(fval2);
		    System.out.println(fval3.getMembership(27.5));
		    System.out.println(fval3);
*/		    
		    System.out.println();
			System.out.println("Temperature Fuzzy Variable Terms are:");
			for (Enumeration e = temp.findTermNames() ; e.hasMoreElements() ;) 
			{
        		 System.out.println(e.nextElement());
		  	}
			System.out.println("Temperature Fuzzy Variable Term Values are:");
			for (Enumeration e = temp.findTerms() ; e.hasMoreElements() ;) 
			{
        		 System.out.println(e.nextElement());
		  	}
			System.out.println("Pressure Fuzzy Variable Terms are:");
			for (Enumeration e = pressure.findTermNames() ; e.hasMoreElements() ;) 
			{
        		 System.out.println(e.nextElement());
		  	}
		  	
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "medium");
/*            System.out.println("Starting loop 1"); System.out.flush();
            for (int jj=0; jj<5000000; jj++)
               fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 2"); System.out.flush();
            for (int jj=0; jj<10000000; jj++)
               fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 3"); System.out.flush();
            for (int jj=0; jj<10000000; jj++)
               fval.maximumOfIntersection(fval2);
            System.out.println("Ending all loops");System.out.flush();
*/     

			// The new fuzzysets with gaussian shapes
			FuzzyVariable temp4 = new FuzzyVariable("temp4", 0, 100, "C");
			temp4.addTerm("hot", new LeftGaussianFuzzySet(80.0, 7.5, 20));
			temp4.addTerm("cold", new RightGaussianFuzzySet(10.0, 10.0));
			temp4.addTerm("veryHot", "very hot");
			temp4.addTerm("medium", new GaussianFuzzySet(50.0, 5.0, 55, 10));
		    System.out.println();
			fval3 = new FuzzyValue(temp4, "medium");
			System.out.println(fval3.plotFuzzyValue("+",30,90));
		    System.out.println("COA defuzzify of 'medium' is " + fval3.centerOfAreaDefuzzify());
			fval3 = new FuzzyValue(temp4, "hot");
			System.out.println(fval3.plotFuzzyValue("+",50,90));
		    System.out.println("COA defuzzify of 'hot' is " + fval3.centerOfAreaDefuzzify());
			fval3 = new FuzzyValue(temp4, "cold");
			System.out.println(fval3.plotFuzzyValue("+",0,50));
		    System.out.println("COA defuzzify of 'cold' is " + fval3.centerOfAreaDefuzzify());
       
			// Some testing of the 'new' method 'getXForMembership' on FuzzySets.
			// It returns the 1st X value associated with a membership value
			// in a fuzzy set (or fuzzy value).
			FuzzySet fs1 = new FuzzySet(); // an empty one ... should get exception for this one
		    System.out.println();
			try {
				fs1.getXforMembership(0.5);
			}
			catch (NoXValueForMembershipException e)
			{ System.err.println("Expecting exception ...\n" + e);
			}
			fs1 = new FuzzySet(); // an empty one ... should get exception for this one
			try {
				fs1.getXforMembership(0.5);
			}
			catch (NoXValueForMembershipException e)
			{ System.err.println("Expecting exception ...\n" + e);
			}
			double axx1[] = {25.0};
			double ayy1[] = {0.7};
			FuzzySet fs2 = new FuzzySet(axx1, ayy1, 1); // an empty one ... should get exception for this one
			try {
				fs2.getXforMembership(0.1);
			}
			catch (NoXValueForMembershipException e)
			{ System.err.println("Expecting exception ...\n" + e);
			}
			double m;
		    System.out.println();
			m = (new FuzzyValue(temp4, "hot")).getXforMembership(0.0);
			System.out.println("getXforMembership: X value expected is 50.0 ... got ..." + m);
			m = (new FuzzyValue(temp4, "hot")).getXforMembership(0.5);
			System.out.println("getXforMembership: X value expected is 71.157255663 ... got ..." + m);
			m = (new FuzzyValue(temp4, "hot")).getXforMembership(1.0);
			System.out.println("getXforMembership: X value expected is 80.0 ... got ..." + m);
			m = (new FuzzyValue(temp4, "cold")).getXforMembership(0.0);
			System.out.println("getXforMembership: X value expected is 50.0 ... got ..." + m);
			m = (new FuzzyValue(temp4, "cold")).getXforMembership(0.5);
			System.out.println("getXforMembership: X value expected is 21.8896577068 ... got ..." + m);
			m = (new FuzzyValue(temp4, "cold")).getXforMembership(1.0);
			System.out.println("getXforMembership: X value expected is 10.0 ... got ..." + m);
			m = (new FuzzyValue(temp4, "medium")).getXforMembership(0.0);
			System.out.println("getXforMembership: X value expected is 30.0 ... got ..." + m);
			m = (new FuzzyValue(temp4, "medium")).getXforMembership(0.5);
			System.out.println("getXforMembership: X value expected is 44.055171146596 ... got ..." + m);
			m = (new FuzzyValue(temp4, "medium")).getXforMembership(1.0);
			System.out.println("getXforMembership: X value expected is 50.0 ... got ..." + m);

		    // special tests for centerOfAreaDefuzzify
		    double xx21[] = {  0,  10,  20, 30, 40, 50 };
		    double yy21[] = {  0,  .5,  .5,  1,  1,  0 };
		    fval = new FuzzyValue(temp, xx21, yy21, xx21.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify() + "  expecting 30.0");

		    double xx22[] = {  0,  10,  50 };
		    double yy22[] = {  1,   1,  0 };
		    fval = new FuzzyValue(temp, xx22, yy22, xx22.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify() + "  expecting 15.35898384862245");

		    double xx23[] = {  0, 40, 60, 100 };
		    double yy23[] = {  1,  0,  0,   1 };
		    fval = new FuzzyValue(temp, xx23, yy23, xx23.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify() + "  expecting 40 to 60 (50 is best?)");
		    System.out.println("Moment defuzzify is " + fval.momentDefuzzify() + "  expecting 50.0");

		    double xx24[] = { 20, 20, 80, 80 };
		    double yy24[] = {  1,  0,  0,  1 };
		    fval = new FuzzyValue(temp, xx24, yy24, xx24.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify() + "  expecting 20 to 80 (50 is best?)");
		    System.out.println("Moment defuzzify is " + fval.momentDefuzzify() + "  expecting 50.0");

		    double xx25[] = { 1,  3,  5,  6, 7 };
		    double yy25[] = { 0,  1, .6, .6, 0 };
		    fval = new FuzzyValue(temp, xx25, yy25, xx25.length);
		    System.out.println();
		    System.out.println(fval);
		    System.out.println("COA defuzzify is " + fval.centerOfAreaDefuzzify() + "  expecting 3.816699867329622");
		    System.out.println("Moment defuzzify is " + fval.momentDefuzzify() + "  expecting 3.942857...");
		    
		    FuzzyVariable fvar10 = new FuzzyVariable("Distance", 1.00, 7.00, "");
		    FuzzySet fs10a = new GaussianFuzzySet(3.42 , 0.78 , 4.59 , 0.78);
		    System.out.println(fs10a);
		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    FuzzyValue fv10b = new FuzzyValue(fvar10, new GaussianFuzzySet(3.42 , 0.78 , 4.59 , 0.78));
		    System.out.println(fv10b);
		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    
		  }
		catch (Exception e)
		  {//System.err.println("error: " + e + "\n" + e.getMessage());
		   e.printStackTrace();}		
	}

}

