package com.fds.services;

import java.util.List;

import com.fds.entities.FdsSysUser;

public interface SysUserService {
	public FdsSysUser findAllByEmail(String email);

	public List<FdsSysUser> findAllUser();
	
	public List<FdsSysUser> findAllUserByActiveflagIsTrue();

	public String createNew(String userid, String email, String fullname);

	public void updateLastLogin(String userid);

	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active);

	public FdsSysUser findByUserid(String userid);
}
