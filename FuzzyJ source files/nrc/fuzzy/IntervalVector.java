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
 * The <code>IntervalVector</code> class implements a growable array
 * of Intervals. Like an array, the values can be accessed using an
 * integer index. However, the size of an IntervalVector can
 * grow as needed to accommodate the addition of Intervals after the
 * IntervalVector has been created.
 *
 * An IntervalVector is the object returned as the result of either
 * an alpha cut, or a request for the support of a set.
 *
 * @author Bob Orchard
 *
 */
 
 

public class IntervalVector implements Serializable
{
    /**
     * The default initial capacity of the IntervalVector.  This default initial capacity
     * is only used when the initial capacity of the IntervalVector is not a parameter
     * passed to the constructor used.
     */

    static final protected int INITIAL_CAPACITY = 5;

    /**
     * The increment amount by which the IntervalVector expands when it reaches
     * capacity and has to insert another Interval.  This increment value
     * can be set by choosing a constructor that allows you to specify it.
     */

    protected int increment;

    /**
     * The array of Interval values.  This array is manipulated in such a way that it
     * will expand as required to accomodate the addition of Intervals after the
     * IntervalVector is created.
     */

    protected Interval[] intervals;

    /**
     * The current number of Intervals in the array, and the index of the array
     * at which the next Interval should be inserted.
     */

    protected int index;

    /*
     **************************************************************************************
     *
     * CONSTRUCTORS
     *
     ***************************************************************************************/

    /**
     * Creates an empty IntervalVector with the default initial capacity and increment values.
     */

    public IntervalVector(){
        increment = 5;

        intervals = new Interval[INITIAL_CAPACITY];
        index = 0;
    }

    /**
     * Creates an empty IntervalVector with the specified initial capacity and the default
     * increment value.
     *
     * @param initialCapacity the int which specifies the desired initial capacity of
     *                        the IntervalVector.  This is best used it is known that
     *                        the IntervalVector will be quite large so as to avoid multiple
     *                        expansions of the IntervalVector, which is inefficient.
     */

    public IntervalVector(int initialCapacity){
        increment = 5;

        intervals = new Interval[initialCapacity];
        index = 0;
    }

    /**
     * Creates an empty IntervalVector with the specified initial capacity and increment
     * value.
     *
     * @param initialCapacity the int to specify the desired initial capacity of
     *                        the IntervalVector.  This is best used it is known that
     *                        the IntervalVector will be quite large so as to avoid multiple
     *                        expansions of the IntervalVector, which is inefficient.
     * @param increment       the int to specify the desired increment of the IntervalVector
     */

    public IntervalVector(int initialCapacity, int increment){
        this.increment = increment;

        intervals = new Interval[initialCapacity];
        index = 0;
    }

    /**
     * Creates a IntervalVector with the length specified in the parameters, and
     * initialized for the full length of the IntervalVector from the Interval
     * values contained in the passed array.
     *
     * @param IntervalArray the array of Intervals which contains the Intervals
     *                    with which to initialize the IntervalVector.
     * @param length      this parameter is basically equivalent to the initial
     *                    capacity parameter in the other constructors.  However,
     *                    it has one additional implication.  It is expected
     *                    that <code>length</code> is less than or equal to the length of
     *                    <code>IntervalArray</code>.
     *
     * @exception ArrayIndexOutOfBoundsException if the <code>length</code> parameter is
     *                    greater than the length of <code>IntervalArray</code>.
     */

    public IntervalVector(Interval[] IntervalArray, int length){
        increment = 5;

        intervals = new Interval[length];

        for(int i=0; i<length; i++){
            intervals[i] = IntervalArray[i];
        }

        index = length;
    }


    /*
     *************************************************************************************
     *
     * ACCESSIBLE METHODS FOR USING THE IntervalVECTOR
     *
     **************************************************************************************/

    /**
     * Adds a Interval to the end of the IntervalVector, increasing
     * its size by one.
     *
     * @param value the Interval to be added to the IntervalVector
     */

    public void addInterval(Interval interval){
        testIntervalArrayLength();
        intervals[index++] = interval;
    }

    /**
     * Concatenates the IntervalVector argument with this IntervalVector.
     * The values contained within the IntervalVector argument are
     * effectively added to the end of this IntervalVector.
     *
     * @param other the IntervalVector to concatenate onto the end of
     *              this IntervalVector
     */

