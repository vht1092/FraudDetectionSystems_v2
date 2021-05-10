package com.fds.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysRoletxn;

@Repository
public interface SysRoleTxnRepo extends CrudRepository<FdsSysRoletxn, Long> {
	List<FdsSysRoletxn> findAllByIdrole(int roleid);	
}
