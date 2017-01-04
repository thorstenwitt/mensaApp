package de.thorstenwitt.mensaapp.helper;

import android.app.Activity;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.thorstenwitt.mensaapp.common.businessobject.Lunch;
import de.thorstenwitt.mensaapp.common.DataMapParcelableUtils;
import de.thorstenwitt.mensaapp.common.businessobject.Mensa;

/**
 * Created by freese on 05.12.2016.
 */

public class DataSync  implements
        CapabilityApi.CapabilityListener,
        MessageApi.MessageListener,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    // Play services errors.
    private static final int REQUEST_RESOLVE_ERROR = 1000;
    private boolean mResolvingError = false;
    private Activity activity;
    // Send DataItems.
    private ScheduledExecutorService mGeneratorExecutor;
    private ScheduledFuture<?> mDataItemGeneratorFuture;


    public DataSync(Activity activity) {
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(activity.getApplicationContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
//        mGeneratorExecutor = new ScheduledThreadPoolExecutor(1);
//        mDataItemGeneratorFuture = mGeneratorExecutor.scheduleWithFixedDelay(
//                new DataItemGenerator(), 1, 5, TimeUnit.SECONDS);
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
        Log.d("DataSync","Google API Client was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!mResolvingError) {

            if (result.hasResolution()) {
                try {
                    mResolvingError = true;
                    result.startResolutionForResult(activity, REQUEST_RESOLVE_ERROR);
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
        Log.d("DataSync", "onCapabilityChanged: " + capabilityInfo);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("MenuActivity", "onDataChanged: " + dataEvents);

        for (DataEvent event : dataEvents) {
            Log.d("DataSync", "Event: "+ event.toString());
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("DataSync", "Message: "+ messageEvent.toString());
    }

    /**
     * Sends the asset that was created from the photo we took by adding it to the Data Item store.
     */

    /**
     * Sendet Strings an die WearableAPI
     * @param text
     */
    public void sendText(String text){
        PutDataMapRequest putDataMapRequestString = PutDataMapRequest.create("/string");
        putDataMapRequestString.getDataMap().putString("string", text);

        PutDataRequest request = putDataMapRequestString.asPutDataRequest();
        request.setUrgent();

        Log.d("DataItemGenerator", "Generating DataItem String: " + request);

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



    public void sendMensa(Mensa mensa){
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/lunch");
        DataMapParcelableUtils.putParcelable(putDataMapRequest.getDataMap(), "lunch", mensa);

        PutDataRequest request = putDataMapRequest.asPutDataRequest();
        request.setUrgent();

        Log.d("DataItemGenerator", "Generating DataItem Lunch: " + request);

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


    private class Event {

        String title;
        String text;

        public Event(String title, String text) {
            this.title = title;
            this.text = text;
        }
    }

}
