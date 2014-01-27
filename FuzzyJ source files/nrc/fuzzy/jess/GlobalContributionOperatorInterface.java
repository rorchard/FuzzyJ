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


package nrc.fuzzy.jess;

import nrc.fuzzy.*;

/**
 * An interface for classes that provide an operator to globally combine
 * the fuzzy values of the output of fuzzy rules. By default
 * the union operator is used, but commonly the
 * sum operator is also used. There may also be other operators added as required.
 * 
 * @author Bob Orchard
 * @see nrc.fuzzy.jess.GlobalContributionOperator
 * @see nrc.fuzzy.jess.UnionGlobalContributionOperator
 * @see nrc.fuzzy.jess.SumGlobalContributionOperator
 */

public interface  GlobalContributionOperatorInterface
{
	/**
	 * Classes that implement the GlobalContributionOperatorInterface must provide
	 * an execute method that accepts 2 fuzzy values and
	 * returns a fuzzy value that is the result of doing the operation that
	 * the class implements (e.g. union or sum or none).
	 * 
	 * @param newFuzzyValue  should be the new FuzzyValue being asserted
	 * @param existingFuzzyValue  should be the existing FuzzyValue
	 * @return FuzzyValue that is the result of applying the operator to the 2 values
	 * 
	 */
  public FuzzyValue execute( FuzzyValue newFuzzyValue, FuzzyValue existingFuzzyValue )
          throws IncompatibleFuzzyValuesException, XValueOutsideUODException;
 
}
