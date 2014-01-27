/** 
 * This is a very simple example of a 'simulated' shower with a hot and a cold
 * valve to control the flow and temperature in a shower. The simulation
 * is extremely crude and does not take into account delays in mixing
 * and travel of the hot and cold water or delays required to move the
 * valves. So we have perfect mixing and perfect and immediate results
 * to changes in the valve positions.The intent is to show a fuzzy system that
 * with a few rules that do not 'cover' the space well, can still do a reasonable
 * job of controlling the shower. Ideally we would have more terms for the 
 * shower temperature and pressure, therefore more rules, and we would
 * monitor the behaviour of the system so that it could tune the rules
 * based on performance of the system. For example, if the pressures of the
 * hot and cold rise, the adjustments will affect bigger system responses
 * so the rules should be tempered to account for the new sensitivity.
 *
 */
package examples.fuzzyshower;

import java.awt.*;
import java.text.NumberFormat;

import nrc.fuzzy.*;


public class ShowerSimulate implements Runnable
{
    FuzzyRule noneNone = new FuzzyRule();
    FuzzyRule coldLow = new FuzzyRule();
    FuzzyRule coldOK = new FuzzyRule();
    FuzzyRule coldStrong = new FuzzyRule();
    FuzzyRule OKLow = new FuzzyRule();
    FuzzyRule OKOK = new FuzzyRule();
    FuzzyRule OKStrong = new FuzzyRule();
    FuzzyRule hotLow = new FuzzyRule();
    FuzzyRule hotOK = new FuzzyRule();
    FuzzyRule hotStrong = new FuzzyRule();
    FuzzyVariable outTemp;
    FuzzyVariable outFlow;
    FuzzyVariable hotValveChange;
    FuzzyVariable coldValveChange;
    FuzzyRule allRules[] =
       {noneNone, coldLow, coldOK, coldStrong, OKLow, OKOK,
        OKStrong, hotLow, hotOK, hotStrong
       };
    double hotTemp = 55.0, hotPressure = 55.0, hotValve = 0.0;
    double coldTemp = 5.0, coldPressure = 55.0, coldValve = 0.0;
    double showerTemp = 0.0, showerFlow = 0.0;
    double  atmosphericPressure = 30.0;
    double  optimalTempMin = 34.0;
    double  optimalTempMax = 38.0;
    double  optimalFlowMin = 11.0;
    double  optimalFlowMax = 13.0;
    
	RightLinearFunction rlf = new RightLinearFunction();
	LeftLinearFunction llf = new LeftLinearFunction();
    
    Thread simulateThread = null;
    ShowerFrame sf;

