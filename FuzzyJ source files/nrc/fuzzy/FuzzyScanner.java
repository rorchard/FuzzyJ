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


// Simple Scanner Class for Fuzzy Linguistic expressions

package nrc.fuzzy;

import java.lang.*;
import java.io.*;
import java_cup.runtime.*;

/**
 * The FuzzyScanner class provides support for scanning fuzzy linguistic
 * expressions such as "very hot or not cold" and return a token (Symbol).
 * It requires and supports a parser/linguistic expression evaluator
 * that was generated using the javaCUP utility for generating parsers.
 * The file FuzzyParser.cup is the input to the javaCUP program.
 * It defines the syntax of the linguistic expressions as well as
 * the actions to perform to evaluate such expressions that will reulst
 * in the creation of a FuzzyValue that represents the expression.
 * <p>
 * NOTE: all linguistic expressions are case insensitive!!
 * <p>
 * Below is the contents of the cup file (note it is for information purposes
 * only and may not reflect the current state of the cup file). Note that
 * javaCUP by default assumes that there will be fixed scanner with a
 * set of static routines to support the parser. However, in a multiple
 * thread situation with the threads each potentially having an instantiation of
 * the parser, a single fixed scanner will not work. So, we have added
 * a bit of code to allow the parser to be constructed with a scanner specified
 * when the parser is created. In this case we normally will instantiate
 * a parser, specifying a new scanner. The scanner constructor will identify the
 * FuzzyVariable that holds the linguistic terms for the fuzzy variable
 * and the string that will be parsed in this FuzzyVariable context.
 * <p>
 * <pre><code>
 *    package nrc.fuzzy;
 *
 *    import java_cup.runtime.*;
 *
 *    // modifications to the parser
 *    parser code {:
 *        // new constructor to allow the scanner to be specified at runtime
 *        //   - without this sort of addition we cannot easily have multiple
 *        //     threads using the same parser
 *        //   - NOTE: should NOT use the parser constructor without arguments!
 *        //
 *        public FuzzyParser(FuzzyScanner fs)
 *          { super();
 *             theScanner = fs;
 *           }
 *
 *        // Varaible that holds the fuzzy scanner
 *        FuzzyScanner theScanner;
 *    :};
 *
 *
 *    // Preliminaries to set up and use the scanner.
 *    init with {:
 *        if (theScanner == null)
 *          report_error("No scanner was specified when the FuzzyParser was constructed", null);
 *        else
 *          theScanner.init();
 *    :};
 *    scan with {:
 *       if (theScanner == null)
 *         {
 *          report_error("No scanner was specified when the FuzzyParser was constructed", null);
 *          return new java_cup.runtime.Symbol(error_sym());
 *         }
 *       else
 *         return theScanner.next_token();
 *    :};
 *
 *    // Terminals (tokens returned by the scanner).
 *    terminal             AND, OR;
 *    terminal             LPAREN, RPAREN;
 *    terminal String      FUZZYMODIFIER;
 *    terminal FuzzyValue  FUZZYTERM;
 *
 *    // Non terminals
 *    non terminal FuzzyValue  lexpr;
 *
 *    // Precedences
 *    precedence left OR;
 *    precedence left AND;
 *    precedence left FUZZYMODIFIER;
 *
 *    // The grammar of a linguistic expression (lexpr)
 *
 *    lexpr   ::= lexpr:e1 OR lexpr:e2
 *    	      {: RESULT = e1.fuzzyUnion(e2); :}
 *    	      |
 *                  lexpr:e1 AND lexpr:e2
 *                  {: RESULT = e1.fuzzyIntersection(e2); :}
 *    	      |
 *                  FUZZYTERM:fval
 *    	      {: RESULT = fval; :}
 *    	      |
 *                  FUZZYMODIFIER:fmod lexpr:e
 *    	      {: RESULT = Modifiers.call(fmod, e); :}
 *    	      |
 *                  LPAREN lexpr:e RPAREN
 *    	      {: RESULT = e; :}
 *    	      ;
 * </code></pre>
 * <p>
 * This when passed through the javaCUP program using a command like:
 * <pre><code>
 *    java -classpath d:\javaCUP;%classpath% java_cup.Main
 *         -package nrc.fuzzy -parser FuzzyParser
 *         -symbols FuzzyParserSym < e:\fuzzyparser\FuzzyParser.cup
 * </code></pre>
 * will create 2 files, FuzzyParserSym.java and FuzzyParser.java.
 * <p> The use of parsing and evaluation of a linguistic expression 
 * can be seen in the following code snippet from one of the 
 * FuzzyValue constructors:
 * <pre><code>
 *    // create a parsing object providing the FuzzyVariable and the expression
 *    FuzzyParser parser_obj = 
 *        new FuzzyParser(new FuzzyScanner(fuzzyVariable, linguisticExpression));
 *    FuzzyValue fval = null;
 *      
 *    try
 *     { // parse and evaluate the expression
 *       fval = (FuzzyValue)(parser_obj.parse().value);
 *     }
 *    catch (Exception e)
 *     { throw new InvalidLinguisticExpressionException();} 
 * </code></pre>
 *
 * @author Bob Orchard
 *
 */

