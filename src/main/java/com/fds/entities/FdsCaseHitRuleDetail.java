package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FDS_CASE_HIT_RULE_DETAIL database table.
 * 
 */
@Entity
@Table(name="FDS_CASE_HIT_RULE_DETAIL")
@NamedQuery(name="FdsCaseHitRuleDetail.findAll", query="SELECT f FROM FdsCaseHitRuleDetail f")
public class FdsCaseHitRuleDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CASE_NO", nullable=false, length=50)
	private String caseNo;

	@Id
	@Column(name="CRE_TMS", nullable=false, precision=17)
	private BigDecimal creTms;

	@Column(name="RULE_ID", nullable=false, length=12)
	private String ruleId;

	@Column(name="TXN_CRE_TMS", nullable=false, precision=17)
	private BigDecimal txnCreTms;

	@Column(name="TXN_DT", nullable=false, precision=8)
	private BigDecimal txnDt;

	@Column(name="TXN_ENC_CRD_NO", nullable=false, length=19)
	private String txnEncCrdNo;

	@Column(name="TXN_MCC", nullable=false, precision=4)
	private BigDecimal txnMcc;

	@Column(name="TXN_SEQ_NO", nullable=false, precision=6)
	private BigDecimal txnSeqNo;

	@Column(name="TXN_TM", nullable=false, precision=8)
	private BigDecimal txnTm;

	@Column(name="UPD_TMS", nullable=false, precision=17)
	private BigDecimal updTms;

	@Column(name="UPD_UID", nullable=false, length=12)
	private String updUid;

	public FdsCaseHitRuleDetail() {
	}

	public String getCaseNo() {
		return this.caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public BigDecimal getCreTms() {
		return this.creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	public String getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getTxnCreTms() {
		return this.txnCreTms;
	}

	public void setTxnCreTms(BigDecimal txnCreTms) {
		this.txnCreTms = txnCreTms;
	}

	public BigDecimal getTxnDt() {
		return this.txnDt;
	}

	public void setTxnDt(BigDecimal txnDt) {
		this.txnDt = txnDt;
	}

	public String getTxnEncCrdNo() {
		return this.txnEncCrdNo;
	}

	public void setTxnEncCrdNo(String txnEncCrdNo) {
		this.txnEncCrdNo = txnEncCrdNo;
	}

	public BigDecimal getTxnMcc() {
		return this.txnMcc;
	}

	public void setTxnMcc(BigDecimal txnMcc) {
		this.txnMcc = txnMcc;
	}

	public BigDecimal getTxnSeqNo() {
		return this.txnSeqNo;
	}

	public void setTxnSeqNo(BigDecimal txnSeqNo) {
		this.txnSeqNo = txnSeqNo;
	}

	public BigDecimal getTxnTm() {
		return this.txnTm;
	}

	public void setTxnTm(BigDecimal txnTm) {
		this.txnTm = txnTm;
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

}