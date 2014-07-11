package com.yelinaung.programmerexcuses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.Random;

public class MyActivity extends Activity {

  @InjectView(R.id.quote_text) TextView mQuoteText;
  @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  public static final String URL = "http://pe-api.herokuapp.com";
  private int[] myColors;
  private SharePrefUtils sharePrefUtils;
  private String randomQuote;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    mSwipeRefreshLayout.setColorSchemeColors(R.color.blue, R.color.red, R.color.yellow,
        R.color.green);

    sharePrefUtils = SharePrefUtils.getInstance(MyActivity.this);

    String[] randomQuotes = getResources().getStringArray(R.array.excuses);
    randomQuote = randomQuotes[new Random().nextInt(randomQuotes.length)];
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
        new GetQuote().execute();
      }
    });
  }

  private class GetQuote extends AsyncTask<Void, Void, String> {
    String data = "";

    @Override protected void onPreExecute() {
      super.onPreExecute();
      mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override protected String doInBackground(Void... params) {
      mSwipeRefreshLayout.setRefreshing(true);
      Ion.with(MyActivity.this)
          .load(URL)
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override public void onCompleted(Exception e, JsonObject result) {
              data = result.get("message").getAsString();
              sharePrefUtils.saveQuote(data);
              Log.i("_res", data);
              runOnUiThread(new Runnable() {
                @Override public void run() {
                  mQuoteText.setText(data);
                }
              });
            }
          });
      return data;
    }

    @Override protected void onPostExecute(String s) {
      super.onPostExecute(s);
      mSwipeRefreshLayout.setRefreshing(false);
      mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.my, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
