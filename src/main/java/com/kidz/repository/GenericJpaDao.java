package com.kidz.repository;

import java.io.Serializable;

import javax.inject.Scope;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Repository;

@Repository
//@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GenericJpaDao< T extends Serializable >
/* extends AbstractJpaDao< T > implements IGenericDao< T >*/{
   //
}