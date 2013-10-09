PA = load '/hzhou/input/mypage.txt' USING PigStorage(',') as (ID, Name, Nationality, CountryCode, Hobby);
PB = foreach PA generate Nationality;
PC = group PB by Nationality;
PD = foreach PC generate group as Nationality, COUNT(PB) as Count;
PE = foreach PD generate Nationality, Count;


STORE PE INTO '/hzhou/output/pigB';