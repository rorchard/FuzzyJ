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
 * The <code>LRFuzzySet</code> class allows the construction of a wide range of FuzzySets
 * with relatively the same shape.
 *
 * <p>Consider the diagram below.  The point labelled <b>1</b> is the farthest left point on
 * the FuzzySet and it also has a membership function of 0.0.  Point <b>2</b> is the
 * farthest left point with a membership value of 1.0, point <b>3</b> is the farthest
 * right point with a membership function of 1.0, and point <b>4</b> is the farthest right
 * point with a membership function of 0.0.
 *
 * <img src="NetGraphics/LRFuzzySet.gif">
 *
 * <p>These four points are a fundamental characteristic of the LRFuzzySet.  The LRFuzzySet
 * will always be normal and convex, as the membership function of the beginning and end
 * points is always 0.0, and the FuzzySet will always reach unity (whether contact with
 * unity is extended or not; in other words, points <b>2</b> and <b>3</b> can have either
 * different x values, or the same, depending on the shape of the LRFuzzySet desired), but
 * never more than once. The x values of these four points are required from the user to
 * construct a LRFuzzySet.
 *
 * <p>In addition to these four points, the LRFuzzySet requires a function to define the left
 * half of the FuzzySet and a function to define the right half of the FuzzySet.  For example,
 * in the small diagram above, it appears that a linear function defines the shape of the
 * FuzzySet between points <b>1</b> and <b>2</b>, and again between points <b>3</b> and
 * <b>4</b>.  Another example would be to have an S function on the left side of the LRFuzzySet
 * and a Z function on the right side of the LRFuzzySet.
 *
 * <p>From this basic template, many different shapes of FuzzySets can be created.  For
 * example, the PI FuzzySet, a rectangle, a trapezoid, a triangle, and others.
 *
 * @author Bob Orchard
 * @author Alexis Eller
 *
 * @see FuzzySet
 * @see RectangleFuzzySet
 * @see TriangleFuzzySet
 * @see TrapezoidFuzzySet
 * @see PIFuzzySet
 *
 */

public class LRFuzzySet extends FuzzySet implements Serializable
{
    /**
     * Constructs a FuzzySet with the LRFuzzySet shape.
     *
     * <p>The values of the different x parameters MUST be as follows:
     * <p>zeroLeftX <= oneLeftX <= oneRightX <= zeroRightX. <p> If values are entered such that
     * the previous expression is not satisfied, an <code>XValuesOutOfOrderException</code>
     * will be thrown.
     *
     * @param zeroLeftX     the x value of the point with zero
     *                      membership on the left side of the LRFuzzySet
     * @param oneLeftX      the x value of the point with
     *                      membership value 1.0 on the left side of the LRFuzzySet
     * @param oneRightX     the x value of the point with
     *                      membership value 1.0 on the right side of the LRFuzzySet
     * @param zeroRightX    the x value of the point with zero
     *                      membership on the right side of the LRFuzzySet
     * @param leftFunction  a FuzzySetFunction that defines the shape of the 
     *                      left portion of the curve
     * @param rightFunction a FuzzySetFunction that defines the shape of the 
     *                      right portion of the curve
     * @exception XValuesOutOfOrderException
     *
     */

    public LRFuzzySet(double zeroLeftX, double oneLeftX, double oneRightX, double zeroRightX,
                      FuzzySetFunction leftFunction, FuzzySetFunction rightFunction) 
               throws XValuesOutOfOrderException 
    {
        checkParameters(zeroLeftX, oneLeftX, oneRightX, zeroRightX);

        FuzzySet leftSide = leftFunction.generateFuzzySet(zeroLeftX, oneLeftX);
        FuzzySet rightSide = rightFunction.generateFuzzySet(oneRightX, zeroRightX);

        set = new SetPoint[leftSide.numPoints + rightSide.numPoints];

        for(int i=0; i<leftSide.numPoints; i++){
            this.appendSetPoint(leftSide.getPoint(i));
        }

        for(int i=0; i<rightSide.numPoints; i++){
            this.appendSetPoint(rightSide.getPoint(i));
        }

        simplifySet();
    }

    /**
     * Checks that the parameters for the LR set creation are in correct x order.
     *
     * @param zeroLeftX     the x value of the point with zero
     *                      membership on the left side of the LRFuzzySet
     * @param oneLeftX      the x value of the point with
     *                      membership value 1.0 on the left side of the LRFuzzySet
     * @param oneRightX     the x value of the point with
     *                      membership value 1.0 on the right side of the LRFuzzySet
     * @param zeroRightX    the x value of the point with zero
     *                      membership on the right side of the LRFuzzySet
     * @exception XValuesOutOfOrderException
     */

    protected void checkParameters(double zeroLeftX, double oneLeftX, double oneRightX, double zeroRightX)
        throws XValuesOutOfOrderException {

        if(zeroLeftX > oneLeftX || oneLeftX > oneRightX || oneRightX > zeroRightX){

            String s = "LRFuzzySet x parameters are not in ascending x value order.\n";
            double prevValue = 0;
            double currentValue = 0;

            if(zeroLeftX > oneLeftX){
                s = s + "-> zeroLeftX MUST be greater than or equal to oneLeftX.\n";
                prevValue = zeroLeftX;
                currentValue = oneLeftX;
            } else if(oneLeftX > oneRightX){
                s = s + "-> oneLeftX MUST be greater than or equal to oneRightX.\n";
                prevValue = oneLeftX;
                currentValue = oneRightX;
            } else if(oneRightX > zeroRightX){
                s = s + "-> oneRightX MUST be greater than or equal to zeroRightX.\n";
                prevValue = oneRightX;
                currentValue = zeroRightX;
            }

            throw new XValuesOutOfOrderException(prevValue, currentValue, s);
        }
    }
}