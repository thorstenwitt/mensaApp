package de.thorstenwitt.mensaapp.activity;

import java.io.IOException;

import de.thorstenwitt.mensaapp.parser.LunchParser;
import de.thorstenwitt.mensaapp.R;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
	
	ImageView iv;
	ProgressBar pb;
	TextView tv;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		iv = (ImageView) findViewById(R.id.splashImage);
		pb = (ProgressBar) findViewById(R.id.splashProgress);
		tv = (TextView) findViewById(R.id.splashText);
		startParseTask();
		
	}

	private void startParseTask() {
		if(isOnline()) {
			parseTask pt = new parseTask();
			pt.execute("");
		}
		else {
			tv.setText("Keine Internetverbindung");
			pb.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, 0, Menu.NONE,"Speisekarte aktualisieren");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			Intent i = new Intent(getApplicationContext(), SplashActivity.class);
			startActivity(i);
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	    return cm.getActiveNetworkInfo() != null && 
	       cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	private class parseTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
	    }
		@Override
		protected String doInBackground(String... params) {
			LunchParser lp = LunchParser.getInstance();
			try {
				lp.parse();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG);
			}
		    return null;
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				Intent i = new Intent(getApplicationContext(), MenuActivity.class);
				startActivity(i);
				finish();

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
