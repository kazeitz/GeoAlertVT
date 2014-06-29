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
package edu.vt.alerts.android.library.callbacks;

/**
 * Callback provided during registration that is used 
 *
 * @author Michael Irwin
 */
public interface RegistrationCallback {

  /**
   * Callback method used when the Terms of Service need to be displayed to the
   * user.
   * @param termsOfService The terms of service to be displayed. The data may
   * contain HTML elements, so should be displayed in a way in which the 
   * formatting is preserved.
   * @param callback A callback to be used when the terms of use are accepted
   * or rejected.
   */
  void displayTermsOfService(String termsOfService, 
      TermsOfServiceCallback callback);
  
  /**
   * Callback method used when registration has been requested when the current
   * device is already registered to the VT-APNS system.
   */
  void alreadyRegistered();
  
  /**
   * Callback method used when registration was aborted.  
   * Typically, this occurs only when the Terms of Service was rejected, 
   * effectively canceling registration.
   */
  void registrationAborted();
  
  /**
   * Callback used when the device has successfully registered with the VT
   * Alerts Push Notification System.
   */
  void registrationSuccessful();
  
  /**
   * Callback used when an unrecoverable exception is thrown.
   * @param exception The exception that was thrown.
   */
  void exceptionThrown(Exception exception);

}
