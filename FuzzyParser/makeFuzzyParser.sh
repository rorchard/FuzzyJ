#! /bin/bash
#
#  Create the FuzzyParser.java and FuzzyParserSym.java files from the FuzzyParser.cup file
#  using the javaCUP program
#
# Once created these 2 files should be moved to the nrc/fuzzy directory and compiled along with all 
# of the other java files in the nrc.fuzzy package


java -classpath ../ java_cup.Main -package nrc.fuzzy -parser FuzzyParser -symbols FuzzyParserSym < FuzzyParser.cup
