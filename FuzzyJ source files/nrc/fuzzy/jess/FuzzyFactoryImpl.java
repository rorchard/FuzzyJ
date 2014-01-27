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

import java.io.Serializable;
import jess.*;
import jess.factory.Factory;

/**
 * FuzzyFactory implementation (perhaps should be called TokenFactory 
 * instead of Factory) that provides newToken methods that return FuzzyToken 
 * objects instead of standard Jess Token objects when using the FuzzyJ
 * extensions. FuzzyToken is an extension of the Jess Token class.
 * 
 * @author Ernest J. Friedman-Hill
 * @author Bob Orchard
 * 
 * @see FuzzyToken
 */

public class FuzzyFactoryImpl implements Factory, Serializable 
{
  public Token newToken(Fact firstFact, int tag) throws JessException
  {
    return new FuzzyToken(firstFact, tag);
  }

  public Token newToken(Token t, Fact newFact) throws JessException
  {
    return new FuzzyToken(t, newFact);
  }

  public Token newToken(Token lt, Token rt) throws JessException
  {
    return new FuzzyToken(lt, rt);
  }

  public Token newToken(Token t) throws JessException
  {
    return new FuzzyToken(t);
  }
}
