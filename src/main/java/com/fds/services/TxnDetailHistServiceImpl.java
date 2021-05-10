package com.fds.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsTxnDetailHist;
import com.fds.repositories.TxnDetailHistRepo;

@Service("txnDetailHistService")
public class TxnDetailHistServiceImpl implements TxnDetailHistService {

	@Autowired
	TxnDetailHistRepo txnDetailRepo;

	@Override
	public FdsTxnDetailHist findOneByF9Oa008CreTmsAndFxOa008UsedPan(BigDecimal cretms, String usedpan) {
		return txnDetailRepo.findOneByF9Oa008CreTmsAndFxOa008UsedPan(cretms, usedpan);
	}

	@Override
	public String findRefCdeByCreTmsAndUsedPan(BigDecimal cretms, String usedpan) {
		return txnDetailRepo.findRefCdeByCreTmsAndUsedPan(cretms, usedpan);
	}

	@Override
	public String findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(String pan, BigDecimal cretms) {		
		return txnDetailRepo.findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(pan, cretms);
	}
	
	@Override
	public String findEciValByCreTmsAndUsedPan(BigDecimal cretms, String usedpan) {
		return txnDetailRepo.findEciValByCreTmsAndUsedPan(cretms, usedpan);
	}

	@Override
	public String findTxnSamsungPay(String panEnc, BigDecimal cretms) {
		return txnDetailRepo.findTxnSamsungPay(panEnc, cretms);
	}

}
