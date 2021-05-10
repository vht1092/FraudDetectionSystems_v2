package com.fds.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fds.entities.FdsTxnDetailHist;
import com.fds.entities.FdsTxnDetailPK;

public interface TxnDetailHistRepo extends CrudRepository<FdsTxnDetailHist, FdsTxnDetailPK> {

	public FdsTxnDetailHist findOneByF9Oa008CreTmsAndFxOa008UsedPan(BigDecimal cretms, String usedpan);

	@Query(nativeQuery = true, value = "select substr(trim(fdstxndeta.fx_oa008_ref_cde), 1, 2) as ref_cde from {h-schema}fds_txn_detail_hist fdstxndeta  where fdstxndeta.f9_oa008_cre_tms = ?1 and fdstxndeta.fx_oa008_used_pan = ?2")
	public String findRefCdeByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);

	@Query(nativeQuery = true, value = "select trim(fdstxndeta0_.fx_oa008_merc_st_cntry) cntry_cde from fds_txn_detail_hist fdstxndeta0_ where fdstxndeta0_.fx_oa008_used_pan = ?1 and fdstxndeta0_.f9_oa008_cre_tms = ?2")
	public String findOneFxOa008CntryCdeByFxOa008UsedPanAndF9Oa008CreTms(String pan, BigDecimal cretms);
	
	@Query(nativeQuery = true, value = "select trim(F9_OA008_ECI_SEC_LVL) as eci_val from {h-schema}fds_txn_detail_hist fdstxndeta  where fdstxndeta.f9_oa008_cre_tms = ?1 and fdstxndeta.fx_oa008_used_pan = ?2")
	public String findEciValByCreTmsAndUsedPan(BigDecimal cretms, String usedpan);
	
	@Query(nativeQuery = true, value = "select FX_SSPTXN_TOKEN from {h-schema}fds_txn_detail_hist inner join DW_SSP_TXN on FX_OA008_USED_PAN = FX_SSPTXN_PAN and P9_OA008_SEQ = F9_SSPTXN_SEQ where FX_OA008_USED_PAN = ?1 and F9_OA008_CRE_TMS = ?2")
	public String findTxnSamsungPay(String panEnc, BigDecimal cretms);

}
