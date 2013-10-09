<h1 align="center">Report for Project1</h1>
##Workload assignment:    
**Ying Wang**:Task A, H(Two version of Task H)    
**Salah** : Task B, C, G (Task H)   
**Hao**: Task C, D, F, Pig A, B, D (a different version of task C)

##Task Description:
###Task D:
1. One map-reduce job with combiner    
2. **Mapper** output: for accesslog table -> `<personalID, '1'>`; for mypage table -> `<personalID, name>`     
   **Combiner** output: for accesslog table -> `<personalID, 'localAccessCount'>`; for mypage table -> `<personalID, name>`    
   **Reducer** output: `<name, 'globalAccessCount'>`
3. ScreenShot with Combiner:
   ![ScreenShot with Combiner](Images/taskDWithCombiner.PNG "ScreenShot with Combiner")

4. wqwq
