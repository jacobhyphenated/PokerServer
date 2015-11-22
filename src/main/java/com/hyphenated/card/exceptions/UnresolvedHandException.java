package com.hyphenated.card.exceptions;

/**
 * Created by Nitin on 10-11-2015.
 */
public class UnresolvedHandException extends IllegalStateException {

	private static final long serialVersionUID = -5608147688555206874L;

	public UnresolvedHandException(String s) {
        super(s);
    }
}
