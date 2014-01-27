/*
 * @(#)FuzzyPendulumModel.java	1.0 2000/06/10
 *
 * Copyright (c) 1998 National Research Council of Canada.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * the National Research Council of Canada. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into
 * with the National Research Council of Canada.
 *
 * THE NATIONAL RESEARCH COUNCIL OF CANADA MAKES NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. NRC SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * Copyright Version 1.0
 *
 */
package examples.fuzzypendulum;

import javax.swing.*;
import nrc.fuzzy.*;

/** 
 * Class to simulate an inverse pendulum balancing (mass balanced on a stick,  
 * with motor at base to apply force to keep mass balanced).
 * 
 * The mass can be 'bopped' by a rod from the left (it hits the mass pushing it
 * to the right).  The mass can be set to 'bob' up and down (as if it were
 * on a spring rather than a rigid stick). The size of the mass and the motor 
 * can be varied. 
 *
 * There are 11 rules that control the balancing. These can be enabled or 
 * disabled (an enhancement that might be considered is to allow the rules to
 * be dynamically modifed; add new rules, change the membership functions 
 * of existing rules, etc.) The rules each have 2 fuzzy antecedent conditions 
 * and a single fuzzy conclusion. On each cycle of the simulation the rules are 
 * considered for firing, given the current situation (position and motion of
 * the pendulum). The rules look like:
 *
 *    if   error is Z
 *    and  omega is Z
 *    then current is Z
 *
 * where:
 *
 * error - deviation of the pendulum position from vertical (PI/2)
 * omega - angular velocity of the pendulum
 * 
 * and various terms describe the degrees of error, omega and current,
 *
 * NM - negative medium 
 * NS - negative small
 * Z - zero
 * PS - positive small
 * PM - positive medium
 *
 * Each rule that 'fires' may produce a current fuzzy value. All of these 
 * outputs are combined to form an aggregated result (union of the fuzzy
 * sets) which is then defuzzified to produce a final 'global' value for the
 * current. The current is set to this value and the next iteration is done.
 * The current output determined by the rules is a value between -1.0 and 1.0.
 * This is scaled by the size of the motor to get the actual current.
 */

public class FuzzyPendulumModel extends java.lang.Thread
{
    // Some constants
    final static double G = 9.80666;  // gravity constant
    final static double dT = 0.06;    // time delta (.04 was original value, use < 0.1
                                      // or it reacts too slow to control the system)
    final static double dT2 = dT*dT;  // time delta squared
    final static double ERROR_MIN = -Math.PI/2, 
                        ERROR_MAX = Math.PI/2;
    final static double OMEGA_MIN = -8.5, 
                        OMEGA_MAX = 8.5;
    final static double CURRENT_MIN = -1.0, 
                        CURRENT_MAX = 1.0;
    
    // state information for the simulation
    boolean bopping = true;  // true is bopping is being done (hit the mass from the left)
    boolean bobbing = false; // true if mass if to 'bob' up and down
    boolean stepMode = false;// true if doing one step of the simulation at a time
    int stepsToDo = 0;       // number of steps pending in step mode
    
    double motorSize;        // size of the motor (max current for Motor, Amps)
    double minMotorSize;     // min size of the motor
    double maxMotorSize;     // max size of the motor
    
    double massSize;         // size of the mass (kg)
    double minMassSize;      // min size of the mass
    double maxMassSize;      // max size of the mass
    
    double stickLength;      // current length of the stick/pendulum (meters)
    double minStickLength;   // min length of the stick
    double maxStickLength;   // max length of the stick
    double stickDirection;   // +1 or -1 as the stick 'bobs' up and down
    
    double theta;            // actual angle of pendulum (0 to PI radians)
    double thetaMin;         // min value for theta
    double thetaMax;         // max value for theta
    double requiredTheta;    // the required value of Theta (PI/2 : vertical)
    
    double error;            // error from required theta value
    double omega;            // angular velocity of the pendulum
    double current;          // output current (from -1.0 to 1.0; scaled by motor size)
        
