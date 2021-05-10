package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import com.fds.entities.FdsCaseStatus;

public interface CaseStatusService {
	void create (String caseno, String comment, String closedreason, String action, String other, String userid);
	List<FdsCaseStatus> findAllByCaseNo(String caseno);
	int insertListCaseStatusClear(String user,String content, String closedReason, BigDecimal fromdate, BigDecimal todate);
}
