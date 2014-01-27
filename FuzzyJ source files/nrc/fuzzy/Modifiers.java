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

import java.lang.*;
import java.util.*;
import java.io.*;

/**
  * This class maintains a list (in a hash table) of the fuzzy modifiers (or hedges)
  * that are available to users. Modifiers manipulate a fuzzy value/fuzzy set
  * returning a new fuzzy value/fuzzy set changed according to the modifier
  * objectives. For example, the modifier 'very' squares each membership value
  * in a fuzzy set.
  * <p>Modifiers are also used in linguistic expressions when defining FuzzyValues.
  * For example we might create the FuzzyValue:
  *<br><pre>
  *    FuzzyValue fv = new FuzzyValue(tempFvar, "very hot");
  </pre>
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
  * </code>
  * The second form uses the Modifiers class to perform a 'variable' modifier method and
  * is used internally by the fuzzy package when creating fuzzy values or
  * fuzzy terms using 'linguistic expressions'.
  * </ul>
  * For convenience an instance of each of the built-in (system supplied) modifiers is
  * created in the Modifiers class and a method is created for each one
  * allowing them to be accessed in a simpler fashion:
  * <code>
  *      aModifiedFuzzyValue = Modifers.very(aFuzzyValue);
  * </code></pre>
  * NOTE: all modifier names are case insensitive ('very' is the same as 'VERY')
  *
  * @author Bob Orchard
  *
  * @see ModifierFunction
  */

public class Modifiers extends java.lang.Object implements Serializable
{
    /** A hashtable that stores all available modifier functions */
    private static Hashtable modifierFunctions;

    static
    {
        modifierFunctions = new Hashtable();
    }

    /** 
     * Adds a modifier function to the hashtable of modifiers.
     * If the modifier name already exists it is replaced by this modifier function.
     * NOTE: all modifier names are case insensitive ('very' is the same as 'VERY')
     *
     * @param mf the modifier function instance
     */
    public static void add( ModifierFunction mf )
    { 
      String name = mf.getName();

      if (modifierFunctions.get(name) != null)
         modifierFunctions.remove(name);

      modifierFunctions.put(name, mf);
    }

    /** 
     * Checks to see if the specified modifier exists.
     *
     * @param modifierName the modifier name as a string
     * @return true if the modifier is available, else false
     */
    public static boolean isModifier( String modifierName )
    {
        if (modifierFunctions.get(modifierName.toLowerCase()) == null)
           return false;
        else
           return true;
    }


    /** 
     * Retrieves the list of modifier names in an array.
     *
     * @return an array of strings with the names of the modifiers
     */
    public static String[] getModifierNames()
    {
        String[] names = new String[modifierFunctions.size()];
        int i = 0;

        for (Enumeration e = modifierFunctions.keys(); e.hasMoreElements();)
        {
            names[i++] = (String)e.nextElement();
        }

        return names;
    }


    /** 
     * Modify the FuzzyValue specified using the ModifierFunction
     * associated with the specified modifier name 
     *
     * @param modifierName the modifier name as a string
     * @param the FuzzyValue to modifiy
     * @return a FuzzyValue that holds the modified fuzzy set or null
     *         if the modifier function cannot be performed
     */
    public static FuzzyValue call( String modifierName, FuzzyValue fv )
    {
        ModifierFunction mf;

        mf = (ModifierFunction)modifierFunctions.get(modifierName.toLowerCase());
        if (mf == null)
           return null;
        else
           return mf.call(fv);
    }

    /** 
     * Modify the FuzzySet specified using the ModifierFunction
     * associated with the specified modifier name 
     *
     * @param modifierName the modifier name as a string
     * @param fs the FuzzySet to modifiy
     * @return a FuzzyValue that holds the modified fuzzy set or null
     *         if the modifier function cannot be performed
     */
    public static FuzzySet call( String modifierName, FuzzySet fs )
    {
        ModifierFunction mf;

        mf = (ModifierFunction)modifierFunctions.get(modifierName.toLowerCase());
        if (mf == null)
           return null;
        else
           return mf.call(fs);
    }

    /*
     *****************************************************************************
     * Methods/variables that support simple calls for built-in modifier functions
     ****************************************************************************/
     
