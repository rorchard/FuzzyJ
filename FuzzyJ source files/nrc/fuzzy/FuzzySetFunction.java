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

/**
 * An interface that is implemented by classes that will generate
 * FuzzySets between 2 X values. For example the LeftLinearFunction 
 * class will generate a fuzzy set representing a straight line
 * with a membership value of 0 at the left and a membership value
 * of 1 at the right. The RightLinearFunction class is similar with
 * the left value of 1 and right value of zero. The SFunction class
 * generates a FuzzySet in the shape of an 'S' (lower left 0, 
 * upper right 1) and the ZFunction class
 * generates a FuzzySet in the shape of a 'Z' (upper left 1, 
 * lower right 0). In turn the classes LFuzzySet, RFuzzySet and LRFuzzySet
 * use instances of these implementors of the FuzzySetFunction to create
 * pieces of more complex fuzzy set shapes. 
 * 
 * @author Bob Orchard
 *
 * @see LeftLinearFunction
 * @see RightLinearFunction
 * @see SFunction
 * @see ZFunction
 * @see LFuzzySet
 * @see RFuzzySet
 * @see LRFuzzySet
 */

public interface FuzzySetFunction
{
    /**
     * Implements a method that will generate a FuzzySet with x values between
     * a leftmost x value and a rightmost x value.
     * 
     * @param leftX the leftmost X value of the returned FuzzySet.
     * @param rightX the rightmost X value of the returned FuzzySet.
     * @return a FuzzySet with X values between leftX and rightX
     */
    public FuzzySet generateFuzzySet(double leftX, double rightX);
    /**
     * Implements a method that will generate a FuzzySet with x values between
     * a left x and a right x.
     * 
     * @param leftX the leftmost X value of the returned FuzzySet.
     * @param rightX the rightmost X value of the returned FuzzySet.
     * @param numPoints the returned FuzzySet should have numPoints in it
     *        (but this is dependent on the FuzzySetFunction implementation)
     * @return a FuzzySet with X values between leftX and rightX
     */
    public FuzzySet generateFuzzySet(double leftX, double rightX, int numPoints);

}