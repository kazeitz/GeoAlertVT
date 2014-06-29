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
package edu.vt.alerts.android.library.api;

/**
 * Enum for selecting the environment that registration should take place in.
 *
 * @author Michael Irwin
 */
public enum Environment {

  SANDBOX(
    "https://register.sandbox.alerts.vt.edu/registration/subscribers",
    "https://notify.sandbox.alerts.vt.edu",
    "https://notify.sandbox.alerts.vt.edu/alerts",
    "https://register.sandbox.alerts.vt.edu/info/mobile/TermsAndConditions.html"
  ),
  PRODUCTION(  // Environment doesn't exist at this point
    "https://register.alerts.vt.edu/registration/subscribers",
    "https://notify.alerts.vt.edu",
    "https://notify.alerts.vt.edu/alerts",
    "https://register.alerts.vt.edu/info/mobile/TermsAndConditions.html"
  );
  
  private String registerUrl;
  private String alertRetrievalBaseUrl;
  private String alertsSummaryUrl;
  private String termsOfUseUrl;
  
  private Environment(String registerUrl, String alertRetrievalBaseUrl,
      String notifyUrl, String termsOfUseUrl) {
    this.registerUrl = registerUrl;
    this.alertRetrievalBaseUrl = alertRetrievalBaseUrl;
    this.alertsSummaryUrl = notifyUrl;
    this.termsOfUseUrl = termsOfUseUrl;
  }
  
  /**
   * Gets the {@code alertRetrievalBaseUrl} property.
   */
  public String getAlertRetrievalBaseUrl() {
    return alertRetrievalBaseUrl;
  }
  
  /**
   * Gets the {@code alertsSummaryUrl} property.
   */
  public String getAlertsSummaryUrl() {
    return alertsSummaryUrl;
  }
  
  /**
   * Gets the {@code registerUrl} property.
   */
  public String getRegisterUrl() {
    return registerUrl;
  }
  
  /**
   * Gets the {@code termsOfUseUrl} property.
   */
  public String getTermsOfUseUrl() {
    return termsOfUseUrl;
  }
  
}
