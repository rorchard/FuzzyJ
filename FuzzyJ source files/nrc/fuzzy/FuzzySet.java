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

import java.text.*; 
import java.io.*;

/**
 * A fuzzy set is a mapping of a set of real numbers onto a membership value
 * in the range [0, 1]. In this fuzzy package a fuzzy set is represented by 
 * a set of pairs u/x, where u is the membership value for the real number x.
 * We can represent the set of values as { u1/x1 u2/x2 ... un/xn }. The 
 * x values in the set are in increasing order (x1 < x2 < ... < xn). Values prior to
 * x1 have the same membership value as x1 and values after xn have the same 
 * membership value as xn. Values between xi and x(i+1) are determined by the value that
 * lies on the straight line between the 2 consecutive points. In effect we
 * are representing a graph with straight lines joining the points in the fuzzy set
 * with horizontal lines connecting to the first and last points. Consider the
 * fuzzy set { 0.0/0.3 1.0/0.5 0.0/0.7 }. As shown in the diagram below, this is a 
 * triangular shaped fuzzy set. This is a very compact representation of a
 * fuzzy set. A fuzzy set with a single point, for example { 0.5/25 }, is
 * represents a single horizontal line (in the example a fuzzy set with 
 * membership value of 0.5 for all x values). Note that this is not a single
 * point! A few examples are shown below.
 * <br><br>
 * <table width=640 align=CENTER border=1>
 * <caption><b>A Visual Guide To FuzzySets</b></caption>
 *
 *      <tr>
 *          <th>FuzzySet </th>
 *          <th>Visual Representation</th>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td width=300>Simple triangular FuzzySet:
 *              <ul>
 *              <li>this FuzzySet is represented by the 3 points
 *                  <pre><code>{ 0.0/0.3 1.0/0.5 0.0/0.7 }</code></pre>
 *                  It is a convex FuzzySet.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/basicFuzzyValue.gif">
 *          </td>
 *      </tr>
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td width=300>Slightly more complex FuzzySET:
 *              <ul>
 *              <li>this FuzzySet is represented by the 5 points
 *                  <pre><code>{ 0.0/0.1 1.0/0.3 0.65/0.4 1.0/0.5 0.0/0.8 }</code></pre>
 *                  It is a non-convex FuzzySet.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/normal.gif">
 *          </td>
 *      </tr>
 * </table>
 * <br> <br>
 * To simplify the creation of FuzzySets a hierarchy of subclasses have been defined.
 * The FuzzySets that can be built by this set of subclasses represents a fairly
 * complete set of common shapes. The user is able to generate further subclasses
 * to meet specific needs. The hierarchy is shown below.
 * <br>
 * <img src = "NetGraphics/FuzzySetHierarchy.gif">
 * <br>
 * NOTE: Although the range of allowed membership values is [0, 1], it
 * is possible to form FuzzySets with membership values > 1.0. There are
 * instances where this is desirable. For example in some approaches it is
 * preferred to collect the outputs of fuzzy rules by doing a 'fuzzySum' of the
 * resultant FuzzyValues (global contribution of rules). The fuzzySum operation
 * can result in FuzzySets with membership values > 1.0. When a FuzzySet is created
 * with a FuzzySet constructor (that uses an array of Doubles or SetPoints) the 
 * values are restricted to being between 0 and 1. However, when the fuzzySum 
 * operation or the methods appendSetPoint and insertSetPoint are used to 
 * add points to the FuzzySet, membership values > 1.0 are allowed.
 * 
 * @author Bob Orchard
 *
 * @see LFuzzySet
 * @see RFuzzySet
 * @see LRFuzzySet
 * @see TriangleFuzzySet
 * @see TrapezoidFuzzySet
 * @see SingletonFuzzySet
 * @see RectangleFuzzySet
 * @see PIFuzzySet
 * @see SFuzzySet
 * @see LeftLinearFuzzySet
 * @see ZFuzzySet
 * @see RightLinearFuzzySet
 * @see FuzzyVariable
 * @see FuzzyValue
 */

public class FuzzySet implements java.lang.Cloneable, Serializable
{
    /**
     * A constant used to flag the Union of FuzzySets operation.
     */
    protected static final int UNION = 1;

    /**
     * A constant used to flag the Intersection of FuzzySets operation.
     */
    protected static final int INTERSECTION = 2;

    /**
     * A constant used to flag the Sum of FuzzySets operation.
     */
    protected static final int SUM = 3;

    /**
     * A constant used to flag the maximumOfIntersection operation.
     */
    protected static final int MAXMIN = 4;

    /**
     * A constant used to flag a colinear or parallel result from the
     * <code>lineSegmentIntersection</code> method in class UITools.
     */
    protected static final boolean COLLINEAR_OR_PARALLEL = true;

    /**
     * A constant used to flag a no intersection result from the
     * <code>lineSegmentIntersection</code> method in class UITools.
     */
    protected static final boolean NO_INTERSECTION = false;

    /**
     * A constant specifying the maximum number of initial points in the
     * FuzzySet.
     */
    protected static final int MAX_INITIAL_POINTS = 8;

    /**
     * A constant specifying the amount by which the FuzzySet will be
     * expanded each time an additional point is added and more space
     * is required to accomodate that point.
     */
    protected static final int INCREMENT = 5;

    /**
     * A static variable which determines how many decimal places will
     * be printed by the <code>toString</code> method of FuzzySet.
     * This precision can be set using the <code>setToStringPrecision</code>
     * method.
     */
    protected static int toStringPrecision;

    /**
     * A Union/Intersection tool object which provides methods required only for
     * Union/Intersection method functionality, and also contains variables to
     * keep track of the state of the Union/Intersection method.
     */
    protected transient UITools tool;

    /**
     * This is the heart of the FuzzySet, an array of SetPoints which represent
     * the FuzzySet.
     */
    protected SetPoint[] set;

    /**
     * The <code>numPoints</code> variable keeps track of the number of points
     * contained in <code>set</code>, the array of SetPoints.
     */
    protected int numPoints;
    
    /**
     * BFALSE and BTRUE are static Boolean constants used to avoid
     * generating instances of Booleans over and over.
     */
    protected static final Boolean BFALSE = new Boolean(false);
    protected static final Boolean BTRUE = new Boolean(true);
    
    /** 
     * The <code>simplified</code> variable is a boolean used to indicate whether 
     * or not the FuzzySet has been 'simplied' (revove extra points in the set -- see
     * the simplifiySet method)
     */
    protected boolean simplified;

    /*
     ***************************************************************************************************
     *
     * CONSTRUCTORS FOR FUZZYSET
     *
     ****************************************************************************************************/


    static {
        toStringPrecision = 2;
    }

    /**
     * Constructs an empty FuzzySet with a default maximum number of elements 
     * allocated for the set array. The array will have no
     * SetPoints in it and the size of the FuzzySet will be 0.
     */

    public FuzzySet(){
        numPoints = 0;
        set = new SetPoint[MAX_INITIAL_POINTS];
        tool = null;
        simplified = true; // empty set is always simplified
    }

    /**
     * Constructs an empty FuzzySet with a specified number of elements 
     * allocated for the set array. The array will have no
     * SetPoints in it and the size of the FuzzySet will be 0.
     *
     * @param initMaxSize  the maximum initial size of the set array.
     */

    public FuzzySet(int initMaxSize){
        numPoints = 0;
        set = new SetPoint[initMaxSize];
        tool = null;
        simplified = true; // empty set is always simplified
    }

    /**
     * Constructs a FuzzySet containing the set of points represented
     * by the array of x values and the array of y values passed. The points 
     * of the set will be examined to make sure that the y values (membership 
     * values) lie between 0.0 and 1.0. To create FuzzySets with membership
     * values > 1.0 use appendSetPoint or insertSetPoint. The x values are checked to 
     * make sure they are in proper increasing order.
     *
     * @param x         the double array which represents the x values of the
     *                  points which are to make up the FuzzySet
     * @param y         the double array which represents the y values of the
     *                  points which are to make up the FuzzySet
     * @param numPoints the int that denotes the number of points contained
     *                  in the x/y arrays
     *
     * @exception XValuesOutOfOrderException if the x values in the x[]
     *                  double array are not in strictly ascending order.
     * @exception YValueOutOfRangeException if the y values in the y[]
     *                  double array are not in the strict range [0.0, 1.0].
     */

    public FuzzySet(double[] x, double[] y, int numPoints) throws
        XValuesOutOfOrderException, YValueOutOfRangeException {

		int i;
        this.numPoints = numPoints;
        tool = null;
        set = new SetPoint[numPoints];

        for(i=0; i<numPoints; i++){
            set[i] = new SetPoint(x[i], y[i]);
        }

		i = checkXValueOrder();
		if (i >= 0)
			throw new XValuesOutOfOrderException(set[i].x, set[i+1].x);
		i = checkYValueRange();
		if (i > 0)
			throw new YValueOutOfRangeException(set[i].y);
        simplifySet();
    }

    /**
     * Constructs a FuzzySet containing the set of points represented
     * by the array of SetPoints passed. The points 
     * of the set will be examined to make sure that the y values (membership 
     * values) lie between 0.0 and 1.0. To create FuzzySets with membership
     * values > 1.0 use appendSetPoint or insertSetPoint. The x values are checked to 
     * make sure they are in proper increasing order.
     *
     * @param setPoints the SetPoint array containing the points which
     *                  are to constitute the FuzzySet
     * @param numPoints the number of points in the SetPoint array
     *
     * @exception XValuesOutOfOrderException if the x values in the SetPoints
     *                  in the SetPoint array are not in strictly ascending order.
     * @exception YValueOutOfRangeException if the y values in the SetPoint
     *                  array are not in the strict range [0.0, 1.0].
     */

    public FuzzySet(SetPoint[] setPoints, int numPoints) throws
        XValuesOutOfOrderException, YValueOutOfRangeException {

		int i;
        this.numPoints = numPoints;
        tool = null;

        set = new SetPoint[numPoints];

        for(i=0; i<numPoints; i++){
            set[i] = new SetPoint(setPoints[i]);
        }

        i = checkXValueOrder();
		if (i >= 0)
			throw new XValuesOutOfOrderException(set[i].x, set[i+1].x);
        i = checkYValueRange();
        if (i > 0)
		    throw new YValueOutOfRangeException(set[i].y);
        simplifySet();
    }

    /**
     * Constructs a new FuzzySet which is essentially a copy of the
     * FuzzySet passed as a parameter.
     *
     * @param newSet the FuzzySet which will be copied to create the
     *               new FuzzySet
     */

    public FuzzySet(FuzzySet newSet){
        
        numPoints = newSet.numPoints;
        tool = null;
        set = new SetPoint[numPoints];
        simplified = newSet.isSimplified();

        for(int i=0; i<numPoints; i++)
        {   this.set[i] = new SetPoint(newSet.set[i]);
        }

        simplifySet(); // should normally be simplified .. but in case it's not
    }


    /**
     * Compare this FuzzySet with another for equality.
     * The sets are equal only if they have the same x and y values (within an
     * adjustable tolerance -- see Parameters.FUZZY_TOLERANCE). Note that
     * it is assumed that the sets have been passed through the simplifySet
     * method to reduce the number of points to the minimum required to 
     * represent the set, otherwise sets that are equivalent may not
     * be considered equal.
     *
     * @see nrc.fuzzy.Parameters
     *
     * @return true if they are equal otherwise return false.
     */

    public boolean equals(FuzzySet otherSet)
    {
        if (numPoints != otherSet.numPoints)
           return false;

        for(int i=0; i<numPoints; i++)
        {
            if (!otherSet.set[i].equals(set[i])) 
                return false;
        }

        return true;
    }
     /**
     * Normalize this <code>FuzzySet</code>.
     * Normalization involves identifying the point in the FuzzyValue
     * with the highest membership value and multiplying all the membership values
     * in the FuzzyValue by a scale factor such that this highest point then has
     * a membership value of 1.0.
     *
     * @return a newly constructed FuzzySet containing the normalization of this FuzzySet
     */

    public FuzzySet fuzzyNormalize()
    {
        double maxY = set[0].y;
        FuzzySet normalSet = new FuzzySet(numPoints);
        normalSet.numPoints = numPoints;
        normalSet.simplified = simplified;

        for(int i=0; i<numPoints; i++)
        {
            if (set[i].y > maxY) 
                maxY = set[i].y;
        }
        double scaleFactor = 1.0 / maxY;

        for(int i=0; i<numPoints; i++){
            normalSet.set[i] = new SetPoint(set[i].x, scaleFactor * set[i].y);
        }

        normalSet.simplifySet();  // probably already simplified ... but just in case
        return(normalSet);
    }
    
    /**
     * Scale this <code>FuzzySet</code>.
     *
     * Returns the fuzzy set which is scaled by:
     * <br><pre>
     *    y/maxYValueOfSet   
     * </pre>
     * <br>
     * Effectively this adjusts the set so that the maximum membership 
     * value of the set has the value y and all other membership values
     * are scaled accordingly. If the membership values are all less than y
     * then a copy of the FuzzySet is returned (ie. it scales values down 
     * but never up).
     *
     * @param y scaling factor (must be be from 0.0 to 1.0, values outside
     *          this range will be set to closest value)
     * @return a newly constructed FuzzySet containing the scaled version of this FuzzySet
     */
      
    public FuzzySet fuzzyScale( double yvalue )
    {
        double maxY = set[0].y;
        FuzzySet scaledSet;
        int i;

        if (yvalue > 1.0) yvalue = 1.0;
        if (yvalue <= 0.0 ) 
        {   // return a set with all 0 values
            scaledSet = new FuzzySet(1);
            scaledSet.numPoints = 1;
            scaledSet.set[0] = new SetPoint(set[0].x, 0);
            return scaledSet;
        }

        for (i=1; i<numPoints; i++)
        {   if (set[i].y > maxY) 
                maxY = set[i].y;
        }
                        
		if (maxY <= yvalue)
			return( new FuzzySet(this) );
        
        scaledSet = new FuzzySet(numPoints);
        scaledSet.numPoints = numPoints;
        scaledSet.simplified = simplified;
        
        double scale = yvalue/maxY;

        for (i=0; i<numPoints; i++)
            scaledSet.set[i] = new SetPoint(set[i].x, scale * set[i].y);
            
        scaledSet.simplifySet();  // probably already simplified ... but just in case
        return( scaledSet );
    }



    /**
     * Takes the compliment of this <code>FuzzySet</code>.
     * More specifically, it takes the compliment of the membership (y) values 
     * of the SetPoints of the FuzzySet.
     * <p>
     * Mathematically (NOT), u(x) = 1 - u(x), or y = 1 - y.
     * <p>
     * This assumes that all of the membership values of the fuzzy set
     * are <= 1.0 and >= 0.0. If values lie outside this then values >1.0
     * will be set to 0.0 and those < 0.0 will be set to 1.0
     *
     * @return a newly constructed FuzzySet containing the complement of this FuzzySet
     */

    public FuzzySet fuzzyComplement(){
        FuzzySet complementSet = new FuzzySet(numPoints);
        complementSet.numPoints = numPoints;
        complementSet.simplified = simplified;

        for(int i=0; i<numPoints; i++){
        	double y = set[i].y;
        	if (y < 0.0) y = 0.0;
        	else  if (y > 1.0) y = 1.0;
            complementSet.set[i] = new SetPoint(set[i].x, 1.0 - y);
        }

        complementSet.simplifySet();  // probably already simplified ... but just in case
        return(complementSet);
    }


    /*
     *****************************************************************************************************
     *
     * PUBLIC METHODS WHICH PERFORM/SUPPORT INTERSECTION, UNION, AND MAXIMUM OF INTERSECTION
     *
     ******************************************************************************************************/

    /**
     * Get the UITools object associated with the FuzzySet. If
     * there is none allocate one.
     *
     * @return the UITools object associated with the FuzzySet
     */

    protected UITools getUITools()
    {
        if (tool == null)
	   tool = new UITools();

        return(tool);
    }


    /**
     * Returns the maximum membership value on the intersection of this FuzzySet with the
     * FuzzySet argument. Consider
     * the two diagrams below.  On the right appears a diagram of two sets, one set
     * in black with green SetPoints, and the other set in grey with orange SetPoints.
     * The maximum y value of the intersection is denoted by a red dot on the graph
     * on the right.
     * <p>
     * <table>
     *      <tr>
     *          <td width=320 align=CENTER>
     *              <b>Two Sets</b>
     *          </td>
     *          <td width=40>
     *          </td>
     *          <td width=323 align=CENTER>
     *              <b>Two Sets showing Maximum of Intersection</b>
     *          </td>
     *      </tr>
     *      <tr>
     *          <td width=320>
     *              <img src="NetGraphics/twoSets.gif">
     *          </td>
     *          <td width=40>
     *          </td>
     *          <td width=323>
     *              <img src="NetGraphics/twoSetsMaxMin.gif">
     *          </td>
     *      </tr>
     * </table>
     * <br>
     * 
     * @param otherSet the FuzzySet which is intersected with this FuzzySet to determine
     *                 the maximum y value of intersection
     *
     * @return the double value which represents the maximum y value on the intersection
     *          of the two FuzzySets
     *
     */

    public double maximumOfIntersection(FuzzySet otherSet) {
        return(((Double) fuzzyIntersectionUnion(otherSet, MAXMIN)).doubleValue());
    }

    /**
     * Returns the maximum membership value on the intersection of the two FuzzySet arguments.
     * For a visual depiction of the max min of intersection, please see the
     * documentation for the method <code>maximumOfIntersection(FuzzySet otherSet)</code>.
     *
     * @param setA one of the two FuzzySets
     * @param setB one of the two FuzzySets
     *
     * @return the double value which represents the maximum y value on the
     *         intersection of the two FuzzySets
     *
     */

    public static double maximumOfIntersection(FuzzySet setA, FuzzySet setB) {
        return(setA.maximumOfIntersection(setB));
    }

