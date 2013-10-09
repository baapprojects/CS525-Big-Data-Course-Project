A = load '/hzhou/input/mypage.txt' USING PigStorage(',') as (ID, Name, Nationality, CountryCode, Hobby);
B = FILTER A BY (Nationality == 'China');
C = foreach B generate Name, Hobby;
STORE C INTO '/hzhou/output/pig1';