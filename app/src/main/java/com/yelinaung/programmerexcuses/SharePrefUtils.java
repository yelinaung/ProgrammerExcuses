package com.yelinaung.programmerexcuses;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefUtils {
  private static SharePrefUtils pref;
  private SharedPreferences mSharePreferences;
  private SharedPreferences.Editor mEditor;

  private static final String PREF_NAME = "mPref";
  private static final String FIRST_TIME = "first_time";
  private static final String SAVED_QUOTE = "saved_quote";

  public SharePrefUtils(Context context) {
    mSharePreferences = context.getSharedPreferences(PREF_NAME, 0);
    mEditor = mSharePreferences.edit();
  }

  public static SharePrefUtils getInstance(Context context) {
    if (pref == null) {
      pref = new SharePrefUtils(context);
    }
    return pref;
  }

  public boolean isFirstTime() {
    return mSharePreferences.getBoolean(FIRST_TIME, true);
  }

  public void noMoreFirstTime() {
    mEditor.putBoolean(FIRST_TIME, false).commit();
  }

  public String getQuote() {
    return mSharePreferences.getString(PREF_NAME, null);
  }

  public void saveQuote(String quote) {
    mEditor.putString(SAVED_QUOTE, quote).commit();
  }
}
