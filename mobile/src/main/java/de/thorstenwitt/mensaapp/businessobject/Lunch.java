package de.thorstenwitt.mensaapp.businessobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Lunch implements Parcelable {

	private String mName;
	private float priceStud;
	private float priceEmp;
	private float priceGuest;
	private ArrayList<Lunch> m = new ArrayList<Lunch>();
	public static final int PRICE_STUDENT = 0;
	public static final int PRICE_EMPLOYEE = 1;
	public static int PRICE_GUEST = 2;

	public Lunch(Parcel in) {
		setmName(in.readString());
		setPriceStud(in.readFloat());
		setPriceEmp(in.readFloat());
		setPriceGuest(in.readFloat());
	}

	public Lunch(String lName, float lpriceStud, float lpriceEmp, float lpriceGuest) {
		
		setmName(lName);
		setPriceStud(lpriceStud);
		setPriceEmp(lpriceEmp);
		setPriceGuest(lpriceGuest);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getmName());
		dest.writeFloat(getPriceStud());
		dest.writeFloat(getPriceEmp());
		dest.writeFloat(getPriceGuest());
	}

	public static final Creator<Lunch> CREATOR = new Creator<Lunch>() {
		public Lunch createFromParcel(Parcel in) {
			return new Lunch(in);
		}

		public Lunch[] newArray(int size) {
			return new Lunch[size];
		}
	};

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public float getPriceStud() {
		return priceStud;
	}

	public void setPriceStud(float priceStud) {
		this.priceStud = priceStud;
	}

	public float getPriceEmp() {
		return priceEmp;
	}

	public void setPriceEmp(float priceEmp) {
		this.priceEmp = priceEmp;
	}

	public float getPriceGuest() {
		return priceGuest;
	}

	public void setPriceGuest(float priceGuest) {
		this.priceGuest = priceGuest;
	}

}

	

