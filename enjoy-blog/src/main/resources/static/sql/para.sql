create table enjoy.code_config (
  `dmbh` varchar(50) collate utf8mb4_bin not null,
  `code` varchar(10) collate utf8mb4_bin not null,
  `content` varchar(255) collate utf8mb4_bin default null,
  `appid` varchar(50) collate utf8mb4_bin not null,
  primary key (`dmbh`,`code`,`appid`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;

create table enjoy.system_para (
  `csbh` varchar(200) collate utf8mb4_bin not null comment '参数编号',
  `csz` varchar(500) collate utf8mb4_bin default null comment '参数值',
  `cssm` varchar(255) collate utf8mb4_bin default null comment '参数说明',
  `appid` varchar(50) collate utf8mb4_bin not null comment '应用标识',
  primary key (`csbh`,`appid`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;


create table enjoy.seq_info (
  `seqname` varchar(50) collate utf8mb4_bin not null,
  `currenthead` varchar(30) collate utf8mb4_bin not null,
  `minhead` varchar(30) collate utf8mb4_bin not null,
  `maxhead` varchar(30) collate utf8mb4_bin not null,
  `seqtype` varchar(1) collate utf8mb4_bin not null,
  primary key (`seqname`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin;