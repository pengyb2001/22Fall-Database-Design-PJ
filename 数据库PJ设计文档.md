### 小组成员：彭一博、周子博

### ER图（详见文件夹内）

### 数据库表结构

```sql
CREATE TABLE `admission_authority` (
  `student_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `campus_name` varchar(50) NOT NULL COMMENT '校区',
  PRIMARY KEY (`student_ID`,`campus_name`),
  CONSTRAINT `student_ID` FOREIGN KEY (`student_ID`) REFERENCES `student` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `daily_report` (
  `report_num` int NOT NULL AUTO_INCREMENT COMMENT '报告编号',
  `student_ID` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `timestamp` datetime NOT NULL COMMENT '时间戳',
  `location` varchar(50) NOT NULL COMMENT '地理位置',
  `is_healthy` tinyint(1) NOT NULL COMMENT '健康状况',
  PRIMARY KEY (`report_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `enter_approval` (
  `form_num` int NOT NULL AUTO_INCREMENT COMMENT '表单号',
  `student_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `timestamp` datetime NOT NULL COMMENT '时间戳',
  `reason` varchar(255) DEFAULT NULL COMMENT '返校原因',
  `lived_area` varchar(255) NOT NULL COMMENT '七天内经过地区',
  `entry_date` date NOT NULL COMMENT '预计进校日期',
  `status` int unsigned NOT NULL COMMENT '0:辅导员审批1：学生修改2：管理员审批3：结束',
  `refuse_reason` varchar(255) DEFAULT NULL COMMENT '拒绝理由',
  PRIMARY KEY (`form_num`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `faculty_administrator` (
  `ID` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '工号',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `faculty_name` varchar(10) NOT NULL COMMENT '院系名',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `instructor` (
  `ID` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '工号',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `class_name` varchar(10) NOT NULL COMMENT '班级名',
  `faculty_name` varchar(10) NOT NULL COMMENT '院系名',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `leave_approval` (
  `form_num` int NOT NULL AUTO_INCREMENT COMMENT '表单号',
  `student_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `timestamp` datetime NOT NULL COMMENT '时间戳',
  `reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '离校原因',
  `destination` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目的地',
  `leave_date` date NOT NULL COMMENT '预计离校日期',
  `entry_date` date NOT NULL COMMENT '预计进校日期',
  `status` int unsigned NOT NULL COMMENT '0:辅导员审批1：学生修改2：管理员审批3：结束',
  `refuse_reason` varchar(255) DEFAULT NULL COMMENT '拒绝理由',
  PRIMARY KEY (`form_num`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `pass_record` (
  `pass_num` int NOT NULL AUTO_INCREMENT COMMENT '进出校记录号',
  `student_ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `timestamp` datetime NOT NULL COMMENT '时间戳',
  `campus_name` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '门禁机器号',
  `type` tinyint(1) NOT NULL COMMENT '1进入0出去',
  PRIMARY KEY (`pass_num`),
  KEY `ID` (`student_ID`),
  CONSTRAINT `ID` FOREIGN KEY (`student_ID`) REFERENCES `student` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;

CREATE TABLE `student` (
  `ID` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '学号',
  `name` varchar(10) NOT NULL COMMENT '姓名',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `personal_address` varchar(255) DEFAULT NULL COMMENT '宿舍或住址',
  `home_address` varchar(255) DEFAULT NULL COMMENT '家庭地址',
  `identity_type` varchar(10) NOT NULL COMMENT '身份证件类型',
  `id_num` varchar(255) NOT NULL COMMENT '证件号码',
  `in_school` varchar(10) NOT NULL COMMENT '在校与否',
  `class_name` varchar(10) NOT NULL COMMENT '班级名称',
  `faculty_name` varchar(10) NOT NULL COMMENT '院系名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
```

### 需求分析和核心SQL语句

#### 1.查询：

	学生：
	
		1.查询自己的个人信息
	
		2.查询自己的出校申请表（并在需要修改时可以编辑）

```sql
select * from leave_approval  
        where student_ID=?  
        and status=?  
        order by form_num DESC;
```
   3.查询自己的入校申请表
```sql
select * from enter_approval  
        where student_ID=?  
        and status=?  
        order by form_num DESC;
```
		4.查询本班的当日填报健康日报人数
```sql
select count(*)  
        from student join daily_report 
        on student.ID=daily_report.student_ID  
        where class_name=? and faculty_name=? 
        and to_days(timestamp) = to_days(now())
```
		5.查询自己的入校权限
```sql
select * from admission_authority where student_ID=?
```
  6.查询自己过去n天的每日填报信息
```sql
select * from daily_report  
        where student_ID=?  
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);
```
	辅导员：
		1.查询本班学生入校申请，支持按状态（待审核、已同意、已拒绝）进行筛选；

```sql
select * from enter_approval, student  
        where student_ID=ID  
        and class_name = ? and faculty_name = ? and status = ?;
```
		2.查询本班学生出校申请，支持按状态（待审核、已同意、已拒绝）进行筛选；
```sql
select * from leave_approval, student  
        where student_ID=ID  
        and class_name = ? and faculty_name = ? and status = ?;
```
		3.查询所在院系别的班级的当日填报健康日报人数
```sql
select count(*)  
        from student join daily_report 
        on student.ID=daily_report.student_ID  
        where class_name=? and faculty_name=? and to_days(timestamp) = to_days(now())
```
   4.查询班级名单（不是要求的 但这便于知道有哪些人）
```sql
select * from student where class_name = ? and faculty_name = ?
```
		5.查询本班学生（从当天算起）过去一年的离校总时长（查询到个人）
```sql
select * from pass_record where  
        student_ID=? 
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp)  
        order by pass_num desc 
```
这里只查询了该学生的进出校记录，然后我们使用java的技术手段计算离校总时长。
6.查询本班过去 n 天尚未批准的入校申请数量及详细信息；

```sql
select * from enter_approval, student  
        where student_ID=ID  
        and class_name = ? and faculty_name = ? and status = ?  
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);
```
		7.查询本班过去 n 天尚未批准的出校申请数量及详细信息；
```sql
select * from leave_approval, student  
        where student_ID=ID  
        and class_name = ? and faculty_name = ? and status = ?  
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp);
```
		8.查询本班已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间；(学生状态不在校但具有进校权限)
```sql
select distinct ID, name, phone, email,  
        personal_address, home_address, identity_type, id_num, 
        in_school, class_name, faculty_name 
        from (select * from student where in_school = '不在校' 
        and class_name = ? and faculty_name = ? ) as o  
        left join admission_authority aa on aa.student_ID = o.ID  
        where aa.campus_name is not null
```
		先获取本班不在校的学生，再左连接进出校权限，挑选出有权限的学生。
```sql
select * from pass_record where  
        student_ID=? and type = 0  
        order by pass_num desc 
```
 然后根据学生学号查找到最近一次出校记录获取时间。
9.查询本班已提交出校申请但未离校的学生数量、个人信息；

```sql
(select student.*  
        from student, leave_approval  
        where ID=student_ID and (in_school != '不在校') 
        and (status = 0 or status = 1 or status = 2) 
        and class_name=? and faculty_name=?)  
        union  
        (select *  
        from student  
        where (in_school != '不在校') and class_name=? 
        and faculty_name=?  
        and not exists(select * from admission_authority  
        where admission_authority.student_ID=student.ID))
```
  10.查询本班未提交出校申请但离校状态超过 24h 的学生数量、个人信息；
```sql
select *  
        from student where (in_school = '不在校') and class_name=? 
        and faculty_name=?  
        and not exists(select * from leave_approval 
        where student_ID=student.ID  
        and (status = 0 or status = 1 or status = 2))  
        and ID in(select student_ID from admission_authority 
        where student_ID=student.ID)
```
	院系管理员：
		1.查询本院系学生入校申请，支持按状态（待审核、已同意、已拒绝）进行筛选；

```sql
select * from leave_approval, student  
        where student_ID=ID  
        and faculty_name = ? and (status=0 or status=1 or status=2);
```
		2.查询本院系学生出校申请，支持按状态（待审核、已同意、已拒绝）进行筛选；
```sql
select * from enter_approval, student  
        where student_ID=ID  
        and faculty_name = ? and (status=0 or status=1 or status=2);
```
   3.查询院系名单（不是要求的 但这便于知道有哪些人）
```sql
select * from student where faculty_name = ?
```
	超级用户：
   0.查询所有用户数量和名单

```sql
select * from student;

select * from instructor;

select * from faculty_administrator;
```
		1.查询全校学生的入校权限（按校区筛选能进入的学生数量和名单）
```sql
select * from student, admission_authority where 
student.ID = admission_authority.student_ID and campus_name=?
```
		2.查询前 n 个提交入校申请最多的学生//支持按多级范围（全校、院系、班级）进行筛选；
```sql
select student.*, count(*) as cnt  
        from student right join enter_approval ea 
        on student.ID = ea.student_ID  
        group by student.ID  
        order by cnt desc
        
select student.*, count(*) as cnt  
        from student right join enter_approval ea 
        on student.ID = ea.student_ID  
        where faculty_name=? 
        group by student.ID  
        order by cnt desc
        
select student.*, count(*) as cnt  
        from student right join enter_approval ea 
        on student.ID = ea.student_ID  
        where faculty_name=? and class_name=? 
        group by student.ID  
        order by cnt desc
```
		3.查询前 n 个平均离校时间最长的学生//支持按多级范围（全校、院系、班级）
```sql
select * from pass_record where 
        student_ID=? 
        order by pass_num desc 
```
与查询学生近一年的离校时长类似，首先根据范围获取学生名单，逐个获取每个学生的平均离校时间（总离校时间除以离校次数），内部排序后输出前n多的
		4.查询全校已出校但尚未返回校园（即离校状态）的学生数量、个人信息及各自的离校时间；

```sql
select distinct ID, name, phone, email,  
        personal_address, home_address, identity_type, id_num, 
        in_school, class_name, faculty_name from  
        (select * from student where in_school = '不在校' ) as o  
        left join admission_authority aa on aa.student_ID = o.ID  
        where aa.campus_name is not null
        
select * from pass_record where  
        student_ID=? and type = 0  
        order by pass_num desc 
```
		实现方法与辅导员类似，去掉班级约束。
5.查询全校已提交出校申请但未离校的学生数量、个人信息；

```sql
select student.*  
        from student, leave_approval  
        where ID=student_ID and (in_school != '不在校') 
        and (status = 0 or status = 1 or status = 2))  
        union  
        (select *  
        from student  
        where (in_school != '不在校')  
        and not exists(select * from admission_authority  
        where admission_authority.student_ID=student.ID))
```

		6.查询全校过去 n 天一直在校未曾出校的学生//支持按多级范围（全校、院系、班级）

```sql
select * from student  
        where (in_school!='不在校')  
        and not exists (select * from pass_record 
        where student_ID=student.ID and type='0'  
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp))
```

7.查询全校（当且仅当）连续 n 天填写“健康日报”时间（精确到分钟）完全一致的学生数量，个人信息

先根据id分组、排序、打行号，同时给表赋予序号，时间戳-行号进行差值检查，此为临时表temp1。剩下的相当于找连续出现的相同dis值我们使用partition by num对dis分组（也就是dis相同的作为一组），然后再用order by对相同的rowNum组里面的id进行排序，观察规律，我们可以发现将rowNum与orde2的相减，只要dis值相同的相减的值都是一样的。得出结果，分为两步。第一步对studen_ID、dis与orde2的组合进行分组检索；第二步检索day_count=？的数字

```sql
with temp1 as 
          (select student_ID, timestamp, 
        date_format(date_sub(timestamp, INTERVAL rn*24*60 MINUTE),
         '%Y-%m-%d %H:%i') as dis,row_number() 
         over (order by student_ID )rowNum 
          from  
             ( 
             select  
             student_ID, timestamp, row_number() 
             over (partition by student_ID order by timestamp asc)rn 
             from daily_report 
             )t1 
          ) 
          select distinct student_ID, day_count,
        date_add(dis, INTERVAL 1 DAY) as start_day from( 
             select student_ID, count(*)day_count, dis from(     
                select student_ID, dis, row_number() 
                over(partition by dis order by rowNum) 
                as orde1, rowNum, rowNum-row_number() 
                over(partition by dis order by rowNum) as orde2 
                from temp1 
                ORDER BY rowNum 
                )as w 
             group by dis,orde2,student_ID 
             )as s where day_count = ?
```
   8.过去 n 天每个院系学生产生最多出入校记录的校区(格式为 [院系名]：[校区名])
```sql
select campus_name, count(*) as cnt  
        from student join pass_record 
        on (student.ID = pass_record.student_ID)  
        where faculty_name = ? 
        and DATE_SUB(CURDATE(), INTERVAL ? DAY) <= date(timestamp)  
        group by campus_name  
        order by cnt desc
```

#### 2.增加：

	学生：
	
		1.打卡进入校区增加进校记录
	
		2.打卡离开校区增加离开记录

type为1时是进校，为0时是出校

```sql
insert into pass_record(student_ID, timestamp, campus_name, type) 
        values (?, now(), ?, ?)
```
		3.每日健康填报增加填报记录
```sql
insert into daily_report(student_ID, timestamp, location, is_healthy) 
        values (?, now(), ?, ?)
```
		4.提交出校申请
```sql
insert into  
        leave_approval(student_ID, timestamp, reason, destination, 
        leave_date, entry_date, status) 
        values (?, now(), ?, ?, ?, ?, 0)
```
		5.提交入校申请
```sql
insert into  
        enter_approval(student_ID, timestamp, reason, lived_area,
         entry_date, status) 
        values (?, now(), ?, ?, ?, 0)
```
	辅导员：无
	院系管理员：无
	
	超级用户：无

#### 3.删除：

    学生：
    
    	1.撤销自己的入校申请

```sql
delete from enter_approval  
        where form_num = ?
```
		2.撤销自己的离校申请
```sql
delete from leave_approval  
        where form_num = ?
```
#### 4.更改：

	学生：

1.更改离校申请

```sql
update leave_approval set  
        student_ID = ?,timestamp = ?,reason = ?,destination = ?,
        leave_date = ?,entry_date = ?, 
        status = ?,refuse_reason = ? where form_num = ?
```
2.更改入校申请
```sql
update enter_approval set  
        student_ID = ?,timestamp = ?,reason = ?,lived_area = ?,
        entry_date = ?, status = ?,refuse_reason = ? 
        where form_num = ?	
```
辅导员：
		1.更改本班学生提交出校申请的审批状态（拒绝、辅导员通过）

```sql
update leave_approval set  
        student_ID = ?,timestamp = ?,reason = ?,destination = ?,
        leave_date = ?,entry_date = ?, 
        status = ?,refuse_reason = ? where form_num = ?
```
		2.更改本班学生提交入校申请的审批状态（拒绝、辅导员通过）
```sql
update enter_approval set  
        student_ID = ?,timestamp = ?,reason = ?,lived_area = ?,
        entry_date = ?, status = ?,refuse_reason = ? 
        where form_num = ?
	院系管理员：
		1.更改本院系学生提交出校申请的审批状态（拒绝、院系管理员通过），同时系统自动更改于次日判定该学生失去入校权限（我们为立马判定）

```sql
update leave_approval set  
        student_ID = ?,timestamp = ?,reason = ?,destination = ?,
        leave_date = ?,entry_date = ?, 
        status = ?,refuse_reason = ? where form_num = ?
```
		2.更改本班学生提交入校申请的审批状态（拒绝、院系管理员通过），同时系统自动更改于次日判定该学生恢复入校权限（我们为立马判定）
```sql
update enter_approval set  
        student_ID = ?,timestamp = ?,reason = ?,lived_area = ?,
        entry_date = ?, status = ?,refuse_reason = ? 
        where form_num = ?
```
	超级用户：
		1.如果出现管控情况，按校区更改学生进入某校区的权限；（选定某一校区管控后，使得除了目前在这个校区的所有人都失去进入该校区权限）

```sql
delete from admission_authority  
        where campus_name = ?  
        and student_ID not in  
        (select a.student_ID  
        from (select a.student_ID from admission_authority a 
        left join student on a.student_ID=student.ID 
        where student.in_school = ?)a)
```
从进校权限里删除相应校区的权限，并且学生不属于此时正在该校区的学生当中。
