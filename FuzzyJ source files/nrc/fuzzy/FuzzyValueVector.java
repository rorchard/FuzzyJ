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
 * The <code>FuzzyValueVector</code> class implements a growable array
 * of FuzzyValues. Like an array, the values can be accessed using an
 * integer index. However, the size of a FuzzyValueVector can
 * grow as needed to accommodate the addition of Intervals after the
 * FuzzyValueVector has been created.
 *
 * A FuzzyValueVector is the used within the fuzzy package to store the
 * antecedents and conclusions of in a FuzzyRule.
 *
 * @author Bob Orchard
 *
 */
 
 

public class FuzzyValueVector implements Serializable 
{
    /**
     * The default initial capacity of the FuzzyValueVector.  This default initial capacity
     * is only used when the initial capacity of the FuzzyValueVector is not a parameter
     * passed to the constructor used.
     */

    static final protected int INITIAL_CAPACITY = 5;

    /**
     * The increment amount by which the FuzzyValueVector expands when it reaches
     * capacity and has to insert another FuzzyValue.  This increment value
     * can be set by choosing a constructor that allows you to specify it.
     */

    protected int increment;

    /**
     * The array of FuzzyValue values.  This array is manipulated in such a way that it
     * will expand as required to accomodate the addition of FuzzyValues after the
     * FuzzyValueVector is created.
     */

    protected FuzzyValue[] fuzzyValues;

    /**
     * The current number of FuzzyValues in the array, and the index of the array
     * at which the next FuzzyValue should be inserted.
     */

    protected int index;

    /*
     **************************************************************************************
     *
     * CONSTRUCTORS
     *
     ***************************************************************************************/

    /**
     * Creates an empty FuzzyValueVector with the default initial capacity and increment values.
     */

    public FuzzyValueVector(){
        increment = 5;

        fuzzyValues = new FuzzyValue[INITIAL_CAPACITY];
        index = 0;
    }

    /**
     * Creates an empty FuzzyValueVector with the specified initial capacity and the default
     * increment value.
     *
     * @param initialCapacity the int which specifies the desired initial capacity of
     *                        the FuzzyValueVector.  This is best used when it is known that
     *                        the FuzzyValueVector will be quite large so as to avoid multiple
     *                        expansions of the FuzzyValueVector, which is inefficient.
     */

    public FuzzyValueVector(int initialCapacity){
        increment = 5;

        fuzzyValues = new FuzzyValue[initialCapacity];
        index = 0;
    }

    /**
     * Creates an empty FuzzyValueVector with the specified initial capacity and increment
     * value.
     *
     * @param initialCapacity the int to specify the desired initial capacity of
     *                        the FuzzyValueVector.  This is best used when it is known that
     *                        the FuzzyValueVector will be quite large so as to avoid multiple
     *                        expansions of the FuzzyValueVector, which is inefficient.
     * @param increment       the int to specify the desired increment of the FuzzyValueVector
     */

    public FuzzyValueVector(int initialCapacity, int increment){
        this.increment = increment;

        fuzzyValues = new FuzzyValue[initialCapacity];
        index = 0;
    }

    /**
     * Creates a FuzzyValueVector with the length specified in the parameters, and
     * initialized for the full length of the FuzzyValueVector from the FuzzyValue
     * values contained in the passed array.
     *
     * @param FuzzyValueArray the array of FuzzyValues which contains the FuzzyValues
     *                    with which to initialize the FuzzyValueVector.
     * @param length      this parameter is basically equivalent to the initial
     *                    capacity parameter in the other constructors.  However,
     *                    it has one additional implication.  It is expected
     *                    that <code>length</code> is less than or equal to the length of
     *                    <code>FuzzyValueArray</code>.
     *
     * @exception ArrayIndexOutOfBoundsException if the <code>length</code> parameter is
     *                    greater than the length of <code>FuzzyValueArray</code>.
     */

    public FuzzyValueVector(FuzzyValue[] FuzzyValueArray, int length){
        increment = 5;

        fuzzyValues = new FuzzyValue[length];

        for(int i=0; i<length; i++){
            fuzzyValues[i] = FuzzyValueArray[i];
        }

        index = length;
    }


    /*
     *************************************************************************************
     *
     * ACCESSIBLE METHODS FOR USING THE FuzzyValueVector
     *
     **************************************************************************************/

