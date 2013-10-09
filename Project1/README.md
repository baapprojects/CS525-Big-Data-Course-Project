<h1 align="center">Report for Project1</h1>
##Workload assignment:    
**Ying Wang**:Task A, H(Two version of Task H)    
**Salah** : Task B, C, G (Task H)   
**Hao**(know something about Hadoop): Task C, D, F, Pig A, B, D (a different version of task C)

##Task Description:
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
2. **Mapper** output: for accesslog table -> `<personalID, '1'>`; for mypage table -> `<personalID, name>`     
   **Combiner** output: for accesslog table -> `<personalID, 'localAccessCount'>`; for mypage table -> `<personalID, name>`    
   **Reducer** output: `<name, 'globalAccessCount'>`
3. ScreenShot with Combiner:      
   ![ScreenShot with Combiner](Images/taskDWithCombiner.PNG "ScreenShot with Combiner")

4. ScreenShot without Combiner:      
   ![ScreenShot with Combiner](Images/taskDWithoutCombiner.PNG "ScreenShot with Combiner")
5. **Conclusion**: Combiner makes the program faster, as it reduce the workload of reducers.