<del>It is almost done. But with small bugs to be fixed. -- 11/08/2013</del>    
**This may be helpful** : [How to configure Hadoop develop environment in Eclipse](http://sbzhouhao.net/2013/11/Configure-Hadoop-develop-environment-in-Eclipse/)       

![kmeansCluster](../flowChart/kmeansCluster.png "kmeansCluster")      


**Broadcast k centroids to all map nodes**(`DistributedCache` is used here).    
##Input & Output
1. **ClusterMapper**:     
	a. input is a `point`   
	b. Compare its distance to all the centroids, then find the nearest one   
	c. output: **key**: centroid.x, centroid.y --- **value**: point.x, point.y            
2. **Combiner**:    
	a.just do aggregation for mapper result here     
	b. output: **key(no change from mapper)**: centroid.x, centroid.y --- **value**: sum(point.x), sum(point.y), *countOfPointsForThisKey* 
3. **UpdateCenterReducer**:     
	Two steps here        
	Step 1. Do the same work as combiner for global points    
	Step 2. According to the result from step 1, generate new centroid, and compare new centroid with previous one    
	output: **key**: newCentroid.x, newCentroid.y --- **value**: **1** if centroid not changed, otherwise **0**


##Final output:
1. We can get new centroid   
2. We can get clustered points with its centroids
