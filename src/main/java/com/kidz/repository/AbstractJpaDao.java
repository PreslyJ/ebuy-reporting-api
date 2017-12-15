package com.kidz.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public  class AbstractJpaDao {
	 	 
	   @PersistenceContext
	   EntityManager entityManager;
	 
	 
	   public <T> T findOne( Long id,Class< T > clazz ){
	      return entityManager.find( clazz, id );
	   }
	
	   @SuppressWarnings("unchecked")
	   public <T> T findOne(Class< T > clazz,String whereClause ){
		      return (T) entityManager.createQuery( "SELECT e from " + clazz.getName()+" e where "+whereClause  )
		   	       .getSingleResult();
	   }
	   
	   @SuppressWarnings("unchecked")
	   public <T> List< T > findAll(Class< T > clazz){
	      return entityManager.createQuery( "from " + clazz.getName() )
	       .getResultList();
	   }

	   
	   public <T> int findCount(Class< T > clazz,String whereClause){
	      return ((Long)entityManager.createQuery( "SELECT count(e) from " + clazz.getName()+" e where "+whereClause )
	       .getSingleResult()).intValue();
	   }
	   
	   public  Integer findCount(String whereClause,Date fromDate,Date toDate){
		   if(toDate==null)
			   return ((Long)entityManager.createQuery(whereClause ).
				   setParameter(1, fromDate).getSingleResult()).intValue();
		   else
			   return ((Long)entityManager.createQuery(whereClause ).
					   setParameter(1, fromDate).setParameter(2,toDate).getSingleResult()).intValue();
	   }
	   
	   public <T> void save( T entity ){
	      entityManager.persist( entity );
	   }
	 
	   public <T> void update( T entity ){
	      entityManager.merge( entity );
	   }
	 
	   public <T> void delete( T entity ){
	      entityManager.remove( entity );
	   }
	   
	   public <T> void deleteById( Long entityId,Class< T > clazz ){
	      T entity = findOne( entityId,clazz );
	      delete( entity );
	   }
	   
	   @SuppressWarnings("unchecked")
	   public <T> List< T > findAll(Class< T > clazz, String whereClause,Date fromDate,Date toDate){
	      if(fromDate!=null && toDate!=null)
	    	  return entityManager.createQuery( "from " + clazz.getName() + " e where "+whereClause ).setParameter(1, fromDate).setParameter(2,toDate)
	    			  .getResultList();
	      else
	    	  return entityManager.createQuery( "from " + clazz.getName() + " e where "+whereClause )
	    		       .getResultList();
	   }
	 
}