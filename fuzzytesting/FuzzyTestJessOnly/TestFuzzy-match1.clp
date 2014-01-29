;; TestJess.clp
;;
;; A simple example to test a complete FuzzyJess program (no Java code at all).
;;
;;
;; Note: future versions (beyond 5.0a5) of Jess will allow us to use --
;;
;;             (new FuzzyValue ... )
;;       etc.
;;
;;       will no longer always need to fully qualify the classes!
;;
;; Example as shown will give result ...
;;
;; Jack is tall with degree (similarity) 0.5363321799307958
;; Jack is tall with degree (match) 0.588235294117647
;; Randy is tall with degree (similarity) 1.0
;; Randy is tall with degree (match) 1.0
;; Ralph is tall with degree (similarity) 0.4117647058823532
;; Ralph is tall with degree (match) 0.49999999999999994
;;
;; NOTE: do not use the (import nrc.fuzzy.*) in this test

(defglobal ?*heightFvar* = (new nrc.fuzzy.FuzzyVariable "height" 0.0 10.0 "feet"))

(defglobal ?*rlf* = (new nrc.fuzzy.RightLinearFunction))
(defglobal ?*llf* = (new nrc.fuzzy.LeftLinearFunction))

(deftemplate person
   (slot name)
   (slot height)
)

(defrule init
   (declare (salience 100))
  =>
   (load-package nrc.fuzzy.jess.FuzzyFunctions)
   (?*heightFvar* addTerm "short" (new nrc.fuzzy.RFuzzySet 0.0 5.0 ?*rlf*))
   (?*heightFvar* addTerm "medium" (new nrc.fuzzy.TrapezoidFuzzySet 4.0 4.8 5.5 6.0))
   (?*heightFvar* addTerm "tall" (new nrc.fuzzy.LFuzzySet 5.5 6.0 ?*llf*))

   (assert (person (name "Ralph")
                   (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
                                (new nrc.fuzzy.PIFuzzySet 5.7 0.1)))
           )
           (person (name "Timothy")
                   (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
                                (new nrc.fuzzy.PIFuzzySet 4.0 0.1)))
           )
           (person (name "Randy")
                   (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
                                (new nrc.fuzzy.PIFuzzySet 6.5 0.1)))
           )
           (person (name "Jack")
                   (height (new nrc.fuzzy.FuzzyValue ?*heightFvar*
                                (new nrc.fuzzy.PIFuzzySet 5.75 0.1)))
           )
   )
)

(defrule identify-tall-people
   (person (name ?n) (height ?ht&:(fuzzy-match ?ht "tall")))
 =>
   (printout t ?n " is tall with degree (similarity) " (fuzzy-rule-similarity) crlf)
   (printout t ?n " is tall with degree (match) " (fuzzy-rule-match-score) crlf)
)

(reset)
(run)
(exit)

;;(batch  "f:\\fuzzyjtoolkit\\fuzzytest.jessonly\\testfuzzy-match1.clp")

