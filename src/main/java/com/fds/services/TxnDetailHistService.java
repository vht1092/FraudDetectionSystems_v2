package com.fds.services;

import java.math.BigDecimal;

import com.fds.entities.FdsTxnDetailHist;

public interface TxnDetailHistService {

	public FdsTxnDetailHist findOneByF9Oa008CreTmsAndFxOa008UsedPan(BigDecimal cretms, String usedpan);

	public String findRefCdeByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);
	
	public String findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(String pan, BigDecimal cretms);
	
	public String findEciValByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);
	
	public String findTxnSamsungPay(String panEnc, BigDecimal cretms);
}
