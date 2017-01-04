package de.thorstenwitt.mensaapp.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

/**
 * Created by freese on 31.12.2016.
 */

public class DataMapParcelableUtils {

    public static void putParcelable(DataMap dataMap, String key, Parcelable parcelable) {
        final Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        dataMap.putByteArray(key, parcel.marshall());
        dataMap.putLong("Time", System.currentTimeMillis());
        parcel.recycle();
    }

    public static <T> T getParcelable(DataMap dataMap, String key, Parcelable.Creator<T> creator) {
        final byte[] byteArray = dataMap.getByteArray(key);
        final Parcel parcel = Parcel.obtain();
        parcel.unmarshall(byteArray, 0, byteArray.length);
        parcel.setDataPosition(0);
        final T object = creator.createFromParcel(parcel);
        parcel.recycle();
        return object;
    }
}
