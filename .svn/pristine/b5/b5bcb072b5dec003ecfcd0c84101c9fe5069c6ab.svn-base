package com.fds.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.vaadin.spring.annotation.VaadinSessionScope;

/**
 * The persistent class for the FDS_CASE_STATUS database table.
 * 
 */
@Entity
@Table(name = "FDS_CASE_STATUS")
@VaadinSessionScope
public class FdsCaseStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "FDS_CASE_STATUS_ID_GENERATOR", sequenceName = "SQ_FDS_CASE_STATUS", allocationSize = 100)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FDS_CASE_STATUS_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private long id;

	@Column(name = "CASE_ACTION", nullable = false, length = 3)
	private String caseAction;

	@Column(name = "CASE_COMMENT", nullable = false, length = 100)
	private String caseComment;

	@Column(name = "CASE_NO", length = 20)
	private String caseNo;

	@Column(name = "CLOSED_REASON", nullable = false, length = 3)
	private String closedReason;

	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;

	@Column(length = 20)
	private String other;

	@Column(name = "USER_ID", nullable = false, length = 12)
	private String userId;

	public FdsCaseStatus() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCaseAction() {
		return this.caseAction;
	}

	public void setCaseAction(String caseAction) {
		this.caseAction = caseAction;
	}

	public String getCaseComment() {
		return this.caseComment;
	}

	public void setCaseComment(String caseComment) {
		this.caseComment = caseComment;
	}

	public String getCaseNo() {
		return this.caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getClosedReason() {
		return this.closedReason;
	}

	public void setClosedReason(String closedReason) {
		this.closedReason = closedReason;
	}

	public BigDecimal getCreTms() {
		return this.creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}