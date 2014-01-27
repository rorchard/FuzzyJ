package examples.fuzzytruckswing;

import nrc.fuzzy.*;
import java.awt.*;
import javax.swing.*;
import java.text.*;

// This is the base class for truck simulator. It handles setting up
// the initial data and running the algorithm.

public class TruckSimulation extends java.lang.Thread
{
 	FuzzyTruckJApplet parent;
 	
	public static final int PAUSE = 0;
	public static final int STEP = 1;
	public static final int RUN = 2;
	
	public static final int ROWS = 7;
	public static final int COLUMNS = 5;
	
	static final double PIBY180 = Math.PI / 180;
	
	boolean pause = true, finished = false, truck_disabled = false;
	// initial state of simulation is paused
	int msg = PAUSE;
	
    static final NumberFormat nf = NumberFormat.getNumberInstance();
    
	// some things to support threads for updating GUI
    String statusText = "";
	SetStatusTextThread sstThread = new SetStatusTextThread();
	Color conclusionColor;         // color of the conclusion button background
	int conclusionI, conclusionJ;  // index values into the conclusion button matrix
    
    
	// X, Y and Phi hold the starting state for the truck
	double X = 50, Y = 50, Phi = 90;
	// Xt, Yt, Phit hold state of truck as simulation progresses
	double Xt = 50, Yt = 50, Phit = 90;
	
	// speed of truck
	double Speed = 1;
	// speed of simulation
	int SimSpeed = 1;
	
	// true when need to re-evaluate truck position
	boolean recompute = true;
	// true when tracing check box is set
	boolean tracingWanted = false;
	// true when tracing to be done during update of graphics (only set
	// to true when tracing check box is on and simulation is under way)
	boolean tracingOn = false;
	// iteration count during simulation
	int Iteration = 0;
	// true when we are to show the rule firings as the simulation proceeds
	boolean showRuleFirings;
    
    // The fuzzy definitions
	static FuzzyVariable xpos;
	static String xposTerms[] = {"LeftBig", "LeftMedium", "Centred", "RightMedium", "RightBig"};
	static FuzzySet xposFzsets[] = new FuzzySet[COLUMNS];
	static FuzzyVariable phi;
	static String phiTerms[] = {"LargeBelow90", "MediumBelow90", "SmallBelow90", "At90", "SmallAbove90", "MediumAbove90", "LargeAbove90"};
	static FuzzySet phiFzSets[] = new FuzzySet[ROWS];
	static FuzzyVariable changePhi;
	static String changePhiTerms[] = {"NB", "NM", "NS", "ZE", "PS", "PM", "PB" };
	static FuzzySet changePhiFzSets[] = new FuzzySet[ROWS];
	
	static FuzzyRule theRules[][] = new FuzzyRule[ROWS][COLUMNS]; // 1st index is phi, 2nd is xpos
	
	// OFF_COLOR is normal color for rule matrix buttons 
	private static final Color OFF_COLOR = new Color(204, 204, 204);
	// matrix of colors that show degreee of matching for a rule that fires
	private static final Color matchColors[] = new Color[256];
	
	static
	{   // use colors from white to pink to deep red to show degree of rule matching
	    int i;
	    for (i=0; i<256; i++)
	        matchColors[i] = new Color(255, i, i);
	        
	    // display 4 digits precision for angle changes
		nf.setMaximumFractionDigits(4);
		nf.setMinimumFractionDigits(4);
	}

	// default values for the rule conclusions matrix (Reset Rules button 
	// resets to these values)
	public static final String DefaultConclusions[][] = 
				{{"PS", "PM", "PM", "PB", "PB"},
				 {"NS", "PS", "PM", "PB", "PB"},
				 {"NM", "NS", "PS", "PM", "PB"},
				 {"NM", "NM", "ZE", "PM", "PM"},
				 {"NB", "NM", "NS", "PS", "PM"},
				 {"NB", "NB", "NM", "NS", "PS"},
				 {"NB", "NB", "NM", "NM", "NS"}};
				
