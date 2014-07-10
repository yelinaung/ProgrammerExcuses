package com.yelinaung.programmerexcuses;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.util.Random;

public class MyActivity extends Activity {

  @InjectView(R.id.quote_text) TextView mQuoteText;
  @InjectView(R.id.quote_background) RelativeLayout mQuoteBackground;

  public static final String URL = "https://apify.heroku.com/api/excuses.json";
  private OkHttpClient client = new OkHttpClient();
  private int[] myColors;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    ButterKnife.inject(this);

    myColors = getResources().getIntArray(R.array.my_colors);

    mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
    mQuoteText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new GetQuote().execute();
        mQuoteBackground.setBackgroundColor(myColors[new Random().nextInt(myColors.length)]);
      }
    });
  }

  private String run(String url) throws IOException {
    return "";
  }

  private class GetQuote extends AsyncTask<Void, Void, String> {

    @Override protected String doInBackground(Void... params) {
      try {
        return run(URL);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(String s) {
      super.onPostExecute(s);
      mQuoteText.setText(s);
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
