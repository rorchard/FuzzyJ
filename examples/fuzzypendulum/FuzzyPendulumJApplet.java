/*
 * @(#)FuzzyPendulumJApplet.java	1.0 2000/06/10
 *
 * Copyright (c) 1998 National Research Council of Canada.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * the National Research Council of Canada. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with the National Research Council of Canada.
 *
 * THE NATIONAL RESEARCH COUNCIL OF CANADA MAKES NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. NRC SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * Copyright Version 1.0
 *
 */
package examples.fuzzypendulum;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import com.klg.jclass.chart.beans.SimpleChart;
import com.klg.jclass.chart.*;
import com.klg.jclass.chart.EventTrigger;
import java.text.*;
import nrc.fuzzy.*;

/**
 * A basic extension of the javax.swing.JApplet class to support the 
   FuzzyJ Toolkit 'Fuzzy Pendulum' demonstration program.
 */
public class FuzzyPendulumJApplet extends JApplet
{
    protected FuzzyPendulumModel fuzzyPendulumModel;
	boolean   fuzzyPendulumModelSuspended = false; // set to true when suspended (when applet leaves page)
    protected PendulumSimulationJPanel simulationPanel;
    protected double minMassSize;  // min mass size from model
    protected double maxMassSize;  // max mass size from model
    protected double minMotorSize; // min motor size from model
    protected double maxMotorSize; // max motor size from model
    protected double motorScale;   // max - min of motor sizes in model
    protected double massScale;    // max - min of mass sizes in model

    static final NumberFormat nf = NumberFormat.getNumberInstance();
    
    String displayedRule = "";             // current rule results being displayed
    
    // Data sources are used by the charts (graphs) -- JClass product from KL Group
    CurrentTraceDataSource ctData;         // data source for the current trace graph
    FinalCurrentDataSource fcData;         // data source for the global current graph
    AntecedentConclusionDataSource eaData; // data source for the error antecedent graph
    AntecedentConclusionDataSource oaData; // data source for the omega antecedent graph
    AntecedentConclusionDataSource ccData; // data source for the current conclusion graph

