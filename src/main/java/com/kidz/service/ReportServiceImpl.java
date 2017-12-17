package com.kidz.service;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kidz.cart.model.Customer;
import com.kidz.cart.model.Item;
import com.kidz.cart.model.PurchasedItems;
import com.kidz.cart.model.StockItems;
import com.kidz.repository.AbstractJpaDao;


@Service
@Transactional
public class ReportServiceImpl implements ReportService{

	@Autowired
    AbstractJpaDao abstractJpaDao; 
	
	public List<Customer> findAll() {

		return abstractJpaDao.findAll(Customer.class);
	
	}

	@Override
	public List<Item> findAllItems() {
		return abstractJpaDao.findAll(Item.class, " status='active'",null,null);
	}

	public int getClosingStock(Date fromDate,Date toDate,Long itemId) {

		 Integer stock=0;
		try {
			stock = abstractJpaDao.findCount("SELECT sum(e.noOfItems) from StockItems e where  e.item.id="+itemId+" and e.inventryDate<?"+1,fromDate,null);
		} catch (Exception e) {
		}
		 Integer purchases=getPurchasedStock(fromDate, itemId);
	
		 if (stock==null)
			 stock=0;
		 
		 if(purchases==null)
			 purchases=0;
		 
		 return stock-purchases;
	}
	
	public int getPurchasedStock(Date fromDate,Date toDate,long itemId) {
		
		Integer purchases=0;
		try {
			purchases = abstractJpaDao.findCount("SELECT sum(e.noOfItems) from PurchasedItems e where  e.item.id="+(int)itemId+" and e.purchasedDate>=?"+1+" and e.purchasedDate<=?"+2,fromDate,toDate);
		} catch (Exception e) {
		}

		if(purchases==null)
			 purchases=0;

		return  purchases;
	
	}

	public int getStockIn(Date fromDate,Date toDate,long itemId) {

		 Integer stock=0;
		try {
			stock = abstractJpaDao.findCount("SELECT sum(e.noOfItems) from StockItems e where  e.item.id="+itemId+" and e.inventryDate>=?"+1 +" and e.inventryDate<=?"+2,fromDate,toDate);
		} catch (Exception e) {
		}
	
		 if (stock==null)
			 stock=0;
		 
	 	 return stock;
		 
	}
	
	
	public Integer getPurchasedStock(Date toDate,long itemId) {
		try {
			return abstractJpaDao.findCount("SELECT sum(e.noOfItems) from PurchasedItems e where  e.item.id="+itemId+" and e.purchasedDate<?"+1,toDate,null);
		} catch (Exception e) {
			return 0;
		}
	}
	

	public List<PurchasedItems> getPurchasedStock(Date fromDate,Date toDate) {
		
		return abstractJpaDao.findAll(PurchasedItems.class,"e.purchasedDate>=?"+1+" and e.purchasedDate<=?"+2,fromDate,toDate);

	}
	
	public StockItems getLastStockByItemId(long itemId) {
		
		StockItems stockItem=abstractJpaDao.findOne(StockItems.class, " e.id=(SELECT max(s.id) from StockItems s where  s.item.id="+itemId+")");
		 
	 	return stockItem;
		 
	}
	
	
}
