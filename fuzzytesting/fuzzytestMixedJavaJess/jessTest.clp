(import nrc.fuzzy.jess.*)
(import nrc.fuzzy.*)

(load-package nrc.fuzzy.jess.FuzzyFunctions)
(set-reset-globals FALSE)
(defglobal ?*SpeedFvar*  = (new nrc.fuzzy.FuzzyVariable "speed" 0.0 100.0 "Units"))
(defglobal ?*rlf* = (new nrc.fuzzy.RightLinearFunction))
(defglobal ?*llf* = (new nrc.fuzzy.LeftLinearFunction))

(?*SpeedFvar* addTerm "ZERO" (new nrc.fuzzy.RFuzzySet 0.0 0.5 ?*rlf*))
(?*SpeedFvar* addTerm "SLOW" (new nrc.fuzzy.TrapezoidFuzzySet 0.0 1.0 5.0 6.0))
(?*SpeedFvar* addTerm "MEDIUM" (new nrc.fuzzy.TrapezoidFuzzySet 5.0 15.0 30.0 45.0))
(?*SpeedFvar* addTerm "FAST" (new nrc.fuzzy.LFuzzySet 30.0 100.0 ?*llf*))

(printout t "After add terms FuzzyVar is: " (call String valueOf ?*SpeedFvar*) crlf)
(bind ?xx (new nrc.fuzzy.FuzzyValue ?*SpeedFvar* "medium"))

(store "SPEED_FUZZYVARIABLE" ?*SpeedFvar*)

(call (engine) setFuzzyGlobalContributionOperator (new UnionGlobalContributionOperator)) 
(set-fuzzy-global-contribution-operator sum) ;; can be union, sum or none
(set-default-antecedent-combine-operator minimum)
(printout t "Set fuzzy global contribution type" crlf)
;;(deffacts myfacts
;;    (Desired Speed (new nrc.fuzzy.FuzzyValue ?*SpeedFvar* "MEDIUM")))
(defrule init
  =>
  (printout t "In init FuzzyVar is: " (call String valueOf ?*SpeedFvar*) crlf)
  (assert 
    (Desired_Speed (new nrc.fuzzy.FuzzyValue ?*SpeedFvar* "FAST"))
  )
)
(defrule init-2
  =>
  (printout t "In init-2 FuzzyVar is: " (call String valueOf ?*SpeedFvar*) crlf)
  (assert 
    (Desired_Speed (new nrc.fuzzy.FuzzyValue ?*SpeedFvar* "MEDIUM"))
  )
)
 
(defrule myrule
    (declare (salience -100))
    (Speed ?cs)
;;    (Desired_Speed ?ds&:(not (fuzzy-match ?ds ?cs)))
    ?dsf <- (Desired_Speed ?ds&:(fuzzy-match ?ds ?cs))
  =>
    (printout t IN_MYRULE crlf)
    (assert (change_thrust 1))
    (facts)
    (printout t (?ds toString) crlf)
)


