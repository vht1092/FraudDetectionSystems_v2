package com.fds.repositories;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fds.entities.FdsCaseDetail;

public class CaseDetailRepoImpl implements CaseDetailRepoCustom {
	@PersistenceContext
	private EntityManager em;
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String SCHEMA;
	// private final static String CLOSEDCASE = "'DIC', 'CAF'";

	@SuppressWarnings("unchecked")
	@Override
	public Page<FdsCaseDetail> searchCase(final String sQuery, final String fromdate, final String todate) {
		final StringBuilder sbQuery = new StringBuilder(
				"select fdscasedetail.cre_tms, fdscasedetail.upd_tms, fdscasedetail.upd_uid, fdscasedetail.asg_tms, fdscasedetail.init_usr_id, fdscasedetail.usr_id, fdscasedetail.cif_no, fdscasedetail.case_no, fdscasedetail.amount, fdscasedetail.avl_bal, fdscasedetail.avl_bal_crncy, fdscasedetail.case_status, fdscasedetail.check_new, fdscasedetail.sms_flg, fdscasedetail.enc_crd_no, fdscasedetail.mcc, fdscasedetail.init_asg_tms, fdscasedetail.txn_cre_tms, fdscasedetail.crd_brn, fdscasedetail.merc_name, fdscasedetail.crncy_cde, fdscasedetail.pos_mode, fdscasedetail.resp_cde, fdscasedetail.txn_stat, fdscasedetail.txn_3d_ind, fdscasedetail.txn_3d_eci, fdscasedetail.loc, fdscasedetail.autoclose from "
						+ SCHEMA + ".fds_case_detail fdscasedetail join " + SCHEMA
						+ ".fds_txn_detail fdstxndetail on fdscasedetail.txn_cre_tms = fdstxndetail.f9_oa008_cre_tms and fdscasedetail.enc_crd_no = fdstxndetail.fx_oa008_used_pan where 1 = 1"
						+ sQuery);
		sbQuery.append(" order by fdscasedetail.cre_tms");
		final Query query = em.createNativeQuery(sbQuery.toString(), FdsCaseDetail.class);
		return new PageImpl<FdsCaseDetail>(query.getResultList());
	}

