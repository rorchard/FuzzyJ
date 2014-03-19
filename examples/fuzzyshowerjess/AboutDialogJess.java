/*
	A basic extension of the java.awt.Dialog class
 */
package examples.fuzzyshowerjess;

import java.awt.*;

public class AboutDialogJess extends Dialog {

	public AboutDialogJess(Frame parent, boolean modal)
	{
		super(parent, modal);

		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.

		//{{INIT_CONTROLS
		setLayout(null);
		setBackground(new java.awt.Color(122,239,196));
		setForeground(new java.awt.Color(64,0,64));
		setSize(497,329);
		setVisible(false);
		label1.setText("Fuzzy Shower Demo");
		label1.setAlignment(java.awt.Label.CENTER);
		add(label1);
		label1.setFont(new Font("Dialog", Font.BOLD, 12));
		label1.setBounds(168,24,200,20);
		okButton.setLabel("OK");
		add(okButton);
		okButton.setFont(new Font("Dialog", Font.BOLD, 12));
		okButton.setBounds(228,84,66,27);
		label2.setText("National Research Council Canada");
		label2.setAlignment(java.awt.Label.CENTER);
		add(label2);
		label2.setFont(new Font("Dialog", Font.BOLD, 12));
		label2.setBounds(168,48,200,20);
		textArea.setEditable(false);
		add(textArea);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		textArea.setBounds(24,132,450,186);
		setTitle("About");
		//}}

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		okButton.addActionListener(lSymAction);
		//}}

	}

	public AboutDialogJess(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
                Dimension d = getSize();

		super.addNotify();

		// Only do this once.
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

		// Used for addNotify check.
		fComponentsAdjusted = true;
	}

	public void setVisible(boolean b)
	{
	    if (b)
	    {
    		Rectangle bounds = getParent().bounds();
    		Rectangle abounds = bounds();

    		move(bounds.x + (bounds.width - abounds.width)/ 2,
    			 bounds.y + (bounds.height - abounds.height)/2);
	    }

		super.setVisible(b);
	}

	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Button okButton = new java.awt.Button();
	java.awt.Label label2 = new java.awt.Label();
	java.awt.TextArea textArea = new java.awt.TextArea();
	//}}

        // Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowOpened(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == AboutDialogJess.this)
				AboutDialog_WindowOpened(event);
		}

		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == AboutDialogJess.this)
				AboutDialog_WindowClosing(event);
		}
	}

	void AboutDialog_WindowClosing(java.awt.event.WindowEvent event)
	{
                dispose();
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == okButton)
				okButton_Clicked(event);
		}
	}

	void okButton_Clicked(java.awt.event.ActionEvent event)
	{
		//{{CONNECTION
		// Clicked from okButton Hide the Dialog
                dispose();
		//}}
	}

	void AboutDialog_WindowOpened(java.awt.event.WindowEvent event)
	{
		// to do: code goes here.
		textArea.setText
		   (  "This simple demo shows the control of a simulated shower.\n" +
		      "There are 10 rules used to control the shower. These rules are\n" +
		      "not really adequate or optimized in any way, but with them and\n" +
		      "some simple coded heuristics for checking error situations, they\n" +
		      "do a good job of keeping the shower under control. The object is\n" +
		      "to get a flow close to 12 litres/min and a temperature close to 36 C.\n" +
		      "If the 'show rule firings' checkbox is on a 2nd window will open\n" +
		      "and results of the rule firing on each cycle of the inferencing\n" +
		      "will be displayed (a cycle is a firing of all rules that can fire\n" +
		      "and the global combination of the rule outputs, followed by a \n" +
		      "defuzzification of the outputs to change the hot and cold valve\n" +
		      "positions. The user can operate in manual mode or let the system do\n" +
		      "control by setting the 'auto Fuzzy control' checkbox. Try moving\n" +
		      "the sliders and getting the temperature and flow in the target\n" +
		      "ranges. When in auto control mode things happen very fast and it \n" +
		      "is difficult to see all that is happening. To slow things down enter\n" +
		      "the number of milliseconds to delay between rule firings in the\n" +
		      "text box at the bottom left. Questions etc. can be sent to:\n\n" +
		      "\t\tbob.orchard@nrc.ca\n"
		   );
	}
}