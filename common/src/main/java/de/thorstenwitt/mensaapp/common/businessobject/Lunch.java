package de.thorstenwitt.mensaapp.common.businessobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;


public class Lunch implements Parcelable,Serializable {

	private String mName;
	private float priceStud;
	private float priceEmp;
	private float priceGuest;
	private boolean isMensaVital;
	private ArrayList<Lunch> m = new ArrayList<Lunch>();
	public static final int PRICE_STUDENT = 0;
	public static final int PRICE_EMPLOYEE = 1;
	public static int PRICE_GUEST = 2;

	public Lunch(Parcel in) {
		setmName(in.readString());
		setPriceStud(in.readFloat());
		setPriceEmp(in.readFloat());
		setPriceGuest(in.readFloat());
		setIsMensaVital(in.readInt() == 0 ? false : true);
	}

	public Lunch(String lName, float lpriceStud, float lpriceEmp, float lpriceGuest, boolean isMensaVital) {
		
		setmName(lName);
		setPriceStud(lpriceStud);
		setPriceEmp(lpriceEmp);
		setPriceGuest(lpriceGuest);
		setIsMensaVital(isMensaVital);
	}

	public Lunch(Lunch lunch){
		setmName(lunch.getmName());
		setPriceStud(lunch.getPriceStud());
		setPriceEmp(lunch.getPriceEmp());
		setPriceGuest(lunch.getPriceGuest());
		setIsMensaVital(lunch.getIsMensaVital());
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
		dest.writeInt(getIsMensaVital() ? 1 : 0);
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

	public boolean getIsMensaVital() {return  this.isMensaVital;}

	public void setIsMensaVital(boolean isMensaVital) { this.isMensaVital = isMensaVital; }

}

	

