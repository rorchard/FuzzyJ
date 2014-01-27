/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at
 *  http://mozilla.org/MPL/2.0/. 
 *
 * Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006 National Research Council of Canada 
 * 
 * This software was initially developed at the National Research Council of Canada (NRC).
 *
 * THE NATIONAL RESEARCH COUNCIL OF CANADA MAKES NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT.
 * THE NATIONAL RESEARCH COUNCIL OF CANADA SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 *
 */


package nrc.fuzzy;

import java.io.*;

/**
 * The <code>SetPoint</code> class provides a means for representing a point
 * with an x and y coordinate.  In this implementation of fuzzy logic, the y
 * value represents the membership value of the FuzzyValue/FuzzySet at that
 * particular x.  The name SetPoint came from the fact that FuzzySets are
 * made up of sets of such points.
 *
 * @author Bob Orchard
 *
 * @see Parameters
 */
 
public class SetPoint implements Serializable
{
    /**
     * The x value of this SetPoint.
     */
    protected double x;
    
    /**
     * The membership value (graphically, the y value) of the point 
     * x in a FuzzySet.
     */
    protected double y;

    /**
     * Constructs a new SetPoint object initialized with its x and y 
     * values equal to zero.
     */
    
    public SetPoint(){
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Constructs a new SetPoint object which has x and y values equal
     * to that of the SetPoint argument.
     *
     * @param a the SetPoint whose x and y values will be used to initialize
     *          the x and y values of the newly created SetPoint object
     */

    public SetPoint(SetPoint a){
        this.x = a.x;
        this.y = a.y;
    }
    
    /**
     * Constructs a new SetPoint object which has x and y values equal
     * to the double arguments, <code>x</code> and <code>y</code>.
     *
     * @param x the double value used to initialize the x value of the
     *          new SetPoint object
     * @param y the double value used to initialize the y value of the
     *          new SetPoint object
     */

    public SetPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Tests whether the SetPoint argument is equal to this SetPoint. Note that 
     * the x and y values of the two SetPoints do not have to be identical, they 
     * only need to be very close as determined by the value of Parameters.FUZZY_TOLERANCE.
     *
     * @param a the SetPoint to compare this SetPoint to
     * @return  <code>true</code> if the x and y variables of the two
     *          SetPoints are equal within the value of Parameters.FUZZY_TOLERANCE.
     */

    public boolean equals(SetPoint a){
    	//code below is compact but this method is called very frequently
    	// so equals method was rewritten to NOT use the call to Math.abs
    	//
    	//return((Math.abs(this.x - a.x) < Parameters.FUZZY_TOLERANCE) &&
        //       (Math.abs(this.y - a.y) < Parameters.FUZZY_TOLERANCE)
        //      );
  	    double diffX = this.x - a.x;
        if (diffX < 0.0) diffX = -diffX;
        if (diffX >= Parameters.FUZZY_TOLERANCE) return false;
        double diffY = this.y - a.y;
        if (diffY < 0.0) diffY = -diffY;
        if (diffY >= Parameters.FUZZY_TOLERANCE) return false;
        return true;
    	
    }
    
    /**
     * Tests whether the SetPoint argument is in vertical alignment
     * with this SetPoint.
     *
     * @param a the SetPoint whose x value is to be compared to
     *          the x value of this SetPoint
     * @return  <code>true</code> if the x values of both SetPoints
     *          are equal within fuzzy tolerance.
     */

    protected boolean inVerticalAlignment(SetPoint a){
        return(Math.abs(this.x - a.x) < Parameters.FUZZY_TOLERANCE);
    }

    /**
     * Tests whether the SetPoint argument is in horizontal alignment
     * with this SetPoint.
     *
     * @param a the SetPoint whose y value is to be compared to
     *          the y value of this SetPoint
     * @return  <code>true</code> if the y values of both SetPoints
     *          are equal within fuzzy tolerance.
     */

    protected boolean inHorizontalAlignment(SetPoint a){
        return(Math.abs(this.y - a.y) < Parameters.FUZZY_TOLERANCE);
    }
    
    /**
     * Sets the x and y values of the SetPoint.
     *
     * @param x the double value to be assigned to the x variable of this SetPoint
     * @param y the double value to be assigned to the y variable of this SetPoint
     */

    public void setXY(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Sets the x value of the SetPoint.
     *
     * @param x the double value to be assigned to the x variable of this SetPoint
     */

    public void setX(double x){
        this.x = x;
    }
    
    /**
     * Sets the y value of the SetPoint.
     *
     * @param y the double value to be assigned to the y variable of this SetPoint
     */

    public void setY(double y){
        this.y = y;
    }

    /**
     * Gets the x value of the SetPoint.
     *
     */

    public double getX(){
        return x;
    }
    
    /**
     * Gets the y value of the SetPoint.
     *
     */

    public double getY(){
        return y;
    }

}
