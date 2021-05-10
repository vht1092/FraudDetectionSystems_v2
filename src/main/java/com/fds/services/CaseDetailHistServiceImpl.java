package com.fds.services;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fds.TimeConverter;
import com.fds.entities.FdsCaseDetailHist;
import com.fds.entities.FdsSysTask;
import com.fds.repositories.CaseDetailHistRepo;
import com.fds.repositories.SysTaskRepo;

@Service("caseDetailHistService")
@Transactional
public class CaseDetailHistServiceImpl implements CaseDetailHistService {

	private final TimeConverter timeConverter = new TimeConverter();
	// Chuyen case
	private final static String TRANSSTAT = "TRA";
	// Dong case
	private final static String DICSTAT = "DIC";
	// Mo lai case
	private final static String REOPENSTAT = "REO";
	// Goi lai sau
	private final static String CALLBACKSTAT = "CAL";
	// Giao dich fraud
	private final static String FRASTAT = "CAF";
	// Case se load trong menu case da dong
	public static final String[] CLOSEDCASESTAT = { DICSTAT, FRASTAT };
	// Case se load trong menu dang xu ly
	public static final String[] INBOXCASESTAT = { TRANSSTAT, REOPENSTAT, " " };

	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private CaseDetailHistRepo caseDetailRepo;
	@Autowired
	private SysTaskRepo sysTaskRepo;

	@Override
	public Page<FdsCaseDetailHist> findAllBycheckNew(Pageable page) {
		return caseDetailRepo.findAllBycheckNewIs("Y", page);
	}

	@Override
	public FdsCaseDetailHist findOneByCaseNo(String caseno) {
		return caseDetailRepo.findOneByCaseNo(caseno);
	}

	@Override
	public int countAllNewestUserNotAssigned() {
		return caseDetailRepo.countBycheckNewIs();
	}

	@Override
	public String findByRoleId(String userid) {
		return caseDetailRepo.findByRoleID(userid);
	};
	
	@Override
	public Page<FdsCaseDetailHist> findAllProcessingByUser(Pageable page, String userid) {
		return caseDetailRepo.findByUsrIIgnoreCasedAndCaseStatusIn(userid, INBOXCASESTAT, page);
	}

	@Override
	public Page<FdsCaseDetailHist> findAllClosedByUser(Pageable page, String userid) {
		return caseDetailRepo.findByUsrIIgnoreCasedAndCaseStatusIn(userid, CLOSEDCASESTAT, page);
	}
	
	@Override
	public Page<FdsCaseDetailHist> findAllAutoClosedByUser(Pageable page, String userid) {
		return caseDetailRepo.findByUsrIIgnoreCasedAndCaseStatusInAndAutoClose(userid, CLOSEDCASESTAT, "Y", page);
	}

	@Override
	public Page<FdsCaseDetailHist> findAllClosed(Pageable page) {
		return caseDetailRepo.findByCaseStatusIn(CLOSEDCASESTAT, page);
	}
	
	@Override
	public Page<FdsCaseDetailHist> findAllAutoClosed(Pageable page) {
		return caseDetailRepo.findByCaseStatusInAndAutoClose(CLOSEDCASESTAT,"Y", page);
	}
	
	@Override
	public int tempLockCard(String encPan, String desc, String userPeform) {
		return caseDetailRepo.tempLockCardRepo(encPan, desc, userPeform);
	}
	
	@Override
	public List<FdsCaseDetailHist> findCaseByMerchantSystask(String userRecord) {
		return caseDetailRepo.findCaseByMerchantSystask(userRecord);
	}
	
