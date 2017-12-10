package com.kidz.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.kidz.cart.model.Customer;
import com.kidz.repository.AbstractJpaDao;

@Service
@Transactional
public class ReportServiceImpl {

	@Autowired
    AbstractJpaDao abstractJpaDao; 
	
	public List<Customer> findAll() {

		return abstractJpaDao.findAll(Customer.class);
	
	}

	
}
