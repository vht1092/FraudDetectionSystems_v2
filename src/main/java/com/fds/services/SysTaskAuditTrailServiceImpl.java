package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsSysTask;
import com.fds.entities.FdsSysTaskAuditTrail;
import com.fds.repositories.SysTaskAuditTrailRepo;
import com.fds.repositories.SysTaskRepo;

@Service("sysTaskAuditTrailService")
@Transactional
public class SysTaskAuditTrailServiceImpl implements SysTaskAuditTrailService {
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private SysTaskAuditTrailRepo sysTaskAuditTrailRepo;

	@Override
	public void save(String object, BigDecimal fromdate, BigDecimal todate, 
			String content, String type, String userid, BigDecimal createDate,
			String userUpdate, String actionStatus, String merchant, String posmode) {
		
		FdsSysTaskAuditTrail fdsSysTaskAuditTrail = new FdsSysTaskAuditTrail();
		fdsSysTaskAuditTrail.setObjecttask(object);
		fdsSysTaskAuditTrail.setFromdate(fromdate);
		fdsSysTaskAuditTrail.setTodate(todate);
		fdsSysTaskAuditTrail.setContenttask(content);
		fdsSysTaskAuditTrail.setTypetask(type);
		fdsSysTaskAuditTrail.setUserid(userid);
		fdsSysTaskAuditTrail.setCreatedate(createDate);
		fdsSysTaskAuditTrail.setUserupdate(userUpdate);
		fdsSysTaskAuditTrail.setActionStatus(actionStatus);
		fdsSysTaskAuditTrail.setMerchant(merchant);
		fdsSysTaskAuditTrail.setPosmode(posmode);
		
		sysTaskAuditTrailRepo.save(fdsSysTaskAuditTrail);
		
		
	}
	
	@Override
	public List<Object[]> getUserUpdate(String objectTask) {
		return sysTaskAuditTrailRepo.getUserUpdate(objectTask);
	}

	
	
}