    FuzzyValue globalCurrentFVal = null; // aggregated result of all rules firing
    
    FuzzyPendulumJApplet simGraphics; // the GUI attached to the simulation
    
    int freezeSeconds = 0;   // number of seconds to freeze the simulation
    
    private boolean suspendRequested = false; // set to true if this thread should be suspended
    
    // *************************************************
    // ***** The Fuzzy Rule definition components ******
    // *************************************************
    final int NUM_RULES = 11;
    // The names associated with the rules ... external reference to rules
    // must use these names ... "Z_NM_PM" means 
    //        if error is Z and omega is NM 
    //        then current is PM
    String[] ruleNames = {"Z_NM_PM", "Z_NS_PS", "PS_NS_Z", "NM_Z_PM", 
                          "NS_Z_PS", "Z_Z_Z", "PS_Z_NS", "PM_Z_NM", 
                          "NS_PS_Z", "Z_PS_NS", "Z_PM_NM"
                         };
    // identifies if a rule is being used (true => enabled, false => diabled)
    boolean[] rulesEnabled = {true, true, true, true, true, true, 
                             true, true, true, true, true
                            };
    // identifies if a rule was fired in the last iteration
    boolean[] rulesFired = {false, false, false, false, false, false, 
                             false, false, false, false, false
                          };
    // the names of the error antecedants, omega antecedents and current
    // conclusions for each rule
    String[] errorAntecedents = {"Z", "Z", "PS", "NM", "NS", "Z", 
                                 "PS", "PM", "NS", "Z", "Z"};                       
    String[] omegaAntecedents = {"NM", "NS", "NS", "Z", "Z", "Z",
                                  "Z", "Z", "PS", "PS", "PM"};    
    String[] currentConclusions = {"PM", "PS", "Z", "PM", "PS", "Z",
                                   "NS", "NM", "Z", "NS", "NM"};                       
    // the fuzzy rules
    FuzzyRule[] fuzzyRules = new FuzzyRule[NUM_RULES];
    

    // define the fuzzy terms for each fuzzy variable
    FuzzyVariable errorFVar;
    FuzzyVariable omegaFVar;
    FuzzyVariable currentFVar;
    
    final int numErrorFuzzyTerms = 5;
    final int numOmegaFuzzyTerms = 5;
    final int numCurrentFuzzyTerms = 5;
    
    String[] termNames = {"NM", "NS", "Z", "PS", "PM"};
    
    FuzzySet[] errorFuzzySets = new FuzzySet[numErrorFuzzyTerms];
    FuzzySet[] omegaFuzzySets = new FuzzySet[numOmegaFuzzyTerms];
    FuzzySet[] currentFuzzySets = new FuzzySet[numCurrentFuzzyTerms];
             