	// REPORT
	/** Bao cao tinh trang case, tac dong case theo user */
	@SuppressWarnings("unchecked")
	public List<Object[]> reportCaseByUser(final String fromdate, final String todate, final String userid, final String closedReason,
			final String crdbrn) {
		// @formatter:off
		//final StringBuilder sQuery = new StringBuilder("select t.case_no, to_char(to_date(t.cre_tms, 'yyyyMMddHH24MISSSSS'), 'dd/mm/yyyy HH24:MI:SS') as cre_tms, (select des1.description from " + SCHEMA + ".fds_description des1 where des1.type = 'ACTION' and des1.id = t.closed_reason) closed_reason, (select des2.description from " + SCHEMA + ".fds_description des2 where des2.type = 'CASE STATUS' and des2.id = d.case_status) case_status, (select px_irpanmap_panmask from ir_pan_map@im.world p where p.px_irpanmap_pan = d.enc_crd_no), decode((select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select f9_ir025_loc_acct from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) loc, d.crd_brn, decode((select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) custname, (select listagg(r.rule_id, ', ') within group(order by r.case_no) a from " + SCHEMA + ".fds_case_hit_rules r where r.case_no = d.case_no group by r.case_no), t.user_id from " + SCHEMA + ".fds_case_status t join " + SCHEMA + ".fds_case_detail d on t.case_no = d.case_no join " + SCHEMA + ".fds_txn_detail fdstxndetail on d.txn_cre_tms = fdstxndetail.f9_oa008_cre_tms and d.enc_crd_no = fdstxndetail.fx_oa008_used_pan left join " + SCHEMA + ".fds_description des on t.closed_reason = des.id where 1 = 1");
		final StringBuilder sQuery = new StringBuilder("select t.case_no, substr(t.cre_tms,7,2)||'/'||substr(t.cre_tms,5,2)||'/'||substr(t.cre_tms,1,4)||' '||substr(t.cre_tms,9,2)||':'||substr(t.cre_tms,11,2)||':'||case when substr(t.cre_tms,13,2)>'60' then '60' else substr(t.cre_tms,13,2) as cre_tms, t.case_comment closed_reason, (select des2.description from " + SCHEMA + ".fds_description des2 where des2.type = 'CASE STATUS' and des2.id = d.case_status) case_status, (select px_irpanmap_panmask from ir_pan_map@im.world p where p.px_irpanmap_pan = d.enc_crd_no), nvl(f9_dw005_loc_acct, f9_dw006_loc_acct) loc, d.crd_brn, nvl(trim(fx_dw005_emb_lst_nm), trim(fx_dw006_emb_lst_nm)) || ' ' || nvl(trim(fx_dw005_emb_mid_nm), trim(fx_dw006_emb_mid_nm)) || ' ' || nvl(trim(fx_dw005_emb_name), trim(fx_dw006_emb_name)) custname, (select listagg(r.rule_id, ', ') within group(order by r.case_no) a from " + SCHEMA + ".fds_case_hit_rules r where r.case_no = d.case_no group by r.case_no), t.user_id from " + SCHEMA + ".fds_case_status t join " + SCHEMA + ".fds_case_detail d on t.case_no = d.case_no join " + SCHEMA + ".fds_txn_detail fdstxndetail on d.txn_cre_tms = fdstxndetail.f9_oa008_cre_tms and d.enc_crd_no = fdstxndetail.fx_oa008_used_pan left join " + SCHEMA + ".fds_description des on t.closed_reason = des.id left join " + SCHEMA + ".dw005 on px_dw005_pan = d.enc_crd_no left join " + SCHEMA + ".dw006 on px_dw006_own_pan = d.enc_crd_no where 1 = 1 ");
		
		if (fromdate != null && todate != null) {
			sQuery.append(" and t.cre_tms = (select max(s.cre_tms) from " + SCHEMA + ".fds_case_status s where s.case_no = t.case_no and s.cre_tms between " + fromdate + " and " + todate + " ) and t.cre_tms between " + fromdate + " and " + todate + " ");
		}
		if (userid != null) {
			sQuery.append(" and upper(t.user_id) = upper('" + userid + "')");
		}
		if (closedReason != null) {
			sQuery.append(" and t.closed_reason = '" + closedReason + "'");
		}
		if (crdbrn != null) {
			if ("MC".equals(crdbrn) || "VS".equals(crdbrn)) {
				sQuery.append(" and d.crd_brn = '" + crdbrn + "'");
			}
			if ("MD".equals(crdbrn)) {
				sQuery.append(" and (d.crd_brn = 'MC' and fdstxndetail.fx_oa008_crd_pgm like 'MD%')");
			}
		}
		sQuery.append(" order by t.cre_tms desc");
		// Danh so thu tu
		final String sTemp = "select rownum as STT, m.* from ( " + sQuery.toString() + " ) m";
		// @formatter:on
		final Query query = em.createNativeQuery(sTemp);
		System.out.println("case lich su:"+sTemp);
		return query.getResultList();
	}

	/** Bao cao tong so case get ve */
	@SuppressWarnings("unchecked")
	public List<Object[]> reportAllCase(final String fromdate, final String todate) {
		// @formatter:off
		final StringBuilder sQuery = new StringBuilder("select d.case_no, substr(d.cre_tms,7,2)||'/'||substr(d.cre_tms,5,2)||'/'||substr(d.cre_tms,1,4)||' '||substr(d.cre_tms,9,2)||':'||substr(d.cre_tms,11,2)||':'||case when substr(d.cre_tms,13,2)>'60' then '60' else substr(d.cre_tms,13,2) as cre_tms, (select px_irpanmap_panmask from ir_pan_map@im.world p where p.px_irpanmap_pan = d.enc_crd_no) panmask, decode((select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select f9_ir025_loc_acct from ir025@im.world where f9_ir025_loc_acct in (select f9_ir025_loc_acct from ir025@im.world where FX_IR025_CRD_BRN <> 'LC') and fx_ir025_ref_pan <> '89CD0D32EC559AF2XXX' and fx_ir025_ref_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) loc, (select listagg(r.rule_id, ',') within group(order by r.case_no) a from " + SCHEMA + ".fds_case_hit_rules r where r.case_no = d.case_no group by r.case_no) ruleid, des2.description case_status from " + SCHEMA + ".fds_case_detail d, " + SCHEMA + ".fds_description des2 where d.cre_tms between " + fromdate + " and " + todate + " and d.case_status = des2.id and des2.type = 'CASE STATUS' order by d.cre_tms desc");
		// @formatter:on
		// Danh so thu tu
		final String sTemp = "select rownum as STT, m.* from ( " + sQuery.toString() + " ) m";
		final Query query = em.createNativeQuery(sTemp);
		System.out.println("case lich su:"+sTemp);
		return query.getResultList();
	}

