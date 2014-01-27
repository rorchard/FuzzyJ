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

import java.lang.Math;
import java.io.*;

/**
 * The abstract <code>ModifierFunction</code> class along with the concrete
 * implementations of this class encapsulate the set of Modifiers (or Hedges)
 * available to operate on fuzzy values or fuzzy sets.  Each concrete 
 * modifier function class provides two 'call' methods that
 * accept as a parameter either a FuzzyValue or a FuzzySet. The method
 * creates a new FuzzyValue or FuzzySet who's value represents the application of the
 * linguistic modifier to the fuzzy set.
 *
 * <p>Many of the modifiers are applied by raising the membership values of the
 * fuzzy set points to a certain exponent.  For example, the modifier <code>very</code>
 * is implemented in this manner using the exponent 2.  However, consider
 * the case where the fuzzy set being modified consists of
 * {0.0/1 1.0/2 0.0/3}.  In this case, the membership values, at x=1, x=2 and x=3, do 
 * not change, regardless of the value of the exponent. 
 *
 * <p>However, if an additional point is inserted in this fuzzy set so that it now
 * consists of {0.0/1 0.5/2.5 1.0/2 0.0/3}, and the modifier <code>very</code> is
 * applied, the point that was originally 0.5/2.5 will now become 0.25/2.5.  If we
 * insert other additional points at arbitrary x values along the FuzzyValue, it will
 * be found that unless the membership value of these arbitrary points is 1.0 or 0.0, the
 * membership value will change as a result of the application of the exponent.
 *
 * <p>Because the representation of the membership function is a set of points that are
 * joined by straight lines, it may be necessary
 * to add additional points to the fuzzy set to enhance the
 * precision to the resulting fuzzy set.
 *
 * <p>The points added to the fuzzy set before the application of an
 * exponential modifier, and hence the precision of the result, can be controlled
 * by the user in three different ways.  The user can specify:
 *
 * <ul>
 * <li>the delta x between additional points by using the
 * <code>setDeltaXPrecision</code> method.  For example, if the user specifies
 * a delta x precision of 0.1, a new point will be added at approximately
 * every 0.1 x value increment between existing points.
 * <li>the delta y between additional points by using the
 * <code>setDeltaYPrecision</code> method.  Similar to delta x, a new
 * point will be added per specified y increment.
 * <li>the number of points, in total, to be in the final FuzzyValue
 * by using the <code>setNumberOfPointsPrecision</code> method.
 * </ul>
 *
 * <p>It is interesting to note that these precisions are, in fact, imprecise
 * themselves.  To accomodate each individual FuzzyValue and try to add points
 * with as even a spacing as possible, a specified delta x of 0.1 might be
 * functionally applied as 0.9555, or a specified 20 additional points to add
 * might be changed to 22 by the program.
 *
 * <p>Although the methods <code>setDeltaXPrecision</code>, <code>setDeltaYPrecision</code>,
 * and <code>setNumberOfPointsPrecision</code> determine the spacing for each of
 * the 3 possible fuzzy expanding techniques, only one of them is used at any time.
 * Which of these techniques is used is controlled by the method 
 * <code>setPrecisionControlType</code>. It takes as an argument one of the following
 * constant values: DELTA_X, DELTA_Y, or NUMBER_OF_POINTS.
 *
 * <p>The following is a visual guide to the built-in (system supplied) 
 * modifiers available to users.
 *
 * <br><br>
 * <table width=640 align=CENTER border=1>
 * <caption><b>A Visual Guide To Modifiers</b></caption>
 *
 *      <tr>
 *          <th>Modifiers</th>
 *          <th>Visual Representation</th>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td width=300>Unmodified:
 *              <ul>
 *              <li>this FuzzyValue has not yet been modified.  It will be the
 *                  FuzzyValue to which the modifiers are applied for all of the
 *                  following examples except 'norm'.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/basicFuzzyValue.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Not:
 *              <ul>
 *              <li>the <code>NOT</code> modifier returns the complement of the
 *                  FuzzyValue passed as its argument.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/not.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>

 *      <tr>
 *          <td valign=TOP>Norm:
 *              <ul>
 *              <li>the <code>NORM</code> modifier returns the normalized FuzzyValue.
 *                  Normalizing the FuzzyValue refers to scaling it so that at least
 *                  one point on the FuzzyValue has a membership value of 1.0.
 *                  FuzzyValue passed as its argument.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <center><b>Before Normalization</b></center>
 *              <br><img src = "NetGraphics/not_normal.gif">
 *              <center><b>After Normalization</b></center>
 *              <br><img src = "NetGraphics/normal.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>More_Or_Less:
 *              <ul>
 *              <li>the <code>MORE_OR_LESS</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having raised all the
 *                  membership values of the FuzzyValue by a factor of 1/3.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src="NetGraphics/more_or_less.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Somewhat:
 *              <ul>
 *              <li>the <code>SOMEWHAT</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having raised all the
 *                  membership values of the FuzzyValue by a factor of 1/2.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/somewhat.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Plus:
 *              <ul>
 *              <li>the <code>PLUS</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having raised all the
 *                  membership values of the FuzzyValue by a factor of 1.25.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/plus.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Very:
 *              <ul>
 *              <li>the <code>VERY</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having raised all the
 *                  membership values of the FuzzyValue by a factor of 2.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/very.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Extremely:
 *              <ul>
 *              <li>the <code>EXTREMELY</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having raised all the
 *                  membership values of the FuzzyValue by a factor of 3.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/extremely.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Slightly:
 *              <ul>
 *              <li>the <code>SLIGHTLY</code> modifier returns the expanded
 *                  FuzzyValue A passed as its argument, having performed
 *                  the following modifications on it:
 *                  <ul>
 *                  <li>intensify [ norm (plus A AND not very A) ]
 *                  </ul>
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/slightly.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2>
 *                  Consider the partial breakdown, shown below, of this
 *                  example.  On the left, directly below, is shown this
 *                  FuzzyValue after applying the <code>plus</code> modifier.
 *                  On the right is this FuzzyValue after applying both the
 *                  <code>not</code> and the <code>very</code> modifiers.
 *                  Below these is depicted <code>plus FuzzyValue AND not
 *                  very FuzzyValue</code>, showing the portion of the FuzzyValue
 *                  that is selected which intuitively matches our linguistic
 *                  understanding of the word "slightly".
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td align=MIDDLE>PLUS</td>
 *          <td align=MIDDLE>NOT VERY</td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE valign=TOP height=250>
 *              <img src="NetGraphics/plus_1.gif">
 *              <img src="NetGraphics/not_very_1.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE>
 *              Now, put them together.
 *              <br>PLUS FuzzyValue AND NOT VERY FuzzyValue
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 align=MIDDLE valign=TOP height=250>
 *              <img src="NetGraphics/plus_1_and_not_very_1.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Intensify:
 *              <ul>
 *              <li>the <code>INTENSIFY</code> modifier returns the expanded
 *                  FuzzyValue passed as its argument, having having performed
 *                  the following modifications on it:
 *                      <ul>
 *                      <li>if (0.0 <= y <= 0.5), y = 2*y<sup>2</sup>
 *                      <li>if (0.5 <  y <= 1.0), y = 1 - 2*(1-y)<sup>2</sup>
 *                      </ul>
 *                  The has the effect of emphasizing the sections of the FuzzyValue
 *                  that have a membership value greater than 0.5, and understating
 *                  any sections of the FuzzyValue with a membership value less than
 *                  or equal to 0.5.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/intensify.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Above:
 *              <ul>
 *              <li>the <code>ABOVE</code> modifier identifies the first x value
 *                  at which the maximum value is reached. All membership values
 *                  below this point are set to zero and all membership values above
 *                  this value are set to 1-y. For convex fuzzy sets this gives 
 *                  an intuitive result.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/above.gif">
 *          </td>
 *      </tr>
 *
 *      <tr>
 *          <td colspan=2 height=40><hr></td>
 *      </tr>
 *
 *      <tr>
 *          <td>Below:
 *              <ul>
 *              <li>the <code>BELOW</code> modifier identifies the first x value
 *                  at which the maximum value is reached. All membership values
 *                  above this point are set to zero and all membership values below
 *                  this value are set to 1-y. For convex fuzzy sets this gives 
 *                  an intuitive result.
 *              </ul>
 *          </td>
 *          <td width=340 align=RIGHT valign=MIDDLE>
 *              <img src = "NetGraphics/below.gif">
 *          </td>
 *      </tr>
 * </table>
 * <p>Each individual fuzzy modifier is created as a subclass of the
 * ModifierFunction class. If created according to the instructions provided
 * these fuzzy modifer classes when Constructed will automatically call the
 * Modifiers.add method to add themselves to the list of available modifiers.
 * So the preferred way to add new modifiers is:
 *
 * <ul>
 * <li> Create the modifier class
 * <pre><code>
 *      public class VeryModifier extends ModifierFunction
 *      {
 *        public VeryModifier() // use a default name for the modifier
 *        {
 *          // required to set the name of the modifier 
 *          // and add to list in Modifiers class
 *          super( "very" ); 
 *        }
 *
 *        public VeryModifier(String s) // use a supplied name for the modifier
 *        {
 *          // required to set the name of the modifier 
 *          // and add to list in Modifiers class
 *          super( s );
 *        }
 *
 *        public FuzzyValue call(FuzzyValue fv)
 *        {
 *          FuzzyValue fvNew = null;
 *          // executes the 'call' method below for FuzzySet to do the modifier function
 *          FuzzySet fs = call(fv.getFuzzySet()); 
 *          try {
 *            fvNew = new FuzzyValue(fv.getFuzzyVariable(), fs);
 *          }
 *          catch (XValueOutsideUODException e)
 *          { // we know that the modifers like 'very' do not expand the x value range
 *            // of the fuzzy set so if get this exception there must be an internal error
 *            System.err.println("Internal error in Modifier function '"+getName()+
 *                               "': " + e);
 *            System.exit(100);
 *          }
 *          // must do this to set the linguistic expression for the new fuzzy value
 *          fvNew.unaryModifyLinguisticExpression(getName(), fv.getLinguisticExpression());
 *          return(fvNew);
 *        }
 *
 *        public FuzzySet call(FuzzySet fs)
 *        { // the real work is done here -- on the fuzzy set
 *          return(concentrateDilute(fs, 2.0));
 *        }
 *      }
 * </code></pre>
 * <li> Create an instance of the new modifier class. This automatically
 * adds the class instance to the list of modifier functions maintained
 * by the Modifiers class.
 * <li> Once this has been done the modifier can be used directly by the user
 * <pre><code>
 *      myVeryMod = new VeryModifier("very");
 *          ...
 *      aModifiedFuzzyValue = myVeryMod.call(aFuzzyValue);
 * OR
 *      String mod = "very";
 *          ...
 *      aModifiedFuzzyValue = Modifers.call(mod, aFuzzyValue);
 * </code></pre>
 * The second form uses the Modifiers class to perform a 'variable' modifier method and
 * is used internally by the fuzzy package when creating fuzzy values or
 * fuzzy terms using 'linguistic expressions'.
 * </ul>
 * <p> For convenience an instance of each of the built-in (system supplied) modifiers is
 * created in the Modifiers class and a method is created for each one
 * allowing them to be accessed in a simpler fashion:
 * <pre><code>
 *      aModifiedFuzzyValue = Modifers.very(aFuzzyValue);
 * </code></pre>
 * NOTE: all modifier names are case insensitive ('very' is the same as 'VERY')
 *
 * @author Bob Orchard
 * 
 *
 * @see nrc.fuzzy.Modifiers
 */

