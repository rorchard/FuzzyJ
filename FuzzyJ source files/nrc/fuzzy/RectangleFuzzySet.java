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
 * Used to build specialized FuzzySets that have a rectangle shape. 
 * The diagram below shows a RectangleFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/RectangleFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only 2 points are required to define
 * the rectangle shape. 
 * <p> This is a specialization of the TrapezoidFuzzySet where the left and right
 * shapes are vertical lines (oneLeftX equals zeroLeftX and
 * oneRightX equals zeroRightX).
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see TrapezoidFuzzySet
 *
 */

public class RectangleFuzzySet extends TrapezoidFuzzySet implements Serializable 
{
    /**
     * Generate a FuzzySet with an rectangle shape. 
     *
     * @param leftX the left x position of the rectangle
     * @param rightX the right x position of the rectangle
     * @exception XValuesOutOfOrderException if the x values are not in correct increasing order
     */

    public RectangleFuzzySet(double leftX, double rightX) throws XValuesOutOfOrderException 
    {
        super(leftX, leftX, rightX, rightX);
    }
}
