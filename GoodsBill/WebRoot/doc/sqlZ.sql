create table goods
(
 id number(10) primary key,
 goodName varchar2(100) not null,
 billStatus number(5) not null,
 goodsDistrict number(5) not null,
 goodsPrice number(10,2) not null,
 goodsCount number(10) not null,
 creationTime date not null

)

create sequence g_id
start with 1
increment by 1 

insert into goods values(g_id.nextval,'Êé',0,3,23.5,10,to_date('2012-11-12 15:31:34','YYYY-MM-DD HH24:MI:SS'))
insert into goods values(g_id.nextval,'±Ê',1,2,45.5,20,to_date('2006-11-12 09:31:34','YYYY-MM-DD HH24:MI:SS'))
insert into goods values(g_id.nextval,'ÏðÆ¤',2,1,10.5,30,to_date('2008-11-12 17:31:34','YYYY-MM-DD HH24:MI:SS'))
select *from goods
