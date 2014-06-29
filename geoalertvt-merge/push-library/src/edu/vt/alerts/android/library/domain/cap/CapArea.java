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

import java.math.BigDecimal;
import java.util.List;

/**
 * Describes the Area element within the CAP v.1.2 protocol.
 *
 * @author Michael Irwin
 */
public class CapArea {

  private String areaDesc;
  private List<String> polygon;
  private List<String> circle;
  private List<CapValueHolder> geocode;
  private BigDecimal altitude;
  private BigDecimal ceiling;
  
  /**
   * Gets the {@code areaDesc} property.
   */
  public String getAreaDesc() {
    return areaDesc;
  }
  
  /**
   * Sets the {@code areaDesc} property.
   */
  public void setAreaDesc(String areaDesc) {
    this.areaDesc = areaDesc;
  }
  
  /**
   * Gets the {@code polygon} property.
   */
  public List<String> getPolygon() {
    return polygon;
  }
  
  /**
   * Sets the {@code polygon} property.
   */
  public void setPolygon(List<String> polygon) {
    this.polygon = polygon;
  }
  
  /**
   * Gets the {@code circle} property.
   */
  public List<String> getCircle() {
    return circle;
  }
  
  /**
   * Sets the {@code circle} property.
   */
  public void setCircle(List<String> circle) {
    this.circle = circle;
  }
  
  /**
   * Gets the {@code geocode} property.
   */
  public List<CapValueHolder> getGeocode() {
    return geocode;
  }
  
  /**
   * Sets the {@code geocode} property.
   */
  public void setGeocode(List<CapValueHolder> geocode) {
    this.geocode = geocode;
  }
  
  /**
   * Gets the {@code altitude} property.
   */
  public BigDecimal getAltitude() {
    return altitude;
  }
  
  /**
   * Sets the {@code altitude} property.
   */
  public void setAltitude(BigDecimal altitude) {
    this.altitude = altitude;
  }
  
  /**
   * Gets the {@code ceiling} property.
   */
  public BigDecimal getCeiling() {
    return ceiling;
  }
  
  /**
   * Sets the {@code ceiling} property.
   */
  public void setCeiling(BigDecimal ceiling) {
    this.ceiling = ceiling;
  }

}