class FuzzyScanner implements Serializable
{
  /** the string (linguistic expression) to parse */
  private /*static*/ String lexpr;

  /**
   * The start position of the current token within the 
   * linguistic expression being parsed 
   */
  private/*static*/ int startOfToken;

  /** 
   * The end position of the current token within the 
   * linguistic expression being parsed
   */
  private /*static*/ int endOfToken;

  /** 
   * The fuzzy variable that holds the fuzzy terms (hot, cold etc.) 
   * that can be used in the linguistic expression
   */
  protected /*static*/ FuzzyVariable fvar;

  /* constructor */
  FuzzyScanner( FuzzyVariable fvar, String lexpr )
  {
    this.fvar = fvar;
    this.lexpr = lexpr.toLowerCase();
    this.startOfToken = 0;
    this.endOfToken = 0;
  }


  /** Initialize the scanner (for this scanner there is nothing to do). */
  public void init()
    {  }

  /**
   * Recognize and return the next complete token from a linguistic
   * expression such as "very hot and (not cold or warm)".
   * The tokens in this case will be 'very', 'hot', 'and', '(', 'not', etc.
   */
  public Symbol next_token()
    {
      int i;
      char ch;
      String token;
      FuzzyValue fval;

      /* remove whitespace at beginning of remaining string */
      while (startOfToken < lexpr.length() && Character.isWhitespace(lexpr.charAt(startOfToken)))
        {
          startOfToken++;
        }

      /* if at end of lexpr string return the EOF token */
      if (startOfToken >= lexpr.length())
         return new Symbol(FuzzyParserSym.EOF, startOfToken, startOfToken);

      /* look for the ( and ) symbols */
      if (lexpr.charAt(startOfToken) == '(')
         return new Symbol(FuzzyParserSym.LPAREN, startOfToken, ++startOfToken);
      if (lexpr.charAt(startOfToken) == ')')
         return new Symbol(FuzzyParserSym.RPAREN, startOfToken, ++startOfToken);

      /* get next word (terminated by whitespace, (, ), or end of string) */
      for (i = startOfToken; i < lexpr.length(); i++)
      {
         ch = lexpr.charAt(i);
         if (Character.isWhitespace(ch) ||
             (ch == '(') ||
             (ch == ')')
            )
             break;
      }
      endOfToken = i;
      token = lexpr.substring(startOfToken, endOfToken);

      try
      {
          if (token.equals("or"))
             return new Symbol(FuzzyParserSym.OR, startOfToken, endOfToken);
          if (token.equals("and"))
             return new Symbol(FuzzyParserSym.AND, startOfToken, endOfToken);

          fval = fvar.findTerm(token);
          if (fval != null)
             return new Symbol(FuzzyParserSym.FUZZYTERM, startOfToken, endOfToken, fval);

          if (Modifiers.isModifier(token))
             return new Symbol(FuzzyParserSym.FUZZYMODIFIER, startOfToken, endOfToken, token);

          return new Symbol(FuzzyParserSym.error, startOfToken, startOfToken);
      }
      finally
      {   /* move past the token for the next call to next_token */
          startOfToken = endOfToken;
      }
    }
};
