package com.hyphenated.card.dao;

import java.util.List;

/**
 * Define a generic base data access object for simple CRUD methods
 * @author jacobhyphenated
 *
 * @param <T> Type of the Entity mapped to the database
 */
public interface BaseDao<T> {
	/**
	 * Get the Class of the entity.
	 * 
	 * @return the class
	 */
	public Class<T> getEntityClass();

	/**
	 * Find an entity by its primary key
	 * 
	 * @param id
	 *            the primary key
	 * @return the entity
	 */
	public T findById(long id);

	/**
	 * Load all entities.
	 * 
	 * @return the list of entities
	 */
	public List<T> findAll();
	
	/**
	 * Save or update an entity to the persistent storage
	 * @param objToSave Object (attached or detached) to be persisted
	 * @return object with updated ID if applicable
	 */
	public T save(T objToSave);
	
	/**
	 * Remove a persistent object from the database
	 * 
	 * @param objToRemove Attached object to remove
	 */
	public void remove(T objToRemove);
}
