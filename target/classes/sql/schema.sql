-- 测试环境
-- 每次删除数据
-- DROP TABLE IF EXISTS `clone_douban_tv_detail`;
-- DROP TABLE IF EXISTS `clone_mv_detail`;
-- DROP TABLE IF EXISTS `clone_data_type`;
-- DROP TABLE IF EXISTS `clone_page_no`;
-- DROP TABLE IF EXISTS `task_management`;
-- DROP TABLE IF EXISTS `task_param`;
-- DROP TABLE IF EXISTS `task_log`;

-- 电视剧表
create table if not exists `clone_douban_tv_detail` (
  `tvid` varchar(32) NOT NULL,
  `tvname` varchar(1000) DEFAULT NULL,
  `director` text COMMENT '导演',
  `scenarist` text COMMENT '编剧',
  `actors` text COMMENT '主演',
  `type` varchar(2000) DEFAULT NULL COMMENT '类型',
  `country` varchar(2000) DEFAULT NULL COMMENT '制片国家/地区',
  `language` varchar(2000) DEFAULT NULL COMMENT '语言',
  `releasedata` varchar(2000) DEFAULT NULL COMMENT '上映日期',
  `runtime` varchar(255) DEFAULT NULL COMMENT '片长',
  `ratingnum` varchar(255) DEFAULT NULL COMMENT '豆瓣评分',
  `tags` varchar(2000) DEFAULT NULL COMMENT '标签',
  `moviedesc` text NULL COMMENT '简介',
  `p_date` varchar(14) DEFAULT NULL COMMENT '日期',
  `aka` varchar(2000) DEFAULT NULL COMMENT '又名',
  `http_image` varchar(500) DEFAULT NULL,
  `filepath` varchar(500) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `episodes_count` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tvid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 电影表
create table if not exists `clone_mv_detail` (
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
  `moviedesc` text COMMENT '简介',
  `p_date` varchar(14) DEFAULT NULL COMMENT '日期',
  `aka` varchar(1000) DEFAULT NULL COMMENT '又名',
  `http_image` varchar(255) DEFAULT NULL COMMENT '封面网络地址',
  `filepath` varchar(255) DEFAULT NULL COMMENT '封面本地地址',
  `url` varchar(255) DEFAULT NULL COMMENT '详情网络地址',
  `mv_typeid` varchar(32) NOT NULL DEFAULT '' COMMENT '电影类别 关联clone_data_type表',
  PRIMARY KEY (`movieid`,`mv_typeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 爬取目标类型表
create table if not exists `clone_data_type` (
  `id` varchar(255) NOT NULL,
  `type_name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 爬取页数表
create table if not exists `clone_page_no` (
  `id` varchar(32) NOT NULL,
  `key` varchar(255) NOT NULL DEFAULT '',
  `value` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(32) NOT NULL DEFAULT '',
  `update_dt` varchar(14) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 定时任务配置表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 定时任务参数表
create table if not exists `task_param` (
  `id` varchar(255) NOT NULL,
  `param_name` varchar(255) DEFAULT NULL,
  `param_value` varchar(255) DEFAULT NULL,
  `task_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 定时任务日志表
create table if not exists `task_log` (
  `id` varchar(32) NOT NULL,
  `begintime` varchar(14) NOT NULL DEFAULT '',
  `endtime` varchar(14) NOT NULL DEFAULT '',
  `time` bigint(20) NOT NULL DEFAULT '0',
  `status` int(1) NOT NULL DEFAULT '0',
  `execute_result` varchar(1000) NOT NULL DEFAULT '',
  `task_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;