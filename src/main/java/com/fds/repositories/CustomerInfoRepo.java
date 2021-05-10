package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.CustomerInfo;

@Repository
public interface CustomerInfoRepo extends JpaRepository<CustomerInfo, String> {

	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, fx_ir056_off_tel_1 as cust_off_tel_1, fx_ir056_off_tel_2 as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, fx_ir056_cif_no as cust_cif from ir056@im.world join FDS_SYS_TASK s on trim(fx_ir056_cif_no) = s.objecttask where s.typetask =:typetask ", nativeQuery = true)
	List<CustomerInfo> findAllTypetask(@Param("typetask") String typetask);

	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, fx_ir056_off_tel_1 as cust_off_tel_1, fx_ir056_off_tel_2 as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, fx_ir056_cif_no as cust_cif from ir056@im.world where rownum = 1 and fx_ir056_cif_no = :cifno ", nativeQuery = true)
	CustomerInfo findAll(@Param("cifno") String cifno);

	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, fx_ir056_off_tel_1 as cust_off_tel_1, fx_ir056_off_tel_2 as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, fx_ir056_cif_no as cust_cif from ir056@im.world where rownum = 1 and trim(fx_ir056_cif_no) =?1", nativeQuery = true)
	CustomerInfo findOneAll(String cifno);

	/**
	 * Lay ten khach hang (chinh/phu) theo so the
	 */
	@Query(value = "select decode((select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = :enccardno union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = :enccardno), null, (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where FX_IR025_REF_PAN = :enccardno union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where FX_IR275_REF_PAN = :enccardno ), (select trim(fx_ir025_emb_lst_nm) || ' ' || trim(fx_ir025_emb_mid_nm) || ' ' || trim(fx_ir025_emb_name) from ir025@im.world where px_ir025_pan = :enccardno union select trim(fx_ir275_emb_lst_nm) || ' ' || trim(fx_ir275_emb_mid_nm) || ' ' || trim(fx_ir275_emb_name) from ir275@im.world where px_ir275_own_pan = :enccardno)) custname from dual", nativeQuery = true)
	String getCustNameByEncCrdNo(String enccardno);

	/**
	 * Lay Loc (chinh/phu) theo so the
	 */
	@Query(value = "select decode((select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = :enccardno union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = :enccardno), null, (select f9_ir025_loc_acct from ir025@im.world where fx_ir025_ref_pan = :enccardno union select f9_ir275_loc_acct from ir275@im.world where fx_ir275_ref_pan = :enccardno), (select f9_ir025_loc_acct from ir025@im.world where px_ir025_pan = :enccardno union select f9_ir275_loc_acct from ir275@im.world where px_ir275_own_pan = :enccardno)) loc from dual", nativeQuery = true)
	String getLocByEncCrdNo(String enccardno);