	public void init()
	{		
		// This line prevents the "Swing: checked access to system event queue" message seen in some browsers.
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
		
		//{{INIT_CONTROLS
		try 
//		{UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		{UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		{UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		}
		catch (Exception ex)
		{System.out.println("ERROR setting look and feel!!!");
		 UIManager.LookAndFeelInfo lookAndFeels[] = UIManager.getInstalledLookAndFeels();
		 for(int i = 0; i < lookAndFeels.length; i++){
			 System.out.println(lookAndFeels[i].getClassName());
		 }
		}
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		getContentPane().setForeground(java.awt.Color.black);
		setSize(770,610);
		JPanel1.setOpaque(false);
		JPanel1.setDoubleBuffered(false);
		JPanel1.setLayout(null);
		getContentPane().add(JPanel1);
		JPanel1.setBounds(0,0,770,610);
		blackJPanel.setBorder(etchedBorder1);
		blackJPanel.setOpaque(false);
		blackJPanel.setLayout(null);
		blackJPanel.setEnabled(false);
		JPanel1.add(blackJPanel);
		blackJPanel.setBackground(java.awt.Color.black);
		blackJPanel.setBounds(408,60,300,164);
		blackJPanel.setVisible(false);
		motorJSlider.setBounds(478,276,236,24);
		motorJSlider.setMinimum(1);
		motorJSlider.setMaximum(10);
		motorJSlider.setPaintTicks(true);
		motorJSlider.setMajorTickSpacing(1);
		motorJSlider.setToolTipText("modify the size of the motor");
		motorJSlider.setMinorTickSpacing(1);
		motorJSlider.setValue(5);
		JPanel1.add(motorJSlider);
		ruleMatrixPanel.setLayout(new GridLayout(5,5,0,0));
		JPanel1.add(ruleMatrixPanel);
		ruleMatrixPanel.setBounds(84,60,276,168);
		JButton1.setText("");
		JButton1.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton1);
		ruleMatrixPanel.add(JButton1);
		JButton2.setText("");
		JButton2.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton2);
		ruleMatrixPanel.add(JButton2);
		Z_NM_PM_JButton.setToolTipText("Enable, disable or Display this rule");
		Z_NM_PM_JButton.setText("PM");
		Z_NM_PM_JButton.setActionCommand("PM");
		ruleMatrixButtonGroup.add(Z_NM_PM_JButton);
		ruleMatrixPanel.add(Z_NM_PM_JButton);
		Z_NM_PM_JButton.setBackground(java.awt.Color.blue);
		JButton4.setText("");
		JButton4.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton4);
		ruleMatrixPanel.add(JButton4);
		JButton5.setText("");
		JButton5.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton5);
		ruleMatrixPanel.add(JButton5);
		JButton6.setText("");
		JButton6.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton6);
		ruleMatrixPanel.add(JButton6);
		JButton7.setText("");
		JButton7.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton7);
		ruleMatrixPanel.add(JButton7);
		Z_NS_PS_JButton.setToolTipText("Enable, disable or Display this rule");
		Z_NS_PS_JButton.setText("PS");
		Z_NS_PS_JButton.setActionCommand("PS");
		ruleMatrixButtonGroup.add(Z_NS_PS_JButton);
		ruleMatrixPanel.add(Z_NS_PS_JButton);
		Z_NS_PS_JButton.setBackground(java.awt.Color.blue);
		PS_NS_Z_JButton.setToolTipText("Enable, disable or Display this rule");
		PS_NS_Z_JButton.setText("Z");
		PS_NS_Z_JButton.setActionCommand("Z");
		ruleMatrixButtonGroup.add(PS_NS_Z_JButton);
		ruleMatrixPanel.add(PS_NS_Z_JButton);
		PS_NS_Z_JButton.setBackground(java.awt.Color.blue);
		JButton10.setText("");
		JButton10.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton10);
		ruleMatrixPanel.add(JButton10);
		NM_Z_PM_JButton.setToolTipText("Enable, disable or Display this rule");
		NM_Z_PM_JButton.setText("PM");
		NM_Z_PM_JButton.setActionCommand("PM");
		ruleMatrixButtonGroup.add(NM_Z_PM_JButton);
		ruleMatrixPanel.add(NM_Z_PM_JButton);
		NM_Z_PM_JButton.setBackground(java.awt.Color.blue);
		NS_Z_PS_JButton.setToolTipText("Enable, disable or Display this rule");
		NS_Z_PS_JButton.setText("PS");
		NS_Z_PS_JButton.setActionCommand("PS");
		ruleMatrixButtonGroup.add(NS_Z_PS_JButton);
		ruleMatrixPanel.add(NS_Z_PS_JButton);
		NS_Z_PS_JButton.setBackground(java.awt.Color.blue);
		Z_Z_Z_JButton.setToolTipText("Enable, disable or Display this rule");
		Z_Z_Z_JButton.setText("Z");
		Z_Z_Z_JButton.setActionCommand("Z");
		ruleMatrixButtonGroup.add(Z_Z_Z_JButton);
		ruleMatrixPanel.add(Z_Z_Z_JButton);
		Z_Z_Z_JButton.setBackground(java.awt.Color.blue);
		PS_Z_NS_JButton.setToolTipText("Enable, disable or Display this rule");
		PS_Z_NS_JButton.setText("NS");
		PS_Z_NS_JButton.setActionCommand("NS");
		ruleMatrixButtonGroup.add(PS_Z_NS_JButton);
		ruleMatrixPanel.add(PS_Z_NS_JButton);
		PS_Z_NS_JButton.setBackground(java.awt.Color.blue);
		PM_Z_NM_JButton.setToolTipText("Enable, disable or Display this rule");
		PM_Z_NM_JButton.setText("NM");
		PM_Z_NM_JButton.setActionCommand("NM");
		ruleMatrixButtonGroup.add(PM_Z_NM_JButton);
		ruleMatrixPanel.add(PM_Z_NM_JButton);
		PM_Z_NM_JButton.setBackground(java.awt.Color.blue);
		JButton16.setText("");
		JButton16.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton16);
		ruleMatrixPanel.add(JButton16);
		NS_PS_Z_JButton.setToolTipText("Enable, disable or Display this rule");
		NS_PS_Z_JButton.setText("Z");
		NS_PS_Z_JButton.setActionCommand("Z");
		ruleMatrixButtonGroup.add(NS_PS_Z_JButton);
		ruleMatrixPanel.add(NS_PS_Z_JButton);
		NS_PS_Z_JButton.setBackground(java.awt.Color.blue);
		Z_PS_NS_JButton.setToolTipText("Enable, disable or Display this rule");
		Z_PS_NS_JButton.setText("NS");
		Z_PS_NS_JButton.setActionCommand("NS");
		ruleMatrixButtonGroup.add(Z_PS_NS_JButton);
		ruleMatrixPanel.add(Z_PS_NS_JButton);
		Z_PS_NS_JButton.setBackground(java.awt.Color.blue);
		JButton19.setText("");
		JButton19.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton19);
		ruleMatrixPanel.add(JButton19);
		JButton20.setText("");
		JButton20.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton20);
		ruleMatrixPanel.add(JButton20);
		JButton21.setText("");
		JButton21.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton21);
		ruleMatrixPanel.add(JButton21);
		JButton22.setText("");
		JButton22.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton22);
		ruleMatrixPanel.add(JButton22);
		Z_PM_NM_JButton.setToolTipText("Enable, disable or Display this rule");
		Z_PM_NM_JButton.setText("NM");
		Z_PM_NM_JButton.setActionCommand("NM");
		ruleMatrixButtonGroup.add(Z_PM_NM_JButton);
		ruleMatrixPanel.add(Z_PM_NM_JButton);
		Z_PM_NM_JButton.setBackground(java.awt.Color.blue);
		JButton24.setText("");
		JButton24.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton24);
		ruleMatrixPanel.add(JButton24);
		JButton25.setText("");
		JButton25.setEnabled(false);
		ruleMatrixButtonGroup.add(JButton25);
		ruleMatrixPanel.add(JButton25);
		JLabel20.setText("Pendulum Simulation");
		JPanel1.add(JLabel20);
		JLabel20.setForeground(new java.awt.Color(0,0,160));
		JLabel20.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel20.setBounds(480,12,204,24);
		radioPanel1.setBorder(etchedBorder1);
		radioPanel1.setLayout(new GridLayout(1,3,5,5));
		JPanel1.add(radioPanel1);
		radioPanel1.setBounds(84,228,276,24);
		enableJRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		enableJRadioButton.setToolTipText("Enable rule in matrix above");
		enableJRadioButton.setText("enable");
		enableJRadioButton.setActionCommand("enable");
		radioButtonGroup1.add(enableJRadioButton);
		radioPanel1.add(enableJRadioButton);
		disableJRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		disableJRadioButton.setToolTipText("Disable rule in matrix above");
		disableJRadioButton.setText("disable");
		disableJRadioButton.setActionCommand("disable");
		radioButtonGroup1.add(disableJRadioButton);
		radioPanel1.add(disableJRadioButton);
		displayJRadioButton.setSelected(true);
		displayJRadioButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		displayJRadioButton.setToolTipText("Select rule to display");
		displayJRadioButton.setText("display");
		displayJRadioButton.setActionCommand("display");
		radioButtonGroup1.add(displayJRadioButton);
		radioPanel1.add(displayJRadioButton);
		JLabel1.setText("O");
		JPanel1.add(JLabel1);
		JLabel1.setForeground(new java.awt.Color(0,0,160));
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel1.setBounds(24,84,18,17);
		JLabel2.setText("m");
		JPanel1.add(JLabel2);
		JLabel2.setForeground(new java.awt.Color(0,0,160));
		JLabel2.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel2.setBounds(24,108,18,17);
		JLabel3.setText("e");
		JPanel1.add(JLabel3);
		JLabel3.setForeground(new java.awt.Color(0,0,160));
		JLabel3.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel3.setBounds(24,132,18,17);
		JLabel4.setText("g");
		JPanel1.add(JLabel4);
		JLabel4.setForeground(new java.awt.Color(0,0,160));
		JLabel4.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel4.setBounds(24,156,18,19);
		JLabel5.setText("a");
		JPanel1.add(JLabel5);
		JLabel5.setForeground(new java.awt.Color(0,0,160));
		JLabel5.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel5.setBounds(24,184,18,17);
		JLabel7.setText("E  r  r  o  r");
		JPanel1.add(JLabel7);
		JLabel7.setBackground(new java.awt.Color(0,0,160));
		JLabel7.setForeground(new java.awt.Color(0,0,160));
		JLabel7.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel7.setBounds(180,12,96,19);
		JLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel8.setText("NM");
		JPanel1.add(JLabel8);
		JLabel8.setBounds(96,36,36,18);
		JLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel9.setText("NS");
		JPanel1.add(JLabel9);
		JLabel9.setBounds(150,36,36,18);
		JLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel10.setText("Z");
		JPanel1.add(JLabel10);
		JLabel10.setBounds(204,36,36,18);
		JLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel11.setText("PS");
		JPanel1.add(JLabel11);
		JLabel11.setBounds(258,36,36,18);
		JLabel12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		JLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		JLabel12.setText("PM");
		JPanel1.add(JLabel12);
		JLabel12.setBounds(312,36,36,18);
		JLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		JLabel13.setText("NM");
		JPanel1.add(JLabel13);
		JLabel13.setBounds(58,60,24,24);
		JLabel14.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		JLabel14.setText("NS");
		JPanel1.add(JLabel14);
		JLabel14.setBounds(58,93,24,24);
		JLabel15.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		JLabel15.setText("Z");
		JPanel1.add(JLabel15);
		JLabel15.setBounds(58,126,24,24);
		JLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		JLabel16.setText("PS");
		JPanel1.add(JLabel16);
		JLabel16.setBounds(58,159,24,24);
		JLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		JLabel17.setText("PM");
		JPanel1.add(JLabel17);
		JLabel17.setBounds(58,192,24,24);
		JLabel18.setText("Mass Size");
		JPanel1.add(JLabel18);
		JLabel18.setForeground(java.awt.Color.red);
		JLabel18.setBounds(408,240,75,15);
		JLabel19.setText("Motor Size");
		JPanel1.add(JLabel19);
		JLabel19.setForeground(new java.awt.Color(0,128,255));
		JLabel19.setBounds(408,276,75,15);
		massJSlider.setBounds(478,240,236,24);
		massJSlider.setMinimum(1);
		massJSlider.setMaximum(10);
		massJSlider.setPaintTicks(true);
		massJSlider.setMajorTickSpacing(1);
		massJSlider.setToolTipText("modify the mass on the end of the penulum");
		massJSlider.setMinorTickSpacing(1);
		massJSlider.setValue(5);
		JPanel1.add(massJSlider);
		finalCurrentSimpleChart.setYAxisVisible(false);
		finalCurrentSimpleChart.setXAxisVisible(false);
		finalCurrentSimpleChart.setData("ARRAY 'Sample JCChart' 1 5 'Point Label0' 'Point Label1' '' 0.0 .4 .45 .55 .6'Series1' .1 .1 .75 .75 0");
		finalCurrentSimpleChart.setBorder(etchedBorder1);
		finalCurrentSimpleChart.setChartType(com.klg.jclass.chart.JCChart.AREA);
		finalCurrentSimpleChart.setXAxisMinMax("0.0,1.0");
		finalCurrentSimpleChart.setAlignmentY(0.1F);
		finalCurrentSimpleChart.setAlignmentX(0.1F);
		finalCurrentSimpleChart.setYAxisMinMax("0.0,1.0");
		finalCurrentSimpleChart.setFooterText("Global Current Output ");
		JPanel1.add(finalCurrentSimpleChart);
		finalCurrentSimpleChart.setBounds(84,456,216,108);
		errorSimpleChart.setYAxisVisible(false);
		errorSimpleChart.setXAxisVisible(false);
		errorSimpleChart.setData("ARRAY 'Sample JCChart' 1 5 'Point Label0' 'Point Label1' 'Point Label2' 'Point Label3' 'Point Label4' 0.0 .4 .45 .55 .6'Series1' .1 .1 .75 .75 0");
		errorSimpleChart.setBorder(etchedBorder1);
		errorSimpleChart.setChartType(com.klg.jclass.chart.JCChart.AREA);
		errorSimpleChart.setXAxisMinMax("0.0,1.0");
		errorSimpleChart.setYAxisMinMax("0.0,1.0");
		errorSimpleChart.setFooterText("error");
		JPanel1.add(errorSimpleChart);
		errorSimpleChart.setBackground(new java.awt.Color(204,204,204));
		errorSimpleChart.setBounds(84,276,132,72);
		ruleCurrentSimpleChart.setYAxisVisible(false);
		ruleCurrentSimpleChart.setXAxisVisible(false);
		ruleCurrentSimpleChart.setData("ARRAY 'Sample JCChart' 1 5 'Point Label0' 'Point Label1' 'Point Label2' 'Point Label3' 'Point Label4'  0.0 .4 .45 .55 .6'Series1' .1 .1 .75 .75 0");
		ruleCurrentSimpleChart.setBorder(etchedBorder1);
		ruleCurrentSimpleChart.setChartType(com.klg.jclass.chart.JCChart.AREA);
		ruleCurrentSimpleChart.setXAxisMinMax("0.0,1.0");
		ruleCurrentSimpleChart.setYAxisMinMax("0.0,1.0");
		ruleCurrentSimpleChart.setFooterText("current");
		JPanel1.add(ruleCurrentSimpleChart);
		ruleCurrentSimpleChart.setBounds(240,312,120,84);
		currentTraceSimpleChart.setXAxisVisible(false);
		currentTraceSimpleChart.setBorder(etchedBorder1);
		currentTraceSimpleChart.setXAxisMinMax("0.0,80.0");
		currentTraceSimpleChart.setYAxisMinMax("-1.0,1.0");
		currentTraceSimpleChart.setFooterText("Global Current Trace");
		JPanel1.add(currentTraceSimpleChart);
		currentTraceSimpleChart.setBounds(492,324,216,108);
		stopBoppingJCheckBox.setToolTipText("Stop bopping the pendulum mass");
		stopBoppingJCheckBox.setText("Stop Bopping");
		stopBoppingJCheckBox.setActionCommand("Stop Bopping");
		JPanel1.add(stopBoppingJCheckBox);
		stopBoppingJCheckBox.setBounds(330,494,130,19);
		stepModeJCheckBox.setToolTipText("Run simulation in step mode");
		stepModeJCheckBox.setText("Step Mode");
		stepModeJCheckBox.setActionCommand("Step Mode");
		JPanel1.add(stepModeJCheckBox);
		stepModeJCheckBox.setBounds(330,458,130,19);
		nextStepJButton.setToolTipText("Execute a single step of simulation");
		nextStepJButton.setText("Next Step");
		nextStepJButton.setActionCommand("Next Step");
		nextStepJButton.setEnabled(false);
		JPanel1.add(nextStepJButton);
		nextStepJButton.setBounds(476,456,102,29);
		freezeJButton.setToolTipText("Pause simulation for 5 seconds");
		freezeJButton.setText("Freeze 5 sec");
		freezeJButton.setActionCommand("Freeze 5 sec");
		JPanel1.add(freezeJButton);
		freezeJButton.setBounds(587,456,121,29);
		pullRightJButton.setToolTipText("Pull mass all the way left");
		pullRightJButton.setText("Pull right");
		pullRightJButton.setActionCommand("Pull to right");
		JPanel1.add(pullRightJButton);
		pullRightJButton.setBounds(587,492,121,29);
		pullLeftJButton.setToolTipText("Pull mass all the way left");
		pullLeftJButton.setText("Pull left");
		pullLeftJButton.setActionCommand("Pull to left");
		JPanel1.add(pullLeftJButton);
		pullLeftJButton.setBounds(476,492,102,29);
		bobUpDownJCheckBox.setToolTipText("Start the pendulum mass bobbing up/down");
		bobUpDownJCheckBox.setText("Bob Up/Down");
		bobUpDownJCheckBox.setActionCommand("Bob Up/Down");
		JPanel1.add(bobUpDownJCheckBox);
		bobUpDownJCheckBox.setBounds(330,530,130,19);
		JLabelImplication.setText("=>");
		JPanel1.add(JLabelImplication);
		JLabelImplication.setForeground(java.awt.Color.gray);
		JLabelImplication.setFont(new Font("Dialog", Font.BOLD, 18));
		JLabelImplication.setBounds(216,316,24,72);
		omegaSimpleChart.setYAxisVisible(false);
		omegaSimpleChart.setXAxisVisible(false);
		omegaSimpleChart.setData("ARRAY 'Sample JCChart' 1 5 'Point Label0' 'Point Label1' 'Point Label2' 'Point Label3' 'Point Label4'  0.0 .4 .45 .55 .6'Series1' .1 .1 .75 .75 0");
		omegaSimpleChart.setBorder(etchedBorder1);
		omegaSimpleChart.setChartType(com.klg.jclass.chart.JCChart.AREA);
		omegaSimpleChart.setXAxisMinMax("0.0,1.0");
		omegaSimpleChart.setYAxisMinMax("0.0,1.0");
		omegaSimpleChart.setFooterText("Omega");
		JPanel1.add(omegaSimpleChart);
		omegaSimpleChart.setBounds(84,360,132,72);
		bumpLeftJButton.setToolTipText("bump the pendulum mass to the left");
		bumpLeftJButton.setText("Bump left");
		bumpLeftJButton.setActionCommand("Pull to left");
		JPanel1.add(bumpLeftJButton);
		bumpLeftJButton.setBounds(476,528,102,29);
		bumpRightJButton.setToolTipText("bump the pendulum mass to the right");
		bumpRightJButton.setText("Bump right");
		bumpRightJButton.setActionCommand("Pull to left");
		JPanel1.add(bumpRightJButton);
		bumpRightJButton.setBounds(587,528,121,29);
		JLabel21.setText("error");
		JPanel1.add(JLabel21);
		JLabel21.setBounds(384,336,48,14);
		JLabel22.setText("Omega");
		JPanel1.add(JLabel22);
		JLabel22.setBounds(384,372,48,14);
		JLabel23.setText("current");
		JPanel1.add(JLabel23);
		JLabel23.setBounds(384,408,48,14);
		errorJTextField.setEditable(false);
		errorJTextField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		errorJTextField.setBorder(etchedBorder1);
		errorJTextField.setText("0.0");
		JPanel1.add(errorJTextField);
		errorJTextField.setBackground(new java.awt.Color(251,254,218));
		errorJTextField.setBounds(432,336,50,17);
		omegaJTextField.setEditable(false);
		omegaJTextField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		omegaJTextField.setBorder(etchedBorder1);
		omegaJTextField.setText("0.0");
		JPanel1.add(omegaJTextField);
		omegaJTextField.setBackground(new java.awt.Color(251,254,218));
		omegaJTextField.setBounds(432,372,50,17);
		currentJTextField.setEditable(false);
		currentJTextField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		currentJTextField.setBorder(etchedBorder1);
		currentJTextField.setText("0.0");
		JPanel1.add(currentJTextField);
		currentJTextField.setBackground(new java.awt.Color(251,254,218));
		currentJTextField.setBounds(432,408,50,17);
		JLabel6.setText("** Error: deviation in radians from vertical (90 deg)");
		JPanel1.add(JLabel6);
		JLabel6.setForeground(java.awt.Color.blue);
		JLabel6.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel6.setBounds(384,576,372,25);
		JLabel24.setText("** Omega: Angular Velocity of the pendulum");
		JPanel1.add(JLabel24);
		JLabel24.setForeground(java.awt.Color.blue);
		JLabel24.setFont(new Font("Dialog", Font.BOLD, 10));
		JLabel24.setBounds(24,576,336,24);
		//$$ etchedBorder1.move(12,624);
		//}}
	
		//{{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		Z_NM_PM_JButton.addActionListener(lSymAction);
		Z_NS_PS_JButton.addActionListener(lSymAction);
		PS_NS_Z_JButton.addActionListener(lSymAction);
		NM_Z_PM_JButton.addActionListener(lSymAction);
		NS_Z_PS_JButton.addActionListener(lSymAction);
		Z_Z_Z_JButton.addActionListener(lSymAction);
		PS_Z_NS_JButton.addActionListener(lSymAction);
		PM_Z_NM_JButton.addActionListener(lSymAction);
		NS_PS_Z_JButton.addActionListener(lSymAction);
		Z_PS_NS_JButton.addActionListener(lSymAction);
		Z_PM_NM_JButton.addActionListener(lSymAction);
		SymItem lSymItem = new SymItem();
		stepModeJCheckBox.addItemListener(lSymItem);
		stopBoppingJCheckBox.addItemListener(lSymItem);
		bobUpDownJCheckBox.addItemListener(lSymItem);
		nextStepJButton.addActionListener(lSymAction);
		freezeJButton.addActionListener(lSymAction);
		pullLeftJButton.addActionListener(lSymAction);
		pullRightJButton.addActionListener(lSymAction);
		bumpLeftJButton.addActionListener(lSymAction);
		bumpRightJButton.addActionListener(lSymAction);
		SymChange lSymChange = new SymChange();
		massJSlider.addChangeListener(lSymChange);
		motorJSlider.addChangeListener(lSymChange);
		//}}
		
		// custom code added here
        fuzzyPendulumModel = new FuzzyPendulumModel(this);
        simulationPanel = new PendulumSimulationJPanel(fuzzyPendulumModel);
		simulationPanel.setOpaque(true);
		simulationPanel.setDoubleBuffered(true);
		simulationPanel.setBackground(java.awt.Color.black);
		Rectangle b = blackJPanel.getBounds();
		simulationPanel.setBounds(0, 0, b.width, b.height);
		blackJPanel.add(simulationPanel);
		
		minMassSize = fuzzyPendulumModel.getMinMassSize();
		maxMassSize = fuzzyPendulumModel.getMaxMassSize();
		minMotorSize = fuzzyPendulumModel.getMinMotorSize();
		maxMotorSize = fuzzyPendulumModel.getMaxMotorSize();
		massScale = maxMassSize - minMassSize;
		motorScale = maxMotorSize - minMotorSize;
		
		double val = (double)motorJSlider.getValue();
		double motorSliderScale = (double)(motorJSlider.getMaximum()-motorJSlider.getMinimum());
		val = (val - motorJSlider.getMinimum())/(motorSliderScale)*motorScale + minMotorSize;
		fuzzyPendulumModel.setMotorSize(val);
		
		val = (double)massJSlider.getValue();
		double massSliderScale = (double)(massJSlider.getMaximum()-massJSlider.getMinimum());
		val = (val - massJSlider.getMinimum())/(massSliderScale)*massScale + minMassSize;
		fuzzyPendulumModel.setMassSize(val);
		
		bobUpDownJCheckBox.setSelected(fuzzyPendulumModel.isBobbing());
		stepModeJCheckBox.setSelected(fuzzyPendulumModel.isStepMode());
		stopBoppingJCheckBox.setSelected(!fuzzyPendulumModel.isBopping());
		
		nf.setMaximumFractionDigits(3);
		            
        // Chart initializations
        //
        // Current trace chart gets its data from a data source object
        ctData = new CurrentTraceDataSource(currentTraceSimpleChart);
        currentTraceSimpleChart.getDataView(0).setDataSource(ctData);
        currentTraceSimpleChart.setTrigger(0, new EventTrigger(Event.META_MASK, EventTrigger.CUSTOMIZE));
        currentTraceSimpleChart.setAllowUserChanges(true);
        currentTraceSimpleChart.getDataView(0).getSeries(0).getStyle().getSymbolStyle().setShape(JCSymbolStyle.NONE);
        currentTraceSimpleChart.getDataView(0).getSeries(0).getStyle().getLineStyle().setPattern(JCLineStyle.SOLID);
        currentTraceSimpleChart.getDataView(0).getSeries(0).getStyle().getLineStyle().setWidth(1);
        // final Current chart gets its data from a data source object
        fcData = new FinalCurrentDataSource(finalCurrentSimpleChart);
        finalCurrentSimpleChart.getDataView(0).setDataSource(fcData);
        finalCurrentSimpleChart.setTrigger(0, new EventTrigger(Event.META_MASK, EventTrigger.CUSTOMIZE));
        finalCurrentSimpleChart.setAllowUserChanges(true);
        // Error antecedent chart gets its data from a data source object
        eaData = new AntecedentConclusionDataSource(errorSimpleChart, FuzzyPendulumModel.ERROR_MIN, FuzzyPendulumModel.ERROR_MAX);
        errorSimpleChart.getDataView(0).setDataSource(eaData);
        errorSimpleChart.setTrigger(0, new EventTrigger(Event.META_MASK, EventTrigger.CUSTOMIZE));
        errorSimpleChart.setAllowUserChanges(true);
        errorSimpleChart.getDataView(0).getSeries(0).getStyle().setFillStyle(new JCFillStyle(Color.cyan, JCFillStyle.SOLID));
        errorSimpleChart.getDataView(0).getSeries(1).getStyle().setFillStyle(new JCFillStyle(Color.red, JCFillStyle.SOLID));
        // Omega antecedent chart gets its data from a data source object
        oaData = new AntecedentConclusionDataSource(omegaSimpleChart, FuzzyPendulumModel.OMEGA_MIN, FuzzyPendulumModel.OMEGA_MAX);
        omegaSimpleChart.getDataView(0).setDataSource(oaData);
        omegaSimpleChart.setTrigger(0, new EventTrigger(Event.META_MASK, EventTrigger.CUSTOMIZE));
        omegaSimpleChart.setAllowUserChanges(true);
        omegaSimpleChart.getDataView(0).getSeries(0).getStyle().setFillStyle(new JCFillStyle(Color.cyan, JCFillStyle.SOLID));
        omegaSimpleChart.getDataView(0).getSeries(1).getStyle().setFillStyle(new JCFillStyle(Color.red, JCFillStyle.SOLID));
        // Current conclusion chart gets its data from a data source object
        ccData = new AntecedentConclusionDataSource(ruleCurrentSimpleChart, FuzzyPendulumModel.CURRENT_MIN, FuzzyPendulumModel.CURRENT_MAX);
        ruleCurrentSimpleChart.getDataView(0).setDataSource(ccData);
        ruleCurrentSimpleChart.setTrigger(0, new EventTrigger(Event.META_MASK, EventTrigger.CUSTOMIZE));
        ruleCurrentSimpleChart.setAllowUserChanges(true);
        ruleCurrentSimpleChart.getDataView(0).getSeries(0).getStyle().setFillStyle(new JCFillStyle(Color.cyan, JCFillStyle.SOLID));
        ruleCurrentSimpleChart.getDataView(0).getSeries(1).getStyle().setFillStyle(new JCFillStyle(Color.red, JCFillStyle.SOLID));
        
        displayedRule = "Z_Z_Z";  // initiall display the 'if error Z and omega Z then current Z' rule
        Z_Z_Z_JButton.setForeground(Color.yellow);
	    errorSimpleChart.setFooterText("if error Z");
	    omegaSimpleChart.setFooterText("and omega Z");
	    ruleCurrentSimpleChart.setFooterText("then current Z");
	}
	
	
	public void start() 
	{
		if (!fuzzyPendulumModelSuspended) 
		    fuzzyPendulumModel.start();
		else
		    fuzzyPendulumModel.resume();
		    
	    fuzzyPendulumModelSuspended = false;
    }

    public void stop() 
    {
         fuzzyPendulumModel.requestSuspend();
         fuzzyPendulumModelSuspended = true;
    }
	
	/********************** MAIN *************************/
	public static void main( String args[] )
	{
	    final JFrame f = new JFrame();
	    FuzzyPendulumJApplet fpa = new FuzzyPendulumJApplet();
	    f.setContentPane(fpa.getContentPane());
	    f.setBounds(100,100,800,640);
	    f.setTitle("FuzzyJ Toolkit Pendulum Control");
	    fpa.init();
	    f.setVisible(true);
	    fpa.start();
	    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    f.addWindowListener
	      ( new WindowAdapter()
	         { public void windowClosed(WindowEvent e) { System.exit(0); }
	         }
	      );
	}
	
	

	//{{DECLARE_CONTROLS
	javax.swing.JPanel JPanel1 = new javax.swing.JPanel();
	javax.swing.JPanel blackJPanel = new javax.swing.JPanel();
	javax.swing.JSlider motorJSlider = new javax.swing.JSlider();
	javax.swing.JPanel ruleMatrixPanel = new javax.swing.JPanel();
	javax.swing.ButtonGroup ruleMatrixButtonGroup = new javax.swing.ButtonGroup();
	javax.swing.JButton JButton1 = new javax.swing.JButton();
	javax.swing.JButton JButton2 = new javax.swing.JButton();
	javax.swing.JButton Z_NM_PM_JButton = new javax.swing.JButton();
	javax.swing.JButton JButton4 = new javax.swing.JButton();
	javax.swing.JButton JButton5 = new javax.swing.JButton();
	javax.swing.JButton JButton6 = new javax.swing.JButton();
	javax.swing.JButton JButton7 = new javax.swing.JButton();
	javax.swing.JButton Z_NS_PS_JButton = new javax.swing.JButton();
	javax.swing.JButton PS_NS_Z_JButton = new javax.swing.JButton();
	javax.swing.JButton JButton10 = new javax.swing.JButton();
	javax.swing.JButton NM_Z_PM_JButton = new javax.swing.JButton();
	javax.swing.JButton NS_Z_PS_JButton = new javax.swing.JButton();
	javax.swing.JButton Z_Z_Z_JButton = new javax.swing.JButton();
	javax.swing.JButton PS_Z_NS_JButton = new javax.swing.JButton();
	javax.swing.JButton PM_Z_NM_JButton = new javax.swing.JButton();
	javax.swing.JButton JButton16 = new javax.swing.JButton();
	javax.swing.JButton NS_PS_Z_JButton = new javax.swing.JButton();
	javax.swing.JButton Z_PS_NS_JButton = new javax.swing.JButton();
	javax.swing.JButton JButton19 = new javax.swing.JButton();
	javax.swing.JButton JButton20 = new javax.swing.JButton();
	javax.swing.JButton JButton21 = new javax.swing.JButton();
	javax.swing.JButton JButton22 = new javax.swing.JButton();
	javax.swing.JButton Z_PM_NM_JButton = new javax.swing.JButton();
	javax.swing.JButton JButton24 = new javax.swing.JButton();
	javax.swing.JButton JButton25 = new javax.swing.JButton();
	javax.swing.JLabel JLabel20 = new javax.swing.JLabel();
	javax.swing.JPanel radioPanel1 = new javax.swing.JPanel();
	javax.swing.ButtonGroup radioButtonGroup1 = new javax.swing.ButtonGroup();
	javax.swing.JRadioButton enableJRadioButton = new javax.swing.JRadioButton();
	javax.swing.JRadioButton disableJRadioButton = new javax.swing.JRadioButton();
	javax.swing.JRadioButton displayJRadioButton = new javax.swing.JRadioButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel7 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel8 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel9 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel10 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel11 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel12 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel13 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel14 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel15 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel16 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel17 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel18 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel19 = new javax.swing.JLabel();
	javax.swing.JSlider massJSlider = new javax.swing.JSlider();
	com.klg.jclass.chart.beans.SimpleChart finalCurrentSimpleChart = new com.klg.jclass.chart.beans.SimpleChart();
	com.klg.jclass.chart.beans.SimpleChart errorSimpleChart = new com.klg.jclass.chart.beans.SimpleChart();
	com.klg.jclass.chart.beans.SimpleChart ruleCurrentSimpleChart = new com.klg.jclass.chart.beans.SimpleChart();
	com.klg.jclass.chart.beans.SimpleChart currentTraceSimpleChart = new com.klg.jclass.chart.beans.SimpleChart();
	javax.swing.JCheckBox stopBoppingJCheckBox = new javax.swing.JCheckBox();
	javax.swing.JCheckBox stepModeJCheckBox = new javax.swing.JCheckBox();
	javax.swing.JButton nextStepJButton = new javax.swing.JButton();
	javax.swing.JButton freezeJButton = new javax.swing.JButton();
	javax.swing.JButton pullRightJButton = new javax.swing.JButton();
	javax.swing.JButton pullLeftJButton = new javax.swing.JButton();
	javax.swing.JCheckBox bobUpDownJCheckBox = new javax.swing.JCheckBox();
	javax.swing.JLabel JLabelImplication = new javax.swing.JLabel();
	com.klg.jclass.chart.beans.SimpleChart omegaSimpleChart = new com.klg.jclass.chart.beans.SimpleChart();
	javax.swing.JButton bumpLeftJButton = new javax.swing.JButton();
	javax.swing.JButton bumpRightJButton = new javax.swing.JButton();
	javax.swing.JLabel JLabel21 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel22 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel23 = new javax.swing.JLabel();
	javax.swing.JTextField errorJTextField = new javax.swing.JTextField();
	javax.swing.JTextField omegaJTextField = new javax.swing.JTextField();
	javax.swing.JTextField currentJTextField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel24 = new javax.swing.JLabel();
	Border etchedBorder1 = BorderFactory.createEtchedBorder();
	//}}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == Z_NM_PM_JButton)
				ZNMPMJButton_actionPerformed(event);
			else if (object == Z_NS_PS_JButton)
				ZNSPSJButton_actionPerformed(event);
			else if (object == PS_NS_Z_JButton)
				PSNSZJButton_actionPerformed(event);
			else if (object == NM_Z_PM_JButton)
				NMZPMJButton_actionPerformed(event);
			else if (object == NS_Z_PS_JButton)
				NSZPSJButton_actionPerformed(event);
			else if (object == Z_Z_Z_JButton)
				ZZZJButton_actionPerformed(event);
			else if (object == PS_Z_NS_JButton)
				PSZNSJButton_actionPerformed(event);
			else if (object == PM_Z_NM_JButton)
				PMZNMJButton_actionPerformed(event);
			else if (object == NS_PS_Z_JButton)
				NSPSZJButton_actionPerformed(event);
			else if (object == Z_PS_NS_JButton)
				ZPSNSJButton_actionPerformed(event);
			else if (object == Z_PM_NM_JButton)
				ZPMNMJButton_actionPerformed(event);
			else if (object == nextStepJButton)
				nextStepJButton_actionPerformed(event);
			else if (object == freezeJButton)
				freezeJButton_actionPerformed(event);
			else if (object == pullLeftJButton)
				pullLeftJButton_actionPerformed(event);
			else if (object == pullRightJButton)
				pullRightJButton_actionPerformed(event);
			else if (object == bumpLeftJButton)
				bumpLeftJButton_actionPerformed(event);
			else if (object == bumpRightJButton)
				bumpRightJButton_actionPerformed(event);
		}
	}

	void ZNMPMJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        Z_NM_PM_JButton.setForeground(Color.yellow);
	        displayedRule = "Z_NM_PM";
	        errorSimpleChart.setFooterText("if error Z");
	        omegaSimpleChart.setFooterText("and omega NM");
	        ruleCurrentSimpleChart.setFooterText("then current PM");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "Z_NM_PM");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        Z_NM_PM_JButton.setBackground(color);
	    }
	}

	void ZNSPSJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        Z_NS_PS_JButton.setForeground(Color.yellow);
	        displayedRule = "Z_NS_PS";
	        errorSimpleChart.setFooterText("if error Z");
	        omegaSimpleChart.setFooterText("and omega NS");
	        ruleCurrentSimpleChart.setFooterText("then current PS");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "Z_NS_PS");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        Z_NS_PS_JButton.setBackground(color);
	    }
	}

	void PSNSZJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        PS_NS_Z_JButton.setForeground(Color.yellow);
	        displayedRule = "PS_NS_Z";
	        errorSimpleChart.setFooterText("if error PS");
	        omegaSimpleChart.setFooterText("and omega NS");
	        ruleCurrentSimpleChart.setFooterText("then current Z");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "PS_NS_Z");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        PS_NS_Z_JButton.setBackground(color);
	    }
	}

	void NMZPMJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "NM_Z_PM";
	        NM_Z_PM_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error NM");
	        omegaSimpleChart.setFooterText("and omega Z");
	        ruleCurrentSimpleChart.setFooterText("then current PM");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "NM_Z_PM");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        NM_Z_PM_JButton.setBackground(color);
	    }
	}

	void NSZPSJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "NS_Z_PS";
	        NS_Z_PS_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error NS");
	        omegaSimpleChart.setFooterText("and omega Z");
	        ruleCurrentSimpleChart.setFooterText("then current PS");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "NS_Z_PS");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        NS_Z_PS_JButton.setBackground(color);
	    }
	}

	void ZZZJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "Z_Z_Z";
	        Z_Z_Z_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error Z");
	        omegaSimpleChart.setFooterText("and omega Z");
	        ruleCurrentSimpleChart.setFooterText("then current Z");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "Z_Z_Z");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        Z_Z_Z_JButton.setBackground(color);
	    }
	}

	void PSZNSJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "PS_Z_NS";
	        PS_Z_NS_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error PS");
	        omegaSimpleChart.setFooterText("and omega Z");
	        ruleCurrentSimpleChart.setFooterText("then current NS");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "PS_Z_NS");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        PS_Z_NS_JButton.setBackground(color);
	    }
	}

	void PMZNMJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "PM_Z_NM";
	        PM_Z_NM_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error PM");
	        omegaSimpleChart.setFooterText("and omega Z");
	        ruleCurrentSimpleChart.setFooterText("then current NM");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "PM_Z_NM");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        PM_Z_NM_JButton.setBackground(color);
	    }
	}

	void NSPSZJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "NS_PS_Z";
	        NS_PS_Z_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error NS");
	        omegaSimpleChart.setFooterText("and omega PS");
	        ruleCurrentSimpleChart.setFooterText("then current Z");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "NS_PS_Z");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        NS_PS_Z_JButton.setBackground(color);
	    }
	}

	void ZPSNSJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "Z_PS_NS";
	        Z_PS_NS_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error Z");
	        omegaSimpleChart.setFooterText("and omega PS");
	        ruleCurrentSimpleChart.setFooterText("then current NS");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "Z_PS_NS");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        Z_PS_NS_JButton.setBackground(color);
	    }
	}

	void ZPMNMJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		if (displayJRadioButton.isSelected())
	    {   resetDisplayForegroundColor();
	        displayedRule = "Z_PM_NM";
	        Z_PM_NM_JButton.setForeground(Color.yellow);
	        errorSimpleChart.setFooterText("if error Z");
	        omegaSimpleChart.setFooterText("and omega PM");
	        ruleCurrentSimpleChart.setFooterText("then current NM");
            updateDisplayedRule(fuzzyPendulumModel.error, fuzzyPendulumModel.omega);
	    }
	    else
	    {   fuzzyPendulumModel.setRuleEnabled(enableJRadioButton.isSelected(), "Z_PM_NM");
	        Color color;
	        if (enableJRadioButton.isSelected())
	           color = Color.blue;
	        else
	           color = Color.gray;
	        Z_PM_NM_JButton.setBackground(color);
	    }
	}

	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == stepModeJCheckBox)
				stepModeJCheckBox_itemStateChanged(event);
			else if (object == stopBoppingJCheckBox)
				stopBoppingJCheckBox_itemStateChanged(event);
			else if (object == bobUpDownJCheckBox)
				bobUpDownJCheckBox_itemStateChanged(event);
		}
	}

	void stepModeJCheckBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
		boolean val = stepModeJCheckBox.isSelected();
		fuzzyPendulumModel.setStepMode(val);
		nextStepJButton.setEnabled(val);

		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void stopBoppingJCheckBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
		boolean val = stopBoppingJCheckBox.isSelected();
		fuzzyPendulumModel.setBopping(!val);
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void bobUpDownJCheckBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
		boolean val = bobUpDownJCheckBox.isSelected();
		fuzzyPendulumModel.setBobbing(val);
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void nextStepJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.doNextStep();
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void freezeJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.setFreeze(5);
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void pullLeftJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.pullLeft();
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void pullRightJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.pullRight();
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void bumpLeftJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.bumpLeft();
		simulationPanel.paintComponent(simulationPanel.getGraphics());			 
	}

	void bumpRightJButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
		fuzzyPendulumModel.bumpRight();
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	class SymChange implements javax.swing.event.ChangeListener
	{
		public void stateChanged(javax.swing.event.ChangeEvent event)
		{
			Object object = event.getSource();
			if (object == massJSlider)
				massJSlider_stateChanged(event);
			else if (object == motorJSlider)
				motorJSlider_stateChanged(event);
		}
	}

	void massJSlider_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		// NOTE: We must return a scaled value to the model ...
		double val = (double)massJSlider.getValue();
		double massSliderScale = (double)(massJSlider.getMaximum()-massJSlider.getMinimum());
		val = (val - motorJSlider.getMinimum())/(massSliderScale)*massScale + minMassSize;
		fuzzyPendulumModel.setMassSize(val);
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	void motorJSlider_stateChanged(javax.swing.event.ChangeEvent event)
	{
		// to do: code goes here.
		// NOTE: We must return a scaled value to the model ...
		double val = (double)motorJSlider.getValue();
		double motorSliderScale = (double)(motorJSlider.getMaximum()-motorJSlider.getMinimum());
		val = (val - massJSlider.getMinimum())/(motorSliderScale)*motorScale + minMotorSize;
		fuzzyPendulumModel.setMotorSize(val);
		simulationPanel.paintComponent(simulationPanel.getGraphics());
	}

	
	void resetDisplayForegroundColor()
	{
	    if (displayedRule.equals("Z_NM_PM"))
	       Z_NM_PM_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("Z_NS_PS"))
	       Z_NS_PS_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("PS_NS_Z"))
	       PS_NS_Z_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("NM_Z_PM"))
	       NM_Z_PM_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("NS_Z_PS"))
	       NS_Z_PS_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("Z_Z_Z"))
	       Z_Z_Z_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("PS_Z_NS"))
	       PS_Z_NS_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("PM_Z_NM"))
	       PM_Z_NM_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("NS_PS_Z"))
	       NS_PS_Z_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("Z_PS_NS"))
	       Z_PS_NS_JButton.setForeground(Color.black);
	    else if (displayedRule.equals("Z_PM_NM"))
	       Z_PM_NM_JButton.setForeground(Color.black);
	}
	
	JButton getRuleButton(String ruleName)
	{
	    if (ruleName.equals("Z_NM_PM"))
	      return Z_NM_PM_JButton;
	    else if (ruleName.equals("Z_NS_PS"))
	      return Z_NS_PS_JButton;
	    else if (ruleName.equals("PS_NS_Z"))
	      return PS_NS_Z_JButton;
	    else if (ruleName.equals("NM_Z_PM"))
	      return NM_Z_PM_JButton;
	    else if (ruleName.equals("NS_Z_PS"))
	      return NS_Z_PS_JButton;
	    else if (ruleName.equals("Z_Z_Z"))
	      return Z_Z_Z_JButton;
	    else if (ruleName.equals("PS_Z_NS"))
	      return PS_Z_NS_JButton;
	    else if (ruleName.equals("PM_Z_NM"))
	      return PM_Z_NM_JButton;
	    else if (ruleName.equals("NS_PS_Z"))
	      return NS_PS_Z_JButton;
	    else if (ruleName.equals("Z_PS_NS"))
	      return Z_PS_NS_JButton;
	    else if (ruleName.equals("Z_PM_NM"))
	      return Z_PM_NM_JButton;
	      
	    return null;
	}
	
	// modify the interface whenever a time step occurs -- called by the 
	// Pendulum Simulation Class (thread)on each simulation cycle
    void updateSimulationComponents
                   (String[] ruleNames, boolean[] rulesFired, 
                    boolean[] rulesEnabled, double theta, 
                    double error, double omega, double current,
                    FuzzySet globalCurrentFS
                   )
    {
        int i;
        
        // draw the pendulum and its parts
        simulationPanel.paintComponent(simulationPanel.getGraphics());
        
        // refresh all other gui components (graphs, etc.)
        for (i=0; i < ruleNames.length; i++)
        {  if (rulesEnabled[i]) 
           {  JButton ruleButton = getRuleButton(ruleNames[i]);
              if (ruleButton != null && rulesFired[i])
                 ruleButton.setBackground(Color.green);
              else
                 ruleButton.setBackground(Color.blue);
           }
        }
        errorJTextField.setText(nf.format(error));
        omegaJTextField.setText(nf.format(omega));
        currentJTextField.setText(nf.format(current));
        
        ctData.addNextValue(current);
        fcData.showFuzzySet(globalCurrentFS);
        
        updateDisplayedRule(error, omega);
    }
    
    void updateDisplayedRule(double error, double omega)
    {
        if (fuzzyPendulumModel.isRuleEnabled(displayedRule))
        {
            FuzzyRule rule = fuzzyPendulumModel.getFuzzyRule(displayedRule);
            FuzzySet eaFS = rule.antecedentAt(0).getFuzzySet();
            FuzzySet oaFS = rule.antecedentAt(1).getFuzzySet();
            FuzzySet ccFS = rule.conclusionAt(0).getFuzzySet();
            double errorMembership = eaFS.getMembership(error);
            double omegaMembership = oaFS.getMembership(omega);
            eaData.showFuzzySets(eaFS, errorMembership);
            oaData.showFuzzySets(oaFS, omegaMembership);
            ccData.showFuzzySets(ccFS, Math.min(errorMembership, omegaMembership));
        }
        else
        {
            eaData.showFuzzySets(null, 0);
            oaData.showFuzzySets(null, 0);
            ccData.showFuzzySets(null, 0);
        }
    }
	
}
