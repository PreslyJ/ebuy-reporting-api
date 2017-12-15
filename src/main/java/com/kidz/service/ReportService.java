package com.kidz.service;

import java.util.Date;
import java.util.List;

import com.kidz.cart.model.Customer;
import com.kidz.cart.model.Item;
import com.kidz.cart.model.PurchasedItems;
import com.kidz.cart.model.StockItems;

public interface ReportService {
	
	public List<Customer> findAll();

	public List<Item> findAllItems();
	
	public int getClosingStock(Date fromDate,Date toDate,long itemId);
	
	public int getPurchasedStock(Date fromDate,Date toDate,long itemId);
	
	public int getStockIn(Date fromDate,Date toDate,long itemId);
	
	public List<PurchasedItems> getPurchasedStock(Date fromDate,Date toDate);
	
	public StockItems getLastStockByItemId(long itemId) ;
	
}
