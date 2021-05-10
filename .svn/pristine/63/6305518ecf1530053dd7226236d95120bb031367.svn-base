package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
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
}
