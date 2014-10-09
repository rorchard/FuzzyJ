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
import jess.*;
import java.util.Vector;
import java.io.*;

/**
 * Extends the Jess Token Class to allow the NRC FuzzyJ
 * extensions to be used.
 *
 * @author Ernest J. Friedman-Hill
 * @author Bob Orchard
 * 
 */

public class FuzzyToken extends Token implements Serializable
{ 
  /**
   * The Parent of this token. This attribute is added so that it 
   * is possible to traverse the tokens associated with an activation 
   * (these hold the facts that matched on the LHS of a rule).
   */
  private FuzzyToken m_theParent;
   
  public FuzzyToken(Fact firstFact) throws JessException
  {
    super(firstFact);
  }

  public FuzzyToken(Token t, Fact newFact) throws JessException
  {
    super(t, newFact);
    m_theParent = (FuzzyToken) t;
  }

  public FuzzyToken(Token lt, Token rt) throws JessException
  {
    super(lt, rt);
    // ????? could we not just assign eData to m_extensionData ????
    Vector eData = ((FuzzyToken) rt).getExtensionData();
    if (eData != null)
      {
        for (int j=0; j<eData.size(); j++)
          addExtensionData(eData.elementAt(j));
      }    
    m_theParent = (FuzzyToken) lt;
  }

  public FuzzyToken(Token t) throws JessException
  {
    super(t);
    m_theParent = ((FuzzyToken) t).m_theParent;
  }

  /** Data needed to support Jess (Fuzzy) Extensions can be carried around
   * in this special Vector. In general this is added to support the
   * need to store 'special' information with facts as they work their
   * way through the rete network. When the RHS of a rule fires the
   * m_token associated with the activation will have a list of all of
   * facts that contributed to the rule's firing. The 'special' information
   * can be added during function calls that are made during the
   * tests that are performed as patterns are matched. In general this 
   * information will be attached to the token that holds the fact as 
   * it moves through the net to a terminal node. It is 
   * required that the data stored be associated with one and only one 
   * traversal of the path. Since a token, with its fact, can actually
   * take several paths through the net, it is sometimes necessary that
   * the 'special' data be added to a new token (a copy of the
   * current token with the extension data added to it) so that it 
   * becomes a unique token from that point on in the traversal through
   * the net. This allows the system to identify that a 
   * new token should be formed and passAlong'd down the net from that point.
   * The original token will be used for other paths without the special
   * data. This is taken care of in various places in the Jess code with 
   * the addition of calls to the 'prepare' method. Other extensions can 
   * then use the same hooks to record any information required as the 
   * facts move through the net.
   *
   * The KEY thing here is that in Jess the 'prepare' method must be called 
   * after each pattern is matched AND any tests were done (in particular
   * for the fuzzy extension whenever the fuzzy-match function is called ...
   * but since this is a general mechanism prepare is called whenever any
   * test functions are called ... the prepare function has a single
   * parameter that is true when the pattern test was successful and false
   * when is was not. This lets the extension take action depending on
   * the success of the test(s). In the fuzzy extension the fuzzy-match 
   * function puts information in the token in the m_extension slot ... 
   * the pattern fuzzy value and the fact fuzzy value pair(s) that matched. 
   * If the tests for the pattern were successful then the token is duplicated
   * and the m_extensionData slot of the original token is cleared. If it
   * fails then the m_extensionData slot of the original is cleared (since
   * the info is not needed ... the pattern match failed).
   *
   */

  private Vector m_extensionData = null;
  
  /**
   * Add an Object to the m_extensionData Vector (in this case
   * the Object will always be a FuzzyValueVector so we could change this
   * Object to be a FuzzyValueVector).
   */
  public final void addExtensionData( Object obj )
  {
    if (m_extensionData == null)
        m_extensionData = new Vector();
        
    m_extensionData.addElement(obj);
  }
  
  /**
   * Get the Vector which is the extension data. 
   * Normally this is a Vector of FuzzyValueVectors or empty (null).
   */
  public final Vector getExtensionData()
  {
    return m_extensionData;
  }

  /**
   * Clear the values in the extension data Vector
   */
  public final void clearExtensionData()
  {
    m_extensionData = null;
  }


  /**
   * Get the Parent of this token. This method added so that it 
   * is possible to traverse the tokens associated with an activation 
   * (these hold the facts that matched on the LHS).
   *
   * This is used by the fuzzy extension to allow one to store the
   * fuzzy values that matched during a 'fuzzy-match' function call
   * during the pattern matching phase. 
   *
   * @return Returns the parent token of this token.
   */
  public FuzzyToken getParent()
  {
    return m_theParent;
  }

  /** 
   * Duplicate the token, if necessary, passing the extensionData
   * to the new FuzzyToken and clearing it from the original. This is
   * determined by the parameter to prepare. If true then the pattern
   * match for the fact was successful and if there was 'extensionData'
   * we need to pass this to the new duplicate token and remove it from 
   * the original token. If false then the pattern match failed so
   * we need to clear the extension data from the original token.
   * 
   * @param success true if the pattern match for the fact associated
   *        with this token was true, else false.
   *
   * @return Returns the token itself (this) with any extensionData removed
   *         if the pattern match just completed was unsuccessful
   *         or a new token that is a copy of this token with any extension 
   *         data passed along if the pattern match was successful.
   */
  public Token prepare( boolean success ) throws JessException
  {
     if (m_extensionData != null)
        { // must clear the extension data in original token
          // (after copying it to new token if pattern match was successful)
          if (success)
            { FuzzyToken tnew = new FuzzyToken(this);
              tnew.m_extensionData = m_extensionData;
              m_extensionData = null;
              return tnew;
            }
          // pattern match failed -- clear extension data and return this token
          m_extensionData = null;          
        }

      return this;
  }
  
}
