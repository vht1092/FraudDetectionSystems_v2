package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FDS_SYS_TASK database table.
 * 
 */
@Entity
@Table(name="FDS_SYS_TASK")
@NamedQuery(name="FdsSysTask.findAll", query="SELECT f FROM FdsSysTask f")
public class FdsSysTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FDS_SYS_TASK_IDTASK_GENERATOR", sequenceName="SQ_FDS_SYS_TASK")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FDS_SYS_TASK_IDTASK_GENERATOR")
	@Column(unique=true, nullable=false)
	private long idtask;

	@Column(nullable=false, length=255)
	private String contenttask;

	@Column(precision=17)
	private BigDecimal fromdate;

	@Column(length=20)
	private String objecttask;

	private BigDecimal priority;

	@Column(precision=17)
	private BigDecimal todate;

	@Column(nullable=false, length=10)
	private String typetask;

	@Column(nullable=false, length=12)
	private String userid;
	
	@Column(name = "CREATEDATE", precision=17)
	private BigDecimal createdate;
	
	@Column(nullable=true,length=22)
	private String merchant;
	
	@Column(nullable=true,length=22)
	private String posmode;

	public FdsSysTask() {
	}

	public long getIdtask() {
		return this.idtask;
	}

	public void setIdtask(long idtask) {
		this.idtask = idtask;
	}

	public String getContenttask() {
		return this.contenttask;
	}

	public void setContenttask(String contenttask) {
		this.contenttask = contenttask;
	}

	public BigDecimal getFromdate() {
		return this.fromdate;
	}

	public void setFromdate(BigDecimal fromdate) {
		this.fromdate = fromdate;
	}

	public String getObjecttask() {
		return this.objecttask;
	}

	public void setObjecttask(String objecttask) {
		this.objecttask = objecttask;
	}

	public BigDecimal getPriority() {
		return this.priority;
	}

	public void setPriority(BigDecimal priority) {
		this.priority = priority;
	}

	public BigDecimal getTodate() {
		return this.todate;
	}

	public void setTodate(BigDecimal todate) {
		this.todate = todate;
	}

	public String getTypetask() {
		return this.typetask;
	}

	public void setTypetask(String typetask) {
		this.typetask = typetask;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public BigDecimal getCreatedate() {
		return createdate;
	}

	public void setCreatedate(BigDecimal createdate) {
		this.createdate = createdate;
	}

	/**
	 * @return the merchant
	 */
	public String getMerchant() {
		return merchant;
	}

	/**
	 * @param merchant the merchant to set
	 */
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	/**
	 * @return the posmode
	 */
	public String getPosmode() {
		return posmode;
	}

	/**
	 * @param posmode the posmode to set
	 */
	public void setPosmode(String posmode) {
		this.posmode = posmode;
	}

}