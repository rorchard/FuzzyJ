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
 * An abstract class that can be sub-classed to provide
 * an execute method that performs a desired operation on antecedent/input
 * pair match values as a rule is being executed. By default
 * the minimum operator (minimum of all match values) is used , but commonly the 
 * product operator (product of all match values) is also used.
 * 
 * @author Bob Orchard
 *
 * @see ProductAntecedentCombineOperator
 * @see MinimumAntecedentCombineOperator
 * @see CompensatoryAndAntecedentCombineOperator
 * @see FuzzyRule
 */
public abstract class AntecedentCombineOperator implements AntecedentCombineOperatorInterface, 
                        Cloneable, Serializable
{
}

