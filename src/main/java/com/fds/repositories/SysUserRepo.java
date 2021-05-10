package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysUser;

@Repository
public interface SysUserRepo extends JpaRepository<FdsSysUser, String> {
	FdsSysUser findByEmail(String email);

	List<FdsSysUser> findAllUserByActiveflagIsTrue();
}