    /**
     * Adds a FuzzyValue to the end of the FuzzyValueVector, increasing
     * its size by one.
     *
     * @param value the FuzzyValue to be added to the IntervalVector
     */

    public void addFuzzyValue(FuzzyValue fuzzyValue){
        testFuzzyValueArrayLength();
        fuzzyValues[index++] = fuzzyValue;
    }

    /**
     * Concatenates the FuzzyValueVector argument with this FuzzyValueVector.
     * The values contained within the FuzzyValueVector argument are
     * effectively added to the end of this FuzzyValueVector.
     *
     * @param other the FuzzyValueVector to concatenate onto the end of
     *              this FuzzyValueVector
     */

    public void concat(FuzzyValueVector other){

        for(int i=0; i<other.size(); i++){
            testFuzzyValueArrayLength();
            this.addFuzzyValue(other.fuzzyValueAt(i));
        }
    }

    /**
     * Returns the FuzzyValue at the specified index.
     *
     * @param i the index in the FuzzyValueVector of the desired
     *          FuzzyValue
     * @return  the FuzzyValue at the specified index
     */

    public FuzzyValue fuzzyValueAt(int i){
        return(fuzzyValues[i]);
    }

    /**
     * Sets the FuzzyValue in the FuzzyValueVector at the specified index. If
     * the index is < 0 or > the highest valid index in the vector nothing
     * is done.
     *
     * @param value the FuzzyValue to set the specified position in the
     *              FuzzyValueVector to
     * @param i     the index in the FuzzyValueVector of the FuzzyValue position
     *              that is desired to be set
     */

    public void setFuzzyValueAt(FuzzyValue fuzzyValue, int i){
        if(0 <= i && i < index)
            fuzzyValues[i] = fuzzyValue;
    }

    /**
     * Removes the FuzzyValue from the FuzzyValueVector at the specified index.
     * The subsequent values in the FuzzyValueVector are then shifted down one
     * index position.  If the specified index is < 0 or > the largest valid
     * index for the vector, nothing is done.
     *
     * @param i the position of the FuzzyValue to remove
     */

    public void removeFuzzyValueAt(int i){
        for(int j=i; j<index-1; j++){
            fuzzyValues[j] = fuzzyValues[j+1];
        }
        fuzzyValues[--index] = null;
    }

    /**
     * Inserts the FuzzyValue argument into the FuzzyValueVector at the specified index.
     * The FuzzyValue at the specified index and all subsequent values in
     * the FuzzyValueVector are then shifted up one index position. If the
     * specified index is < 0 then it is treated as 0. If the index is >
     * the highest valid index the value is added to the end of the vector.
     *
     * @param value the FuzzyValue to be inserted into the FuzzyValueVector
     * @param i     the index position of the FuzzyValueVector into which the
     *              FuzzyValue argument is to be inserted
     */

    public void insertFuzzyValueAt(FuzzyValue fuzzyValue, int i)
    {
        if (i < 0) i = 0;
        else if (i > index) i = index;
        
        testFuzzyValueArrayLength();
        for (int j=index; j > i; j--){
                fuzzyValues[j] = fuzzyValues[j-1];
            }

        fuzzyValues[i] = fuzzyValue;
        index++;
    }

    /**
     * Trims the capacity of this FuzzyValueVector to be the FuzzyValueVector's
     * current size. This can be used to minimize the storage of a FuzzyValueVector.
     */

    public void trimToSize(){
        FuzzyValue[] newArray = new FuzzyValue[index];

        for(int i=0; i<index; i++){
            newArray[i] = fuzzyValues[i];
        }

        fuzzyValues = newArray;
    }

    /**
     * Returns the size of the FuzzyValueVector, or the number of FuzzyValues
     * in the FuzzyValueVector.
     *
     * @return the size of the FuzzyValueVector, or the number of FuzzyValues
     *         contained within the FuzzyValueVector
     */

    public int size(){
        return(index);
    }
    
    /**
     * Tests whether or not the FuzzyValueVector is empty, ie. contains 
     * zero FuzzyValues.
     *
     * @return <code>true</code> if the FuzzyValueVector is empty, if
     *         it contains zero FuzzyValues
     */
     
     public boolean isEmpty(){
        return(index == 0);
     }   