	/** Bao cao theo thoi gian giao dich the KH */
	@SuppressWarnings("unchecked")
	public List<Object[]> reportCaseByTxn(final String fromdate, final String todate, final String crdbrn) {
		// @formatter:off
		final StringBuilder sQuery = new StringBuilder("select px_irpanmap_panmask as crd_no, decode((select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select f9_ir025_loc_acct from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) loc, decode((select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) cust_name, d.crd_brn, to_char(to_date(substr(d.txn_cre_tms, 0, 14), 'yyyyMMddHH24MISS'), 'dd/mm/yyyy HH24:MI:SS') txn_time, d.case_no, (select listagg(r.rule_id, ', ') within group(order by r.case_no) a from " + SCHEMA + ".fds_case_hit_rules r where r.case_no = d.case_no group by r.case_no) ruleid, (select t.closed_reason from " + SCHEMA + ".fds_case_status t where t.cre_tms = (select max(t2.cre_tms) from " + SCHEMA + ".fds_case_status t2 where t2.case_no = t.case_no) and t.case_no = d.case_no) closed_reason, d.case_status, (select t.user_id from " + SCHEMA + ".fds_case_status t where t.cre_tms = (select max(t2.cre_tms) from " + SCHEMA + ".fds_case_status t2 where t2.case_no = t.case_no) and t.case_no = d.case_no and rownum = 1) usr_id from " + SCHEMA + ".fds_case_detail d join fds_txn_detail fdstxndetail on d.txn_cre_tms = fdstxndetail.f9_oa008_cre_tms and d.enc_crd_no = fdstxndetail.fx_oa008_used_pan left join ir_pan_map@im.world p on p.px_irpanmap_pan = d.enc_crd_no where 1 = 1 and d.txn_cre_tms between " + fromdate + " and " + todate);
		// @formatter:on
		if (crdbrn != null && !"".equals(crdbrn)) {
			if ("MC".equals(crdbrn) || "VS".equals(crdbrn)) {
				sQuery.append(" and crd_brn='" + crdbrn + "'");
			}
			if ("MD".equals(crdbrn)) {
				sQuery.append(" and (crd_brn='MC' and fdstxndetail.fx_oa008_crd_pgm like 'MD%')");
			}
		}
		sQuery.append(" order by d.txn_cre_tms desc");
		// @formatter:off
		final String sTemp = "select rownum, m.crd_no, m.loc, m.cust_name, m.crd_brn, m.txn_time, m.case_no, m.ruleid, (select des1.description from "+ SCHEMA +".fds_description des1 where des1.type = 'ACTION' and des1.id = m.closed_reason) closed_reason, (select des2.description from "+ SCHEMA + ".fds_description des2 where des2.type = 'CASE STATUS' and des2.id = m.case_status) case_status,m.usr_id from ( "+ sQuery.toString() + " ) m";
		// @formatter:on
		final Query query = em.createNativeQuery(sTemp);
		return query.getResultList();
	}