    // current values for the rule conclusions
	public static String CurrentConclusions[][] = 
				{{"PS", "PM", "PM", "PB", "PB"},
				 {"NS", "PS", "PM", "PB", "PB"},
				 {"NM", "NS", "PS", "PM", "PB"},
				 {"NM", "NM", "ZE", "PM", "PM"},
				 {"NB", "NM", "NS", "PS", "PM"},
				 {"NB", "NB", "NM", "NS", "PS"},
				 {"NB", "NB", "NM", "NM", "NS"}};
				 
	
	// Set up the data in a constructor
	public TruckSimulation (FuzzyTruckJApplet p) {
		parent = p;
		int i, j;
		
		// define the fuzzyVariables and terms
		// define the fuzzy input values (curentXpos and currentPhi)
		// define the fuzzy output value (changePhi)
		try 
		{
          // data from Kosko's book.
	      phiFzSets[0] = new RFuzzySet(-45.0, 10.0, new RightLinearFunction());
		  phiFzSets[1] = new TriangleFuzzySet(-10.0, 25.0, 60.0);
		  phiFzSets[2] = new TriangleFuzzySet(50.0, 70.0, 90.0);
		  phiFzSets[3] = new TriangleFuzzySet(80.0, 90.0, 100.0);
		  phiFzSets[4] = new TriangleFuzzySet(90.0, 110.0, 130.0);
		  phiFzSets[5] = new TriangleFuzzySet(120.0, 155.0, 190.0);
		  phiFzSets[6] = new LFuzzySet(170.0, 225.0, new LeftLinearFunction());

		  changePhiFzSets[0] = new TriangleFuzzySet(-45.0, -30.0, -15.0);
	      changePhiFzSets[1] = new TriangleFuzzySet(-25.0, -15.0, -5.0);
	      changePhiFzSets[2] = new TriangleFuzzySet(-10.0, -5.0, 0.0);
	      changePhiFzSets[3] = new TriangleFuzzySet(-5.0, 0.0, 5.0);
	      changePhiFzSets[4] = new TriangleFuzzySet(0.0, 5.0, 10.0);
	      changePhiFzSets[5] = new TriangleFuzzySet(5.0, 15.0, 25.0);
	      changePhiFzSets[6] = new TriangleFuzzySet(15.0, 30.0, 45.0);

          xposFzsets[0] = new RFuzzySet(10.0, 35.0, new RightLinearFunction());
	      xposFzsets[1] = new TriangleFuzzySet(30.0, 40.0, 50.0);
	      xposFzsets[2] = new TriangleFuzzySet(45.0, 50.0, 55.0);
	      xposFzsets[3] = new TriangleFuzzySet(50.0, 60.0, 70.0);
	      xposFzsets[4] = new LFuzzySet(65.0, 90.0, new LeftLinearFunction());

          // Input fuzzy variable for the truck's x coordinate position
		  xpos = new FuzzyVariable("Xpos", 0, 100, "");
		  for (i=0; i<xposTerms.length; i++)
		      xpos.addTerm(xposTerms[i], xposFzsets[i]); 
          // Input fuzzy variable for the truck's angular position
		  phi = new FuzzyVariable("Phi", -90, 270, "Degrees");
		  for (i=0; i<phiTerms.length; i++)
		      phi.addTerm(phiTerms[i], phiFzSets[i]); 
          // Output fuzzy variable for changing the truck's angular position
		  changePhi = new FuzzyVariable("changePhi", -45.0, 45.0, "Degrees");
		  for (i=0; i<changePhiTerms.length; i++)
		      changePhi.addTerm(changePhiTerms[i], changePhiFzSets[i]); 

		  //define the fuzzy rules
		  for (i=0; i<phiTerms.length; i++)
		    for (j=0; j<xposTerms.length; j++)
		    { 
		     theRules[i][j] = new FuzzyRule();
		     theRules[i][j].addAntecedent(new FuzzyValue(xpos, xposTerms[j]));
		     theRules[i][j].addAntecedent(new FuzzyValue(phi, phiTerms[i]));
		     theRules[i][j].addConclusion(new FuzzyValue(changePhi, CurrentConclusions[i][j]));
		    }
		}
		catch (FuzzyException fe)
		{System.out.println(fe);}
		
	}

	// Set the truck to its initial set of Rule conclusions
	public void resetRules() 
	{
		for (int i=0; i<ROWS; i++)
		   for (int j=0; j<COLUMNS; j++)
		   {
		     CurrentConclusions[i][j] = DefaultConclusions[i][j];
		   }
	}

	// Set the truck to its initial state before any computations
	public void reset() 
	{
		Xt = X;
		Yt = Y;
		Phit = Phi;
        
		recompute = true;
		finished = false;
		truck_disabled = false;
		Iteration = 0;

        if (parent.viewArea.getGraphics() != null) 
           parent.viewArea.paintComponent(parent.viewArea.getGraphics());
		parent.JLabelSimulationStatus.setText(" ");

        // reset background color of rule matrix buttons
        parent.resetConclusionButtonsBackground(OFF_COLOR);
	}

