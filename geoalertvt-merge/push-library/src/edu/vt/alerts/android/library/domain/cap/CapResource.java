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

/**
 * Defines the Resource element with the Alert.Info element, according to the
 * CAP v.1.2 protocol.
 *
 * @author Michael Irwin
 */
public class CapResource {

  private String resourceDesc;
  private String mimeType;
  private Long size;
  private URI uri;
  private String derefUri;
  private String digest;
  
  /**
   * Gets the {@code resourceDesc} property.
   */
  public String getResourceDesc() {
    return resourceDesc;
  }

  /**
   * Sets the {@code resourceDesc} property.
   */
  public void setResourceDesc(String resourceDesc) {
    this.resourceDesc = resourceDesc;
  }

  /**
   * Gets the {@code mimeType} property.
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Sets the {@code mimeType} property.
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Gets the {@code size} property.
   */
  public Long getSize() {
    return size;
  }

  /**
   * Sets the {@code size} property.
   */
  public void setSize(Long size) {
    this.size = size;
  }

  /**
   * Gets the {@code uri} property.
   */
  public URI getUri() {
    return uri;
  }

  /**
   * Sets the {@code uri} property.
   */
  public void setUri(URI uri) {
    this.uri = uri;
  }

  /**
   * Gets the {@code derefUri} property.
   */
  public String getDerefUri() {
    return derefUri;
  }

  /**
   * Sets the {@code derefUri} property.
   */
  public void setDerefUri(String derefUri) {
    this.derefUri = derefUri;
  }

  /**
   * Gets the {@code digest} property.
   */
  public String getDigest() {
    return digest;
  }

  /**
   * Sets the {@code digest} property.
   */
  public void setDigest(String digest) {
    this.digest = digest;
  }
  
}