    /**
     * Returns the intersection of this FuzzySet with the FuzzySet argument.
     * The visual representation of the intersection of two example FuzzySets
     * is depicted below.  One set is black with green SetPoints, and the
     * other set is grey with orange SetPoints.  The diagram on the left is
     * of the two FuzzySets, and the diagram on the right is of the two sets
     * overlaid by the intersection set in red.  Intersection is synonymous
     * with the logical operator <code>AND</code>.
     *
     * <p>
     * <table>
     *      <tr>
     *          <td width=320 align=CENTER>
     *              <b>Two Sets</b>
     *          </td>
     *          <td width=40>
     *          </td>
     *          <td width=323 align=CENTER>
     *              <b>Two Sets showing Intersection Set</b>
     *          </td>
     *      </tr>
     *      <tr>
     *          <td width=320>
     *              <img src="NetGraphics/twoSets.gif">
     *          </td>
     *          <td width=40>
     *          </td>
     *          <td width=323>
     *              <img src="NetGraphics/twoSetsIntersection.gif">
     *          </td>
     *      </tr>
     * </table>
     * <br>
     *
     * @param otherSet the FuzzySet to be intersected with this FuzzySet.
     *
     * @return a new FuzzySet object that represents the intersection set
     *         of the two FuzzySets
     */

    public FuzzySet fuzzyIntersection(FuzzySet otherSet) {
        return((FuzzySet)fuzzyIntersectionUnion(otherSet, INTERSECTION));
    }

    /**
     * Returns the intersection set of the two FuzzySet arguments.
     * For a visual depiction of the intersection set and more information, please see the
     * documentation for the method <code>fuzzyIntersection(FuzzySet otherSet)</code>.
     *
     * @param setA one of the two FuzzySets
     * @param setB one of the two FuzzySets
     *
     * @return the FuzzySet intersection set of the two FuzzySets
     *
     */

    public static FuzzySet fuzzyIntersection(FuzzySet setA, FuzzySet setB) {
        return((FuzzySet)setA.fuzzyIntersection(setB));
    }

	/**
	 * Returns the union of this FuzzySet with the FuzzySet argument.
	 * The visual representation of the union of two example FuzzySets
	 * is depicted below.  One set is black with green SetPoints, and the
	 * other set is grey with orange SetPoints.  The diagram on the left is
	 * of the two FuzzySets, and the diagram on the right is of the two sets
	 * overlaid by the union set in red.  Union is synonymous
	 * with the logical operator <code>OR</code>.
	 *
	 * <p>
	 * <table>
	 *      <tr>
	 *          <td width=320 align=CENTER>
	 *              <b>Two Sets</b>
	 *          </td>
	 *          <td width=40>
	 *          </td>
	 *          <td width=323 align=CENTER>
	 *              <b>Two Sets showing Union Set</b>
	 *          </td>
	 *      </tr>
	 *      <tr>
	 *          <td width=320>
	 *              <img src="NetGraphics/twoSets.gif">
	 *          </td>
	 *          <td width=40>
	 *          </td>
	 *          <td width=323>
	 *              <img src="NetGraphics/twoSetsUnion.gif">
	 *          </td>
	 *      </tr>
	 * </table>
	 * <br>
	 *
	 * @param otherSet the FuzzySet to be unionized with this FuzzySet.
	 *
	 * @return a new FuzzySet object that represents the union set
	 *         of the two FuzzySets
	 */

	public FuzzySet fuzzyUnion(FuzzySet otherSet) {
		return((FuzzySet)fuzzyIntersectionUnion(otherSet, UNION));
	}

	/**
	 * Returns the union set of the two FuzzySet arguments.
	 * For a visual depiction of the union set and more information, please see the
	 * documentation for the method <code>fuzzyUnion(FuzzySet otherSet)</code>.
	 *
	 * @param setA one of the two FuzzySets
	 * @param setB one of the two FuzzySets
	 *
	 * @return the FuzzySet union set of the two FuzzySets
	 *
	 */

	public static FuzzySet fuzzyUnion(FuzzySet setA, FuzzySet setB) 
	{
		return((FuzzySet)setA.fuzzyUnion(setB));
	}


	/**
	 * Returns the sum of this FuzzySet with the FuzzySet argument.
	 * The sum of 2 fuzzy sets is a set where the membership (y) value at every x 
	 * position is the sum of the memberships values of the two sets
	 * at the corresponding x values. 
	 * <p>
	 * NOTE WELL: The sum can lead to FuzzySets with membership values 
	 * greater than 1.0. This is sometimes used to collect 
	 * output of fuzzy rules (global contribution) rather than doing the union
	 * of the outputs.
	 *  
	 *
	 *
	 * @param otherSet the FuzzySet to be summed with this FuzzySet.
	 *
	 * @return a new FuzzySet object that represents the sum
	 *         of the two FuzzySets
	 */

	public FuzzySet fuzzySum(FuzzySet otherSet) {
		return((FuzzySet)fuzzyIntersectionUnion(otherSet, SUM));
	}

	/**
	 * Returns the sum of the two FuzzySet arguments.
	 * The sum of 2 fuzzy sets is a set where the membership (y) value at every x 
	 * position is the sum of the memberships values of the two sets
	 * at the corresponding x values. 
	 * <p>
	 * NOTE WELL: The sum can lead to FuzzySets with membership values 
	 * greater than 1.0. This is sometimes used to collect 
	 * output of fuzzy rules (global contribution) rather than doing the union
	 * of the outputs.
	 *
	 * @param setA one of the two FuzzySets
	 * @param setB one of the two FuzzySets
	 *
	 * @return the FuzzySet sum of the two FuzzySets
	 *
	 */

	public static FuzzySet fuzzySum(FuzzySet setA, FuzzySet setB) 
	{
		return((FuzzySet)setA.fuzzySum(setB));
	}

    /**
     * Returns the intersection set, the union set, the sum set or the maximum of intersection
     * of this FuzzySet with the FuzzySet argument, depending on the operation
     * specified by the <code>op</code> argument.  The value of the <code>op</code>
     * argument can be one of the following FuzzySet constants: UNION, INTERSECTION,
     * SUM, or MAXMIN.
     *
     * @param otherSet the other FuzzySet with which to perform the specified
     *                 operation with this FuzzySet
     * @param op      the int which specifies whether intersection, union, sum
     *                 or max min of intersection is to be performed
     *
     * @return         an Object which can either be an <code>instanceof</code>
     *                 Double or FuzzySet.  If the Object is an <code>instanceof</code>
     *                 Double, then that Double object contains the value of the maximum
     *                 of intersection.  If the Object is an <code>instanceof</code>
     *                 FuzzySet, then that FuzzySet object contains either an
     *                 intersection, union or sum set; any method that calls this method
     *                 is expecting one or the other.
     */

    protected Object fuzzyIntersectionUnion(FuzzySet otherSet, int op) 
    {
        Object intersection;
        SetPoint intersectPoint;
        SetPoint point;
        boolean intersectFlag;
        double max;
        UITools a;
        UITools b;
        FuzzySet resultSet;

        // If the FuzzySets are the same object then simple calculations possible
        if (this == otherSet)
        {
          if (op == MAXMIN) // use max of the membership values
             return( new Double(getMaxY()) );
             
          // if sum of 2 identical sets then each membership value is simply doubled
          if (op == SUM)
          {  resultSet = new FuzzySet(numPoints);
             resultSet.simplified = simplified; // same as for the existing set
          	 for (int i = 0; i < numPoints; i++)
          	 {  point = this.getPoint(i);
          	 	resultSet.appendSetPoint(point.x, point.y+point.y);
          	 }  
          	 resultSet.simplifySet(); // probably not required but ...
          	 return(resultSet); 
          }

          // if Union or Intersection then we just return a copy of the FuzzySet
          return( new FuzzySet(this) );
        }
        
        // If both are of size 1 (both just horizontal lines) then the result 
        // will just be the higher line for Union or the lower line for 
        // Intersection or the sum of the 2 lines for Sum or the membership
        // value of the lower line for maximum of intersection
        if(this.numPoints == 1 && otherSet.numPoints == 1)
        {   if(op == MAXMIN) 
            {
                return( new Double((this.set[0].y > otherSet.set[0].y) ? otherSet.set[0].y : this.set[0].y) );
            } 
            else if (op == SUM)
            {   resultSet = new FuzzySet(1);
            	resultSet.appendSetPoint(this.set[0].x, this.set[0].y+otherSet.set[0].y);
            	resultSet.simplified = true; // only 1 point;
            	return(resultSet);
            }
            else
            {   if(this.set[0].y < otherSet.set[0].y)
                {   if(op == UNION) resultSet = new FuzzySet(otherSet);
                    else            resultSet = new FuzzySet(this);
                } 
                else 
                {   if(op == UNION) resultSet = new FuzzySet(this);
                    else            resultSet = new FuzzySet(otherSet);
                }
        	    resultSet.simplified = true; // only 1 point;
                return(resultSet);
            }
        }
        
        // will hold the maximum y value as required
        max=0; 
        
        // If only one of the fuzzy sets is a horizontal line then do a simpler 
        // horizontal intersection or union; if the operation is SUM then just add
        // the y value of the horizontal set to each y value in the other set        
        if(this.numPoints == 1 || otherSet.numPoints == 1)
        {   FuzzySet onePointSet;
        	FuzzySet multiPointSet;
        	if (this.numPoints == 1)
        	{   onePointSet = this;
        		multiPointSet = otherSet;
        	}
        	else
        	{   onePointSet = otherSet;
        		multiPointSet = this;
        	}
        	if(op == MAXMIN) 
            {   for(int i=0; i<multiPointSet.numPoints; i++)
                   if(max < multiPointSet.set[i].y) max = multiPointSet.set[i].y;
                return(new Double((max < onePointSet.set[0].y) ? max : onePointSet.set[0].y));
            } 
            else if (op == SUM)
            {   int npoints = multiPointSet.numPoints;
            	resultSet = new FuzzySet(npoints);
            	resultSet.simplified = multiPointSet.simplified;
            	for (int i= 0; i < npoints; i++)
            	   resultSet.appendSetPoint(multiPointSet.set[i].x, onePointSet.set[0].y+multiPointSet.set[i].y);
        	    resultSet.simplifySet();
            	return(resultSet);
            }
            else 
            {   if(op == UNION) resultSet = multiPointSet.horizontalUnion(onePointSet.set[0].y);
                else            resultSet = multiPointSet.horizontalIntersection(onePointSet.set[0].y);
                return(resultSet); //horizontalUnion etc do a simplify
            }
        }
        
        // initialze an empty fuzzy set for the resultant set to be stored into
        // if not doing the max of intersection operation (MAXMIN)
        resultSet = new FuzzySet();

        // If there is definitely NO overlap between the 2 fuzzy sets then
        // a simple result can be calculated; for SUM it is the same as the UNION
        if( noIntersectionTest(otherSet) )
        {   if(op == MAXMIN)
               return(new Double(0.0));
            else if(op == UNION || op == SUM) 
               resultSet = concat(this, otherSet);
            else 
            {  resultSet.insertSetPoint(this.set[0].x, 0.0);
               resultSet.simplified = true;
            }
               
            // resultSet.simplifySet(); not required ... 1 pt or concat simplifies
            return(resultSet);
        }

        a = this.getUITools();
        b = otherSet.getUITools();

        // Set the starting points (previous and current) for each fuzzy set. We make sure
        // that they each have a start point (previous) with the same x value.

        a.initializeStartingPoint(otherSet.set[0], otherSet.set[otherSet.numPoints-1]);
        b.initializeStartingPoint(this.set[0], this.set[this.numPoints-1]);

        // now that we know the first point, let's store it OR record the
        // maximum y value before going on.
        if (op != MAXMIN)
            if (op == SUM)
               resultSet.insertSetPoint(a.previous.x, a.previous.y+b.previous.y);
            else 
               if (a.previous.y <= b.previous.y) resultSet.insertSetPoint((op == UNION) ? b.previous : a.previous);
               else                              resultSet.insertSetPoint((op == UNION) ? a.previous : b.previous);
        else
        {   max = a.previous.y;
            if (max > b.previous.y) max = b.previous.y;
        }
        
        // find all of the other points of intersection or union or find Maximum of intersection
        while (!a.endOfSet() || !b.endOfSet())
        {
            if (a.previous.equals(b.previous))
            {   // same 1st points of line segments
                if (a.current.x == b.current.x) 
                {   // last points of line segments end at same x values
                    point = a.handleType1(b, op);
                    if (op == MAXMIN) 
                    {   if (max < point.y) max = point.y;
                    }
                    else 
                       resultSet.appendSetPoint(point);
                } 
                else 
                {   // last points of line segments end at different x values
                    point = a.handleType2(b, op);
                    if (point != null) 
                    {   if (op == MAXMIN) 
                        {   if (max < point.y) max = point.y;
                        }
                        else 
                            resultSet.appendSetPoint(point);
                    }
                }
            } 
            else 
            {   // different 1st points of line segments
                if(a.current.equals(b.current)) 
                {   // same last points of line segments
                    point = a.handleType3(b, op);
                    if (op == MAXMIN) 
                    {   if (max < point.y) max = point.y;
                    }
                    else 
                        resultSet.appendSetPoint(point);
                } 
                else 
                {   // different last points of line segments -- use intersection of line segments
                    intersection = a.lineSegmentIntersection(b);
                    intersectFlag  = (intersection instanceof Boolean) ? ((Boolean)intersection).booleanValue() : false;
                    intersectPoint = (intersection instanceof SetPoint) ? (SetPoint)intersection : null;

                    if (intersectPoint != null)
                    {   // there is intersection of line segments -- store the intersect point
                        point = a.handleType4(b, intersectPoint, op);
                        if (op == MAXMIN) 
                        {   if (max < intersectPoint.y) max = intersectPoint.y;
                        }
                        else
                            resultSet.appendSetPoint(point);
                    } 
                    else 
                    {   // no intersection of line segments
                        if (a.current.x == b.current.x)
                        {
                            if (intersectFlag == COLLINEAR_OR_PARALLEL && 
                                (op == INTERSECTION || op == MAXMIN || op == SUM))
                            {   // looking for special case where the line segments are both vertical, they 'point'
                            	// in different 'directions' and there is an overlap of the 2 line segments
                            	double overlapY = findMaxYOverlapValue(a.previous, a.current, b.previous, b.current);
                                if (overlapY > 0.0) 
                                {   if (op == MAXMIN)
                                    {  if (overlapY > max) max = overlapY;
                                    }
                                    else if (op == INTERSECTION)
                                        resultSet.appendSetPoint(a.current.x, overlapY);
									else // op is SUM
									{   double maxY1 = Math.max(a.current.y, a.previous.y);
										double maxY2 = Math.max(b.current.y, b.previous.y);
										resultSet.appendSetPoint(a.current.x, maxY1 + maxY2);
									}
                                }
                            }

                            point = a.handleType5(b, op);
                            if (op == MAXMIN)
                            {   if (max < point.y) max = point.y;
                            }
                            else
                                resultSet.insertSetPoint(point);
                        } 
                        else if (a.current.x < b.current.x)
                        {
                            point = a.handleType6(b, op);
                            if (point != null) 
                            {
                                if (op == MAXMIN)
                                {  if (max < point.y) max = point.y;
                                }
                                else
                                    resultSet.appendSetPoint(point);
                            }
                        } 
                        else 
                        {   //a.current.x > b.current.x
                            point = b.handleType6(a, op);
                            if(point != null) 
                            {
                                if(op == MAXMIN)
                                {  if (max < point.y) max = point.y;
                                }
                                else
                                    resultSet.appendSetPoint(point);
                            }
                        }
                    } // end of if(intersectPoint != null)
                } // end of if(a.current.equals(b.current))
            } //end of if (a.previous.equals(b.previous))
        } //end of while(!a.endOfSet() || !b.endOfSet()

        if(op == MAXMIN) return(new Double(max));

        a.addFinalPoint(b, resultSet, op);
        resultSet.simplifySet();
        return(resultSet);
    }


    /*
     *****************************************************************************************************
     *
     * UNION AND INTERSECTION TOOLS
     *
     ******************************************************************************************************/


    /**
     * The <code>UITools</code> class was created to support the Fuzzy Union, Intersection
     * and Sum methods.  The method <code>fuzzyIntersectionUnion</code>, the only method that
     * truly does any union/intersection type calculations, requires that certain
     * information be stored about each FuzzySet.  Each UITools object is relevant
     * only to the FuzzySet that contains it.
     *
     * <p>For example, the fuzzyIntersectionUnion algorithm works with the concept of
     * comparing line segments; therefore, it is important to store two points for each FuzzySet
     * to define the current line segment for that FuzzySet.
     * <br>
     * <img src="NetGraphics/UIToolsDocumentation.gif">
     *
     * <p>As shown above, the two points stored to define the current line segment
     * are the UITools variables <code>previous</code> and <code>current</code>.
     * The <code>previous</code> point always has a smaller x value than the
     * <code>current</code> point.
     *
     * <p>Because each line segment of the FuzzySet is considered in sequence, it is
     * also important to store the index of the <code>current</code> point in the
     * FuzzySet; in this manner, the UITools object can keep track of where it is in the
     * FuzzySet and can advance to the next line segment.
     *
     * <p>The UITools object also contains methods that have relevance only in terms of
     * the fuzzyIntersectionUnion method.  For example, testing whether the end of the
     * FuzzySet has been reached, calculating the slope of the current line segment
     * (showed in the above diagram), finding the intersection point of two line
     * segments, and other such methods.
     *
     * <p>Basically, the UITools class bundles together all the essential variables
     * and methods for the purpose of Intersection and Union that do not belong
     * as a direct part of the FuzzySet.
     */

    protected class UITools {

        /**
         * One of the two points required to define the current line segment
         * being analyzed.  This point will always have a smaller x value than
         * the <code>current</code> point.
         */
        protected SetPoint previous;

        /**
         * One of the two points required to define the current line segment
         * being analyzed.  This point will always have a smaller x value than
         * the <code>current</code> point.
         */
        protected SetPoint current;

