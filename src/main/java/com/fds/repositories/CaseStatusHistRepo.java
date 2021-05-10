package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsCaseStatusHist;

@Repository
public interface CaseStatusHistRepo extends CrudRepository<FdsCaseStatusHist, Long> {

	List<FdsCaseStatusHist> findAllByCaseNo(@Param("caseno") String caseno, Pageable page);

	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();
}
