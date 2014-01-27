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
 * Used to build specialized FuzzySets that have a single value with membership 
 * value of 1.0. Note that such a FuzzySet requires 3 points in it's definition:
 * <br>
 * e.g. (x,0) (x,1) (x,0)
 * <br>
 * Also note that the definition of a FuzzySet with only 1 point defines a 
 * horizontal line at the specifiec membership value and not a singleton value.
 * <br>
 * The diagram below shows a SingletonFuzzySet.
 * <br> <br>
 * <img src = "NetGraphics/SingletonFuzzySet.gif">
 * <br> <br>
 * Note that the Constructors have been defined to make the generation of 
 * such curves as simple as possible. Only 1 point is required to define
 * the singleton shape. 
 * <p> This is a specialization of the TriangleFuzzySet where the left, middle 
 * and right x values are the same.
 *
 * @author Bob Orchard
 *
 * @see FuzzySet
 * @see LRFuzzySet
 * @see TriangleFuzzySet
 *
 */

public class SingletonFuzzySet extends TriangleFuzzySet implements Serializable
{
    /**
     * Generate a FuzzySet with an triangle shape. 
     *
     * @param leftBottom the start of the curve at the left with a membership value of 0
     * @param middleTop the middle point with a membership value of 1
     * @param rightBottom the end of the curve with a membership value of 0
     * @exception XValuesOutOfOrderException if the x values are not in correct increasing order
     */
    public SingletonFuzzySet(double xValue) throws XValuesOutOfOrderException 
    {
        super(xValue, xValue, xValue);
    }

}
