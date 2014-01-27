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
 * Used to build specialized FuzzySets that in general have membership values
 * of 0 at the left edge and 1 at the right edge. Users do not generally 
 * use this class to create FuzzySets directly but most often use
 * the subclasses of this class, such as SFuzzySet or LeftLinearFuzzySet. The 
 * shape of the curves between the left and right x values of an LFuzzySet
 * are determined by the function specified. This function will be a class
 * that implements the FuzzySetFunction Interface. In particular the 
 * SFuzzySet class uses the SFunction class to create an S-shaped curve between the left
 * and right x values.
 * <p>The diagram below shows the concept of the LFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/LFuzzySet.gif">
 * <br> <br>
 * The code below shows how the SFuzzySet constructor is defined.
 * <br>
 * <pre>
 *  private static SFunction ssf = new SFunction();
 *
 *  public SFuzzySet(double leftEdge, double rightEdge) throws XValuesOutOfOrderException 
 *  {
 *      super(leftEdge, rightEdge, ssf);
 *  }
 * </pre>
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see SFunction
 * @see SFuzzySet
 * @see LeftLinearFunction
 * @see LeftLinearFuzzySet
 *
 */
public class LFuzzySet extends FuzzySet implements Serializable
{
    /**
     * Create an LFuzzySet using the FuzzySetFunction supplied to construct it.
     *
     * @param leftX Left x value (where membership is 0) of the L (left sided) FuzzySet
     * @param rightX Right x value (where membership is 1) of the L (left sided) FuzzySet
     * @param leftFunction Function that will generate the shaped left side for the FuzzySet
     * @exception XValuesOutOfOrderException
     */

    public LFuzzySet(double leftX, double rightX, FuzzySetFunction leftFunction) 
        throws XValuesOutOfOrderException
    {
        checkParameters(leftX, rightX);

        FuzzySet leftSide = leftFunction.generateFuzzySet(leftX, rightX);

        set = leftSide.set;
        numPoints = leftSide.numPoints;

       // simplifySet();  -- (should be) already simplified in generateFuzzySet
    }

    /**
     * Make sure that left x value is less than or equal to the right x value.
     *
     * @param leftX Left x value (where membership is 0) of the L (left sided) FuzzySet
     * @param rightX Right x value (where membership is 1) of the L (left sided) FuzzySet
     * @exception XValuesOutOfOrderException
     */

    protected void checkParameters(double leftX, double rightX) 
        throws XValuesOutOfOrderException
    {
        if(leftX > rightX){
            String s = "LFuzzySet - leftX MUST be less than or equal to rightX.\n";

            throw new XValuesOutOfOrderException(leftX, rightX, s);
        }
    }
}