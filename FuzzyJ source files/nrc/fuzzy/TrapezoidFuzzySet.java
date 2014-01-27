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
 * Used to build specialized FuzzySets that have a trapezoid shape. 
 * The diagram below shows a TrapezoidFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/TrapezoidFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only 4 points are required to define
 * the trapezoid shape. 
 * <p> This is a specialization of the LRFuzzySet where the left and right
 * shapes of the curve are linear.
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see TriangleFuzzySet
 * @see LeftLinearFunction
 * @see RightLinearFunction
 *
 */

public class TrapezoidFuzzySet extends LRFuzzySet implements Serializable
{
    /**
     * Generate a FuzzySet with an trapeziod shape. 
     *
     * @param zeroLeftX the start of the curve at the left with a membership value of 0
     * @param oneLeftX the end of the left upwards sloping line with a membership value of 1
     * @param oneRightX the start of the right downwards sloping line with a membership value of 1
     * @param zeroRightX the end of the right downwards sloping line with a membership value of 0
     * @exception XValuesOutOfOrderException if the x values are not in correct increasing order
     */

    public TrapezoidFuzzySet(double zeroLeftX, double oneLeftX, double oneRightX, double zeroRightX) throws 
        XValuesOutOfOrderException {

        super(zeroLeftX, oneLeftX, oneRightX, zeroRightX,
              new LeftLinearFunction(), new RightLinearFunction());
    }
}
