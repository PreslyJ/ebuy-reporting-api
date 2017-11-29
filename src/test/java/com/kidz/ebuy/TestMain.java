package com.kidz.ebuy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidz.util.ExcelGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		
		List<Object> o=new ArrayList<>();
		
		o.add("asdsad");
		o.add("asdsad");
		o.add("asdsad");		o.add("asdsad");		o.add("asdsad");		o.add("asdsad");		o.add("asdsad");		o.add("asdsad");
		
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
