package com.moonlight.consumer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moonlight.business.PointRuleBusiness;
import com.moonlight.entity.Behavior;
import com.moonlight.entity.PointRule;
import com.moonlight.repository.PointRuleRepository;

@Component
public class Consumer {

	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	PointRuleRepository pointRuleRepository;

	@SuppressWarnings("unchecked")
	@JmsListener(destination = "user-behavior")
	public void processMessage(String content) {
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		System.out.println("开始消费内容:" + content);
		ObjectMapper objectMapper = new ObjectMapper();
		Behavior behavior = new Behavior();
		try {
			behavior = objectMapper.readValue(content, Behavior.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		int behaviorId = behavior.getbType();
		int userId = behavior.getUserId();
		List<PointRule> pointRulelist = pointRuleRepository.findByBehaviorId(behaviorId);
		String d = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String dMinus1day = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		for (PointRule pointRule : pointRulelist) {
			int ruleType = pointRule.getRuleType();
			String ruleKey = userId + ":" + behaviorId + ":" + ruleType + ":" + d;
			System.out.println(ruleKey);
			Object pointProgress = redisTemplate.opsForValue().get(ruleKey);
			switch (ruleType) {
			// 类型一（每日前几次行为奖励）
			case 1:
				PointRuleBusiness.rule1(pointProgress, ruleKey, pointRule);
				break;
			// 类型二（每日累计次数后奖励）
			case 2:
				PointRuleBusiness.rule2(pointProgress, ruleKey, pointRule);
				break;
			// 类型三（每次行为奖励）
			case 3:
					
				break;
			// 类型四（连续几天完成后奖励）
			case 4:
				String ruleKeyYesterday = userId + ":" + behaviorId + ":" + ruleType + ":" + dMinus1day;
				PointRuleBusiness.rule4(pointProgress, ruleKey, ruleKeyYesterday, pointRule);
				break;
			// 类型五（一次性奖励）
			case 5:

				break;
			// 类型六（按金额比例返利）
			case 6:

				break;

			default:
				break;
			}
		}
	}
}
