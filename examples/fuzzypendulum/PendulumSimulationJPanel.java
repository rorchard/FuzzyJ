/*
 * @(#)PendulumSimulationJPanel.java	1.0 2000/06/10
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

import javax.swing.*;
import java.beans.*;
import java.awt.*;

/** 
* Draw the pendulum, base, bopper etc in a panel, taking into account
* whether bopping and/or bobbing are enabled.
*/
public class PendulumSimulationJPanel extends javax.swing.JPanel
{
    // the model that holds the current values for the simulation (current,
    // theta, mass size, motor size)
    private FuzzyPendulumModel fuzzyModel; 
	private int midX;        // middle X position in the panel
	private int baseBottom;  // bottom Y position of the base in the drawing
	private int baseTop;     // top Y position of the base in the drawing
	private int[] basePolyX; // polygon X values for the base in the drawing
	private int[] basePolyY; // polygon Y values for the base in the drawing
	private Polygon basePoly;// polygon for the base in the drawing
	private boolean firstPaint = true; // true only for the 1st execution of paintComponent

    // Some drawing constants
    final static int BASEWIDTH = 40;
    final static int BASEWIDTHBY2 = BASEWIDTH/2;
    final static int BASEHEIGHT = 25;
    final static int MAXSTICKLENGTH = 75;
    final static int BOPPER_HEIGHT = 10;
    
    // next values used to calculate a radius for the mass and motor
    // circles ... below we take the actual mass or motor size value
    // and scale it to between 0 and 1, then multiply by the SIZE_FACTOR
    // (and add a SIZE_MIN to keep from being a radius of 0), then take 
    // the sqrt of the value to get a radius that gives an area roughly
    // proportional to the sizes of the motor or mass.
    final static double MOTOR_SIZE_FACTOR = 200;
    final static double MASS_SIZE_FACTOR = 200;
    final static double MOTOR_SIZE_MIN = 16;
    final static double MASS_SIZE_MIN = 16;
    
    // some values obtained from the model and used to scale to drawing area
    private double maxStickLength;
    private double maxMassSize;
    private double minMassSize;
    private double maxMotorSize;
    private double minMotorSize;
    private double massSizeRange;
    private double motorSizeRange;
    
    // a counter used to draw the 'bopper' rod that comes from the left
    private int bopCount = 0;
    
	public PendulumSimulationJPanel(FuzzyPendulumModel fuzzyPendulumModel)
	{
		super();
		//{{INIT_CONTROLS
		setLayout(null);
		//Insets ins = getInsets();
		//setSize(ins.left + ins.right + 430,ins.top + ins.bottom + 270);
		//}}

		//{{REGISTER_LISTENERS
		//}}
		
		fuzzyModel = fuzzyPendulumModel;
		
        maxStickLength = fuzzyModel.getMaxStickLength();
        maxMassSize = fuzzyModel.getMaxMassSize();
        minMassSize = fuzzyModel.getMinMassSize();
        maxMotorSize = fuzzyModel.getMaxMotorSize();
        minMotorSize = fuzzyModel.getMinMotorSize();
        massSizeRange = maxMassSize - minMassSize;
        motorSizeRange = maxMotorSize - minMotorSize;
        
		// Following 2 lines may be needed ... tool tips sometimes cause problems
		// Seems only to be the case in Visual Cafe environment (version 4.0a)
		//if (ToolTipManager.sharedInstance().isEnabled())
        //      ToolTipManager.sharedInstance().setEnabled(false);
	}

	//{{DECLARE_CONTROLS
	//}}

    /** Draw the pendulum for the simulation */
    public void paintComponent(Graphics g)
    {
        int stickTopX, stickTopY;
        double theta = fuzzyModel.getTheta();
        double scaledMotorSize = 
               (fuzzyModel.getMotorSize()-minMotorSize)/motorSizeRange*MOTOR_SIZE_FACTOR+MOTOR_SIZE_MIN;
        int motorRadius = (int)(Math.round(Math.sqrt(scaledMotorSize)));
        double scaledMassSize = 
               (fuzzyModel.getMassSize()-minMassSize)/massSizeRange*MASS_SIZE_FACTOR+MASS_SIZE_MIN;
        int massRadius = (int)(Math.round(Math.sqrt(scaledMassSize)));
        double stickLength = fuzzyModel.getStickLength()*MAXSTICKLENGTH;
        stickTopX = (int)(Math.round(stickLength * Math.cos(theta)));
        stickTopY = (int)(Math.round(stickLength * Math.sin(theta)));

        if (g == null) return;

        super.paintComponent( g );
        
        if (firstPaint)
        {   // first time through we do calcs for the base polygon on
            // which the motor rests
		    midX = getSize().width/2;
		    baseBottom = getSize().height;
		    baseTop = getSize().height-BASEHEIGHT;
		    basePolyX = new int[3];
		    basePolyX[0] = midX-BASEWIDTHBY2;
		    basePolyX[1] = midX;
		    basePolyX[2] = midX+BASEWIDTHBY2;
		    basePolyY = new int[3];
		    basePolyY[0] = baseBottom;
		    basePolyY[1] = baseTop;
		    basePolyY[2] = baseBottom;
		    basePoly = new Polygon(basePolyX, basePolyY, 3);
		    firstPaint = false;
        }
        g.setColor(Color.blue);
        g.fillPolygon(basePoly);
		//g.setPaintMode();
        g.setColor(Color.yellow);
		g.drawLine(midX, baseTop, midX+stickTopX, baseTop-stickTopY);
        g.setColor(Color.cyan);
        g.fillOval(midX-motorRadius, baseTop-motorRadius, motorRadius*2, motorRadius*2);
        g.setColor(Color.red);
        g.fillOval(midX+stickTopX-massRadius, baseTop-stickTopY-massRadius, massRadius*2, massRadius*2);
        
        // draw the bopper if enabled and needed
        if (fuzzyModel.isBopping() &&
             ( (Math.abs(fuzzyModel.error) < .02 &&
                Math.abs(fuzzyModel.omega) < .02
               ) 
                 ||
               ( bopCount < 8 && bopCount > 0)
             )
           )
        {  int width, y;
           if (bopCount < 8)
           {  bopCount++;
              width = (midX+stickTopX-massRadius)*bopCount/8;
           }
           else
              width = midX+stickTopX-massRadius;
           y = baseTop-stickTopY-BOPPER_HEIGHT/2;
           g.setColor(Color.yellow);
           g.fillRect(0, y, width, BOPPER_HEIGHT);
           if (bopCount == 8)
           {   fuzzyModel.omega = -5.0;
               bopCount = 0;
           }
        }
        else 
           bopCount = 0;
    }
}