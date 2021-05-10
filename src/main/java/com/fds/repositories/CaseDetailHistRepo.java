package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsCaseDetailHist;

@Repository
public interface CaseDetailHistRepo extends JpaRepository<FdsCaseDetailHist, String>, CaseDetailHistRepoCustom {

	public static final String TABLE_NAME = "FDS_CASE_DETAIL_HIST";
	public static final String CLOSED_CASE_STATUS = "'DIC','CAF'";
	public static final String DESC_CASE_STATUS = "CASE STATUS";

	/**
	 * Lay danh sach cac case o trang thay moi chua tiep nhan
	 */
	// @Query(value = "select f from FdsCaseDetailHist f where f.checkNew = :ischecknew and f.creTms >= to_number(to_char(sysdate, 'yyyymmdd') || '000000000')")
	@Query(value = "select f from FdsCaseDetailHist f where f.checkNew = :ischecknew")
	Page<FdsCaseDetailHist> findAllBycheckNewIs(@Param(value = "ischecknew") String ischecknew, Pageable page);

	/**
	 * select '#pageable', to_char(to_date(fdscasedet.cre_tms,
	 * 'yyyyMMddHH24MISSSSS'), 'dd/mm/yyyy HH24:MI:SS') as cre_tms,
	 * to_char(to_date(fdscasedet.upd_tms, 'yyyyMMddHH24MISSSSS'), 'dd/mm/yyyy
	 * HH24:MI:SS') as upd_tms, fdscasedet.case_no as case_no, fdscasedet.amount
	 * as amount, ccps.ded2(fdscasedet.enc_crd_no, ' ') crd_no,
	 * fdscasedet.crncy_cde as crncy_cde, fdscasedet.txn_stat as
	 * txn_stat, fdscasedet.pos_mode as pos_mode, fdscasedet.txn_3d_ind as
	 * txn_3d_ind, (select listagg(r.rule_id, ',') within group(order by
	 * r.case_no) a from fds_case_hit_rules r where r.case_no
	 * = fdscasedet.case_no group by r.case_no) rule_id, fdscasedet.cif_no as
	 * cif_no, fdscasedet.crd_brn as crd_brn, (select r.rule_level from
	 * fds_rules r join fds_case_hit_rules h on
	 * h.rule_id = r.rule_id where h.case_no = fdscasedet.case_no and rownum = 1
	 * and r.rule_priority = (select min(r.rule_priority) from fds_rules r
	 * join fds_case_hit_rules h on h.rule_id =
	 * r.rule_id where h.case_no = fdscasedet.case_no)) color, (select
	 * nvl(f.objecttask, null) from fds_sys_task f where
	 * to_number(to_char(sysdate, 'yyyyMMddHH24MISSSSS')) between f.fromdate and
	 * f.todate and f.typetask = 'EXCEPTION' and f.objecttask =
	 * fdscasedet.cif_no) as task from FDS_CASE_DETAIL_HIST fdscasedet where
	 * fdscasedet.check_new = 'Y' order by fdscasedet.cre_tms desc
	 */
	// @Query(value = "select /*'#pageable',*/ fdscasedet.cre_tms,
	// fdscasedet.upd_tms, fdscasedet.upd_uid, fdscasedet.asg_tms,
	// fdscasedet.init_usr_id, fdscasedet.usr_id, fdscasedet.cif_no as cif_no,
	// fdscasedet.case_no as case_no, fdscasedet.amount as amount,
	// fdscasedet.avl_bal, fdscasedet.avl_bal_crncy, fdscasedet.case_status,
	// fdscasedet.check_new, fdscasedet.sms_flg,
	// ccps.ded2(fdscasedet.enc_crd_no, ' ') enc_crd_no, fdscasedet.mcc,
	// fdscasedet.crd_brn as crd_brn, fdscasedet.merc_name, fdscasedet.crncy_cde
	// as crncy_cde, fdscasedet.pos_mode as pos_mode,
	// fdscasedet.resp_cde, fdscasedet.txn_stat as txn_stat,
	// fdscasedet.txn_3d_ind as txn_3d_ind, fdscasedet.txn_3d_eci, (select
	// listagg(r.rule_id, ',') within group(order by r.case_no) a from
	// fds_case_hit_rules r where r.case_no = fdscasedet.case_no group by
	// r.case_no) rule_id, (select r.rule_level from fds_rules r join
	// fds_case_hit_rules h on h.rule_id = r.rule_id
	// where h.case_no = fdscasedet.case_no and rownum = 1 and r.rule_priority =
	// (select min(r.rule_priority) from fds_rules r join
	// fds_case_hit_rules h on h.rule_id = r.rule_id where
	// h.case_no = fdscasedet.case_no)) color from FDS_CASE_DETAIL_HIST
	// fdscasedet where fdscasedet.check_new = ?1 order by fdscasedet.cre_tms
	// desc", countQuery = "select count(fdscasedet.case_no)
	// from {h-schema}FDS_CASE_DETAIL_HIST fdscasedet where fdscasedet.check_new =
	// ?1", nativeQuery = true)
	// Page<FdsCaseDetailHist> findAllBycheckNewIs(String query,Pageable page);

