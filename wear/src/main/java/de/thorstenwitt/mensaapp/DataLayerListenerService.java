/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.thorstenwitt.mensaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

import de.thorstenwitt.mensaapp.common.DataMapParcelableUtils;
import de.thorstenwitt.mensaapp.common.businessobject.Mensa;
import de.thorstenwitt.mensaapp.common.businessobject.Properties;
import de.thorstenwitt.mensaapp.fragments.AssetFragment;
import de.thorstenwitt.mensaapp.fragments.DataFragment;
import de.thorstenwitt.mensaapp.views.MensaActivityWear;

/**
 * Listens to DataItems and Messages from the local node.
 */
public class DataLayerListenerService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener,
        MessageApi.MessageListener,
        CapabilityApi.CapabilityListener{

    private static final String TAG = "DLLService";


    private static final String LUNCH_PATH = "/lunch";
    private static final String LAUNCHAPP_PATH = "/launch-app";
    private static final String PROPERTIES_PATH = "/properties";

    private GoogleApiClient mGoogleApiClient;
    private DataFragment mDataFragment;
    private AssetFragment mAssetFragment;
    private MensaActivityWear mensaActivityWear;

    public DataLayerListenerService(MensaActivityWear mensaActivityWear) {
        this.mensaActivityWear = mensaActivityWear;
        mGoogleApiClient = new GoogleApiClient.Builder(mensaActivityWear)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mDataFragment = new DataFragment();
        mAssetFragment = new AssetFragment();
    }

    public static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public void pause(){
        if ((mGoogleApiClient != null) && mGoogleApiClient.isConnected()) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.CapabilityApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public DataFragment getmDataFragment() {
        return mDataFragment;
    }

    public AssetFragment getmAssetFragment() {
        return mAssetFragment;
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        LOGD(TAG, "onConnected(): Successfully connected to Google API client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.CapabilityApi.addListener(
                mGoogleApiClient, this, Uri.parse("wear://"), CapabilityApi.FILTER_REACHABLE);
        PendingResult<DataItemBuffer> pendingResult = Wearable.DataApi.getDataItems(mGoogleApiClient);
        pendingResult.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                if(dataItems.getCount()!=0) {
                    for(int i=0; i<dataItems.getCount(); i++) {
                        String path = dataItems.get(i).getUri().getPath();
                        if (DataLayerListenerService.LUNCH_PATH.equals(path)) {
                            DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItems.get(i));
                            Mensa m = DataMapParcelableUtils.getParcelable(dataMapItem.getDataMap(), "lunch", Mensa.CREATOR);
                            mensaActivityWear.notifyAboutNewMensaData(m);
                        } else if (DataLayerListenerService.PROPERTIES_PATH.equals(path)){
                            DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItems.get(i));
                            Properties p = DataMapParcelableUtils.getParcelable(dataMapItem.getDataMap(), "properties", Properties.CREATOR);
                            mensaActivityWear.notifyAboutNewProperties(p);
                        }
                        else {
                            LOGD(TAG, "Unrecognized path: " + path);
                        }
                    }
                }
                else {
                    PendingResult<NodeApi.GetConnectedNodesResult> nodeResult= Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
                    nodeResult.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                        @Override
                        public void onResult(@NonNull NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                            List<Node> nodeList = getConnectedNodesResult.getNodes();
                            for(Node node: nodeList) {
                                Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), LAUNCHAPP_PATH,new byte[1]);
                            }
                        }
                    });

                }

                dataItems.release();
            }
        });


    }

    @Override
    public void onConnectionSuspended(int cause) {
        LOGD(TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed(): Failed to connect, with result: " + result);
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        LOGD(TAG, "onCapabilityChanged: " + capabilityInfo);
        mDataFragment.appendItem("onCapabilityChanged", capabilityInfo.toString());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        LOGD(TAG, "onDataChanged(): " + dataEvents);
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (DataLayerListenerService.LUNCH_PATH.equals(path)) {
                    Log.d(TAG, "Data Changed for LUNCH_PATH");
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    Mensa m = DataMapParcelableUtils.getParcelable(dataMapItem.getDataMap(), "lunch", Mensa.CREATOR);
                    mensaActivityWear.notifyAboutNewMensaData(m);

                } else if (DataLayerListenerService.PROPERTIES_PATH.equals(path)){
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    Properties p = DataMapParcelableUtils.getParcelable(dataMapItem.getDataMap(), "properties", Properties.CREATOR);
                    mensaActivityWear.notifyAboutNewProperties(p);
                } else {
                    LOGD(TAG, "Unrecognized path: " + path);
                }

            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        LOGD(TAG, "onMessageReceived: " + event);
        mDataFragment.appendItem("Message", event.toString());
    }


}