    /**
     * Create a model of the fuzzy pendulum, setting the appropriate values
     * for the mass, motor, stick, theta, and fuzzy rules etc.
     */
    public FuzzyPendulumModel( FuzzyPendulumJApplet a )
    {
        int i;
        suspendRequested = false;
        simGraphics = a;
        
        massSize = 2.5;       // mass is 0.5 kg to 3.5 kg
        minMassSize = 0.5;
        maxMassSize = 3.5;
        
        motorSize = 100.0;    // motor size is from 60 to 180 Amps (or mA?)
        minMotorSize = 60.0;
        maxMotorSize = 180.0;
        
        stickLength = 1.3;    // stick can vary from 0.7 to 1.3 m
        minStickLength = 0.7;
        maxStickLength = 1.3;
        stickDirection = -0.1;
        
        theta = Math.PI/2.0;         // start vertical
        requiredTheta = Math.PI/2.0; // want to keep it vertical
        thetaMin = 0.0;              // min value for theta
        thetaMax = Math.PI;          // max value for theta
        error = 0.0;                 // initial error is 0.0 (balanced)
        omega = 0.0;                 // initial angular velocity is 0.0 (balanced)
        current = 0.0;               // initial current is 0.0 (balanced)
        setBopping( true );          // initially bopping in ON
        setBobbing( false );         // initially bobbing (up/down) is OFF
        setStepMode( false);         // initially NOT in step mode
        
        // define the fuzzy variables and their terms and the fuzzy rules
        try 
        {
          errorFVar = new FuzzyVariable("error", ERROR_MIN, ERROR_MAX);
          omegaFVar = new FuzzyVariable("omega", OMEGA_MIN, OMEGA_MAX);
          currentFVar = new FuzzyVariable("current", CURRENT_MIN, CURRENT_MAX);
        
          errorFuzzySets[0] = new RFuzzySet(-1.28, -0.64, new RightLinearFunction());
          errorFuzzySets[1] = new TriangleFuzzySet(-1.28, -0.64, 0);
          errorFuzzySets[2] = new TriangleFuzzySet(-0.64, 0, 0.64);
          errorFuzzySets[3] = new TriangleFuzzySet(0, 0.64, 1.28);
          errorFuzzySets[4] = new LFuzzySet(0.64, 1.28, new LeftLinearFunction());
             
          omegaFuzzySets[0] = new RFuzzySet(-4.27, -2.13, new RightLinearFunction());
          omegaFuzzySets[1] = new TriangleFuzzySet(-4.27, -2.13, 0);
          omegaFuzzySets[2] = new TriangleFuzzySet(-2.13, 0, 2.13);
          omegaFuzzySets[3] = new TriangleFuzzySet(0, 2.13, 4.27);
          omegaFuzzySets[4] = new LFuzzySet(2.13, 4.27, new LeftLinearFunction());
             
          currentFuzzySets[0] = new RFuzzySet(-1.0, -0.25, new RightLinearFunction());
          currentFuzzySets[1] = new TriangleFuzzySet(-0.5, -0.25, 0);
          currentFuzzySets[2] = new TriangleFuzzySet(-0.25, 0, 0.25);
          currentFuzzySets[3] = new TriangleFuzzySet(0, 0.25, 0.5);
          currentFuzzySets[4] = new LFuzzySet(0.25, 1.0, new LeftLinearFunction());
       
          for (i=0; i<numErrorFuzzyTerms; i++)
            errorFVar.addTerm(termNames[i], errorFuzzySets[i]);

          for (i=0; i<numOmegaFuzzyTerms; i++)
            omegaFVar.addTerm(termNames[i], omegaFuzzySets[i]);
    
          for (i=0; i<numErrorFuzzyTerms; i++)
            currentFVar.addTerm(termNames[i], currentFuzzySets[i]);
            
          for (i=0; i<NUM_RULES; i++)
          { fuzzyRules[i] = new FuzzyRule();
            fuzzyRules[i].addAntecedent(new FuzzyValue(errorFVar, errorAntecedents[i]));
            fuzzyRules[i].addAntecedent(new FuzzyValue(omegaFVar, omegaAntecedents[i]));
            fuzzyRules[i].addConclusion(new FuzzyValue(currentFVar, currentConclusions[i]));
          }
        }
        catch (FuzzyException fe)
        {}
    }
    
    
    void requestSuspend()
    {
        suspendRequested = true;
    }
    
    /** Set the state of 'bopping' for the simulation.
     *
     * @param b true when bopping is enabled
     */
    void setBopping( boolean b )
    {
        bopping = b;
    }
    
    /** Get the state of bopping in the simulation
     *
     * @return true when bopping is enabled 
     */
    boolean isBopping()
    {
        return bopping;
    }
        
    /** Set the state of 'bobbing' (up/down motion) for the simulation.
     *
     * @param b true when bobbing is enabled
     */
    void setBobbing( boolean b )
    {
        bobbing = b;
    }
    
    /** Get the state of bobbing in the simulation
     *
     * @return true when bobbing is enabled 
     */
    boolean isBobbing()
    {
        return bobbing;
    }
    
