-- 测试环境
-- 每次删除数据
-- DROP TABLE IF EXISTS `clone_douban_tv_detail`;
-- DROP TABLE IF EXISTS `clone_douban_mv_detail`;
-- DROP TABLE IF EXISTS `clone_mv`;
-- DROP TABLE IF EXISTS `clone_tv`;
-- DROP TABLE IF EXISTS `task_management`;
-- DROP TABLE IF EXISTS `task_param`;
-- DROP TABLE IF EXISTS `task_log`;




create table if not exists `clone_douban_tv_detail` (
  `tvid` varchar(32) NOT NULL,
  `tvname` varchar(1000) DEFAULT NULL,
  `director` varchar(3000) DEFAULT '' COMMENT '导演',
  `scenarist` varchar(3000) DEFAULT NULL COMMENT '编剧',
  `actors` varchar(8000) DEFAULT NULL COMMENT '主演',
  `type` varchar(255) DEFAULT NULL COMMENT '类型',
  `country` varchar(1000) DEFAULT NULL COMMENT '制片国家/地区',
  `language` varchar(1000) DEFAULT NULL COMMENT '语言',
  `releasedata` varchar(1000) DEFAULT NULL COMMENT '上映日期',
  `runtime` varchar(255) DEFAULT NULL COMMENT '片长',
  `ratingnum` varchar(255) DEFAULT NULL COMMENT '豆瓣评分',
  `tags` varchar(3000) DEFAULT NULL COMMENT '标签',
  `moviedesc` varchar(8000) DEFAULT NULL COMMENT '简介',
  `p_date` varchar(14) DEFAULT NULL COMMENT '日期',
  `aka` varchar(2000) DEFAULT NULL COMMENT '又名',
  `http_image` varchar(500) DEFAULT NULL,
  `filepath` varchar(500) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `episodes_count` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tvid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists `clone_douban_mv_detail` (
  `movieid` varchar(32) NOT NULL,
  `moviename` varchar(2000) DEFAULT NULL,
  `director` text COMMENT '导演',
  `scenarist` text COMMENT '编剧',
  `actors` text COMMENT '主演',
  `type` varchar(2000) DEFAULT NULL COMMENT '类型',
  `country` varchar(2000) DEFAULT NULL COMMENT '制片国家/地区',
  `language` varchar(2000) DEFAULT NULL COMMENT '语言',
  `releasedata` varchar(2000) DEFAULT NULL COMMENT '上映日期',
  `runtime` varchar(300) DEFAULT NULL COMMENT '片长',
  `ratingnum` varchar(100) DEFAULT NULL COMMENT '豆瓣评分',
  `tags` varchar(2000) DEFAULT NULL COMMENT '标签',
  `moviedesc` varchar(3000) DEFAULT NULL COMMENT '简介',
  `p_date` varchar(14) DEFAULT NULL COMMENT '日期',
  `aka` varchar(1000) DEFAULT NULL COMMENT '又名',
  `http_image` varchar(255) DEFAULT NULL,
  `filepath` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`movieid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists `clone_mv` (
  `id` varchar(32) NOT NULL,
  `movieid` varchar(32) NOT NULL,
  `type` int(1) NOT NULL COMMENT '1-豆瓣',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists `clone_tv` (
  `id` varchar(32) NOT NULL,
  `tvid` varchar(32) NOT NULL,
  `type` int(1) NOT NULL COMMENT '1-豆瓣',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists `task_management` (
  `id` varchar(255) NOT NULL,
  `excute_target` varchar(255) NOT NULL DEFAULT '',
  `external_code` varchar(255) NOT NULL DEFAULT '',
  `task_excute_plan` varchar(255) NOT NULL DEFAULT '',
  `task_id` varchar(255) NOT NULL DEFAULT '',
  `task_name` varchar(255) NOT NULL DEFAULT '',
  `task_status` varchar(255) NOT NULL DEFAULT '' COMMENT '1-正常\r\n2-错误',
  `task_type` varchar(255) NOT NULL DEFAULT '',
  `timeout_second` smallint(6) NOT NULL DEFAULT '0',
  `cre_dt` varchar(14) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table if not exists `task_param` (
  `id` varchar(255) NOT NULL,
  `param_name` varchar(255) DEFAULT NULL,
  `param_value` varchar(255) DEFAULT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table if not exists `task_log` (
  `id` varchar(32) NOT NULL,
  `begintime` varchar(14) NOT NULL DEFAULT '',
  `endtime` varchar(14) NOT NULL DEFAULT '',
  `time` bigint(20) NOT NULL DEFAULT '0',
  `status` int(1) NOT NULL DEFAULT '0',
  `execute_result` varchar(1000) NOT NULL DEFAULT '',
  `task_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;