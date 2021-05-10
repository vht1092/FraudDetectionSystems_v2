package com.fds.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class CustomerInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String cust_name;
	private String cust_gendr;
	private String cust_hp;
	private String cust_off_tel_1;
	private String cust_off_tel_2;
	private String cust_cif;
	@Id
	private String cust_email_addr;

	public String getCust_name() {
		return cust_name;
	}
	public String getCust_gendr() {
		return cust_gendr;
	}
	public String getCust_hp() {
		return cust_hp;
	}
	public String getCust_off_tel_1() {
		return cust_off_tel_1;
	}
	public String getCust_off_tel_2() {
		return cust_off_tel_2;
	}
	@Id
	public String getCust_email_addr() {
		return cust_email_addr;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public void setCust_gendr(String cust_gendr) {
		this.cust_gendr = cust_gendr;
	}
	public void setCust_hp(String cust_hp) {
		this.cust_hp = cust_hp;
	}
	public void setCust_off_tel_1(String cust_off_tel_1) {
		this.cust_off_tel_1 = cust_off_tel_1;
	}
	public void setCust_off_tel_2(String cust_off_tel_2) {
		this.cust_off_tel_2 = cust_off_tel_2;
	}
	public void setCust_email_addr(String cust_email_addr) {
		this.cust_email_addr = cust_email_addr;
	}
	public String getCust_cif() {
		return cust_cif;
	}
	public void setCust_cif(String cust_cif) {
		this.cust_cif = cust_cif;
	}

}
