package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsCaseStatus;

@Repository
public interface CaseStatusRepo extends CrudRepository<FdsCaseStatus, Long> {

	List<FdsCaseStatus> findAllByCaseNo(@Param("caseno") String caseno, Pageable page);

	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();
	
	@Modifying
	@Query(value = "INSERT INTO FDS_CASE_STATUS(ID, CASE_NO, CRE_TMS, USER_ID, CASE_COMMENT, CASE_ACTION, CLOSED_REASON, OTHER)\r\n" + 
			"SELECT SQ_FDS_CASE_STATUS.nextval ID,\r\n" + 
			"CASE_NO,(select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) CRE_TMS,\r\n" + 
			":user USER_ID,:content CASE_COMMENT,'DIC' CASE_ACTION,\r\n" + 
			":closedReason CLOSED_REASON,null OTHER\r\n" + 
			"FROM FDS_CASE_DETAIL\r\n" + 
			"WHERE CRE_TMS BETWEEN :fromdate AND :todate\r\n" + 
			"AND CHECK_NEW='Y' ",nativeQuery = true)
	int insertListCaseStatusClear(@Param(value = "user") String user,@Param(value = "content") String content, @Param(value = "closedReason") String closedReason,@Param(value = "fromdate") BigDecimal fromdate,@Param(value = "todate") BigDecimal todate);
	
}
