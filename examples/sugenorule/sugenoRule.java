// Example of a simple Takagi-Sugeno-Kang rule system (order 1).
//
// In this case there are 2 rules each with 2 inputs and 1 output.
//
// The inputs are:
//	x with fuzzy values small and big
//	y with fuzzy values medium and big
//
// The output is z
//
// The 2 rules are:
//
//	Rule1: IF x is big AND y is medium THEN z = x - 3*y
//	Rule2: IF x is small AND y is big THEN  z = 4 + 2*x


package examples.sugenorule;

import nrc.fuzzy.*;

public class sugenoRule
{

  public sugenoRule()
  {
  }

  public static void main(String[] argv) throws FuzzyException
  {
   // Step 1  (define the FuzzyVariables for x and y)

     // x has terms small and big
     FuzzyVariable x = new FuzzyVariable("x", 0.0, 10.0, "");
     x.addTerm("small", new RightLinearFuzzySet(0.0, 10.0));
     x.addTerm("big", new LeftLinearFuzzySet(0.0, 10.0));

     // y has terms medium and big
     FuzzyVariable y = new FuzzyVariable("x", 0.0, 100.0, "");
     y.addTerm("medium", new TriangleFuzzySet(10, 50.0, 90.0));
     y.addTerm("big", new LeftLinearFuzzySet(38.0, 100.0));

     // z has no terms ... we'll use singletons for the output
     FuzzyVariable z = new FuzzyVariable("z", -300.0, 300.0, "");

  // Step 2 (define our rules)

     // the fuzzy rule ...
     // IF x is big AND y is medium THEN z = x - 3*y
     FuzzyRule bigMedium = new FuzzyRule();
     bigMedium.addAntecedent(new FuzzyValue(x,"big"));
     bigMedium.addAntecedent(new FuzzyValue(y,"medium"));

     // the fuzzy rule ...
     //	IF x is small AND y is big THEN  z = 4 + 2*x
     FuzzyRule smallBig = new FuzzyRule();
     smallBig.addAntecedent(new FuzzyValue(x,"small"));
     smallBig.addAntecedent(new FuzzyValue(y,"big"));

  // Step 3 (provide the fuzzified inputs for the rules)


     // x crisp value is 4.0 and the y crisp value is 60.0
     double xVal = 3.0;
     double yVal = 60.0;
     // create fuzzy values from the crisp values
     FuzzyValue xFVal =  new FuzzyValue(x, new SingletonFuzzySet(xVal)); 
     FuzzyValue yFVal =  new FuzzyValue(y, new SingletonFuzzySet(yVal)); 

  // Step 4 (execute the rules with these inputs, after defining the rule conclusions
  //         and aggregate the results from the 2 rule firings)

     // remove any inputs associated with the rules, then add the new inputs to the rule
     bigMedium.removeAllInputs();
     bigMedium.addInput(xFVal);
     bigMedium.addInput(yFVal);
     smallBig.removeAllInputs();
     smallBig.addInput(xFVal);
     smallBig.addInput(yFVal);
     // add the conclusions that depend on the inputs
     bigMedium.addConclusion(new FuzzyValue(z, new SingletonFuzzySet(xVal - 3.0*yVal)));
     smallBig.addConclusion(new FuzzyValue(z, new SingletonFuzzySet(4.0 + 2.0*xVal)));
     // fire the rules, the result of firing is a vector of FuzzyValues that represents the output
     FuzzyValueVector zfvv1 = bigMedium.execute();
     FuzzyValueVector zfvv2 = smallBig.execute();
     // aggregate the results from the 2 rules
     FuzzyValue zFVal = zfvv1.fuzzyValueAt(0).fuzzySum(zfvv2.fuzzyValueAt(0));


  // Step 5 (defuzzify the output to get crisp values)

     // calculate the deffuzified value
     double crispz = zFVal.weightedAverageDefuzzify();

  // Step 6 (let's look at the fuzzy set of the output and the crisp value)

  FuzzySet.setToStringPrecision(6);
  System.out.println(zFVal);
  System.out.println("");
  System.out.println(zFVal.plotFuzzyValue("+"));
     System.out.println("\nDefuzzified z value is: " + crispz);
  }

}
