
package examples.simplerule;

import nrc.fuzzy.*;

public class simpleRule
{

  public simpleRule()
  {
  }

  public static void main(String[] argv) throws FuzzyException
  {
// Step 1  (define the FuzzyVariables for temperature, flow, cold valve change and hot valve change)

     // Temperature has terms cold, OK and hot
     FuzzyVariable outTemp = new FuzzyVariable("temperature", 5.0, 65.0, "Degrees C");
     outTemp.addTerm("cold", new TrapezoidFuzzySet(5.0, 5.05, 10.0, 35.0));
     outTemp.addTerm("OK", new PIFuzzySet(36.0, 3.5));
     outTemp.addTerm("hot", new SFuzzySet(37.0, 60.0));
     // Flow has terms low, OK, and strong
     FuzzyVariable outFlow = new FuzzyVariable("flow", 0, 100.0, "litres/minute");
     outFlow.addTerm("low", new TrapezoidFuzzySet(0.0, 0.025, 3.0, 11.5));
     outFlow.addTerm("OK", new PIFuzzySet(12.0, 1.8));
     outFlow.addTerm("strong", new SFuzzySet(12.5, 25.0));
     // hotValveChange has terms NB, NM, NS, Z, PS, PM, and PB which correspond to
     // negative big, negative medium, negative small, zero, positive small,
     // positive medium and positive big
     FuzzyVariable hotValveChange = new FuzzyVariable("hotValveChange", -1.0, 1.0, "");
     hotValveChange.addTerm("NB", new ZFuzzySet(-0.5, -.25));
     hotValveChange.addTerm("NM", new TriangleFuzzySet(-.35, -.3, -.15));
     hotValveChange.addTerm("NS", new TriangleFuzzySet(-.25, -.15, 0.0));
     hotValveChange.addTerm("Z", new TriangleFuzzySet(-.05, 0.0, 0.05));
     hotValveChange.addTerm("PS", new TriangleFuzzySet(0.0, .15, .25));
     hotValveChange.addTerm("PM", new TriangleFuzzySet(.15, .3, .35));
     hotValveChange.addTerm("PB", new SFuzzySet(.25, .5));
     // coldValveChange has terms NB, NM, NS, Z, PS, PM, and PB which correspond to
     // negative big, negative medium, negative small, zero, positive small,
     // positive medium and positive big
     FuzzyVariable coldValveChange = new FuzzyVariable("coldValveChange", -1.0, 1.0, "");
     coldValveChange.addTerm("NB", new ZFuzzySet(-0.5, -.25));
     coldValveChange.addTerm("NM", new TriangleFuzzySet(-.35, -.3, -.15));
     coldValveChange.addTerm("NS", new TriangleFuzzySet(-.25, -.15, 0.0));
     coldValveChange.addTerm("Z", new TriangleFuzzySet(-.05, 0.0, 0.05));
     coldValveChange.addTerm("PS", new TriangleFuzzySet(0.0, .15, .25));
     coldValveChange.addTerm("PM", new TriangleFuzzySet(.15, .3, .35));
     coldValveChange.addTerm("PB", new SFuzzySet(.25, .5));

// Step 2 (define our rule)

     // the fuzzy rule ...
     // if temperature is hot and flow is low
     // then change the hot valve zero and the clod valve positive big
     FuzzyRule hotLow = new FuzzyRule();
     hotLow.addAntecedent(new FuzzyValue(outTemp,"hot"));
     hotLow.addAntecedent(new FuzzyValue(outFlow,"low"));
     hotLow.addConclusion(new FuzzyValue(hotValveChange,"Z"));
     hotLow.addConclusion(new FuzzyValue(coldValveChange,"PB"));

// Step 3 (provide the fuzzified inputs for the rule)


     // temperature reading is 45.0 degrees C and the flow is 8.0 litres/minute
     double showerTemp = 45.0;
     double showerFlow = 8.0;
     // create fuzzy values from the crisp values
     FuzzyValue inputTemp =  new FuzzyValue(outTemp, new TriangleFuzzySet(showerTemp-0.05, 
                                            showerTemp, showerTemp+0.05));
     FuzzyValue inputFlow =  new FuzzyValue(outFlow, new TriangleFuzzySet(showerFlow-0.05, 
                                            showerFlow, showerFlow+0.05));

// Step 4 (execute the rule with these inputs)

     // remove any inputs associated with the rule, then add the new inputs to the rule
     hotLow.removeAllInputs();
     hotLow.addInput(inputTemp);
     hotLow.addInput(inputFlow);
     // fire the rule, the result of firing is a vector of FuzzyValues that repreent the outputs
     FuzzyValueVector fvv = hotLow.execute();

// Step 5 (defuzzify the outputs to get crisp values)

     // get the output FuzzyValues from the vector
     FuzzyValue hotValveChangeFval = fvv.fuzzyValueAt(0);
     FuzzyValue coldValveChangeFval = fvv.fuzzyValueAt(1);
     // calculate the deffuzified value
     double crispHotValveChange = hotValveChangeFval.momentDefuzzify();
     double crispColdValveChange = coldValveChangeFval.momentDefuzzify();

// Step 6 (let's look at the fuzzy sets of the outputs and the crisp values)


     System.out.println(hotValveChangeFval.plotFuzzyValue("+"));
     System.out.println(coldValveChangeFval.plotFuzzyValue("+"));
     System.out.println("Defuzzified hot valve change is: " + crispHotValveChange);
     System.out.println("Defuzzified cold valve change is: " + crispColdValveChange);
     
// extra code to show how to access the x,y pairs of a FuzzyValue
     FuzzyValue fvalsA[] = new FuzzyValue[3];
     fvalsA[0] = outTemp.findTerm("cold");
     fvalsA[1] = outTemp.findTerm("OK");
     fvalsA[2] = outTemp.findTerm("hot");
     
     System.out.println();
     int i, j;
     for (j=0; j<3; j++)
     {
       System.out.println("\nValues for term: " + fvalsA[j].getLinguisticExpression());
       for (i=0; i<fvalsA[0].size(); i++)
    	   System.out.println(fvalsA[j].getX(i) + ", " + fvalsA[j].getY(i));
     }
  }

}
