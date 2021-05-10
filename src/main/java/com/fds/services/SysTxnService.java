package com.fds.services;

import java.util.List;

import com.fds.entities.FdsSysTxn;

public interface SysTxnService {
	List<Object[]> findAllByUserId(String id);
	List<Object[]> findAllByRoleId(int id);
	List<FdsSysTxn> findAll();
}
