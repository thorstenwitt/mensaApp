package de.thorstenwitt.mensaapp.common.businessobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Thorsten on 24.02.2017.
 */

public class Properties implements Parcelable, Serializable {

    private int selectedMensa;
    private int selectedPriceCategory;

    public Properties(int selectedMensa, int selectedPriceCategory) {
        this.selectedMensa = selectedMensa;
        this.selectedPriceCategory = selectedPriceCategory;
    }

    protected Properties(Parcel in) {
        selectedMensa = in.readInt();
        selectedPriceCategory = in.readInt();
    }

    public static final Creator<Properties> CREATOR = new Creator<Properties>() {
        @Override
        public Properties createFromParcel(Parcel in) {
            return new Properties(in);
        }

        @Override
        public Properties[] newArray(int size) {
            return new Properties[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(selectedMensa);
        parcel.writeInt(selectedPriceCategory);
    }

    public int getSelectedMensa() {
        return selectedMensa;
    }

    public void setSelectedMensa(int selectedMensa) {
        this.selectedMensa = selectedMensa;
    }

    public int getSelectedPriceCategory() {
        return selectedPriceCategory;
    }

    public void setSelectedPriceCategory(int selectedPriceCategory) {
        this.selectedPriceCategory = selectedPriceCategory;
    }
}
