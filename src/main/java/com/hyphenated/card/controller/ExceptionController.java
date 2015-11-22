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
package com.hyphenated.card.controller;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handling controller/controller advice.
 * <br /><br />
 * This class covers all other controller classes, and it will handle exceptions thrown in any of them.
 * This class will handle specific (and generic) exceptions and respond with the appropriate
 * HTTP Response Code as well as the appropriate JSON error message. 
 * 
 * @author jacobhyphenated
 */
@ControllerAdvice
public class ExceptionController {

	private static Logger log = LoggerFactory.getLogger(ExceptionController.class);

	/**
	 * Handles any Illegal State Exception from a controller method
	 * @param e The exception that was thrown
	 * @return JSON Map with error messages specific to the illegal state exception thrown
	 */
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public @ResponseBody Map<String,String> handleIOExcpetion(IllegalStateException e){
		Map<String,String> error = new HashMap<String, String>();
		error.put("error","Opperation not currently allowed.");
		error.put("errorDetails", e.getMessage());
		log.error("Error: " + e.getMessage());
		return error;
	}
	
	/**
	 * MissingServletRequestParameterException handler.  This exception will occur whenever
	 * a controller method does not receive a required request parameter.
	 * @param e Exception being thrown
	 * @return JSON Error messages specific to the missing parameter exception.
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody Map<String,String> handleBadParameterExcpetion(MissingServletRequestParameterException e){
		Map<String,String> error = new HashMap<String, String>();
		error.put("error","Required parameter not present.");
		error.put("errorDetails", "The parameter " + e.getParameterName() + " is required and not present in the request.");
		log.error("Error: " + e.getMessage());
		return error;
	}
	
	/**
	 * Catch all exception handler for any other exception thrown in the controller.
	 * @param e Exception that was thrown.
	 * @return A more generic error message that does not give away any implementation details
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public @ResponseBody Map<String,String> handleExcpetion( Exception e){
		Map<String,String> error = new HashMap<String, String>();
		error.put("error","There was an error on the server");
		error.put("errorDetails", "Make sure you request was formatted correctly, and all parameters were correct. " +
				"If you believe you received this message by mistake, you are probably out of luck.");
		log.error("Error: " + e.getMessage());
		log.error("Class? " + e.getClass());
		return error;
	}
}
