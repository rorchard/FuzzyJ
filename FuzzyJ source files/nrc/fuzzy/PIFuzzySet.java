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
 * Used to build specialized FuzzySets that in have a 'bell' shape with a 
 * 0 at the left and right edges of the curve and a 1 at the middle. 
 * The diagram below shows an PIFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/PIFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only the middle point and the width of the 
 * curve need be specified (with an optional constructor that allows the number of
 * points on the curve approximation to be specified if necessary -- the default
 * PIFuzzySet is created with an SFunctions and ZFunction with 5 points each
 * so that the default PI curve will be approximated by 9 points).
 * <p> Note that this is a specialization of the LRFuzzySet with specific 
 * functions used to determine the shape of the left (SFunction) and 
 * right (ZFunction) sides. Also the left and right shapes join at the same 
 * point giving the required 'bell' shape.
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LFuzzySet
 * @see SFunction
 *
 */

public class PIFuzzySet extends LRFuzzySet implements Serializable
{    
    private static SFunction ssf = new SFunction(5);
    private static ZFunction szf = new ZFunction(5);
    

    /**
     * Generate a FuzzySet with a 'bell' shape. 
     *
     * @param center the center of the curve with a membership value of 1
     * @param curveWidth the width of each side of the curve 
     * @exception XValuesOutOfOrderException if the parameters determine
     *                an illegal arrangement (such as a negative width)
     */

    public PIFuzzySet(double center, double curveWidth) throws XValuesOutOfOrderException {
        super(center - curveWidth, center, center, center + curveWidth, ssf, szf);
    }

    /**
     * Generate a FuzzySet with a 'bell' shape. 
     *
     * @param center the center of the curve with a membership value of 1
     * @param curveWidth the width of each side of the curve 
     * @param numPoints the number of points to use to approximate each side of the shape
     * @exception XValuesOutOfOrderException if the parameters determine
     *                an illegal arrangement (such as a negative width)
     */
    public PIFuzzySet(double center, double curveWidth, int numPoints) throws XValuesOutOfOrderException {
        super(center - curveWidth, center, center, center + curveWidth,
              new SFunction(numPoints), new ZFunction(numPoints));
    }
}
