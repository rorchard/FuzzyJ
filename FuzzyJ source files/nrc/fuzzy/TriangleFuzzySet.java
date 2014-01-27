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
 * Used to build specialized FuzzySets that have a triangle shape. 
 * The diagram below shows a TriangleFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/TriangleFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only 3 points are required to define
 * the triangle shape. 
 * <p> This is a specialization of the TrapezoidFuzzySet where the left and right
 * shapes meet at the same point (oneLeftX equals oneRightX for a Trapezoid).
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see TrapezoidFuzzySet
 *
 */

public class TriangleFuzzySet extends TrapezoidFuzzySet implements Serializable
{
    /**
     * Generate a FuzzySet with a triangle shape, specifying the centre value and the left and
     * right values of the triangle.
     *
     * @param leftBottom the start of the curve at the left with a membership value of 0
     * @param middleTop the middle point with a membership value of 1
     * @param rightBottom the end of the curve with a membership value of 0
     * @exception XValuesOutOfOrderException if the x values are not in correct increasing order
     */
    public TriangleFuzzySet(double leftBottom, double middleTop, double rightBottom) throws XValuesOutOfOrderException {
        super(leftBottom, middleTop, middleTop, rightBottom);
    }

    /**
     * Generate a FuzzySet with a symmetric triangle shape, specifying the centre point and the 
     * full width of the base of the triangle
     *
     * @param middleTop the middle point with a membership value of 1
     * @param baseWidth the full width of the base of the triangle
     * @exception XValuesOutOfOrderException if the values create an impossible situation (eg. negative width)
     */
    public TriangleFuzzySet(double middleTop, double baseWidth) throws XValuesOutOfOrderException {
    //NOTE: inconsistency in that most other 'baseWidth' type variables actually represent
    // half the base width of their shape, not the full baseWidth as below
        super(middleTop - baseWidth/2, middleTop, middleTop, middleTop + baseWidth/2);
    }
}
