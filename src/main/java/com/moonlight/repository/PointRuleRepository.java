package com.moonlight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moonlight.entity.PointRule;

public interface PointRuleRepository extends JpaRepository<PointRule, Long> {
	public List<PointRule> findByBehaviorId(int behaviorId);
	
}