    /**
     * Returns the array of FuzzyValues, trimmed to size, that this FuzzyValueVector
     * contains.
     *
     * @return the FuzzyValue[] of the values contained by the FuzzyValueVector.  The
     *         length of the returned FuzzyValue vector is the current size of the
     *         FuzzyValueVector; in other words, the exact number of FuzzyValues.
     */

    public FuzzyValue[] toFuzzyValueArray(){
        trimToSize();
        return(fuzzyValues);
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
        
        if (size() < 1) return "";
        
        sb.append(fuzzyValues[0].toString());
        for(int i=1; i<size(); i++)
        {
            sb.append(fuzzyValues[i].toString());
            sb.append(" ");
        }
        
        return(sb.toString());
    }    

    /**
     * Returns a FuzzyValue that is the union of all of the FuzzyValues
     * that this FuzzyValueVector contains. All of the FuzzyValues in the
     * FuzzyValueVector must have the same FuzzyVariable.
     *
     * @return the FuzzyValue that is the union of all of the FuzzyValues
     * that this FuzzyValueVector contains.
     *
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public FuzzyValue fuzzyUnion()
           throws XValueOutsideUODException, IncompatibleFuzzyValuesException
    {   FuzzyValue fvResult;
        int i;
        if (index < 1)  return null;

        fvResult = fuzzyValues[0];
        for (i=1; i<index; i++)
        { fvResult = fvResult.fuzzyUnion(fuzzyValues[i]);
        }
        return fvResult;
    }

    /**
     * Returns a FuzzyValue that is the intersection of all of the FuzzyValues
     * that this FuzzyValueVector contains. All of the FuzzyValues in the
     * FuzzyValueVector must have the same FuzzyVariable.
     *
     * @return the FuzzyValue that is the intersection of all of the FuzzyValues
     * that this FuzzyValueVector contains.
     *
     * @exception XValueOutsideUODException if the fuzzy set x values are not within the 
     *                              range of the universe of discourse
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                              identical fuzzy variables the operation cannot be done
     */

    public FuzzyValue fuzzyIntersection()
           throws XValueOutsideUODException, IncompatibleFuzzyValuesException
    {   FuzzyValue fvResult;
        int i;
        if (index < 1)  return null;

        fvResult = fuzzyValues[0];
        for (i=1; i<index; i++)
        { fvResult = fvResult.fuzzyIntersection(fuzzyValues[i]);
        }
        return fvResult;
    }

    /**
     *  Finds the weighted average of the x-values in all of the FuzzySets in
     *  the FuzzyValues of the FuzzyValueVector. The weights are the
     *  membership values. This is the defuzzification value
     *  for the FuzzyValueVector. This is slightly different than the
     *  maximumDefuzzify since the maximumDefuzzify uses only points that
     *  have the same membership value (the one that is the maximum in 
     *  the set of points). The weightedAverageDefuzzify uses all of the 
     *  points with non-zero membership values and calculates the average 
     *  of the x values using the membership values as weights in this average.
     *  <br>
     *  NOTE: All FuzzyValues in the FuzzyValueVector MUST have the same FuzzyVariable.
     *  <br>
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
     *                  ^       ^
     *                  |       |
     *                  |       | gives this as the weighted average
     *                  | this is more reasonable???
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
     * <br>
     * Where would one use this? Normally when rules fire and fuzzy outputs are generated
     * the FuzzyValue outputs with the same FuzzyVariable are combined (global contribution)
     * using a fuzzyUnion operation. Then a defuzzification operation is performed on
     * this resultant FuzzyValue after all the rules have fired. But sometimes the
     * union operation would 'hide' some of the results. For example in the example
     * above it might be that some rules generated singletons at the same x value. The
     * union operation would combine these 2 x values and look as if there was just
     * one (the one with the largest membership value). Then the weighting would not take into
     * account that there were 2 outputs at this x value. Now in most case this is
     * the desired effect but in some cases it is not. In order to get the desired effect, the
     * union must not be done and the collection of outputs can be saved in a FuzzyValueVector.
     * Then the FuzzyValueVector can use this method to allow all values to be used
     * in the calculation.
     *  <br>
     *  NOTE: All FuzzyValues in the FuzzyValueVector MUST have the same FuzzyVariable.
     *  <br>
     *       
     * @return the floating value that is the weighted average of the values
     *         of all of the FuzzySets in the FuzzyValues in the FuzzyValueVector.
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet(s)
     * @exception XValuesOutOfOrderException occurs when the MinUOD is
     *                 greater than or equal to the MaxUOD parameter (this
     *                 should never happen).
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                 identical fuzzy variables or there are no FuzzyValues
     *                 in the FuzzyValueVector the operation cannot be done
     */
     
