package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysTaskAuditTrail;

@Repository
public interface SysTaskAuditTrailRepo  extends CrudRepository<FdsSysTaskAuditTrail, Long> {
	
	@Query(value = " select USERUPDATE, CREATEDATE " + 
				   " from {h-schema}FDS_SYS_TASK_AUDIT_TRAIL " +
				   " where OBJECTTASK = :objectTask "+ 
				   "	   and CREATEDATE = (select max(CREATEDATE) from {h-schema}FDS_SYS_TASK_AUDIT_TRAIL where OBJECTTASK = :objectTask)", nativeQuery = true)      
	public List<Object[]> getUserUpdate(@Param("objectTask") String objectTask);

}

