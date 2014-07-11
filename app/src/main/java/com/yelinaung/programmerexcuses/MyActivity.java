package com.yelinaung.programmerexcuses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import java.util.Random;

public class MyActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

  @InjectView(R.id.quote_text) TextView mQuoteText;
  @InjectView(R.id.quote_background) SwipeRefreshLayout mQuoteBackground;

  public static final String URL = "http://pe-api.herokuapp.com";
  private int[] myColors;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    SharePrefUtils sharePrefUtils = SharePrefUtils.getInstance(MyActivity.this);

    String[] randomQuotes = getResources().getStringArray(R.array.excuses);
    String randomQuote = randomQuotes[new Random().nextInt(randomQuotes.length)];
    if (sharePrefUtils.isFirstTime()) {
      sharePrefUtils.saveQuote(randomQuote);
      mQuoteText.setText(randomQuote);
      sharePrefUtils.noMoreFirstTime();
    }

    myColors = getResources().getIntArray(R.array.my_colors);
    mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
    mQuoteText.setText(sharePrefUtils.getQuote());
  }

  @Override public void onRefresh() {
    new GetQuote().execute();
    mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
  }

  private class GetQuote extends AsyncTask<String, String, String> {
    String data = "";

    @Override protected String doInBackground(String... params) {
      Ion.with(MyActivity.this)
          .load(URL)
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
            @Override public void onCompleted(Exception e, JsonObject result) {
              data = result.get("message").getAsString();
              Log.i("_res", data);
              runOnUiThread(new Runnable() {
                @Override public void run() {
                  mQuoteText.setText(data + ".");
                }
              });
            }
          });
      return data;
    }

    @Override protected void onPostExecute(String s) {
      super.onPostExecute(s);
      Log.i("_res", s);
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