        /**
         * The maximum x value of any point in either this FuzzySet, or the other
         * FuzzySet in question.
         */
        protected double maxX;

        /**
         * Stores the slope of the current line segment after the
         * <code>calculateSlope</code> method is called.
         */

        //IMPORTANT: note that this variable DOES NOT always contain the
        //current slope.  calculateSlope MUST be called to update the variable
        //before it is refered to.
        private double slope;

        /**
         * The index of the <code>current</code> point in the FuzzySet to
         * which this UITools object belongs.
         */
        protected int index;

        /**
         * Constructs a new UITools object.
         */

        protected UITools()
        {
            previous = null;
            current = null;
            index = -1;
        }

        /**
         * Returns true if the end of the FuzzySet has been reached.
         *
         * @return <code>true</code> if the last point of this FuzzySet
         *         has been reached
         */

        protected boolean endOfSet()
        {
            return(index >= numPoints);
        }

        /**
         * Assigns the value of <code>current</code>, the current point, to
         * <code>previous</code>, the previous point, and assigns the value
         * of the next point in the FuzzySet to <code>current</code>.  In
         * essence, this method moves the UITool onto the next line
         * segment in the FuzzySet.
         */

        protected void moveAheadOnePoint() 
        {
            index++;

            previous = current;

            if(index < numPoints) 
               current = set[index];
            else
               // already at last point in the set
               current = new SetPoint(maxX, current.y);
        }

        /**
         * Initializes the previous point, the current point, the index
         * to keep track of what point we're on in the FuzzySet, and
         * maxX, the maximum x value in either of the FuzzySets in question.
         *
         * @param bFirstPoint the first SetPoint in the other FuzzySet
         * @param bLastPoint  the last SetPoint in the other FuzzySet
         */

        protected void initializeStartingPoint(SetPoint bFirstPoint, SetPoint bLastPoint) 
        {
            previous = new SetPoint();
            previous.y   = set[0].y;

            if(set[0].x <= bFirstPoint.x)
            {   previous.x = set[0].x;
                index = 1;
            } 
            else 
            {  //set[0].x > bFirstPoint.x
                previous.x = bFirstPoint.x;
                index = 0;
            }

            current = new SetPoint(set[index].x, set[index].y);

            maxX = ( set[numPoints-1].x >= bLastPoint.x ) ? set[numPoints-1].x : bLastPoint.x;
        }

        /**
         * Caluculates the slope of the line defined by <code>previous</code>, the
         * previous point, and <code>current</code>, the current point.
         */

        protected void calculateSlope()
        {
            if (previous.x == current.x) slope = (current.y > previous.y) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            else                         slope = (current.y - previous.y)/(current.x - previous.x);
        }


        /**
         * Tests to see if two line segments, represented by the previous and
         * current points of this UITool, and the UITool argument, intersect.
         * If the line segments intersect, the point of intersection is returned.
         * If the line segments do not intersect, a boolean value is returned to
         * indicate whether the line segments are collinear, or simply do not
         * intersect.
         *
         * @param b the UITools object which provides the other line segment
         *          (previous and current point) to test against the line
         *          segment represented by this previous and current point
         *
         * @return a SetPoint object representing the intersection point, if
         *         the line segments intersect.  If the line segments do not
         *         intersect, a Boolean object with a value of <code>true</code>
         *         is returned if the lines are collinear, and a Boolean object
         *         with a value of <code>false</code> is returned if the lines
         *         simply do not intersect.
         */

        /*
         * Let A = this.previous, B = this.current
         * Let C = b.previous, D = b.current
         * ----------------------------------------------
         * Assume that Ax < Bx, Cx < Dx, A != B, C != D
         *
         * AB and CD can be given by:
         *
         * AB = A+r(B-A), r in [0,1]
         * CD = C+s(D-c), s in [0,1]
         *
         * If AB & CD intersect, then
         *
         * A+r(B-A) = C+s(D-C), or
         *
         * XA+r(XB-XA) = XC+s(XD-XC)
         * YA+r(YB-YA) = YC+s(YD-YC)  FOR SOME s,r IN [0,1]
         *
         * Solving for r and s gives:
         *
         *        (YA-YC) (XD-XC) - (XA-XC) (YD-YC)
         *  r = ------------------------------------   eqn 1
         *        (XB-XA) (YD-YC) - (YB-YA) (XD-XC)
         *
         *       (YA-YC) (XB-XA) - (XA-XC) (YB-YA)
         * S = ------------------------------------   eqn 2
         *       (XB-XA) (YD-YC) - (YB-YA) (XD-XC)
         *
         * Then intersection is
         *
         *   I = A+r(B-A), or
         *
         *   XI = XA+r(XB-XA)
         *   YI = YA+r(YB-YA)
         *
         * If 0<=r<=1 and 0<=s<=1 the segments intersect
         * If r<0 or r>1 or s<0 or s>1 then no intersection of segments
         *
         * If denominator in eqn 1 (same as denom in eqn 2) is 0, AB & CB parallel
         * If numerator of eqn 1 is also 0, AB and CD are collinear or
         * If numerator of eqn 2 is also 0, AB and CD are collinear
         *
         * Note: demoninator is 0 basically means that AB and CD have same slope
         * numerator of eqn1 is 0 when CA and CD have same slope
         * numerator of eqn2 is 0 when CA and AB have same slope
         */

        protected Object lineSegmentIntersection(UITools b) 
        {
            double denominator;
            double numerator1, numerator2;
            double r, s;
            double prevX, prevY, bPrevX, bPrevY;
            
            prevX = previous.x;
            prevY = previous.y;
            bPrevX = b.previous.x;
            bPrevY = b.previous.y;

            double aRise = current.y - prevY;
            double aRun  = current.x - prevX;
            double bRise = b.current.y - bPrevY;
            double bRun  = b.current.x - bPrevX;

            double abRise = previous.y - bPrevY;
            double abRun  = previous.x - bPrevX;

            denominator = (aRun)*(bRise) - (aRise)*(bRun);
            numerator1 =  (abRise)*(bRun) - (abRun)*(bRise);

            /* Sometimes we get into trouble when lines are very close to being
            parallel and the limits of the fl. pt. precision take over so we
            chose to make a simple test for close to 0 denominator.
            */
            if (Math.abs(denominator) < 1.0e-12)
                if (numerator1 < 1.0e-12) return(BTRUE);  /* on same lines - collinear */
                else                      return(BFALSE);  /* no intersection -- parallel lines */

            r = numerator1/denominator;
            if (r<0 || r>1) return(BFALSE);    /* no intersection of segments */

            numerator2 =  (abRise)*(aRun) - (abRun)*(aRise);
            s = numerator2/denominator;
            if (s<0 || s>1) return(BFALSE);    /* no intersection of segments */

            /* We can expect vertical lines or horizontal lines.
                In that case avoid a mult and subtract and add and likely
                some rounding errors by checking if x vals (y vals) equal for
                either line segment. Note we could do other special checks for
                speed considerations BUT this has relevance in this application
                since horizontal lines appear often.
            */

            SetPoint result = new SetPoint();

            if      (aRun == 0.0) result.x = prevX;
            else if (bRun == 0.0) result.x = bPrevX;
            else                  result.x = prevX + r*(aRun);


            if      (bPrevY == b.current.y) result.y = bPrevY;
            else if (current.y == prevY)    result.y = prevY;
            else                            result.y = prevY + r*(aRise);

            return(result);  /* intersection obtained */

        }

        /*
         *****************************************************************************************************
         *
         * GRAPH HANDLERS AND OTHER METHODS CALLED BY INTERSECTION, UNION, SUM AND MAXMININTERSECTION
         *
         ******************************************************************************************************/

        /**
         * Returns the next point that is to be stored in the Intersection, Union or Sum set.
         * The conditions which <bold>must</bold> be affirmed before calling this method
         * are as follows:
         * <ul>
         * <li>that the previous point of this UITools <b>is equal to</b> the previous point
         *     of the UITools argument
         * <li>that the x value of the current point of this UITools is the same as
         *     the x value of the current point of the UITools argument
         * </ul>
         * <p>For a visual depiction of this situation, see the graph below.
         * <br>
         * <img src = "NetGraphics/type1.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union
         *                  or Intersection set
         */

        protected SetPoint handleType1(UITools b, int operation) 
        {
            SetPoint pointToStore;

            if (operation == SUM)
                pointToStore = new SetPoint(this.current.x, this.current.y+b.current.y);
            else if (b.current.y > this.current.y)
                pointToStore = (operation == UNION) ? b.current : this.current;
            else
                pointToStore = (operation == UNION) ? this.current : b.current;

            if (!this.endOfSet()) this.moveAheadOnePoint();
            if (!b.endOfSet()) b.moveAheadOnePoint();

            return(pointToStore);
        }

        /**
         * Returns the next point to be stored in the Fuzzy Intersection, Union or Sum Set, or
         * returns <code>null</code> if there is no point to be stored.
         * The condition which <bold>must</bold> be affirmed before calling this method
         * is as follows:
         * <ul>
         * <li>that the previous point of this UITools is the same as the previous point
         *     of the UITools argument (the x values of the current points are not equal)
         * </ul>
         * <p>For a visual depiction of this situation, see the graph below.
         * <br>
         * <img src = "NetGraphics/type2.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union,
         *                  Intersection or Sum set, or <code>null</code> if no such point exists
         *                  and the handler simply advanced the UITools to the next line segment(s)
         */

        protected SetPoint handleType2(UITools b, int operation) 
        {
            SetPoint pointToStore;

            this.calculateSlope();
            b.calculateSlope();

            if (this.current.x < b.current.x)
            {   if (operation == SUM)
            	{   double yAtX = b.slope*(this.current.x-this.previous.x)+this.previous.y;
            	    pointToStore = new SetPoint(this.current.x, this.current.y + yAtX);
            	}
                else if ((this.slope <= b.slope && (operation == INTERSECTION || operation == MAXMIN)) ||
                    (this.slope >= b.slope && operation == UNION))
                    pointToStore = this.current;
                else 
                    pointToStore = null;

                if(this.slope == b.slope) b.previous = this.current;

                this.moveAheadOnePoint();
            }
            else 
            {   //b.current.x < this.current.x
                if (operation == SUM)
            	{   double yAtX = this.slope*(b.current.x-this.previous.x)+this.previous.y;
            	    pointToStore = new SetPoint(b.current.x, b.current.y + yAtX);
            	}
                else if ((b.slope <= this.slope && (operation == INTERSECTION || operation == MAXMIN)) ||
                    (b.slope >= this.slope && operation == UNION))
                     pointToStore = b.current;
                else pointToStore = null;

                if(this.slope == b.slope) this.previous = b.current;

                b.moveAheadOnePoint();
            }

            return(pointToStore);
        }

        /**
         * Returns the next point to be stored in the Intersection, Union, or Sum Set.
         * The conditions which <bold>must</bold> be affirmed before calling this method
         * are as follows:
         * <ul>
         * <li>that the previous point of this UITools <b>is not equal to</b> the previous point
         *     of the UITools argument
         * <li>that the current point of this UITools <b>is equal to</b> the current point
         *     of the UITools argument
         * </ul>
         * <p>For a visual depiction of this situation, see the graph below.
         * <br>
         * <img src = "NetGraphics/type3.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union,
         *                  Intersection or Sum set
         */

        protected SetPoint handleType3(UITools b, int operation) 
        {
            SetPoint pointToStore;

            if (operation == SUM)
               pointToStore = new SetPoint(this.current.x, this.current.y+this.current.y);
            else
               pointToStore = this.current;

            this.moveAheadOnePoint();
            b.moveAheadOnePoint();

            return(pointToStore);
        }

        /**
         * Returns the next point to be stored in the Intersection, Union or Sum Set.
         * The conditions which <bold>must</bold> be affirmed before calling this method
         * are as follows:
         * <ul>
         * <li>that the previous point of this UITools <b>is not equal to</b> the previous point
         *     of the UITools argument
         * <li>that the current point of this UITools <b>is not equal to</b> the current point
         *     of the UITools argument
         * <li>that an intersection exists between the line segments represented by the
         *     previous and current points in this UITools object and the UITools argument
         * </ul>
         * <p>For a visual depiction of this situation, see the graph below.
         * <br>
         *
         * <img src = "NetGraphics/type4.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param intersect the SetPoint representing the intersection point of the two
         *                  line segments
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union
         *                  or Intersection set
         */

        protected SetPoint handleType4(UITools b, SetPoint intersect, int operation) 
        {
            SetPoint pointToStore;

            this.previous = intersect;
            b.previous = intersect;

            if (operation == SUM)
               pointToStore = new SetPoint(intersect.x, intersect.y+intersect.y);
            else 
               pointToStore = intersect;
               
            if (this.current.x == intersect.x && this.current.y == intersect.y) this.moveAheadOnePoint();
            if (b.current.x == intersect.x && b.current.y == intersect.y) b.moveAheadOnePoint();
            
            return(pointToStore);
        }

        /**
         * Returns the next point to be stored in the Intersection or Union Set.
         * The conditions which <bold>must</bold> be affirmed before calling this method
         * are as follows:
         * <ul>
         * <li>that the previous point of this UITools <b>is not equal to</b> the previous point
         *     of the UITools argument
         * <li>that the current point of this UITools <b>is not equal to</b> the current point
         *     of the UITools argument
         * <li>that the x value of the current point of this UITools <b>is equal to</b>
         *     the x value of the current point of the UITools argument
         * <li>that an intersection <b>does not exist</b> between the line segments represented by the
         *     previous and current points in this UITools object and the UITools argument
         * </ul>
         * <p>For a visual depiction of this situation, see the 1st situation in the graph below.
         * <br>
         *
         * <img src = "NetGraphics/type5.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union
         *                  or Intersection set
         */

        protected SetPoint handleType5(UITools b, int operation) 
        {
            SetPoint pointToStore;
            
            if (operation == SUM)
                pointToStore = new SetPoint(this.current.x, this.current.y+b.current.y);
            else if(this.current.y > b.current.y)
                pointToStore = (operation == UNION) ? this.current : b.current;
            else
                pointToStore = (operation == UNION) ? b.current : this.current;

            this.moveAheadOnePoint();
            b.moveAheadOnePoint();

            return(pointToStore);
        }

        /**
         * Returns the next point to be stored in the Intersection, Union, or Sum Set, or
         * returns <code>null</code> if there is no point to be stored.  Note that the
         * third stipulation for the calling of this method is that that the x value
         * of the current point of this UITools <b>is less than</b>
         * the x value of the current point of the UITools argument.  Therefore, the
         * manner in which this is called, ie. either <code>a.handleType6(b)</code> or
         * <code>b.handleType6(a)</code>, is the result of a test in the
         * fuzzyIntersectionUnion method.
         *
         * <p>The conditions which <bold>must</bold> be affirmed before calling this method
         * is as follows:
         * <ul>
         * <li>that the previous point of this UITools <b>is not equal to</b> the previous point
         *     of the UITools argument
         * <li>that the current point of this UITools <b>is not equal to</b> the current point
         *     of the UITools argument
         * <li>that the x value of the current point of this UITools <b>is less than</b>
         *     the x value of the current point of the UITools argument
         * <li>that an intersection <b>does not exist</b> between the line segments represented by the
         *     previous and current points in this UITools object and the UITools argument
         * </ul>
         * <p>For a visual depiction of this situation, see the graph below.
         * <br>
         * <img src = "NetGraphics/type6.gif">
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION, INTERSECTION, SUM
         *                  and MAXMIN.  In all of the UITools handlers, INTERSECTION
         *                  and MAXMIN are treated in the same manner.
         *
         * @return          the SetPoint that is the next point to be stored in the Union
         *                  or Intersection set, or <code>null</code> if no such point exists
         *                  and the handler simply advanced the UITools to the next line segment(s)
         */

        protected SetPoint handleType6(UITools b, int operation) 
        {
            SetPoint pointToStore;

            b.calculateSlope();

            if (operation == SUM)
            {   double yAtX = b.slope*(this.current.x-b.previous.x)+b.previous.y;
            	pointToStore = new SetPoint(this.current.x, this.current.y+yAtX);
            }
            else
            {   double slopeAB = (this.current.y - b.previous.y)/(this.current.x - b.previous.x);

                if ((slopeAB <= b.slope && (operation == INTERSECTION || operation == MAXMIN)) ||
                    (slopeAB >= b.slope && operation == UNION)
                )
                    pointToStore = this.current;
                else 
                    pointToStore = null;

                if (slopeAB == b.slope) b.previous = this.current;
            }
            this.moveAheadOnePoint();

            return(pointToStore);
        }

        /**
         * Adds the final point to the Union, Intersection or Sum set.
         *
         * @param b         the other UITools object through which to access any necessary
         *                  information such as previous and current points
         * @param fuzzySet  the FuzzySet that the final point is to be added to.  This is
         *                  either a Union set or an Intersection set; the method
         *                  determines which from the <code>operation</code> parameter.
         * @param operation the integer specifying the operation being performed.  The values possible
         *                  for this parameter are the constants UNION and INTERSECTION.
         */

        protected void addFinalPoint(UITools b, FuzzySet fuzzySet, int operation)
        {
            double x;
            double y;
            
            if (operation == SUM)
            {   x = (this.current.x >= b.current.x) ? this.current.x : b.current.x;
            	y = this.current.y + b.current.y;
            }
            else if (this.current.y < b.current.y)
            {
                x = (operation == UNION) ? b.current.x : this.current.x;
                y = (operation == UNION) ? b.current.y : this.current.y;
            } 
            else 
            {
                x = (operation == UNION) ? this.current.x : b.current.x;
                y = (operation == UNION) ? this.current.y : b.current.y;
            }

            fuzzySet.appendSetPoint(x, y);
        }
    }


    /*
     *****************************************************************************************************
     *
     * PUBLIC METHODS THAT RETURN FUZZYSETS (OTHER THAN INTERSECTION AND UNION)
     *
     ******************************************************************************************************/

