package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fds.TimeConverter;
import com.fds.entities.FdsSysTask;
import com.fds.repositories.SysTaskRepo;

@Service("sysTaskService")
@Transactional
public class SysTaskServiceImpl implements SysTaskService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private SysTaskRepo sysTaskRepo;
	private TimeConverter timeConverter;

	@Override
	public Iterable<FdsSysTask> findAllByUseridWithCurrentTime(String userid, String type) {
		return sysTaskRepo.findAllByUseridWithCurrentTime(userid, type);
	}

	@Override
	public void save(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid, BigDecimal createDate, String merchant, String posmode) {

		FdsSysTask fdsSysTask = new FdsSysTask();
		FdsSysTask tempFdsSysTask = sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
		if (tempFdsSysTask != null) {
			fdsSysTask = tempFdsSysTask;
		}
		fdsSysTask.setObjecttask(object);
		fdsSysTask.setFromdate(fromdate);
		fdsSysTask.setTodate(todate);
		fdsSysTask.setContenttask(content);
		fdsSysTask.setTypetask(type);
		fdsSysTask.setUserid(userid);
		fdsSysTask.setCreatedate(createDate);
		fdsSysTask.setMerchant(merchant);
		fdsSysTask.setPosmode(posmode);
		sysTaskRepo.save(fdsSysTask);
	}

	@Override

	public void save(String object, String content, String type, String userid) {
		FdsSysTask fdsSysTask = new FdsSysTask();
		timeConverter = new TimeConverter();
		fdsSysTask.setObjecttask(object);
		fdsSysTask.setFromdate(new BigDecimal(timeConverter.getCurrentTime()));
		fdsSysTask.setTodate(new BigDecimal(timeConverter.getCurrentTime()));
		fdsSysTask.setContenttask(content);
		fdsSysTask.setTypetask(type);
		fdsSysTask.setUserid(userid);
		sysTaskRepo.save(fdsSysTask);
	}

	@Override
	public FdsSysTask findOneByObjectTaskAndTypeTask(String object, String type) {
		return sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
	}

	@Override
	public FdsSysTask findOneByObjectAndCurrentTime(String object, String type) {
		return sysTaskRepo.findOneByObjectAndCurrentTime(object, type);
	}

	@Override
	public void update(String object, BigDecimal fromdate, BigDecimal todate, String content, String type, String userid) {
		FdsSysTask fdsSysTask = sysTaskRepo.findOneByObjecttaskAndTypetask(object, type);
		fdsSysTask.setFromdate(fromdate);
		fdsSysTask.setTodate(todate);
		fdsSysTask.setContenttask(content);
		fdsSysTask.setUserid(userid);
		sysTaskRepo.save(fdsSysTask);

	}
	
	@Override
	public FdsSysTask findOneByObject(String object) {
		return sysTaskRepo.findOneByObject(object);
	}

	@Override
	public void delete(String userid, String object, String type) {
		sysTaskRepo.deleteByUseridAndObjecttaskAndTypetask(userid, object, type);
	}

	@Override
	public void delete(String userid, String type) {
		sysTaskRepo.deleteByUseridAndTypetask(userid, type);
	}

	@Override
	public List<FdsSysTask> findAllByTypeTask(String typetask) {
		return sysTaskRepo.findAllByTypetask(typetask);
	}

	@Override
	public void deleteByObjecttaskAndTypetask(String object, String type) {
		sysTaskRepo.deleteByObjecttaskAndTypetask(object, type);

	}
	
	@Override
	public List<Object[]> getUserUpdate(String objectTask) {
		return sysTaskRepo.getUserUpdate(objectTask);
	}


}
