
This test does a some java FuzzyJ testing and also executes a Jess program
as well.

If we are in the directory (fuzzytesting/fuzzytestMixedJavaJess) with the test class we can execute the test with a command like:

	java -cp ../../:../../fuzzyJ-OpenSource-2.0.jar:../../jess.jar fuzzytesting.fuzzytestMixedJavaJess.FuzzyTestMixedJavaJess




********************************* Expected Output from the test ************************

FuzzyVariable -> temp2 [ 0.0, 100.0 ] C
Terms:
  cold -> { 1/5 0/15 }
  medium -> { 0/5 1/15 1/25 0/35 }
  veryhot -> { 0/25 0.01/26 0.04/27 0.09/28 0.16/29 0.25/30 0.36/31 0.49/32 0.64/33 0.81/34 1/35 }
  hot -> { 0/25 1/35 }

Modifiers:
	norm
	intensify
	very
	not
	slightly
	plus
	more_or_less
	above
	below
	extremely
	somewhat
0.25
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 1/2 0/3 }
0.75
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 1/1 0/2 1/3 }
0.06500000000000002
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.01/1.1 0.04/1.2 0.09/1.3 0.16/1.4 0.25/1.5 0.36/1.6 0.49/1.7 0.64/1.8 0.81/1.9 1/2 0.81/2.1 0.64/2.2 0.49/2.3 0.36/2.4 0.25/2.5 0.16/2.6 0.09/2.7 0.04/2.8 0.01/2.9 0/3 }
0.01750000000000001
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0/1.1 0.01/1.2 0.03/1.3 0.06/1.4 0.12/1.5 0.22/1.6 0.34/1.7 0.51/1.8 0.73/1.9 1/2 0.73/2.1 0.51/2.2 0.34/2.3 0.22/2.4 0.12/2.5 0.06/2.6 0.03/2.7 0.01/2.8 0/2.9 0/3 }
0.49746807650256203
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.32/1.1 0.45/1.2 0.55/1.3 0.63/1.4 0.71/1.5 0.77/1.6 0.84/1.7 0.89/1.8 0.95/1.9 1/2 0.95/2.1 0.89/2.2 0.84/2.3 0.77/2.4 0.71/2.5 0.63/2.6 0.55/2.7 0.45/2.8 0.32/2.9 0/3 }
0.6271182488623714
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.46/1.1 0.58/1.2 0.67/1.3 0.74/1.4 0.79/1.5 0.84/1.6 0.89/1.7 0.93/1.8 0.97/1.9 1/2 0.97/2.1 0.93/2.2 0.89/2.3 0.84/2.4 0.79/2.5 0.74/2.6 0.67/2.7 0.58/2.8 0.46/2.9 0/3 }
0.6271182488623714
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.46/1.1 0.58/1.2 0.67/1.3 0.74/1.4 0.79/1.5 0.84/1.6 0.89/1.7 0.93/1.8 0.97/1.9 1/2 0.97/2.1 0.93/2.2 0.89/2.3 0.84/2.4 0.79/2.5 0.74/2.6 0.67/2.7 0.58/2.8 0.46/2.9 0/3 }
0.177886451171485
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.06/1.1 0.13/1.2 0.22/1.3 0.32/1.4 0.42/1.5 0.53/1.6 0.64/1.7 0.76/1.8 0.88/1.9 1/2 0.88/2.1 0.76/2.2 0.64/2.3 0.53/2.4 0.42/2.5 0.32/2.6 0.22/2.7 0.13/2.8 0.06/2.9 0/3 }
0.25
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 1/2 0/3 }
0.1881796918636813
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.02/1.1 0.05/1.15 0.11/1.2 0.19/1.25 0.29/1.3 0.43/1.35 0.59/1.4 0.74/1.45 0.85/1.5 0.93/1.55 0.98/1.6 1/1.65 0.99/1.67 0.97/1.7 0.91/1.73 0.83/1.77 0.71/1.8 0.55/1.83 0.36/1.87 0.21/1.9 0.12/1.92 0.05/1.95 0.01/1.98 0/2 0.01/2.02 0.05/2.05 0.12/2.08 0.21/2.1 0.36/2.13 0.55/2.17 0.71/2.2 0.83/2.23 0.91/2.27 0.97/2.3 0.99/2.33 1/2.35 0.98/2.4 0.93/2.45 0.85/2.5 0.74/2.55 0.59/2.6 0.43/2.65 0.29/2.7 0.19/2.75 0.11/2.8 0.05/2.85 0.02/2.9 0/3 }
Fuzzy Value: temperature
Linguistic Value: ??? (.),  ??? (+)

 1.00               +++       .       +++               
 0.95              +   +     . .     +   +              
 0.90             +        ..   ..        +             
 0.85                   + .       . +                   
 0.80            +       .         .       +            
 0.75                   .           .                   
 0.70           +     .. +         + ..     +           
 0.65                .                 .                
 0.60          +    .                   .    +          
 0.55              .                     .              
 0.50            ..       +       +       ..            
 0.45         + .                           . +         
 0.40          .                             .          
 0.35        +.                               .+        
 0.30       ..             +     +             ..       
 0.25      .+                                   +.      
 0.20     .                                       .     
 0.15    . +                +   +                + .    
 0.10  .. +                                       + ..  
 0.05 . ++                   + +                   ++ . 
 0.00+++                      +                      +++
     |----|----|----|----|----|----|----|----|----|----|
    1.00      1.40      1.80      2.20      2.60      3.00   
