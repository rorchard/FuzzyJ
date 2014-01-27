;; FuzzyShowerJess.clp
;;
;; A set of rules to provide a suggested change for the hot and cold
;; valve positions for a shower example.
;;

(import nrc.fuzzy.*)
(import nrc.fuzzy.jess.*)
(load-package FuzzyFunctions)


(defglobal ?*tempFvar* = (new nrc.fuzzy.FuzzyVariable "temperature" 5.0 65.0 "Degrees C"))
(defglobal ?*flowFvar* = (new nrc.fuzzy.FuzzyVariable "flow" 0.0 100.0 "litres/minute"))


(defglobal ?*coldValveChangeFvar* = (new nrc.fuzzy.FuzzyVariable "coldValveChange" -1.0 1.0 ""))
(defglobal ?*hotValveChangeFvar* = (new nrc.fuzzy.FuzzyVariable "hotValveChange" -1.0 1.0 ""))
(defglobal ?*rlf* = (new nrc.fuzzy.RightLinearFunction))
(defglobal ?*llf* = (new nrc.fuzzy.LeftLinearFunction))

(defglobal ?*rulesThatFired* = "")


(defrule init
   (declare (salience 100))
  =>
   (import nrc.fuzzy.*)
   (load-package nrc.fuzzy.jess.FuzzyFunctions)
   (?*tempFvar* addTerm "none" (new RFuzzySet 5.0 5.1 ?*rlf*))
   (?*tempFvar* addTerm "cold" (new TrapezoidFuzzySet 5.0 5.05 10.0 35.0))
   (?*tempFvar* addTerm "OK" (new PIFuzzySet 36.0 3.5))
   (?*tempFvar* addTerm "hot" (new SFuzzySet 37.0 60.0))
   (?*flowFvar* addTerm "none" (new RFuzzySet 0.0 0.05 ?*rlf*))
   (?*flowFvar* addTerm "low" (new TrapezoidFuzzySet 0.0 0.025 3.0 11.5))
   (?*flowFvar* addTerm "OK" (new PIFuzzySet 12.0 1.8))
   (?*flowFvar* addTerm "strong" (new SFuzzySet 12.5 25.0))
   (?*hotValveChangeFvar* addTerm "NB" (new RFuzzySet -0.5 -.25 ?*rlf*))
   (?*hotValveChangeFvar* addTerm "NM" (new TriangleFuzzySet -.35 -.3 -.15))
   (?*hotValveChangeFvar* addTerm "NS" (new TriangleFuzzySet -.25 -.15 0.0))
   (?*hotValveChangeFvar* addTerm "Z" (new TriangleFuzzySet -.05 0.0 0.05))
   (?*hotValveChangeFvar* addTerm "PS" (new TriangleFuzzySet 0.0 .15 .25))
   (?*hotValveChangeFvar* addTerm "PM" (new TriangleFuzzySet .15 .3 .35))
   (?*hotValveChangeFvar* addTerm "PB" (new LFuzzySet .25 .5 ?*llf*))
   (?*coldValveChangeFvar* addTerm "NB" (new RFuzzySet -0.5 -.25 ?*rlf*))
   (?*coldValveChangeFvar* addTerm "NM" (new TriangleFuzzySet -.35 -.3 -.15))
   (?*coldValveChangeFvar* addTerm "NS" (new TriangleFuzzySet -.25 -.15 0.0))
   (?*coldValveChangeFvar* addTerm "Z" (new TriangleFuzzySet -.05 0.0 0.05))
   (?*coldValveChangeFvar* addTerm "PS" (new TriangleFuzzySet 0.0 .15 .25))
   (?*coldValveChangeFvar* addTerm "PM" (new TriangleFuzzySet .15 .3 .35))
   (?*coldValveChangeFvar* addTerm "PB" (new LFuzzySet .25 .5 ?*llf*))
   (store TEMPFUZZYVARIABLE ?*tempFvar*)
   (store FLOWFUZZYVARIABLE ?*flowFvar*)
)

(defrule none_none
  (temp ?t&:(fuzzy-match ?t "none"))
  (flow ?f&:(fuzzy-match ?f "none"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "PS"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "PM"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp none and Flow none then change Hot Valve PS and change Cold Valve PM fires%")
  )
)


(defrule cold_low
  (temp ?t&:(fuzzy-match ?t "cold"))
  (flow ?f&:(fuzzy-match ?f "low"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "PB"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "Z"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp cold and Flow low then change Hot Valve PB and change Cold Valve Z fires%")
  )
)


(defrule cold_OK
  (temp ?t&:(fuzzy-match ?t "cold"))
  (flow ?f&:(fuzzy-match ?f "OK"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "PM"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "Z"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp cold and Flow OK then change Hot Valve PM and change Cold Valve Z fires%")
  )
)


(defrule cold_strong
  (temp ?t&:(fuzzy-match ?t "cold"))
  (flow ?f&:(fuzzy-match ?f "strong"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "Z"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "NB"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp cold and Flow strong then change Hot Valve Z and change Cold Valve NB fires%")
  )
)


(defrule OK_low
  (temp ?t&:(fuzzy-match ?t "OK"))
  (flow ?f&:(fuzzy-match ?f "low"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "PS"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "PS"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp OK and Flow low then change Hot Valve PS and change Cold Valve PS fires%")
  )
)


(defrule OK_OK
  (temp ?t&:(fuzzy-match ?t "OK"))
  (flow ?f&:(fuzzy-match ?f "OK"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "Z"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "Z"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp OK and Flow OK then change Hot Valve Z and change Cold Valve Z fires%")
  )
)


(defrule OK_strong
  (temp ?t&:(fuzzy-match ?t "OK"))
  (flow ?f&:(fuzzy-match ?f "strong"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "NS"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "NS"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp OK and Flow strong then change Hot Valve NS and change Cold Valve NS fires%")
  )
)


(defrule hot_low
  (temp ?t&:(fuzzy-match ?t "hot"))
  (flow ?f&:(fuzzy-match ?f "low"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "Z"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "PB"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp hot and Flow low then change Hot Valve Z and change Cold Valve PB fires%")
  )
)


(defrule hot_OK
  (temp ?t&:(fuzzy-match ?t "hot"))
  (flow ?f&:(fuzzy-match ?f "OK"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "NM"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "Z"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp hot and Flow OK then change Hot Valve NM and change Cold Valve Z fires%")
  )
)


(defrule hot_strong
  (temp ?t&:(fuzzy-match ?t "hot"))
  (flow ?f&:(fuzzy-match ?f "strong"))
 =>
  (assert (change_hv (new FuzzyValue ?*hotValveChangeFvar* "NB"))
          (change_cv (new FuzzyValue ?*coldValveChangeFvar* "Z"))
  )
  (bind ?*rulesThatFired* (str-cat ?*rulesThatFired*
        "!Rule: if Temp hot and Flow strong then change Hot Valve NB and change Cold Valve Z fires%")
  )
)


(defrule defuzzify "low salience to allow all rules to fire and do global contribution"
   (declare (salience -100))
  ?hf <- (change_hv ?h)
  ?cf <- (change_cv ?c)
  ?temp <- (temp ?)
  ?flow <- (flow ?)
 =>
  (bind ?hot-change (?h momentDefuzzify))
  (bind ?cold-change (?c momentDefuzzify))
  (store COLDVALVECHANGE ?cold-change)
  (store HOTVALVECHANGE ?hot-change)
  (store RULESTHATFIRED ?*rulesThatFired*)
  (bind ?*rulesThatFired* "")
  (retract ?hf ?cf ?temp ?flow)
)


