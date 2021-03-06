/*
	This simple extension of the java.awt.Frame class
	contains all the elements necessary to act as the
	main window of an application.
 */
 
package fuzzytesting.fuzzytestMixedJavaJess;
 
import jess.*;
import nrc.fuzzy.*;
import nrc.fuzzy.jess.*;
import java.awt.*;
import java.util.*;

public class FuzzyTestMixedJavaJess extends Frame
{
	public FuzzyTestMixedJavaJess()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.

		//{{INIT_CONTROLS
		setLayout(null);
		setSize(405,305);
		setVisible(false);
		openFileDialog1.setMode(FileDialog.LOAD);
		openFileDialog1.setTitle("Open");
		//$$ openFileDialog1.move(40,277);
		setTitle("A Basic Application");
		//}}

		//{{INIT_MENUS
		menu1.setLabel("File");
		menu1.add(miNew);
		miNew.setLabel("New");
		miNew.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_N,false));
		menu1.add(miOpen);
		miOpen.setLabel("Open...");
		miOpen.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_O,false));
		menu1.add(miSave);
		miSave.setLabel("Save");
		miSave.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_S,false));
		menu1.add(miSaveAs);
		miSaveAs.setLabel("Save As...");
		menu1.addSeparator();
		menu1.add(miExit);
		miExit.setLabel("Exit");
		mainMenuBar.add(menu1);
		menu2.setLabel("Edit");
		menu2.add(miCut);
		miCut.setLabel("Cut");
		miCut.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_X,false));
		menu2.add(miCopy);
		miCopy.setLabel("Copy");
		miCopy.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_C,false));
		menu2.add(miPaste);
		miPaste.setLabel("Paste");
		miPaste.setShortcut(new MenuShortcut(java.awt.event.KeyEvent.VK_V,false));
		mainMenuBar.add(menu2);
		menu3.setLabel("Help");
		menu3.add(miAbout);
		miAbout.setLabel("About..");
		mainMenuBar.add(menu3);
		mainMenuBar.setHelpMenu(menu3);
		//$$ mainMenuBar.move(4,277);
		setMenuBar(mainMenuBar);
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		miOpen.addActionListener(lSymAction);
		miAbout.addActionListener(lSymAction);
		miExit.addActionListener(lSymAction);
		//}}
	}

	public FuzzyTestMixedJavaJess(String title)
	{
		this();
		setTitle(title);
	}

    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
		super.setVisible(b);
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
	    
	    String appDir = "";
		if (args.length > 0)
		{	appDir = args[0];
		}

		(new FuzzyTestMixedJavaJess()).setVisible(true);
		try
		  {
		    FuzzySet fsHot = new FuzzySet(xHot, yHot, 2);
		    FuzzySet fsCold = new FuzzySet(xCold, yCold, 2);
		    FuzzySet fSets[] = {fsHot, fsCold};
		    String fTerms[] = {"hot", "cold"};
            FuzzyValue fvals[] = new FuzzyValue[2];
            FuzzyVariable temp = new FuzzyVariable("temperature", 0, 100, "C", fTerms, fSets, 2);
            FuzzyVariable pressure = new FuzzyVariable("pressure", 0, 10, "kilo-pascals");
		    FuzzyVariable temp2 = new FuzzyVariable("temp2", 0, 100, "C");
		   // temp.addTerm("hot", xHot, yHot, 2);
		   // temp.addTerm("cold", xCold, yCold, 2);
		    temp.addTerm("veryHot", "very hot");
		    temp.addTerm("medium", "(not hot and (not cold))");
		    temp2.addTerm("hot", xHot, yHot, 2);
		    temp2.addTerm("cold", xCold, yCold, 2);
		    temp2.addTerm("veryHot", "very hot");
		    temp2.addTerm("medium", "(not hot and (not cold))");
		    pressure.addTerm("low", new ZFuzzySet(2.0, 5.0));
		    pressure.addTerm("medium", new PIFuzzySet(5.0, 2.5));
		    pressure.addTerm("high", new SFuzzySet(5.0, 8.0));
		    System.out.println(temp2);
		    mods = Modifiers.getModifierNames();
		    System.out.println("Modifiers:");
		    for (i=0; i < mods.length; i++)
		       System.out.println("\t" + mods[i]);

		    fval = new FuzzyValue(temp, x, y, 3);
		    System.out.println(fval.getMembership(1.25));
		    System.out.println(fval.toString());
		    FuzzyValue fval2 = Modifiers.not(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);
		    fval2 = Modifiers.very(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);
		    fval2 = Modifiers.extremely(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.somewhat(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.more_or_less(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.call("More_or_Less", fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.plus(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.norm(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = Modifiers.slightly(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);
		    fvals[0] = fval;
		    fvals[1] = fval2;
		    System.out.println(FuzzyValue.plotFuzzyValues(".+", 1, 3, fvals));
		    System.out.println("maxmin of slightly fval and fval = " + fval.maximumOfIntersection(fval2));
		    System.out.println("match of slightly fval and fval at threshold 0.8 = " + fval.fuzzyMatch(fval2, 0.8));
		    System.out.println("match of slightly fval and fval at threshold 0.75 = " + fval.fuzzyMatch(fval2, 0.75));
		    Interval.setToStringPrecision(5);
		    System.out.println("Support is: " + fval2.getSupport());
		    System.out.println("Weak Alpha cut at 0.0 is: " + fval2.getAlphaCut(Parameters.WEAK, 0.0));
		    System.out.println("Weak Alpha cut at 0.52 is: " + fval2.getAlphaCut(Parameters.WEAK, 0.52));
		    System.out.println("Strong Alpha cut at 0.52 is: " + fval2.getAlphaCut(Parameters.STRONG, 0.52));

		    fval2 = Modifiers.intensify(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);
		    fvals[0] = fval;
		    fvals[1] = fval2;
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 5, fvals));

		    fval2 = Modifiers.above(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);
		    fval2 = Modifiers.below(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval2 = (new VeryModifier()).call(fval);
		    System.out.println(fval2.getMembership(1.25));
		    System.out.println(fval2);

		    fval = new FuzzyValue(temp, "very hot or cold");
		    fval3 = new FuzzyValue(temp, "medium");
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+", 0, 40));
		    fvals[0] = fval;
		    fvals[1] = fval3;
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 40, fvals));

		    fval = fval.fuzzyUnion(fval2);
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);

		    fval = new FuzzyValue(temp, new PIFuzzySet(50, 10, 11));
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+", 30, 70));

		    fval = new FuzzyValue(temp, new ZFuzzySet(10.0, 50.0, 11));
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",0,60));

		    fval = new FuzzyValue(temp, new RectangleFuzzySet(10.0, 50.0));
		    System.out.println(fval.getMembership(9.999));
		    System.out.println(fval.getMembership(10));
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval.getMembership(50));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",5,55));
		    System.out.println("Moment is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment is " + fval.momentDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());

		    fval = new FuzzyValue(temp, new TrapezoidFuzzySet(10.0, 15, 30, 50.0));
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",5,55));
		    System.out.println("Moment is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment is " + fval.momentDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());

		    fval = new FuzzyValue(temp, new LRFuzzySet(10.0, 15, 30, 50.0,
		                          new SFunction(), new ZFunction()));
		    System.out.println(fval.getMembership(27.5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",10,50));

		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    fval = new FuzzyValue(temp, new PIFuzzySet(1, 5));
		    System.out.println(new PIFuzzySet(1, 5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",0,10));
		    FuzzyValue.setConfineFuzzySetsToUOD(false);
		    
		    FuzzyValue.setConfineFuzzySetsToUOD(true);
		    fval = new FuzzyValue(temp, new PIFuzzySet(99, 5));
		    System.out.println(new PIFuzzySet(99, 5));
		    System.out.println(fval);
		    System.out.println(fval.plotFuzzyValue("+",90,100));
		    System.out.println("Moment is " + 
		         fval.getFuzzySet().momentDefuzzify(fval.getMinUOD(), fval.getMaxUOD()));
		    System.out.println("Moment is " + fval.momentDefuzzify());
		    System.out.println("Maximum defuzzify is " + fval.maximumDefuzzify());
		    FuzzyValue.setConfineFuzzySetsToUOD(false);
		    System.out.println(fval.horizontalIntersection(.75).plotFuzzyValue("+",90,100));
		    System.out.println(fval.horizontalUnion(.75).plotFuzzyValue("+",90,100));
		    System.out.println(fval.fuzzyScale(.75).plotFuzzyValue("+",90,100));

		    Interval.setToStringPrecision(5);
		    System.out.println("Support is: " + fval.getSupport());
		    System.out.println("Weak Alpha cut at 0.0 is: " + fval.getAlphaCut(Parameters.WEAK, 0.0));
		    System.out.println("Weak Alpha cut at 0.52 is: " + fval.getAlphaCut(Parameters.WEAK, 0.52));

            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "cold");
            System.out.println("non intersection of hot and cold is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "cold");
            System.out.println("NO intersection of hot and cold is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "medium");
            System.out.println("non intersection of hot and medium is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "medium");
            System.out.println("NO intersection of hot and medium is " + 
                                fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet())
                              );
            Thread.sleep(500);
            System.out.println("Starting loop 1"); System.out.flush();
            for (int jj=0; jj<10000; jj++)
               fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 2"); System.out.flush();
            for (int jj=0; jj<10000; jj++)
               fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 3"); System.out.flush();
            for (int jj=0; jj<10000; jj++)
               fval.maximumOfIntersection(fval2);
            System.out.println("Ending all loops");System.out.flush();

            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "not hot");
            System.out.println("non intersection of hot and not hot is " + 
                                fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet())
                              );
            fval = new FuzzyValue(temp, "hot");
            fval2 = new FuzzyValue(temp, "not hot");
            System.out.println("NO intersection of hot and not hot is " + 
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
            for (int jj=0; jj<10000; jj++)
               fval.getFuzzySet().nonIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 2"); System.out.flush();
            for (int jj=0; jj<10000; jj++)
               fval.getFuzzySet().noIntersectionTest(fval2.getFuzzySet());
            System.out.println("Starting loop 3"); System.out.flush();
            for (int jj=0; jj<10000; jj++)
               fval.maximumOfIntersection(fval2);
            System.out.println("Ending all loops");System.out.flush();
            
            // let's try some rules --- 
            FuzzyRule rule1 = new FuzzyRule();
            fval = new FuzzyValue( temp, "hot");
            fval2 = new FuzzyValue( pressure, "low or medium");
            fval3 = new FuzzyValue( temp, "medium");
		    System.out.println("Medium is ... " + fval3);
            fval3 = new FuzzyValue( temp, "very medium");
		    System.out.println("Very medium is ... " + fval3);
		    fvals[0] = fval;
		    fvals[1] = fval3;
            rule1.addAntecedent(fval);
            rule1.addConclusion(fval2);
            rule1.addInput(fval3);
            FuzzyValueVector fvv = rule1.execute();
		    System.out.println(FuzzyValue.plotFuzzyValues("*+", 0, 50, fvals));
		    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
		    System.out.println(fval2.plotFuzzyValue("*", 0, 10));
		    System.out.println("Firing rule many times ... " + System.currentTimeMillis());
		    for (i=0; i<50000; i++)
              fvv = rule1.execute(new LarsenProductMaxMinRuleExecutor());
		    System.out.println("Fired rule many times ... " + System.currentTimeMillis());
		    System.out.println(fvv.fuzzyValueAt(0).plotFuzzyValue("*", 0, 10));
		    
		    System.out.println("Similarity of 'temp very medium' and 'temp hot' is: " +
		                       fval.similarity(fval3));
		    
/*
		    fval3 = new FuzzyValue(temp2, x, y, 3);
		    fval3 = fval3.fuzzyUnion(fval2);
		    System.out.println(fval3.getMembership(27.5));
		    System.out.println(fval3);
*/		    

            // Stuff to test Jess with Java
            FuzzyRete rete = new FuzzyRete();
            rete.executeCommand("(batch " + appDir + "jessTest.clp)");
            FuzzyVariable OUT_Speed = (FuzzyVariable) rete.fetch("SPEED_FUZZYVARIABLE").externalAddressValue(null);
            FuzzyValue inSpeed = new FuzzyValue(OUT_Speed, new TrapezoidFuzzySet(0.0, 5.0, 10.0, 20.0));
            rete.reset();
            rete.store("IN_SPEED", inSpeed);
            rete.executeCommand("(printout t \"Facts (before run) \" (facts) crlf)");
            rete.executeCommand("(assert (Speed (fetch IN_SPEED)))");
			rete.executeCommand("(printout t \"Facts (before run) \" (facts) crlf)");
            rete.run();
			rete.executeCommand("(printout t \"Facts (after run) \" (facts) crlf)");
		  }
		catch (Exception e)
		  {System.err.println("error: " + e); }
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		setSize(insets().left + insets().right + d.width, insets().top + insets().bottom + d.height);
		Component components[] = getComponents();
		for (int i = 0; i < components.length; i++)
		{
			Point p = components[i].getLocation();
			p.translate(insets().left, insets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	//{{DECLARE_CONTROLS
	java.awt.FileDialog openFileDialog1 = new java.awt.FileDialog(this);
	//}}

	//{{DECLARE_MENUS
	java.awt.MenuBar mainMenuBar = new java.awt.MenuBar();
	java.awt.Menu menu1 = new java.awt.Menu();
	java.awt.MenuItem miNew = new java.awt.MenuItem();
	java.awt.MenuItem miOpen = new java.awt.MenuItem();
	java.awt.MenuItem miSave = new java.awt.MenuItem();
	java.awt.MenuItem miSaveAs = new java.awt.MenuItem();
	java.awt.MenuItem miExit = new java.awt.MenuItem();
	java.awt.Menu menu2 = new java.awt.Menu();
	java.awt.MenuItem miCut = new java.awt.MenuItem();
	java.awt.MenuItem miCopy = new java.awt.MenuItem();
	java.awt.MenuItem miPaste = new java.awt.MenuItem();
	java.awt.Menu menu3 = new java.awt.Menu();
	java.awt.MenuItem miAbout = new java.awt.MenuItem();
	//}}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == FuzzyTestMixedJavaJess.this)
				Frame1_WindowClosing(event);
		}
	}

	void Frame1_WindowClosing(java.awt.event.WindowEvent event)
	{
		setVisible(false);	// hide the Frame
		dispose();			// free the system resources
		System.exit(0);		// close the application
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == miOpen)
				miOpen_Action(event);
			else if (object == miAbout)
				miAbout_Action(event);
			else if (object == miExit)
				miExit_Action(event);
		}
	}

	void miAbout_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from About Create and show as modal
		(new AboutDialog(this, true)).setVisible(true);
		//}}
	}

	void miExit_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from Exit Create and show as modal
		(new QuitDialog(this, true)).setVisible(true);
		//}}
	}

	void miOpen_Action(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Action from Open... Show the OpenFileDialog
		int		defMode			= openFileDialog1.getMode();
		String	defTitle		= openFileDialog1.getTitle();
		String defDirectory	= openFileDialog1.getDirectory();
		String defFile			= openFileDialog1.getFile();

		openFileDialog1 = new java.awt.FileDialog(this, defTitle, defMode);
		openFileDialog1.setDirectory(defDirectory);
		openFileDialog1.setFile(defFile);
		openFileDialog1.setVisible(true);
		//}}
	}
}