    public ShowerSimulate( ShowerFrame showerFrame )
    {
        sf = showerFrame;
// FuzzySet.setToStringPrecision(18);
        init();
    }
	public void init()
	{
	  try
	  {
	    // define the fuzzy variables
	    outTemp = new FuzzyVariable("temperature", 5.0, 65.0, "Degrees C");
	    outTemp.addTerm("none", new RFuzzySet(5.0, 5.1, rlf));
	    outTemp.addTerm("cold", new TrapezoidFuzzySet(5.0, 5.05, 10.0, 35.0));
	    outTemp.addTerm("OK", new PIFuzzySet(36.0, 3.5));
	    outTemp.addTerm("hot", new SFuzzySet(37.0, 60.0));
	    outFlow = new FuzzyVariable("flow", 0, 100.0, "litres/minute");
	    outFlow.addTerm("none", new RFuzzySet(0.0, 0.05, rlf));
	    outFlow.addTerm("low", new TrapezoidFuzzySet(0.0, 0.025, 3.0, 11.5));
	    outFlow.addTerm("OK", new PIFuzzySet(12.0, 1.8));
	    outFlow.addTerm("strong", new SFuzzySet(12.5, 25.0));
	    hotValveChange = new FuzzyVariable("hotValveChange", -1.0, 1.0, "");
	    hotValveChange.addTerm("NB", new RFuzzySet(-0.5, -.25, rlf));
	    hotValveChange.addTerm("NM", new TriangleFuzzySet(-.35, -.3, -.15));
	    hotValveChange.addTerm("NS", new TriangleFuzzySet(-.25, -.15, 0.0));
	    hotValveChange.addTerm("Z", new TriangleFuzzySet(-.05, 0.0, 0.05));
	    hotValveChange.addTerm("PS", new TriangleFuzzySet(0.0, .15, .25));
	    hotValveChange.addTerm("PM", new TriangleFuzzySet(.15, .3, .35));
	    hotValveChange.addTerm("PB", new LFuzzySet(.25, .5, llf));
	    coldValveChange = new FuzzyVariable("coldValveChange", -1.0, 1.0, "");
	    coldValveChange.addTerm("NB", new RFuzzySet(-0.5, -.25, rlf));
	    coldValveChange.addTerm("NM", new TriangleFuzzySet(-.35, -.3, -.15));
	    coldValveChange.addTerm("NS", new TriangleFuzzySet(-.25, -.15, 0.0));
	    coldValveChange.addTerm("Z", new TriangleFuzzySet(-.05, 0.0, 0.05));
	    coldValveChange.addTerm("PS", new TriangleFuzzySet(0.0, .15, .25));
	    coldValveChange.addTerm("PM", new TriangleFuzzySet(.15, .3, .35));
	    coldValveChange.addTerm("PB", new LFuzzySet(.25, .5, llf));
	    // define the 10 fuzzy rules ... Rule noneNone jump starts an 'off' system
		noneNone.addAntecedent(new FuzzyValue(outTemp,"none"));
		noneNone.addAntecedent(new FuzzyValue(outFlow,"none"));
		noneNone.addConclusion(new FuzzyValue(hotValveChange,"PS"));
		noneNone.addConclusion(new FuzzyValue(coldValveChange,"PM"));
		coldLow.addAntecedent(new FuzzyValue(outTemp,"cold"));
		coldLow.addAntecedent(new FuzzyValue(outFlow,"low"));
		coldLow.addConclusion(new FuzzyValue(hotValveChange,"PB"));
		coldLow.addConclusion(new FuzzyValue(coldValveChange,"Z"));
		coldOK.addAntecedent(new FuzzyValue(outTemp,"cold"));
		coldOK.addAntecedent(new FuzzyValue(outFlow,"OK"));
		coldOK.addConclusion(new FuzzyValue(hotValveChange,"PM"));
		coldOK.addConclusion(new FuzzyValue(coldValveChange,"Z"));
		coldStrong.addAntecedent(new FuzzyValue(outTemp,"cold"));
		coldStrong.addAntecedent(new FuzzyValue(outFlow,"strong"));
		coldStrong.addConclusion(new FuzzyValue(hotValveChange,"Z"));
		coldStrong.addConclusion(new FuzzyValue(coldValveChange,"NB"));
		OKLow.addAntecedent(new FuzzyValue(outTemp,"OK"));
		OKLow.addAntecedent(new FuzzyValue(outFlow,"low"));
		OKLow.addConclusion(new FuzzyValue(hotValveChange,"PS"));
		OKLow.addConclusion(new FuzzyValue(coldValveChange,"PS"));
		OKOK.addAntecedent(new FuzzyValue(outTemp,"OK"));
		OKOK.addAntecedent(new FuzzyValue(outFlow,"OK"));
		OKOK.addConclusion(new FuzzyValue(hotValveChange,"Z"));
		OKOK.addConclusion(new FuzzyValue(coldValveChange,"Z"));
		OKStrong.addAntecedent(new FuzzyValue(outTemp,"OK"));
		OKStrong.addAntecedent(new FuzzyValue(outFlow,"strong"));
		OKStrong.addConclusion(new FuzzyValue(hotValveChange,"NS"));
		OKStrong.addConclusion(new FuzzyValue(coldValveChange,"NS"));
		hotLow.addAntecedent(new FuzzyValue(outTemp,"hot"));
		hotLow.addAntecedent(new FuzzyValue(outFlow,"low"));
		hotLow.addConclusion(new FuzzyValue(hotValveChange,"Z"));
		hotLow.addConclusion(new FuzzyValue(coldValveChange,"PB"));
		hotOK.addAntecedent(new FuzzyValue(outTemp,"hot"));
		hotOK.addAntecedent(new FuzzyValue(outFlow,"OK"));
		hotOK.addConclusion(new FuzzyValue(hotValveChange,"NM"));
		hotOK.addConclusion(new FuzzyValue(coldValveChange,"Z"));
		hotStrong.addAntecedent(new FuzzyValue(outTemp,"hot"));
		hotStrong.addAntecedent(new FuzzyValue(outFlow,"strong"));
		hotStrong.addConclusion(new FuzzyValue(hotValveChange,"NB"));
		hotStrong.addConclusion(new FuzzyValue(coldValveChange,"Z"));
	  }
	  catch (FuzzyException fe)
	  { System.err.println("Error initializing fuzzy variables/rules\n" + fe);
	    System.exit(100);
	  }
	}
	