    public double weightedAverageDefuzzify()
        throws XValuesOutOfOrderException, InvalidDefuzzifyException,
               IncompatibleFuzzyValuesException
    { int i;
      FuzzySet fset;
      FuzzyValue fval;
      FuzzyVariable firstFvar = null, fvar;
      double sumOfWAndSumOfWTimesXvals[];
      double sumOfWeights = 0.0;
      double sumOfWeightsTimesXvals = 0.0;

      // all FuzzyValues must have same FuzzyVariable and there must be at least
      // one FuzzyValue in the Vector
      if (index < 1) 
          throw new IncompatibleFuzzyValuesException("The FuzzyValueVector has no FuzzyValues in it");
      for (i=0; i<index; i++)
      {   fval = fuzzyValues[i];
      	  if (fval != null)
          {  fvar = fval.getFuzzyVariable();
             if (firstFvar != null)
             {  if (firstFvar != fvar)
                   throw  new IncompatibleFuzzyValuesException("All FuzzyValues in the FuzzyValueVector must have the 'same' FuzzyVariable");
             }
             // remember first FuzzyVariable to compare against all others
             else firstFvar = fvar;
             // accumulate the sum of values*weights and sum of weights
             sumOfWAndSumOfWTimesXvals = 
                 fval.getFuzzySet().calulateSumOfWeightsAndSumOfWeightsTimesXvals(fval.getMinUOD(), fval.getMaxUOD());
             sumOfWeights += sumOfWAndSumOfWTimesXvals[1];
             sumOfWeightsTimesXvals += sumOfWAndSumOfWTimesXvals[0];
          }
      }
      // if didn't find any FuzzyValues then fail
      if (firstFvar == null) 
         throw new IncompatibleFuzzyValuesException("The FuzzyValueVector has no FuzzyValues in it");
      if (sumOfWeights == 0.0)
         throw new InvalidDefuzzifyException("The FuzzySets in the FuzzyValueVector had no points with membership value > 0.0");
        
      return sumOfWeightsTimesXvals/sumOfWeights;    	
    }


     
    /**
     *  Finds the mean of maxima of the x-values in all of the FuzzySets in
     *  the FuzzyValues of the FuzzyValueVector.
     *   
     *  NOTE: This doesn't always work well because there can be x ranges
     *         where the y value is constant at the max value and other 
     *         places where the max is only reached for a single x value.
     *         When this happens the single value gets too much of a say
     *         in the defuzzified value. So use moment defuzzify in cases
     *         like this.
     *  <br>
     *  NOTE: All FuzzyValues in the FuzzyValueVector MUST have the same FuzzyVariable.
     *  <br>
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
     *       
     * @return the floating value that is the first average of the maximum 
     *          values of the FuzzySets in the FuzzyValues of the FuzzyValueVector.
     *
     * @exception InvalidDefuzzifyException there are no points in the FuzzySet
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *             greater than or equal to the xMax parameter.
     * @exception IncompatibleFuzzyValuesException if the fuzzy values do not have
     *                 identical fuzzy variables or there are no FuzzyValues
     *                 in the FuzzyValueVector the operation cannot be done
     */
     
