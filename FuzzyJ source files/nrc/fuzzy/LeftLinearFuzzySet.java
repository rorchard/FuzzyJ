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
 * Used to build specialized LFuzzySets that have a straight line
 * from the 0 value at the lower left edge to the 1 value at the upper right edge. 
 * The diagram below shows a LeftLinearFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/LeftLinearFuzzySet.gif">
 * <br> <br>
 *
 * @author Bob Orchard
 * @author Alexis Eller
 *
 * @see FuzzySet
 * @see LFuzzySet
 * @see LeftLinearFunction
 *
 */
public class LeftLinearFuzzySet extends LFuzzySet  implements Serializable
{    
    private static LeftLinearFunction llf = new LeftLinearFunction();

    /**
     * Generate an LFuzzySet with a straight line from lower left to upper right. 
     *
     * @param leftEdge the start of the straight line at the left with a membership value of 0
     * @param rightEdge the end of the straight line at the right with a membership value of 1
     * @exception XValuesOutOfOrderException if the leftEdge argument is less than the
     *                rightEdge argument
     */

    public LeftLinearFuzzySet(double leftEdge, double rightEdge) throws XValuesOutOfOrderException {
        super(leftEdge, rightEdge, llf);
    }

}


