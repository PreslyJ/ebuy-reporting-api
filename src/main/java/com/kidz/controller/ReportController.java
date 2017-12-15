package com.kidz.controller;

import java.io.IOException;
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
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.kidz.cart.model.Customer;
import com.kidz.cart.model.Item;
import com.kidz.cart.model.PurchasedItems;
import com.kidz.cart.model.StockItems;
import com.kidz.service.ReportService;
import com.kidz.util.ExcelGenerator;

@RestController
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping("/report")
public class ReportController {

	@Autowired
	ReportService reportService;
	
	@Autowired
	ExcelGenerator excelGenerator;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getCustomerReport",method=RequestMethod.GET)
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

	@PreAuthorize("hasRole('ROLE_REPORTUSER')")
	@RequestMapping(value="/R1",method=RequestMethod.GET)
	public void getRep1(){}

	@PreAuthorize("hasRole('ROLE_REPORTUSER')")
	@RequestMapping(value="/R2",method=RequestMethod.GET)
	public void getRep2(){}

	@PreAuthorize("hasRole('ROLE_REPORTUSER')")
	@RequestMapping(value="/R3",method=RequestMethod.GET)
	public void getRep3(){}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getStockReport",method=RequestMethod.GET)
	public void getStockSummary(HttpServletRequest request,HttpServletResponse response,@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date  toDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) throws  IOException {
		
	    try {
	 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=stockReport.xls");
				 
			List<Item> itemList=reportService.findAllItems();
			 
			String[] headers = {"ID", "Name","Description","Subcategory","category","Opening Stock","Purchase In","Purchase OUT","Closing Stock"};
			 
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
 			 
			excelGenerator.printReport(data,headers,"Stock Report",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	
	}
	    
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getSalesSummaryReport",method=RequestMethod.GET)
	public void getSalesSummary(HttpServletRequest request,HttpServletResponse response,@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date  toDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) throws  IOException {
		
	    try {
	 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			
			if (toDate == null && fromDate != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
			}
			
					
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=sales_detail.xls");
				 
			List<PurchasedItems> list= reportService.getPurchasedStock(fromDate,toDate); 
			
			String[] headers = {"ID", "Username","Purchased item","Units","Unit price","Total"};
			 
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

	/*		List row=new ArrayList<>();
			row.add("");	
			row.add("");						
			row.add("Total");				
			row.add("");				
			row.add("Total");
			
			data.put(i, row);*/
			 
			excelGenerator.printReport(data,headers,"Sales Detail",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/getProfitReport",method=RequestMethod.GET)
	public void getProfitReport(HttpServletRequest request,HttpServletResponse response,@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date  toDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate) throws  IOException {
		
	    try {

	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=Profits.xls");
				 
			List<PurchasedItems> list= reportService.getPurchasedStock(fromDate,toDate); 
			
			String[] headers = {"ID", "Purchased Item","Selling Price","Unit Cost","Per Unit Profit","QTY Sold","Profit in total"};
			 
			Map data=new HashMap<>();
			int i=0;
			 
			 for (PurchasedItems item : list) {
				
				List row=new ArrayList<>();

				row.add(item.getPurchasedDate());	
				row.add(item.getItem().getName());
				
				double selling=item.getPurchasePrice()/item.getNoOfItems();
				row.add(selling);	
				
				StockItems stockItem;
				double perUnitPro=0;
				try {
					stockItem = reportService.getLastStockByItemId(item.getItem().getId());
					row.add(stockItem.getBuyingPrice());
					perUnitPro=selling-stockItem.getBuyingPrice();
					row.add(perUnitPro);

				} catch (Exception e) {
					row.add(0);
					perUnitPro=selling;
					row.add(perUnitPro);
					e.printStackTrace();
				}				
				
				
				row.add(item.getNoOfItems());
				row.add(item.getNoOfItems()*perUnitPro);

				data.put(i, row);
				
				i++;
				
			}

			/*List row=new ArrayList<>();
			row.add("");	
			row.add("");
			row.add("");	
			row.add("");				
			row.add("");
			row.add("Total");
			row.add("Total");

			data.put(i, row);*/
			 
			excelGenerator.printReport(data,headers,"Profits",sdf.format(new Date()),response.getOutputStream());
	     
		} catch (Exception e){
			e.printStackTrace();
		} 
	
	}	
	
}
