package de.thorstenwitt.mensaapp.common.businessobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class LunchOffer implements Parcelable{
	public String mydate;
	public ArrayList<Lunch> lunchList;

	public LunchOffer(Parcel in) {
		mydate = in.readString();
		lunchList = in.readArrayList(Lunch.class.getClassLoader());
	}
	
	
	public LunchOffer(String mydate, ArrayList<Lunch> lunchList) {
		this.mydate = mydate;
		this.lunchList = lunchList;
	}
	public String getMydate() {
		return mydate;
	}

	public ArrayList<Lunch> getLunchList() {
		return lunchList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mydate);
		dest.writeList(lunchList);
	}

	public static final Creator<LunchOffer> CREATOR = new Creator<LunchOffer>() {
		public LunchOffer createFromParcel(Parcel in) {
			return new LunchOffer(in);
		}

		public LunchOffer[] newArray(int size) {
			return new LunchOffer[size];
		}
	};

}
