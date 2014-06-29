/*
 * File created on Apr 4, 2013 
 *
 * Copyright 2008-2011 Virginia Polytechnic Institute and State University
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

import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Contains information about a particular CAP Alert.
 *
 * @author Michael Irwin
 */
public class CapInfo {

  public enum Category {
    Geo, Met, Safety, Security, Rescue, Fire, Health, Env, Transport, Infra,
    CBRNE, Other
  }
  
  public enum ResponseType {
    Shelter, Evacuate, Prepare, Execute, Avoid, Monitor, Assess, AllClear, None
  }
  
  public enum Urgency  {
    Immediate, Expected, Future, Past, Unknown
  }
  
  public enum Severity {
    Extreme, Severe, Moderate, Minor, Unknown
  }
  
  public enum Certainty {
    Observed, Likely, Possible, Unlikely, Unknown
  }
  
  private String language = "en-US";
  private List<Category> category;
  private String event;
  private List<ResponseType> responseType;
  private Urgency urgency;
  private Severity severity;
  private Certainty certainty;
  private String audience;
  private List<CapValueHolder> eventCode;
  private Date effective;
  private Date onset;
  private Date expires;
  private String senderName;
  private String headline;
  private String description;
  private String instruction;
  private URI web;
  private String contact;
  private List<CapValueHolder> parameter;
  private List<CapResource> resource;
  private List<CapArea> area;
  
  /**
   * Gets the {@code language} property.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets the {@code language} property.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Gets the {@code category} property.
   */
  public List<Category> getCategory() {
    return category;
  }

  /**
   * Sets the {@code category} property.
   */
  public void setCategory(List<Category> category) {
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
   * Gets the {@code responseType} property.
   */
  public List<ResponseType> getResponseType() {
    return responseType;
  }
  
  /**
   * Sets the {@code responseType} property.
   */
  public void setResponseType(List<ResponseType> responseType) {
    this.responseType = responseType;
  }

  /**
   * Gets the {@code urgency} property.
   */
  public Urgency getUrgency() {
    return urgency;
  }

  /**
   * Sets the {@code urgency} property.
   */
  public void setUrgency(Urgency urgency) {
    this.urgency = urgency;
  }

  /**
   * Gets the {@code severity} property.
   */
  public Severity getSeverity() {
    return severity;
  }

  /**
   * Sets the {@code severity} property.
   */
  public void setSeverity(Severity severity) {
    this.severity = severity;
  }

  /**
   * Gets the {@code certainty} property.
   */
  public Certainty getCertainty() {
    return certainty;
  }

  /**
   * Sets the {@code certainty} property.
   */
  public void setCertainty(Certainty certainty) {
    this.certainty = certainty;
  }

  /**
   * Gets the {@code audience} property.
   */
  public String getAudience() {
    return audience;
  }

  /**
   * Sets the {@code audience} property.
   */
  public void setAudience(String audience) {
    this.audience = audience;
  }

  /**
   * Gets the {@code eventCode} property.
   */
  public List<CapValueHolder> getEventCode() {
    return eventCode;
  }
  
  /**
   * Sets the {@code eventCode} property.
   */
  public void setEventCode(List<CapValueHolder> eventCode) {
    this.eventCode = eventCode;
  }

  /**
   * Gets the {@code effective} property.
   */
  public Date getEffective() {
    return effective;
  }

  /**
   * Sets the {@code effective} property.
   */
  public void setEffective(Date effective) {
    this.effective = effective;
  }

  /**
   * Gets the {@code onset} property.
   */
  public Date getOnset() {
    return onset;
  }

  /**
   * Sets the {@code onset} property.
   */
  public void setOnset(Date onset) {
    this.onset = onset;
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

  /**
   * Gets the {@code senderName} property.
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Sets the {@code senderName} property.
   */
  public void setSenderName(String senderName) {
    this.senderName = senderName;
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
   * Gets the {@code description} property.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the {@code description} property.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the {@code instruction} property.
   */
  public String getInstruction() {
    return instruction;
  }

  /**
   * Sets the {@code instruction} property.
   */
  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  /**
   * Gets the {@code web} property.
   */
  public URI getWeb() {
    return web;
  }

  /**
   * Sets the {@code web} property.
   */
  public void setWeb(URI web) {
    this.web = web;
  }

  /**
   * Gets the {@code contact} property.
   */
  public String getContact() {
    return contact;
  }

  /**
   * Sets the {@code contact} property.
   */
  public void setContact(String contact) {
    this.contact = contact;
  }

  /**
   * Gets the {@code parameter} property.
   */
  public List<CapValueHolder> getParameter() {
    return parameter;
  }
  
  /**
   * Sets the {@code parameter} property.
   */
  public void setParameter(List<CapValueHolder> parameter) {
    this.parameter = parameter;
  }
  
  /**
   * Gets the {@code resource} property.
   */
  public List<CapResource> getResource() {
    return resource;
  }
  
  /**
   * Sets the {@code resource} property.
   */
  public void setResource(List<CapResource> resource) {
    this.resource = resource;
  }
  
  /**
   * Gets the {@code area} property.
   */
  public List<CapArea> getArea() {
    return area;
  }
  
  /**
   * Sets the {@code area} property.
   */
  public void setArea(List<CapArea> area) {
    this.area = area;
  }
  
}
