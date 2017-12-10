package com.kidz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidz.cart.model.Customer;
import com.kidz.util.ExcelGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		Customer c=new Customer();
		
		
		List<Object> o=new ArrayList<>();
		
	
		Map<Integer, List<Object>> map1=new HashMap<>();
map1.put(1,o);		
		
		
		String[] headarray1={"as"};
 
		try {
			ExcelGenerator.printReport(map1, headarray1, "test","2017/20/20", "tst");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
