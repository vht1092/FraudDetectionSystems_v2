package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * The persistent class for the FDS_CASE_DETAIL database table.
 */
@Entity
@Table(name = "FDS_CASE_DETAIL")
@NamedQuery(name = "FdsCaseDetail.findAll", query = "SELECT f FROM FdsCaseDetail f")
public class FdsCaseDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CASE_NO", unique = true, nullable = false, length = 50)
	private String caseNo;

	@Column(nullable = false, precision = 18, scale = 2)
	private BigDecimal amount;

	@Column(name = "ASG_TMS", nullable = false, precision = 17)
	private BigDecimal asgTms;

	@Column(name = "AVL_BAL", nullable = false, precision = 18, scale = 2)
	private BigDecimal avlBal;

	@Column(name = "AVL_BAL_CRNCY", nullable = false, precision = 18, scale = 2)
	private BigDecimal avlBalCrncy;

	@Column(name = "CASE_STATUS", nullable = false, length = 3)
	private String caseStatus;

	@Column(name = "CHECK_NEW", nullable = false, length = 1)
	private String checkNew;

	@Column(name = "CIF_NO", nullable = false, length = 20)
	private String cifNo;

	@Column(name = "CRD_BRN", nullable = false, length = 2)
	private String crdBrn;

	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;

	@Column(name = "CRNCY_CDE", nullable = false, length = 3)
	private String crncyCde;

	@Column(name = "ENC_CRD_NO", nullable = false, length = 19)
	private String encCrdNo;

	@Column(name = "INIT_ASG_TMS", nullable = false, precision = 17)
	private BigDecimal initAsgTms;

	@Column(name = "INIT_USR_ID", nullable = false, length = 17)
	private String initUsrId;

	@Column(nullable = false, precision = 12)
	private BigDecimal loc;

	@Column(nullable = false, length = 4)
	private String mcc;

	@Column(name = "MERC_NAME", nullable = false, length = 22)
	private String mercName;

	@Column(name = "POS_MODE", nullable = false, length = 3)
	private String posMode;

	@Column(name = "RESP_CDE", nullable = false, length = 2)
	private String respCde;

	@Column(name = "SMS_FLG", nullable = false, length = 1)
	private String smsFlg;

	@Column(name = "TXN_3D_ECI", nullable = false, length = 2)
	private String txn3dEci;

	@Column(name = "TXN_3D_IND", nullable = false, length = 1)
	private String txn3dInd;

	@Column(name = "TXN_CRE_TMS", nullable = false, precision = 17)
	private BigDecimal txnCreTms;

	@Column(name = "TXN_STAT", nullable = false, length = 1)
	private String txnStat;

	@Column(name = "UPD_TMS", nullable = false, precision = 17)
	private BigDecimal updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;

	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "AUTOCLOSE", nullable = true, length = 1)
	private String autoClose;
	
	/*@Column(name = "CRN_LMT_AVL", nullable = false, precision = 18, scale = 2)
	private BigDecimal CrnMSLAvlBal;
	
	@Column(name = "CAV_AVL_LMT", nullable = false, precision = 18, scale = 2)
	private BigDecimal CavAvlBal;
	
	@Column(name = "ACC_AVL_BAL", nullable = false, precision = 18, scale = 2)
	private BigDecimal AccAvlBal;
	
	@Column(name = "crn", nullable = false, length = 8)
	private String crn;*/

	// bi-directional many-to-many association to FdsRule
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "FDS_CASE_HIT_RULES", joinColumns = { @JoinColumn(name = "CASE_NO", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "RULE_ID", nullable = false) })
	private List<FdsRule> fdsRules;

	public FdsCaseDetail() {
	}

	public String getCaseNo() {
		return this.caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAsgTms() {
		return this.asgTms;
	}

	public void setAsgTms(BigDecimal asgTms) {
		this.asgTms = asgTms;
	}

	public BigDecimal getAvlBal() {
		return this.avlBal;
	}

	public void setAvlBal(BigDecimal avlBal) {
		this.avlBal = avlBal;
	}

	public BigDecimal getAvlBalCrncy() {
		return this.avlBalCrncy;
	}

	public void setAvlBalCrncy(BigDecimal avlBalCrncy) {
		this.avlBalCrncy = avlBalCrncy;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCheckNew() {
		return this.checkNew;
	}

	public void setCheckNew(String checkNew) {
		this.checkNew = checkNew;
	}

	public String getCifNo() {
		return this.cifNo;
	}

	public void setCifNo(String cifNo) {
		this.cifNo = cifNo;
	}

	public String getCrdBrn() {
		return this.crdBrn;
	}

	public void setCrdBrn(String crdBrn) {
		this.crdBrn = crdBrn;
	}

	public BigDecimal getCreTms() {
		return this.creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	public String getCrncyCde() {
		return this.crncyCde;
	}

	public void setCrncyCde(String crncyCde) {
		this.crncyCde = crncyCde;
	}

	public String getEncCrdNo() {
		return this.encCrdNo;
	}

	public void setEncCrdNo(String encCrdNo) {
		this.encCrdNo = encCrdNo;
	}

	public BigDecimal getInitAsgTms() {
		return this.initAsgTms;
	}

	public void setInitAsgTms(BigDecimal initAsgTms) {
		this.initAsgTms = initAsgTms;
	}

	public String getInitUsrId() {
		return this.initUsrId;
	}

	public void setInitUsrId(String initUsrId) {
		this.initUsrId = initUsrId;
	}

	public BigDecimal getLoc() {
		return this.loc;
	}

	public void setLoc(BigDecimal loc) {
		this.loc = loc;
	}

	public String getMcc() {
		return this.mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMercName() {
		return this.mercName;
	}

	public void setMercName(String mercName) {
		this.mercName = mercName;
	}

	public String getPosMode() {
		return this.posMode;
	}

	public void setPosMode(String posMode) {
		this.posMode = posMode;
	}

	public String getRespCde() {
		return this.respCde;
	}

	public void setRespCde(String respCde) {
		this.respCde = respCde;
	}

	public String getSmsFlg() {
		return this.smsFlg;
	}

	public void setSmsFlg(String smsFlg) {
		this.smsFlg = smsFlg;
	}

	public String getTxn3dEci() {
		return this.txn3dEci;
	}

	public void setTxn3dEci(String txn3dEci) {
		this.txn3dEci = txn3dEci;
	}

	public String getTxn3dInd() {
		return this.txn3dInd;
	}

	public void setTxn3dInd(String txn3dInd) {
		this.txn3dInd = txn3dInd;
	}

	public BigDecimal getTxnCreTms() {
		return this.txnCreTms;
	}

	public void setTxnCreTms(BigDecimal txnCreTms) {
		this.txnCreTms = txnCreTms;
	}

	public String getTxnStat() {
		return this.txnStat;
	}

	public void setTxnStat(String txnStat) {
		this.txnStat = txnStat;
	}

	public BigDecimal getUpdTms() {
		return this.updTms;
	}

	public void setUpdTms(BigDecimal updTms) {
		this.updTms = updTms;
	}

	public String getUpdUid() {
		return this.updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public String getUsrId() {
		return this.usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public List<FdsRule> getFdsRules() {
		return this.fdsRules;
	}

	public void setFdsRules(List<FdsRule> fdsRules) {
		this.fdsRules = fdsRules;
	}

	/**
	 * @return the autoClose
	 */
	public String getAutoClose() {
		return autoClose;
	}

	/**
	 * @param autoClose the autoClose to set
	 */
	public void setAutoClose(String autoClose) {
		this.autoClose = autoClose;
	}

	/*public BigDecimal getAccAvlBal() {
		return this.AccAvlBal;
	}

	public void setAccAvlBal(BigDecimal AccAvlBal) {
		this.AccAvlBal = AccAvlBal;
	}
	
	public BigDecimal getCavAvlBal() {
		return this.CavAvlBal;
	}

	public void setCavAvlBal(BigDecimal CavAvlBal) {
		this.CavAvlBal = CavAvlBal;
	}
	
	public BigDecimal getCrnMSLAvlBal() {
		return this.CrnMSLAvlBal;
	}

	public void setCrnMSLAvlBal(BigDecimal CrnMSLAvlBal) {
		this.CrnMSLAvlBal = CrnMSLAvlBal;
	}
	
	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}*/
}