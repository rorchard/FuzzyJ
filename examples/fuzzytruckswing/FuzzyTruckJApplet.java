package examples.fuzzytruckswing;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * A basic extension of the javax.swing.JApplet class
 */
public class FuzzyTruckJApplet extends JApplet
{
	public void init()
	{
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		setSize(710,472);
		JPanelRuleButtons.setBorder(etchedBorder1);
		JPanelRuleButtons.setToolTipText("Click on a Rule Conclusion to change it");
		JPanelRuleButtons.setLayout(new GridLayout(7,5,2,2));
		getContentPane().add(JPanelRuleButtons);
		JPanelRuleButtons.setBounds(472,12,225,210);
		//$$ etchedBorder1.move(672,480);
		RadioPanel1.setBorder(etchedBorder1);
		RadioPanel1.setLayout(new GridLayout(8,1,0,2));
		getContentPane().add(RadioPanel1);
		RadioPanel1.setBounds(420,12,48,210);
		JRadioButtonPB.setText("PB");
		JRadioButtonPB.setActionCommand("PB");
		JRadioButtonPB.setToolTipText("Positive Big");
		radioButtonGroup1.add(JRadioButtonPB);
		RadioPanel1.add(JRadioButtonPB);
		JRadioButtonPB.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonPB.setBounds(2,2,44,24);
		JRadioButtonPM.setText("PM");
		JRadioButtonPM.setActionCommand("PM");
		JRadioButtonPM.setToolTipText("Positive Medium");
		radioButtonGroup1.add(JRadioButtonPM);
		RadioPanel1.add(JRadioButtonPM);
		JRadioButtonPM.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonPM.setBounds(2,28,44,24);
		JRadioButtonPS.setText("PS");
		JRadioButtonPS.setActionCommand("PS");
		JRadioButtonPS.setToolTipText("Positive Small");
		radioButtonGroup1.add(JRadioButtonPS);
		RadioPanel1.add(JRadioButtonPS);
		JRadioButtonPS.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonPS.setBounds(2,54,44,24);
		JRadioButtonZE.setText("ZE");
		JRadioButtonZE.setActionCommand("ZE");
		JRadioButtonZE.setToolTipText("Zero");
		radioButtonGroup1.add(JRadioButtonZE);
		RadioPanel1.add(JRadioButtonZE);
		JRadioButtonZE.setBackground(new java.awt.Color(204,204,204));
		JRadioButtonZE.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonZE.setBounds(2,80,44,24);
		JRadioButtonNS.setText("NS");
		JRadioButtonNS.setActionCommand("NS");
		JRadioButtonNS.setToolTipText("Negative Small");
		radioButtonGroup1.add(JRadioButtonNS);
		RadioPanel1.add(JRadioButtonNS);
		JRadioButtonNS.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonNS.setBounds(2,106,44,24);
		JRadioButtonNM.setText("NM");
		JRadioButtonNM.setActionCommand("NM");
		JRadioButtonNM.setToolTipText("Negative Medium");
		radioButtonGroup1.add(JRadioButtonNM);
		RadioPanel1.add(JRadioButtonNM);
		JRadioButtonNM.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonNM.setBounds(2,132,44,24);
		JRadioButtonNB.setText("NB");
		JRadioButtonNB.setActionCommand("NB");
		JRadioButtonNB.setToolTipText("Negative Big");
		radioButtonGroup1.add(JRadioButtonNB);
		RadioPanel1.add(JRadioButtonNB);
		JRadioButtonNB.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonNB.setBounds(2,158,44,24);
		JRadioButtonKill.setText("Kill");
		JRadioButtonKill.setActionCommand("Kill");
		JRadioButtonKill.setToolTipText("Kill (remove) rule");
		radioButtonGroup1.add(JRadioButtonKill);
		RadioPanel1.add(JRadioButtonKill);
		JRadioButtonKill.setFont(new Font("Dialog", Font.BOLD, 11));
		JRadioButtonKill.setBounds(2,184,44,24);
		JPanel2.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		getContentPane().add(JPanel2);
		JPanel2.setBounds(418,280,286,35);
		JButtonGo.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		JButtonGo.setText("Go");
		JButtonGo.setToolTipText("Park the truck");
		JPanel2.add(JButtonGo);
		JButtonGo.setFont(new Font("Dialog", Font.BOLD, 12));
		JButtonGo.setBounds(11,5,49,25);
		JButtonPause.setText("Pause");
		JButtonPause.setToolTipText("Pause the simulation");
		JPanel2.add(JButtonPause);
		JButtonPause.setFont(new Font("Dialog", Font.BOLD, 12));
		JButtonPause.setBounds(65,5,71,25);
		JButtonStep.setText("Step");
		JButtonStep.setToolTipText("Do one step of the simulation");
		JPanel2.add(JButtonStep);
		JButtonStep.setFont(new Font("Dialog", Font.BOLD, 12));
		JButtonStep.setBounds(141,5,61,25);
		JButtonReset.setText("Reset");
		JButtonReset.setToolTipText("Reset Truck to starting position");
		JPanel2.add(JButtonReset);
		JButtonReset.setFont(new Font("Dialog", Font.BOLD, 12));
		JButtonReset.setBounds(207,5,67,25);
		JButtonGo.setActionCommand("Go");
		JButtonPause.setActionCommand("Pause");
		JButtonStep.setActionCommand("Step");
		JButtonReset.setActionCommand("Reset");
		JPanelUpperStatus.setBorder(etchedBorder1);
		JPanelUpperStatus.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		getContentPane().add(JPanelUpperStatus);
		JPanelUpperStatus.setBackground(java.awt.Color.pink);
		JPanelUpperStatus.setBounds(10,12,404,24);
		JLabelXPos.setText("X = 50.0");
		JPanelUpperStatus.add(JLabelXPos);
		JLabelXPos.setFont(new Font("MonoSpaced", Font.BOLD, 12));
		JLabelXPos.setBounds(143,7,56,16);
		JLabelYPos.setText("Y = 50.0");
		JPanelUpperStatus.add(JLabelYPos);
		JLabelYPos.setFont(new Font("MonoSpaced", Font.BOLD, 12));
		JLabelYPos.setBounds(204,7,56,16);
		JPanelLowerStatus.setBorder(etchedBorder1);
		JPanelLowerStatus.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		getContentPane().add(JPanelLowerStatus);
		JPanelLowerStatus.setBackground(java.awt.Color.pink);
		JPanelLowerStatus.setBounds(10,440,404,24);
		JLabelSimulationStatus.setText("Status");
		JPanelLowerStatus.add(JLabelSimulationStatus);
		JLabelSimulationStatus.setBounds(183,7,37,15);
		JSliderTruckAngle.setBounds(520,320,180,44);
		JSliderTruckAngle.setMinimum(-90);
		JSliderTruckAngle.setValue(90);
		JSliderTruckAngle.setMaximum(270);
		JSliderTruckAngle.setPaintLabels(true);
		JSliderTruckAngle.setToolTipText("Set truck angle");
		JSliderTruckAngle.setPaintTicks(true);
		JSliderTruckAngle.setMajorTickSpacing(90);
		getContentPane().add(JSliderTruckAngle);
		JSliderTruckAngle.setBackground(new java.awt.Color(204,204,204));
		JSliderTruckAngle.setForeground(java.awt.Color.darkGray);
		JSliderTruckAngle.setFont(new Font("Dialog", Font.PLAIN, 10));
		JLabelTruckAngle.setText("Truck Angle");
		getContentPane().add(JLabelTruckAngle);
		JLabelTruckAngle.setForeground(java.awt.Color.black);
		JLabelTruckAngle.setFont(new Font("Dialog", Font.BOLD, 11));
		JLabelTruckAngle.setBounds(420,320,85,23);
		JSliderSimulationSpeed.setBounds(542,378,154,28);
		JSliderSimulationSpeed.setMinimum(1);
		JSliderSimulationSpeed.setValue(1);
		JSliderSimulationSpeed.setMaximum(10);
		JSliderSimulationSpeed.setSnapToTicks(true);
		JSliderSimulationSpeed.setToolTipText("Set Speed of Simulation");
		JSliderSimulationSpeed.setPaintTicks(true);
		JSliderSimulationSpeed.setMajorTickSpacing(1);
		getContentPane().add(JSliderSimulationSpeed);
		JLabelSimulationSpeed.setText("Simulation Speed");
		getContentPane().add(JLabelSimulationSpeed);
		JLabelSimulationSpeed.setForeground(java.awt.Color.black);
		JLabelSimulationSpeed.setFont(new Font("Dialog", Font.BOLD, 11));
		JLabelSimulationSpeed.setBounds(420,372,120,24);
		JLabelTruckSpeed.setText("Truck Speed");
		getContentPane().add(JLabelTruckSpeed);
		JLabelTruckSpeed.setForeground(java.awt.Color.black);
		JLabelTruckSpeed.setFont(new Font("Dialog", Font.BOLD, 11));
		JLabelTruckSpeed.setBounds(420,416,96,28);
		JLabel2.setText("fast");
		getContentPane().add(JLabel2);
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel2.setBounds(540,405,32,12);
		JLabel3.setText("slow");
		getContentPane().add(JLabel3);
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel3.setBounds(672,405,32,12);
		JSliderTruckSpeed.setBounds(542,425,154,28);
		JSliderTruckSpeed.setMinimum(1);
		JSliderTruckSpeed.setValue(1);
		JSliderTruckSpeed.setMaximum(3);
		JSliderTruckSpeed.setSnapToTicks(true);
		JSliderTruckSpeed.setToolTipText("Set Truck Speed");
		JSliderTruckSpeed.setPaintTicks(true);
		JSliderTruckSpeed.setMajorTickSpacing(1);
		getContentPane().add(JSliderTruckSpeed);
		JLabel4.setText("slow");
		getContentPane().add(JLabel4);
		JLabel4.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel4.setBounds(540,453,32,12);
		JLabel5.setText("fast");
		getContentPane().add(JLabel5);
		JLabel5.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel5.setBounds(672,453,32,12);
		JButtonResetRules.setText("Reset rules");
		JButtonResetRules.setToolTipText("Reset rules to original state");
		getContentPane().add(JButtonResetRules);
		JButtonResetRules.setFont(new Font("Dialog", Font.BOLD, 12));
		JButtonResetRules.setBounds(572,235,118,25);
		JLabelAngleValue.setText("0");
		getContentPane().add(JLabelAngleValue);
		JLabelAngleValue.setBounds(444,340,53,17);
		JCheckBoxTracing.setText("Trace Truck Path");
		JCheckBoxTracing.setToolTipText("Enable/disable tracing of Truck as it parks");
		getContentPane().add(JCheckBoxTracing);
		JCheckBoxTracing.setBounds(430,253,138,24);
		JPanelOuter.setBorder(etchedBorder1);
		JPanelOuter.setLayout(null);
		getContentPane().add(JPanelOuter);
		JPanelOuter.setBounds(10,36,404,404);
		JCheckBoxShowRuleFirings.setToolTipText("Set rule button colors as they fire");
		JCheckBoxShowRuleFirings.setText("Show Rule Firings");
		getContentPane().add(JCheckBoxShowRuleFirings);
		JCheckBoxShowRuleFirings.setBounds(430,228,138,24);
		//}}
	
		//{{REGISTER_LISTENERS
		SymChange lSymChange = new SymChange();
		JSliderTruckAngle.addChangeListener(lSymChange);
		SymAction lSymAction = new SymAction();
		JButtonGo.addActionListener(lSymAction);
		JButtonPause.addActionListener(lSymAction);
		JButtonStep.addActionListener(lSymAction);
		JButtonReset.addActionListener(lSymAction);
		JButtonResetRules.addActionListener(lSymAction);
		JSliderSimulationSpeed.addChangeListener(lSymChange);
		JSliderTruckSpeed.addChangeListener(lSymChange);
		JCheckBoxTracing.addChangeListener(lSymChange);
		JCheckBoxShowRuleFirings.addChangeListener(lSymChange);
		//}}

        // custom code added here
        Truck = new TruckSimulation( this );
        viewArea = new DrawPanel(Truck);
		viewArea.setBackground(new java.awt.Color(220,220,220));
		viewArea.setBounds(2, 2, 400, 400);
		JPanelOuter.add(viewArea);
		SymMouseMotion aSymMouseMotion = new SymMouseMotion();
		viewArea.addMouseMotionListener(aSymMouseMotion);
		SymMouse aSymMouse = new SymMouse();
		viewArea.addMouseListener(aSymMouse);

	    // set up the values in the sliders etc. to make sure in synch 
		// with initial states
		JSliderSimulationSpeed.setValue(Truck.getSimSpeed());
		JSliderTruckAngle.setValue((int)Truck.getTruckAngle());
		JSliderTruckSpeed.setValue((int)Truck.getTruckSpeed());
		
		// Now buttons for the rules
		int rows = Truck.getRows();
		int columns = Truck.getColumns();
		conclusionButtons = new JButton[rows][columns];
		JPanelRuleButtons.setLayout(new GridLayout(rows,columns,2,2));
		Insets insets = new Insets(2,5,2,5);
		for (int i=0; i<rows; i++)
		   for (int j=0; j<columns; j++)
		   {
		    JButton button = new JButton();
		    JPanelRuleButtons.add(button);
		    button.setMargin(insets);
		    button.setText(Truck.getConclusionExpression(i,j));
	        button.addActionListener(lSymAction);
		    conclusionButtons[i][j] = button;
		   }
		   
		JButtonResetRules.setMargin(insets);
		JButtonGo.setMargin(insets);
		JButtonReset.setMargin(insets);
		JButtonPause.setMargin(insets);
		JButtonStep.setMargin(insets);
		JCheckBoxTracing.setMargin(insets);
				
		Truck.start();
		Truck.reset();
	}

