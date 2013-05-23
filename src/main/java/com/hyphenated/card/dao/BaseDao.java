/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
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
	 * Merge a transient object with an existing persistent context object. Used for updating
	 * Existing objects that have left the persistence context
	 * @param objToMerge
	 * @return object with persistent context attached
	 */
	public T merge(T objToMerge);
	
	/**
	 * Remove a persistent object from the database
	 * 
	 * @param objToRemove Attached object to remove
	 */
	public void remove(T objToRemove);
}
