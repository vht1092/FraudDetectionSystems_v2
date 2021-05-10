package com.fds.services;

import java.math.BigDecimal;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsCaseClearLog;
import com.fds.repositories.CaseClearLogRepo;

@Service("caseClearLogService")
@Transactional
public class CaseClearLogServiceImpl implements CaseClearLogService {

	@Autowired
	private CaseClearLogRepo caseClearLogRepo;

	@Override
	public void create(String userid,String content,String closedreason,BigDecimal datefrom, BigDecimal dateto, BigDecimal totalCase) {

		FdsCaseClearLog fdsCaseClearLog = new FdsCaseClearLog();
		fdsCaseClearLog.setCreTms(caseClearLogRepo.getCurrentTime());
		fdsCaseClearLog.setUserId(userid.toUpperCase());
		fdsCaseClearLog.setContent(content);
		fdsCaseClearLog.setClosedReason(closedreason);
		fdsCaseClearLog.setDateFrom(datefrom);
		fdsCaseClearLog.setDateTo(dateto);
		fdsCaseClearLog.setTotalCase(totalCase);
		
		caseClearLogRepo.save(fdsCaseClearLog);
	}

}