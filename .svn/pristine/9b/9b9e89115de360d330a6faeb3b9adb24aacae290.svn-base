package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fds.entities.FdsRule;

public interface RuleRepo extends JpaRepository<FdsRule, String> {

	FdsRule findOneByruleId(String ruleid);

	@Query(value = "select r.* from {h-schema}fds_case_hit_rules t join {h-schema}fds_rules r on t.rule_id=r.rule_id where t.case_no=:caseno order by r.rule_priority", nativeQuery = true)
	List<FdsRule> findAllByCaseNo(@Param("caseno") String caseno);

	@Query(value = "select rule_level from (select r.rule_level from {h-schema}fds_rules r, {h-schema}fds_case_hit_rules h where h.rule_id = r.rule_id and h.case_no = :caseno order by r.rule_priority asc) where rownum = 1", nativeQuery = true)
	String findColorByCaseNo(@Param("caseno") String caseno);

}