    public double maximumDefuzzify()
        throws XValuesOutOfOrderException, InvalidDefuzzifyException,
               IncompatibleFuzzyValuesException
    { int i;
      FuzzySet fset;
      FuzzyValue fval;
      FuzzyVariable firstFvar = null, fvar;
      double maxYandSumOfXandNumberOfX[];
      double numberOfX = 0.0;
      double maxY = 0.0;
      double sumOfX = 0.0;
      double overallMaxY = 0.0;

      // all FuzzyValues must have same FuzzyVariable and there must be at least
      // one FuzzyValue in the Vector
      if (index < 1) 
          throw new InvalidDefuzzifyException("The FuzzyValueVector has no FuzzyValues in it");
      for (i=0; i<index; i++)
      {   fval = fuzzyValues[i];
      	  if (fval != null)
          {  fvar = fval.getFuzzyVariable();
             if (firstFvar != null)
             {  if (firstFvar != fvar)
                   throw  new IncompatibleFuzzyValuesException("All FuzzyValues in the FuzzyValueVector must have the 'same' FuzzyVariable");
             }
             // remember first FuzzyVariable to compare against all others
             else firstFvar = fvar;
                // accumulate the sum of values*weights and sum of weights
                maxYandSumOfXandNumberOfX = 
                   fval.getFuzzySet().calculateMaxYandSumOfXandNumberOfX(fval.getMinUOD(), fval.getMaxUOD());
			 maxY = maxYandSumOfXandNumberOfX[0];
			 // when we find a new maxY value start accumulating counts and sums of X values
			 if (maxY > overallMaxY)
			 {  numberOfX = maxYandSumOfXandNumberOfX[2];
			 	sumOfX = maxYandSumOfXandNumberOfX[1];
			 	overallMaxY = maxY;
			 }
			 else
			 // if maxY value is same as in previous FuzzySet(s) add to counts and sum of X values
			 {  numberOfX += maxYandSumOfXandNumberOfX[2];
			    sumOfX += maxYandSumOfXandNumberOfX[1]; 
			 }
		  }
      }
      // if didn't find any FuzzyValues then fail
      if (firstFvar == null) 
         throw new InvalidDefuzzifyException("The FuzzyValueVector has no FuzzyValues in it");
      if (numberOfX == 0)
            throw new InvalidDefuzzifyException("The FuzzySets in the FuzzyValueVector had no points");
        
      return sumOfX/numberOfX;    	
    }


     /**
      *  Moment defuzzification defuzzifies a FuzzyVector of FuzzySets returning a
      *  floating point (double value) that represents the moment of these FuzzySets.
      *
      *  It calculates the first moment of area of these FuzzySets about the
      *  y axis.  Each set is subdivided into different shapes by partitioning
      *  vertically at each point in the set, resulting in rectangles, 
      *  triangles, and trapezoids.  The centre of gravity (moment) and area of
      *  each subdivision is calculated using the appropriate formulas
      *  for each shape.  The first moment of area of all of the FuzzySets is
      *  then:
      *  <b><pre>
      *      sum of ( moment(i) * area(i) )          <-- top
      *      ------------------------------
      *          sum of (area(i))                    <-- bottom
      *  </pre></b>
      *  If the total area is 0 then throw an exception since the moment
      *  is not defined.
      *  <br>
      *  NOTE: All FuzzyValues in the FuzzyValueVector MUST have the same FuzzyVariable.
      *  <br>
      *
      * @return the floating value that is the first moment of the fuzzy set
      *
      * @exception InvalidDefuzzifyException there is no valid moment for the fuzzy set
      *                 (generally the area is 0 under the fuzzy set graph)
      * @exception XValuesOutOfOrderException occurs when the xMin parameter is
      *                 greater than or equal to the xMax parameter.
      */   
    public double momentDefuzzify()
    throws XValuesOutOfOrderException, InvalidDefuzzifyException,
           IncompatibleFuzzyValuesException
 { int i;
  FuzzySet fset;
  FuzzyValue fval;
  FuzzyVariable firstFvar = null, fvar;
  double sumOfMomentsTimesAreasAndSumOfAreas[];
  double sumOfAreas = 0.0;
  double sumOfMomentsTimesAreas = 0.0;

  // all FuzzyValues must have same FuzzyVariable and there must be at least
  // one FuzzyValue in the Vector
  if (index < 1) 
      throw new InvalidDefuzzifyException("The FuzzyValueVector has no FuzzyValues in it");
  for (i=0; i<index; i++)
  {   fval = fuzzyValues[i];
  	  if (fval != null)
      {  fvar = fval.getFuzzyVariable();
         if (firstFvar != null)
         {  if (firstFvar != fvar)
               throw  new IncompatibleFuzzyValuesException("All FuzzyValues in the FuzzyValueVector must have the 'same' FuzzyVariable");
         }
         // remember first FuzzyVariable to compare against all others
         else firstFvar = fvar;
         // accumulate the sum of values*weights and sum of weights
         sumOfMomentsTimesAreasAndSumOfAreas = 
             fval.getFuzzySet().calulateSumOfMomentsTimesAreasAndSumOfAreas(fval.getMinUOD(), fval.getMaxUOD());
         sumOfAreas += sumOfMomentsTimesAreasAndSumOfAreas[1];
         sumOfMomentsTimesAreas += sumOfMomentsTimesAreasAndSumOfAreas[0];
      }
  }
  // if didn't find any FuzzyValues then fail
  if (firstFvar == null) 
     throw new IncompatibleFuzzyValuesException("The FuzzyValueVector has no FuzzyValues in it");
  if (sumOfAreas == 0.0)
     throw new InvalidDefuzzifyException("The FuzzySets in the FuzzyValueVector had no points with membership value > 0.0");
    
  return sumOfMomentsTimesAreas/sumOfAreas;    	
}
    
    
    /**
     *  Center of Area (COA) defuzzification defuzzifies a FuzzyVector of 
     *  fuzzy sets returning a floating point (double value) that represents 
     *  crips value of these fuzzy sets. In this case what is actually done is
     *  a fuzzySum of the fuzzy set is calculated and the center of area of 
     *  that fuzzy set is returned. It provides more weight to the overlapping 
     *  areas of these fuzzy sets than would be the case if a union of the sets is
     *  done and the COA of that set is calculated.
     *
     *  It calculates the x value that splits the 'combined' fuzzy set so that there
     *  is an equal area on either side of the x value.
     *  The set is subdivided into different shapes by partitioning
     *  vertically at each point in the set, resulting in rectangles, 
     *  triangles, and trapezoids.  
     *  <br>
     *  If the total area is 0 then throw an exception since the center
     *  of area is not defined.
     *  <br>
     *  NOTE: All FuzzyValues in the FuzzyValueVector MUST have the same FuzzyVariable.
     *  <br>
     *
     * @return the floating value that is the center of area (COA) of the fuzzy sets
     *
     * @exception InvalidDefuzzifyException there is no valid COA for the fuzzy set
     *                 (generally the area is 0 under the fuzzy set graph)
     * @exception XValuesOutOfOrderException occurs when the xMin parameter is
     *                 greater than or equal to the xMax parameter.
     */   
    
