package de.thorstenwitt.mensaapp.datasync;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


import static android.content.ContentValues.TAG;
import android.content.Context;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by freese on 13.11.2016.
 */

public class SyncHelper implements GoogleApiClient.ConnectionCallbacks, CapabilityApi.CapabilityListener, MessageApi.MessageListener, DataApi.DataListener, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private Context context;
    private static SyncHelper instance;
    private boolean mResolvingError = false;

    private ListView mDataItemList;
    //private DataItemAdapter mDataItemListAdapter;

    // Send DataItems.
    private ScheduledExecutorService mGeneratorExecutor;
    private ScheduledFuture<?> mDataItemGeneratorFuture;



    public static synchronized SyncHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SyncHelper(context.getApplicationContext());
        }

        return instance;
    }

    public SyncHelper(Context context) {
        this.context = context;

       // this.googleApiClient = new GoogleApiClient.Builder(context)
         //       .addApi(Wearable.API)
           //     .addConnectionCallbacks(this)
             //   .build();
    }

    public void testConnection() {
        Log.d(TAG, "testConnection()");
        //mCameraSupported = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        //setContentView(R.layout.main_activity);
        //setupViews();

        // Stores DataItems received by the local broadcaster or from the paired watch.
        //mDataItemListAdapter = new DataItemAdapter(this, android.R.layout.simple_list_item_1);
       // mDataItemList.setAdapter(mDataItemListAdapter);

        mGeneratorExecutor = new ScheduledThreadPoolExecutor(1);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        Log.d(TAG, "gSTATUS:" + googleApiClient.isConnected());
     }




    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google API Client was connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API client was suspended.");
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
