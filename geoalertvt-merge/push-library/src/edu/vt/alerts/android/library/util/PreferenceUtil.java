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
package edu.vt.alerts.android.library.util;

import edu.vt.alerts.android.library.api.Environment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Utility class to make working with the preferences a little easier.
 * 
 * @author Michael Irwin
 */
public class PreferenceUtil {
	
	public static final String PREF_PACKAGE = "edu.vt.alerts.android.library.preferences";
	private static final String ACCEPTED_TERMS = "accepted_terms";
	private static final String SUBSCRIBER_URL = "subscriber_url";
	
	/**
	 * Were the terms of service accepted?
	 * @param context The application context
	 * @param env The environment to check
	 * @param accepted True if accepted.
	 */
	public static void setAcceptedTerms(Context context, Environment env,
	    Boolean accepted) {
		putBoolean(context, getEnvironmentKey(ACCEPTED_TERMS, env), accepted);
	}
	
	/**
	 * Have the terms of service been accepted?
	 * @param context The application context
   * @param env The environment to check
	 * @return True if accepted. Otherwise, false.
	 */
	public static Boolean hasAcceptedTerms(Context context, Environment env) {
		return getBoolean(context, getEnvironmentKey(ACCEPTED_TERMS, env), false);
	}
	
	/**
	 * Sets the URL used to check the subscriber's subscription.
	 * @param context The application context
   * @param env The environment to check
	 * @param url The URL to be used to check subscription.
	 */
	public static void setSubscriberUrl(Context context, Environment env, String url) {
		putString(context, getEnvironmentKey(SUBSCRIBER_URL, env), url);
	}
	
	/**
	 * Get the url to be used to check the device's subscription.
	 * @param context The application context
   * @param env The environment to check
	 * @return The URL to use, if one is set.  Otherwise, null.
	 */
	public static String getSubscriberUrl(Context context, 
	    Environment env) {
		String st = getString(context, getEnvironmentKey(SUBSCRIBER_URL, env));
		return (st == null || st.isEmpty()) ? null : st;
	}

	/**
   * Store a string value into persistence
   * @param context The Application context
   * @param env The VTAPNS environment 
   * @param key The preference key
   * @param value The preference value.
   */
  public static void putString(Context context, Environment env,
      String key, String value) {
    putString(context, getEnvironmentKey(key, env), value);
  }
  
  /**
   * Store a string value into persistence
   * @param context The Application context
   * @param env The VTAPNS environment 
   * @param key The preference key
   * @param value The preference value.
   */
  public static String getString(Context context, Environment env,
      String key) {
    String st = getString(context, getEnvironmentKey(key, env));
    return (st == null || st.isEmpty()) ? null : st;
  }
  
	private static String getEnvironmentKey(String key, Environment env) {
	  return env.name() + ":" + key;
	}
	
	private static String getString(Context context, String key) {
		return getSharedPreferences(context).getString(key, "");
	}
	
	private static void putString(Context context, String key, String value) {
		if (value == null)
			removeKey(context, key);
		else {
			Editor editor = getSharedPreferences(context).edit();
			editor.putString(key, value);
			editor.commit();
		}
	}
	
	private static Boolean getBoolean(Context context, String key, 
	    Boolean defaultValue) {
		return getSharedPreferences(context).getBoolean(key, defaultValue);
	}
	
	private static void putBoolean(Context context, String key, Boolean value) {
		Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private static void removeKey(Context context, String key) {
		Editor editor = getSharedPreferences(context).edit();
		editor.remove(key);
		editor.commit();
	}
	
	private static SharedPreferences getSharedPreferences(Context context) {
      return context.getSharedPreferences(PREF_PACKAGE, Context.MODE_PRIVATE);
  }

}
