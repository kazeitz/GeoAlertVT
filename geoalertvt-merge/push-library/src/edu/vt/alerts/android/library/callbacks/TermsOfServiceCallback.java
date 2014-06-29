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
 * A callback provided by the library to the application when the Terms of
 * Service need to be displayed to the end user.
 *
 * @author Michael Irwin
 */
public interface TermsOfServiceCallback {

  /**
   * Callback to be used when the Terms of Service have been accepted by the
   * end user.
   */
  void termsAccepted();
  
  /**
   * Callback to be used when the Terms of Service have been rejected by the
   * end user.
   */
  void termsRejected();
  
}