maxmin of slightly fval and fval = 0.780429042775165
match of slightly fval and fval at threshold 0.8 = false
match of slightly fval and fval at threshold 0.75 = true
Support is: (1, 2)  (2, 3)   
Weak Alpha cut at 0.0 is: [0, 100]  
Weak Alpha cut at 0.52 is: [1.37719, 1.83789]  [2.16211, 2.62281]   
Strong Alpha cut at 0.52 is: (1.37719, 1.83789)  (2.16211, 2.62281)   
0.13000000000000003
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.02/1.1 0.08/1.2 0.18/1.3 0.32/1.4 0.5/1.5 0.68/1.6 0.82/1.7 0.92/1.8 0.98/1.9 1/2 0.98/2.1 0.92/2.2 0.82/2.3 0.68/2.4 0.5/2.5 0.32/2.6 0.18/2.7 0.08/2.8 0.02/2.9 0/3 }
Fuzzy Value: temperature
Linguistic Value: ??? (*),  ??? (+)

 1.00                   +++                             
 0.95                                                   
 0.90                  +* *+                            
 0.85                                                   
 0.80                 +*   *+                           
 0.75                                                   
 0.70                +*     *+                          
 0.65                                                   
 0.60                *       *                          
 0.55                                                   
 0.50               +         +                         
 0.45                                                   
 0.40              *           *                        
 0.35                                                   
 0.30             *+           +*                       
 0.25                                                   
 0.20            *+             +*                      
 0.15                                                   
 0.10           *+               +*                     
 0.05                                                   
 0.00++++++++++++                 ++++++++++++++++++++++
     |----|----|----|----|----|----|----|----|----|----|
    0.00      1.00      2.00      3.00      4.00      5.00   
0.0
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/2 1/3 }
0.75
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 1/1 0/2 }
0.06500000000000002
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/1 0.01/1.1 0.04/1.2 0.09/1.3 0.16/1.4 0.25/1.5 0.36/1.6 0.49/1.7 0.64/1.8 0.81/1.9 1/2 0.81/2.1 0.64/2.2 0.49/2.3 0.36/2.4 0.25/2.5 0.16/2.6 0.09/2.7 0.04/2.8 0.01/2.9 0/3 }
0.06500000000000002
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> very hot or cold
FuzzySet              -> { 1/5 0/15 0/25 0.01/26 0.04/27 0.09/28 0.16/29 0.25/30 0.36/31 0.49/32 0.64/33 0.81/34 1/35 }
Fuzzy Value: temperature
Linguistic Value: very hot or cold (+)

 1.00+++++++                                     +++++++
 0.95       +                                           
 0.90                                           +       
 0.85        +                                          
 0.80         +                                         
 0.75                                          +        
 0.70          +                                        
 0.65                                                   
 0.60           +                             +         
 0.55            +                                      
 0.50                                        +          
 0.45             +                                     
 0.40              +                        +           
 0.35                                                   
 0.30               +                      +            
 0.25                                                   
 0.20                +                    +             
 0.15                 +                  +              
 0.10                                   +               
 0.05                  +               +                
 0.00                   +++++++++++++++                 
     |----|----|----|----|----|----|----|----|----|----|
    0.00      8.00     16.00     24.00     32.00     40.00   
