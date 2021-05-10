package com.fds.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.vaadin.spring.annotation.VaadinSessionScope;

/**
 * The persistent class for the FDS_CASE_CLEAR_LOG database table.
 * 
 */
@Entity
@Table(name = "FDS_CASE_CLEAR_LOG")
@VaadinSessionScope
public class FdsCaseClearLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;
	
	@Column(name = "USER_ID", nullable = false, length = 12)
	private String userId;
	
	@Column(name = "CONTENT", nullable = false, length = 100)
	private String content;
	
	@Column(name = "CLOSED_REASON", nullable = false, length = 3)
	private String closedReason;
	
	@Column(name = "DATE_FROM", nullable = false, precision = 17)
	private BigDecimal dateFrom;
	
	@Column(name = "DATE_TO", nullable = false, precision = 17)
	private BigDecimal dateTo;
	
	@Column(name = "TOTAL_CASE", nullable = false, precision = 17)
	private BigDecimal totalCase;
	

	public FdsCaseClearLog() {
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


	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the dateFrom
	 */
	public BigDecimal getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(BigDecimal dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public BigDecimal getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(BigDecimal dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * @return the totalCase
	 */
	public BigDecimal getTotalCase() {
		return totalCase;
	}

	/**
	 * @param totalCase the totalCase to set
	 */
	public void setTotalCase(BigDecimal totalCase) {
		this.totalCase = totalCase;
	}
	
	

}