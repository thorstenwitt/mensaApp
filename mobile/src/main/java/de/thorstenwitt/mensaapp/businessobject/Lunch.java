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

	 
	
	public Lunch() {
		
	}

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
	
	public void addMittagsgericht(String lName, float lpriceStud, float lpriceEmp, float lpreisGast) {
		/*mName = lName;
		priceStud = lpriceStud;
		priceEmp = lpriceEmp;
		priceGuest = lpreisGast;
		*/
		getM().add(new Lunch(lName, lpriceStud, lpriceEmp,lpreisGast));
		
	}
	
	public ArrayList<Lunch> returnGerichte () {
		
		return getM();
	}
	// 1=Student, 2=Employee, 3=Guest

	public ArrayList<String> returnGerichtsnamen () {
		ArrayList<String> gn = new ArrayList<String>();
		for (int i = 0; i< getM().size(); i++) {
			if(getM().get(i).getmName().equals("")) {
				gn.add("<Unbenanntes Lunch>");
			}
			else {
				gn.add(getM().get(i).getmName());
			}
			
		}
		
		
		return gn;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getmName());
		dest.writeFloat(getPriceGuest());
		dest.writeFloat(getPriceEmp());
		dest.writeFloat(getPriceStud());
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

	public ArrayList<Lunch> getM() {
		return m;
	}

	public void setM(ArrayList<Lunch> m) {
		this.m = m;
	}
}

	