Fuzzy Value: temperature
Linguistic Value: very hot or cold (*),  medium (+)

 1.00*******            +++++++++++++            *******
 0.95       *          +             +                  
 0.90                                           *       
 0.85        *        +               +                 
 0.80         *      +                 +                
 0.75                                          *        
 0.70          *    +                   +               
 0.65                                                   
 0.60           *  +                     +    *         
 0.55            *+                       +             
 0.50                                        *          
 0.45            +*                        +            
 0.40           +  *                        +           
 0.35                                                   
 0.30          +    *                      * +          
 0.25                                                   
 0.20         +      *                    *   +         
 0.15        +        *                  *     +        
 0.10                                   *               
 0.05       +          *               *        +       
 0.00+++++++            ***************          +++++++
     |----|----|----|----|----|----|----|----|----|----|
    0.00      8.00     16.00     24.00     32.00     40.00   
0.06500000000000002
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 1/5 0/15 0/25 0.01/26 0.04/27 0.09/28 0.16/29 0.25/30 0.36/31 0.49/32 0.64/33 0.81/34 1/35 }
0.0
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/40 0.02/41 0.08/42 0.18/43 0.32/44 0.5/45 0.68/46 0.82/47 0.92/48 0.98/49 1/50 0.98/51 0.92/52 0.82/53 0.68/54 0.5/55 0.32/56 0.18/57 0.08/58 0.02/59 0/60 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00                        +++                        
 0.95                       +   +                       
 0.90                      +     +                      
 0.85                                                   
 0.80                     +       +                     
 0.75                                                   
 0.70                    +         +                    
 0.65                                                   
 0.60                                                   
 0.55                   +           +                   
 0.50                                                   
 0.45                                                   
 0.40                  +             +                  
 0.35                                                   
 0.30                                                   
 0.25                 +               +                 
 0.20                                                   
 0.15                +                 +                
 0.10               +                   +               
 0.05              +                     +              
 0.00++++++++++++++                       ++++++++++++++
     |----|----|----|----|----|----|----|----|----|----|
   30.00     38.00     46.00     54.00     62.00     70.00   
0.6124999999999999
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 1/10 0.98/14 0.92/18 0.82/22 0.68/26 0.5/30 0.32/34 0.18/38 0.08/42 0.02/46 0/50 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00++++++++++++                                       
 0.95            +++                                    
 0.90               ++                                  
 0.85                 ++                                
 0.80                   +                               
 0.75                    +                              
 0.70                     +                             
 0.65                      +                            
 0.60                       +                           
 0.55                        +                          
 0.50                         +                         
 0.45                          +                        
 0.40                           +                       
 0.35                            +                      
 0.30                             +                     
 0.25                              +                    
 0.20                               +                   
 0.15                                ++                 
 0.10                                  ++               
 0.05                                    +++            
 0.00                                       ++++++++++++
     |----|----|----|----|----|----|----|----|----|----|
    0.00     12.00     24.00     36.00     48.00     60.00   
0.0
1.0
1.0
1.0
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/10 1/10 1/50 0/50 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00     +++++++++++++++++++++++++++++++++++++++++     
 0.95     +                                       +     
 0.90     +                                       +     
 0.85     +                                       +     
 0.80     +                                       +     
 0.75     +                                       +     
 0.70     +                                       +     
 0.65     +                                       +     
 0.60     +                                       +     
 0.55     +                                       +     
 0.50     +                                       +     
 0.45     +                                       +     
 0.40     +                                       +     
 0.35     +                                       +     
 0.30     +                                       +     
 0.25     +                                       +     
 0.20     +                                       +     
 0.15     +                                       +     
 0.10     +                                       +     
 0.05     +                                       +     
 0.00++++++                                       ++++++
     |----|----|----|----|----|----|----|----|----|----|
    5.00     15.00     25.00     35.00     45.00     55.00   
