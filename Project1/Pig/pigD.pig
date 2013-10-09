PA = load '/hzhou/input/mypage.txt' USING PigStorage(',') as (ID, Name, Nationality, CountryCode, Hobby);
PB = foreach PA generate ID, Name, Salary;
TA = load '/hzhou/input/friends.txt' USING PigStorage(',') as (FriendRel, PersonID, MyFriend, DataofFriendship, Desc);
TB = foreach TA generate TransID, CustID, TransTotal, TransNumItems;
TC = group TB by CustID;
TD = foreach TC generate group as CustID, COUNT(TB) as Count, SUM(TB.TransTotal) as Sum, MIN(TB.TransNumItems) as Min;
C = join PB by ID, TD by CustID;
E = foreach C generate ID, Name, Salary, Count, Sum, Min;
STORE E INTO '/hzhou/output/Query2';