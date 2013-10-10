<h1 align="center">Report for Project1</h1>
##Workload assignment:    
**Ying Wang**:Task A, H(Two version of Task H)    
**Salah** : Task B, C, G (Task H)   
**Hao**(know something about Hadoop): Task C, D, F, Pig A, B, D (a different version of task C)

##Task Description:
###Task A:
1.	We can use only one map for the function    
2.	**Mapper** output: `<ID,name+hobby>`
Because we just need to list the name and hobby of a certain nationality, we don’t need a combiner to do extra job. It’s better that we make the task simple.
3.	ScreenShot without reducer:      
![taskA](Images/image001.PNG "taskA")

###Task D:
1. One map-reduce job with combiner    
2. **Mapper** output: for accesslog table -> `<personalID, '1'>`; for mypage table -> `<personalID, name>`     
   **Combiner** output: for accesslog table -> `<personalID, 'localAccessCount'>`; for mypage table -> `<personalID, name>`    
   **Reducer** output: `<name, 'globalAccessCount'>`
3. ScreenShot with Combiner:      
   ![ScreenShot with Combiner](Images/taskDWithCombiner.PNG "ScreenShot with Combiner")

4. ScreenShot without Combiner:      
   ![ScreenShot with Combiner](Images/taskDWithoutCombiner.PNG "ScreenShot with Combiner")
5. **Conclusion**: Combiner makes the program faster, as it reduce the workload of reducers[But when I use a small dataset(500 records in mypage.txt),with combiner will cost more time than without combiner].     


###Task E:
1. Two map-reduce job with combiner    
2. *Job1*->:   
   **Mapper** output:  `<personID (byWho) + pageID (WhatPage), ' '>`        
   **Reducer** output: `<' ', personID + CountOfVaulues)>`      
   *Job2*->:   
   **Mapper** output: `<personID, CountOfVaulues>`     
   **Reducer** output: `<personID,'totalAccess' + 'distinctPageAmount'>`
3. ScreenShot:      
   ![taskE](Images/taskE.PNG "taskE")
4. **Conclusion**: Understand the role of KEY, then the whole task will be easy.

###Task F:
1. One map-reduce job with combiner    
2. **Mapper** output: for friends table -> `<personalID, 'F'+myFriend>`; for accesslog table -> `<Whatpage, 'A'+ByWho>`       
   **Reducer** output: `<personalID, 'FriendIDWhoNeverAccessMyPage'>`    
3. ScreenShot:      
   ![taskF](Images/taskF.PNG "taskF")

###Task H:
**TaskH describe 1:**
We can check the number of a certain person’s friends, and the visiting number of it. It’s a problem that we connect all the three forms together using the ID and return information in each of the forms. The connection of each forms is the personal ID.     
1.	We use a map and a reducer for the function       
2.	Mapper output: for mypage table ->`<ID,name >`;for friends table`<ID,”F”>`;for accesslog table->`<ID,”A”>`;     
3.	Reducer output:`<name,countToBeFriendOfOthers>`    
4.	ScreenShot with reducer:       
 ![taskH](Images/image003.PNG "taskH")

**TaskI describe 2:**
We can check the number of a certain nationality, how many person are in that nationality, we can use map to find the nationality and use reduce to count for each nationality. Then we can get how many people are in the facebook with the same nationality.     
1.	We use a map and a reducer for the function    
2.	Mapper output: for mypage table ->`< nationality,’1’ >`;    
3.	 Reducer output:`<key,countToBenationality>`;    
4.	ScreenShot with reducer:      
 ![taskH](Images/image005.PNG "taskH")
##Comparison:
###Task D -> Map-Reduce VS Pig
1. *Job Count*:   
   In **map-reduce**, everything can be done in one job.    
   In **Pig**, the same work needs two jobs.     
   ![PigD](Images/pigDJobCount.PNG "PigD")
2. *CPU Time*:
   ![CPUTimeForTaskD](Images/CPUTimeForTaskD.png "CPUTimeForTaskD")
3. **Conclusion**: Pig is high language for Hadoop, and it is not as efficient as Map-reduce in Java. 