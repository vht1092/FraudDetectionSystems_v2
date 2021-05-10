package com.fds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsDescription;
import com.fds.repositories.DescriptionRepo;

@Service("descriptionService")
public class DescriptionServiceImpl implements DescriptionService {

	@Autowired
	private DescriptionRepo descriptionRepo;

	@Override
	public Iterable<FdsDescription> findAllByType(String type) {
		return descriptionRepo.findAllByType(type);
	}
	
	@Override
	public Iterable<FdsDescription> findAllByTypeByOrderBySequencenoAsc(String type) {
		return descriptionRepo.findAllByTypeByOrderBySequencenoAsc(type);
	}
	
	@Override
	public void save(String id, String desc, String type) {

		FdsDescription fdsDescription = new FdsDescription();
		fdsDescription.setId(id);
		fdsDescription.setDescription(desc);
		fdsDescription.setType(type);
		descriptionRepo.save(fdsDescription);
	}
	
	@Override
	public String getNextIdContentDetail() {
		return descriptionRepo.getNextIdContentDetail();
	}
	
	@Override
	public void deleteById(String id) {
		FdsDescription fdsDescription = new FdsDescription();
		fdsDescription.setId(id);
		descriptionRepo.delete(fdsDescription);
	}

}
