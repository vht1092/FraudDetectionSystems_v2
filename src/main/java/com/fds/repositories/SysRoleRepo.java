package com.fds.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysRole;

@Repository
public interface SysRoleRepo extends CrudRepository<FdsSysRole, Integer> {
	List<FdsSysRole> findAllByDefaultroleIsTrue();
	List<FdsSysRole> findAll();
}
