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
package edu.vt.alerts.android.library.demo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.vt.alerts.android.library.api.domain.summary.AlertSummary;

/**
 * DESCRIBE THE TYPE HERE.
 *
 * @author Michael Irwin
 */
public class AlertSummaryAdapter extends ArrayAdapter<AlertSummary> {

  /**
   * @param context
   * @param resource
   * @param objects
   */
  public AlertSummaryAdapter(Context context, AlertSummary[] objects) {
    super(context, android.R.layout.simple_list_item_1, objects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView view = (TextView) super.getView(position, convertView, parent);
    view.setText(getItem(position).getInfo().get(0).getHeadline());
    return view;
  }
  
}