	/**
	 * Count danh sach cac case o trang thay moi chua tiep nhan
	 */
	@Query(value = "select count(t.case_no) from {h-schema}" + TABLE_NAME + " t where lower(t.check_new) = lower('Y')", nativeQuery = true)
	int countBycheckNewIs();

	/**
	 * Count danh sach cac case theo user va trang thai case
	 * 
	 * @param
	 * @return
	 */
	int countByUsrIIgnoreCasedAndCaseStatusIn(String userid, String[] casestatus);

	/**
	 * Count danh sach cac case theo trang thai
	 * 
	 * @param
	 * @return int
	 */
	int countByCaseStatusIn(String[] casestatus);

	/**
	 * Count danh sach cac case trong 45 ngay dung cho man hinh thong ke chung
	 * 
	 * @return int
	 */
	@Query(value = "select count(a.cre_tms) from  {h-schema}" + TABLE_NAME + " a  where a.case_status in (" + CLOSED_CASE_STATUS
			+ ") and a.cre_tms >= (select to_number(to_char(sysdate - 45, 'yyyyMMddHH24MISSSSS')) from dual)", nativeQuery = true)
	int countByCaseStatusInAnd45Day();

	/**
	 * Lay danh sach case theo user id va trang thai case
	 * 
	 * @param
	 * @return Page<FdsCaseDetailHist>
	 * @see Inbox
	 */
	Page<FdsCaseDetailHist> findByUsrIIgnoreCasedAndCaseStatusIn(String userid, String[] casestatus, Pageable page);
	
	Page<FdsCaseDetailHist> findByCaseStatusIn(String[] casestatus, Pageable page);
	
	Page<FdsCaseDetailHist> findByUsrIIgnoreCasedAndCaseStatusInAndAutoClose(String userid, String[] casestatus, String autoClose, Pageable page);
	
	Page<FdsCaseDetailHist> findByCaseStatusInAndAutoClose(String[] casestatus, String autoClose, Pageable page);
	/**
	 * Lay danh sach case theo list of user id va trang thai case
	 * 
	 * @param
	 * @return Page<FdsCaseDetailHist>
	 * @see ClosedCase
	 */
	@Query(value = "select f from FdsCaseDetailHist f where f.caseStatus in (:casestatus) and f.usrId in (:listofuserid)")
	Page<FdsCaseDetailHist> findByUsrListAndCaseStatus(@Param(value = "listofuserid") String[] listofuserid,@Param(value = "casestatus") String[] casestatus, Pageable page);

	/**
	 * Lay danh sach case theo trang thai
	 * 
	 * @param status
	 *            Trang thai case
	 * @param page
	 *            PageRequest
	 * @return Page<FdsCaseDetailHist>
	 */
	Page<FdsCaseDetailHist> findAllBycaseStatus(@Param("status") String status, Pageable page);