public abstract class ModifierFunction implements Serializable
{
    /**
     * This class variable represents a delta x spacing to be used
     * in the expansion of the FuzzyValue/FuzzySet as required by
     * certain fuzzy modifier functions. The deltaX value is used
     * only when precisionControlType is set to DELTA_X.
     * The smaller the delta value, the greater the precision when
     * a fuzzy set is modified (i.e. more points will be generated
     * in the resulting fuzzy set).
     *
     * @see #expandSet
     * @see #precisionControlType
     * @see nrc.fuzzy.Modifiers
     */
    protected static double deltaX;

    /**
     * This class variable represents a delta y spacing to be used
     * in the expansion of the FuzzyValue/FuzzySet as required by
     * certain fuzzy modifier functions. The deltaY value is used
     * only when precisionControlType is set to DELTA_Y.
     * The smaller the delta value, the greater the precision when
     * a fuzzy set is modified (i.e. more points will be generated
     * in the resulting fuzzy set).
     *
     * @see #expandSet
     * @see #precisionControlType
     * @see nrc.fuzzy.Modifiers
     */
    protected static double deltaY;

    /**
     * This class variable represents the number of points to be used
     * in the expansion of the FuzzyValue/FuzzySet as required by
     * certain fuzzy modifier functions. The numberOfPoints value is used
     * only when precisionControlType is set to NUMBER_OF_POINTS.
     * The lager the number of points, the greater the precision when
     * a fuzzy set is modified (i.e. more points will be generated
     * in the resulting fuzzy set).
     *
     * @see #expandSet
     * @see #precisionControlType
     * @see nrc.fuzzy.Modifiers
     */
    protected static int numberOfPoints;

