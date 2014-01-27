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
package examples.fuzzyshowerjess;

import java.awt.*;
import java.applet.*;

import nrc.fuzzy.*;
import nrc.fuzzy.jess.*;
import jess.*;


public class ShowerSimulateJess implements Runnable
{
    FuzzyVariable outTemp;
    FuzzyVariable outFlow;
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
    ShowerFrameJess sf;
    Applet applet = null;
    
    
    FuzzyRete rete;

    public ShowerSimulateJess( ShowerFrameJess showerFrame, Applet app )
    {
        sf = showerFrame;
        applet = app;
        init();
    }
	public void init()
	{
	  sf.repaint();
	  
	  // Connect to a Jess Rete engine
      rete = new FuzzyRete();
      if (applet != null)
         rete.setApplet(applet);
      // Read in the rules, reset to get ready to run rules, and fire the init rule
      try
      {
		java.net.URL clpFileUrl;
		if (sf.applicationDirectory.equals(new String("")))
		{	clpFileUrl = symantec.itools.net.RelativeURL.getURL("FuzzyShowerJess.clp");
	 	    if (!clpFileUrl.toString().contains("examples/fuzzyshowerjess"))
	 		   clpFileUrl = symantec.itools.net.RelativeURL.getURL("examples/fuzzyshowerjess/FuzzyShowerJess.clp");
		}
		else
			clpFileUrl = new java.net.URL("file:/" + sf.applicationDirectory + "FuzzyShowerJess.clp");
      	String clpFile = "(batch \"" + clpFileUrl.getFile() + "\")";
        rete.executeCommand(clpFile);
        rete.executeCommand("(reset)");
        rete.executeCommand("(run 1)");
        outTemp = (FuzzyVariable)(rete.fetch("TEMPFUZZYVARIABLE")).externalAddressValue(null);
        outFlow = (FuzzyVariable)(rete.fetch("FLOWFUZZYVARIABLE")).externalAddressValue(null);
      }
	  catch (java.net.MalformedURLException error) 
	  { System.out.println("*** ERROR *** " + error); }
	  catch (JessException je)
      { System.err.println(je); }
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
        
        try
        {
          rete.store("FLOW", inputFlow);
          rete.store("TEMP", inputTemp);
          rete.executeCommand("(assert (temp (fetch TEMP)) (flow (fetch FLOW)))");
          rete.executeCommand("(run)");
          coldValveChange = rete.fetch("COLDVALVECHANGE").numericValue(null);
          hotValveChange = rete.fetch("HOTVALVECHANGE").numericValue(null);
        }
        catch (JessException je)
        {
          System.err.println(je);
        }

        counter++; // count each inference loop (cycle)

	    // show results if requested  ... start of new cycle
	    if (sf.showFiringsCheckbox.getState())
	    {
	        sf.inferenceCycleResults.resultsTextArea.append("Cycle " + counter + ":\n");
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
	        try
	        { String rulesThatFired = rete.fetch("RULESTHATFIRED").stringValue(null);
	          rulesThatFired = rulesThatFired.replace('!', '\t');
	          rulesThatFired = rulesThatFired.replace('%', '\n');
              sf.inferenceCycleResults.resultsTextArea.append
                 ( rulesThatFired +
                   "\tCold Valve Change: " + scaledColdValveChange +
                   "\n\tHot Valve Change:  " + scaledHotValveChange + 
	               "\n\tCold Valve: " + coldValve + "\n\tHot Valve: " + hotValve +
	               "\n\tShowerTemp: " + showerTemp +  "\n\tShower Flow: " + showerFlow + "\n" 
	             );
            }
            catch (JessException je)
            {
              System.err.println(je);
            }
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
