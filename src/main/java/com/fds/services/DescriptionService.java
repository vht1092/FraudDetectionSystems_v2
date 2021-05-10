package com.fds.services;

import com.fds.entities.FdsDescription;

public interface DescriptionService {
	Iterable<FdsDescription> findAllByType(String type);
	Iterable<FdsDescription> findAllByTypeByOrderBySequencenoAsc(String type);
	public void save(String id, String desc, String type);
	public String getNextIdContentDetail();
	public void deleteById(String id);
}
