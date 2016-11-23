package de.thorstenwitt.mensaapp.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import de.thorstenwitt.mensaapp.parser.LunchParser;
import de.thorstenwitt.mensaapp.R;
import de.thorstenwitt.mensaapp.businessobject.Lunch;
import de.thorstenwitt.mensaapp.businessobject.LunchOffer;

public class MenuActivity extends AppCompatActivity implements
		CapabilityApi.CapabilityListener,
		MessageApi.MessageListener,
		DataApi.DataListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {
	
	public TextView lbAmount;
	public Button btReset;
	public Spinner spDate;
	public ListView lstLunch;
	public MenuListAdapter menuListAdapter;
	public float totalAmount = 0.0f;
	public ArrayList<LunchOffer> myLunchData;
	int selectedDay=0;
	private int priceCategory = Lunch.PRICE_STUDENT;

	// Send DataItems.
	private ScheduledExecutorService mGeneratorExecutor;
	private ScheduledFuture<?> mDataItemGeneratorFuture;



	private GoogleApiClient mGoogleApiClient;
	private boolean mResolvingError = false;
	//Request code for launching the Intent to resolve Google
	//
	// Play services errors.
	private static final int REQUEST_RESOLVE_ERROR = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		lbAmount = (TextView) findViewById(R.id.textViewBetrag);
		btReset = (Button) findViewById(R.id.buttonReset);
		spDate = (Spinner) findViewById(R.id.spinnerDay);
		spDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedDay = position;
				setListAdapter(selectedDay);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		lstLunch = (ListView) findViewById(R.id.listViewLunch);
		LunchParser lp = LunchParser.getInstance();

		if(savedInstanceState!=null) {
			myLunchData = savedInstanceState.getParcelableArrayList("menues");
		}
		else {
			myLunchData = lp.getLunchDataForStralsund();
		}
		ArrayList<String> lunchDates = new ArrayList<String>();
		for(LunchOffer t:myLunchData) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date lunchOfferDate = sdf.parse(t.getMydate());
				String localizedLunchDate = DateFormat.getDateInstance().format(lunchOfferDate);
				lunchDates.add(localizedLunchDate);
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG);
			}

		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lunchDates);
		spDate.setAdapter(spinnerAdapter);


		updateAmountLabel(totalAmount);
		btReset.setText("Reset");
		btReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				totalAmount =0.0f;
				updateAmountLabel(totalAmount);
				
			}
		});

		mGeneratorExecutor = new ScheduledThreadPoolExecutor(1);
		mDataItemGeneratorFuture = mGeneratorExecutor.scheduleWithFixedDelay(
				new DataItemGenerator(), 1, 5, TimeUnit.SECONDS);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		if (!mResolvingError) {
			mGoogleApiClient.connect();
		}
		//if(mGoogleApiClient.isConnected()){
			Bitmap bitmap = Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
			sendPhoto(toAsset(bitmap));
			//sendText("Tach, Post!!!");
			//sendText("22222222222222222");
		//}
	}


	public void updateAmountLabel(float amount) {
		lbAmount.setText("Gesamtbetrag: "+NumberFormat.getCurrencyInstance().format(amount));
	}
	public void setListAdapter(int selectedDay){
		final ArrayList<Lunch> selectedLunches = myLunchData.get(selectedDay).getLunchList();
		menuListAdapter = new MenuListAdapter(this.getApplicationContext(), selectedLunches, priceCategory);
		lstLunch.setAdapter(menuListAdapter);
		lstLunch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				if (priceCategory == Lunch.PRICE_STUDENT	){
					totalAmount += selectedLunches.get(position).getPriceStud();
					updateAmountLabel(totalAmount);
				}else if (priceCategory == Lunch.PRICE_EMPLOYEE){
					totalAmount += selectedLunches.get(position).getPriceEmp();
					updateAmountLabel(totalAmount);
				}else{
					totalAmount += selectedLunches.get(position).getPriceGuest();
					updateAmountLabel(totalAmount);
				}
			}
		});
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putParcelableArrayList("menues", myLunchData);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, 0, Menu.NONE,"Speisekarte aktualisieren");
		
		SubMenu submenu = menu.addSubMenu(Menu.NONE, 1, Menu.NONE,"Preiskategorie auswaehlen");
		menu.add(Menu.NONE, 2, Menu.NONE,"Beenden");
		submenu.add(Menu.NONE, 11, Menu.NONE,"Studenten");
		submenu.add(Menu.NONE, 12, Menu.NONE,"Angestellte");
		submenu.add(Menu.NONE, 13, Menu.NONE,"Gäste");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==0) {
			Intent i = new Intent(getApplicationContext(), SplashActivity.class);
			startActivity(i);
			finish();
		}
		if(item.getItemId()==2) {
			finish();
			super.onDestroy();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
		if(item.getItemId()==11) {
			priceCategory = Lunch.PRICE_STUDENT;
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==12) {
			priceCategory = Lunch.PRICE_EMPLOYEE;
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==13) {
			priceCategory = Lunch.PRICE_GUEST;
			setListAdapter(selectedDay);
		}		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Log.d("MenuActivity", "Google API Client was connected");
		mResolvingError = false;
		Wearable.DataApi.addListener(mGoogleApiClient, this);
		Wearable.MessageApi.addListener(mGoogleApiClient, this);
		Wearable.CapabilityApi.addListener(
				mGoogleApiClient, this, Uri.parse("wear://"), CapabilityApi.FILTER_REACHABLE);

	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d("MenuActivity", "Google API Client was suspended");
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {
		if (!mResolvingError) {

			if (result.hasResolution()) {
				try {
					mResolvingError = true;
					result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
				} catch (IntentSender.SendIntentException e) {
					// There was an error with the resolution intent. Try again.
					mGoogleApiClient.connect();
				}
			} else {
				Log.e("MenuActivity", "Connection to Google API client has failed: " + result.getErrorMessage());
				mResolvingError = false;
				Wearable.DataApi.removeListener(mGoogleApiClient, this);
				Wearable.MessageApi.removeListener(mGoogleApiClient, this);
				Wearable.CapabilityApi.removeListener(mGoogleApiClient, this);
			}
		}
	}

	@Override
	public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
		Log.d("MenuActivity", "onCapabilityChanged: " + capabilityInfo);
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		Log.d("MenuActivity", "onDataChanged: " + dataEvents);

		for (DataEvent event : dataEvents) {
			Log.d("MenuActivity", "Event: "+ event.toString());
		}
	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent) {

	}

	/**
	 * Sends the asset that was created from the photo we took by adding it to the Data Item store.
	 */
	private void sendPhoto(Asset asset) {
		PutDataMapRequest dataMap = PutDataMapRequest.create("/image");
		dataMap.getDataMap().putAsset("photo", asset);
		dataMap.getDataMap().putLong("time", new Date().getTime());
		PutDataRequest request = dataMap.asPutDataRequest();
		request.setUrgent();

		Wearable.DataApi.putDataItem(mGoogleApiClient, request)
				.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
					@Override
					public void onResult(DataApi.DataItemResult dataItemResult) {
						Log.d("MenuActivity", "Sending image was successful: " + dataItemResult.getStatus()
								.isSuccess());
					}
				});
	}
	private void sendText(String text) {
		PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/count");
		putDataMapRequest.getDataMap().putString("count", text);

		PutDataRequest request = putDataMapRequest.asPutDataRequest();
		request.setUrgent();
		Log.d("MenuActivity", "Generating DataItem: " + request);
		//if (!mGoogleApiClient.isConnected()) {
		//	return;
		//}
		Wearable.DataApi.putDataItem(mGoogleApiClient, request)
				.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
					@Override
					public void onResult(DataApi.DataItemResult dataItemResult) {
						if (!dataItemResult.getStatus().isSuccess()) {
							Log.e("MenuActivity", "ERROR: failed to putDataItem, status code: "
									+ dataItemResult.getStatus().getStatusCode());
						}
					}
				});

	}

	/**
	 * Builds an {@link com.google.android.gms.wearable.Asset} from a bitmap. The image that we get
	 * back from the camera in "data" is a thumbnail size. Typically, your image should not exceed
	 * 320x320 and if you want to have zoom and parallax effect in your app, limit the size of your
	 * image to 640x400. Resize your image before transferring to your wearable device.
	 */
	private static Asset toAsset(Bitmap bitmap) {
		ByteArrayOutputStream byteStream = null;

		try {
			byteStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
			return Asset.createFromBytes(byteStream.toByteArray());
		} finally {
			if (null != byteStream) {
				try {
					byteStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}


	private class Event {

		String title;
		String text;

		public Event(String title, String text) {
			this.title = title;
			this.text = text;
		}
	}

	/**
	 * Generates a DataItem based on an incrementing count.
	 */
	private class DataItemGenerator implements Runnable {

		private int count = 0;

		@Override
		public void run() {
			PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/count");
			putDataMapRequest.getDataMap().putInt("count", count++);

			PutDataRequest request = putDataMapRequest.asPutDataRequest();
			request.setUrgent();

			Log.d("DataItemGenerator", "Generating DataItem: " + request);
			if (!mGoogleApiClient.isConnected()) {
				return;
			}
			Wearable.DataApi.putDataItem(mGoogleApiClient, request)
					.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
						@Override
						public void onResult(DataApi.DataItemResult dataItemResult) {
							if (!dataItemResult.getStatus().isSuccess()) {
								Log.e("DataItemGenerator", "ERROR: failed to putDataItem, status code: "
										+ dataItemResult.getStatus().getStatusCode());
							}
						}
					});
		}
	}
}
