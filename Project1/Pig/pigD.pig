PA = load '/hzhou/input/mypage.txt' USING PigStorage(',') as (ID, Name, Nationality, CountryCode, Hobby);
PB = foreach PA generate ID, Name;
FA = load '/hzhou/input/friends.txt' USING PigStorage(',') as (FriendRel, PersonID, MyFriend, DaFAofFriendship, Description);
FB = foreach FA generate MyFriend;
FC = group FB by MyFriend;
FD = foreach FC generate group as MyFriend, COUNT(FB) as Count;
C = join PB by ID, FD by MyFriend;
E = foreach C generate Name, Count;
STORE E INTO '/hzhou/output/pigD';