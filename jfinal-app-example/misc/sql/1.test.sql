CREATE TABLE `res` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(120) DEFAULT NULL COMMENT '资源名称',
  `type` varchar(120) DEFAULT NULL COMMENT '资源类型',
  `path` varchar(120) DEFAULT NULL COMMENT '资源地址',
  `action` varchar(120) DEFAULT NULL COMMENT '资源请求',
  `controller` varchar(120) DEFAULT NULL COMMENT '资源控制',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;