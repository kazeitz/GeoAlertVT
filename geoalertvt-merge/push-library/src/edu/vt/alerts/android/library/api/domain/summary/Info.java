/*
 * File created on Feb 28, 2014 
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
package edu.vt.alerts.android.library.api.domain.summary;

import java.io.Serializable;
import java.util.Date;

import edu.vt.alerts.android.library.domain.cap.CapInfo.Category;

/**
 * Describes the Info object that is placed within the Alert summary object.
 *
 * @author Michael Irwin
 */
public class Info implements Serializable {

  private static final long serialVersionUID = 468585227542262323L;
  
  private Category[] category;
  private String event;
  private String headline;
  private Date expires;
  
  /**
   * Gets the {@code category} property.
   */
  public Category[] getCategory() {
    return category;
  }
  
  /**
   * Sets the {@code category} property.
   */
  public void setCategory(Category[] category) {
    this.category = category;
  }
  
  /**
   * Gets the {@code event} property.
   */
  public String getEvent() {
    return event;
  }
  
  /**
   * Sets the {@code event} property.
   */
  public void setEvent(String event) {
    this.event = event;
  }
  
  /**
   * Gets the {@code headline} property.
   */
  public String getHeadline() {
    return headline;
  }
  
  /**
   * Sets the {@code headline} property.
   */
  public void setHeadline(String headline) {
    this.headline = headline;
  }
  
  /**
   * Gets the {@code expires} property.
   */
  public Date getExpires() {
    return expires;
  }
  
  /**
   * Sets the {@code expires} property.
   */
  public void setExpires(Date expires) {
    this.expires = expires;
  }
  
}
