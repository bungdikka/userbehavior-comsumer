package com.moonlight.business;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.moonlight.entity.PointRule;

@SuppressWarnings("unchecked")
public class PointRuleBusiness {
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private static RedisTemplate redisTemplate;
	
	static {
		redisTemplate.setValueSerializer(new StringRedisSerializer());
	}
	
	public static void rule1(Object pointProgress,String ruleKey,PointRule pointRule) {
		if (pointProgress == null) {
			redisTemplate.opsForValue().set(ruleKey, "0", 1, TimeUnit.DAYS);
		}
		long r1CurrentCount = redisTemplate.opsForValue().increment(ruleKey);
		System.out.println("rt=" + r1CurrentCount);
		int r1limitBehaviorCount = pointRule.getBehaviorCount();
		// 每天N次内的指定行为都给与奖励
		if (r1CurrentCount <= r1limitBehaviorCount) {
			// 增加积分
			System.out.println("第" + r1CurrentCount + "次分享增加积分");
		}
	}
	
	public static void rule2(Object pointProgress,String ruleKey,PointRule pointRule) {
		if (pointProgress == null) {
			redisTemplate.opsForValue().set(ruleKey, "0", 1, TimeUnit.DAYS);
		}
		long r2CurrentCount = redisTemplate.opsForValue().increment(ruleKey);
		System.out.println("rt=" + r2CurrentCount);
		int r2limitBehaviorCount = pointRule.getBehaviorCount();
		// 累计到达目标次数则奖励
		if (r2CurrentCount == r2limitBehaviorCount) {
			// 增加积分
			System.out.println("达成："+r2CurrentCount+"次");
		}
	}

	public static void rule4(Object pointProgress,String ruleKey,String ruleKeyYesterday,PointRule pointRule) {
		if (pointProgress == null) {
			Object pointProgressMinus1day = redisTemplate.opsForValue().get(ruleKeyYesterday);
			if (pointProgressMinus1day == null) {
				// 昨天未做此行为，意味着没有连续N天完成任务，今天标记为任务的第一天
				redisTemplate.opsForValue().set(ruleKey, "1", 2, TimeUnit.DAYS);
			} else {
				// 此规则下，这个字段表示规则要求连续N天完成
				int r4limitBehaviorCount = pointRule.getBehaviorCount();
				int intPointProgressMinus1day = Integer.parseInt(pointProgressMinus1day.toString());
				if (intPointProgressMinus1day >= r4limitBehaviorCount) {
					// 昨天达成了任务奖励，今天重新开始计算
					redisTemplate.opsForValue().set(ruleKey, "1", 2, TimeUnit.DAYS);
				} else {
					if ((++intPointProgressMinus1day) == r4limitBehaviorCount) {
						// 昨天是第N天完成此任务，今天是第N+1天完成此任务，如果今天满足了任务条件则奖励
						// 奖励todo
					}
					redisTemplate.opsForValue().set(ruleKey, intPointProgressMinus1day, 2, TimeUnit.DAYS);
				}
			}
		}
	}
}
