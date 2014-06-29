/*
 * @COPYRIGHT_TEXT@
 * @LICENSE_TEXT@
 *
 * Created on Sep 24, 2010 
 */
package edu.vt.alerts.android.library.domain.cap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Representation of a CAP Alert message, based on CAP version 1.2.
 * 
 * @author Michael Irwin
 */
public class CapAlert implements Serializable {

  public enum Status {
    Actual, Exercise, 
    System, Test, Draft
  }
  
  public enum Type {
    Alert, Update, Cancel, 
    Ack, Error
  }
  
  public enum Scope {
    Public, Restricted, Private
  }
  
  private static final long serialVersionUID = 1809451188509714046L;

  private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
  
  private String identifier;
  private String sender;
  private Date sent;
  private Status status;
  private Type msgType;
  private String source;
  private Scope scope;
  private String restriction;
  private String addresses;
  private List<String> code;
  private String note;
  private String references;
  private String incidents;
  private List<CapInfo> info = new ArrayList<CapInfo>();
  
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
   * Gets the {@code sender} property.
   */
  public String getSender() {
    return sender;
  }

  /**
   * Sets the {@code sender} property.
   */
  public void setSender(String sender) {
    this.sender = sender;
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
   * Gets the {@code source} property.
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the {@code source} property.
   */
  public void setSource(String source) {
    this.source = source;
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
   * Gets the {@code restriction} property.
   */
  public String getRestriction() {
    return restriction;
  }

  /**
   * Sets the {@code restriction} property.
   */
  public void setRestriction(String restriction) {
    this.restriction = restriction;
  }

  /**
   * Gets the {@code addresses} property.
   */
  public String getAddresses() {
    return addresses;
  }

  /**
   * Sets the {@code addresses} property.
   */
  public void setAddresses(String addresses) {
    this.addresses = addresses;
  }

  /**
   * Gets the {@code code} property.
   */
  public List<String> getCode() {
    return code;
  }

  /**
   * Sets the {@code code} property.
   */
  public void setCode(List<String> code) {
    this.code = code;
  }
  
  /**
   * Gets the {@code note} property.
   */
  public String getNote() {
    return note;
  }
  
  /**
   * Sets the {@code note} property.
   */
  public void setNote(String note) {
    this.note = note;
  }

  /**
   * Gets the {@code references} property.
   */
  public String getReferences() {
    return references;
  }

  /**
   * Sets the {@code references} property.
   */
  public void setReferences(String references) {
    this.references = references;
  }

  /**
   * Gets the {@code incidents} property.
   */
  public String getIncidents() {
    return incidents;
  }

  /**
   * Sets the {@code incidents} property.
   */
  public void setIncidents(String incidents) {
    this.incidents = incidents;
  }

  /**
   * Gets the {@code datePattern} property.
   */
  public static String getDatePattern() {
    return DATE_PATTERN;
  }

  /**
   * Gets the {@code info} property.
   */
  public List<CapInfo> getInfo() {
    return info;
  }

  /**
   * Sets the {@code info} property.
   */
  public void setInfo(List<CapInfo> info) {
    System.out.println("Setting " + info.size() + " infos");
    this.info = info;
  }

  /**
   * Add a CapInfo to this CapAlert.
   * @param info
   */
  public void addInfo(CapInfo info) {
    this.info.add(info);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    if (this.identifier == null) return 0;
    return this.identifier.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CapAlert)) return false;
    if (this.identifier == null) return false;
    return this.identifier.equals(((CapAlert) obj).identifier);
  }

}
