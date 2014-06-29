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
package edu.vt.alerts.android.library.tasks;

/**
 * Encapsulates the results from any of the AsyncTasks that are used in the
 * library.  Allows either results to be returned or an exception.
 * 
 * @author Michael Irwin
 */
public class TaskResult<T> {

	private final T result;
	private final Exception exception;
	
	public TaskResult(T result, Exception exception) {
		this.result = result;
		this.exception = exception;
	}
	
	public boolean exceptionThrown() {
		return this.exception != null;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public T getResult() {
		return result;
	}
	
}