    public void resetConclusionButtonsBackground(Color offColor)
    {
		int rows = Truck.getRows();
		int columns = Truck.getColumns();
        // reset background color of rule matrix buttons
	    for (int j = 0; j<columns; j++) 
	        for (int i = 0; i<rows; i++) 
	            if (conclusionButtons[i][j].getBackground() != offColor)
			       conclusionButtons[i][j].setBackground(offColor);
        
    }
    public void setConclusionButtonBackground(int i, int j, Color color)
    {
      if (conclusionButtons[i][j].getBackground() != color)
         conclusionButtons[i][j].setBackground(color);
    }
    
    
    public static void main( String args[] )
    {
       // f is final because it is accessed from the inner class below
       final JFrame f = new JFrame();
       JApplet applet = new FuzzyTruckJApplet();

       applet.init();
       
       f.setContentPane(applet.getContentPane());
       f.setBounds(100, 100, 720, 510);
       f.setTitle("Fuzzy Truck Parking Simulation");
       f.setVisible(true);
       
       f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
       f.addWindowListener
        (
          new WindowAdapter()
          { public void windowClosed(WindowEvent e) { System.exit(0); }
          }
        );
    }


	//{{DECLARE_CONTROLS
	javax.swing.JPanel JPanelRuleButtons = new javax.swing.JPanel();
	Border etchedBorder1 = BorderFactory.createEtchedBorder();
	javax.swing.JPanel RadioPanel1 = new javax.swing.JPanel();
	javax.swing.ButtonGroup radioButtonGroup1 = new javax.swing.ButtonGroup();
	javax.swing.JRadioButton JRadioButtonPB = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonPM = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonPS = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonZE = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonNS = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonNM = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonNB = new javax.swing.JRadioButton();
	javax.swing.JRadioButton JRadioButtonKill = new javax.swing.JRadioButton();
	javax.swing.JPanel JPanel2 = new javax.swing.JPanel();
	javax.swing.JButton JButtonGo = new javax.swing.JButton();
	javax.swing.JButton JButtonPause = new javax.swing.JButton();
	javax.swing.JButton JButtonStep = new javax.swing.JButton();
	javax.swing.JButton JButtonReset = new javax.swing.JButton();
	javax.swing.JPanel JPanelUpperStatus = new javax.swing.JPanel();
	javax.swing.JLabel JLabelXPos = new javax.swing.JLabel();
	javax.swing.JLabel JLabelYPos = new javax.swing.JLabel();
	javax.swing.JPanel JPanelLowerStatus = new javax.swing.JPanel();
	javax.swing.JLabel JLabelSimulationStatus = new javax.swing.JLabel();
	javax.swing.JSlider JSliderTruckAngle = new javax.swing.JSlider();
	javax.swing.JLabel JLabelTruckAngle = new javax.swing.JLabel();
	javax.swing.JSlider JSliderSimulationSpeed = new javax.swing.JSlider();
	javax.swing.JLabel JLabelSimulationSpeed = new javax.swing.JLabel();
	javax.swing.JLabel JLabelTruckSpeed = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JSlider JSliderTruckSpeed = new javax.swing.JSlider();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	javax.swing.JButton JButtonResetRules = new javax.swing.JButton();
	javax.swing.JLabel JLabelAngleValue = new javax.swing.JLabel();
	javax.swing.JCheckBox JCheckBoxTracing = new javax.swing.JCheckBox();
	javax.swing.JPanel JPanelOuter = new javax.swing.JPanel();
	javax.swing.JCheckBox JCheckBoxShowRuleFirings = new javax.swing.JCheckBox();
	//}}