	/*
	 * select trim(fx_ir056_name) as cust_name,
	 * decode(fx_ir056_gendr, 'F', 'Bà', 'Ông') as cust_gendr,
	 * fx_ir056_hp as cust_hp,
	 * fx_ir056_hme_tel as cust_off_tel_1,
	 * fx_ir056_off_tel_1 as cust_off_tel_2,
	 * fx_ir056_email_addr as cust_email_addr,
	 * fx_ir056_cif_no as cust_cif
	 * from ir056@im.world
	 * where p9_ir056_crn = (select f9_ir275_crn
	 * from ir275@im.world
	 * where px_ir275_own_pan = :crdno)
	 * union
	 * select trim(fx_ir056_name) as cust_name,
	 * decode(fx_ir056_gendr, 'F', 'Bà', 'Ông') as cust_gendr,
	 * fx_ir056_hp as cust_hp,
	 * fx_ir056_hme_tel as cust_off_tel_1,
	 * fx_ir056_off_tel_1 as cust_off_tel_2,
	 * fx_ir056_email_addr as cust_email_addr,
	 * fx_ir056_cif_no as cust_cif
	 * from ir056@im.world
	 * where p9_ir056_crn =
	 * (select f9_ir025_crn from ir025@im.world where px_ir025_pan = :crdno)
	 */
	/*khoa khong check so dien thoai fcc
	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, '0' as cust_off_tel_1, fx_ir056_off_tel_1 as cust_off_tel_2, trim(fx_ir056_email_addr) as cust_email_addr, trim(fx_ir056_cif_no) as cust_cif from ir056@im.world where p9_ir056_crn = (select nvl((select f9_ir025_crn from ir025@im.world where px_ir025_pan = :crdno),(select f9_ir275_crn from ir275@im.world where px_ir275_own_pan = :crdno)) from dual)", nativeQuery = true)
	CustomerInfo findByCrdNo(@Param("crdno") String crdno);
	*/
	/*huyennt add them ngay 14Oct2017*/
	/*@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, '0' as cust_off_tel_1, fx_ir056_off_tel_1 as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, trim(fx_ir056_cif_no) as cust_cif from ir056@im.world where trim(fx_ir056_cif_no) = :cifno and rownum <= 1", nativeQuery = true)
	CustomerInfo findByCif(@Param("cifno") String cifno);
	*/
	//tam khoa check so dt fcc
	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, '' as cust_off_tel_1, '' as cust_off_tel_2, trim(fx_ir056_email_addr) as cust_email_addr, trim(fx_ir056_cif_no) as cust_cif from ir056@im.world where p9_ir056_crn = (select nvl((select f9_ir025_crn from ir025@im.world where px_ir025_pan = :crdno),(select f9_ir275_crn from ir275@im.world where px_ir275_own_pan = :crdno)) from dual)", nativeQuery = true)
	CustomerInfo findByCrdNo(@Param("crdno") String crdno);
	
	//huyennt add them ngay 14Oct2017
	//tanvh1
//	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, '0963137131' as cust_off_tel_1, '0935569842' as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, trim(fx_ir056_cif_no) as cust_cif from ir056@im.world where trim(fx_ir056_cif_no) = :cifno and rownum <= 1", nativeQuery = true)
	@Query(value = "select trim(fx_ir056_name) as cust_name, case trim(fx_ir056_ttl) when 'MISS' then 'Bà' when 'MR' then 'Ông' when 'MRS' then 'Bà' else '' end cust_gendr, fx_ir056_hp as cust_hp, nvl((SELECT trim(MOBILE_NUMBER) FROM fcusr01.STTM_CUST_PERSONAL@exadata where CUSTOMER_NO = trim(fx_ir056_cif_no)),' ') as cust_off_tel_1, nvl((SELECT trim(telephone) FROM fcusr01.STTM_CUST_PERSONAL@exadata where CUSTOMER_NO = trim(fx_ir056_cif_no)),' ') as cust_off_tel_2, fx_ir056_email_addr as cust_email_addr, trim(fx_ir056_cif_no) as cust_cif from ir056@im.world where trim(fx_ir056_cif_no) = :cifno and rownum <= 1", nativeQuery = true)
	CustomerInfo findByCif(@Param("cifno") String cifno);
}

/*
 * select trim(fx_ir056_name) as cust_name,
 * decode(fx_ir056_gendr, 'F', 'Bà', 'Ông') as cust_gendr,
 * trim(fx_ir056_hp) as cust_hp,
 * trim(fx_ir056_off_tel_1) as cust_off_tel_1,
 * trim(fx_ir056_off_tel_2) as cust_off_tel_2,
 * trim(fx_ir056_email_addr) as cust_email_addr,
 * trim(fx_ir056_cif_no) as cust_cif
 * from ir056@im.world
 * where p9_ir056_crn =
 * (select F9_IR275_CRN
 * from ir275@im.world
 * where PX_IR275_OWN_PAN = '48E219A099958F1CXXX')
 * union
 * select trim(fx_ir056_name) as cust_name,
 * decode(fx_ir056_gendr, 'F', 'Bà', 'Ông') as cust_gendr,
 * trim(fx_ir056_hp) as cust_hp,
 * trim(fx_ir056_off_tel_1) as cust_off_tel_1,
 * trim(fx_ir056_off_tel_2) as cust_off_tel_2,
 * trim(fx_ir056_email_addr) as cust_email_addr,
 * trim(fx_ir056_cif_no) as cust_cif
 * from ir056@im.world
 * where p9_ir056_crn =
 * (select F9_IR025_CRN
 * from ir025@im.world
 * where PX_IR025_PAN = '48E219A099958F1CXXX');
 */
