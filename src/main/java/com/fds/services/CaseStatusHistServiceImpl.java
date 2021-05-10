package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsCaseStatusHist;
import com.fds.repositories.CaseStatusHistRepo;

@Service("caseStatusHistService")
public class CaseStatusHistServiceImpl implements CaseStatusHistService {

	@Autowired
	private CaseStatusHistRepo caseStatusRepo;

	@Override
	public void create(String caseno, String comment, String closedreason, String action, String other, String userid) {

		FdsCaseStatusHist fdsCaseStatus = new FdsCaseStatusHist();
		fdsCaseStatus.setCaseNo(caseno);
		fdsCaseStatus.setClosedReason(closedreason);
		fdsCaseStatus.setCaseComment(comment);
		fdsCaseStatus.setOther(other.toUpperCase());
		fdsCaseStatus.setCreTms(caseStatusRepo.getCurrentTime());
		fdsCaseStatus.setUserId(userid.toUpperCase());
		fdsCaseStatus.setCaseAction(action);
		caseStatusRepo.save(fdsCaseStatus);
	}

	@Override
	public List<FdsCaseStatusHist> findAllByCaseNo(String caseno) {
		return caseStatusRepo.findAllByCaseNo(caseno, new PageRequest(0, 20, Sort.Direction.DESC, "creTms"));
	}
}