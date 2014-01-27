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
 * The <code>StringVector</code> class implements a growable array
 * of String objects. Like an array, the values can be accessed using an
 * integer index. However, the size of a StringVector can
 * grow as needed to accommodate the addition of Strings after the
 * StringVector has been created.
 * 
 * @author Bob Orchard
 *
 */
public class StringVector implements Serializable
{
    static private int INITIAL_CAPACITY = 30;
    static private int INCREMENT = 10;

    private String[] s;
    private int index;

    /***************************************************************************************
     *
     * CONSTRUCTORS
     *
     ***************************************************************************************/

    public StringVector(){
        s = new String[INITIAL_CAPACITY];
        index = 0;
    }

    public StringVector(int initialSize){
        INITIAL_CAPACITY = initialSize;

        s = new String[INITIAL_CAPACITY];
        index = 0;
    }

    public StringVector(int initialSize, int increment){
        INITIAL_CAPACITY = initialSize;
        INCREMENT = increment;

        s = new String[INITIAL_CAPACITY];
        index = 0;
    }

    /**************************************************************************************
     *
     * ACCESSIBLE METHODS FOR USING THE STRINGVECTOR
     *
     **************************************************************************************/

    public void addString(String string){
        testStringArrayLength();
        s[index++] = string;
    }

    public void concat(StringVector other){

        for(int i=0; i<other.size(); i++){
            testStringArrayLength();
            this.addString(other.StringAt(i));
        }
    }

   public void removeAllStrings(){
        index = 0;
    }

    public String StringAt(int i){
        return(s[i]);
    }

    void reset(){
        index = 0;
        s = new String[INITIAL_CAPACITY];
    }

    public void trimToSize(){
        String[] newArray = new String[index];

        for(int i=0; i<index; i++){
            newArray[i] = s[i];
        }

        s = newArray;
    }

    public int size(){
        return(index);
    }

    public String[] toStringArray(){
        trimToSize();
        return(s);
    }


    /**************************************************************************************
     *
     * PRIVATE METHODS FOR MAINTAINING THE STRINGVECTOR
     *
     **************************************************************************************/

    private void testStringArrayLength(){
        if(index >= s.length) incrementStringArray();
    }

    private void incrementStringArray(){
        String[] newArray = new String[s.length+INCREMENT];

        for(int i=0; i<s.length; i++){
            newArray[i] = s[i];
        }

        s = newArray;
    }

    private void incrementStringArray(int newSize){
        String[] newArray = new String[newSize];

        for(int i=0; i<s.length && i<newSize; i++){
            newArray[i] = s[i];
        }

        s = newArray;
    }
}


