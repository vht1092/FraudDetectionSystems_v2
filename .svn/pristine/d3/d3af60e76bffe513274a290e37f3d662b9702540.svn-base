package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the FDS_SYS_USERROLE database table.
 * 
 */
@Entity
@Table(name="FDS_SYS_USERROLE")
@NamedQuery(name="FdsSysUserrole.findAll", query="SELECT f FROM FdsSysUserrole f")
public class FdsSysUserrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FdsSysUserrolePK id;

	public FdsSysUserrole() {
	}
	

	public FdsSysUserrole(FdsSysUserrolePK id) {
		super();
		this.id = id;
	}



	public FdsSysUserrolePK getId() {
		return this.id;
	}

	public void setId(FdsSysUserrolePK id) {
		this.id = id;
	}

}