    /**
     * This class variable designates the type of precision control to be
     * used in the expansion of fuzzy sets.  The values allowed are DELTA_X,
     * DELTA_Y, and NUMBER_OF_POINTS.
     *
     * @see #expandSet
     * @see nrc.fuzzy.Modifiers
     */
    protected static int precisionControlType;

    /**
     * A constant used in setting the precisionControlType so that
     * delta x spacing will be used when expanding fuzzy sets
     *
     * @see #precisionControlType
     */
    public static final int DELTA_X = 1;

    /**
     * A constant used in setting the precisionControlType so that
     * delta y spacing will be used when expanding fuzzy sets
     *
     * @see #precisionControlType
     */
    public static final int DELTA_Y = 2;

    /**
     * A constant used in setting the precisionControlType so that
     * number of points setting will be used when expanding fuzzy sets
     *
     * @see #precisionControlType
     */
    public static final int NUMBER_OF_POINTS = 3;


    /* ********** Initialize static varibles to default values *******/
    static
    {
        deltaX = 0.1;
        deltaY = 0.1;
        numberOfPoints = 20;

        precisionControlType = DELTA_Y;
    }


    /**
     * The string name of the modifier function (as used in linguistic expressions).
     * For example, the modifier function "very" might be used in an expression
     * like "very cold".
     */
    private String m_name;



