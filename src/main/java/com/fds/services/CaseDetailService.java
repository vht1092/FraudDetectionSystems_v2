package com.fds.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.fds.entities.FdsCaseDetail;

public interface CaseDetailService {

	/**
	 * Lay danh sach cac case o trang thay moi chua tiep nhan
	 * 
	 * @param page
	 *            PageRequest
	 * @return Page
	 */
	public Page<FdsCaseDetail> findAllBycheckNew(Pageable page);
	
	public Page<FdsCaseDetail> findAllBycheckNewIsAndNotMerc(Pageable page);
	
	public Page<FdsCaseDetail> findAllBycheckNewAndMerc(Pageable page);
	
	/**
	 * Danh sach case dang xu ly boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<FdsCaseDetail> findAllProcessingByUser(Pageable page, String userid);

	/**
	 * Lay danh sach case theo trang thai
	 * 
	 * @param page
	 *            PageRequest
	 * @param status
	 *            Trang thai case
	 * @return Page
	 */

	public Page<FdsCaseDetail> findAllByStatus(Pageable page, String status);

	/**
	 * 
	 * @param roleid
	 * @return
	 */
	public String findByRoleId(String userid);
	
	/**
	 * Danh sach case da duoc dong boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<FdsCaseDetail> findAllClosedByUser(Pageable page, String userid);
	
	public Page<FdsCaseDetail> findAllAutoClosedByUser(Pageable page, String userid);

	public Page<FdsCaseDetail> findAllClosed(Pageable page);
	
	public Page<FdsCaseDetail> findAllAutoClosed(Pageable page);
	/**
	 * Dung cho man hinh tim kiem
	 * 
	 * @param
	 * @return Page
	 */
	public Page<FdsCaseDetail> search(String caseno, String custname, String userid, String fromdate, String todate, String status, String cardno, String cardbrand);

	/**
	 * Lay danh sach cac giao dich co lien quan theo case
	 * 
	 * @param caseno
	 *            So case
	 * @return List
	 */
	public List<Object[]> findTransactionDetailByCaseNo(String caseno, int numberofmonth);

	/**
	 * Thong tin cho man hinh chi tiet case
	 * 
	 * @param caseno
	 *            So case
	 * @return Object
	 * 
	 */	
	//public FdsCaseDetail findCaseDetailByCaseno(String caseno);
	public List<Object[]> findCaseDetailByCaseno(String caseno);
	/**
	 * Thong tin case
	 * 
	 * @param caseNo
	 *            So case
	 * @return FdsCaseDetail
	 */
	public FdsCaseDetail findOneByCaseNo(String caseNo);

	/**
	 * Danh sach case moi nhat chua duoc tiep nhan
	 * 
	 * @return int
	 */
	public int countAllNewestUserNotAssigned();

	// public int countProecssingByUser(String userid);

	// public int countAllAssigned();

	public int countAllClosedCase();

	// public void updateStatusCase(String caseno, String userid, String staus);

	/**
	 * Dong case va cap nhat trang thai
	 * 
	 * @param caseno
	 *            So case
	 * @param userid
	 *            User id
	 * @param status
	 *            Trang thai cua case: "DIC", "CAF"
	 * @return boolean
	 */
	public boolean closeCase(String caseno, String userid, String status);
	
	/**
	 * Dong case va cap nhat trang thai, flag auto close case
	 * 
	 * @param caseno
	 *            So case
	 * @param userid
	 *            User id
	 * @param status
	 *            Trang thai cua case: "DIC", "CAF"
	 * @return boolean
	 */
	public boolean autoCloseCase(String caseno, String userid, String status);

	/**
	 * Mo lai case, trang thai case se la REO
	 * 
	 * @param caseno
	 *            So case
	 * @param userid
	 *            User id
	 */
	public void reopenCase(String caseno, String userid);

	/**
	 * Dung cho dang ky callback, trang thai case = CAL
	 * 
	 * @param
	 * @return boolean
	 */
	public boolean callBackCase(String caseno, String userid, BigDecimal fromdate, BigDecimal todate);

	/**
	 * Dung khi chuyen case cho user nao do, trang thai case se la TRA
	 * 
	 * @param caseno
	 *            So case
	 * @param userid
	 *            User id can chuyen
	 */
	public void updateAssignedUser(String caseno, String userid);

	/**
	 * Giai ma ma so the dung ham DED2 trong database
	 * 
	 * @param cardno
	 *            So the da ma hoa
	 * @return <b>String</b> So the da duoc giai ma
	 */
	/*huyennt adding 20170710*/
	public boolean unAssignedCase(String caseno);
	
	public String getDed2(String cardno);

	/**
	 * Cap nhat trang thai case khi het thoi gian callback
	 * 
	 */
	public void updateTaskCase();
	
	/**
	 * Thuc hien tam khoa the cho KH
	 * @param encPan
	 * @param loc
	 * @param desc
	 * @param userPeform (User thuc hien tam khoa the cho KH)
	 * @return
	 */
	public int tempLockCard(String encPan, String desc, String userPeform);
	
	public List<FdsCaseDetail> findCaseByMerchantSystask(String userRecord);

	// Report
	public List<Object[]> reportCaseByUser(String fromdate, String todate, String userid, String closed_reason, String crdbrn);
	public List<Object[]> reportAllCase(String fromdate, String todate);
	public List<Object[]> reportCaseByTxn(String fromdate, String todate, String crdbrn);
	public List<Object[]> reportCaseByStatus(String fromdate, String todate, String crdbrn, String userid, String status);
	public List<Object[]> reportRuleId(String fromdate, String todate, String ruleid);
	public List<Object[]> reportMerchant(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc);
	public List<Object[]> reportTxnCrdDet(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc);
	
	public int updateStatusCaseClear(String user, BigDecimal fromdate, BigDecimal todate);
}
