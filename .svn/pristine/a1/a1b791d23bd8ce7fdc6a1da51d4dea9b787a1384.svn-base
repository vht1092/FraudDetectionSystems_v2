package com.fds.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.TimeConverter;
import com.fds.entities.FdsSysRole;
import com.fds.entities.FdsSysUser;
import com.fds.entities.FdsSysUserrole;
import com.fds.entities.FdsSysUserrolePK;
import com.fds.repositories.SysRoleRepo;
import com.fds.repositories.SysUserRepo;
import com.fds.repositories.SysUserroleRepo;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserRepo sysUserRepo;

	@Autowired
	private SysRoleRepo roleRepository;
	@Autowired
	private SysUserroleRepo userroleRepo;
	private final TimeConverter timeConverter = new TimeConverter();

	@Override
	public FdsSysUser findAllByEmail(String email) {
		return sysUserRepo.findByEmail(email);
	}

	@Override
	public FdsSysUser findByUserid(String userid) {
		return sysUserRepo.findOne(userid);
	}

	@Override
	public String createNew(String userid, String email, String fullname) {
		FdsSysUser sysUser = new FdsSysUser();
		sysUser.setUserid(userid);
		sysUser.setActiveflag(true);// Mac dinh duoc active
		sysUser.setEmail(email);
		sysUser.setFullname(fullname);
		sysUser.setUsertype("OFF");// User mac dinh la officer
		sysUser.setCreatedate(timeConverter.getCurrentTime());
		sysUser.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(sysUser);
		// Gan role mac dinh
		List<FdsSysRole> listDefaultRole = roleRepository.findAllByDefaultroleIsTrue();

		for (FdsSysRole list : listDefaultRole) {
			FdsSysUserrolePK id = new FdsSysUserrolePK();
			id.setIdrole(list.getId());
			id.setIduser(sysUser.getUserid());
			userroleRepo.save(new FdsSysUserrole(id));
		}
		return sysUser.getUserid();
	}

	@Override
	public void updateLastLogin(String userid) {
		FdsSysUser user = sysUserRepo.findOne(userid);
		user.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(user);
	}

	@Override
	public List<FdsSysUser> findAllUser() {
		return sysUserRepo.findAll();
	}

	@Override
	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active) {
		FdsSysUser fdsSysUser = sysUserRepo.findOne(userid);
		if (fdsSysUser == null) {
			fdsSysUser = new FdsSysUser();
			fdsSysUser.setUserid(userid);
		}
		fdsSysUser.setFullname(fullname);
		fdsSysUser.setUsertype(usertype);
		fdsSysUser.setActiveflag(active);
		fdsSysUser.setUpdatedate(timeConverter.getCurrentTime());
		sysUserRepo.save(fdsSysUser);

	}

	@Override
	public List<FdsSysUser> findAllUserByActiveflagIsTrue() {
		return sysUserRepo.findAllUserByActiveflagIsTrue();
	}

}