    void setMassSize( double size )
    /**
     * Set the size of the mass on the pendulum.
     * Up to the user to keep values between min and max sizes.
     * The user can get the min and max sizes and scale appropriately.
     *
     * @param size the value to set for the size of the mass on the pendulum
     */
    {
        if (size > maxMassSize) massSize = maxMassSize;
        else if (size < minMassSize) massSize = minMassSize;
        else massSize = size;
    }
    
    /** Get the current size of the mass on the pendulum.
     *
     * @return the current size of the mass (KG).
     */
    double getMassSize()
    {
        return massSize;
    }
        
    /** Get the current maximum size of the mass on the pendulum.
     *
     * @return the current maximum size of the mass (KG).
     */
    double getMaxMassSize()
    {
        return maxMassSize;
    }
        
    /** Get the current minimum size of the mass on the pendulum.
     *
     * @return the current minimum size of the mass (KG).
     */
    double getMinMassSize()
    {
        return minMassSize;
    }
        
    /**
     * Set the size of the motor on the pendulum.
     * Up to the user to keep values between min and max sizes.
     * The user can get the min and max sizes and scale appropriately.
     *
     * @param size the value to set for the size of the motor on the pendulum
     */
    void setMotorSize( double size )
    {
        if (size > maxMotorSize) motorSize = maxMotorSize;
        else if (size < minMotorSize) motorSize = minMotorSize;
        else motorSize = size;
    }
    
    /** Get the current size of the motor on the pendulum.
     *
     * @return the current size of the motor.
     */
    double getMotorSize()
    {
        return motorSize;
    }
    
    /** Get the minimum size of the motor on the pendulum.
     *
     * @return the minimum size of the motor.
     */
    double getMinMotorSize()
    {
        return minMotorSize;
    }
    
    /** Get the maximum size of the motor on the pendulum.
     *
     * @return the maximum size of the motor.
     */
    double getMaxMotorSize()
    {
        return maxMotorSize;
    }
    
    /** Freeze the simulation for a specified number of seconds.
     *
     * @param seconds the number of seconds to freeze the simulation
     */
    void setFreeze( int seconds )
    {
        freezeSeconds = seconds;
    }
    
    /** Bump the mass to the left. In this case just increase the angular
     * momentum by 3.
     */
    void bumpLeft()
    {
        omega += 3.0;
    }
    
    /** Bump the mass to the right. In this case just decrease the angular
     * momentum by 3.
     */
    void bumpRight()
    {
        omega -= 3.0;
    }
    
    /** Pull the mass all the way to the left. In this case set the angular
     * momentum to 0 and set theta to PI.
     */
    void pullLeft()
    {
        omega = 0.0;
        theta = Math.PI;
    }
    
    /** Pull the mass all the way to the right. In this case set the angular
     * momentum to 0 and set theta to 0.
     */
    void pullRight()
    {
        omega = 0.0;
        theta = 0.0;
    }
    
    /** Turn on or off step mode, allowing the user to execute one step of the
     * simulation at a time.
     *
     * @param b true if step mode turned on.
     */
    synchronized void setStepMode( boolean b )
    {
        stepMode = b;
        stepsToDo = 0;
    }
    
    /** Get state of step mode, ON or OFF.
     *
     * @return ture if step mode is on.
     */
    boolean isStepMode()
    {
        return stepMode;
    }
     
    /** Request that the next step in the simulation be performed.
     */
    synchronized void doNextStep()
    {
        if (stepMode)
           stepsToDo++;
    }
    
    /** Identify that the next step has been done.
     */
    synchronized void nextStepDone()
    {
        if (stepsToDo > 0)
           stepsToDo--;
    }
    
    /** Get the current value of theta, the angle of the pendulum.
     *
     * @return the angle of the pendulum.
     */
    double getTheta()
    {
        return theta;
    }
    
    /** Get the current value of the length of the pendulum (stick).
     *
     * @return the length of the stick.
     */
    double getStickLength()
    {
        return stickLength;
    }
    
    /** Get the minimum value of the length of the pendulum (stick).
     *
     * @return the minimum length of the stick.
     */
    double getMinStickLength()
    {
        return minStickLength;
    }
    
    /** Get the maximum value of the length of the pendulum (stick).
     *
     * @return the maximum length of the stick.
     */
    double getMaxStickLength()
    {
        return maxStickLength;
    }
    
    /** Enbable or disable a rule from being used in the rule firings.
     *
     * @param b true if rule is to be enabled
     * @param rule string name of the rule to be enabled or disabled. The rule
     *        names have the format Z_NM_PM etc.
     */
    void setRuleEnabled(boolean b, String rule)
    {
        int i;
        
        for (i=0; i<NUM_RULES; i++)
            if (rule.equalsIgnoreCase(ruleNames[i]))
            {  rulesEnabled[i] = b;
               return;
            }
    }
    
    /** Determine if a rule is enabled or disabled.
     *
     * @param rule string name of the rule being queried. The rule
     *        names have the format Z_NM_PM etc.
     */
    boolean isRuleEnabled(String rule)
    {
        int i;
        
        for (i=0; i<NUM_RULES; i++)
            if (rule.equalsIgnoreCase(ruleNames[i]))
               return rulesEnabled[i];
               
        return false;
    }
    
    /** Get a rule.
     *
     * @param rule string name of the rule being queried. The rule
     *        names have the format Z_NM_PM etc.
     * @return a FuzzyRule object.
     */
    FuzzyRule getFuzzyRule(String rule)
    {
        int i;
        
        for (i=0; i<NUM_RULES; i++)
            if (rule.equalsIgnoreCase(ruleNames[i]))
               return fuzzyRules[i];
               
        // error ... rule not found
        return null;
    }
    
    
    
    /** The heart of the simulation, the run method. 
     * 
     * Simulates the balancing of a pendulum. At each cycle (time step) 
     * we calculate the current simulation state (theta, omega, etc.) and
     * we execute a set of Fuzzy Rules that determine the current to
     * apply in the motor in order to try to bring the pendulum to a vertical
     * position.
     */
    public void run()
    {
        UpdateSimulationComponentsThread uscThread = new UpdateSimulationComponentsThread();

        while (true)
        {   // if being asked to suspend itself call suspend ...
            // this happens when the Applet leaves a page for example; the applet
            // will do a resume when it starts again
            if (suspendRequested)
            { suspendRequested = false;
              suspend();
            }
            
            // if not in step mode or in step mode with a step to do
            // perform the next step of the simulation
            if ((isStepMode() && stepsToDo > 0) ||
                !isStepMode()
               )
            {
                nextStepDone();
                // handle freeze frame if required
                if (freezeSeconds > 0)
                {  try 
		           {   sleep((long)freezeSeconds*1000); } 
			       catch (InterruptedException e) 
			       {}
			       freezeSeconds = 0;
                }
                // deal with stick 'bobbing' if required
                if (isBobbing())
                {  // adjust the length of the stick
                   stickLength += stickDirection;
                   if (stickLength < minStickLength)
                      stickDirection = 0.1;
                   else if (stickLength > maxStickLength)
                      stickDirection = -0.1;
                }
                
                // determine the new value for theta given the current being 
                // applied (size of motor plus normalized current determined), 
                // the size of the mass, the current position of the mass/stick.
                //
                // mainTorque ... torque exerted by gravity
                // coilTorque ... torque exerted by the motor
                // totalTorque ... combined torque
                double mainTorque = -(stickLength*massSize*Math.cos(theta)*G);
                double coilTorque = motorSize*(current);
                double totalTorque = mainTorque + coilTorque;
                double inertia = stickLength*stickLength * massSize;
                double alpha = totalTorque/inertia; // mass angular acceleration
                omega = omega + alpha*dT; // mass angular velocity
                theta = theta + omega*dT + alpha*dT2;
                
                if (theta > thetaMax) 
                {  theta = thetaMax;
                   omega = 0.0;
                }
                else if (theta < thetaMin) 
                {  theta = thetaMin;
                   omega = 0.0;
                }
                
                // get error and delta error (actually angular velocity!!)
                error = theta - requiredTheta;
                
                // fire rules to calculate the next current setting
                fireRules( error, omega );

                // always draw the modified simulation panel after each time step
                // and draw the modified rule firings information
                try 
                { SwingUtilities.invokeAndWait(uscThread);
                }
                catch (Throwable t) 
                { t.printStackTrace();
	                //Ensure the application exits with an error condition.
	                System.exit(1);
                }
            }

            // Seem to need to sleep for >150 ms for things to work
            // correctly!!! should NOT have to do this?? I guess we
            // need to give up control at some point so other threads
            // get in but ... 
            // yield() has the same problem
            // This problem seems to go away when I get out of Visual Cafe!!
            //
            // Always need to sleep (or yield?) so that button clicks etc.
            // can be processed -- seem to need about 5 millisecs for
            // tooltips to be recognized!
            try { sleep(5); } catch (InterruptedException ie) {}
        }
    }
    