	@Override
	@CacheEvict(value = { "FdsRule.findColorByCaseNo", "FdsRule.findByCaseNo" }, key = "#caseno")
	public boolean closeCase(String caseno, String userid, String status) {
		FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		if (!fdsCaseDetail.getCaseStatus().equals(status)) {
			fdsCaseDetail.setCaseStatus(status);
			if (fdsCaseDetail.getAsgTms().toString().equals("0")) {
				fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
			}
			fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());

			fdsCaseDetail.setUsrId(userid.toUpperCase());
			fdsCaseDetail.setCheckNew(" ");
			caseDetailRepo.save(fdsCaseDetail);
			return true;
		}
		return false;
	}
	
	@Override
	@CacheEvict(value = { "FdsRule.findColorByCaseNo", "FdsRule.findByCaseNo" }, key = "#caseno")
	public boolean autoCloseCase(String caseno, String userid, String status) {
		FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		if (!fdsCaseDetail.getCaseStatus().equals(status)) {
			fdsCaseDetail.setCaseStatus(status);
			if (fdsCaseDetail.getAsgTms().toString().equals("0")) {
				fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
			}
			fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());

			fdsCaseDetail.setUsrId(userid.toUpperCase());
			fdsCaseDetail.setCheckNew(" ");
			fdsCaseDetail.setAutoClose("Y");
			caseDetailRepo.save(fdsCaseDetail);
			return true;
		}
		return false;
	}
	
	@Override
	public void reopenCase(String caseno, String userid) {
		FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setCaseStatus(CALLBACKSTAT);//Chuyen về box case theo doi
		fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());
		caseDetailRepo.save(fdsCaseDetail);
	}

	@Override
	public void updateAssignedUser(String caseno, String userid) {
		FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setUsrId(userid.toUpperCase());
		fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
		fdsCaseDetail.setCaseStatus(TRANSSTAT);
		fdsCaseDetail.setCheckNew(" ");
		caseDetailRepo.save(fdsCaseDetail);
	}

	/*huyennt adding 20170710*/
	@Override
	public boolean unAssignedCase(String caseno){
		FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
		fdsCaseDetail.setUsrId(" ");
		fdsCaseDetail.setAsgTms(BigDecimal.valueOf(0));
		fdsCaseDetail.setCaseStatus("NEW");
		fdsCaseDetail.setCheckNew("Y");
		try {
			caseDetailRepo.save(fdsCaseDetail);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public Page<FdsCaseDetailHist> search(String caseno, String custname, String userid, String fromdate, String todate, String status, String cardno, String cardbrand) {
		final String _fromdate = fromdate;
		final String _todate = todate;
		StringBuilder partitionQuery = new StringBuilder(" AND (fdscasedetail.cre_tms between " + fromdate + " and " + todate
				+ " OR fdscasedetail.upd_tms between " + fromdate + " and " + todate + ")");
		StringBuilder searchTerm = new StringBuilder();
		if (!"".equals(caseno)) {
			searchTerm.append(" AND fdscasedetail.case_no='" + caseno + "'");
		}
		
		if (!"".equals(custname)) {
			searchTerm.append(" AND fdscasedetail.cif_no in (select trim(fx_ir056_cif_no) from ccps.ir056@im where trim(fx_ir056_name) like '%"+ custname +"%')");
		}
		
		if (!"".equals(cardbrand)) {
			if ("MC".equals(cardbrand) || "VS".equals(cardbrand)) {
				searchTerm.append(" AND fdscasedetail.crd_brn='" + cardbrand + "'");
			}
			// Neu la MasterCard Debit
			if ("MD".equals(cardbrand)) {
				searchTerm.append(" AND (fdscasedetail.crd_brn='MC' AND fdstxndetail.fx_oa008_crd_pgm like 'MD%')");
			}
		}
		if (!"".equals(userid)) {
			searchTerm.append(" AND lower(fdscasedetail.usr_id)=lower('" + userid + "')");
		}
		if (!"".equals(cardno)) {
			searchTerm.append(" AND fdscasedetail.enc_crd_no =" + sSchema + ".ecd2('" + cardno + "','FDS')");
		}
		if (!"".equals(status)) {
			searchTerm.append(" AND lower(fdscasedetail.case_status)=lower('" + status + "')");
			if (status.equals("DIC") || status.equals("REO") || status.equals("CAF")) {
				partitionQuery = new StringBuilder(" AND fdscasedetail.upd_tms between " + fromdate + " and " + todate);
			}
		}
		if (!searchTerm.equals("")) {
			searchTerm = searchTerm.append(partitionQuery.toString());
		} else {
			searchTerm = partitionQuery;
		}
		return caseDetailRepo.searchCase(searchTerm.toString(), _fromdate, _todate);
	}

	@Override
	public boolean callBackCase(String caseno, String userid, BigDecimal fromdate, BigDecimal todate) {
		if (sysTaskRepo.countByCaseNo(caseno, fromdate) == 0) {

			FdsCaseDetailHist fdsCaseDetail = caseDetailRepo.findOneByCaseNo(caseno);
			fdsCaseDetail.setUsrId(userid.toUpperCase());
			fdsCaseDetail.setAsgTms(caseDetailRepo.getCurrentTime());
			fdsCaseDetail.setCaseStatus(CALLBACKSTAT);
			fdsCaseDetail.setUpdTms(caseDetailRepo.getCurrentTime());
			fdsCaseDetail.setCheckNew(" ");

			FdsSysTask fdsSysTask = new FdsSysTask();
			fdsSysTask.setObjecttask(caseno);
			fdsSysTask.setTypetask(CALLBACKSTAT);
			fdsSysTask.setPriority(new BigDecimal("1"));
			fdsSysTask.setFromdate(fromdate);
			fdsSysTask.setContenttask(" ");
			fdsSysTask.setTodate(todate);
			fdsSysTask.setUserid(userid);

			caseDetailRepo.save(fdsCaseDetail);
			sysTaskRepo.save(fdsSysTask);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int countAllClosedCase() {
		return caseDetailRepo.countByCaseStatusInAnd45Day();
	}

	@Override
	//tanvh1
//	@Cacheable(value = "FdsCaseDetailHist.getDed2A", key = "#cardno")
	@Cacheable(value = "FdsCaseDetailHist.getDed2", key = "#cardno")
	public String getDed2(String cardno) {
		return caseDetailRepo.getDed2(cardno);
	}

	@Override
	public List<Object[]> findTransactionDetailByCaseNo(String enccrdno, int numberofmonth) {
		if (numberofmonth != -1) {
			return caseDetailRepo.findTransactionByCase(enccrdno, numberofmonth);
		}else{
			return caseDetailRepo.findAllTransactionByCase(enccrdno);
		}
	}

	@Override
	public Page<FdsCaseDetailHist> findAllByStatus(Pageable page, String status) {
		return caseDetailRepo.findAllBycaseStatus(status, page);
	}

	@Override
	public void updateTaskCase() {
		BigDecimal currentTime = new BigDecimal(timeConverter.convertDateTimeToStr(getTimeAfter(0)));
		Iterable<FdsSysTask> resultCaseDetail = sysTaskRepo.findAllByTypeAndNotInCurrenttime(currentTime, "CAL");
		resultCaseDetail.forEach(s -> {
			sysTaskRepo.delete(s);
			caseDetailRepo.updateStatusCase(s.getObjecttask(), " ");
		});
	}

	/**
	 * Thoi gian hien tai + them so phut
	 * 
	 * @param amount
	 *            So phut can them
	 * @return Date
	 */
	private Date getTimeAfter(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}

	@Override
	public List<Object[]> findCaseDetailByCaseno(String caseno) {
		return caseDetailRepo.findCaseDetailByCaseno(caseno);
	}

	// Reports
	@Override
	public List<Object[]> reportCaseByUser(String fromdate, String todate, String userid, String closed_reason, String crdbrn) {
		return caseDetailRepo.reportCaseByUser(fromdate, todate, userid, closed_reason, crdbrn);
	}

	@Override
	public List<Object[]> reportAllCase(String fromdate, String todate) {
		return caseDetailRepo.reportAllCase(fromdate, todate);
	}

	@Override
	public List<Object[]> reportCaseByTxn(String fromdate, String todate, String crdbrn) {
		return caseDetailRepo.reportCaseByTxn(fromdate, todate, crdbrn);
	}

	@Override
	public List<Object[]> reportCaseByStatus(String fromdate, String todate, String crdbrn, String userid, String status) {
		return caseDetailRepo.reportCaseByStatus(fromdate, todate, crdbrn, userid, status);

	}

	@Override
	public List<Object[]> reportRuleId(String fromdate, String todate, String ruleid) {
		return caseDetailRepo.reportRuleId(fromdate, todate, ruleid);

	}

	@Override
	public List<Object[]> reportMerchant(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc) {
		return null;
	}

	@Override
	public List<Object[]> reportTxnCrdDet(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc) {
		String sFromdate = "";
		String sTodate = "";
		String sMerchant = "";
		String sTerminalId = "";
		String sCardNo = "";
		String sMcc = "";

		if (StringUtils.hasText(fromdate)) {
			sFromdate = fromdate;
		}
		if (StringUtils.hasText(todate)) {
			sTodate = todate;
		}
		if (StringUtils.hasText(merchant)) {
			sMerchant = merchant;
		}
		if (StringUtils.hasText(terminalId)) {
			sTerminalId = terminalId;
		}
		if (StringUtils.hasText(cardNo)) {
			sCardNo = cardNo;
		}
		if (StringUtils.hasText(mcc)) {
			sMcc = mcc;
		}

		return caseDetailRepo.reportTxnCrdDet(sFromdate, sTodate, sMerchant, sTerminalId, sCardNo, sMcc);
	}

}
