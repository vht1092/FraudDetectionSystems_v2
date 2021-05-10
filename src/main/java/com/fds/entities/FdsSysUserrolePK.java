package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the FDS_SYS_USERROLE database table.
 * 
 */
@Embeddable
public class FdsSysUserrolePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false, length=12)
	private String iduser;

	@Column(unique=true, nullable=false, precision=3)
	private long idrole;

	public FdsSysUserrolePK() {
	}
	
	public FdsSysUserrolePK(String iduser, long idrole) {	
		this.iduser = iduser;
		this.idrole = idrole;
	}


	public String getIduser() {
		return this.iduser;
	}
	public void setIduser(String iduser) {
		this.iduser = iduser;
	}
	public long getIdrole() {
		return this.idrole;
	}
	public void setIdrole(long idrole) {
		this.idrole = idrole;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FdsSysUserrolePK)) {
			return false;
		}
		FdsSysUserrolePK castOther = (FdsSysUserrolePK)other;
		return 
			this.iduser.equals(castOther.iduser)
			&& (this.idrole == castOther.idrole);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.iduser.hashCode();
		hash = hash * prime + ((int) (this.idrole ^ (this.idrole >>> 32)));
		
		return hash;
	}
}