    /**
     * All Modifier Functions must have a String name that they are known by
     * so they can be used in linguistic expressions such as "very cold".
     * The concrete implementations of ModifierFucntion must supply a
     * constructor with a string name argument. Normally the constructor
     * will look like:
     *<pre><code>
     *    VeryModifier( String s )
     *    {
     *       super(s);
     *    }
     * </code>(/pre>
     * The super constructor will take care of recording the string name of
     * the modifier in the class and will also add the modifier to the list
     * (hash table) of modifiers in the Modifiers class.
     *
     * @see nrc.fuzzy.Modifiers
     */
    public ModifierFunction( String name )
    {
        m_name = name.toLowerCase();
        Modifiers.add(this);
    }


    /**
     * Sets the delta x precision value used for fuzzy set expansion.  It is important to note
     * that setting the delta x precision value <b>does not</b> set the precision control
     * type.  The default precision control type is DELTA_Y, therefore, to set
     * the delta x precision and have it applied by certain modifier functions (that use
     * expandSet for example), the <code>setPrecisionControlType</code> method must also
     * be used.
     *
     * @param precision the value of the desired precision for expanding FuzzyValues/FuzzySets.
     *                  For example, if this argument has the value
     *                  0.1, additional points will be added to the FuzzyValue at
     *                  x increments of approximately 0.1.
     *
     * @see #expandSet
     * @see nrc.fuzzy.Modifiers
     * @see #precisionControlType
     *
     */
    public static void setDeltaXPrecision(double precision)
    {
        deltaX = (precision > Parameters.FUZZY_TOLERANCE) ? precision : Parameters.FUZZY_TOLERANCE;
    }