Moment is 30.0
Moment is 30.0
Maximum defuzzify is 30.0
1.0
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/10 1/15 1/30 0/50 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00          ++++++++++++++++                         
 0.95                          +                        
 0.90                           +                       
 0.85                            +                      
 0.80         +                   +                     
 0.75                              +                    
 0.70                               +                   
 0.65                                +                  
 0.60        +                        +                 
 0.55                                  +                
 0.50                                   +               
 0.45                                    +              
 0.40       +                             +             
 0.35                                      +            
 0.30                                       +           
 0.25                                        +          
 0.20      +                                  +         
 0.15                                          +        
 0.10                                           +       
 0.05                                            +      
 0.00++++++                                       ++++++
     |----|----|----|----|----|----|----|----|----|----|
    5.00     15.00     25.00     35.00     45.00     55.00   
Moment is 26.818181818181817
Moment is 26.818181818181817
Maximum defuzzify is 22.5
1.0
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/10 0.03/10.62 0.12/11.25 0.28/11.88 0.5/12.5 0.72/13.12 0.88/13.75 0.97/14.38 1/15 1/30 0.97/32.5 0.88/35 0.72/37.5 0.5/40 0.28/42.5 0.12/45 0.03/47.5 0/50 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00      ++++++++++++++++++++++                       
 0.95                            ++                     
 0.90     +                        ++                   
 0.85                                +                  
 0.80                                 +                 
 0.75    +                             +                
 0.70                                   +               
 0.65                                                   
 0.60                                    +              
 0.55                                     +             
 0.50                                                   
 0.45   +                                  +            
 0.40                                       +           
 0.35                                        +          
 0.30                                                   
 0.25                                         +         
 0.20  +                                       +        
 0.15                                           +       
 0.10                                            ++     
 0.05 +                                            ++   
 0.00+                                               +++
     |----|----|----|----|----|----|----|----|----|----|
   10.00     18.00     26.00     34.00     42.00     50.00   
{ 0/-4 0.12/-2.75 0.5/-1.5 0.88/-0.25 1/1 0.88/2.25 0.5/3.5 0.12/4.75 0/6 }
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/0 0.9/0 1/1 0.88/2.25 0.5/3.5 0.12/4.75 0/6 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00    +++                                            
 0.95  ++   ++                                          
 0.90++       +++                                       
 0.85+           +                                      
 0.80+                                                  
 0.75+            +                                     
 0.70+             +                                    
 0.65+              +                                   
 0.60+               +                                  
 0.55+                +                                 
 0.50+                                                  
 0.45+                 +                                
 0.40+                  +                               
 0.35+                   +                              
 0.30+                    +                             
 0.25+                     +                            
 0.20+                                                  
 0.15+                      +                           
 0.10+                       +++                        
 0.05+                          ++                      
 0.00+                            ++++++++++++++++++++++
     |----|----|----|----|----|----|----|----|----|----|
    0.00      2.00      4.00      6.00      8.00     10.00   
{ 0/94 0.12/95.25 0.5/96.5 0.88/97.75 1/99 0.88/100.25 0.5/101.5 0.12/102.75 0/104 }
FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> ???
FuzzySet              -> { 0/94 0.12/95.25 0.5/96.5 0.88/97.75 1/99 0.9/100 0/100 }
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00                                            +++    
 0.95                                          ++   ++  
 0.90                                       +++       ++
 0.85                                      +           +
 0.80                                                  +
 0.75                                     +            +
 0.70                                    +             +
 0.65                                   +              +
 0.60                                  +               +
 0.55                                 +                +
 0.50                                                  +
 0.45                                +                 +
 0.40                               +                  +
 0.35                              +                   +
 0.30                             +                    +
 0.25                            +                     +
 0.20                                                  +
 0.15                           +                      +
 0.10                        +++                       +
 0.05                      ++                          +
 0.00++++++++++++++++++++++                            +
     |----|----|----|----|----|----|----|----|----|----|
   90.00     92.00     94.00     96.00     98.00    100.00   
