package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.CustomerInfo;
import com.fds.repositories.CustomerInfoRepo;

@Service("customerInfoService")
public class CustomerInfoServiceImpl implements CustomerInfoService {

	@Autowired
	CustomerInfoRepo customerInfoRepo;

	@Override
	public CustomerInfo findAll(String cifno) {
		return customerInfoRepo.findAll(cifno);
	}

	@Override
	public CustomerInfo findByCrdNo(String crdno) {
		return customerInfoRepo.findByCrdNo(crdno);
	}
	
	@Override
	public CustomerInfo findByCif(String cifno) {
		return customerInfoRepo.findByCif(cifno);
	}

	@Override
	public CustomerInfo findOneAll(String crdno) {
		return customerInfoRepo.findOneAll(crdno);
	}

	@Override
	public List<CustomerInfo> findAllTypetask(String type) {
		return customerInfoRepo.findAllTypetask(type);
	}

}
