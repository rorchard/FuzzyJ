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
 * of 1 at the left edge and 0 at the right edge. Users do not generally 
 * use this class to create FuzzySets directly but most often use
 * the subclasses of this class, such as ZFuzzySet or RightLinearFuzzySet. The 
 * shape of the curves between the left and right x values of an RFuzzySet
 * are determined by the function specified. This function will be a class
 * that implements the FuzzySetFunction Interface. In particular the 
 * ZFuzzySet class uses the ZFunction class to create a Z-shaped curve between the left
 * and right x values.
 * <p>The diagram below shows the concept of the RFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/RFuzzySet.gif">
 * <br> <br>
 * The code below shows how the ZFuzzySet constructor is defined.
 * <br>
 * <pre>
 *  private static ZFunction szf = new ZFunction();
 *
 *  public ZFuzzySet(double leftEdge, double rightEdge) throws XValuesOutOfOrderException 
 *  {
 *      super(leftEdge, rightEdge, szf);
 *  }
 * </pre>
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see ZFuzzySet
 * @see ZFunction
 * @see RightLinearFunction
 * @see RightLinearFuzzySet
 *
 */
public class RFuzzySet extends FuzzySet implements Serializable 
{
    /**
     * Create an RFuzzySet using the FuzzySetFunction supplied to construct it.
     *
     * @param leftX Left x value (where membership is 1) of the R (right sided) FuzzySet
     * @param rightX Right x value (where membership is 0) of the R (right sided) FuzzySet
     * @param rightFunction Function that will generate the shaped right side for the FuzzySet
     * @exception XValuesOutOfOrderException
     */
    public RFuzzySet(double leftX, double rightX, FuzzySetFunction rightFunction) 
        throws XValuesOutOfOrderException 
    {
        checkParameters(leftX, rightX);

        FuzzySet rightSide = rightFunction.generateFuzzySet(leftX, rightX);

        set = rightSide.set;
        numPoints = rightSide.numPoints;

        //simplifySet(); --- simplify is (should be) done in generateFuzzySet
    }

    /**
     * Checks that left x value is less than or equal to right x value.
     *
     * @param leftX Left x value (where membership is 1) of the R (right sided) FuzzySet
     * @param rightX Right x value (where membership is 0) of the R (right sided) FuzzySet
     * @exception XValuesOutOfOrderException
     */

    protected void checkParameters(double leftX, double rightX) 
        throws XValuesOutOfOrderException 
    {
        if(leftX > rightX){
            String s = "RFuzzySet - leftX MUST be less than or equal to rightX.\n";

            throw new XValuesOutOfOrderException(leftX, rightX, s);
        }
    }

}