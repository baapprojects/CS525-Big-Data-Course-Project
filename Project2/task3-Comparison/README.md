####Task 3 is almost done.

1. I do cluster for 14439 tweets    
2. Do not care whether the centroid is changed between 2 iteration, instead, always use new centroids for next iteration. Therefore, the program just stop after MAX-ITERATION.[It doesn't make sense to judge whether a centroid is changed by THRESHOLD, as different size of datasets have different THRESHOLD]      
3. I just run 2 iterations in my current program, but it is pretty fast [in 15 minutes].     

###What to do next
1. Make combiner more robust     
2. Generate top 10 terms for each cluster -- Not so hard (in different MapReduce Job)  
3. Run on more iterations    
4. Compare with Mahout with Pengfei's work  
