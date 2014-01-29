In order to run the grand daddy test you will need to have access to the following:

1. directory with the class files for the grandaddytest (start in this directory);
   main method is in the GrandDaddyController class (package is fuzzytesting.granddaddytest)
   
2. in this same directory should be the files needed to do the tests:

	- verified_cases.dat
	- verified_results.dat
	- test_values.dat
	- results.dat
	- compare.out
	
3. Need 2 'libraries' of classes

	- 	symbeans.jar
	-   fuzzyJ class files (fuzzyJ-OpenSource-2.0.jar or access to the nrc directory with the classes)
	
4. Also needs the graphics folder with a number of arrows, etc. for the display.

5. From the directory with the granddaddytest classes one can execute the following command (or something similar):
	
	java -cp ../../nrc:../../symbeans.jar:../../ fuzzytesting.granddaddytest.GrandDaddyController
	
	
6. There will be a graphical display opened:

	Selecting the Run Preset Tests button will use the verified_cases.dat file to create a set of new results in the 
	results.dat file. It will then compare these new results with the verified_results.dat file creating a
	compare.out file that will show any tests that do not have the same output. If all is well this file will be empty.
	The program will also display a small window that displays the test case numbers as they are processed with a 
	count at the end of the number of differences encountered ... normally 0, unless there is a problem. This is the main
	test to verify that the fuzzy operations are still getting valid results.
	
	The file test_values.dat has a set of (180 as of Jan 2014) fuzzy values that can be accessed interactively and used
	in pairs to see the results of various fuzzy operations (NOT of the fuzzy values, union of the pair, 
	intersection of the pair, etc.). 
	

	This program needs some work to be more useful and to be better documented. The major set of test cases is
	still quite useful for catching errors but in no way exhausts all of FuzzyJ.
	