    /**
     * Sets the delta y precision value for fuzzy set expansion.  It is important to note
     * that setting the delta y precision <b>does not</b> set the precision control
     * type.  The default precision control type is DELTA_Y, but, to be sure to set
     * the delta y precision and have it applied by certain modifier functions (that use
     * expandSet for example), the <code>setPrecisionControlType</code> method should
     * be used.
     *
     * @param precision the value of the desired precision for expanding FuzzyValues/FuzzySets.
     *                  For example, if this argument has the value
     *                  0.1, additional points will be added to the FuzzyValue/FuzzySet at
     *                  y increments of approximately 0.1.
     *
     * @see #expandSet
     * @see nrc.fuzzy.Modifiers
     * @see #precisionControlType
     *
     */
    public static void setDeltaYPrecision(double precision)
    {
        deltaY = (precision > Parameters.FUZZY_TOLERANCE) ? precision : Parameters.FUZZY_TOLERANCE;
    }

    /**
     * Sets the number of points used for FuzzyValue/FuzzySet expansion.  It is important to note
     * that setting the number of points <b>does not</b> set the precision control
     * type.  The default precision control type is DELTA_Y, therefore, to set
     * the number of points precision and have it applied by certain modifier functions (that use
     * expandSet for example), the <code>setPrecisionControlType</code> method must also
     * be used.
     *
     * @param numPoints the desired integer number of points to be added to each
     *                  FuzzyValue. Must be > 4.
     *
     * @see #expandSet
     * @see nrc.fuzzy.Modifiers
     * @see #precisionControlType
     *
     */
    public static void setNumberOfPointsPrecision(int numPoints)
    {
        if ( numPoints > 4 ) numberOfPoints = numPoints;
    }

    /**
     * Sets the type of precision control that will be used by the expandSet
     * method that expands the number of points in a fuzzy set.
     * See the class description for a complete justification
     * as to why expansion of the set and precision control are required,
     * and for an explanation of the three different types of precision
     * control.  The argument for this method must be one of the following
     * three constants: DELTA_X, DELTA_Y, NUMBER_OF_POINTS.
     *
     * <p>Note that setting the individual precisions does not affect
     * which precision is used in expansion calculations.  <b>Only</b> this
     * method designates which type of precision control will be used.
     *
     * @param type the precision control type desired for the expansion of the
     *             FuzzyValue, if expansion is required.  This argument must
     *             be one of the following constants: DELTA_X, DELTA_Y,
     *             NUMBER_OF_POINTS.
     *
     * @see #expandSet
     * @see nrc.fuzzy.Modifiers
     * @see #precisionControlType
     */
    public static void setPrecisionControlType(int type)
    {
        if (type == DELTA_X || type == DELTA_Y || type == NUMBER_OF_POINTS)
        {
            precisionControlType = type;
        }
    }




    /**
     * Retrieves the string name of the ModifierFunction.
     */
    public final String getName()
    {
        return m_name;
    }


    /**
     * A concrete implementation of a Modifier Function will need to supply
     * this method, since it will be called to perform the modifier operation
     * on the Fuzzy Value.
     *
     * @param fv the fuzzy value to be modified
     */
    abstract public FuzzyValue call( FuzzyValue fv );

