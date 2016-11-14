package de.thorstenwitt.mensaapp.datasync;
import android.provider.Settings;
import android.util.Log;


import static android.content.ContentValues.TAG;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by freese on 13.11.2016.
 */

public class SyncHelper {
    private GoogleApiClient googleApiClient;
    private Context context;
    private static SyncHelper instance;

    public static synchronized SyncHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SyncHelper(context.getApplicationContext());
        }

        return instance;
    }

    public SyncHelper(Context context) {
        this.context = context;

        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    public void testConnection() {

        if (validateConnection()) {
            PutDataMapRequest dataMap = PutDataMapRequest.create("/filter");

            //dataMap.getDataMap().putInt(DataMapKeys.FILTER, sensorId);
            //dataMap.getDataMap().putLong(DataMapKeys.TIMESTAMP, System.currentTimeMillis());

            PutDataRequest putDataRequest = dataMap.asPutDataRequest();
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                       Log.d(TAG, "MEEEEEHHHHH: " + dataItemResult.getStatus().isSuccess());

                }
            });
        }
    Log.d(TAG, "GOOGLEAPI!!!!!!!!! :" + googleApiClient.isConnected() );

    }
    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }
        return false;

        //ConnectionResult result = googleApiClient.blockingConnect(15000, TimeUnit.MILLISECONDS);

        //return result.isSuccess();
    }
}
