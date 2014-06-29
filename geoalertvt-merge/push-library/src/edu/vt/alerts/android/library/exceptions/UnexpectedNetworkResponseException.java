/*
 * File created on Feb 24, 2014 
 *
 * Copyright 2008-2014 Virginia Polytechnic Institute and State University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.vt.alerts.android.library.exceptions;

/**
 * An exception used to indicate that an unexpected response was provided from
 * the network.
 * 
 * @author Michael Irwin
 */
public class UnexpectedNetworkResponseException extends Exception {

	private static final long serialVersionUID = 5928826400685096854L;

	private Integer statusCode;
	private String status;
	
	public UnexpectedNetworkResponseException(String message,
	    Integer statusCode, String status) {
	  super(message);
		this.statusCode = statusCode;
		this.status = status;
	}
	
	/**
	 * Get the status string for the response received. 
	 * @return The status string for the response received.
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Get the status code for the unexpected response received.
	 * @return The numerical status code for the response.
	 */
	public Integer getStatusCode() {
		return statusCode;
	}
	
}
