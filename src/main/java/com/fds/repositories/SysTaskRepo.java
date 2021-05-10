package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysTask;

@Repository
public interface SysTaskRepo extends CrudRepository<FdsSysTask, Long> {
	
	@Query(value = "select count(t.id) from FdsSysTask t where t.objecttask = :caseno and :currentdate between t.fromdate and t.todate")
	int countByCaseNo(@Param("caseno") String caseno, @Param("currentdate") BigDecimal currentdate);

	@Query(value = "select t.id,t.fromdate,t.todate,t.object_task,t.type_task,t.priority,t.userid  from fds_sys_task t where t.userid = :userid and substr(t.todate, 0, 12) = to_char(to_date('201610311631','yyyyMMddHH24MI'), 'yyyyMMddHH24MI') and t.type_task = :type", nativeQuery = true)
	Iterable<FdsSysTask> findAllByUseridWithCurrentTime(@Param("userid") String userid, @Param("type") String type);

	@Query("select f from FdsSysTask f where f.objecttask=:object and to_number(to_char(sysdate, 'yyyyMMddHH24MISSSSS')) between f.fromdate and f.todate and f.typetask=:type")
	FdsSysTask findOneByObjectAndCurrentTime(@Param("object") String object, @Param("type") String type);

	@Query("select f from FdsSysTask f where f.objecttask=:object and f.typetask=:type")
	Iterable<FdsSysTask> findAllByObjectTask(@Param("object") String object, @Param("type") String type);

	List<FdsSysTask> findAllByTypetask(String type);

	// Dung cho exception case
	@Query("select f from FdsSysTask f where f.objecttask=:object and f.typetask=:type")
	FdsSysTask findOneByObjecttaskAndTypetask(@Param("object") String object, @Param("type") String type);

	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();

	@Query("select t from FdsSysTask t where not (:currentdate between t.fromdate and t.todate) and t.typetask=:type")
	Iterable<FdsSysTask> findAllByTypeAndNotInCurrenttime(@Param("currentdate") BigDecimal currentdate, @Param("type") String type);

	void deleteByUseridAndObjecttaskAndTypetask(@Param("userid") String userid, @Param("object") String object, @Param("type") String type);

	void deleteByUseridAndTypetask(@Param("userid") String userid, @Param("type") String type);

	void deleteByObjecttaskAndTypetask(@Param("object") String object, @Param("type") String type);
	
	@Query("select t from FdsSysTask t where objecttask =:object")
	FdsSysTask findOneByObject(@Param("object") String object);
	
	@Query(value = "select USERID, CREATEDATE from {h-schema}FDS_SYS_TASK where OBJECTTASK = :objectTask", nativeQuery = true)
	public List<Object[]> getUserUpdate(@Param("objectTask") String objectTask);
}
