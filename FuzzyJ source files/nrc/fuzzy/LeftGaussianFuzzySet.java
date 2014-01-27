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
 * Used to build specialized FuzzySets that in have a shape similar to
 * the left half of a gaussian curve with a 0 at the lower left edge and 1 at the upper right edge. 
 * The diagram below shows an LeftGaussianFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/LeftGaussianFuzzySet.gif">
  * <br>
 * The curves are defined by the following equation:
 * <br> <br>
 * <img src = "NetGraphics/GaussianEquation.gif">
 * <br> <br>
 * where a is the mean and sigma is the standard deviation.
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only the centre and the standard deviation of the 
 * gaussian curve need be specified (with an optional constructor that allows the number of
 * points on the curve approximation to be specified if necessary). Points from 4 standard deviations 
 * left of the centre will be 0. Points to the right of the Centre point will be 1.
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LFuzzySet
 * @see LeftGaussianFunction
 *
 */
public class LeftGaussianFuzzySet extends LFuzzySet implements Serializable
{    
    private static LeftGaussianFunction lgf = new LeftGaussianFunction();

    /**
     * Generate a FuzzySet with left half of a gaussian shape. 
     *
     * @param centre the upper right of the curve with a membership value of 1
     * @param standardDeviation the standard deviation for the gaussian curve (points 4 
     * standard deviations to the left of the centre point will all be 0).
     *  
     * @exception XValuesOutOfOrderException if the leftEdge argument is greater than the
     *                rightEdge argument
     */

    public LeftGaussianFuzzySet(double centre, double standardDeviation) throws XValuesOutOfOrderException {
        super(centre-4.0*standardDeviation, centre, lgf);
    }

    /**
     * Generate a FuzzySet with left half of a gaussian shape. 
     *
     * @param centre the upper right of the curve with a membership value of 1
     * @param standardDeviation the standard deviation for the gaussian curve (points 4 
     * standard deviations to the left of the centre point will all be 0).
     * @param numPoints the number of points to use to approximate the gaussian shape
     * @exception XValuesOutOfOrderException if the leftEdge argument is less than the
     *                rightEdge argument
     */
    public LeftGaussianFuzzySet(double centre, double standardDeviation, int numPoints) throws XValuesOutOfOrderException {
        super(centre-4.0*standardDeviation, centre, new LeftGaussianFunction(numPoints));
    }
}


