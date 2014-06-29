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
package edu.vt.alerts.android.library.demo;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

/**
 * DESCRIBE THE TYPE HERE.
 *
 * @author Michael Irwin
 */
public class BaseActivity extends Activity {
  
  private ProgressDialog dialog;

  /**
   * {@inheritDoc}
   */
  public void exceptionThrown(Exception e) {
    checkDialog();
    Log.e("demo", "An exception was thrown - ", e);
    
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    
    new AlertDialog.Builder(this)
      .setTitle("Oops! An error occurred")
      .setMessage(sw.toString())
      .setCancelable(true)
      .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog,int id) {
          dialog.dismiss();
        }
      }).create().show();
  }
  
  protected void displayDialog(String title) {
    dialog = ProgressDialog.show(this, "", title, true);
    dialog.show();
  }
  
  protected void checkDialog() {
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }
}
