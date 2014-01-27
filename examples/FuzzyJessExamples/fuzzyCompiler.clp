;    fuzzyCompiler.clp
;
; An implementation of the example given in the paper 'A Compiler for Fuzzy Logic 
; Controllers' by P. Bonissone in Fuzzy Eng toward Human Friendly Systems Vol 2
; pp. 706-717
;
; Does not do any compile as described, just outputs the results of the controller
; over a range of input values for comparison.

; Note: in FuzzyJess we use centre of gravity (COG) to defuzzify
;

; Define the fuzzy linguistic variables needed -- 2 inputs (temp and pressure) and
; 1 output (throttle)
;
; perhaps rather than use the pi function to describe the fuzzy sets we should use 
; some traingular fuzzy sets via a singleton description to be more like those of the paper
;
;    eg.    (PI 20 50)  can be represented as singletons  (30 0) (50 1) (70 0)
;
;           The PI, Z and S functions generate 9 singletons to represent an
;           approximation of the curves.
;
;
; We run the example with various setting in FuzzyJess to vary how the inference
; method (larsenproduct or mamdanimin), how the antecedent combine is done
; (minimum, maximum or compensatoryAnd) and how global contribution is done
; (union or sum).
;
; In this example we have 2 antecedent fuzzy variables and 
; 1 conclusion fuzzy variable. They are:
; 
; Antecedent Fuzzy Variables:
;
; temperature 0 100 degrees C
;    terms: low, medium, high
;
; pressure 0 500 kPa
;    terms: low, medium, high
;
; Conclusion Fuzzy Variable:
;
; throttle 0 1 units
;    terms: verylow, low, midlow, medium, midhigh, high 
;
; the 9 rules can be written as:
;
; if  temperature is low
; and pressure is low
; then set throttle to high
;
; if  temperature is low
; and pressure is medium
; then set throttle to medium
;
; if  temperature is low
; and pressure is high
; then set throttle to midlow
;
; if  temperature is medium
; and pressure is low
; then set throttle to midhigh
;
; if  temperature is medium
; and pressure is medium
; then set throttle to midlow
;
; if  temperature is medium
; and pressure is high
; then set throttle to low
;
; if  temperature is high
; and pressure is low
; then set throttle to midlow
;
; if  temperature is high
; and pressure is medium
; then set throttle to low
;
; if  temperature is high
; and pressure is high
; then set throttle to verylow
;

(import nrc.fuzzy.*)
(import nrc.fuzzy.jess.*)

(load-package nrc.fuzzy.jess.FuzzyFunctions)

(defglobal ?*tempFvar* = (new FuzzyVariable "temperature" 0.0 100.0 "Degrees C"))
(defglobal ?*pressFvar* = (new FuzzyVariable "pressure" 0.0 500.0 "kPa"))
(defglobal ?*throttleFvar* = (new FuzzyVariable "throttle" 0.0 1.0 "units"))

(defglobal ?*time1* = 0)
(defglobal ?*time2* = 0)


(defrule init-FuzzyVariables
   (declare (salience 100))
  =>
;;   (?*tempFvar* addTerm "low" (new ZFuzzySet 20.0 40.0))
   (?*tempFvar* addTerm "low" (new PIFuzzySet 20.0 20.0))
   (?*tempFvar* addTerm "med" (new PIFuzzySet 50.0 20.0))
;;   (?*tempFvar* addTerm "high" (new SFuzzySet 60.0 80.0))
   (?*tempFvar* addTerm "high" (new PIFuzzySet 80.0 20.0))

;;   (?*pressFvar* addTerm "low" (new ZFuzzySet 100.0 200.0))
   (?*pressFvar* addTerm "low" (new PIFuzzySet 100.0 100.0))
   (?*pressFvar* addTerm "med" (new PIFuzzySet 250.0 100.0))
;;   (?*pressFvar* addTerm "high" (new SFuzzySet 300.0 400.0))
   (?*pressFvar* addTerm "high" (new PIFuzzySet 400.0 100.0))

;;   (?*throttleFvar* addTerm "very_low" (new ZFuzzySet .1 .2))
   (?*throttleFvar* addTerm "very_low" (new PIFuzzySet .1 .1))
   (?*throttleFvar* addTerm "low" (new PIFuzzySet .25 .15))
   (?*throttleFvar* addTerm "mid_low" (new PIFuzzySet .4 .1))
   (?*throttleFvar* addTerm "med" (new PIFuzzySet .55 .15))
   (?*throttleFvar* addTerm "mid_high" (new PIFuzzySet .75 .1))
;;   (?*throttleFvar* addTerm "high" (new SFuzzySet .8 .9))
   (?*throttleFvar* addTerm "high" (new PIFuzzySet .9 .1))
)


; Now define the rules that work to set the value of the throttle given a set of
; inputs  (9 rules)

(defrule low_low
   (temp ?t&:(fuzzy-match ?t "low"))
   (pressure ?p&:(fuzzy-match ?p "low"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "high")))
)

(defrule low_med
   (temp ?t&:(fuzzy-match ?t "low"))
   (pressure ?p&:(fuzzy-match ?p "med"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "med")))
)

(defrule low_high
   (temp ?t&:(fuzzy-match ?t"low"))
   (pressure ?p&:(fuzzy-match ?p "high"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "mid_low")))
)