     /** Instance of built-in ModifierFunction for the modifier 'not' */
     private static NotModifier notMod = new NotModifier();
     /** Instance of built-in ModifierFunction for the modifier 'very' */
     private static VeryModifier veryMod = new VeryModifier();
     /** Instance of built-in ModifierFunction for the modifier 'extremely' */
     private static ExtremelyModifier extremelyMod = new ExtremelyModifier();
     /** Instance of built-in ModifierFunction for the modifier 'somewhat' */
     private static SomewhatModifier somewhatMod = new SomewhatModifier();
     /** Instance of built-in ModifierFunction for the modifier 'more_or_less' */
     private static MoreorlessModifier moreorlessMod = new MoreorlessModifier();
     /** Instance of built-in ModifierFunction for the modifier 'plus' */
     private static PlusModifier plusMod = new PlusModifier();
     /** Instance of built-in ModifierFunction for the modifier 'norm' */
     private static NormModifier normMod = new NormModifier();
     /** Instance of built-in ModifierFunction for the modifier 'slightly' */
     private static SlightlyModifier slightlyMod = new SlightlyModifier();
     /** Instance of built-in ModifierFunction for the modifier 'intensify' */
     private static IntensifyModifier intensifyMod = new IntensifyModifier();
     /** Instance of built-in ModifierFunction for the modifier 'above' */
     private static AboveModifier aboveMod = new AboveModifier();
     /** Instance of built-in ModifierFunction for the modifier 'below' */
     private static BelowModifier belowMod = new BelowModifier();

     /**
      * Execute the 'not' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see NotModifier
      */
     public static FuzzyValue not(FuzzyValue fv)
     {
        return notMod.call(fv);
     }

     /**
      * Execute the 'not' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see NotModifier
      */
     public static FuzzySet not(FuzzySet fs)
     {
        return notMod.call(fs);
     }


     /**
      * Execute the 'very' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see VeryModifier
      */
     public static FuzzyValue very(FuzzyValue fv)
     {
         return veryMod.call(fv);
     }

     /**
      * Execute the 'very' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see VeryModifier
      */
     public static FuzzySet very(FuzzySet fs)
     {
        return veryMod.call(fs);
     }

     /**
      * Execute the 'extremely' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see ExtremelyModifier
      */
     public static FuzzyValue extremely(FuzzyValue fv)
     {
        return extremelyMod.call(fv);
     }

     /**
      * Execute the 'extremely' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see ExtremelyModifier
      */
     public static FuzzySet extremely(FuzzySet fs)
     {
        return extremelyMod.call(fs);
     }

     /**
      * Execute the 'somewhat' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see SomewhatModifier
      */
     public static FuzzyValue somewhat(FuzzyValue fv)
     {
        return somewhatMod.call(fv);
     }

     /**
      * Execute the 'somewhat' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see SomewhatModifier
      */
     public static FuzzySet somewhat(FuzzySet fs)
     {
        return somewhatMod.call(fs);
     }

     /**
      * Execute the 'more_or_less' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see MoreorlessModifier
      */
     public static FuzzyValue more_or_less(FuzzyValue fv)
     {
        return moreorlessMod.call(fv);
     }

     /**
      * Execute the 'more_or_less' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see MoreorlessModifier
      */
     public static FuzzySet more_or_less(FuzzySet fs)
     {
        return moreorlessMod.call(fs);
     }

     /**
      * Execute the 'plus' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see PlusModifier
      */
     public static FuzzyValue plus(FuzzyValue fv)
     {
        return plusMod.call(fv);
     }

     /**
      * Execute the 'plus' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see PlusModifier
      */
     public static FuzzySet plus(FuzzySet fs)
     {
        return plusMod.call(fs);
     }

     /**
      * Execute the 'norm' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see NormModifier
      */
     public static FuzzyValue norm(FuzzyValue fv)
     {
        return normMod.call(fv);
     }

     /**
      * Execute the 'norm' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see NormModifier
      */
     public static FuzzySet norm(FuzzySet fs)
     {
        return normMod.call(fs);
     }

     /**
      * Execute the 'slightly' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see SlightlyModifier
      */
     public static FuzzyValue slightly(FuzzyValue fv)
     {
        return slightlyMod.call(fv);
     }

     /**
      * Execute the 'slightly' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see SlightlyModifier
      */
     public static FuzzySet slightly(FuzzySet fs)
     {
        return slightlyMod.call(fs);
     }

     /**
      * Execute the 'intensify' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see IntensifyModifier
      */
     public static FuzzyValue intensify(FuzzyValue fv)
     {
        return intensifyMod.call(fv);
     }

     /**
      * Execute the 'intensify' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see IntensifyModifier
      */
     public static FuzzySet intensify(FuzzySet fs)
     {
        return intensifyMod.call(fs);
     }

     /**
      * Execute the 'above' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see AboveModifier
      */
     public static FuzzyValue above(FuzzyValue fv)
     {
        return aboveMod.call(fv);
     }

     /**
      * Execute the 'above' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see AboveModifier
      */
     public static FuzzySet above(FuzzySet fs)
     {
        return aboveMod.call(fs);
     }

     /**
      * Execute the 'below' modifier on a FuzzyValue
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy value
      *
      * @see BelowModifier
      */
     public static FuzzyValue below(FuzzyValue fv)
     {
        return belowMod.call(fv);
     }

     /**
      * Execute the 'below' modifier on a FuzzySet
      *
      * @param fv the fuzzy value to be modified
      * @return the modified fuzzy set
      *
      * @see BelowModifier
      */
     public static FuzzySet below(FuzzySet fs)
     {
        return belowMod.call(fs);
     }

}
