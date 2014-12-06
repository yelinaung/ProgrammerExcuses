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
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.squareup.otto.Subscribe;
import com.yelinaung.programmerexcuses.event.BusProvider;
import com.yelinaung.programmerexcuses.event.OnSwipeDownEvent;
import com.yelinaung.programmerexcuses.event.QuoteDownloadFailEvent;
import com.yelinaung.programmerexcuses.event.QuoteDownloadedEvent;
import com.yelinaung.programmerexcuses.model.Excuse;
import com.yelinaung.programmerexcuses.widget.SecretTextView;
import java.util.Random;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyActivity extends Activity {

  // View Injections
  @InjectView(R.id.quote_text) SecretTextView mQuoteText;
  @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  private int[] myColors;
  private SharePrefUtils sharePrefUtils;
  private ConnManager connManager;
  private RestAdapter restAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.api))
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .build();

    connManager = new ConnManager(MyActivity.this);

    mQuoteText.show();

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
          BusProvider.getInstance().post(new OnSwipeDownEvent());
        } else {
          mSwipeRefreshLayout.setRefreshing(false);
          Toast.makeText(MyActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Subscribe public void setQuoteToUiEvent(QuoteDownloadedEvent event) {
    if (mQuoteText != null) {
      mQuoteText.setText(event.message);
    }
  }

  @Subscribe public void QuoteDownloadFail(QuoteDownloadFailEvent event) {
    Toast.makeText(this, event.failMsg, Toast.LENGTH_SHORT).show();
    mSwipeRefreshLayout.setRefreshing(false);
    mQuoteText.setText(mQuoteText.getText());
  }

  @Subscribe public void onDownloadQuote(OnSwipeDownEvent event) {
    getQuoteFromApi();
  }

  // Doing http stuff here
  private void getQuoteFromApi() {
    mSwipeRefreshLayout.setRefreshing(true);

    ExcuseService service = restAdapter.create(ExcuseService.class);
    service.getExcuse(new Callback<Excuse>() {
      @Override public void success(Excuse excuse, Response response) {
        mSwipeRefreshLayout.setRefreshing(false);
        mQuoteText.show();
        BusProvider.getInstance().post(new QuoteDownloadedEvent(excuse.message));
        sharePrefUtils.saveQuote(excuse.message); // save to pref
        mQuoteText.setText(excuse.message);
        mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
      }

      @Override public void failure(RetrofitError error) {
        mSwipeRefreshLayout.setRefreshing(false);
        // TODO proper error handling
        //BusProvider.getInstance().post(new QuoteDownloadFailEvent(getString(R.string.timeout)));
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

  @Override protected void onResume() {
    super.onResume();
    BusProvider.getInstance().register(this);
  }

  @Override protected void onPause() {
    super.onPause();
    BusProvider.getInstance().unregister(this);
  }
}