    public void concat(IntervalVector other){

        for(int i=0; i<other.size(); i++){
            testIntervalArrayLength();
            this.addInterval(other.intervalAt(i));
        }
    }

    /**
     * Returns the Interval at the specified index.
     *
     * @param i the index in the IntervalVector of the desired
     *          Interval
     * @return  the Interval at the specified index
     */

    public Interval intervalAt(int i){
        return(intervals[i]);
    }

    /**
     * Sets the Interval in the IntervalVector at the specified index.
     *
     * @param value the Interval to set the specified position in the
     *              IntervalVector to
     * @param i     the index in the IntervalVector of the Interval position
     *              that is desired to be set
     */

    public void setIntervalAt(Interval interval, int i){
        if(0 <= i && i < index)
            intervals[i] = interval;
    }

    /**
     * Removes the Interval from the IntervalVector at the specified index.
     * The subsequent values in the IntervalVector are then shifted down one
     * index position.
     *
     * @param i the position of the Interval to remove
     */

    public void removeIntervalAt(int i){
        for(int j=i; j<index-1; j++){
            intervals[j] = intervals[j+1];
        }
    }

    /**
     * Inserts the Interval argument into the IntervalVector at the specified index.
     * The Interval at the specified index and all subsequent values in
     * the IntervalVector are then shifted up one index position.
     *
     * @param value the Interval to be inserted into the IntervalVector
     * @param i     the index position of the IntervalVector into which the
     *              Interval argument is to be inserted
     */

    public void insertIntervalAt(Interval interval, int i)
    {
        if (i < 0) i = 0;
        else if (i > index) i = index;

        testIntervalArrayLength();
        for(int j=index; j > i; j--){
            intervals[j] = intervals[j-1];
        }

        intervals[i] = interval;
    }

    /**
     * Trims the capacity of this IntervalVector to be the IntervalVector's
     * current size. This can be used to minimize the storage of a IntervalVector.
     */

    public void trimToSize(){
        Interval[] newArray = new Interval[index];

        for(int i=0; i<index; i++){
            newArray[i] = intervals[i];
        }

        intervals = newArray;
    }

    /**
     * Returns the size of the IntervalVector, or the number of Intervals
     * in the IntervalVector.
     *
     * @return the size of the IntervalVector, or the number of Intervals
     *         contained within the IntervalVector
     */

    public int size(){
        return(index);
    }
    
    /**
     * Tests whether or not the IntervalVector is empty, ie. contains 
     * zero Intervals.
     *
     * @return <code>true</code> if the IntervalVector is empty, if
     *         it contains zero Intervals
     */
     
     public boolean isEmpty(){
        return(index == 0);
     }   

    /**
     * Returns the array of Intervals, trimmed to size, that this IntervalVector
     * contains.
     *
     * @return the Interval[] of the values contained by the IntervalVector.  The
     *         length of the returned Interval vector is the current size of the
     *         IntervalVector; in other words, the exact number of Intervals
     *         in the IntervalVector.
     */

    public Interval[] toIntervalArray(){
        trimToSize();
        return(intervals);
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
        
        if (size() < 1) return "";
        
        sb.append(intervals[0].toString());
        for(int i=1; i<size(); i++)
        {
            sb.append(intervals[i].toString());
            sb.append(" ");
        }
        
        return(sb.toString());
    }    

    /*
     *************************************************************************************
     *
     * PRIVATE METHODS FOR MAINTAINING THE IntervalVECTOR
     *
     **************************************************************************************/

    /**
     * Tests the adequacy of the current capacity by comparing the length
     * of the current Interval array against the number of Intervals
     * contained in the array.  If the number of Intervals in the
     * IntervalVector is equal to the length of the Interval array, the array
     * needs to be incremented, or expanded.
     */

    private void testIntervalArrayLength(){
        if(index >= intervals.length) incrementIntervalArray();
    }

    /**
     * Expands the current Interval array by the amount specified in the
     * <code>increment</code> variable.
     */

    private void incrementIntervalArray(){
        Interval[] newArray = new Interval[intervals.length+increment];

        for(int i=0; i<intervals.length; i++){
            newArray[i] = intervals[i];
        }

        intervals = newArray;
    }
}