	/**
	 * Thong tin case
	 * 
	 * @param caseno
	 *            So case
	 * @return FdsCaseDetailHist
	 */
	FdsCaseDetailHist findOneByCaseNo(@Param(value = "caseno") String caseno);

	/**
	 * Thong tin chi tiet case voi so tien da duoc chuyen doi sang VND + phi
	 * 
	 * @param caseno
	 *            So case
	 * @return Object
	 */
	// @Query(value = "select casedet.case_no, (select
	// sum(txndet.f9_oa008_amt_req + txndet.f9_oa008_load_fee) from
	// {h-schema}fds_txn_detail_hist txndet where txndet.f9_oa008_cre_tms =
	// casedet.txn_cre_tms and txndet.px_oa008_pan = casedet.enc_crd_no) as
	// amount, casedet.asg_tms as asg_tms, casedet.avl_bal as avl_bal,
	// casedet.avl_bal_crncy as avl_bal_crncy, casedet.case_status,
	// casedet.check_new as check_new, casedet.cif_no, casedet.crd_brn as
	// crd_brn, casedet.cre_tms, casedet.crncy_cde, casedet.enc_crd_no,
	// casedet.init_asg_tms as init_asg_tms, casedet.init_usr_id as init_usr_id,
	// casedet.mcc, casedet.merc_name, casedet.pos_mode as pos_mode,
	// casedet.resp_cde as resp_cde, casedet.sms_flg as sms_flg,
	// casedet.txn_3d_eci as txn_3d_eci, casedet.txn_3d_ind as txn_3d_ind,
	// casedet.txn_cre_tms, casedet.txn_stat as txn_stat, casedet.upd_tms as
	// upd_tms, casedet.upd_uid as upd_uid, casedet.usr_id, casedet.loc as loc
	// from {h-schema}FDS_CASE_DETAIL_HIST casedet where casedet.case_no = :caseno",
	// nativeQuery = true)
	// FdsCaseDetailHist findCaseDetailByCaseno(@Param(value = "caseno") String
	// caseno);

	// @Query(value = "select det.amount, det.case_no, det.case_status, det.cif_no, det.cre_tms, det.enc_crd_no, det.mcc, det.merc_name, det.usr_id, (txndet.f9_oa008_amt_req +
	// txndet.f9_oa008_load_fee) as amt_req, det.crncy_cde, det.txn_cre_tms, decode(det.crd_brn, 'MC', (select trim(fx_oa274_mc_resp_cde) from oa274@im.world where
	// substr(replace(trim(txndet.fx_oa008_ref_cde), ' ', ' '), 1, 2) = trim(px_oa274_ref_cde) and fx_oa274_mc_resp_cde = det.resp_cde), (select trim(fx_oa274_vs_resp_cde) from oa274@im.world where
	// substr(replace(trim(txndet.fx_oa008_ref_cde), ' ', ' '), 1, 2) = trim(px_oa274_ref_cde) and fx_oa274_vs_resp_cde = det.resp_cde)) resp_cde, decode(det.crd_brn, 'MC', (select det.crd_brn || ' -
	// ' || trim(fx_oa274_ref_cde_desc) from oa274@im.world where substr(replace(trim(txndet.fx_oa008_ref_cde), ' ', ' '), 1, 2) = trim(px_oa274_ref_cde) and fx_oa274_mc_resp_cde = det.resp_cde),
	// (select det.crd_brn || ' - ' || trim(fx_oa274_ref_cde_desc) from oa274@im.world where substr(replace(trim(txndet.fx_oa008_ref_cde), ' ', ' '), 1, 2) = trim(px_oa274_ref_cde) and
	// fx_oa274_vs_resp_cde = det.resp_cde)) ref_cde_desc from {h-schema}FDS_CASE_DETAIL_HIST det join {h-schema}fds_txn_detail_hist txndet on det.txn_cre_tms = txndet.f9_oa008_cre_tms and det.enc_crd_no =
	// txndet.fx_oa008_used_pan and det.case_no = :caseno", nativeQuery = true)
	@Query(value = "select det.amount, det.case_no, det.case_status, det.cif_no, det.cre_tms, det.enc_crd_no, det.mcc, det.merc_name, det.usr_id, (txndet.f9_oa008_amt_req + txndet.f9_oa008_load_fee) as amt_req, det.crncy_cde, det.txn_cre_tms, substr(trim(txndet.fx_oa008_ref_cde),1,2) ref_cde,RESP_CDE,TXN_3D_IND,F9_OA008_CHRG_AMT,F9_OA008_ECI_SEC_LVL from {h-schema}FDS_CASE_DETAIL_HIST det join {h-schema}fds_txn_detail_hist txndet on det.txn_cre_tms = txndet.f9_oa008_cre_tms and det.enc_crd_no = txndet.fx_oa008_used_pan and det.case_no = :caseno ", nativeQuery = true)
	List<Object[]> findCaseDetailByCaseno(@Param(value = "caseno") String caseno);

