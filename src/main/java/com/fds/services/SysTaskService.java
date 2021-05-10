package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import com.fds.entities.FdsSysTask;

public interface SysTaskService {
	Iterable<FdsSysTask> findAllByUseridWithCurrentTime(String userid, String type);

	FdsSysTask findOneByObjectTaskAndTypeTask(String object, String type);

	FdsSysTask findOneByObjectAndCurrentTime(String object, String type);

	List<FdsSysTask> findAllByTypeTask(String typetask);
	
	FdsSysTask findOneByObject(String object);

	void save(String object, String content, String type, String userid);

	void save(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid, BigDecimal createDate, String merchant, String posmode);

	void update(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid);

	void delete(String userid, String object, String type);

	void delete(String userid, String type);

	void deleteByObjecttaskAndTypetask(String object, String type);
	
	
	/**
	 * 
	 * @param objectTask
	 * @return
	 */
	public List<Object[]> getUserUpdate(String objectTask);

}