	public void initSliders()
	{
	  // set the initial values for the sliders
	  try
	  {
    	  sf.hotPressureSlider.setValue((int)Math.round(hotPressure));
    	  sf.coldPressureSlider.setValue((int)Math.round(coldPressure));
    	  sf.hotValveSlider.setValue((int)Math.round(hotValve*100.0));
    	  sf.coldValveSlider.setValue((int)Math.round(coldValve*100.0));
    	  sf.hotTempSlider.setValue((int)Math.round(hotTemp));
    	  sf.coldTempSlider.setValue((int)Math.round(coldTemp));
    	  sf.showerTempSlider.setValue((int)Math.round(showerTemp));
    	  sf.showerFlowSlider.setValue((int)Math.round(showerFlow));
	  }
	  catch (Exception e)
	  {System.out.println("Error initializing sliders:\n" + e);
	   e.printStackTrace();
	  }
	}
	
	public void setOutFlowAndOutTemp()
	{
	    double hotFlow = hotValve * (hotPressure - atmosphericPressure);
	    double coldFlow = coldValve * (coldPressure - atmosphericPressure);
        showerFlow =  hotFlow + coldFlow;
        if (showerFlow == 0.0)
           showerTemp = 5.0;
        else
           showerTemp = ((coldFlow*coldTemp) + (hotFlow*hotTemp))/showerFlow;
           
        try
        {   sf.showerTempSlider.setValue((int)Math.round(showerTemp));
            sf.showerFlowSlider.setValue((int)Math.round(showerFlow));
    		if (showerFlow >= optimalFlowMin && showerFlow <= optimalFlowMax)
    		    sf.showerFlowLabel3D.setBackground(Color.green);
    		else
    		    sf.showerFlowLabel3D.setBackground(Color.white);
    		if (showerTemp >= optimalTempMin && showerTemp <= optimalTempMax)
    		    sf.showerTempLabel3D.setBackground(Color.green);
    		else
    		    sf.showerTempLabel3D.setBackground(Color.white);
		}   
        catch (Exception e)
        {   System.err.println("Error setting shower temp/flow sliders in setOutFlowAndOutTemp.\n" + e);
        }
	}
	
	public void shutdownAndSetToManual()
	{
        coldValve = 0.0;
        hotValve  = 0.0;
        try
        {
    	    sf.hotValveSlider.setValue(0);
    	    sf.coldValveSlider.setValue(0);
	    }
	    catch (Exception e)
	    {System.err.println("Error setting valve sliders in shutdownAndSetToManual.\n" + e);
	    }
	    sf.autoCheckbox.setState(false);
        setOutFlowAndOutTemp();
	}
	
	
	public void simulateShower()
	{
	    if (simulateThread == null || !simulateThread.isAlive())
	    {
            simulateThread = new Thread(this, "Fuzzy");
            simulateThread.start();
	    }
    }


