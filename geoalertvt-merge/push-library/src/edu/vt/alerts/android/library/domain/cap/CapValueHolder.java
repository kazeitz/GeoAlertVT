/*
 * File created on Mar 3, 2014 
 *
 * Copyright 2008-2013 Virginia Polytechnic Institute and State University
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
package edu.vt.alerts.android.library.domain.cap;


/**
 * A value holder, basically a key/value pair.
 *
 * @author Michael Irwin
 */
public class CapValueHolder {

  private String valueName;
  private String value;
  
  /**
   * Gets the {@code valueName} property.
   */
  public String getValueName() {
    return valueName;
  }
  
  /**
   * Sets the {@code valueName} property.
   */
  public void setValueName(String valueName) {
    this.valueName = valueName;
  }
  
  /**
   * Gets the {@code value} property.
   */
  public String getValue() {
    return value;
  }
  
  /**
   * Sets the {@code value} property.
   */
  public void setValue(String value) {
    this.value = value;
  }
  
}
