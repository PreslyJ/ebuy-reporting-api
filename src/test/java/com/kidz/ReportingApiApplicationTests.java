package com.kidz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kidz.service.ReportServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportingApiApplicationTests {

	@Autowired
	ReportServiceImpl reportServiceImpl;
	
	
	@Test
	public void contextLoads() {
		System.out.println(reportServiceImpl.findAll().get(0).getEmail());
	}

}