	@Query(value = "select count(t.case_no) from FDS_CASE_DETAIL_HIST t where trim(t.usr_id)!=' '", nativeQuery = true)
	int countAllUserAssigned();

	/**
	 * 
	 * @param userid
	 * @return
	 */
	@Query(value = "select IDROLE from fds_sys_userrole where IDUSER = :userid and rownum <= 1", nativeQuery = true)
	String findByRoleID(@Param(value = "userid") String userid);
	
	/**
	 * Lay thoi gian hien tai theo thoi gian cua database
	 * 
	 * @return BigDecimal
	 */
	@Query(value = "select to_number(to_char(SYSDATE, 'yyyyMMddHH24MISSSSS')) from dual", nativeQuery = true)
	BigDecimal getCurrentTime();

	/**
	 * Giai ma ma so the dung ham ccps.ded2 trong database
	 * 
	 * @param cardno
	 *            So the da ma hoa
	 * @return String
	 */
	//tanvh1
//	@Query(value = "select ccps.ded2A@im(:cardno,'FDS') from dual", nativeQuery = true)
	@Query(value = "select ccps.ded2(:cardno,'FDS') from dual", nativeQuery = true)
	String getDed2(@Param(value = "cardno") String cardno);

	//@formatter:off	
	/*
		  select distinct *
			  from (select d.case_no,
			               to_char(to_date(substr(t1.txn_cre_tms, 0, 14),
			                               'yyyyMMddHH24MISS'),
			                       'dd/mm/yyyy HH24:MI:SS') as cre_tms,
			               td.f9_oa008_amt_req as amount,
			               d.usr_id,
			               (select t2.case_comment
			                  from fds_case_status_hist t2
			                 where t2.cre_tms =
			                       (select max(t.cre_tms)
			                          from fds_case_status_hist t
			                         where t.case_no = d.case_no)) as case_comment,
			               des.description
			          from fds_case_hit_rule_detail_hist t1
			          left join FDS_CASE_DETAIL_HIST d
			            on t1.txn_cre_tms = d.txn_cre_tms
			          join fds_txn_detail_hist td
			            on t1.txn_cre_tms = td.f9_oa008_cre_tms
			           and t1.txn_enc_crd_no = td.px_oa008_pan
			          left join fds_description des
			            on des.id = d.case_status
			           and des.type = 'CASE STATUS'
			         where t1.case_no = :caseno) m
			 order by cre_tms desc 
	 */		
	/**
	 * Lay danh sach cac giao dich co lien qua theo case	 
	 * @param caseno So case
	 * @return List
	 * */
	//@Query(value ="select d.case_no, to_char(to_date(substr(td.f9_oa008_cre_tms, 0, 14),'yyyyMMddHH24MISS'),'dd/mm/yyyy HH24:MI:SS') as cre_tms, d.usr_id, (select t2.case_comment from {h-schema}fds_case_status_hist t2 where t2.cre_tms = (select max(t.cre_tms) from {h-schema}fds_case_status_hist t where t.case_no = d.case_no) and t2.case_no = d.case_no) as case_comment, des.description, td.fx_oa008_pos_mode pos_mode, td.f9_oa008_mcc mcc, td.f9_oa008_ori_amt amount, td.f9_oa008_crncy_cde crncy_cde, (td.f9_oa008_amt_req + td.f9_oa008_load_fee) amt_req, (select listagg(r.rule_id, ',') within group(order by r.case_no) a from {h-schema}fds_case_hit_rule_detail_hist r where r.txn_cre_tms = td.f9_oa008_cre_tms and r.txn_enc_crd_no = td.fx_oa008_used_pan and r.case_no = d.case_no group by r.txn_cre_tms) ruleid from {h-schema}fds_txn_detail_hist td left join {h-schema}FDS_CASE_DETAIL_HIST d on fx_oa008_used_pan = enc_crd_no and f9_oa008_cre_tms = txn_cre_tms left join {h-schema}fds_description des on des.id = d.case_status and des.type = '"+DESC_CASE_STATUS+"' where fx_oa008_used_pan = :enccrdno  and td.f9_oa008_amt_req >= 0 order by td.f9_oa008_cre_tms desc",nativeQuery = true)
	//List<Object[]> findTransactionByCase(@Param(value = "enccrdno") String enccrdno);
	@Query(value ="select fdsCaseDetailHist.case_no, to_char(to_date(substr(e.f9_oa008_cre_tms, 0, 14), 'yyyyMMddHH24MISS'), 'dd/mm/yyyy HH24:MI:SS') as cre_tms, fdsCaseDetailHist.usr_id, (select t2.case_comment from {h-schema}fds_case_status_hist t2 where t2.cre_tms = (select max(t.cre_tms) from {h-schema}fds_case_status_hist t where t.case_no = fdsCaseDetailHist.case_no) and t2.case_no = fdsCaseDetailHist.case_no and rownum = 1) as case_comment, fdsdescription.description, e.fx_oa008_pos_mode pos_mode, e.f9_oa008_mcc mcc, e.f9_oa008_ori_amt amount, e.f9_oa008_crncy_cde crncy_cde, (e.f9_oa008_amt_req + e.f9_oa008_load_fee) amt_req, (select listagg(r.rule_id, ',') within group(order by r.case_no) a from {h-schema}fds_case_hit_rule_detail_hist r where r.txn_cre_tms = e.f9_oa008_cre_tms and r.txn_enc_crd_no = e.fx_oa008_used_pan and r.case_no = fdsCaseDetailHist.case_no group by r.txn_cre_tms) ruleid, substr(trim(e.fx_oa008_ref_cde),1,2) ref_cde, DECODE(e.fx_oa008_given_resp_cde,'00','00',DECODE(e.FX_OA008_CRD_BRN, 'MC',(SELECT FX_OA274_MC_RESP_CDE FROM OA274@IM WHERE TRIM(PX_OA274_REF_CDE) = SUBSTR(TRIM(e.FX_OA008_REF_CDE), 1, 2)), (SELECT FX_OA274_VS_RESP_CDE FROM OA274@IM WHERE TRIM(PX_OA274_REF_CDE) = SUBSTR(TRIM(e.FX_OA008_REF_CDE), 1, 2)))) AS RESP_CDE, NVL((SELECT FX_OA126_3D_IND FROM OA126@IM WHERE PX_OA126_PAN = e.fx_oa008_used_pan AND P9_OA126_SEQ_NUM = e.p9_oa008_seq AND substr(F9_OA126_UPD_TMS,1,8) = e.f9_oa008_dt),'N') as TXN_3D_IND,e.F9_OA008_CHRG_AMT,e.fx_oa008_merc_st_cntry,e.F9_OA008_ECI_SEC_LVL from (select fdstxndetail.fx_oa008_pos_mode, fdstxndetail.f9_oa008_mcc, fdstxndetail.f9_oa008_ori_amt, fdstxndetail.f9_oa008_crncy_cde, fdstxndetail.f9_oa008_amt_req, fdstxndetail.f9_oa008_load_fee, fdstxndetail.f9_oa008_cre_tms, fdstxndetail.fx_oa008_used_pan,fdstxndetail.fx_oa008_ref_cde,fdstxndetail.F9_OA008_CHRG_AMT,fdstxndetail.fx_oa008_merc_st_cntry,fdstxndetail.FX_OA008_CRD_BRN,fdstxndetail.p9_oa008_seq,fdstxndetail.fx_oa008_given_resp_cde,fdstxndetail.F9_OA008_ECI_SEC_LVL,f9_oa008_dt from {h-schema}fds_txn_detail_hist fdstxndetail where fdstxndetail.f9_oa008_dt between to_number(to_char(add_months(sysdate,-:numberofmonth),'YYYYMMDD')) and to_number(to_char(sysdate,'YYYYMMDD')) and fdstxndetail.fx_oa008_used_pan = :enccrdno and fdstxndetail.f9_oa008_amt_req >= 0) e left join {h-schema}FDS_CASE_DETAIL_HIST fdsCaseDetailHist on e.fx_oa008_used_pan = fdsCaseDetailHist.enc_crd_no and e.f9_oa008_cre_tms = fdsCaseDetailHist.txn_cre_tms left join {h-schema}fds_description fdsdescription on fdsdescription.id = fdsCaseDetailHist.case_status and fdsdescription.type = '"+DESC_CASE_STATUS+"' order by e.f9_oa008_cre_tms desc ",nativeQuery = true)
	List<Object[]> findTransactionByCase(@Param(value = "enccrdno") String enccrdno, @Param(value = "numberofmonth") int numberofmonth);
	
