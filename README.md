# userbehavior-comsumer
用户行为上传mq,消费用户行为进行积分奖励

activemq生产者略 
例子：发送信息：{"bType":1,"userId":2}到queue:user-behavior 该服务消费这个信息 含义为userId=2的用户进行了bType=1的行为（例如登录/点击事件）等



CREATE TABLE `point_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `rule_type` varchar(255) DEFAULT NULL COMMENT '规则类型',
  `behavior_id` int(11) DEFAULT NULL COMMENT '用户行为id',
  `behavior_count` int(6) DEFAULT NULL COMMENT '用户行为发生次数要求',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


INSERT INTO `tiger_fastened_shut`.`point_rule` (`id`, `rule_name`, `rule_type`, `behavior_id`, `behavior_count`) VALUES ('1', '应用启动', '1', '1', '5');
INSERT INTO `tiger_fastened_shut`.`point_rule` (`id`, `rule_name`, `rule_type`, `behavior_id`, `behavior_count`) VALUES ('2', '首次注册', '2', '2', '1');