			public void run()
			{
			  double iterationFactor = 1.0;
			  FuzzyValue inputFlow, inputTemp;
			  FuzzyValueVector fvv;
			  FuzzyValue globalColdValveChange, globalHotValveChange;
			  int counter = 0;
			  double coldValveChange = 0.0;
			  double hotValveChange = 0.0;
			  double scaledColdValveChange = 0.0;
			  double scaledHotValveChange = 0.0;
		
		      sf.setNoticeLabel("Fuzzy Control in effect - watch valve changes.");
		
		      // calculate the new outputs - flow and temp
		      setOutFlowAndOutTemp();
		
			  // show results if requested
			  if (sf.showFiringsCheckbox.getState())
			  {
			     if (sf.inferenceCycleResults.isVisible()==false)
			         sf.inferenceCycleResults.setVisible(true);
			     sf.inferenceCycleResults.resultsTextArea.setText
			           ( "Initially ..." +
			             "\n\tCold Valve: " + coldValve + 
			             "\n\tHot Valve: " + hotValve + 
			             "\n\tShowerTemp: " + showerTemp +
			             "\n\tShower Flow: " + showerFlow + 
			             "\n"
			           );
			  }
			  else
			      if (sf.inferenceCycleResults.isVisible()==true)
			          sf.inferenceCycleResults.setVisible(false);
			  
			  while (true) // try firing rules until under control or can't control
			  { 
		        // if both output flow and temp are within correct range control is
		        // completed -- halt
		        if (showerFlow >= optimalFlowMin && showerFlow <= optimalFlowMax &&
		            showerTemp >= optimalTempMin && showerTemp <= optimalTempMax
		           )
		        {
		          sf.setNoticeLabel("Shower is under control! Used " + counter + " inference cycles." );
			      if (sf.showFiringsCheckbox.getState())
			      {
			        sf.inferenceCycleResults.resultsTextArea.append
			          (
			            "\nShower now within limits: " + 
			            "\n\tTemp between " + optimalTempMin + " and " + optimalTempMax +
			            "\n\tFlow between " + optimalFlowMin + " and " + optimalFlowMax
			          );
			      }
		          sf.repaint();
		          return;
		        }
		        
		        // Check for some rather bad situations where control is lost
		        if (coldValve == 1.0 && hotValve == 1.0  &&
		            coldValveChange >= 0.0 && hotValveChange >= 0.0 & counter > 1
		           )
		        {
		            sf.setNoticeLabel("*** Pressure Problem: Shutting Down - Both valves full open and trying to open more ***");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (coldValve == 0.0 && hotValve == 0.0  &&
		            coldValveChange <= 0.0 && hotValveChange <= 0.0 && counter > 1
		           )
		        {
		            sf.setNoticeLabel("*** Pressure Problem: Shutting Down - Both valves fully closed and trying to close more ***");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (coldValve == 1.0 && coldValveChange > 0.0 && Math.abs(hotValveChange) < 0.0001)
		        {
		            sf.setNoticeLabel("*** Cold Water or Pressure Problem: Shutting Down - Cold valve full open, trying to open more ***");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (hotValve == 1.0 && hotValveChange > 0.0 && Math.abs(coldValveChange) < 0.0001)
		        {
		            sf.setNoticeLabel("*** Hot Water or Pressure Problem: Shutting Down - Hot valve full open, trying to open more ***");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (coldValve == 0.0 && coldValveChange < 0.0 && Math.abs(hotValveChange) < 0.0001)
		        {
		            sf.setNoticeLabel("*** HOT TEMPERATURE PROBLEM: Shutting down - cannot get temperature high enough");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (hotValve == 0.0 && hotValveChange < 0.0 && Math.abs(coldValveChange) < 0.0001)
		        {
		            sf.setNoticeLabel("*** HOT TEMPERATURE PROBLEM: Shutting down - cannot get temperature low enough");
		            iterationFactor = 1;
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
		        if (counter > 150)
		        {
		            sf.setNoticeLabel("*** Unknown PROBLEM: Too many attempts to control the system .. poor rule set likely the cause");
		            iterationFactor = 1;
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
			    // delay between inference cycles if required
			    if (counter > 0)
		    	    try {Thread.sleep(Integer.valueOf(sf.msSleepTextField.getText()).intValue());}
		    	    catch (Exception e) {}
		
		        // now 'assert' the fuzzified values of the Flow and Temp of Output
		        // (apply the new values to the rules and get next set of recommendations)
		        try
		        {
		         /* following code replaced by simpler code using the confineToXBounds
		            feature of FuzzyValue creation -- this keeps the FuzzyValue from 
		            throwing an exception when any of its values are outside of the 
		            UOD and by clipping the FuzzySet at the X boundaries
		            
		         if (showerFlow <= 0.0)
		            inputFlow = new FuzzyValue(outFlow, new RFuzzySet(0.0, 0.1, rlf));
		         else if (showerFlow >= 100.0)
		            inputFlow = new FuzzyValue(outFlow, new LFuzzySet(99.9, 100.0, llf));
		         else
		            inputFlow = 
		                new FuzzyValue(outFlow, new TriangleFuzzySet(Math.max(0.0,showerFlow-0.1), showerFlow, Math.min(100.0,showerFlow+0.1)));
		         if (showerTemp <= 5.0)
		            inputTemp = new FuzzyValue(outTemp, new RFuzzySet(5.0, 5.05, rlf));
		         else if (showerTemp >= 65.0)
		            inputTemp = new FuzzyValue(outTemp, new LFuzzySet(64.95, 65.0, llf));
		         else
		            inputTemp = 
		                new FuzzyValue(outTemp, new TriangleFuzzySet(Math.max(5.0,showerTemp-0.05), showerTemp, Math.min(65.0,showerTemp+0.05)));
		         */
		         FuzzyValue.setConfineFuzzySetsToUOD(true);
		         inputFlow = new FuzzyValue(outFlow, new TriangleFuzzySet(showerFlow-0.1, showerFlow, showerFlow+0.1));
		         inputTemp = new FuzzyValue(outTemp, new TriangleFuzzySet(showerTemp-0.05, showerTemp, showerTemp+0.05));
		         FuzzyValue.setConfineFuzzySetsToUOD(false);
		        }
		        catch (FuzzyException fe)
		        {
		            System.err.println(fe);
		            sf.setNoticeLabel("*** Internal Error ... stopping fuzzy control");
		            shutdownAndSetToManual();
		            sf.repaint();
		            return;
		        }
		        
			    counter++; // count each inference loop (cycle)
			    
			    // show results if requested  ... start of new cycle
			    if (sf.showFiringsCheckbox.getState())
			    {
			        sf.inferenceCycleResults.resultsTextArea.append("Cycle " + counter + ":\n");
			    }
			    
		        // now fire the rules with the new output flow and output temp
		        // and calculate the new recommended valve changes
			    globalColdValveChange = null;
			    globalHotValveChange  = null;
		        
		        for (int i=0; i<allRules.length; i++)
		        {
			        // add inputs to rules and fire them and do global contribution
			        allRules[i].removeAllInputs();
			        allRules[i].addInput(inputTemp);
			        allRules[i].addInput(inputFlow);
			        try
			        {
		    	        if (allRules[i].testRuleMatching())
		    	        {
		            	    if (sf.showFiringsCheckbox.getState())
		            	    {
		            	        sf.inferenceCycleResults.resultsTextArea.append
		            	          ( "\tRule: if Temp " + 
		            	            allRules[i].antecedentAt(0).getLinguisticExpression() +
		            	            " and Flow " + 
		            	            allRules[i].antecedentAt(1).getLinguisticExpression() +
		            	            " then change Hot Valve " +
		            	            allRules[i].conclusionAt(0).getLinguisticExpression() +
		            	            " and change Cold Valve " +
		            	            allRules[i].conclusionAt(1).getLinguisticExpression() +
		            	            " fires\n"
		            	          );  
		            	    }
		    	            fvv = allRules[i].execute();
		                    if (globalHotValveChange == null)
		    	                globalHotValveChange = fvv.fuzzyValueAt(0);
		    	            else
							globalHotValveChange = globalHotValveChange.fuzzyUnion(fvv.fuzzyValueAt(0));
		//					globalHotValveChange = globalHotValveChange.fuzzySum(fvv.fuzzyValueAt(0));
		    	            if (globalColdValveChange == null)
		    	                globalColdValveChange = fvv.fuzzyValueAt(1);
		    	            else
							globalColdValveChange = globalColdValveChange.fuzzyUnion(fvv.fuzzyValueAt(1));
		//					globalColdValveChange = globalColdValveChange.fuzzySum(fvv.fuzzyValueAt(1));
		    	        }
			        }
			        catch (FuzzyException fe)
			        { System.err.println(fe);
		              sf.setNoticeLabel("*** Internal Error ... stopping fuzzy control");
		              shutdownAndSetToManual();
		              sf.repaint();
		              return;
			        }
		        }
		        
		        try
		        {
		            if (globalHotValveChange == null)
		                hotValveChange = 0.001;
		            else
					hotValveChange = globalHotValveChange.momentDefuzzify();
		            if (globalColdValveChange == null)
		                coldValveChange = 0.001;
		            else
		            	coldValveChange = globalColdValveChange.momentDefuzzify();
		        }
		        catch (FuzzyException fe)
		        {   System.err.println(fe);
		            sf.setNoticeLabel("*** Internal Error ... stopping fuzzy control");
		             shutdownAndSetToManual();
		             sf.repaint();
		             return;
		        }
		                
		        // calc new hot and cold valve positions based on the recommendations
		        // for change provided 
		        // NOTE: we perform a scaling factor on the recommendations making the assumption
		        //       that at high valve settings a change will produce less effect than
		        //       if we are operating at a low valve setting; this is due to the effect
		        //       of pressures -- low pressures will tend to put us at higher valve
		        //       positions; of course in a REAL situation the actual operating conditions
		        //       of the system would likely be known and we couuld adjust accordingly;
		        //       One problem that can occur is that the adjustments cause the system
		        //       to be too course in its adjustment and it makes a change to go up and then
		        //       a symmetric change to go down -- it will then endlessly flip flop between
		        //       a too high and a too low condition -- this is solved by reducing the scale
		        //       factor after each iteration so the adjustments get smaller and smaller.
		        //       Also note that we could also tune the fuzzy sets to reduce the range 
		        //       of changes recommended.
		        //       REMEMBER the conditions of the problem -- we do not know the temps or
		        //       pressures of hot and cold supplies
		        
		        if (coldValveChange != 0.0 || hotValveChange != 0.0)
		        {
		            scaledColdValveChange = coldValveChange*iterationFactor*Math.max(0.1, coldValve);
		            scaledHotValveChange = hotValveChange*iterationFactor*Math.max(0.1, hotValve);
		            coldValve = 
		               Math.max(0.0, Math.min(1.0, coldValve + scaledColdValveChange));
		            hotValve = 
		               Math.max(0.0, Math.min(1.0, hotValve + scaledHotValveChange));
		            try
		            {    sf.hotValveSlider.setValue((int)Math.round(hotValve*100.0));
		        	     sf.coldValveSlider.setValue((int)Math.round(coldValve*100.0));
		        	}
		        	catch (Exception e)
		        	{   System.err.println("Error setting valve sliders. " + e);
		        	}
		        }
		
		        // calculate the new outputs - flow and temp
		        setOutFlowAndOutTemp();
		   
		        // iterationFactor reduces each cycle through
		        if (iterationFactor > 0.1)
		            iterationFactor = iterationFactor * 0.99;
		   
			    // show results if requested
			    if (sf.showFiringsCheckbox.getState())
			    {
		            sf.inferenceCycleResults.resultsTextArea.append
		                 ( "\tCold Valve Change: " + scaledColdValveChange +
		                   "\n\tHot Valve Change:  " + scaledHotValveChange + 
			               "\n\tCold Valve: " + coldValve + "\n\tHot Valve: " + hotValve +
			               "\n\tShowerTemp: " + showerTemp +  "\n\tShower Flow: " + showerFlow + "\n" 
			             );
		        }
		
		      } // end of while (true)
		        
		    }
	
    public void setHotPressure( double v )
    {
        hotPressure = v;
    }

    public void setHotValve( double v )
    {
        hotValve = v;
    }

    public void setHotTemp( double v )
    {
        hotTemp = v;
    }

    public void setColdPressure( double v )
    {
        coldPressure = v;
    }

    public void setColdValve( double v )
    {
        coldValve = v;
    }

    public void setColdTemp( double v )
    {
        coldTemp = v;
    }

    public double getColdTemp( )
    {
        return coldTemp;
    }

    public double getColdValve( )
    {
        return coldValve;
    }

    public double getColdPressure( )
    {
        return coldPressure;
    }

    public double getHotTemp( )
    {
        return hotTemp;
    }

    public double getHotValve( )
    {
        return hotValve;
    }

    public double getHotPressure( )
    {
        return hotPressure;
    }

}
