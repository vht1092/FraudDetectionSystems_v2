package com.fds.services;

import java.util.List;

import com.fds.entities.FdsCaseStatusHist;

public interface CaseStatusHistService {
	void create(String caseno, String comment, String closedreason, String action, String other, String userid);
	List<FdsCaseStatusHist> findAllByCaseNo(String caseno);
}
