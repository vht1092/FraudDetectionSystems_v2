package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsRule;
import com.fds.repositories.RuleRepo;

@Service("ruleService")
public class RuleServiceImpl implements RuleService {

	@Autowired
	private RuleRepo ruleRepo;

	@Override
	public List<FdsRule> findAll() {
		return ruleRepo.findAll(new Sort(Sort.Direction.ASC, "rulePriority"));
	}

	@Override
	@Cacheable(cacheNames = "FdsRule.findByCaseNo", key = "#caseno")
	public List<FdsRule> findByCaseNo(String caseno) {
		return ruleRepo.findAllByCaseNo(caseno);
	}

	@Override
	@CacheEvict(cacheNames = {"FdsRule.findColorByCaseNo"}, allEntries = true)
	public void updateByRuleid(String id, String color) {
		FdsRule fdsRuleList = ruleRepo.findOneByruleId(id);
		fdsRuleList.setRuleLevel(color);
		ruleRepo.save(fdsRuleList);
	}

	@Override
	@Cacheable(cacheNames = "FdsRule.findColorByCaseNo", key = "#caseno")
	public String findColorByCaseNo(String caseno) {
		return ruleRepo.findColorByCaseNo(caseno);
	}

}