Moment is 98.0407608695652
Moment is 98.0407608695652
Maximum defuzzify is 99.0
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00                                                   
 0.95                                                   
 0.90                                                   
 0.85                                                   
 0.80                                                   
 0.75                                     ++++++++++++++
 0.70                                    +             +
 0.65                                   +              +
 0.60                                  +               +
 0.55                                 +                +
 0.50                                                  +
 0.45                                +                 +
 0.40                               +                  +
 0.35                              +                   +
 0.30                             +                    +
 0.25                            +                     +
 0.20                                                  +
 0.15                           +                      +
 0.10                        +++                       +
 0.05                      ++                          +
 0.00++++++++++++++++++++++                            +
     |----|----|----|----|----|----|----|----|----|----|
   90.00     92.00     94.00     96.00     98.00    100.00   
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00                                            +++    
 0.95                                          ++   ++  
 0.90                                       +++       ++
 0.85                                      +           +
 0.80                                                  +
 0.75++++++++++++++++++++++++++++++++++++++            +
 0.70                                                   
 0.65                                                   
 0.60                                                   
 0.55                                                   
 0.50                                                   
 0.45                                                   
 0.40                                                   
 0.35                                                   
 0.30                                                   
 0.25                                                   
 0.20                                                   
 0.15                                                   
 0.10                                                   
 0.05                                                   
 0.00                                                   
     |----|----|----|----|----|----|----|----|----|----|
   90.00     92.00     94.00     96.00     98.00    100.00   
Fuzzy Value: temperature
Linguistic Value: ??? (+)

 1.00                                                   
 0.95                                                   
 0.90                                                   
 0.85                                                   
 0.80                                                   
 0.75                                            +++    
 0.70                                        ++++   ++++
 0.65                                       +          +
 0.60                                     ++           +
 0.55                                    +             +
 0.50                                   +              +
 0.45                                  +               +
 0.40                                 +                +
 0.35                                +                 +
 0.30                               +                  +
 0.25                              +                   +
 0.20                             +                    +
 0.15                           ++                     +
 0.10                         ++                       +
 0.05                      +++                         +
 0.00++++++++++++++++++++++                            +
     |----|----|----|----|----|----|----|----|----|----|
   90.00     92.00     94.00     96.00     98.00    100.00   
Support is: (94, 100]  
Weak Alpha cut at 0.0 is: [0, 100]  
Weak Alpha cut at 0.52 is: [96.56667, 100]  
non intersection of hot and cold is true
NO intersection of hot and cold is true
non intersection of hot and medium is false
NO intersection of hot and medium is false
Starting loop 1
Starting loop 2
Starting loop 3
Ending all loops
non intersection of hot and not hot is false
NO intersection of hot and not hot is false
non intersection of S30-40 and Z10-30 is true
NO intersection of S30-40 and Z10-30 is false
non intersection of RectangleFuzzySet30-40 and RectangleFuzzySet10-30 is false
NO intersection of RectangleFuzzySet30-40 and RectangleFuzzySet10-30 is false
Starting loop 1
Starting loop 2
Starting loop 3
Ending all loops
Medium is ... FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> medium
FuzzySet              -> { 0/5 1/15 1/25 0/35 }
Very medium is ... FuzzyVariable         -> temperature [ 0.0, 100.0 ] C
Linguistic Expression -> very medium
FuzzySet              -> { 0/5 0.01/6 0.04/7 0.09/8 0.16/9 0.25/10 0.36/11 0.49/12 0.64/13 0.81/14 1/15 1/25 0.81/26 0.64/27 0.49/28 0.36/29 0.25/30 0.16/31 0.09/32 0.04/33 0.01/34 0/35 }
Fuzzy Value: temperature
Linguistic Value: hot (*),  very medium (+)

 1.00               +++++++++++         ****************
 0.95                                                   
 0.90                                  *                
 0.85                                                   
 0.80              +           +      *                 
 0.75                                                   
 0.70                                *                  
 0.65             +             +                       
 0.60                               *                   
 0.55                                                   
 0.50            +               + *                    
 0.45                                                   
 0.40                             *                     
 0.35           +                 +                     
 0.30                            *                      
 0.25          +                   +                    
 0.20                           *                       
 0.15         +                     +                   
 0.10        +                 *     +                  
 0.05       +                         +                 
 0.00+++++++*******************        +++++++++++++++++
     |----|----|----|----|----|----|----|----|----|----|
    0.00     10.00     20.00     30.00     40.00     50.00   
Fuzzy Value: pressure
Linguistic Value: ??? (*)

 1.00                                                   
 0.95                                                   
 0.90                                                   
 0.85                                                   
 0.80                                                   
 0.75                                                   
 0.70                                                   
 0.65                                                   
 0.60                                                   
 0.55                                                   
 0.50                                                   
 0.45                                                   
 0.40*********************************                  
 0.35                                                   
 0.30                                 *                 
 0.25                                                   
 0.20                                                   
 0.15                                  *                
 0.10                                   *               
 0.05                                    *              
 0.00                                     **************
     |----|----|----|----|----|----|----|----|----|----|
    0.00      2.00      4.00      6.00      8.00     10.00   
