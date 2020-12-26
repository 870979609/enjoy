-- 文章表
create table enjoy.article
(
	id varchar(20) collate utf8mb4_bin not null comment '文章id',
	title varchar(100) collate utf8mb4_bin not null comment '文章标题',
	user_id varchar(20) collate utf8mb4_bin not null comment '用户id',
  summary varchar(200) collate utf8mb4_bin default null comment '文章摘要',
	categorys varchar(100) collate utf8mb4_bin not null comment '文章所属分类,逗号隔开',
	categorys_name varchar(200) collate utf8mb4_bin not null comment '文章所属分类名称,逗号隔开',
	tags varchar(100) collate utf8mb4_bin default null comment '文章所属标签,逗号隔开',
	tags_name varchar(200) collate utf8mb4_bin default null comment '文章所属标签名称,逗号隔开',
	origin varchar(3) collate utf8mb4_bin not null comment '文章来源,code articleorigin',
	visibility varchar(3) collate utf8mb4_bin not null comment '文章可见度,私密/公开code articlevisibility',
	status varchar(3) collate utf8mb4_bin not null comment '文章状态,code articlestatus',
	is_top tinyint(1) collate utf8mb4_bin default 00 comment '文章是否置顶状态,[0否 1是]',
	praise_num int(11) collate utf8mb4_bin default 0 comment '喜欢数量/点赞数',
	collection_num int(11) collate utf8mb4_bin default 0 comment '收藏数量',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
	primary key (`id`),
  key `key_article_userid` (`user_id`),
  key `key_article_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='博客文章表';

-- 分类
create table enjoy.category
(
	id varchar(20) collate utf8mb4_bin not null comment '分类id',
	name varchar(100) collate utf8mb4_bin not null comment '分类名',
	user_id varchar(20) collate utf8mb4_bin not null comment '用户id',
  description varchar(200) collate utf8mb4_bin default null comment '分类描述',
	img_url varchar(100) collate utf8mb4_bin default null comment '分类图片',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
	primary key (`id`),
  key `key_category_userid` (`user_id`),
  key `key_category_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='博客文章分类表';

-- 文章分类关系表
create table enjoy.article_category
(
	article_id varchar(20) collate utf8mb4_bin not null comment '文章id',
	category_id varchar(20) collate utf8mb4_bin not null comment '分类id',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
  key `key_article_category_articleid` (`article_id`),
  key `key_article_category_categoryid` (`category_id`),
  key `key_article_category_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='文章分类关系表';

-- 文章内容表
create table enjoy.article_content
(
	article_id varchar(20) collate utf8mb4_bin not null comment '文章id',
	content longtext collate utf8mb4_bin default null comment '文章内容',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
	primary key (`article_id`),
  key `key_article_content_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='文章分类关系表';

-- 文章图片表
create table enjoy.article_picture
(
	article_id varchar(20) collate utf8mb4_bin not null comment '文章id',
	img_url varchar(100) collate utf8mb4_bin default null comment '图片路径',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
  key `key_article_picture_articleid` (`article_id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='文章图片表';

-- 标签
create table enjoy.tag
(
	id varchar(20) collate utf8mb4_bin not null comment '标签id',
	name varchar(50) collate utf8mb4_bin not null comment '标签名',
	user_id varchar(20) collate utf8mb4_bin default null comment '用户id',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
	primary key (`id`),
  key `key_tag_userid` (`user_id`),
  key `key_tag_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='文章图片表';

-- 评论表
create table enjoy.comment (
  id varchar(20) collate utf8mb4_bin not null comment '评论id',
  user_id varchar(32) collate utf8mb4_bin not null comment '评论人userid',
  article_id varchar(32) collate utf8mb4_bin not null comment '评论的文章id',
  parent_comment_id varchar(32) collate utf8mb4_bin default '' comment '父评论id',
  reply_comment_id varchar(32) collate utf8mb4_bin default '' comment '被回复的评论id',
  comment_level tinyint(4) not null default '1' comment '评论等级[ 1 一级评论 默认 ，2 二级评论]',
  content varchar(255) collate utf8mb4_bin not null default '' comment '评论的内容',
  praise_num int(11) not null default '0' comment '点赞数',
  create_time datetime not null default current_timestamp comment '创建时间',
  primary key (`id`),
  key `key_comment_articleid` (`article_id`) ,
  key `key_comment_userid` (`user_id`),
  key `key_comment_createtime` (`create_time`),
  key `key_comment_parentcommentid` (`parent_comment_id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_bin comment='评论表';

-- 用户基本信息表
create table enjoy.user
(
  id varchar(20) collate utf8mb4_bin not null comment '用户id',
  loginname varchar(100) collate utf8mb4_bin not null comment '登录名',
  password varchar(100) collate utf8mb4_bin not null comment '密码',
  nickname varchar(100) collate utf8mb4_bin not null comment '昵称',
  realname varchar(100) collate utf8mb4_bin default null comment '真实姓名',
  gender char(1) collate utf8mb4_bin default null comment '性别',
  introduction varchar(300) collate utf8mb4_bin default null comment '简介',
  school varchar(100) collate utf8mb4_bin default null comment '学校',
  graduationtime datetime collate utf8mb4_bin default null comment '毕业时间',
  company varchar(100) collate utf8mb4_bin default null comment '公司',
  job varchar(100) collate utf8mb4_bin default null comment '工作',
  tel varchar(11) collate utf8mb4_bin default null comment '联系电话',
  email varchar(30) collate utf8mb4_bin default null comment '常用邮箱',
  img_url varchar(100) collate utf8mb4_bin default null comment '头像图片路径',
  resume_url varchar(100) collate utf8mb4_bin default null comment '简历文件路径',
  create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
  primary key (`id`),
  key `key_user_createtime` (`create_time`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='用户基本信息表';

-- 文章用户点赞记录关系表
CREATE TABLE enjoy.article_user_like (
  `article_id` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '文章id',
  `user_id` varchar(20) COLLATE utf8mb4_bin COMMENT '用户id',
  `praise_time` datetime DEFAULT NULL COMMENT '创建时间',
  KEY `key_like_articleid` (`article_id`),
	KEY `key_like_userid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='文章用户点赞记录关系表';

-- 博客通用设置信息表
create table enjoy.blog_settings
(
	user_id varchar(20) collate utf8mb4_bin not null comment '用户id',
	title varchar(100) collate utf8mb4_bin not null comment '博客标题',
	description varchar(100) collate utf8mb4_bin not null comment '博客描述',
  themeid varchar(200) collate utf8mb4_bin default null comment '博客主题',
	defaulteditor varchar(100) collate utf8mb4_bin default null comment '默认编辑器,code defaulteditor',
	commentnotifiedby char(3) collate utf8mb4_bin default null comment '评论通知方式,code commentnotifiedby',
	create_time datetime default null comment '创建时间',
	update_time datetime default null comment '更新时间',
	primary key (`user_id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_bin comment='博客通用设置表';