(defrule med_low
   (temp ?t&:(fuzzy-match ?t"med"))
   (pressure ?p&:(fuzzy-match ?p "low"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "mid_high")))
)

(defrule med_med
   (temp ?t&:(fuzzy-match ?t "med"))
   (pressure ?p&:(fuzzy-match ?p "med"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "mid_low")))
)

(defrule med_high
   (temp ?t&:(fuzzy-match ?t "med"))
   (pressure ?p&:(fuzzy-match ?p "high"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "low")))
)

(defrule high_low
   (temp ?t&:(fuzzy-match ?t "high"))
   (pressure ?p&:(fuzzy-match ?p "low"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "mid_low")))
)

(defrule high_med
   (temp ?t&:(fuzzy-match ?t "high"))
   (pressure ?p&:(fuzzy-match ?p "med"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "low")))
)

(defrule high_high
   (temp ?t&:(fuzzy-match ?t "high"))
   (pressure ?p&:(fuzzy-match ?p "high"))
  =>
   (assert (throttle (new FuzzyValue ?*throttleFvar* "very_low")))
)


; rules to control the setting of values to test and produce the outputs

(defrule init
  
  =>
   (format t "Temp       5     10     15     20     25     30     35     40     45     50     55     60     65     70     75     80     85     90     95%nPress%n  10   ")
   (assert (temp (new FuzzyValue ?*tempFvar* (new PIFuzzySet 5.0 0.5)))
           (pressure (new FuzzyValue ?*pressFvar* (new PIFuzzySet 10.0 0.5)))
   )
)

(defrule match_temp_throttle
   (declare
	(salience -100)
   )
   ?tf <- (temp ?t)
   ?thf <- (throttle ?th)
  =>
   (assert (crisp temp (?t momentDefuzzify)))
   (assert (crisp throttle (?th momentDefuzzify)))
   (retract ?tf ?thf)
)

(defrule next_temp
   ?ctf <- (crisp temp ?ct)
   ?pf <- (pressure ?p)
   ?cthf <- (crisp throttle ?th)
  =>
   (format t "%7.4f" ?th)
   (if (>= ?ct 95)
       then
	   (bind ?ct 5)
	   (assert (crisp pressure (?p momentDefuzzify)))
	   (retract ?pf)
       else
	   (bind ?ct (+ 5 (integer (+ 0.1 ?ct))))
    )
           (assert (temp (new FuzzyValue ?*tempFvar* (new PIFuzzySet (float ?ct) .5))))  
           (retract ?ctf ?cthf)
)


(defrule next_press
   ?cpf <- (crisp pressure ?cp)
  =>
    (retract ?cpf)
    (bind ?cp (+ 10 (integer (+ 0.1 ?cp))))
    (if (> ?cp 490)
         then
	   (format t "%n%n")
	   (halt)
        else
           (format t "%n %3d   " ?cp)
           (assert (pressure (new FuzzyValue ?*pressFvar* (new PIFuzzySet (float ?cp) .5))))
    )
)

;; defaults are 
;;	rule executor: mamdaniMin 
;;	global contribution: union
;;	antecedent combine: minimum
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: mamdaniMin 
;;	global contribution: union
;;	antecedent combine: product
(set-default-antecedent-combine-operator product)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: mamdaniMin 
;;	global contribution: union
;;	antecedent combine: compensatoryAnd
(set-default-antecedent-combine-operator compensatoryAnd)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try are 
;;	rule executor: mamdaniMin 
;;	global contribution: sum
;;	antecedent combine: minimum
(set-default-antecedent-combine-operator minimum)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: mamdaniMin 
;;	global contribution: sum
;;	antecedent combine: product
(set-default-antecedent-combine-operator product)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: mamdaniMin 
;;	global contribution: sum
;;	antecedent combine: compensatoryAnd
(set-default-antecedent-combine-operator compensatoryAnd)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: larsenProduct 
;;	global contribution: union
;;	antecedent combine: minimum
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator minimum)
(set-fuzzy-global-contribution-operator union)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: larsenProduct 
;;	global contribution: union
;;	antecedent combine: product
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator product)
(set-fuzzy-global-contribution-operator union)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: larsenProduct 
;;	global contribution: union
;;	antecedent combine: compensatoryAnd
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator compensatoryAnd)
(set-fuzzy-global-contribution-operator union)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try are 
;;	rule executor: larsenProduct 
;;	global contribution: sum
;;	antecedent combine: minimum
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator minimum)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: larsenProduct 
;;	global contribution: sum
;;	antecedent combine: product
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator product)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)

;; next try 
;;	rule executor: larsenProduct 
;;	global contribution: sum
;;	antecedent combine: compensatoryAnd
(set-default-fuzzy-rule-executor larsenProduct)
(set-default-antecedent-combine-operator compensatoryAnd)
(set-fuzzy-global-contribution-operator sum)
(printout t "Testing with: " crlf "   Rule Executor = " (get-default-fuzzy-rule-executor) crlf)
(printout t "   Antecedent Combine Operator = " (get-default-antecedent-combine-operator) crlf)
(printout t "   Global Contribution Operator = " (get-fuzzy-global-contribution-operator) crlf)
(printout t crlf)
(reset)
(bind ?numrules (run))
(printout t "Rules fired is: " ?numrules crlf crlf)


