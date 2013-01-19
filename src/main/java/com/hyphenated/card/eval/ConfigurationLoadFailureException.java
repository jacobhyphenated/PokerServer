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

/**
 * Exception thrown when resource loading is impossible.
 */
public class ConfigurationLoadFailureException extends RuntimeException {

	private static final long serialVersionUID = -500993246962687812L;

	public ConfigurationLoadFailureException() {
		super();
	}

	public ConfigurationLoadFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationLoadFailureException(String message) {
		super(message);
	}

	public ConfigurationLoadFailureException(Throwable cause) {
		super(cause);
	}

}
