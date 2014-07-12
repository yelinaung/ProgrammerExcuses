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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.util.Random;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MyActivity extends Activity {

  @InjectView(R.id.quote_text) TextView mQuoteText;
  @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  public static final String URL = "http://pe-api.herokuapp.com";
  private int[] myColors;
  private SharePrefUtils sharePrefUtils;
  private AsyncHttpClient client = new AsyncHttpClient();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.red, R.color.yellow,
        R.color.green);
    mSwipeRefreshLayout.setEnabled(true);

    sharePrefUtils = SharePrefUtils.getInstance(MyActivity.this);

    String[] randomQuotes = getResources().getStringArray(R.array.excuses);
    String randomQuote = randomQuotes[new Random().nextInt(randomQuotes.length)];

    // if first time, then just get the random quote
    if (sharePrefUtils.isFirstTime()) {
      mQuoteText.setText(randomQuote);
      sharePrefUtils.noMoreFirstTime();
    } else {
      mQuoteText.setText(sharePrefUtils.getQuote());
    }

    // In case, if saved quote is null, just set a random quote
    if (sharePrefUtils.getQuote() == null) {
      mQuoteText.setText(randomQuote);
    }

    myColors = getResources().getIntArray(R.array.my_colors);
    mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);

    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        getQuote();
      }
    });
  }

  private void getQuote() {
    client.get(URL, new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        // Show Refreshing Progress at first
        mSwipeRefreshLayout.setRefreshing(true);
      }

      @Override public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        // Don't show Refreshing Progress if succeed
        mSwipeRefreshLayout.setRefreshing(false);
        try {
          String msg = response.get("message").toString();
          sharePrefUtils.saveQuote(msg); // save to pref
          mQuoteText.setText(msg);
          mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
