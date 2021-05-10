package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FDS_SYS_BRANCH database table.
 * 
 */
@Entity
@Table(name="FDS_SYS_BRANCH")
@NamedQuery(name="FdsSysBranch.findAll", query="SELECT f FROM FdsSysBranch f")
public class FdsSysBranch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=3)
	private String code;

	@Column(nullable=false, length=255)
	private String address;

	@Column(nullable=false, length=20)
	private String fax;

	@Column(nullable=false, length=50)
	private String name;

	@Column(name="PAGEAUTH_DATE", length=10)
	private String pageauthDate;

	@Column(name="PAGEAUTH_NO", length=50)
	private String pageauthNo;

	@Column(nullable=false, length=20)
	private String phone;

	@Column(nullable=false, length=50)
	private String representer;

	@Column(name="REPRESENTER_POS", nullable=false, length=50)
	private String representerPos;

	public FdsSysBranch() {
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPageauthDate() {
		return this.pageauthDate;
	}

	public void setPageauthDate(String pageauthDate) {
		this.pageauthDate = pageauthDate;
	}

	public String getPageauthNo() {
		return this.pageauthNo;
	}

	public void setPageauthNo(String pageauthNo) {
		this.pageauthNo = pageauthNo;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRepresenter() {
		return this.representer;
	}

	public void setRepresenter(String representer) {
		this.representer = representer;
	}

	public String getRepresenterPos() {
		return this.representerPos;
	}

	public void setRepresenterPos(String representerPos) {
		this.representerPos = representerPos;
	}

}