    public void setX( double x )
    {  X = x;
    }

    public void setY( double y )
    {  Y = y;
    }

    public void setTruckAngle( double a )
    {  Phi = a;
    }

    public void setSimSpeed( int s )
    {  SimSpeed = s;
    }

    public void setTruckSpeed( double s )
    {  Speed = s;
    }
    
    public void setTracing( boolean b )
    {  tracingWanted = b;
    }
    
    public boolean getTracing( )
    {  return tracingWanted;
    }

    public void setShowRuleFirings( boolean b )
    {  showRuleFirings = b;
    }
    
    public boolean getShowRuleFirings( )
    {  return showRuleFirings;
    }

    public double getTruckAngle( )
    {  return Phi;
    }

    public int getSimSpeed( )
    {  return SimSpeed;
    }

    public double getTruckSpeed( )
    {  return Speed;
    }

    public int getRows( )
    {
        return ROWS;
    }

    public int getColumns( )
    {  return COLUMNS;
    }

    public String getConclusionExpression(int i, int j)
    {  return CurrentConclusions[i][j];
    }

    public void setConclusionExpression(String s, int i, int j)
    {  CurrentConclusions[i][j] = s;
    }


	// Core of the fuzzy truck algorithm
	public void run() 
	{   int m, i=0, j=0;
		double angleInRadians;
        double changePhiValue;
		
		ResetConclusionButtonsThread rcbThread = new ResetConclusionButtonsThread();
		SetConclusionButtonColorThread scbcThread = new SetConclusionButtonColorThread();
		ViewAreaPaintComponentThread vapcThread = new ViewAreaPaintComponentThread();

        while (true)
		{ // need sync. for button presses. when a msg. 
          // comes in, we start the simulation.
          m = getMsg();
                      
          if (!simulationFinished()) 
          { FuzzyValue globalResult = null;
            changePhiValue = 0.0;
            try
            { // if this is the first time we run the alg.
              // then get the rules from the matrix.
              if (Iteration == 0) 
			  { Xt = X;
	  		    Yt = Y;
	 			Phit = Phi;
	 			// get the correct conclusions for each rule
                for (i=0; i<phiTerms.length; i++)
                    for (j=0; j<xposTerms.length; j++)
		            { 
		              String fzExpression = CurrentConclusions[i][j];
                      theRules[i][j].removeAllConclusions();
		              if (!fzExpression.equals(" "))
		                 theRules[i][j].addConclusion(new FuzzyValue(changePhi, fzExpression));
		            }
			  }
			  // reset the conclusion buttons backgrounds
              SwingUtilities.invokeAndWait( rcbThread );

		      // compute the results of the rule firing for the current xpos and phi values.
		      FuzzyValueVector fvvInputs = new FuzzyValueVector(2);
			  FuzzyValueVector result = null;
			  fvvInputs.addFuzzyValue(new FuzzyValue(xpos, new TriangleFuzzySet(Xt, Xt, Xt)));
			  fvvInputs.addFuzzyValue(new FuzzyValue(phi, new TriangleFuzzySet(Phit, Phit, Phit)));
              for (i=0; i<phiTerms.length; i++)
              {  for (j=0; j<xposTerms.length; j++)
		         { FuzzyValueVector concFvv = theRules[i][j].getConclusions();
		           if ( concFvv != null &&
		                concFvv.size() > 0 &&
		                theRules[i][j].testRuleMatching(fvvInputs)
		              )
		           { // execute rule with required input values
		             result = theRules[i][j].execute(fvvInputs);
		             FuzzyValue fv = result.fuzzyValueAt(0);
		             // set color of rule button background to indicate degree of matching
		             if (showRuleFirings)
		             {
		               double maxY = fv.getMaxY();
		               int colorDegree = (int)(255.0*(1.0-maxY));
		               conclusionColor = matchColors[colorDegree];
		               conclusionI = i;
		               conclusionJ = j;
                       SwingUtilities.invokeAndWait( scbcThread );
                     }
                     
		             // add to global result for all rules 
   		             if (globalResult == null)
    		            globalResult = fv;
    		         else
    		            globalResult = globalResult.fuzzyUnion(fv);
 		           }
		         }
              }
		      // new values based on the alogorithm for finding the new
			  // angle (see Kosko's book).
		      // globalResult could be null if we take out some rules
		      // that and no rules now match ... 
              if (globalResult != null) 
                 changePhiValue = globalResult.momentDefuzzify();
		      Phit += changePhiValue;
			  Xt += Speed * Math.cos(angleInRadians = Phit * PIBY180);
			  Yt -= Speed * Math.sin(angleInRadians);
            }
            catch (FuzzyException fe)
            {  System.out.println(fe+"\n"+i+", "+j);
            }
		    catch (Throwable t) 
		    {  t.printStackTrace();
			   //Ensure the application exits with an error condition.
			   System.exit(1);
			}

			// compute the new coords for display
			parent.viewArea.computeTruckCoords();

			// test if the simulation is complete
			test(Xt, Yt, Phit);

			// update the applet
			Iteration++;
			tracingOn = tracingWanted;
			
			statusText = "Angle change = " + nf.format(changePhiValue) + ", Iteration = " + Iteration;
            try 
            {  
               SwingUtilities.invokeLater( vapcThread );
               SwingUtilities.invokeAndWait( sstThread );
            }
		    catch (Throwable t) 
		    { t.printStackTrace();
			  //Ensure the application exits with an error condition.
			  System.exit(1);
			}
			    
			tracingOn = false;

			// if someone hit the pause button, we have to 
			// stop running temporarily
			if (m == STEP) pause = true;
		  }
		}
	}