Fuzzy Value: pressure
Linguistic Value: low or medium (*)

 1.00************             *                         
 0.95            *           * *                        
 0.90             *        **   **                      
 0.85              *                                    
 0.80                                                   
 0.75               *     *       *                     
 0.70                                                   
 0.65                *   *         *                    
 0.60                                                   
 0.55                 * *           *                   
 0.50                                                   
 0.45                  **                               
 0.40                                *                  
 0.35                                                   
 0.30                                 *                 
 0.25                                                   
 0.20                                                   
 0.15                                  *                
 0.10                                   *               
 0.05                                    *              
 0.00                                     **************
     |----|----|----|----|----|----|----|----|----|----|
    0.00      2.00      4.00      6.00      8.00     10.00   
Firing rule many times ... 1390953116943
Fired rule many times ... 1390953117169
Fuzzy Value: pressure
Linguistic Value: ??? (*)

 1.00                                                   
 0.95                                                   
 0.90                                                   
 0.85                                                   
 0.80                                                   
 0.75                                                   
 0.70                                                   
 0.65                                                   
 0.60                                                   
 0.55                                                   
 0.50                                                   
 0.45                                                   
 0.40************             *                         
 0.35            ***       *** ***                      
 0.30               *     *       *                     
 0.25                *   *         *                    
 0.20                 * *           *                   
 0.15                  **            *                  
 0.10                                 *                 
 0.05                                  **               
 0.00                                    ***************
     |----|----|----|----|----|----|----|----|----|----|
    0.00      2.00      4.00      6.00      8.00     10.00   
Similarity of 'temp very medium' and 'temp hot' is: 0.19130434782608696
After add terms FuzzyVar is: FuzzyVariable -> speed [ 0.0, 100.0 ] Units
Terms:
  fast -> { 0/30 1/100 }
  medium -> { 0/5 1/15 1/30 0/45 }
  zero -> { 1/0 0/0.5 }
  slow -> { 0/0 1/1 1/5 0/6 }

Set fuzzy global contribution type
f-0   (MAIN::initial-fact)
For a total of 1 facts in module MAIN.

Jess Operation after this point ...

Facts (before run) nil
f-0   (MAIN::initial-fact)
f-1   (MAIN::Speed <Java-Object:nrc.fuzzy.FuzzyValue>)
For a total of 2 facts in module MAIN.
Facts (before run) nil
In init FuzzyVar is: FuzzyVariable -> speed [ 0.0, 100.0 ] Units
Terms:
  fast -> { 0/30 1/100 }
  medium -> { 0/5 1/15 1/30 0/45 }
  zero -> { 1/0 0/0.5 }
  slow -> { 0/0 1/1 1/5 0/6 }

In init-2 FuzzyVar is: FuzzyVariable -> speed [ 0.0, 100.0 ] Units
Terms:
  fast -> { 0/30 1/100 }
  medium -> { 0/5 1/15 1/30 0/45 }
  zero -> { 1/0 0/0.5 }
  slow -> { 0/0 1/1 1/5 0/6 }

IN_MYRULE
f-0   (MAIN::initial-fact)
f-1   (MAIN::Speed <Java-Object:nrc.fuzzy.FuzzyValue>)
f-3   (MAIN::Desired_Speed <Java-Object:nrc.fuzzy.FuzzyValue>)
f-4   (MAIN::change_thrust 1)
For a total of 4 facts in module MAIN.
FuzzyVariable         -> speed [ 0.0, 100.0 ] Units
Linguistic Expression -> (MEDIUM) sum (FAST)
FuzzySet              -> { 0/5 1/15 1/30 0.35/42.35 0.21/45 1/100 }
f-0   (MAIN::initial-fact)
f-1   (MAIN::Speed <Java-Object:nrc.fuzzy.FuzzyValue>)
f-3   (MAIN::Desired_Speed <Java-Object:nrc.fuzzy.FuzzyValue>)
f-4   (MAIN::change_thrust 1)
For a total of 4 facts in module MAIN.
Facts (after run) nil