	@Query(value ="select fdsCaseDetailHist.case_no, to_char(to_date(substr(e.f9_oa008_cre_tms, 0, 14), 'yyyyMMddHH24MISS'), 'dd/mm/yyyy HH24:MI:SS') as cre_tms, fdsCaseDetailHist.usr_id, (select t2.case_comment from {h-schema}fds_case_status_hist t2 where t2.cre_tms = (select max(t.cre_tms) from {h-schema}fds_case_status_hist t where t.case_no = fdsCaseDetailHist.case_no) and t2.case_no = fdsCaseDetailHist.case_no and rownum = 1) as case_comment, fdsdescription.description, e.fx_oa008_pos_mode pos_mode, e.f9_oa008_mcc mcc, e.f9_oa008_ori_amt amount, e.f9_oa008_crncy_cde crncy_cde, (e.f9_oa008_amt_req + e.f9_oa008_load_fee) amt_req, (select listagg(r.rule_id, ',') within group(order by r.case_no) a from {h-schema}fds_case_hit_rule_detail_hist r where r.txn_cre_tms = e.f9_oa008_cre_tms and r.txn_enc_crd_no = e.fx_oa008_used_pan and r.case_no = fdsCaseDetailHist.case_no group by r.txn_cre_tms) ruleid, substr(trim(e.fx_oa008_ref_cde),1,2) ref_cde, DECODE(e.fx_oa008_given_resp_cde,'00','00',DECODE(e.FX_OA008_CRD_BRN, 'MC',(SELECT FX_OA274_MC_RESP_CDE FROM OA274@IM WHERE TRIM(PX_OA274_REF_CDE) = SUBSTR(TRIM(e.FX_OA008_REF_CDE), 1, 2)), (SELECT FX_OA274_VS_RESP_CDE FROM OA274@IM WHERE TRIM(PX_OA274_REF_CDE) = SUBSTR(TRIM(e.FX_OA008_REF_CDE), 1, 2)))) AS RESP_CDE, NVL((SELECT FX_OA126_3D_IND FROM OA126@IM WHERE PX_OA126_PAN = e.fx_oa008_used_pan AND P9_OA126_SEQ_NUM = e.p9_oa008_seq AND substr(F9_OA126_UPD_TMS,1,8) = e.f9_oa008_dt),'N') as TXN_3D_IND,e.F9_OA008_CHRG_AMT,e.fx_oa008_merc_st_cntry,e.F9_OA008_ECI_SEC_LVL from (select fdstxndetail.fx_oa008_pos_mode, fdstxndetail.f9_oa008_mcc, fdstxndetail.f9_oa008_ori_amt, fdstxndetail.f9_oa008_crncy_cde, fdstxndetail.f9_oa008_amt_req, fdstxndetail.f9_oa008_load_fee, fdstxndetail.f9_oa008_cre_tms, fdstxndetail.fx_oa008_used_pan,fdstxndetail.fx_oa008_ref_cde,fdstxndetail.F9_OA008_CHRG_AMT,fdstxndetail.fx_oa008_merc_st_cntry,fdstxndetail.FX_OA008_CRD_BRN,fdstxndetail.p9_oa008_seq,fdstxndetail.fx_oa008_given_resp_cde,fdstxndetail.F9_OA008_ECI_SEC_LVL,f9_oa008_dt from {h-schema}fds_txn_detail_hist fdstxndetail where fdstxndetail.fx_oa008_used_pan = :enccrdno and fdstxndetail.f9_oa008_amt_req >= 0) e left join {h-schema}FDS_CASE_DETAIL_HIST fdsCaseDetailHist on e.fx_oa008_used_pan = fdsCaseDetailHist.enc_crd_no and e.f9_oa008_cre_tms = fdsCaseDetailHist.txn_cre_tms left join {h-schema}fds_description fdsdescription on fdsdescription.id = fdsCaseDetailHist.case_status and fdsdescription.type = '"+DESC_CASE_STATUS+"' order by e.f9_oa008_cre_tms desc ",nativeQuery = true)
	List<Object[]> findAllTransactionByCase(@Param(value = "enccrdno") String enccrdno);
	
	
	//@formatter:on
	@Modifying
	@Query(value = "update FdsCaseDetailHist f set f.caseStatus=:status where f.caseNo=:caseno ")
	void updateStatusCase(@Param(value = "caseno") String caseno, @Param(value = "status") String status);
	
	
	