	// test if the sim. is complete or if the truck left the 
	// canvas.
	private void test(double x, double y, double angle) 
	{   if ((y <= 0.3) && 
	        (Math.abs(50.0 - x) <= 1.0) &&
	        (Math.abs(angle-90.0) <= 5.0)
	       )
	    {
			finished = true;
			return;
		}
		if ((y < 0) || (x < 0) || (x > 100) || ((y > 100) &&
		    (Math.abs(x - 50) > 1))) 
		    truck_disabled = true;
	}

	// print the information if the sim. is complete or
	// if the truck has left the canvas.
	private boolean simulationFinished() 
	{
        try 
        {  
		  if (finished) 
		  {
		    statusText = "Simulation Complete - parking successful, Iterations = "+Iteration;
		    SwingUtilities.invokeLater( sstThread );
			pause = true;
	 		return true;
		  }
		  if (truck_disabled) 
		  {
	 		statusText = "Simulation Complete - parking unsuccessful, Iterations = "+Iteration;
	 		SwingUtilities.invokeLater( sstThread );
			pause = true;
	    	return true;
		  }
        }
		catch (Throwable t) 
		{ t.printStackTrace();
		  //Ensure the application exits with an error condition.
		  System.exit(1);
		}
		
		return false;
	}

	// check for any button presses
	public synchronized int getMsg() 
	{
        // disable tooltips while simulation running ... they appear to cause 
        // problems with painting .... sometimes???
        if (pause) 
        {  if (!(ToolTipManager.sharedInstance().isEnabled()))
              ToolTipManager.sharedInstance().setEnabled(true);
        }
        else
        {  if (ToolTipManager.sharedInstance().isEnabled())
              ToolTipManager.sharedInstance().setEnabled(false);
        }
        
		while (pause == true) 
		{   try 
		    {   wait(500); 
		    } 
			catch (InterruptedException e) 
			{}
		}
		
		try 
		{   if (SimSpeed > 1) 
		       sleep(10 * (SimSpeed-1));
		    else
		       yield();
		} 
		catch (InterruptedException e) 
		{}
		return msg;
	}

	// external classes send messages to this class to 
	// execute or stop iterating
	public synchronized void putMsg(int m) 
	{
		msg = m;
		pause = (msg == RUN) ? false : (msg == STEP) ? false : true;
//		notify();
	}
	
	
    class ResetConclusionButtonsThread implements Runnable 
    {   
       public void run() 
       { 
          // reset background color of rule matrix buttons
          parent.resetConclusionButtonsBackground(OFF_COLOR);
       }
    }
	
    class SetConclusionButtonColorThread implements Runnable 
    {   
       public void run() 
       { 
          // set background color of a rule matrix conclusion button
          parent.setConclusionButtonBackground(conclusionI, conclusionJ, conclusionColor);
       }
    }
	
    class SetStatusTextThread implements Runnable 
    {   
       public void run() 
       { 
          // set text in status area
          parent.JLabelSimulationStatus.setText(statusText);
       }
    }
    
    class ViewAreaPaintComponentThread implements Runnable 
    {   
       public void run() 
       { 
          // paint the truck driving area
          parent.viewArea.paintComponent(parent.viewArea.getGraphics());
       }
    }
}
