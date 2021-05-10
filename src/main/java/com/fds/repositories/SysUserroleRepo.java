package com.fds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fds.entities.FdsSysUserrole;

@Repository
public interface SysUserroleRepo extends CrudRepository<FdsSysUserrole, String> {

	@Query(value = "select f from FdsSysUserrole f where f.id.iduser=:iduser")
	List<FdsSysUserrole> findAllByIdUser(@Param("iduser") String iduser);

	@Query(value = "delete from {h-schema}fds_sys_userrole t where t.iduser=:iduser", nativeQuery = true)
	List<FdsSysUserrole> deleteByIduser(@Param("iduser") String iduser);
}