    public double centerOfAreaDefuzzify()
    throws XValuesOutOfOrderException, InvalidDefuzzifyException,
           IncompatibleFuzzyValuesException, XValueOutsideUODException
 { int i;
  FuzzyValue fval = null, fvalSum = null;
  FuzzyVariable firstFvar = null, fvar;

  // all FuzzyValues must have same FuzzyVariable and there must be at least
  // one FuzzyValue in the Vector
  if (index < 1) 
      throw new InvalidDefuzzifyException("The FuzzyValueVector has no FuzzyValues in it");
  
  for (i=0; i<index; i++)
  {   fval = fuzzyValues[i];
  	  if (fval != null)
      {  fvar = fval.getFuzzyVariable();
         if (firstFvar != null)
         {  if (firstFvar != fvar)
               throw  new IncompatibleFuzzyValuesException("All FuzzyValues in the FuzzyValueVector must have the 'same' FuzzyVariable");
            // do a fuzzy sum of all fuzzy values 
            fvalSum = fvalSum.fuzzySum(fval);
         }
         // remember first FuzzyVariable to compare against all others
         else 
         {	firstFvar = fvar;
            fvalSum = fval;
         }
      }
  }
  // if didn't find any FuzzyValues then fail
  if (firstFvar == null) 
     throw new IncompatibleFuzzyValuesException("The FuzzyValueVector has no FuzzyValues in it");
    
  return fvalSum.centerOfAreaDefuzzify();    	
}



    /*
     *************************************************************************************
     *
     * PRIVATE METHODS FOR MAINTAINING THE FuzzyValueVECTOR
     *
     **************************************************************************************/

    /**
     * Tests the adequacy of the current capacity by comparing the length
     * of the current FuzzyValue array against the number of FuzzyValues
     * contained in the array.  If the number of FuzzyValues in the
     * FuzzyValueVector is equal to the length of the FuzzyValue array, the array
     * needs to be incremented, or expanded.
     */

    private void testFuzzyValueArrayLength(){
        if(index >= fuzzyValues.length) incrementFuzzyValueArray();
    }

    /**
     * Expands the current FuzzyValue array by the amount specified in the
     * <code>increment</code> variable.
     */

    private void incrementFuzzyValueArray(){
        FuzzyValue[] newArray = new FuzzyValue[fuzzyValues.length+increment];

        for(int i=0; i<fuzzyValues.length; i++){
            newArray[i] = fuzzyValues[i];
        }

        fuzzyValues = newArray;
    }
}


