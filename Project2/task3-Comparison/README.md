####Task 3 is almost done.

1. I do cluster for 14439 tweets    
2. Do not care whether the centroid is changed between 2 iteration, instead, always use new centroids for next iteration. Therefore, the program just stop after MAX-ITERATION.[It doesn't make sense to judge whether a centroid is changed by THRESHOLD, as different size of datasets have different THRESHOLD]    
3. Keep top 30 terms for each new centroid, which will be used in next iteration       
4. I just run 2 iterations in my current program, but it is pretty fast [in 15 minutes].     

###What to do next
1. Make combiner more robust     
2. Generate top 10 terms for each cluster -- Not so hard (in different MapReduce Job)  
3. Run on more iterations    
4. Compare with Mahout with Pengfei's work  

###Report
**MapReduce**:    
1. **9** iterations -- The output for each iteration is the **new centroids**(My mistake, I should run 10 iterations)   
 **[start at 13:38:57 ~ end at 13:52:34] = 13 minutes 37 seconds**     

	CPU Time 1: 69670         
	CPU Time 2: 75660     
	CPU Time 3: 77180     
	CPU Time 4: 76380     
	CPU Time 5: 77490     
	CPU Time 6: 77170     
	CPU Time 7: 78300     
	CPU Time 8: 77340     
	CPU Time 9: 77940      

2. **Final output of Clusters with its tweets** -- map only    
CPU Time: 3670    