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
import java.util.List;

import edu.vt.alerts.android.library.domain.cap.CapAlert.Scope;
import edu.vt.alerts.android.library.domain.cap.CapAlert.Status;
import edu.vt.alerts.android.library.domain.cap.CapAlert.Type;

/**
 * Defines an Alert summary message.
 *
 * @author Michael Irwin
 */
public class AlertSummary implements Serializable {

  private static final long serialVersionUID = 3988207906953977034L;
  
  private String identifier;
  private Type msgType;
  private Date sent;
  private Status status;
  private Scope scope;
  private String url;
  private List<Info> info;
  
  /**
   * Gets the {@code identifier} property.
   */
  public String getIdentifier() {
    return identifier;
  }
  
  /**
   * Sets the {@code identifier} property.
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  
  /**
   * Gets the {@code sent} property.
   */
  public Date getSent() {
    return sent;
  }
  
  /**
   * Sets the {@code sent} property.
   */
  public void setSent(Date sent) {
    this.sent = sent;
  }
  
  /**
   * Gets the {@code msgType} property.
   */
  public Type getMsgType() {
    return msgType;
  }
  
  /**
   * Sets the {@code msgType} property.
   */
  public void setMsgType(Type msgType) {
    this.msgType = msgType;
  }
  
  /**
   * Gets the {@code status} property.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the {@code status} property.
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Gets the {@code scope} property.
   */
  public Scope getScope() {
    return scope;
  }

  /**
   * Sets the {@code scope} property.
   */
  public void setScope(Scope scope) {
    this.scope = scope;
  }

  /**
   * Gets the {@code url} property.
   */
  public String getUrl() {
    return url;
  }
  
  /**
   * Sets the {@code url} property.
   */
  public void setUrl(String url) {
    this.url = url;
  }
  
  /**
   * Gets the {@code info} property.
   */
  public List<Info> getInfo() {
    return info;
  }
  
  /**
   * Sets the {@code info} property.
   */
  public void setInfo(List<Info> info) {
    this.info = info;
  }
  
}
