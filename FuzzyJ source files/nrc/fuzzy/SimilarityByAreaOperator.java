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
 * A class that provides an operator to calculate
 * the similarity between 2 fuzzy values. The similarity
 * measure is the ratio of area of intersection
 * to area of union of the fuzzy values.
 * <p>
 * The default similarity operator is the SimilarityByPossibilityOperator. 
 * This can be changed using the static setDefaultSimilarityOperator method of the FuzzyValue class.
 * <p>
 * 
 * @author Bob Orchard
 * 
 * @see SimilarityOperator
 * @see SimilarityOperatorInterface
 * @see SimilarityByPossibilityOperator
*/

public class SimilarityByAreaOperator extends SimilarityOperator
       implements Serializable
{
  /**
   * SimilarityByAreaOperator provides
   * a similarity method that accepts 2 FuzzyValue objects and returns the 
   * measure of the similarity between them.
   * 
   * @param fuzzyValue1
   * @param fuzzyValue2
   * @return double that is the result of applying the operator to the 2 values (the similarity measure)
   * 
   */
  public double similarity( FuzzyValue fv1, FuzzyValue fv2 )
       throws IncompatibleFuzzyValuesException
  {
       // Check for identical fuzzy values 1st      
       if (fv1.equals(fv2)) 
       	 return( 1.0 );
       
       // calculate areas of the Fuzzy Intersect and fuzzy union
       FuzzySet fs1 = fv1.getFuzzySet();
       FuzzySet fs2 = fv2.getFuzzySet();
       FuzzySet intersection = fs1.fuzzyIntersection(fs2);
       FuzzySet union = fs1.fuzzyUnion(fs2);
       double xmin = fv1.getMinUOD();
       double xmax = fv1.getMaxUOD();
       // xmax should always be >= xmin ... but swap if not
       // and catch the XValuesOutOfOrderException that can NOT occur
       // so we don't have to pass it along to the next level
       if (xmin > xmax)
       { double temp = xmin;
         xmin = xmax;
         xmax = temp;
       }
       double intersectionArea, unionArea;
       try
       { intersectionArea = intersection.getArea(xmin, xmax);
         unionArea = union.getArea(xmin, xmax);
       }
       catch (XValuesOutOfOrderException e)
       {  return( 0.0 );
       }
       
       // result is expressed as ratio of intersection area to union area
       if ( unionArea == 0.0)
   	   	  return( 0.0 );
       return(intersectionArea/unionArea );
  }
 
}
