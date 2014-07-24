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
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.util.Random;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MyActivity extends Activity {

  // View Injections
  @InjectView(R.id.quote_text) TextView mQuoteText;
  @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  private int[] myColors;
  private SharePrefUtils sharePrefUtils;
  private final AsyncHttpClient client = new AsyncHttpClient();
  private ConnManager connManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    connManager = new ConnManager(MyActivity.this);

    mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.red, R.color.yellow,
        R.color.green);
    mSwipeRefreshLayout.setEnabled(true);

    sharePrefUtils = SharePrefUtils.getInstance(MyActivity.this);

    String[] randomQuotes = getResources().getStringArray(R.array.excuses);
    String randomQuote = randomQuotes[new Random().nextInt(randomQuotes.length)];

    // if the app is launched for the first time, then just get the random quote
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
        if (connManager.isConnected()) {
          getQuoteFromApi();
        } else {
          Toast.makeText(MyActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  // Doing http stuff here
  private void getQuoteFromApi() {
    client.get(getString(R.string.api), new JsonHttpResponseHandler() {

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

  public class ConnManager {
    private final Context mContext;

    public ConnManager(Context context) {
      this.mContext = context;
    }

    public boolean isConnected() {
      ConnectivityManager connectivity =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

      if (connectivity != null) {
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
          for (NetworkInfo anInfo : info) {
            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
              return true;
            }
          }
        }
      }
      return false;
    }
  }

  @SuppressWarnings("UnusedDeclaration") @OnClick(R.id.share_btn)
  public void share() {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT,
        String.format(getString(R.string.share_text), mQuoteText.getText()));
    sendIntent.setType("text/plain");
    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_to)));
  }
}