    /**
     * Returns a copy of this instance of FuzzySet.
     *
     * @return a copy of this FuzzySet
     *
     * @exception CloneNotSupportedException if the object to be cloned does not implement
     *                                     the Cloneable Interface.
     */

    public FuzzySet copyFuzzySet() 
        throws CloneNotSupportedException 
    {
        return( (FuzzySet)this.clone() );
    }

    /**
     * Simplifies the array of points by removing any extraneous points
     * from the FuzzySet.  If the following cases
     * occur, the method takes the described actions:
     * <ul>
     * <li>Case 1: two of the same points exist in the FuzzySet
     * <br>Result: one of the points is removed
     *
     * <li>Case 2: there are three points in a vertical line, of which all have the
     *             same direction (either increasing or decreasing)
     * <br>Result: the point between the two points with extreme y values is removed
     *
     * <li>Case 3: there are three points in a row with the same y value
     * <br>Result: the middle point is removed
     *
     * <li>Case 4: the first two points or the last two points of the set have the same
     *             y value
     * <br>Result: the first or last point, respectively, is removed
     * </ul>
     */

    public void simplifySet()
    {   
    	if (simplified) return; // if already simplified nothing to do
    	
        //Case 1:  if two of the same point exist, remove one
        for(int i=0; i < numPoints-1; i++)
        {
            if(set[i].equals(set[i+1]))
            {
                shiftArrayLeft(i+1);
                i--;
            }
        }

        //Case 2:  deals with the possibility that there exists three points
        //in a vertical vector which all have the same direction, in which case
        //the point with the middle y value should be removed.
        for (int i=0; i < numPoints-2; i++)
        {
            if(set[i].inVerticalAlignment(set[i+1]) && set[i+1].inVerticalAlignment(set[i+2]))
            {
                if(sameDirection(set[i].y, set[i+1].y, set[i+2].y))
                {
                    shiftArrayLeft(i+1);
                    i--;
                }
            }
        }

        //Case 3: if three points in a row have the same y value, the middle point is removed
        for (int i=0; i < numPoints-2; i++)
        {
            if(set[i].inHorizontalAlignment(set[i+1]) && set[i+1].inHorizontalAlignment(set[i+2]))
            {
                shiftArrayLeft(i+1);
                i--;
            }
        }

        //Case 4: the first or last two points of the array have the same y value, the first
        //or last point, respectively is removed
        boolean removedPoint = true;
		while (removedPoint == true && numPoints > 1)
        {  if (set[0].inHorizontalAlignment(set[1]))
              shiftArrayLeft(0);
           else
            removedPoint = false;
        }

        removedPoint = true;
		while (removedPoint == true && numPoints > 1)
        {  if (set[numPoints-1].inHorizontalAlignment(set[numPoints-2]))
              numPoints--;
           else
           	  removedPoint = false;                
        }
        
        trimToSize();
        simplified = true; // identify that FuzzySet has been simplified
    }

    /**
     * Checks to assure that all the x values of the FuzzySet are in ascending
     * order. Returns -1 if they are ordered properly and the index of the 
     * first of the values that is out of order if they are not.
     *
     * @return Returns -1 if all x values are ordered properly and the index of the 
     *         first of the values that is out of order if they are not
     *
     * @exception XValuesOutOfOrderException if the x values of the FuzzySet are
     *            not in strictly ascending order.
     */

    public int checkXValueOrder() 
    {
        for(int i=0; i<numPoints-1; i++)
        {
            if(set[i].x > set[i+1].x)
                return( i );
        }
        return( -1 );
    }

    /**
     * Checks to assure that all the y values of the FuzzySet are within
     * the strict range of 0.0 to 1.0 inclusive, or [0.0, 1.0]. Returns 
     * -1 if they are in the correct range and the index of the 
     * first value that is out of range if they are not.
     *
     * @return Returns -1 if all membership values are in the range [0, 1] and the index of the 
     * first value that is out of range if they are not.
     *
     * @exception YValueOutOfRangeException if the y values in the FuzzySet
     *            are not all within the range [0.0, 1.0]
     */

    public int checkYValueRange()  
    {
        for(int i=0; i<numPoints; i++)
        {
            if(set[i].y < 0.0)
                return ( i );
            else if(set[i].y > (1.0 + Parameters.FUZZY_TOLERANCE))
                return ( i );
        }
        return ( -1 );
    }



    /*
     *****************************************************************************************************
     *
     * PUBLIC METHOD FOR GETTING INFORMATION ABOUT THE SET
     *
     ******************************************************************************************************/

    /**
     * Returns the membership value of the FuzzySet at the specified x
     * value.  This is done by interpolation. Note that if there are
     * several membership values at the x value (e.g. a vertical line
     * at this x value) then the maximum membership value is returned).
     *
     * @param X the x value at which to find the
     *          membership value of the FuzzySet
     * @return  the membership value of the FuzzySet at the specified x value
     *          or -1.0 if no points in the set
     *
     */

    public double getMembership(double X)
    {
        // have to assume that none of the points between 0 and numPoints-1
        // are null ... if so this is an internal error!
        
        if (numPoints < 1)
            return -1.0;
            
        // if all points have x values > X return y value of 1st point
        if(set[0].x > X)              
            return(set[0].y);

        // if all x values are < X then return y value of last point
        if(set[numPoints-1].x < X) 
            return(set[numPoints-1].y);

        // find 1st point with x value >= the X value -- there must
        // be one or we have not put the x values in proper order
        // in the set!! BUG in this case!!
        int i=0;
        while(set[i].x < X)
            i++;
            
        // if we find an x value = X then look for MAXIMUM Y of all
        // x values that are = X
        if (set[i].x == X)
        {   double maxY = set[i++].y;
            while(i < numPoints && set[i] != null && set[i].x == X)
            {   if (set[i].y > maxY) 
                   maxY = set[i].y;
                i++;
            }
            return maxY;
        }
        // X falls between 2 x values, interpolate
        else 
        {   double deltaY = set[i].y - set[i-1].y;
            double deltaX = set[i].x - set[i-1].x;
            double x      = X - set[i-1].x;
            return(set[i-1].y + x*(deltaY/deltaX));
        }
    }
    
    /**
     * Returns the 1st X value with the specified membership value in the FuzzySet
     * This is done by interpolation. Note that if there are
     * multiple x values with this membership value 
     * then the only the 1st X value is returned.
     * <br>
     * Note that this is most often used to get the X value corresponding to
     * a membership value in a FuzzySet that is strictly increasing from 0.0 to 1.0
     * or strictly decreasing from 1.0 to 0.0 (e.g. an SFuzzySet or a ZFuzzySet).
     *
     * @param membership  the membership value at which to find the
     *          X value of the FuzzySet
     * @return  the 1st X value of the FuzzySet with the specified membership value
     *          or exception if the set does not have this membership value
     *
     */

    public double getXforMembership(double m) 
           throws NoXValueForMembershipException
    {
    	// make sure that FuzzySet is simplified so we don't have to deal with
    	// horizontal lines as 1st or last line segments
    	simplifySet();
    	
    	// if only 1 point and not the membership value throw exception else return the x value
    	if (numPoints == 1)
    		if (set[0].y == m)
    			return set[0].x;
    		else
    			throw new NoXValueForMembershipException(m);
    	
        // find 1st pair of x values that span the membership value
        int i=1;
        while(i<numPoints)
        {   if (set[i].y == m) return set[i].x;
        	if (set[i-1].y == m) return set[i-1].x;
        	if ((set[i-1].y < m && set[i].y > m)||
        	    (set[i-1].y > m && set[i].y < m))
        		break;
        	i++;
        }
            
        // if empty set or beyond end of set and no success throw exception
        if (i >= numPoints)
			throw new NoXValueForMembershipException(m);

        // membership falls between 2 x values, interpolate to get required x value
        double deltaY = set[i].y - set[i-1].y;
        double deltaX = set[i].x - set[i-1].x;
        double y      = m - set[i-1].y;
        return(set[i-1].x + y*(deltaX/deltaY));
    }
    
    /**
     * Confines the FuzzySet to the x boundaries specified, usually the 
     * Universe of Discourse.  This is achieved by truncating the FuzzySet
     * at the x boundaries.  Consider the example below:
     * <p>
     * <img src="NetGraphics/confineToXBounds.gif">
     *
     * <p>The FuzzySet on the left is a depiction of the set before being
     * confined to the UOD (Universe of Discourse), and the FuzzySet to the
     * right is the same set after being confined.  Essentially, the set 
     * to the right is the same FuzzySet; however, all points outside 
     * the UOD now have a membership value of zero.
     * <p>Note that this is infact equivalent to doing an intersection 
     * between the given FuzzySet and the 
     * <code>RectangleFuzzySet(lowXBound, highXBound)</code>, but
     * this is considerably more efficient.
     *
     * @param lowXBound  the low x value at which to bound the set
     * @param highXBound the high x value at which to bound the set
     * @exception XValuesOutOfOrderException if low and high bounds out of order
     */
     
    public void confineToXBounds(double lowXBound, double highXBound)
        throws XValuesOutOfOrderException
    {
        double lowX, lowY, highX, highY;
        if (lowXBound > highXBound)
           throw new XValuesOutOfOrderException(lowXBound, highXBound);
        if (numPoints > 0)
        {   // if entire set is inside do nothing -- most common case likely
            if (set[0].x >= lowXBound && set[numPoints-1].x <= highXBound)
               return;
               
            // if x values of set are entirely outside the bounds set to rectangular
            // FuzzySet or single point (if all y values are 0.0 inside bounds)
            lowX = set[0].x;
            highX = set[numPoints-1].x;
            if (highX < lowXBound || lowX > highXBound)
            {  lowY = set[0].y;
               highY = set[numPoints-1].y;
               numPoints = 0;
               insertSetPoint(lowXBound, 0.0);
               if (lowX > highXBound && lowY > 0.0)
               { appendSetPoint(lowXBound, lowY);
                 appendSetPoint(highXBound, lowY);
                 appendSetPoint(highXBound, 0.0);
               }
               else if (highX < lowXBound && highY > 0.0)
               { appendSetPoint(lowXBound, highY);
                 appendSetPoint(highXBound, highY);
                 appendSetPoint(highXBound, 0.0);
               }
               trimToSize();
               simplified = true;
               return;
            }
            // neither totally inside or totally outside the bounds
            //
            // check the lower end for points outside
            // remember the y value at the lowXBound  position before we start
            // to change the set
            lowY = getMembership(lowXBound);
            // should probably do a shift left of all points below lowXBound
            // at once rather than doing removes since they are inefficient
            while ( set[0].x < lowXBound )
                removeSetPoint(set[0]);
            // add the 1st 2 points as required ... 
            // if both added add the (lowXBound,0) 1st because insertPoint will add
            // point with same x value at end of all those with that x value
            if (set[0].y != 0.0)
               if (set[0].x != lowXBound)
               {   if (lowY != 0.0) // don't add same point twice
                      insertSetPoint(lowXBound, 0.0);
                   insertSetPoint(lowXBound, lowY);
               }
               else
               {   // just add the (lowXBound,0) unless 1st y value already is 0
                   // have to make sure it gets added at the beginning
                   shiftArrayRight(0);
                   set[0] = new SetPoint(lowXBound, 0.0);  
               }
                
            //check the upper end for points outside    
            highY = getMembership(highXBound);
            int i = numPoints-1;
            while (set[i].x > highXBound)
               {  set[i] = null;
                  i--; 
                  numPoints--;
               }
            if (set[i].y != 0.0)
               if (set[i].x != highXBound)
               {  appendSetPoint(highXBound, highY);
                  if (highY > 0.0)
                     appendSetPoint(highXBound, 0.0);
               }
               else
                  appendSetPoint(highXBound, 0.0);
            trimToSize();
            simplifySet();
        }
    }     
                
    /**
     * Returns the size, or the number of points, in the FuzzySet.
     *
     * @return the integer which represents the number of points in the FuzzySet.
     */

    public int size()
    {
        return(numPoints);
    }

    /**
     * Returns the x value of the SetPoint at the specified index in
     * the FuzzySet.
     *
     * @param i the integer index of the SetPoint whose x value is to be returned
     * @return  the double x value of the SetPoint at the specified index
     *          in the FuzzySet
     * @exception ArrayOutOfBoundsException if the parameter <code>i</code>, the
     *            specified index, is an illegal index
     */

    public double getX(int i)
    {
        return(set[i].x);
    }

    /**
     * Returns the y value of the SetPoint at the specified index in
     * the FuzzySet.
     *
     * @param i the integer index of the SetPoint whose y value is to be returned
     * @return  the double y value of the SetPoint at the specified index
     *          in the FuzzySet
     * @exception ArrayOutOfBoundsException if the parameter <code>i</code>, the
     *            specified index, is an illegal index
     */

    public double getY(int i)
    {
        return(set[i].y);
    }

    /**
     * Returns the minimum membership value of the FuzzySet.
     *
     * @return the double value representing the minimum membership value
     *         of the FuzzySet. If the FuzzySet is empty returns 0.0.
     */

    public double getMinY()
    {
        double min;
       
        if (numPoints <= 0) return 0.0;
        
        min = set[0].y;
        for(int i=1; i < numPoints; i++)
        {   if (set[i].y < min) min = set[i].y;
            if(min == 0.0) return(min);
        }

        return(min);
    }

    /**
     * Returns the maximum membership value of the FuzzySet.
     *
     * @return the double value representing the maximum membership value
     *         of the FuzzySet. If the FuzzySet is empty returns 0.0.
     */

    public double getMaxY()
    {
        double max = 0.0;

        for(int i=0; i < numPoints; i++)
           if (set[i].y > max) max = set[i].y;

        return((numPoints > 0) ? max : 0.0);
    }

    /**
     * Returns the SetPoint at the specified index in the FuzzySet.
     *
     * @param i the index of the SetPoint to return
     * @return  the SetPoint in the FuzzySet at the specified index
     * @exception ArrayIndexOutOfBoundsException if the parameter numPoints is
     *                  greater than the length of the SetPoint array, or the
     *                  number of SetPoints contained in the array.
     */
    public SetPoint getPoint(int i)
    {
        return(set[i]);
    }

    /**
     * Sets the precision, in terms of the number of decimal places that will
     * be printed for each double value, of the <code>toString</code> method.
     *
     * @param numDecimalPlaces the desired number of decimal places for each
     *                         double value printed by the <code>toString</code>
     *                         method
     */

    public static void setToStringPrecision(int numDecimalPlaces)
    {
        toStringPrecision = numDecimalPlaces;
    }


    /**
     * Returns a string representation of the FuzzySet.  For example, if the FuzzySet
     * consisted of the following points, where the format is (x, u(x)), where u(x) is
     * the membership value or the y value:
     * <ul>
     * <li>(1.0, 0.0) (2.0, 1.0) (3.0, 0.0)
     * </ul>
     * <br>then the string representation takes the form: u(x)/x.
     * <ul>
     * <li>{ 0.0/1.0 1.0/2.0 0.0/3.0 }
     * </ul>
     *
     * @return the String representation of the FuzzySet
     */

    public String toString()
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(toStringPrecision);
        StringBuffer fuzzySetString = new StringBuffer(50);

        fuzzySetString.append("{ ");

  	    for(int i=0; i<numPoints; i++)
  	    {
  	        fuzzySetString.append(nf.format(set[i].y));
  	        fuzzySetString.append('/');
  	        fuzzySetString.append(nf.format(set[i].x));
  	        fuzzySetString.append(" ");
  	    }

        fuzzySetString.append("}");

