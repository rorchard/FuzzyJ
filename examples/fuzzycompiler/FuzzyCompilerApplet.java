/*
	A basic extension of the java.applet.Applet class
 */
package examples.fuzzycompiler;

import java.awt.*;
import java.applet.*;

public class FuzzyCompilerApplet extends Applet
{
	public void init()
	{
	 
		//{{INIT_CONTROLS
		setLayout(null);
		setSize(232,56);
		startDemo.setLabel("Start the Fuzzy Output Table Demo");
		add(startDemo);
		startDemo.setBackground(java.awt.Color.lightGray);
		startDemo.setFont(new Font("Dialog", Font.BOLD, 12));
		startDemo.setBounds(0,0,216,44);
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		startDemo.addActionListener(lSymAction);
		//}}
	}
	
	//{{DECLARE_CONTROLS
	java.awt.Button startDemo = new java.awt.Button();
	//}}
	
	

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == startDemo)
				startDemo_ActionPerformed(event);
		}
	}

	void startDemo_ActionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Frame1 f = new Frame1();
		f.setVisible(true);
		f.init();
	}
}