    /** Exceute the rules, calcualting the new value for the current to
     * be applied in the motor.
     *
     * @param error the error from required theta value
     * @param omega the angular velocity of the pendulum
     */
    public void fireRules(double error, double omega )
    {
        int i;
        
        FuzzyValueVector ruleResultFVV = null;
        globalCurrentFVal = null;
        
        // to fuzzify error and omega they need to be in range
        if (error > ERROR_MAX) error = ERROR_MAX;
        if (error < ERROR_MIN) error = ERROR_MIN;
        if (omega > OMEGA_MAX) omega = OMEGA_MAX;
        if (omega < OMEGA_MIN) omega = OMEGA_MIN;
        
        // clear all rulesFired flags;
        for (i=0; i<NUM_RULES; i++)
            rulesFired[i] = false;
        
        try
        {
          // fuzzify error and omega 
          FuzzyValue errorFVal = new FuzzyValue(errorFVar, new TriangleFuzzySet(error, error, error));
          FuzzyValue omegaFVal = new FuzzyValue(omegaFVar, new TriangleFuzzySet(omega, omega, omega));
        
          // use these as the inputs to each rule firing
          FuzzyValueVector ruleInputs = new FuzzyValueVector(2);
          ruleInputs.addFuzzyValue(errorFVal);
          ruleInputs.addFuzzyValue(omegaFVal);
        
          // fire the rules if they match the inputs
          for (i=0; i<NUM_RULES; i++)
          {
            FuzzyRule rule = fuzzyRules[i];
            if (rulesEnabled[i] && rule.testRuleMatching(ruleInputs))
            {
                rulesFired[i] = true;
                ruleResultFVV = rule.execute(ruleInputs);
                FuzzyValue fval = ruleResultFVV.fuzzyValueAt(0);
                if (globalCurrentFVal == null)
                   globalCurrentFVal = fval;
                else
                   globalCurrentFVal = globalCurrentFVal.fuzzyUnion(fval);
            }
          }
          // determine the new value for the motor current (between -1 and +1)
          if (globalCurrentFVal != null)
             current = globalCurrentFVal.momentDefuzzify();
        }
        catch (FuzzyException fe)
        {}
    }
    
    /* Threads for repainting etc. Note ... MUST do the painting 
       in a separate thread that has been added to the 'event queue'
       or display can be corrupted ... swing is single threaded!
       See call to invokeAndWait above!
    */
    class UpdateSimulationComponentsThread implements Runnable 
    {   
       public void run() 
       { 
          // paint the simulation display area
          FuzzySet gcfs = null;
          if (globalCurrentFVal != null) 
             gcfs = globalCurrentFVal.getFuzzySet();
          simGraphics.updateSimulationComponents(ruleNames, rulesFired, rulesEnabled, 
                                         theta, error, omega, current, gcfs
                                        );
       }
    }
    
}