	@Query(value ="select Temp_Lock_Card(:encPan,:desc,:userPeform) from dual ",nativeQuery = true)
	int tempLockCardRepo(@Param("encPan") String encPan, @Param("desc") String desc, @Param("userPeform") String userPeform);
	
	@Query(value ="SELECT CRE_TMS, UPD_TMS, UPD_UID, ASG_TMS, INIT_USR_ID, :userRecord USR_ID, CIF_NO, " + 
			"CASE_NO, AMOUNT, AVL_BAL, AVL_BAL_CRNCY, CASE_STATUS, CHECK_NEW, SMS_FLG, " + 
			"ENC_CRD_NO, MCC, INIT_ASG_TMS, TXN_CRE_TMS, CRD_BRN, MERC_NAME, CRNCY_CDE, " + 
			"POS_MODE, RESP_CDE, TXN_STAT, TXN_3D_IND, TXN_3D_ECI, LOC, ACC_AVL_BAL, " + 
			"CAV_AVL_LMT, CRN_LMT_AVL, CRN, AUTOCLOSE " + 
			"FROM FDS_CASE_DETAIL_HIST A INNER JOIN FDS_SYS_TASK B ON A.CIF_NO = B.OBJECTTASK " + 
			"INNER JOIN FDS_DESCRIPTION C ON C.TYPE='MERC' and regexp_like (C.ID,REPLACE(B.MERCHANT,'-','|')) " + 
			"WHERE CHECK_NEW='Y' AND A.MERC_NAME LIKE '%' || C.DESCRIPTION || '%' AND B.MERCHANT IS NOT NULL " + 
			"AND regexp_like (SUBSTR(A.POS_MODE,1,2),REPLACE(B.POSMODE,'-','|'))",nativeQuery = true)
	public List<FdsCaseDetailHist> findCaseByMerchantSystask(@Param("userRecord") String userRecord);
}
