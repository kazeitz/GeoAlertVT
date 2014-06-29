/*
 * File created on Mar 3, 2014 
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
package edu.vt.alerts.android.library.util.marshal;

/**
 * An unmarshaller that can convert data from a String into its Java 
 * representation.
 *
 * @author Michael Irwin
 */
public interface Unmarshaller<T> {

  /**
   * The mime type that this unmarshaller knows how to convert
   * @return The mime type (example: application/json)
   */
  String getMimeType();
  
  /**
   * Perform the unmarshalling from the string data into the Java object
   * @param data The string data
   * @return A Java representation of the data.
   */
  T convertData(String data);
  
}