	/** Bao cao theo tinh trang case */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> reportCaseByStatus(final String fromdate, final String todate, final String crdbrn, final String userid,
			final String status) {
		// @formatter:off
		final StringBuilder sQuery= new StringBuilder("select d.case_no, substr(d.cre_tms,7,2)||'/'||substr(d.cre_tms,5,2)||'/'||substr(d.cre_tms,1,4)||' '||substr(d.cre_tms,9,2)||':'||substr(d.cre_tms,11,2)||':'||case when substr(d.cre_tms,13,2)>'60' then '60' else substr(d.cre_tms,13,2) as cre_tms, (select des2.description from "+SCHEMA+".fds_description des2 where des2.type = 'CASE STATUS' and des2.id = d.case_status) as case_status, (select px_irpanmap_panmask from ir_pan_map@im.world where d.enc_crd_no = px_irpanmap_pan) panmask, decode((select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select f9_ir025_loc_acct from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = d.enc_crd_no union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) loc, d.crd_brn, decode((select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no), null, (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where fx_ir025_ref_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where fx_ir275_ref_pan = d.enc_crd_no), (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = d.enc_crd_no union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = d.enc_crd_no)) custname, (select listagg(r.rule_id, ', ') within group(order by r.case_no) a from "+SCHEMA+".fds_case_hit_rules r where r.case_no = d.case_no group by r.case_no) ruleid, upper(d.usr_id) as user_id from "+SCHEMA+".fds_case_detail d join fds_txn_detail fdstxndetail on d.txn_cre_tms = fdstxndetail.f9_oa008_cre_tms and d.enc_crd_no = fdstxndetail.fx_oa008_used_pan where d.cre_tms between "+fromdate+" and "+todate);
		// @formatter:on
		if (crdbrn != null) {
			if ("MC".equals(crdbrn) || "VS".equals(crdbrn)) {
				sQuery.append(" and d.crd_brn='" + crdbrn + "'");
			}
			if ("MD".equals(crdbrn)) {
				sQuery.append(" and (d.crd_brn='MC' and fdstxndetail.fx_oa008_crd_pgm like 'MD%')");
			}
		}
		if (userid != null) {
			sQuery.append(" and upper(d.usr_id)=upper('" + userid + "')");
		}
		if (status != null) {
			sQuery.append(" and d.case_status='" + status + "'");
		}
		// Danh so thu tu
		final String sTemp = "select rownum, m.* from (" + sQuery.toString() + " order by d.cre_tms desc) m";
		final Query query = em.createNativeQuery(sTemp);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> reportRuleId(final String fromdate, final String todate, final String ruleid) {
		// @formatter:off
		final StringBuilder sQuery=new StringBuilder("select t.rule_id, t.rule_desc, (select count(r1.case_no) from fds_case_hit_rules r1 join fds_case_detail d1 on r1.case_no = d1.case_no where r1.cre_tms between "+ fromdate +" and "+ todate +" and r1.rule_id = t.rule_id) casehit, (select count(r1.case_no) from fds_case_hit_rules r1 join fds_case_detail d1 on r1.case_no = d1.case_no where r1.cre_tms between "+ fromdate +" and "+ todate +" and r1.rule_id = t.rule_id and d1.case_status in ('DIC', 'CAF')) processedcase, (select count(r1.case_no) from fds_case_hit_rules r1 join fds_case_detail d1 on r1.case_no = d1.case_no where r1.cre_tms between "+ fromdate +" and "+ todate +" and r1.rule_id = t.rule_id and d1.case_status not in ('DIC', 'CAF')) casewaitpro from fds_rules t ");
		// @formatter:on
		if (ruleid != null) {
			sQuery.append(" where t.rule_id = '" + ruleid + "'");
		}
		sQuery.append(" order by t.rule_priority");
		final String sTemp = "select rownum, m.* from ( " + sQuery.toString() + " ) m";
		final Query query = em.createNativeQuery(sTemp);
		return query.getResultList();
	}

	@Override
	public List<Object[]> reportMerchant(final String fromdate, final String todate, final String merchant, final String terminalId,
			final String cardNo, final String mcc) {
		// TODO Auto-generated method stub
		return null;
	}

	// Report chi tiet giao dich
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> reportTxnCrdDet(String fromdate, String todate, String merchant, String terminalId, String cardNo, String mcc) {
		final StoredProcedureQuery query = em.createStoredProcedureQuery("RP_FDS_TXNCRDDET");
		query.registerStoredProcedureParameter("P_MERCHANT", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_TID", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_CRDNO", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_MCC", String.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_FROMDATE", BigDecimal.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("P_TODATE", BigDecimal.class, ParameterMode.IN);
		query.registerStoredProcedureParameter("OUT_RS", void.class, ParameterMode.REF_CURSOR);

		query.setParameter("P_MERCHANT", merchant);
		query.setParameter("P_TID", terminalId);
		query.setParameter("P_CRDNO", cardNo);
		query.setParameter("P_MCC", mcc);
		query.setParameter("P_FROMDATE", new BigDecimal(fromdate));
		query.setParameter("P_TODATE", new BigDecimal(todate));

		query.execute();
		return query.getResultList();
	}
	
	

}
