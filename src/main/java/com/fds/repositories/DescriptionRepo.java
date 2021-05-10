package com.fds.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fds.entities.FdsDescription;

public interface DescriptionRepo extends CrudRepository<FdsDescription, Long> {
	Iterable<FdsDescription> findAllByType(String type);
	
//	Iterable<FdsDescription> findAllByTypeByOrderBySequencenoAsc(String type);
	
	@Query(nativeQuery = true, value = "SELECT * FROM FDS_DESCRIPTION WHERE TYPE = :type ORDER BY SEQUENCENO ASC")
	public Iterable<FdsDescription> findAllByTypeByOrderBySequencenoAsc(@Param(value = "type") String type);
	
	@Query(nativeQuery = true, value = "SELECT 'A' || (MAX(SUBSTR(ID,2,2))+1) FROM FDS_DESCRIPTION WHERE ID LIKE 'A%' AND TYPE <> 'ACTION' AND ID<'A98'")
	public String getNextIdContentDetail();
}
