create table enjoy.timed_work_define (
  `twdid` varchar(120) collate utf8mb4_bin not null,
  `twdname` varchar(190) collate utf8mb4_bin not null,
  `appid` varchar(50) collate utf8mb4_bin not null,
  `description` varchar(250) collate utf8mb4_bin default null,
  `workclassname` varchar(250) collate utf8mb4_bin not null,
  `available` varchar(1) collate utf8mb4_bin not null,
  `concurrent` varchar(1) collate utf8mb4_bin not null,
  `parameter` varchar(200) collate utf8mb4_bin default null,
  `cron` varchar(200) collate utf8mb4_bin not null,
  `pausebegintime` datetime default null,
  `pauseendtime` datetime default null,
  primary key (`twdid`,`appid`),
  key `idx_twdname` (`twdname`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

create table enjoy.timed_work_log (
  `logid` int(11) not null auto_increment,
  `twdid` varchar(120) not null,
  `starttime` datetime default null,
  `endtime` datetime default null,
  `serverip` varchar(50) default null,
  `serverport` varchar(10) default null,
  `status` varchar(3) default null,
	`executecount` int(11) default null,
  primary key (`logid`)
) engine=innodb auto_increment=25 default charset=utf8mb4;

create table enjoy.timed_work_log_details (
  `logid` int(11) not null,
	`businessid` varchar(64) not null,
  `message` varchar(120) not null ,
   key `logid` (`logid`),
	 key `businessid` (`businessid`)
) engine=innodb default charset=utf8mb4;

create table enjoy.timed_work_exception_details (
  `logid` int(11) not null,
	`businessid` varchar(64) not null,
  `message` varchar(120) not null ,
   key `logid` (`logid`),
	 key `businessid` (`businessid`)
) engine=innodb default charset=utf8mb4;