    /**
     * A concrete implementation of a Modifier Function will need to supply
     * this method, since it will be called to perform the modifier operation
     * on the Fuzzy Set.
     *
     * @param fs the fuzzy set to be modified
     */
    abstract public FuzzySet call( FuzzySet fs );


    /*
     **********************************************************************************
     *
     * METHODS USED TO ASSIST IN THE IMPLEMENTATION OF MODIFIER FUNCTIONS
     *
     ***********************************************************************************/

    /**
     * This method expands a fuzzy set to (possibly) include more points as
     * required for the implementation of certain fuzzy modifier functions.
     * The expansion is controlled by the setting of precisionControlType.
     *
     * @param  a  the fuzzy set to be expanded.
     * @return the expanded fuzzy set
     *
     * @see nrc.fuzzy.Modifiers
     * @see #precisionControlType
     * @see #setPrecisionControlType
     */

    public static FuzzySet expandSet(FuzzySet a)
    {
        FuzzySet fs = new FuzzySet();
        double numDivs, dY, dX, dY2add, dX2add;

        if(a.numPoints == 1)
        {   fs.appendSetPoint(a.getPoint(0));
            return(fs);
        }

        for(int i=0; i<a.numPoints-1; i++)
        {   int iPlus1 = i+1;
            numDivs = 0.0;
            //if the x of the two points in question are the same, the line
            //is vertical and need not be expanded. Same for horizontal lines.
            if(a.getX(i) != a.getX(iPlus1) && a.getY(i) != a.getY(iPlus1))
            {   switch(precisionControlType)
                {
                    case DELTA_X:
    			        numDivs = Math.abs((a.getX(iPlus1) - a.getX(i)) / deltaX);
    			        break;
                    case NUMBER_OF_POINTS:
    			        numDivs = ((a.getX(iPlus1) - a.getX(i))*numberOfPoints)/(a.getX(a.numPoints-1) - a.getX(0));
    			        break;
                    case DELTA_Y:
                    default:
    			        numDivs = Math.abs((a.getY(iPlus1) - a.getY(i)) / deltaY);
    			        break;
                }
                // ceil will move 1.000001 up to 2 so subtract a small value
                // to prevent very close values from going up
                numDivs = Math.ceil(numDivs-Parameters.FUZZY_TOLERANCE);
            }    
            
            // always need the 1st point of the pair
            fs.appendSetPoint(a.getX(i), a.getY(i));

            if (numDivs > 1)
            {
                dY = (a.getY(iPlus1) - a.getY(i)) / numDivs;
                dX = (a.getX(iPlus1) - a.getX(i)) / numDivs;

                dX2add = dY2add = 0.0;

                for(int j=1; j<numDivs; j++)
                {
                    dX2add += dX;
                    dY2add += dY;
                    fs.appendSetPoint(a.getX(i) + dX2add, a.getY(i) + dY2add);
                }
            }
        }

        if(a.numPoints > 1) fs.appendSetPoint(a.getPoint(a.numPoints-1));

        fs.simplifySet();
        return(fs);
    }


    /**
     * Returns a new FuzzySet object which represents the expansion of the FuzzySet
     * argument, with all the membership values raised to the specified power.
     *
     * @param a     the FuzzySet to expand and either concentrate or dilute
     *              via exponential methods
     * @param power the power (exponent) to which the membership values are raised
     *
     * @return a new FuzzySet object representing the concentration or dilution of the
     *         FuzzySet argument by the exponent argument.
     */

    public static FuzzySet concentrateDilute(FuzzySet a, double power){
        FuzzySet b = expandSet(a);
        FuzzySet fs = new FuzzySet();

        for(int i=0; i<b.numPoints; i++){
            fs.appendSetPoint(b.getX(i), Math.pow(b.getY(i), power));
        }

        fs.simplifySet();
        return(fs);
    }
}
