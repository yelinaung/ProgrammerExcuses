/*
 * Copyright 2014 Ye Lin Aung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yelinaung.programmerexcuses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefUtils {
  private static SharePrefUtils pref;
  private final SharedPreferences mSharePreferences;
  private final SharedPreferences.Editor mEditor;

  private static final String PREF_NAME = "mPref";
  private static final String FIRST_TIME = "first_time";
  private static final String SAVED_QUOTE = "saved_quote";

  @SuppressLint("CommitPrefEdits") private SharePrefUtils(Context context) {
    mSharePreferences = context.getSharedPreferences(PREF_NAME, 0);
    mEditor = mSharePreferences.edit();
  }

  /**
   * @param context Accepts context as a param
   */
  public static SharePrefUtils getInstance(Context context) {
    if (pref == null) {
      pref = new SharePrefUtils(context);
    }
    return pref;
  }

  /**
   * Get true if the app is launched first time
   */
  public boolean isFirstTime() {
    return mSharePreferences.getBoolean(FIRST_TIME, true);
  }

  /**
   * Once app is launched for the first time, it's no longer first time
   */
  public void noMoreFirstTime() {
    mEditor.putBoolean(FIRST_TIME, false).commit();
  }

  public String getQuote() {
    return mSharePreferences.getString(SAVED_QUOTE, null);
  }

  /**
   * @param quote The quote to save in the preference
   */
  public void saveQuote(String quote) {
    mEditor.putString(SAVED_QUOTE, quote).commit();
  }
}
