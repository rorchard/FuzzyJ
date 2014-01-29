;; NetTestJess.clp
;;
;; A simple example to test a complete FuzzyJess program (no Java code at all).
;; Mainly to see the complexity of a pattern/join net for debugging FuzzyJess
;; and to use fuzzy-match in a test construct on the LHS
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
;; Timothy is short with degree (similarity) 0.5363321799307958
;; Timothy is short with degree (match) 0.588235294117647
;; *************************************************************
;; BOB: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.59/4.47 0.59/5.71 0/6 }
;; DAN: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.59/4.8 0.59/5.5 0/6 }
;; Randy is tall with degree (similarity) 0.6085526315789473
;; Randy is tall with degree (match) 0.6085526315789473
;; Timothy is short with degree (similarity) 0.6085526315789473
;; Timothy is short with degree (match) 0.6085526315789473
;; *************************************************************
;; BOB: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.61/4.49 0.61/5.7 0/6 }
;; DAN: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.61/4.8 0.61/5.5 0/6 }
;; Ralph is tall with degree (similarity) 0.4117647058823532
;; Ralph is tall with degree (match) 0.49999999999999994
;; Timothy is short with degree (similarity) 0.4117647058823532
;; Timothy is short with degree (match) 0.49999999999999994
;; *************************************************************
;; BOB: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.5/4.4 0.61/4.49 0.61/5.7 0/6 }
;; DAN: FuzzyVariable         -> height [ 0.0, 10.0 ] feet
;; Linguistic Expression -> ???
;; FuzzySet              -> { 0/4 0.61/4.8 0.61/5.5 0/6 }

(import nrc.fuzzy.*)

(defglobal ?*heightFvar* = (new FuzzyVariable "height" 0.0 10.0 "feet"))

(deftemplate person
   (slot name)
   (slot height)
)

(defrule init
   (declare (salience 100))
  =>
   (load-package nrc.fuzzy.jess.FuzzyFunctions)
   (?*heightFvar* addTerm "short" (new RightLinearFuzzySet 0.0 5.0))
   (?*heightFvar* addTerm "medium" (new TrapezoidFuzzySet 4.0 4.8 5.5 6.0))
   (?*heightFvar* addTerm "tall" (new LeftLinearFuzzySet 5.5 6.0))

   (assert (person (name "Ralph")
                   (height (new FuzzyValue ?*heightFvar*
                                (new PIFuzzySet 5.7 0.1)))
           )
           (person (name "Timothy")
                   (height (new FuzzyValue ?*heightFvar*
                                (new PIFuzzySet 2.0 0.1)))
           )
           (person (name "Randy")
                   (height (new FuzzyValue ?*heightFvar*
                                (new PIFuzzySet 6.5 0.1)))
           )
           (person (name "Jack")
                   (height (new FuzzyValue ?*heightFvar*
                                (new PIFuzzySet 5.75 0.1)))
           )
           (do-pairs)
           (extra-fact 111)
   )
)

(defrule identify-tall-and-short-people-pairs
   (do-pairs)
   (person (name ?n1) (height ?ht1))
   (person (name ?n2&~?n1) (height ?ht2))
   (extra-fact 111)
   (test (and (fuzzy-match ?ht2 "short")(fuzzy-match ?ht1 "tall")))
 =>
   (printout t ?n1 " is tall with degree (similarity) " (fuzzy-rule-similarity) crlf)
   (printout t ?n1 " is tall with degree (match) " (fuzzy-rule-match-score) crlf)
   (printout t ?n2 " is short with degree (similarity) " (fuzzy-rule-similarity) crlf)
   (printout t ?n2 " is short with degree (match) " (fuzzy-rule-match-score) crlf)
   (printout t "*************************************************************" crlf)
   (call FuzzyRule setDefaultRuleExecutor (new MamdaniMinMaxMinRuleExecutor))
   (assert (bob (new FuzzyValue ?*heightFvar* "medium")))
   ;; change inference method to larsen product  ... 
   (call FuzzyRule setDefaultRuleExecutor (new LarsenProductMaxMinRuleExecutor))
   (assert (dan (new FuzzyValue ?*heightFvar* "medium")))
   
)

(defrule pp
  (declare (salience 100))
  (bob ?x)
  (dan ?y)
=>
  (printout t "BOB: " (?x toString) crlf "DAN: " (?y toString) crlf)
)

(reset)
(run)
;;(exit)

;;(batch  "f:\\fuzzyjtoolkit\\fuzzytest.jessonly\\testfuzzy-match4.clp")
