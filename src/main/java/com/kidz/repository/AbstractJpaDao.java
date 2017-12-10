package com.kidz.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

@Service
public  class GenericJpaDao {
	 	 
	   @PersistenceContext
	   EntityManager entityManager;
	 
	 
	   public <T> T findOne( Long id,Class< T > clazz ){
	      return entityManager.find( clazz, id );
	   }
	 
	   @SuppressWarnings("unchecked")
	   public <T> List< T > findAll(Class< T > clazz){
	      return entityManager.createQuery( "from " + clazz.getName() )
	       .getResultList();
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
	   
	   public <T> List< T > findAll(Class< T > clazz, String whereClause){
		      return entityManager.createQuery( "from " + clazz.getName() + " "+whereClause )
		       .getResultList();
	   }
	 
}