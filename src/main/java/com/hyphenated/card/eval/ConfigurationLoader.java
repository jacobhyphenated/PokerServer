/*
 * This file is part of fisth, an FSM-based Texas Hold'em hand evaluator.
 * Copyright (C) 2010 Robert Strack <robert.strack@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.hyphenated.card.eval;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.google.common.io.Closeables;

/**
 * Utility class with methods appropriate for resource reading.
 */
public class ConfigurationLoader {

	/**
	 * Reads resources.
	 * 
	 * @param <T>
	 *            return type
	 * @param name
	 *            resource URI
	 * @return loaded resource
	 * @throws ConfigurationLoadFailureException
	 *             if resources cannot be loaded
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T loadObjectFromResource(String name)
			throws ConfigurationLoadFailureException {
		try {
			InputStream resourceStream = ConfigurationLoader.class.getResourceAsStream(name);
			if (resourceStream != null) {
				try {
					ObjectInputStream objectStream = new ObjectInputStream(resourceStream);
					try {
						return (T) objectStream.readObject();
					} finally {
						Closeables.closeQuietly(objectStream);
					}
				} finally {
					Closeables.closeQuietly(resourceStream);
				}
			} else {
				throw new ConfigurationLoadFailureException("resource " + name + " does not exist");
			}
		} catch (IOException e) {
			throw new ConfigurationLoadFailureException("cannot read resource " + name, e);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationLoadFailureException("unknown class for resource " + name, e);
		}
	}

}
