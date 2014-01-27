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
 * Used to build specialized FuzzySets that have a 'gaussian' shape with a 
 * 0 at the left and right edges of the curve and a 1 in the middle section. 
 * The diagram below shows a couple of GaussianFuzzySets, one where the
 * left and right sides of the curve meet at a single point and one where
 * the left and right sides are separated.
 * <br> <br>
 * <img src = "NetGraphics/GaussianFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only the middle point(s) 
 * and the standard deviation(s) of the curve need be specified 
 * (with optional constructor(s) that allow the number of points on the curve 
 * approximation to be specified if necessary -- or use the default number of
 * points for a leftGaussianFuzzySet and rightGaussianFuzzySet; 
 * this is controlled by parameters in the leftGaussianFunction
 * and rightGaussianFunction classes).
 * <p> Note that this is a specialization of the LRFuzzySet with specific 
 * functions used to determine the shape of the left (leftGaussianFunction) and 
 * right (rightGaussianFunction) sides. The left and right shapes may or may not
 * join at the same point.
 * <br>
 * The curves are defined by the following equation:
 * <br> <br>
 * <img src = "NetGraphics/GaussianEquation.gif">
 * <br> <br>
 * where a is the mean and sigma is the standard deviation.
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see leftGaussianFunction
 * @see rightGaussianFunction
 *
 */

public class GaussianFuzzySet extends LRFuzzySet implements Serializable
{    
    private static LeftGaussianFunction lgf = new LeftGaussianFunction();
    private static RightGaussianFunction rgf = new RightGaussianFunction();
    

    /**
     * Generate a FuzzySet with a 'gaussian' shape. 
     *
     * @param center the center of the curve with a membership value of 1
     * @param standardDeviation the standard deviation for the gaussian curve 
     * @exception XValuesOutOfOrderException if the parameters determine
     *                an illegal arrangement (such as a negative width)
     */

    public GaussianFuzzySet(double center, double standardDeviation) throws XValuesOutOfOrderException 
    {
        super(center - standardDeviation*4.0, center, center, center + standardDeviation*4.0, lgf, rgf);
    }

	/**
	 * Generate a FuzzySet with a 'gaussian' shape. 
	 *
	 * @param center the center of the curve with a membership value of 1
	 * @param standardDeviation the standard deviation for the gaussian curve
	 * @param numPoints the number of points to use to approximate each side of the shape
	 * @exception XValuesOutOfOrderException if the parameters determine
	 *                an illegal arrangement (such as a negative width)
	 */
	public GaussianFuzzySet(double center, double standardDeviation, int numPoints) throws XValuesOutOfOrderException 
	{
		super(center - standardDeviation*4.0, center, center, center + standardDeviation*4.0,
			  new LeftGaussianFunction(numPoints), new RightGaussianFunction(numPoints));
	}

	/**
	 * Generate a FuzzySet with a 'gaussian' shape. The left part of the shape
	 * may have a different standard deviation from the right part of the shape and
	 * the left and right shapes may not meet at the same point.
	 *
	 * @param leftCentre the center of the left side curve with a membership value of 1
	 * @param leftStandardDeviation the standard deviation for the left half of the gaussian curve
	 * @param rightCentre the center of the right side curve with a membership value of 1
	 * @param rightStandardDeviation the standard deviation for the right half of the gaussian curve
	 * @param numPoints the number of points to use to approximate each side of the shape
	 * @exception XValuesOutOfOrderException if the parameters determine
	 *                an illegal arrangement (such as a negative width)
	 */
	public GaussianFuzzySet(double leftCenter, double leftStandardDeviation,
							double rightCenter, double rightStandardDeviation, 
							int numPoints) throws XValuesOutOfOrderException 
	{
		super(leftCenter - leftStandardDeviation*4.0, leftCenter, rightCenter, rightCenter + rightStandardDeviation*4.0,
			  new LeftGaussianFunction(numPoints), new RightGaussianFunction(numPoints));
	}

	/**
	 * Generate a FuzzySet with a 'gaussian' shape. The left part of the shape
	 * may have a different standard deviation from the right part of the shape and
	 * the left and right shapes may not meet at the same point.
	 *
	 * @param leftCentre the center of the left side curve with a membership value of 1
	 * @param leftStandardDeviation the standard deviation for the left half of the gaussian curve
	 * @param rightCentre the center of the right side curve with a membership value of 1
	 * @param rightStandardDeviation the standard deviation for the right half of the gaussian curve
	 * @exception XValuesOutOfOrderException if the parameters determine
	 *                an illegal arrangement (such as a negative width)
	 */
	public GaussianFuzzySet(double leftCenter, double leftStandardDeviation,
							double rightCenter, double rightStandardDeviation) 
							throws XValuesOutOfOrderException 
	{
		super(leftCenter - leftStandardDeviation*4.0, leftCenter, 
		      rightCenter, rightCenter + rightStandardDeviation*4.0,
			  lgf, rgf);
	}
}
