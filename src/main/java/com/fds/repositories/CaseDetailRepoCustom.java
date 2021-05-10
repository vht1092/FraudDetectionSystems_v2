package com.fds.repositories;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fds.entities.FdsCaseDetail;

public interface CaseDetailRepoCustom {
	public Page<FdsCaseDetail> searchCase(String query, String fromdate, String todate);
	
	public List<Object[]> reportCaseByUser(String fromdate, String todate, String userid, String closed_reason, String crdbrn);
	public List<Object[]> reportAllCase(String fromdate, String todate);
	public List<Object[]> reportCaseByTxn(String fromdate, String todate, String crdbrn);
	public List<Object[]> reportCaseByStatus(String fromdate, String todate, String crdbrn, String userid, String status);
	public List<Object[]> reportRuleId(String fromdate, String todate, String ruleid);
	public List<Object[]> reportMerchant(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc);	
	public List<Object[]> reportTxnCrdDet(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc);
}
