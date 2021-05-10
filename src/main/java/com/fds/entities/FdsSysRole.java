package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="FDS_SYS_ROLE")
public class FdsSysRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	private boolean defaultrole;

	@Column(length=100)
	private String description;

	@Column(nullable=false, length=10)
	private String name;

	public FdsSysRole() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getDefaultrole() {
		return this.defaultrole;
	}

	public void setDefaultrole(boolean defaultrole) {
		this.defaultrole = defaultrole;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}