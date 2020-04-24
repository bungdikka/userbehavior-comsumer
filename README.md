# userbehavior-comsumer
用户行为上传mq,消费用户行为进行积分奖励

activemq生产者略 
例子：发送信息：{"bType":1,"userId":2}到queue:user-behavior 该服务消费这个信息 含义为userId=2的用户进行了bType=1的行为（例如登录/点击事件）等
