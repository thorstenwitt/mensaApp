package de.thorstenwitt.mensaapp.common.businessobject;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Mensa implements Parcelable{

    private String mensaName;
    private ArrayList<LunchOffer> lunchOffers;


    public Mensa(String mensaName, ArrayList<LunchOffer> lunchOffers) {
        this.mensaName = mensaName;
        this.lunchOffers = lunchOffers;
    }

    protected Mensa(Parcel in) {
        mensaName = in.readString();
        lunchOffers = in.readArrayList(LunchOffer.class.getClassLoader());
    }

    public static final Creator<Mensa> CREATOR = new Creator<Mensa>() {
        @Override
        public Mensa createFromParcel(Parcel in) {
            return new Mensa(in);
        }

        @Override
        public Mensa[] newArray(int size) {
            return new Mensa[size];
        }
    };

    public String getMensaName() {
        return mensaName;
    }

    public void setMensaName(String mensaName) {
        this.mensaName = mensaName;
    }

    public ArrayList<LunchOffer> getLunchOffers() {
        return lunchOffers;
    }

    public void setLunchOffers(ArrayList<LunchOffer> lunchOffers) {
        lunchOffers = lunchOffers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mensaName);
        parcel.writeList(lunchOffers);
    }
}
