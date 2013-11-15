##Something about this task
1. Find more information about this project here: [http://web.cs.wpi.edu/~cs525/f13b-EAR//cs525-homepage/projects/project2-cs525b-fall2013.pdf](http://web.cs.wpi.edu/~cs525/f13b-EAR//cs525-homepage/projects/project2-cs525b-fall2013.pdf)     
2. Most of my operation are found from here: [https://cwiki.apache.org/confluence/display/MAHOUT/Quick+tour+of+text+analysis+using+the+Mahout+command+line](https://cwiki.apache.org/confluence/display/MAHOUT/Quick+tour+of+text+analysis+using+the+Mahout+command+line)    

##How to run this task
###Step 1. Generate the DataSet
1. Compile & Run [DataSet.java](https://github.com/zhouhao/CS525-Big-Data-Course-Project/blob/master/Project2/task1-Mahout/DataSetGenerator/DataSet.java) 
	- a. Compile it with `javac DataSet.java`    
	- b. Run it with `java DataSet`    
2. Now, you get a file list of 119848 tweets(one tweet one file) [it seems too many].   
3. A tricky here: as there are too many files in that folder, it is hard to open it. But before we upload the folder to HDFS, we must remove the txt, and source files from it.      
![mv](../../ScreenShots/move.png)       

###Step 2. Upload these files into HDFS 
1. First, we create a directory in HDFS using: `hadoop fs -mkdir -p /CS525/mahout`    
2. Now, we use this command to upload the tweets files: `hadoop fs -put ~/Desktop/tweets /CS525/mahout`   

###Step 3. Convert Text to Mahout Sequence files
	mahout seqdirectory \
		-c UTF-8 \
		-i /CS525/mahout/tweets/ \
		-o /CS525/mahout/seqfiles
***Note:This step is time-consuming, and it costs me about half an hour, which makes me thought Hadoop is down***

###Step 4. Generate tf-idf Vectors from Sequence files
	mahout seq2sparse \
	   -i /CS525/mahout/seqfiles/ \
	   -o /CS525/mahout/vectors/ \
	   -ow -chunk 100 \
	   -x 50 \
	   -seq \
	   -ml 50 \
	   -n 2 \
	   -nv
***Note:***  
This uses the default analyzer and default TFIDF weighting, `-n 2` is good for **cosine distance**, which we are using in clustering and for similarity, `-x 50` meaning that if a token appears in 50% of the docs it is considered a stop word, `-nv` to get named vectors making further data files easier to inspect.
###Step 5. Generate Kmeans Cluster from vectors
	mahout kmeans \
	   -i /CS525/mahout/vectors/tfidf-vectors/ \
	   -c /CS525/mahout/kmeans-centroids \
	   -cl \
	   -o /CS525/mahout/kmeans-clusters \
	   -k 20 \
	   -ow \
	   -x 10 \
	   -dm org.apache.mahout.common.distance.CosineDistanceMeasure
***Note:***   
1. If `-c` and `-k` are specified, kmeans will put random seed vectors into the `-c` directory, if `-c` is provided without `-k` then the `-c` directory is assumed to be input and kmeans will use each vector in it to seed the clustering. `-cl` tell kmeans to also assign the input doc vectors to clusters at the end of the process and put them in `/CS525/mahout/kmeans-clusters/clusteredPoints`. if `-cl` is not specified then the documents will not be assigned to clusters.    
2. `-x 10` means that the number of iterations will be 10    
3. `-k 20` means that the number of clusters will be 10  
###Step 6. Examine the clusters 
	mahout clusterdump \
	   -d /CS525/mahout/vectors/dictionary.file-0 \
	   -dt sequencefile \
	   -i /CS525/mahout/kmeans-clusters/clusters-1-final \
	   -n 20 \
	   -b 100 \
	   -o clusterDump.txt \
	   -p /CS525/mahout/kmeans-clusters/clusteredPoints/
***Note:***      
1. `clusterDump.txt` is in you local file system, you can find it in your current directory where you run this command       
2. `-i /CS525/mahout/kmeans-clusters/clusters-1-final \`, the number in `clusters-X-final` may be changed, just check the HDFS.      
3. Remove `-p /CS525/mahout/kmeans-clusters/clusteredPoints/`, so that you will just dump the top terms  

##Result
1. Click to check the output for [20-cluster](https://github.com/zhouhao/CS525-Big-Data-Course-Project/blob/master/Project2/task1-Mahout/ClusterResultDump/clusterDump-20.txt)(Output top 10 terms for each cluster)     
##Times 
###0. For all:
1. 21.00 minutes - [`hadoop fs -put`] - Uploading tweets to HDFS - **This time is not accurate**   
2. 19.78 minutes - [`mahout seqdirectory`]    
3. 02.01 minutes - [`mahout seq2sparse`] 
###1. Cluster - 20:
1. 00.90 minutes - [`mahout kmeans`]     
2. 00.04 minutes - [`mahout clusterdump`]    

###2. Cluster - 5000:
1. **76.56** minutes - [`mahout kmeans`]     

###3. Cluster - 2:
1. 00.78 minutes - [`mahout kmeans`]   

------
##Appendix:
<del>Error when uploading files to HDFS:</del> -- Doesn't happen when I do the second time in another machine.    
![error](../../ScreenShots/error.png "error")     
**Find more about this bug report by others**:https://issues.apache.org/jira/browse/HDFS-148