  	    return(fuzzySetString.toString());
    }


    /*
     *****************************************************************************************************
     *
     * MATHEMATICAL METHODS TO DEAL WITH FLOATS
     *
     ******************************************************************************************************/

    /**
     * Tests two double values for equality with a fine tolerance.
     *
     * @param value1 the first double value
     * @param value2 the second double value
     *
     * @return <code>true</code> if the values are equal within a certain tolerance.
     */

    protected static boolean fuzzyEqual(double value1, double value2)
    {
        return( Math.abs(value1 - value2) < Parameters.FUZZY_TOLERANCE );
    }


    /*
     *****************************************************************************************************
     *
     * SUPPORT AND ALPHA CUTS ->  both return a support
     *
     ******************************************************************************************************/

    /**
     * Returns the support of this FuzzySet.  The support set of a FuzzySet
     * is the set of its elements that have a membership function other than the
     * trivial membership of zero.
     *
     * @return an IntervalVector object which represents the support set of this FuzzyValue,
     *         or <code>null</code> if the FuzzySet has a uniform membership value of zero.
     */

    protected IntervalVector getSupport(double minUOD, double maxUOD)
    {
        return(getAlphaCut(Parameters.STRONG, 0.0, minUOD, maxUOD));
    }

    /**
     * Returns the alpha cut of this FuzzySet at the membership level specified by
     * the <code>cut</code> argument.  Formally, a distinction is made betweeen two
     * types of alpha cuts, the <i>strong</i> and the <i>weak</i> alpha cut.  
     *
     * <p>The WEAK alpha cut of a FuzzySet is the set of all elements in the
     * Universe of Discourse for which the membership function of the FuzzySet
     * is greater than or equal to the <code>cut</code> argument.
     *
     * <p>The STRONG alpha cut of a FuzzySet is the set of all elements in the
     * Universe of Discourse for which the membership function of the FuzzySet
     * is greater than the <code>cut</code> argument. Note that a STRONG alpha cut 
     * at 0.0 is equivalent to the Support of the fuzzy set)
     *
     * @param cutType either Paramters.WEAK or Parameters.STRONG (defaults to WEAK if 
     *               any other value)
     * @param cut    the double membership value at which the alpha cut is taken.  The
     *               resulting support set is the set of all elements for which the
     *               membership value is greater than or equal to the cut value.
     *               (normally a value between 0.0 and 1.0)
     * @param minUOD the minimum x value to be considered 
     * @param maxUOD the maximum x value to be considered. Note if minUOD > maxUOD the values
     *               will be switched.
     *
     * @return       the IntervalVector which represents the alpha cut of this FuzzyValue
     *               at the double value of the <code>cut</code> argument, or <code>null</code>
     *               if the alpha cut at the cut level results in zero intervals.
     *
     */

    public IntervalVector getAlphaCut(boolean cutType, double cut, double minUOD, double maxUOD)
    {
        if(numPoints == 0)
        {   // empty fuzzy set! Perhaps should just return the empty Interval Vector ... size of zero!!??
            return null;
        }
        
        IntervalVector alphaCut = new IntervalVector();
        
        if (cutType != Parameters.WEAK && cutType != Parameters.STRONG)
            cutType = Parameters.WEAK;
            
        if (minUOD > maxUOD) 
        {
            double temp = minUOD;
            minUOD = maxUOD;
            maxUOD = temp;
        }

        simplifySet();

        if (cutType == Parameters.WEAK && cut == 0.0) 
        {
            alphaCut.addInterval(new Interval(minUOD, false, maxUOD, false));
            alphaCut.trimToSize();
            return(alphaCut);
        }

        //special case: only one point
        if (numPoints == 1)
        {
            if ((cutType == Parameters.WEAK && set[0].y < cut) || (cutType == Parameters.STRONG && set[0].y <= cut))
            { alphaCut = null;
            } 
            else 
            { alphaCut.addInterval(new Interval(minUOD, false, maxUOD, false));
              alphaCut.trimToSize();
            }
            return(alphaCut);
        }

        int i=0, j=0;
        double[] cutPoints = new double[numPoints+2];
        boolean[] cutFlags = new boolean[numPoints+2];

        if ((cutType == Parameters.WEAK && set[0].y >= cut) || (cutType == Parameters.STRONG && set[0].y > cut))
        {
            cutPoints[j] = minUOD;
            cutFlags[j++] = false;
        }

        while (i < numPoints-1)
        {
            //if there is an intersection of the FuzzySet with the imaginary alpha cut line
            if ((set[i].y <= cut && cut <= set[i+1].y) || (set[i].y >= cut && cut >= set[i+1].y))
            {
                if (cutType == Parameters.STRONG)
                {
            		//
                	//    o
                	//     \
                	// -----o---- alpha cut line
                	//
                	if (set[i].y > cut && cut == set[i+1].y)
                	{
                        cutPoints[j] = set[i+1].x;
                        cutFlags[j++] = (set[i].x != set[i+1].x);
                    }

            		//
                	//        o
                	//       /
                	// -----o---- alpha cut line
                	//
                	if (set[i].y == cut && cut < set[i+1].y)
                	{
                        cutPoints[j] = set[i].x;
                        cutFlags[j++] = (set[i].x != set[i+1].x);
                    }

                }
                else //(cutType == Parameters.WEAK)
                {
    		        //
                	// -----o----  alpha cut line
                	//       \
                	//        o
                	//
                	if (set[i].y == cut && cut > set[i+1].y)
                	{
                        cutPoints[j] = set[i].x;
                        cutFlags[j++] = false;
                    }

            		//
                	// -----o----  alpha cut line
                	//     /
                	//    o
                	//
                	if (set[i].y < cut && cut == set[i+1].y)
                	{
                        cutPoints[j] = set[i+1].x;
                        cutFlags[j++] = false;
                    }
                }

            	//        o                          o
		        //       /                            \
            	// -----/----  alpha cut line or  -----\-----
            	//     /                                \
            	//    o                                  o
            	//
            	if ((set[i].y < cut && cut < set[i+1].y) ||
            	    (set[i].y > cut && cut > set[i+1].y)
            	   )
            	{
                    //Diagram of similar triangles algorithm to find the x value
                    //at the specified y value
                    //
                    //                       O P2
                    //                      /|
                    //                     / |      P1 ~ point 1
                    //                    /  |      P2 ~ point 2
                    //                IP /   |      IP ~ intersection point
                    //------------------o----|-------- y (membership value to cut at)
                    //	        	   /|    | dY
                    //                / |dy  |
                    //               /  |    |
                    //           P1 O---o----o
                    //                x   dX
                    //
                    //    let dy = y - P1y
                    //        dY = P2y - P1y
                    //        dX = P2x - P1x
                    //
                    //  x = P1x + (dy*dX)/dY
                    //  where x is the x coordinate of the point of intersection
                    //

                    double dy = cut - set[i].y;
                    double dX = set[i+1].x - set[i].x;
                    double dY = set[i+1].y - set[i].y;
                    double x = set[i].x + (dy*dX)/dY;

                    cutPoints[j] = x;
                    if (cutType == Parameters.STRONG)
                       cutFlags[j++] = (set[i].x != set[i+1].x);
                    else
                       cutFlags[j++] = false;
                }
            }

            i++;
        }

        if((cutType == Parameters.WEAK && set[numPoints-1].y >= cut) ||
           (cutType == Parameters.STRONG && set[numPoints-1].y > cut))
        {
            cutPoints[j] = maxUOD;
            cutFlags[j++] = false;
        }

        // create the Intervals ... making sure that any 2 consecutive
        // intervals do not have the 1st interval end  and second 
        // interval beginning do not have identical 'closed' values ..
        // if so join them into a single Interval
        for (int z=0; z<j; z+=2)
        {
            int beg = z;
            while (z+2 < j && 
                   (cutPoints[z+1]==cutPoints[z+2] && cutFlags[z+1]==false &&
                    cutFlags[z+2]==false
                   )
                  )
               z += 2;
            int end = z+1;
            
            alphaCut.addInterval(new Interval(cutPoints[beg], cutFlags[beg],
                                              cutPoints[end], cutFlags[end]));
        }

        alphaCut.trimToSize();

        if(alphaCut.size() > 0) 
             return(alphaCut);
        else 
             return(null);
    }

    /*
     *************************************************************************************************
     *
     * PUBLIC METHODS FOR ASSESSING VARIOUS ATTRIBUTES OF THE FUZZY SET
     *
     **************************************************************************************************/

    /**
     * Returns true if this FuzzySet has been simplified; in other words, if this FuzzySet does not
     * contain any unnecessary points (see simplifySet method).
     *
     * @return true if this FuzzySet is empty
     */

    public boolean isSimplified()
    {
        return(simplified);
    }


    /**
     * Returns true if this FuzzySet is empty; in other words, if this FuzzySet does not
     * contain any points.
     *
     * @return true if this FuzzySet is empty
     */

    public boolean isEmpty()
    {
        return(numPoints==0);
    }


    /**
     * Returns true if this FuzzySet is normal. The definition of normal is that
     * there is at least one point in the universe of discourse where the membership
     * function reaches unity (in terms of this fuzzy package, unity is equal to 1.0).
     * Note that since we do allow membership values to be > 1.0 to accomodate special
     * uses of the FuzzySets, there must be no values > 1.0 for it to be normal.
     *
     * @return true if this FuzzySet is normal
     */

    public boolean isNormal()
    {   boolean bResult = false;
        for(int i=0; i<numPoints; i++)
        {   if (set[i].y > 1.0) return (false);
			if (set[i].y == 1.0) bResult = true;
        }
        return(bResult);
    }

    /**
     * Returns true if this FuzzySet is convex.  The definition of convex is that
     * the membership function of a convex FuzzySet does not go "up-and-down"
     * more than once.
     *
     * @return true if this FuzzySet is convex
     */

    public boolean isConvex()
    {
        boolean negativeSlope = false;
        boolean convex = true;
        double previous = set[0].y;
        double prevDiff = 0, currentDiff = 0;

        for(int i=0; i < numPoints; i++){
            currentDiff = set[i].y - previous;
            
            if( !sameSign(prevDiff, currentDiff) && negativeSlope){
                convex = false;
            }
            
            prevDiff = (set[i].y - previous == 0) ? prevDiff : set[i].y - previous;
            previous = set[i].y;
            if( prevDiff < 0 ) negativeSlope = true;
        }

        return(convex);
    }

    /**************************************************************************************************
     *
     * PROTECTED METHODS FOR SIMPLIFYING TESTS
     *
     **************************************************************************************************/

    /**
     * Tests whether the two double value arguments have the same sign.
     *
     * @param number1 one of the two double values to compare
     * @param number2 one of the two double values to compare
     * @return        <code>true</code> if the two double values have the
     *                same sign, ie. they are either both negative or
     *                both positive.  It is important to note that if
     *                one of the values is zero, the method will always
     *                return <code>true</code>, as this method regards
     *                zero as being both (or neither, depending on how you
     *                look at it) negative and positive.
     */

    protected boolean sameSign(double number1, double number2)
    {
        return(((number1 >= 0.0) && (number2 >= 0.0)) || ((number1 <= 0.0) && (number2 <= 0.0)));
    }

    /**
     * Tests whether the y values are in strictly ascending or descending order
     * in the order they are passed as parameters.  Please see below for a
     * visual depiction of the situation.
     * <p>
     * <img src="NetGraphics/sameDirection.gif">
     *
     * @param y1 the first y value
     * @param y2 the second y value
     * @param y3 the third y value
     *
     * @return   <code>true</code> if the y values are in strictly ascending
     *           or descending order, as shown in the diagram above
     */

    protected boolean sameDirection(double y1, double y2, double y3)
    {
        return( ((y1 < y2) && (y2 < y3)) || ((y1 > y2) && (y2 > y3)) );
    }

    /**
     * Tests whether the y value parameters are in ascending order.  This
     * means that the first y value is less than the second y value.
     *
     * @param y1 the first y value
     * @param y2 the second y value
     *
     * @return   <code>true</code> if the first y value is less than
     *           the second y value
     */

    protected boolean goingUp(double y1, double y2)
    {
        return(y1 < y2);
    }

    /**
     * Tests whether the y value parameters are in descending order.  This
     * means that the first y value is greater than the second y value.
     *
     * @param y1 the first y value
     * @param y2 the second y value
     *
     * @return   <code>true</code> if the first y value is greater than
     *           the second y value
     */

    protected boolean goingDown(double y1, double y2)
    {
        return(y1 > y2);
    }

    /**
     * Returns the horizontal union of the FuzzySet at the
     * membership level specified by the y parameter.  Please see the
     * diagram below for a visual depiction. If y is < 0 then y is set to 0.
     * <p>
     * <img src="NetGraphics/horizontalUnion.gif">
     *
     * @param y the y value with which the set is being unioned
     *
     * @return a new FuzzySet object representing the horizontal
     *         union for the FuzzySet with the y value
     *         argument
     */

    public FuzzySet horizontalUnion(double y) 
    {
        return(horizontalIntersectionUnion(y, UNION));
    }

    /**
     * Returns the horizontal intersection of the FuzzySet at the
     * membership level specified by the y parameter.  Please see the
     * diagram below for a visual depiction. If y is < 0 then y is set to 0.
     * <p>
     * <img src="NetGraphics/horizontalIntersection.gif">
     *
     * @param y the y value with which the set is being intersected
     *
     * @return a new FuzzySet object representing the horizontal
     *         intersection for the FuzzySet with the y value
     *         argument
     */

    public FuzzySet horizontalIntersection(double y) 
    {
        return(horizontalIntersectionUnion(y, INTERSECTION));
    }

    /**
     * Returns the horizontal intersection or union of the FuzzySet
     * at the membership level specified by the y parameter.
     * Whether a horizontal intersection is performed, or a horizontal
     * union, is determined by the <code>op</code> parameter.
     * If y is < 0 then y is set to 0.
     *
     * @param y  the y value with which the set is being intersected or
     *           unionized
     * @param op the integer which specifies the operation to perform.  The
     *           only possible values for this parameter are the constants
     *           INTERSECTION and UNION.
     *
     * @return   a new FuzzySet object that represents the horizontal union
     *           or horizontal intersection, depending on what was requested
     *
     */

    protected FuzzySet horizontalIntersectionUnion(double y, int op) 
    {
        FuzzySet resultSet;

        double previousY, currentY;
        double previousX, currentX;
        double x;
        
        if (y < 0.0) y = 0.0;

        if ( (op == UNION) && (y == 0.0) )
        {
            resultSet = new FuzzySet(this);
            return(resultSet); // no need to simplify the set since it was already a fuzzySet
        }
        
        resultSet = new FuzzySet();
        previousX = currentX = set[0].x;
        previousY = currentY = set[0].y;

        for (int i=0; i < numPoints; i++)
        {
            previousX = currentX;  currentX = set[i].x;
            previousY = currentY;  currentY = set[i].y;

            if ( (previousY < y && y < currentY) || (previousY > y && y > currentY) )
            {   //by similar triangles (a diagram is in order here guys...)
                x = previousX + ((currentX - previousX)*(y - previousY))/(currentY - previousY);
                resultSet.appendSetPoint(x, y);
            }

            if ( ((op == INTERSECTION) && (currentY <= y)) || ((op == UNION) && (currentY >= y)) )
                resultSet.appendSetPoint(currentX, currentY);
        }

        if (resultSet.numPoints == 0)
            resultSet.insertSetPoint(set[0].x, y);

        resultSet.simplifySet();
        return(resultSet);
    }

    /**
     * Returns a tentative indication of whether or not the FuzzySets intersect.
     * If this method returns <code>true</code>, meaning that to its knowledge
     * the sets don't intersect, this can be taken for gospel.  However, if the
     * method returns <code>false</code>, it is indicative that an intersection
     * of the FuzzySets is probable, but not certain. This is done as a fast test
     * for no intersection.
     *
     * @param b a FuzzySet to be tested against this FuzzySet no intersection
     *
     * @return  <code>true</code> if the FuzzySets definitely do not intersect;
     *          <code>false</code> if there is a possibility that they do
     */

    public boolean noIntersectionTest(FuzzySet b)
    {   // do simple test to see if either set is completely below the other
        // and if so return true since they definitely do NOT overlap
        if (numPoints > 1 && b.numPoints >1)
           if ( test(this, b) || test(b, this) )
              return true;
              
        // finally if either of the sets has only zero membership values
        // then they definitely do NOT overlap (otherwise they may overlap)
        boolean thisAllZeros = true;
        for (int i=0; i<numPoints; i++)
            if (set[i].y > 0) 
               { thisAllZeros = false;
                 break;
               }
        if (thisAllZeros) return true;
        
        for (int i=0; i<b.numPoints; i++)
            if (b.set[i].y > 0) return false; // sets overlap
            
        return true; // set b all zeros .. no overlap
    }

    /**
     * Returns a tentative indication of whether or not the FuzzySets intersect.
     * If this method returns <code>true</code>, meaning that to its knowledge
     * the sets don't intersect, this can be taken for gospel.  However, if the
     * method returns <code>false</code>, it is indicative that an intersection
     * of the FuzzySets is probable, but not certain. This is done as a fast test
     * for no intersection.
     *
     * <p>This method tests its intersection conditions in a lop-sided manner.
     * It will test to see if the first FuzzySet occurs before the second FuzzySet
     * on the x axis (and that the end of the first FuzzySet at is zero y and the
     * beginning of the second is at zero y) to check for non-intersection, but it
     * won't check to see if the same is true for the second FuzzySet lower on
     * the x axis than the first.  For this reason, this method should be called
     * twice with the parameters exchanged the second time in order to gain a more
     * thorough analysis.
     *
     * <p>For example: (test(set1, set2) || test(set2, set1)).
     *
     * <p>OR is the conditional operator used in the call, because all it takes is
     * one return of <code>true</code> to confirm that the FuzzySets definitely
     * do not intersect.
     *
     * @param a a FuzzySet which may intersect the other FuzzySet passed
     * @param b a FuzzySet which may intersect the other FuzzySet passed
     *
     * @return  <code>true</code> if the FuzzySets definitely do not intersect;
     *          <code>false</code> if there is a possibility that they do
     */

    private boolean test(FuzzySet a, FuzzySet b)
    {
        boolean aLastPointZero = ( a.set[a.numPoints-1].y == 0 );
        boolean bFirstPointZero = ( b.set[0].y == 0 );

        if ((a.set[a.numPoints-1].x < b.set[0].x) && aLastPointZero && bFirstPointZero)
            return true;

        return false;
    }

    /**
     * Tests whether or not two FuzzySets intersect.  This intersection test
     * is complete and thorough unlike the noIntersectionTest method.  Without
     * dwelling on what we would intuitively know as intersection of two
     * FuzzySets, the more subtle differences between intersection and
     * nonintersection are depicted below.
     * <p>
     * <table>
     *      <tr>
     *          <td align=MIDDLE>
     *              <b>No Overlap, No Intersection</b>
     *          </td>
     *          <td align=MIDDLE>
     *              <b>Overlap, therefore Intersection</b>
     *          </td>
     *      </tr>
     *      <tr>
     *          <td>
     *              <img src="NetGraphics/nonIntersectionTest_TRUE.gif">
     *          </td>
     *          <td>
     *              <img src="NetGraphics/nonIntersectionTest_FALSE.gif">
     *          </td>
     *      </tr>
     * </table>
     *
     * @param otherSet the other FuzzySet which might possibly intersect
     *                 with this FuzzySet
     * @return <code>true</code> if the FuzzySets <b>do not</b> intersect,
     *         <code>false</code> if they <b>do</b> intersect
     */

    public boolean nonIntersectionTest(FuzzySet otherSet)
    {
        IntervalVector a = this.getSupport(Double.MIN_VALUE, Double.MAX_VALUE);
        IntervalVector b = otherSet.getSupport(Double.MIN_VALUE, Double.MAX_VALUE);

        // do any of the support intervals of the 2 sets overlap? 
        // if so return false, if not return true
        for(int i=0; i<a.size(); i++)
        {
            for(int j=0; j<b.size(); j++)
            {
                if (a.intervalAt(i).getLowX() > b.intervalAt(j).getHighX())
                    continue;

                else if (a.intervalAt(i).getHighX() < b.intervalAt(j).getLowX())
                    continue;

                else if (a.intervalAt(i).getLowX() == b.intervalAt(j).getHighX())
                {
                    if (!a.intervalAt(i).getLowOpenFlag() && !b.intervalAt(j).getHighOpenFlag())
                        return false;
                }
                else if (a.intervalAt(i).getHighX() == b.intervalAt(j).getLowX())
                {
                    if (!a.intervalAt(i).getHighOpenFlag() && !b.intervalAt(j).getLowOpenFlag())
                        return false;
                }
                else
                    return false;
            }
        }

        return true;
    }

    /**
     * Returns the maximum y value of the overlapping portion of two vertical line 
     * segments that run in opposite directions. If the line segments are in the 
     * same direction (in other words, both ascending vertically or descending vertically)
     * or they do not overlap the return value is -1.0.
     *
     * @param a the first point of the first line segment
     * @param b the second point of the first line segment
     * @param c the first point of the second line segment
     * @param d the second point of the second ine segment
     *
     * @return  <ul>
     *          <li>-1.0 if the line segments are in the same direction, in other words, one
     *          is descending and the other is ascending. This is also the return value if the
     *          line segments do not overlap.
     *          <li>the maximum y value of the two line segments.
     *          </ul>
     */

    protected double findMaxYOverlapValue(SetPoint a, SetPoint b, SetPoint c, SetPoint d)
    {
        if( (goingUp(a.y, b.y) && goingUp(c.y, d.y)) || (goingDown(a.y, b.y) && goingDown(c.y, d.y)) )
            return(-1.0);

        double maxAB = (a.y > b.y) ? a.y : b.y;
        double minAB = (a.y < b.y) ? a.y : b.y;

        double maxCD = (c.y > d.y) ? c.y : d.y;
        double minCD = (c.y < d.y) ? c.y : d.y;

        if( maxAB < minCD || maxCD < minAB )
            return(-1.0);

        if(maxAB > maxCD) return(maxCD);
        else              return(maxAB);
    }



    /*
     *****************************************************************************************************
     *
     * PUBLIC METHODS FOR MODIFYING THE ACTUAL ARRAY OF SETPOINTS
     *
     ******************************************************************************************************/

    /**
     * Inserts the SetPoint parameter into the SetPoint array which constitutes
     * the <code>FuzzySet</code>.  The new SetPoint is placed in the FuzzySet so
     * as to keep the x values of the set in consistently increasing order.
     *
     * <p>For example, let us pretend that the existing FuzzySet looks like this:
     * <p>0/1 1/2 1/4 0.5/4 0/5  (y/x... or u(x)/x format)
     * <p>If the point 0.25/1.5 was inserted by this method, the result would be:
     * 0/1 0.25/1.5 1/2 1/4 0.5/4 0/5.  If the point 0.75/4 was inserted, the
     * result would be: 0/1 1/2 1/4 0.5/4 0.75/4 0/5.  Therefore, it is important
     * to note that if a point is inserted and it's x value is the same
     * as that of an existing point, it will be inserted into the FuzzySet at a
     * position after the existing point with the same x value.
     *
     * @param a the SetPoint to be inserted into the FuzzySet
     */

    public void insertSetPoint(SetPoint a)
    {
        insertSetPoint(a.x, a.y);
    }

    /**
     * Inserts the double parameters X and Y into the SetPoint array which constitutes
     * the <code>FuzzySet</code>.  The new SetPoint consisting of the passed X and Y values
     * is placed in the FuzzySet so as to keep the x values of the set in consistently
     * increasing order.
     *
     * <p>For example, let us pretend that the existing FuzzySet looks like this:
     * <p>0/1 1/2 1/4 0.5/4 0/5  (y/x... or u(x)/x format)
     * <p>If the point 0.25/1.5 was inserted by this method, the result would be:
     * 0/1 0.25/1.5 1/2 1/4 0.5/4 0/5.  If the point 0.75/4 was inserted, the
     * result would be: 0/1 1/2 1/4 0.5/4 0.75/4 0/5.  Therefore, it is important
     * to note that if a point is inserted and it's x value is the same
     * as that of an existing point, it will be inserted into the FuzzySet at a
     * position after the existing point with the same x value.
     * <br>
     * Also note that any membership values must be >= 0. If not they will be set to 0.
     *
     * @param X the double x value to be inserted in the FuzzySet
     * @param Y the double y value to be inserted in the FuzzySet
     */

    public void insertSetPoint(double X, double Y)
    {
        // make sure there is enuff room for this new point
        testArrayLength();
        // make sure membership values are >= 0.0
        if (Y < 0.0) Y = 0.0;
        
        // when the set is empty just add the point
        if (numPoints == 0) 
            set[numPoints++] = new SetPoint(X, Y);
        else
        {   // find the place to put the new point ...
            // results in i == the index where the point should be inserted
            int i=0;
            
            while ((i < numPoints) && (set[i].x <= X))
                i++;

            shiftArrayRight(i);
            set[i] = new SetPoint(X, Y);  
        }
        simplified = false; // may no longer be a simplfied Fuzzy Set
    }

    /**
     * Appends the SetPoint parameter to the end of the SetPoint array which constitutes
     * the <code>FuzzySet</code>.  The new SetPoint is placed in the FuzzySet so
     * as to keep the x values of the set in consistently increasing order. If the
     * x value of the SetPoint being added is < the last x value in the array it will
     * work backwards to place it in proper x order.
     *
     * <p>For example, let us pretend that the existing FuzzySet looks like this:
     * <p>0/1 1/2 1/4 0.5/4 0/5  (y/x ... or u(x)/x format)
     * <p>If the point 0.25/5.5 was appended by this method, the result would be:
     * 0/1 1/2 1/4 0.5/4 0/5 0.25/5.5. If the point 0.75/4 was inserted, the
     * result would be: 0/1 1/2 1/4 0.5/4 0.75/4 0/5.  Therefore, it is important
     * to note that if a point is appended and it's x value is the less than
     * that of the last point, it will be placed into the FuzzySet at a
     * position after all points with the x values less than or equal to it's x value.
     * <br>
     * Also note that any membership values must be >= 0. If not they will be set to 0.
     *
     * @param a the SetPoint to be inserted into the FuzzySet
     */

    public void appendSetPoint(SetPoint a)
    {
        appendSetPoint(a.x, a.y);
    }

    /**
     * Appends the SetPoint parameter to the end of the SetPoint array which constitutes
     * the <code>FuzzySet</code>.  The new SetPoint is placed in the FuzzySet so
     * as to keep the x values of the set in consistently increasing order. If the
     * x value of the SetPoint being added is < the last x value in the array it will
     * work backwards to place it in proper x order.
     *
     * <p>For example, let us pretend that the existing FuzzySet looks like this:
     * <p>0/1 1/2 1/4 0.5/4 0/5  (y/x ... or u(x)/x format)
     * <p>If the point 0.25/5.5 was appended by this method, the result would be:
     * 0/1 1/2 1/4 0.5/4 0/5 0.25/5.5. If the point 0.75/4 was inserted, the
     * result would be: 0/1 1/2 1/4 0.5/4 0.75/4 0/5.  Therefore, it is important
     * to note that if a point is appended and it's x value is the less than
     * that of the last point, it will be placed into the FuzzySet at a
     * position after all points with the x values less than or equal to it's x value.
     * <br>
     * Also note that any membership values must be >= 0. If not they will be set to 0.
     *
     * @param X the double x value to be inserted in the FuzzySet
     * @param Y the double y value to be inserted in the FuzzySet
     */

    public void appendSetPoint(double X, double Y)
    {
        int i;

        // make sure there is enuff room for this new point
        testArrayLength();
		// make sure membership values are >= 0.0
		if (Y < 0.0) Y = 0.0;
        
        // if the last x value is less than or equal to this one 
        // OR this is the first point add it to end
        if (numPoints == 0 || set[numPoints-1].x <= X)
            set[numPoints++] = new SetPoint(X, Y);
        else
        {   // otherwise find the place to put the new point ...
            // results in i == the index before the point should be inserted
            i = numPoints-2;
            while ((i >= 0) && (set[i].x > X))
                i--;

            shiftArrayRight(++i);
            set[i] = new SetPoint(X, Y);     
        }
        simplified = false; // may no longer be a simplfied Fuzzy Set
    }


    /**
     * Find the SetPoint in the FuzzySet and remove it if found.
     * @param a the SetPoint
     *
     * @return a boolean value with the following significance
     * <ul>
     * <li>true: the point was found and removed
     * <li>false: the point did not exist in the FuzzySet and no action was taken
     * </ul>
     */

    public boolean removeSetPoint(SetPoint a)
    {
        int i;
        
        for (i=0; i<numPoints; i++)
            if ( set[i].equals(a) ) 
            {
                shiftArrayLeft(i);
                simplified = false; // may no longer be a simplfied Fuzzy Set
                return(true);
            }
            
        return(false);        
    }

    /**
     * Find the SetPoint with x,y values in the FuzzySet and remove it if found.
     *
     * @param X the double value
     * @param Y the double value
     *
     * @return a boolean value with the following significance
     * <ul>
     * <li>true: the point was found and removed
     * <li>false: the point did not exist in the FuzzySet and no action was taken
     * </ul>
     */

    public boolean removeSetPoint(double X, double Y){
        return( removeSetPoint(new SetPoint(X, Y)) );
    }

    /**************************************************************************************************
     *
     * PROTECTED METHODS FOR MANIPULATING THE ACTUAL ARRAY OF SETPOINTS
     *
     **************************************************************************************************/

    /**
     * Concatenates the FuzzySet passed as a parameter to this instance
     * of FuzzySet.  This method does not affect the FuzzySet calling
     * it or the FuzzySet passed to it.
     *
     * @param otherSet the FuzzySet to concatenate to this instance of
     *                 FuzzySet
     *
     * @return         a newly constructed FuzzySet which is the concatenation
     *                 of this instance with the passed FuzzySet.
     *
     */

    protected FuzzySet concat(FuzzySet otherSet) {
        return(concat(this, otherSet));
    }

    /**
     * Concatenates the two FuzzySets passed as a parameters.  This method
     * does not affect or alter the FuzzySets passed to it.
     *
     * @param a the first of the FuzzySets to be concatenated
     * @param b the second of the FuzzySets to be concatenated
     *
     * @return  a newly constructed FuzzySet which is the concatenation
     *          of the two FuzzySet parameters.
     *
     */

    protected FuzzySet concat(FuzzySet a, FuzzySet b) {
        FuzzySet fs = new FuzzySet();

        //Note: the method appendSetPoint will insert 
        //      the points in the correct position relative to its x value
        //
        //Also Note:  because appendSetPoint adds points in the 
        //      correct order relative to the x value there is no need to 
        //      throw the XValuesOutOfOrderException.
        //
        //Also there is an assumption that in general the 2 sets are 
        //most often going to be non-overlapping and that append will
        //be most efficient in this case!
        if (a.set[0].x < b.set[0].x)
        {
            for(int i=0; i<a.numPoints; i++)
               fs.appendSetPoint(a.set[i]);

            for (int i=0; i<b.numPoints; i++)
               fs.appendSetPoint(b.set[i]);
        }
        else
        {
            for(int i=0; i<b.numPoints; i++)
               fs.appendSetPoint(b.set[i]);

            for (int i=0; i<a.numPoints; i++)
               fs.appendSetPoint(a.set[i]);
        }
        
        fs.simplifySet();
        return(fs);
    }

    /**
     * Remove excess space that might be allocated in 
     * the array holding the fuzzy set points.
     */

    protected void trimToSize()
    {
        if(set.length != numPoints)
        {
            SetPoint[] newArray = new SetPoint[numPoints];

            for(int i=0; i<numPoints; i++)
                newArray[i] = set[i];

            set = newArray;
        }
    }

    /**
     * Test to see if set array of SetPoints is full and if so 
     * increment space in array.
     */

    protected void testArrayLength()
    {
        if(numPoints == set.length) incrementArrayLength();
    }

    /**
     * Add space to the set array holding the SetPoints
     */

    protected void incrementArrayLength()
    {
        SetPoint[] newArray = new SetPoint[numPoints+INCREMENT];

        for(int i=0; i < this.size(); i++)
            newArray[i] = set[i];

        set = newArray;
    }

    /**
     * Add space to the set array holding the SetPoints
     */

    protected void incrementArrayLength(int newSize)
    {
        SetPoint[] newArray = new SetPoint[(numPoints < newSize) ? newSize : numPoints];

        for(int i=0; i < this.size(); i++)
            newArray[i] = set[i];

        set = newArray;
    }

    /**
     * shifts all the SetPoint array elements which have an index value greater 
     * than or equal to the parameter <code>index</code> left one position.  In addition to
     * performing the shift, this method also decrements the count of the elements 
     * n the array (UNLESS it is EMPTY!!).
     *
     * @param index     the index value of the first element in the array which is to be shifted.
     *                  This element and all further sequential elements in the array are shifted
     *                  down one index value.
     */

    protected void shiftArrayLeft(int index)
    {
        if (numPoints == 0) return;
        
        for (int i=index; i<(numPoints-1); i++)
            set[i]=set[i+1];
        
        numPoints--;
    }

    /**
     * shifts all the SetPoint array elements which have an index value greater 
     * than or equal to the parameter <code>index</code> right one position.  In addition to
     * performing the shift, this method also increments the
     * count of the elements in the array (UNLESS it is EMPTY!!).
     * <p>Important Note: any method that calls this method to make space for a new element
     * must necessarily call the <code>testArrayLength</code> method first to ensure that there
     * is enough room in the array for the shifting to occur.
     *
     * @param index     the index value of the first element in the array which is to be shifted.
     *                  This element and all further sequential elements in the array are shifted
     *                  up one index value.
     */

    protected void shiftArrayRight(int index)
    {
        if (numPoints == 0) return;
        
        for (int i=numPoints-1; i>=index; i--)
            set[i+1]=set[i];
        
        numPoints++;
    }


    /*=================================================
     *
     * METHODS TO SUPPORT DEFUZZIFICATION OF FUZZY SETS
     *
     *================================================*/
 
    /**
     * This class is used by the momentDefuzzify method, creating
     * instances so that both a moment and area can be returned from
     * the getMomentAndArea method.
     */
    class MomentAndArea 
    {
        double moment;
        double area;
    }
     
     /**
      *  Moment defuzzification defuzzifies a fuzzy set returning a
      *  floating point (double value) that represents the fuzzy set.
      *
      *  It calculates the first moment of area of a fuzzy set about the
      *  y axis.  The set is subdivided into different shapes by partitioning
      *  vertically at each point in the set, resulting in rectangles, 
      *  triangles, and trapezoids.  The centre of gravity (moment) and area of
      *  each subdivision is calculated using the appropriate formulas
      *  for each shape.  The first moment of area of the whole set is
      *  then:
      *  <b><pre>
      *      sum of ( moment(i) * area(i) )          <-- top
      *      ------------------------------
      *          sum of (area(i))                    <-- bottom
      *  </pre></b>
      *  If the total area is 0 then throw an exception since the moment
      *  is not defined.
      *
      * @param xMin minimum x value to use when performing defuzzification
      * @param xMax maximum x value to use when performing defuzzification
      * @return the floating value that is the first moment of the fuzzy set
      *
      * @exception InvalidDefuzzifyException there is no valid moment for the fuzzy set
      *                 (generally the area is 0 under the fuzzy set graph)
      * @exception XValuesOutOfOrderException occurs when the xMin parameter is
      *                 greater than or equal to the xMax parameter.
      */   
    public double momentDefuzzify(double xMin, double xMax)
        throws InvalidDefuzzifyException, XValuesOutOfOrderException
    {
        double result = 0.0;
    	
        if (numPoints == 0)
            throw new InvalidDefuzzifyException("The fuzzy set had no points");
            
        double sumOfMomentsTimesAreasAndSumOfAreas[] = 
                calulateSumOfMomentsTimesAreasAndSumOfAreas(xMin, xMax);
                
        double sumOfAreas = sumOfMomentsTimesAreasAndSumOfAreas[1];
        
        if (sumOfAreas == 0.0)
           throw new InvalidDefuzzifyException("The area of the fuzzy set was 0");
           
        return sumOfMomentsTimesAreasAndSumOfAreas[0]/sumOfAreas;
    }
    
    
    /**
     *  Finds the sum of the moments time the areas and the sum of the areas
     *  for the areas bounded by the pairs of points in a FuzzySet. 
     *  This is used by the methods that need to find the moment for a FuzzySet 
     *  (see momentDefuzzify in this class) or a collection of FuzzySets 
     *  (see momentDefuzzify in the FuzyValueVector class).
     *
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return a double array of size two with the first element holding the 
     *          sum of the moments * the areas and the second holding the
     *          sum of the areas.
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     */

    double[] calulateSumOfMomentsTimesAreasAndSumOfAreas(double xMin, double xMax)
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
    	int i;
        double localMoment, localArea;
        double currentx, currenty, nextx, nexty;
        double top = 0.0, bottom = 0.0;
        FuzzySet fs = this;
        double topAndBottom[] = {0, 0};
        
        if (xMin >= xMax)
            throw new XValuesOutOfOrderException(xMin, xMax);

        if (numPoints == 0)
            return topAndBottom;
            
        // if xMin is > lowest X value OR xMax is < highest x value
        // we must constrict the fuzzy set to lie within xMin and xMax.
        // Create a new fuzzySet (copy) and restrict the x values of it ..
        // don't modify this FuzzySet since user expects it to stay the same!
        if (xMin > set[0].x || xMax < set[numPoints-1].x)
        {
            fs = new FuzzySet(this);
            try 
            { fs.confineToXBounds(xMin, xMax);
            }
            catch (XValuesOutOfOrderException e)
            { throw e; 
            }
        }
        
        /**********************************************
         Start calculating the c.o.g.
         **********************************************/

        if ( fs.numPoints <= 1 )  /* single point is constant fuzzy set over universe */
          {
            /* if no points in the set (should NEVER happen??)
               or the y-value of the single point is 0
               then the area is zero -- invalid moment
            */
            if (fs.numPoints < 1 || fs.set[0].y == 0.0)
               throw new InvalidDefuzzifyException("The area of the fuzzy set was 0 or the fuzzy set had no points");
               
            localArea = (xMax - xMin) * fs.set[0].y;
            localMoment = 0.5 * (xMax + xMin);
            topAndBottom[0] = localMoment;
            topAndBottom[1] = localArea;
            return topAndBottom;
          }
        else
          {
            currentx = fs.set[0].x;
            currenty = fs.set[0].y;

            /* Check for open-ended set & add initial rectangle if needed */
            if ( currenty != 0.0 && currentx != xMin )    
              {
                localMoment = 0.5 * (currentx+xMin);
                localArea = (currentx - xMin) * currenty;
                top += localMoment * localArea;
                bottom += localArea;    
              }

            for ( i = 1; i < fs.numPoints; i++ )
              {
                nextx = fs.set[i].x;
                nexty = fs.set[i].y;

                MomentAndArea ma = getMomentAndArea ( currentx, currenty, nextx, nexty );
                top += ma.moment * ma.area;
                bottom += ma.area;
                currentx = nextx;
                currenty = nexty;
              }
        
            /* Check for open-ended set & add final rectangle if needed */
            if ( currenty != 0.0 && currentx < xMax )
              {
                localMoment = 0.5 * (currentx + xMax);
                localArea = (xMax - currentx) * currenty;
                top += localMoment * localArea;
                bottom += localArea;        
              }
          
            /*******************
            Set the final result
            ********************/
            topAndBottom[0] = top;
            topAndBottom[1] = bottom;
          }

       return topAndBottom;

    }

    /**
     *  Given a polygon defined by the vertices (x1, 0), (x1, y1),
     *  (x2, y2), (x2, 0), find the first moment of area of the polygon 
     *  and the area of the polygon and return these in a MomentAndArea instance.
     *  <br><br>
     *  Conditions assumed: x2 > x1.
     *
     * @param x1 x value of 1st point in line segment
     * @param y1 y value of 1st point in line segment
     * @param x2 x value of 2nd point in line segment
     * @param y2 y value of 2nd point in line segment
     * @result MomentAnDArea instance that holds moment and area of the polygon
     */
    private MomentAndArea getMomentAndArea ( double x1, double y1, double x2, double y2 )
    {
        MomentAndArea ma = new MomentAndArea();
        
        /* rectangle of zero height or zero width? */
        if (( y1 == 0.0 && y2 == 0.0 ) ||
            ( x1 == x2)
           )
          {
            ma.moment = 0.0;
            ma.area = 0.0;
          }
        else if ( y1 == y2 )   /* rectangle */
          {
            ma.moment = 0.5 * ( x1 + x2 );
            ma.area = ( x2 - x1 ) * y1;
          }
        else if ( y1 == 0.0 && y2 != 0.0 )    /* triangle, height y2 */
          {
            ma.moment = (2.0/3.0) * ( x2 - x1 ) + x1;
            ma.area = 0.5 * ( x2 - x1 ) * y2;
          }
        else if ( y2 == 0.0 && y1 != 0.0 )    /* triangle, height y1 */
          {
            ma.moment = (1.0/3.0) * ( x2 - x1 ) + x1;
            ma.area = 0.5 * (x2 - x1 ) * y1;
          }
        else    /* trapezoid */
          {
            ma.moment = ( (2.0/3.0) * (x2-x1) * (y2+0.5*y1))/(y1+y2) + x1;
            ma.area = 0.5 * ( x2 - x1 ) * ( y1 + y2 );
          }
        
        return ma;
    }

    
    /**
     *  Center of Area (COA) defuzzification defuzzifies a fuzzy set returning a
     *  floating point (double value) that represents the fuzzy set.
     *
     *  It calculates the x value that spilts the fuzzy set so that there
     *  is an equal area on either side of the x value.
     *  The set is subdivided into different shapes by partitioning
     *  vertically at each point in the set, resulting in rectangles, 
     *  triangles, and trapezoids.  
     *  <br>
     *  In some cases where the fuzzy set has 2 equal areas separated by
     *  a range of x values with 0 membership values (i.e. area is 0) then
     *  the center of area x value could be any value from the end of the non-zero
     *  area on the left and the beginning of the non-zero area on the right.
     *  In this case we return the middle of this span of x values with 0 membership
     *  values. For example the fuzzy set defined by the points:
     *  <br><pre>
     *     (5,0) (6,1) (7,0) (15,0) (16,1) (17,0)
     *  </pre><br>
     *  could have a COA of any value from 7.0 to 15.0 since the 2 triangles
     *  lie on either side of 7.0 and 15.0. We will return a value of 11.0 in this case.
     *  <br>
     *  If the total area is 0 then throw an exception since the center
     *  of area is not defined.
     *
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return the floating value that is the center of area (COA) of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there is no valid COA for the fuzzy set
     *                 (generally the area is 0 under the fuzzy set graph)
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *                 greater than or equal to the xMax parameter.
     */   
   public double centerOfAreaDefuzzify(double xMin, double xMax)
       throws InvalidDefuzzifyException, XValuesOutOfOrderException
   {
   	int i, j;
    double area, previousArea, totalArea = 0.0, halfOfArea;
    double currentx, currenty, nextx, nexty;
    FuzzySet fs = this;
   	
    if (numPoints == 0)
        throw new InvalidDefuzzifyException("The fuzzy set had no points");
       
    if (xMin >= xMax)
       throw new XValuesOutOfOrderException(xMin, xMax);
        
    // if xMin is > lowest X value OR xMax is < highest x value
    // we must constrict the fuzzy set to lie within xMin and xMax.
    // Create a new fuzzySet (copy) and restrict the x values of it ..
    // don't modify this FuzzySet since user expects it to stay the same!
    if (xMin > set[0].x || xMax < set[numPoints-1].x)
    {
        fs = new FuzzySet(this);
        try 
        { fs.confineToXBounds(xMin, xMax);}
        catch (XValuesOutOfOrderException e)
        { throw e; }
    }
    /**********************************************
     Start calculating the c.o.a.
     **********************************************/
    if ( fs.numPoints <= 1 )  /* single point is constant fuzzy set over universe */
    { // if no points in the set (should NEVER happen??) or the y-value of the 
      // single point is 0 then the area is zero -- invalid defuzzify
      if (fs.numPoints < 1 || fs.set[0].y == 0.0)
         throw new InvalidDefuzzifyException("The area of the fuzzy set was 0 or the fuzzy set had no points");
      // center of area is in the middle of xMin and xMax  
      return ((xMax - xMin) * 0.5);
    }

    // find total area of fuzzy set
    currentx = fs.set[0].x;
    currenty = fs.set[0].y;

    /* Check for open-ended set & add initial rectangle if needed */
    if ( currenty != 0.0 && currentx != xMin )    
      totalArea = (currentx - xMin) * currenty;

    for (i=1; i<fs.numPoints; i++ )
    { nextx = fs.set[i].x;
      nexty = fs.set[i].y;
      totalArea += getPolygonArea( currentx, currenty, nextx, nexty );
      currentx = nextx;
      currenty = nexty;
    }
    
    /* Check for open-ended set & add final rectangle if needed */
    if ( currenty != 0.0 && currentx < xMax )
      totalArea += (xMax - currentx) * currenty;
      
    if (totalArea == 0.0)
       throw new InvalidDefuzzifyException("The area of the fuzzy set was 0");
       
    // now find the x value where the area is half on one side and half on the other 
    halfOfArea = totalArea*0.5;
    totalArea = previousArea = 0.0;
    currentx = fs.set[0].x;
    currenty = fs.set[0].y;
    /* Check for open-ended set & add initial rectangle if needed */
    if ( currenty != 0.0 && currentx != xMin )    
      totalArea = (currentx - xMin) * currenty;
    // if this area takes us over the 1/2 mark then find x value within it where 
    // the 1/2 mark occurs
    if (totalArea>halfOfArea)
    	return findXatCOA(halfOfArea-previousArea, xMin, currenty, currentx, currenty);
    //special case where the end of this rectangle has exactly 1/2 of the area
    // before it. Could just return this x value (currentx) but it is
    // (perhaps) better to look ahead to see if any shapes of 0 area follow
    // this one and if so return the average of the end of the rectangle
    // x value and the beginning x value of the 1st non-zero area following it.
    if (totalArea == halfOfArea)
    { double startx = currentx;
      for (i=1; i<fs.numPoints; i++)
      { nextx = fs.set[i].x;
        nexty = fs.set[i].y;
        if (getPolygonArea( currentx, currenty, nextx, nexty ) != 0.0)
           return ((currentx+startx)*0.5);
        currentx = nextx;
        currenty = nexty;
      }
      // get here when only the open-end final rectangle is left and it
      // must have 1/2 of the area
      return((currentx+startx)*0.5);
    }

    for ( i = 1; i < fs.numPoints; i++ )
    { previousArea = totalArea;
      nextx = fs.set[i].x;
      nexty = fs.set[i].y;
      totalArea += getPolygonArea( currentx, currenty, nextx, nexty );
      // if this area takes us over the 1/2 mark then find x value within it where 
      // the 1/2 mark occurs
      if (totalArea>halfOfArea)
    	return findXatCOA(halfOfArea-previousArea, currentx, currenty, nextx, nexty);
      //special case where the end of this current shape has exactly 1/2 of the area
      // before it. Could just return this x value (currentx) but it is
      // (perhaps) better to look ahead to see if any shapes of 0 area follow
      // this one and if so return the average of the end of the current shape
      // x value and the beginning x value of the 1st non-zero area following it.
      if (totalArea == halfOfArea)
      { double startx = nextx;
        for (j=i+1; j<fs.numPoints; j++)
        { currentx = nextx;
          currenty = nexty;
          nextx = fs.set[j].x;
          nexty = fs.set[j].y;
          if (getPolygonArea( currentx, currenty, nextx, nexty ) != 0.0)
             return ((currentx+startx)*0.5);
        }
        // get here when only the open-end final rectangle is left and it
        // must have 1/2 of the area
        return((currentx+startx)*0.5);
      }
      currentx = nextx;
      currenty = nexty;
    }
    
    /* Check for open-ended set & add final rectangle if needed */
    previousArea = totalArea;
    if ( currenty != 0.0 && currentx < xMax )
    {  totalArea = (xMax - currentx) * currenty;
       if (totalArea>=halfOfArea)
    	  return findXatCOA(halfOfArea-previousArea, currentx, currenty, xMax, currenty);
    }
    
    // shouldn't ever get here but ...
    throw new InvalidDefuzzifyException("Could not find Center of Area .. probable bug in centerOfAreaDefuzzify method");
   }
   
   /**
    *  Given a polygon defined by the vertices (x1, 0), (x1, y1),
    *  (x2, y2), (x2, 0), find the area of the polygon 
    *  <br><br>
    *  Conditions assumed: x2 > x1.
    *
    * @param x1 x value of 1st point in line segment
    * @param y1 y value of 1st point in line segment
    * @param x2 x value of 2nd point in line segment
    * @param y2 y value of 2nd point in line segment
    * @result area of the polygon
    */
   private double getPolygonArea( double x1, double y1, double x2, double y2 )
   {
       if ( y1 == y2 )   /* rectangle */
          return (( x2 - x1 ) * y1);

       /* else a triangle or trapezoid -- same formula for both since
        * triangle just a special case of a trapezoid with y1 or y2 = 0 */
       return (0.5 * ( x2 - x1 ) * ( y1 + y2 ));
   }

   /**
    *  Given a polygon defined by the vertices (x1, 0), (x1, y1),
    *  (x2, y2), (x2, 0), find the x value between x1 and x2 where
    *  the area of the polygon from x1 to x is a specified value 
    *  <br><br>
    *  Conditions assumed: x2 > x1; area of the polygon is >= the
    *  specified area
    *
    * @param area we are looking for
    * @param x1 x value of 1st point in line segment
    * @param y1 y value of 1st point in line segment
    * @param x2 x value of 2nd point in line segment
    * @param y2 y value of 2nd point in line segment
    * @result required x value
    */
   private double findXatCOA( double area, double x1, double y1, double x2, double y2 )
   {
   	 double root;
   	 
     if ( y1 == y2 )   /* rectangle */
     {  if (y1 == 0.0)
          return x1; // should NOT happen ...
       	else
       	  return (area/y1 + x1);
     }

     double x2minusx1 = x2 - x1;
     if ( y1 == 0.0 && y2 != 0.0 )    /* triangle, height y2 */
     {  root = Math.sqrt(2.0*area*x2minusx1/y2);
        return (x1 + root);
     }
     
     if ( y2 == 0.0 && y1 != 0.0 )    /* triangle, height y1 */
     {  root = Math.sqrt(x2minusx1*x2minusx1 - (2.0*area*x2minusx1/y1));
        return (x2 - root);
     }
     
     /* trapezoid */
     double m = (y2-y1)/x2minusx1;
     root = Math.sqrt(y1*y1 + 2.0*m*area);
     return (x1 - (y1 - root)/m);
   }

   /**
    *  Calculate the area of the fuzzy set (bounded by a minimum x value and
    *  a maximum x value). The bounds are required because the first and last
    *  y values of the set may not be 0.0 and the area would then be infinite.
    *  In this case the bounds are chosen to be the min and max x values of the
    *  fuzzy set.
    * <br>
    *  The set is subdivided into different shapes by partitioning
    *  vertically at each point in the set, resulting in rectangles, 
    *  triangles, and trapezoids.  
    *  <br>
    *
    * @return the area of the fuzzy set
    *
    */   
  public double getArea()
  {
	  double result = 0.0;
	  
	  try
	  { result = getArea(set[0].x, set[numPoints-1].x); }
	  catch (XValuesOutOfOrderException e)
	  {} // should never so this BUT the fuzzy set should have valid x order!!
	  
	  return result;
  }

   /**
    *  Calculate the area of the fuzzy set (bounded by a minimum x value and
    *  a maximum x value). The bounds are required because the first and last
    *  y values of the set may not be 0.0 and the area would then be infinite.
    * <br>
    *  The set is subdivided into different shapes by partitioning
    *  vertically at each point in the set, resulting in rectangles, 
    *  triangles, and trapezoids.  
    *  <br>
    *
    * @param xMin minimum x value to use when performing area calculation
    * @param xMax maximum x value to use when performing area calculation
    * @return the area of the fuzzy set
    *
    * @exception XValuesOutOfOrderException occurs when the xMin parameter is
    *                 greater than or equal to the xMax parameter.
    */   
  public double getArea(double xMin, double xMax)
      throws XValuesOutOfOrderException
  {
  	int i, j;
   double area, previousArea, totalArea = 0.0;
   double currentx, currenty, nextx, nexty;
   FuzzySet fs = this;
  	
   if (numPoints == 0)
       return( 0.0 );
      
   if (xMin >= xMax)
      throw new XValuesOutOfOrderException(xMin, xMax);
       
   // if xMin is > lowest X value OR xMax is < highest x value
   // we must constrict the fuzzy set to lie within xMin and xMax.
   // Create a new fuzzySet (copy) and restrict the x values of it ..
   // don't modify this FuzzySet since user expects it to stay the same!
   if (xMin > set[0].x || xMax < set[numPoints-1].x)
   {
       fs = new FuzzySet(this);
       try 
       { fs.confineToXBounds(xMin, xMax);}
       catch (XValuesOutOfOrderException e)
       { throw e; }
   }
   /**********************************************
    Start calculating the area
    **********************************************/
   // if no points in the set (should NEVER happen??) area is 0.0
   if (fs.numPoints < 1)
       return( 0.0 );
   if ( fs.numPoints == 1 )  /* single point is constant fuzzy set over universe */
   { // area is y value * (xmax - xmin)  
     return ((xMax - xMin) * fs.set[0].y);
   }

   // find total area of fuzzy set
   currentx = fs.set[0].x;
   currenty = fs.set[0].y;

   /* Check for open-ended set & add initial rectangle if needed */
   if ( currenty != 0.0 && currentx != xMin )    
     totalArea = (currentx - xMin) * currenty;

   for (i=1; i<fs.numPoints; i++ )
   { nextx = fs.set[i].x;
     nexty = fs.set[i].y;
     totalArea += getPolygonArea( currentx, currenty, nextx, nexty );
     currentx = nextx;
     currenty = nexty;
   }
   
   /* Check for open-ended set & add final rectangle if needed */
   if ( currenty != 0.0 && currentx < xMax )
     totalArea += (xMax - currentx) * currenty;
     
   return( totalArea );
  }
  



    /**
     *  Finds the mean of maxima of a fuzzy set as the defuzzification value.
     *   
     *  NOTE: This doesn't always work well because there can be x ranges
     *         where the y value is constant at the max value and other 
     *         places where the max is only reached for a single x value.
     *         When this happens the single value gets too much of a say
     *         in the defuzzified value. So use moment defuzzify in cases
     *         like this.
     *
     * <br><pre>
     *             /------\                   /\
     *            /        \                 /  \
     *         --/          \---------------/    \-------------
     *                 ^       ^
     *                 |       |
     *                 |       | gives this as the mean of maximum
     *                 | this is more reasonable???
     *
     * </pre>
     * Centre of gravity (moment) defuzzify is likely more useful most of the time.
     *       
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return the floating value that is the first average of the maximum 
     *          values of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *             greater than or equal to the xMax parameter.
     */
     
    public double maximumDefuzzify(double xMin, double xMax)
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
    	double maxYandSumOfXandNumberOfX[] =
    	   calculateMaxYandSumOfXandNumberOfX(xMin, xMax);
    	   
    	double numberOfX = maxYandSumOfXandNumberOfX[2];
        if (numberOfX == 0)
            throw new InvalidDefuzzifyException("The FuzzySet had no points");
            
        double sumOfX = maxYandSumOfXandNumberOfX[1];
        
        return sumOfX/numberOfX;
    }
    
    
    
    /**
     *  Supports finding the mean of the maxima of a FuzzySet by determining the 
     *  maximum y (membership) value in the FuzzySet,the sum of the x values 
     *  for all points at the maximum Y value, and the number of x values 
     *  with that maximum y value. Used by the method maximumDefuzzify in both this
     *  class and the FuzzyValueVector class.
     * 
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return an array containing the maximum y (membership) value in the FuzzySet, 
     *          the sum of the x values for all points at the maximum Y value, and
     * *        the number of x values with the maximum y value.
     *
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *                 greater than or equal to the xMax parameter.
     */
    
    double[] calculateMaxYandSumOfXandNumberOfX(double xMin, double xMax)
        throws XValuesOutOfOrderException
    {
        int i, count;
        double maxy, sum;
        FuzzySet fs = this;
        double maxYandSumOfXandNumberOfX[] = {0.0, 0.0, 0.0};
        
        if (xMin >= xMax)
            throw new XValuesOutOfOrderException(xMin, xMax);
            
        if (numPoints == 0)
            return maxYandSumOfXandNumberOfX;
            
        // if xMin is > lowest X value OR xMax is < highest x value
        // we must constrict the fuzzy set to lie within xMin and xMax.
        // Create a new fuzzySet (copy) and restrict the x values of it ..
        // don't modify this FuzzySet since user expects it to stay the same!
        if (xMin > set[0].x || xMax < set[numPoints-1].x)
        {
            fs = new FuzzySet(this);
            try 
            { fs.confineToXBounds(xMin, xMax);
            }
            catch (XValuesOutOfOrderException e)
            { throw e; 
            }
        }
        
        /******************************************
            Find Mean of Maxima
         ******************************************/
         
        // find max y value over the range 
        maxy = 0.0;
        for (i=0; i < fs.numPoints; i++ )
            if ( fs.set[i].y > maxy )
                maxy = fs.set[i].y;

        count = 0;
        sum = 0.0;

        // Check for a zero max value or a single point in the set 
        // In this case just use the extreme x values of the FuzzySet
        if (maxy == 0.0 || fs.numPoints == 1)
          { count = 2;
            sum = xMax + xMin;
          } 
        else /* Set has at least two points */
          { /* check for maximum at beginning of open-ended set */
            if ( fs.set[0].y == maxy)
              { sum += xMin;
                count++;
                if ( fs.set[0].x != xMin && fs.set[1].y != maxy )
                  { sum += fs.set[0].x;
                    count++;
                  }
              }
              
            for ( i = 1; i < fs.numPoints - 1; i++ )
              { if ( fs.set[i].y == maxy )
                  { if ( fs.set[i-1].y != maxy || fs.set[i+1].y != maxy )
                      { sum += fs.set[i].x;
                        count++;
                      }
                  }
              }
              
            /* check for maximum at end of open-ended set */
            if ( fs.set[numPoints-1].y == maxy )
              { if ( fs.set[fs.numPoints-1].x != xMax && fs.set[fs.numPoints-2].y != maxy )
                  { sum += fs.set[fs.numPoints-1].x;
                    count++;
                  }
                sum += xMax;
                count++;
              }
          }
          
       maxYandSumOfXandNumberOfX[0] = maxy;        
       maxYandSumOfXandNumberOfX[1] = sum;        
       maxYandSumOfXandNumberOfX[2] = count; 
              
       return maxYandSumOfXandNumberOfX;
    }

    /**
     *  Finds the weighted average of the values of a fuzzy set as the 
     *  defuzzification value. This is slightly different than the 
     *  maximumDefuzzify since the maximumDefuzzify uses only points that
     *  have the same membership value (the one that is the maximum in 
     *  the set of points). The weightedAverageDefuzzify uses all of the 
     *  points with non-zero membership values and calculates the average 
     *  of the x values using the membership values as weights in this average.
     * 
     *  NOTE: This doesn't always work well because there can be x ranges
     *         where the y value is constant at the max value and other 
     *         places where the max is only reached for a single x value.
     *         When this happens the single value gets too much of a say
     *         in the defuzzified value. So use moment defuzzify in cases
     *         like this.
     *
     * <br><pre>
     *             /------\                   /\
     *            /        \                 /  \
     *         --/          \---------------/    \-------------
     *                 ^       ^
     *                 |       |
     *                 |       | gives this as the weighted average
     *                 | this is more reasonable???
     *
     * <br></pre>
     * However, it is especially effective if the output has a number of singleton
     * points in it. This might be, for example, when one has only singleton
     * values describing the output fuzzy values (Takagi-Sugeno-Kang zero order type rules).
     * In this case one gets a result that would be expected and one that would 
     * result in an exception with the momentDefuzzify (area is 0) and a poor 
     * result with the maximumDefuzzify (since only the values with the max 
     * membership are used - 20 in the example below).
     *
     * <br><pre>
     *    
     *      1.0                 |
     *                          |
     *      0.5        |        |
     *                 |        |        |
     *         --------|--------|--------|--------
     *         0      10        20       30      40
     *                        ^
     *                        |
     *                        | gives 18.57 as the weighted average   
     * 
     *      (10*0.5 + 20*1 + 30*0.25) / (0.5 + 1 + 0.25) = 18.57
     *
     * <br></pre>
     * Centre of gravity (moment) defuzzify is likely more useful most of the time.
     *       
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return the floating value that is the weighted average of the fuzzy set
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     */
     
    public double weightedAverageDefuzzify(double xMin, double xMax)
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
    	double sums[] = calulateSumOfWeightsAndSumOfWeightsTimesXvals(xMin, xMax);
        double sumOfWeights = sums[1];
        double sumOfWeightsTimesXvals = sums[0];
        
        if (sumOfWeights == 0.0)
           throw new InvalidDefuzzifyException("The fuzzy set had no points with membership value > 0.0");
        
        return sumOfWeightsTimesXvals/sumOfWeights;    	
    }

    /**
     *  Finds the sum of the weights (membership values) and the sum of the
     *  weights * the x values for a fuzzy set. This is used by the methods that
     *  need to find the weighted average for a FuzzySet (see weightedAverageDefuzzify
     *  in this class) or a collection of FuzzySets (see weightedAverageDefuzzify in the
     *  FuzyValueVector class).
     *
     * @param xMin minimum x value to use when performing defuzzification
     * @param xMax maximum x value to use when performing defuzzification
     * @return a double array of size two with the first element holding the 
     *          sum of the weights * the x values and the second holding the
     *          sum of the weights (membership values).
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     */

    double[] calulateSumOfWeightsAndSumOfWeightsTimesXvals(double xMin, double xMax)
        throws XValuesOutOfOrderException, InvalidDefuzzifyException
    {
        int i;
        double sumOfWeights = 0.0;
        double sumOfWeightsTimesXvals = 0.0;
        double sumOfWeightsAndSumOfWeightsTimesXvals[] = new double[2];
        FuzzySet fs = this;
        
        if (xMin >= xMax)
            throw new XValuesOutOfOrderException(xMin, xMax);
            
        if (numPoints == 0)
            throw new InvalidDefuzzifyException("The fuzzy set had no points");
            
        // if xMin is > lowest X value OR xMax is < highest x value
        // we must constrict the fuzzy set to lie within xMin and xMax.
        // Create a new fuzzySet (copy) and restrict the x values of it ..
        // don't modify this FuzzySet since user expects it to stay the same!
        if (xMin > set[0].x || xMax < set[numPoints-1].x)
        {
            fs = new FuzzySet(this);
            try 
            { fs.confineToXBounds(xMin, xMax);
            }
            catch (XValuesOutOfOrderException e)
            { throw e; 
            }
        }
        /******************************************
            Find Weighted Average
         ******************************************/
        for (i=0; i < fs.numPoints; i++ )
        {   double yVal = fs.set[i].y;
        	if ( yVal > 0.0 )
            {   sumOfWeights += yVal;
            	sumOfWeightsTimesXvals += yVal * fs.set[i].x;
            }
        }
        
        sumOfWeightsAndSumOfWeightsTimesXvals[0] = sumOfWeightsTimesXvals;
        sumOfWeightsAndSumOfWeightsTimesXvals[1] = sumOfWeights;
        return sumOfWeightsAndSumOfWeightsTimesXvals;
    }



     

    /*======================================================
     *
     * METHODS TO SUPPORT THE PLOTTING OF FUZZY SETS
     *
     *====================================================*/



  private static final int NROWS = 20; /* number of rows in the plot */
  private static final int NCOLS = 50; /* number of columns in the plot */
  private static final int NTICKS = 5; /* number of ticks per division in x axis */

  /**     
   * Plots the fuzzy set in an ascii format producing a String that can be displayed
   * The string must be displayed using a non-proportional spaced font. 
   *
   * @param plotchar  character to use for plotting (normally *, +, ., etc)
   * @param low       lower x value in plot
   * @param high      upper x value in plot
   * 
   */
  public String plotFuzzySet( String plotChar, double lowX, double highX)
  {
    FuzzySet fsets[] = new FuzzySet[1];
    fsets[0] = this;
    return FuzzySet.plotFuzzySets(plotChar, lowX, highX, fsets);
  }

  /**
   * Calculate an index for the proper row in the plot given a y value
   * and a scaling factor
   *
   * @param y       the y value for which we need a row index
   * @param yscale  the scaling factor
   */

  private static int getYindex( double y, double yscale)
  {
     int j = (int)Math.round(y*yscale*NROWS);
     if (j > NROWS) j = NROWS;
     else if (j < 0) j = 0;
     return j;
  }

  /**     
   * Plots the fuzzy sets in an ascii format producing a String that can be displayed
   * The string must be displayed using a non-proportional spaced font such as courier. 
   *
   * @param plotchars character(s) to use for plotting (normally *, +, ., etc)
   * @param low       lower x value in plot
   * @param high      upper x value in plot
   * @param fSets     array of fuzzy sets to plot the single graph
   * 
   */
  public static String plotFuzzySets( String plotChars, double lowX, double highX, FuzzySet fSets[] )
  {
   int i, j, k, m, lastk = 0, previousLastk, n;
   char thePlotChar = '*'; // default plotting char
   FuzzySet fs;
   StringBuffer plotRows[] = new StringBuffer[NROWS+1];
   StringBuffer result = new StringBuffer(500);
   double range;
   double fsx[], fsy[], y;
   double thisx = 0.0, thisy = 0.0, lastx = 0.0, lasty = 0.0;
   double nextx = 0.0, nexty = 0.0, previousThisy = 0.0;
   double miny, maxy;
   double maxYinFuzzySets = 0.0, yscale;
   int numPlotchars;
   int fsNum;

   if (fSets.length < 1)
      return "";

   numPlotchars = plotChars.length();
 
   for (i=0; i <= NROWS; i++)
       plotRows[i] = new StringBuffer(NCOLS+2);

   if (lowX == highX)
     { System.err.println("plot low limit = high limit, no plot possible\n");
       return "";
     }
   if (lowX >= highX)
     { System.err.println("plot low limit > high limit, interchanging values\n");
       double temp = lowX;
       lowX = highX; highX = temp;
     }

	range = highX - lowX;

   /*============================================================*/
   /* Set up to Do the plotting                                  */
   /*============================================================*/

   for (i=0; i<=NROWS; i++)
      { StringBuffer s = plotRows[i];
        for (j=0; j<NCOLS+1; j++)
           s.append(' ');
        s.append('\n');
      }

   // Determine the max Y value in all of the fuzzy sets to be plotted.
   // Need this so we can determine the Y range for the plotting space.
   // We'll also use it to calculate the Y scaling factor .. we want
   // the Y range to be from 0 to an integral value (remember that
   // y values (membership values) may not always be between 0 and 1.
   for (fsNum = 0; fsNum < fSets.length; fsNum++)
   {  fs = fSets[fsNum];
      double yTmp = fs.getMaxY();
      if (maxYinFuzzySets < yTmp) maxYinFuzzySets = yTmp;
   }
   yscale = 1.0/Math.ceil(maxYinFuzzySets);
   
   /*============================================================*/
   /* Get the fuzzy sets() and do the plotting                   */
   /*============================================================*/

   for (fsNum = 0; fsNum < fSets.length; fsNum++)
     {
      fs = fSets[fsNum];
      
      if (numPlotchars > fsNum) 
         thePlotChar = plotChars.charAt(fsNum);
 
      /* make arrays with point added to beginning and end to 
         capture the extreme values of the plot (lowX and HighX)
         if necessary
      */
      n = fs.size();
      if (n < 1) break; // if empty fuzzy set nothing to plot
      
      if (fs.set[0].x > lowX) 
         { n++; i = 1; }
      else
         { i = 0; }

      if (fs.set[fs.size()-1].x < highX) n++;

      fsx = new double[n];
      fsy = new double[n];

      if (fs.set[0].x > lowX) 
        { fsx[0] = lowX;
          fsy[0] = fs.set[0].y;
        }

      for (j=0; j < fs.size(); i++, j++)
        { fsx[i] = fs.set[j].x;  fsy[i] = fs.set[j].y;
        } 

      if (fs.set[fs.size()-1].x < highX)  
        { fsx[n-1] = highX;
          fsy[n-1] = fsy[n-2];
        }

      /* NOTE:  thisx, thisy  are current points -- at plotting points
                lastx, lasty  is 1st pt of line that crosses thisx 
                nextx, nexty  is end pt of line that crosses thisx 
      */

      thisx = lowX;
      previousLastk = 0;
      previousThisy = 0.0; 
      k = 0; 

      /* plot the points -- when a point is plotted it is
         necessary to go back over the 'actual' points in the array
         to see if any lie outside the y range of the point just plotted
         and the last one; if they do then plot them as well; if we
         don't do this then some points that 'fall between the cracks' will
         not show on the plot
      */
      while ((thisx-Parameters.FUZZY_TOLERANCE) <= highX && k <= NCOLS)
       {
        for (i=previousLastk; i<n-1; i++)
          {    
            lastx = fsx[i]; lasty = fsy[i];
            lastk = i;
            nextx = fsx[i+1]; nexty = fsy[i+1];
            if (Math.abs(thisx - lastx) < Parameters.FUZZY_TOLERANCE)
              { /* print points with same x values -- should have diff y values */
               thisy = fsy[lastk];
               j = getYindex(thisy, yscale);
               plotRows[NROWS-j].setCharAt(k, thePlotChar);
               if (Math.abs(thisx - nextx) >= Parameters.FUZZY_TOLERANCE)
                   break;
               else
                { /* we have a vertical line -- plot it */
                  m = getYindex(nexty, yscale);
                  if (m > j)
                     while (m > j)
                       { plotRows[NROWS-m].setCharAt(k, thePlotChar); m--; }
                  else
                     while (m < j)
                       { plotRows[NROWS-m].setCharAt(k, thePlotChar); m++; }
                }
              }
            else if (thisx > lastx && thisx < nextx - Parameters.FUZZY_TOLERANCE)
              break;
          }
        if (nextx == lastx) /* same x points -- just find min and max y values */
          {
            miny = thisy; maxy = thisy;
            if (previousThisy < miny) miny = previousThisy;
            if (nexty < miny) miny = nexty;
            if (previousThisy > maxy) previousThisy = maxy;
            if (nexty > maxy) maxy = nexty;
          }
        else
          {
           /* found the points that span this point - calc y value and plot it*/
           thisy = lasty + (thisx - lastx)*(nexty - lasty)/(nextx - lastx);
           j = getYindex(thisy, yscale);
           plotRows[NROWS-j].setCharAt(k, thePlotChar);
       
           /* now plot any points that between this point and the previous this
              point that lie outside of the y range of these 2 points OR
              are at same x location (vertical line)
           */
           if (thisy < previousThisy)
             { miny = thisy; maxy = previousThisy; }
           else
             { maxy = thisy; miny = previousThisy; }
	      }

        /* this second test (fsx[i] >= lowX) is included so that we 
           don't wander back before the start of the plot
        */
        for (i = lastk; i > previousLastk  && fsx[i] >= lowX; i--)
         {
          if (fsy[i] > maxy || fsy[i] < miny)
            {
              y = fsy[i];
              j = getYindex(y, yscale);
              plotRows[NROWS-j].setCharAt(k, thePlotChar);
            }
          if (fsx[i] == fsx[i-1])
            { /* we have a straight line plot it! */
              int j1, j2;
              j1 = getYindex(fsy[i], yscale);
              j2 = getYindex(fsy[i-1], yscale);
              if (j1 > j2)
                 while (j1 > j2)
                   { plotRows[NROWS-j1].setCharAt(k, thePlotChar); j1--; }
              else
                 while (j1 < j2)
                   { plotRows[NROWS-j1].setCharAt(k, thePlotChar); j1++; }
            }
         }  

        previousLastk = lastk;
        previousThisy = thisy;
        k++;
		thisx = lowX + (range * k)/(double)NCOLS;
       }  /* end of   while (thisx <= highX && k <= NCOLS)   */
     }  /* end of  for (fsNum = 0 ...    */

   NumberFormat nf = NumberFormat.getInstance();
   nf.setMaximumFractionDigits(2);
   nf.setMinimumFractionDigits(2);
   nf.setMinimumIntegerDigits(1);
   nf.setMaximumIntegerDigits(1);

   result.append("\n");
   double yscaleByNROWS = yscale*NROWS;
   for (i=NROWS; i>=0; i--)
      { result.append(' ' + nf.format((float)i /yscaleByNROWS));
        result.append(plotRows[NROWS-i]);
      }
   result.append("     ");
   for (i=0; i<NCOLS/NTICKS; i++)
      {
        result.append('|');
        for (j=1; j<NTICKS; j++)
            result.append('-');
      }
   result.append("|\n ");
   for (i=0; i<=NCOLS; i=i+2*NTICKS)
      {
        nf.setMaximumIntegerDigits(4);
        StringBuffer tmp = new StringBuffer();
        FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);
        nf.format(lowX+(range * i)/(double)NCOLS, tmp, fp);
        for (j=0; j < 4-fp.getEndIndex(); j++)
           result.append(' ');
        result.append(tmp);
        for (j=0; j<NTICKS*2-7; j++) 
            result.append(' ');
      }
      
    return result.toString();
  }
 
}
