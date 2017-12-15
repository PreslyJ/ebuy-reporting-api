package com.kidz.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.kidz.cart.model.Customer;
import com.kidz.cart.model.Item;
import com.kidz.cart.model.PurchasedItems;
import com.kidz.cart.model.StockItems;
import com.kidz.service.ReportService;
import com.kidz.util.ExcelGenerator;


@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	ReportService reportService;
	
	@Autowired
	ExcelGenerator excelGenerator;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getCustomerReport",method=RequestMethod.POST)
	public void saveCustomer(HttpServletRequest request,HttpServletResponse response) throws  IOException {
		
	    try {
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-Disposition","attachment; filename=customers.xls");
		 
		    List<Customer> cusList=reportService.findAll();
	
		    String[] headers = {"ID", "First Name","Last Name","Email","Phone No","Address 1","Address 2","Address 3","Registered Date"};
		     
		    Map data=new HashMap<>();
		     
		    int i=0;
		     
		    for (Customer customer : cusList) {
				
		    	List row=new ArrayList<>();
		    	
				row.add(customer.getId());
		    	row.add(customer.getFirstName());				
		    	row.add(customer.getLastName());				
		    	row.add(customer.getEmail());				
		    	row.add(customer.getPhoneNumber());				
		    	row.add(customer.getAddress1());				
		    	row.add(customer.getAddress2());				
		    	row.add(customer.getAddress3());				
		    	row.add(customer.getRegisteredDate());
		    	
		    	data.put(i, row);
		    	
		    	i++;
		    	
			}
	
		    excelGenerator.printReport(data,headers,"Customer Report",sdf.format(new Date()),response.getOutputStream());
		     
	    } catch (Exception e){
	    	e.printStackTrace();
	    } 
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getStockReport",method=RequestMethod.POST)
	public void getStockSummary(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> filterMap) throws  IOException {
		
	    try {
	 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date fromDate = null;
			try {
				fromDate = filterMap.get("fromDate") != null ? sdf.parse((String) filterMap.get("fromDate")) : null;
			} catch (ParseException e) {
			}
			Date toDate = null;
			try {
				toDate = filterMap.get("toDate") != null ? sdf.parse((String) filterMap.get("toDate")) : null;
			} catch (ParseException e) {
			}
			
			
			if (toDate == null && fromDate != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}
			
					
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=customers.xls");
				 
			List<Item> itemList=reportService.findAllItems();
			 
			String[] headers = {"ID", "First Name","Last Name","Email","Phone No","Address 1","Address 2","Address 3","Registered Date"};
			 
			Map data=new HashMap<>();
			int i=0;
			 
			 for (Item item : itemList) {
				
				List row=new ArrayList<>();
				
				row.add(item.getId());
				row.add(item.getName());				
				row.add(item.getDescription());				
				row.add(item.getSubCategory().getName());				
				row.add(item.getSubCategory().getCategory().getName());				

				int opening=reportService.getClosingStock(fromDate, toDate, item.getId()); //Last closing will be the opening of this stock						
				row.add(opening);
				
				int stockIn=reportService.getStockIn(fromDate, toDate, item.getId());
				row.add(stockIn);				
				
				int stockOut=reportService.getPurchasedStock(fromDate, toDate, item.getId());
				row.add(stockOut);				
				
				row.add(opening+stockIn-stockOut);
				
				data.put(i, row);
				
				i++;
				
			}
			
			List row=new ArrayList<>();
			
			row.add("");
			row.add("");				
			row.add("");				
			row.add("");				
			row.add("");				
			row.add("Total");
			row.add("Total");
			row.add("Total");
			row.add("Total");
			
			data.put(i, row);
 			 
			excelGenerator.printReport(data,headers,"Stock Report",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	
	}
	    
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getSalesSummaryReport",method=RequestMethod.POST)
	public void getSalesSummary(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> filterMap) throws  IOException {
		
	    try {
	 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date fromDate = null;
			try {
				fromDate = filterMap.get("fromDate") != null ? sdf.parse((String) filterMap.get("fromDate")) : null;
			} catch (ParseException e) {
			}
			Date toDate = null;
			try {
				toDate = filterMap.get("toDate") != null ? sdf.parse((String) filterMap.get("toDate")) : null;
			} catch (ParseException e) {
			}
			
			
			if (toDate == null && fromDate != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}
			
					
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=salesSummary.xls");
				 
			List<PurchasedItems> list= reportService.getPurchasedStock(fromDate,toDate); 
			
			String[] headers = {"ID", "First Name","Last Name","Email","Phone No","Address 1","Address 2","Address 3","Registered Date"};
			 
			Map data=new HashMap<>();
			int i=0;
			 
			 for (PurchasedItems item : list) {
				
				List row=new ArrayList<>();
				row.add(item.getPurchasedDate());
				row.add(item.getCustomer().getEmail());	
				row.add(item.getItem().getName());						
				row.add(item.getNoOfItems());				
				row.add(item.getPurchasePrice()/item.getNoOfItems());				
				row.add(item.getPurchasePrice());
				
				data.put(i, row);
				
				i++;
				
			}

			List row=new ArrayList<>();
			row.add("");	
			row.add("");						
			row.add("Total");				
			row.add("");				
			row.add("Total");
			
			data.put(i, row);
			 
			excelGenerator.printReport(data,headers,"Sales Summary",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getProfitReport",method=RequestMethod.POST)
	public void getProfitReport(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String, Object> filterMap) throws  IOException {
		
	    try {

	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date fromDate = null;
			try {
				fromDate = filterMap.get("fromDate") != null ? sdf.parse((String) filterMap.get("fromDate")) : null;
			} catch (ParseException e) {
			}
			Date toDate = null;
			try {
				toDate = filterMap.get("toDate") != null ? sdf.parse((String) filterMap.get("toDate")) : null;
			} catch (ParseException e) {
			}
			
			if (toDate == null && fromDate != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}
					
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=salesSummary.xls");
				 
			List<PurchasedItems> list= reportService.getPurchasedStock(fromDate,toDate); 
			
			String[] headers = {"ID", "First Name","Last Name","Email","Phone No","Address 1","Address 2","Address 3","Registered Date"};
			 
			Map data=new HashMap<>();
			int i=0;
			 
			 for (PurchasedItems item : list) {
				
				List row=new ArrayList<>();

				row.add(item.getPurchasedDate());	
				row.add(item.getItem().getName());
				
				double selling=item.getPurchasePrice()/item.getNoOfItems();
				row.add(selling);	
				
				StockItems stockItem=reportService.getLastStockByItemId(item.getItem().getId());
				row.add(stockItem.getBuyingPrice());				
				
				double perUnitPro=selling-stockItem.getBuyingPrice();
				row.add(perUnitPro);
				
				row.add(item.getNoOfItems());
				row.add(item.getNoOfItems()*perUnitPro);

				data.put(i, row);
				
				i++;
				
			}

			List row=new ArrayList<>();
			row.add("");	
			row.add("");
			row.add("");	
			row.add("");				
			row.add("");
			row.add("Total");
			row.add("Total");

			data.put(i, row);
			 
			excelGenerator.printReport(data,headers,"Sales Summary",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	}	
	
}