    // Custom variables
    TruckSimulation Truck;
    SymMouseMotion aSymMouseMotion;
    DrawPanel viewArea;
    JButton conclusionButtons[][];

	class SymMouseMotion extends java.awt.event.MouseMotionAdapter
	{
		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == viewArea)
				viewArea_mouseMoved(event);
		}
	}

	void viewArea_mouseMoved(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		int x = event.getX();
		int y = event.getY();
		double width = (double)viewArea.getWidth();
		double height = (double)viewArea.getHeight();
		// convert the x, y position into values between 0 and 100
		// with 0,0 in lower left corner
		double fx = (double)x/width*100.0;
		double fy = (double)(height-y)/height*100.0;
		// display values in upper status area
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		JLabelXPos.setText("X = " + nf.format(fx) +",");
		JLabelYPos.setText("Y = " + nf.format(fy));
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == JSliderTruckAngle)
				JSliderTruckAngle_stateChanged(event);
			else if (object == JSliderSimulationSpeed)
				JSliderSimulationSpeed_stateChanged(event);
			else if (object == JSliderTruckSpeed)
				JSliderTruckSpeed_stateChanged(event);
			else if (object == JCheckBoxTracing)
				JCheckBoxTracing_stateChanged(event);
			else if (object == JCheckBoxShowRuleFirings)
				JCheckBoxShowRuleFirings_stateChanged(event);
		}
	}

	void JSliderTruckAngle_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		JSlider slider = (JSlider)event.getSource();
		JLabelAngleValue.setText(String.valueOf(slider.getValue()));
		Truck.setTruckAngle((double)slider.getValue());
		Truck.reset();
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == JButtonGo)
				JButtonGo_actionPerformed(event);
			else if (object == JButtonPause)
				JButtonPause_actionPerformed(event);
			else if (object == JButtonStep)
				JButtonStep_actionPerformed(event);
			else if (object == JButtonReset)
				JButtonReset_actionPerformed(event);
			else if (object == JButtonResetRules)
				JButtonResetRules_actionPerformed(event);
		    else 
		        // the buttons in the conclusion button group
		        conclusionButton_actionPerformed(event);
		}
	}

	void conclusionButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		// get the current setting in the radio buttons and use the
		// text of the radio button as the text for the conclusion button
		// AND tell the Truck simulation it has changed
		JButton button = (JButton)event.getSource();
		JRadioButton selectedRadioButton = null;
		Enumeration radioButtonsEnum = radioButtonGroup1.getElements();
		while (radioButtonsEnum.hasMoreElements())
		{ JRadioButton rb = (JRadioButton)radioButtonsEnum.nextElement();
		  if (rb.isSelected())
		  { selectedRadioButton = rb;
		  	break;
		  }
		}
		if (selectedRadioButton == null) return;
		
		int rows = Truck.getRows();
		int columns = Truck.getColumns();
		for (int i=0; i<rows; i++)
		   for (int j=0; j<columns; j++)
		   {
		    if (button == conclusionButtons[i][j])
		    {
		        String s = selectedRadioButton.getText();
		        if (s.equals("Kill")) s = " ";
		        button.setText(s);
		        Truck.setConclusionExpression(s, i, j);
		        return;
		    }
		   }
	}

	void JButtonGo_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Truck.putMsg(Truck.RUN);

	}

	void JButtonPause_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Truck.putMsg(Truck.PAUSE);
	}

	void JButtonStep_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Truck.putMsg(Truck.STEP);
	}

	void JButtonReset_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Truck.reset();
	}

	void JButtonResetRules_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		Truck.resetRules();
		int rows = Truck.getRows();
		int columns = Truck.getColumns();
		for (int i=0; i<rows; i++)
		   for (int j=0; j<columns; j++)
		   {
		    conclusionButtons[i][j].setText(Truck.getConclusionExpression(i,j));
		   }
	}

	void JSliderSimulationSpeed_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		JSlider slider = (JSlider)event.getSource();
		Truck.setSimSpeed(slider.getValue());
	}

	void JSliderTruckSpeed_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		JSlider slider = (JSlider)event.getSource();
		Truck.setTruckSpeed((double)slider.getValue());
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mouseClicked(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == viewArea)
				viewArea_mouseClicked(event);
		}
	}

	void viewArea_mouseClicked(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		int x = event.getX();
		int y = event.getY();
		Truck.setX((double)x/viewArea.getWidth()*100.0);
		Truck.setY((double)y/viewArea.getHeight()*100.0);
		Truck.reset();
	}

	void JCheckBoxTracing_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		JCheckBox cb = (JCheckBox)event.getSource();
		Truck.setTracing(cb.isSelected());
	}

	void JCheckBoxShowRuleFirings_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		JCheckBox cb = (JCheckBox)event.getSource();
		Truck.setShowRuleFirings(cb.isSelected());
	}
}
