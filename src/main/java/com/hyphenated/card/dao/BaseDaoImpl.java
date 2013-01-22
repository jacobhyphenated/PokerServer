package com.hyphenated.card.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<T> implements BaseDao<T> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public T findById(long id){
		return (T) getSession().get(persistentClass, id);
	}

	@Override
	public Class<T> getEntityClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return getSession().createCriteria(persistentClass).list();
	}
	
	@Override
	public void remove(T objToRemove){
		getSession().delete(objToRemove);
	}
	
	@Override
	public T save(T objToSave){
		getSession().saveOrUpdate(objToSave);
		return objToSave;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T merge(T objToMerge){
		return (T) getSession().merge(objToMerge);
	}
}
