PA = load '/hzhou/input/mypage.txt' USING PigStorage(',') as (ID, Name, Nationality, CountryCode, Hobby);
PB = foreach PA generate ID;
FA = load '/hzhou/input/accesslog.txt' USING PigStorage(',') as (AccessID, ByWho, WhatPage, TypeOfAccess, AccessTime);
FB = foreach FA generate WhatPage;
FC = group FB by WhatPage;
FD = foreach FC generate group as WhatPage, COUNT(FB) as Count;
C = join PB by ID, FD by WhatPage;
E = foreach C generate ID, Count;
F = GROUP E BY ID;
R = FOREACH F {
    result = TOP(10, 2, E); 
    GENERATE FLATTEN(result);
}

STORE R INTO '/hzhou/output/pigC';