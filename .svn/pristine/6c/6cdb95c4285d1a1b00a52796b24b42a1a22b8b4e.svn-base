package com.fds.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the FDS_RULES database table.
 * 
 */
@Entity
@Table(name = "FDS_RULES")
@NamedQuery(name = "FdsRule.findAll", query = "SELECT f FROM FdsRule f")
public class FdsRule implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RULE_ID", unique = true, nullable = false, length = 12)
	private String ruleId;

	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;

	@Column(name = "RULE_DESC", nullable = false, length = 200)
	private String ruleDesc;

	@Column(name = "RULE_LEVEL", nullable = false, length = 20)
	private String ruleLevel;

	@Column(name = "RULE_PRIORITY", nullable = false, precision = 2)
	private int rulePriority;

	@Column(name = "RULE_SP_NAME", nullable = false, length = 50)
	private String ruleSpName;

	@Column(name = "RULE_NAME", nullable = false, length = 200)
	private String ruleName;

	@Column(name = "UPD_TMS", nullable = false, precision = 17)
	private BigDecimal updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;

	// bi-directional many-to-many association to FdsCaseDetail
	@ManyToMany(mappedBy = "fdsRules")
	private List<FdsCaseDetail> fdsCaseDetails;

	public FdsRule() {
	}

	public String getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getCreTms() {
		return this.creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	public String getRuleDesc() {
		return this.ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public String getRuleLevel() {
		return this.ruleLevel;
	}

	public void setRuleLevel(String ruleLevel) {
		this.ruleLevel = ruleLevel;
	}

	public int getRulePriority() {
		return this.rulePriority;
	}

	public void setRulePriority(int rulePriority) {
		this.rulePriority = rulePriority;
	}

	public String getRuleSpName() {
		return this.ruleSpName;
	}

	public void setRuleSpName(String ruleSpName) {
		this.ruleSpName = ruleSpName;
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

	public List<FdsCaseDetail> getFdsCaseDetails() {
		return this.fdsCaseDetails;
	}

	public void setFdsCaseDetails(List<FdsCaseDetail> fdsCaseDetails) {
		this.fdsCaseDetails = fdsCaseDetails;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

}