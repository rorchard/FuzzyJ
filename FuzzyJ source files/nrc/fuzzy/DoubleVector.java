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
 * The DoubleVector class implements a growable array of <code>double</code> values. Like an array,
 * the values can be accessed using an integer index. However, the size of a DoubleVector can
 * grow as needed to accommodate adding items after the DoubleVector has been created.
 *
 * This class was created for the utility of the package and was not written with the intention
 * of allowing user access.
 *
 * @author Alexis Eller
 * @author Bob Orchard
 *
 */

public class DoubleVector implements Serializable 
{
    /**
     * The default initial capacity of the DoubleVector.  This default initial capacity
     * is only used when the initial capacity of the DoubleVector is not a parameter
     * passed to the constructor used.
     */

    static final protected int INITIAL_CAPACITY = 30;

    /**
     * The increment amount by which the DoubleVector expands when it reaches 
     * capacity and has to insert another double value.  This increment value 
     * can be set by choosing a constructor that allows you to specify it.
     */

    protected int increment;

    /**
     * The array of doubles values.  This array is manipulated in such a way that it
     * will expand as required to accomodate the addition of double values after the
     * DoubleVector is created.
     */

    protected double[] d;

    /**
     * The current number of double values in the array, and the index of the array
     * at which the next double value should be inserted.
     */

    protected int index;

    /***************************************************************************************
     *
     * CONSTRUCTORS
     *
     ***************************************************************************************/

    /**
     * Creates an empty DoubleVector with the default initial capacity and increment values.
     */

    public DoubleVector(){
        increment = 10;

        d = new double[INITIAL_CAPACITY];
        index = 0;
    }

    /**
     * Creates an empty DoubleVector with the specified initial capacity and the default
     * increment value.
     *
     * @param initialCapacity the int which specifies the desired initial capacity of
     *                        the DoubleVector.  This is best used it is known that
     *                        the DoubleVector will be quite large so as to avoid multiple
     *                        expansions of the DoubleVector, which is inefficient.
     */

    public DoubleVector(int initialCapacity){
        increment = 10;

        d = new double[initialCapacity];
        index = 0;
    }

    /**
     * Creates an empty DoubleVector with the specified initial capacity and increment
     * value.
     *
     * @param initialCapacity the int to specify the desired initial capacity of
     *                        the DoubleVector.  This is best used it is known that
     *                        the DoubleVector will be quite large so as to avoid multiple
     *                        expansions of the DoubleVector, which is inefficient.
     * @param increment       the int to specify the desired increment of the DoubleVector
     */

    public DoubleVector(int initialCapacity, int increment){
        this.increment = increment;

        d = new double[initialCapacity];
        index = 0;
    }

    /**
     * Creates a DoubleVector with the length specified in the parameters, and
     * initialized for the full length of the DoubleVector from the double
     * values contained in the passed array.
     *
     * @param doubleArray the array of doubles which contains the double values
     *                    with which to initialize the DoubleVector.
     * @param length      this parameter is basically equivalent to the initial
     *                    capacity parameter in the other constructors.  However,
     *                    it has one additional implication.  It is expected
     *                    that <code>length</code> is less than or equal to the length of
     *                    <code>doubleArray</code>.
     *
     * @exception ArrayIndexOutOfBoundsException if the <code>length</code> parameter is
     *                    greater than the length of <code>doubleArray</code>.
     */

    public DoubleVector(double[] doubleArray, int length){
        increment = 10;

        d = new double[length];

        for(int i=0; i<length; i++){
            d[i] = doubleArray[i];
        }

        index = length;
    }


    /*
     *************************************************************************************
     *
     * ACCESSIBLE METHODS FOR USING THE DoubleVECTOR
     *
     **************************************************************************************/

    /**
     * Adds a double value to the end of the DoubleVector, increasing 
     * its size by one.
     *
     * @param value the double value to be added to the DoubleVector
     */
     
    public void addDouble(double value){
        testDoubleArrayLength();
        d[index++] = value;
    }

    /**
     * Concatenates the DoubleVector argument with this DoubleVector.
     * The values contained within the DoubleVector argument are
     * effectively added to the end of this DoubleVector.
     *
     * @param other the DoubleVector to concatenate onto the end of
     *              this DoubleVector
     */
     
    public void concat(DoubleVector other){

        for(int i=0; i<other.size(); i++){
            testDoubleArrayLength();
            this.addDouble(other.doubleAt(i));
        }
    }
    
    /**
     * Returns the double value at the specified index.
     *
     * @param i the index in the DoubleVector of the desired  
     *          double value 
     * @return  the double value at the specified index
     */

    public double doubleAt(int i){
        return(d[i]);
    }
    
    /**
     * Sets the double value in the DoubleVector at the specified index.
     *
     * @param value the double value to set the specified position in the 
     *              DoubleVector to
     * @param i     the index in the DoubleVector of the double value position
     *              that is desired to be set
     */

    public void setDoubleAt(double value, int i){
        if(0 <= i && i < index)
            d[i] = value;
    }
    
    /**
     * Removes the double value from the DoubleVector at the specified index.
     * The subsequent values in the DoubleVector are then shifted down one
     * index position.
     *
     * @param i the position of the double value to remove
     */

    public void removeDoubleAt(int i){
        for(int j=i; j<index-1; j++){
            d[j] = d[j+1];
        }
    }
    
    /**
     * Inserts the double argument into the DoubleVector at the specified index.
     * The double value at the specified index and all subsequent values in     
     * the DoubleVector are then shifted up one index position.
     *
     * @param value the double value to be inserted into the DoubleVector
     * @param i     the index position of the DoubleVector into which the
     *              double value argument is to be inserted
     */

    public void insertDoubleAt(double value, int i){
        if(0 <= i && i < index){
            testDoubleArrayLength();
            for(int j=index; j > i; j--){
                d[j] = d[j-1];
            }

            d[i] = value;
        }
    }
    
    /**
     * Trims the capacity of this DoubleVector to be the DoubleVector's 
     * current size. This can be used to minimize the storage of a DoubleVector. 
     */

    public void trimToSize(){
        double[] newArray = new double[index];

        for(int i=0; i<index; i++){
            newArray[i] = d[i];
        }

        d = newArray;
    }

    /**
     * Returns the size of the DoubleVector, or the number of double values
     * in the DoubleVector.
     */
     
    public int size(){
        return(index);
    }
    
    /**
     * Returns the array of doubles, trimmed to size, that this DoubleVector
     * contains.
     *
     * @return the double[] of the values contained by the DoubleVector.  The
     *         length of the returned double vector is the current size of the
     *         DoubleVector; in other words, the exact number of double values 
     *         in the DoubleVector.
     */

    public double[] todoubleArray(){
        trimToSize();
        return(d);
    }

    /*
     *************************************************************************************
     *
     * PRIVATE METHODS FOR MAINTAINING THE DoubleVECTOR
     *
     **************************************************************************************/

    /**
     * Tests the adequacy of the current capacity by comparing the length
     * of the current double array against the number of double values 
     * contained in the array.  If the number of double values in the 
     * DoubleVector is equal to the length of the double array, the array
     * needs to be incremented, or expanded.
     */
    
    private void testDoubleArrayLength(){
        if(index >= d.length) incrementDoubleArray();
    }
    
    /**
     * Expands the current double array by the amount specified in the 
     * <code>increment</code> variable.
     */

    private void incrementDoubleArray(){
        double[] newArray = new double[d.length+increment];

        for(int i=0; i<d.length; i++){
            newArray[i] = d[i];
        }

        d = newArray;
    }
}


