package de.thorstenwitt.mensaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Lunch implements Parcelable {
	String mName;
	float priceStud;
	float priceEmp;
	float priceGuest;
	ArrayList<Lunch> m = new ArrayList<Lunch>();

	 
	
	public Lunch() {
		
	}

	public Lunch(Parcel in) {
		mName = in.readString();
		priceStud = in.readFloat();
		priceEmp = in.readFloat();
		priceGuest = in.readFloat();

	}
	public Lunch(String lName, float lpriceStud, float lpriceEmp, float lpriceGuest) {
		
		mName = lName;
		priceStud = lpriceStud;
		priceEmp = lpriceEmp;
		priceGuest = lpriceGuest;
	}
	
	public void addMittagsgericht(String lName, float lpriceStud, float lpriceEmp, float lpreisGast) {
		/*mName = lName;
		priceStud = lpriceStud;
		priceEmp = lpriceEmp;
		priceGuest = lpreisGast;
		*/
		m.add(new Lunch(lName, lpriceStud, lpriceEmp,lpreisGast));
		
	}
	
	public ArrayList<Lunch> returnGerichte () {
		
		return m;
	}
	// 1=Student, 2=Employee, 3=Guest

	public ArrayList<String> returnGerichtsnamen () {
		ArrayList<String> gn = new ArrayList<String>();
		for (int i=0; i<m.size(); i++) {
			if(m.get(i).mName.equals("")) {
				gn.add("<Unbenanntes Lunch>");
			}
			else {
				gn.add(m.get(i).mName);
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
		dest.writeString(mName);
		dest.writeFloat(priceGuest);
		dest.writeFloat(priceEmp);
		dest.writeFloat(priceStud);
	}

	public static final Creator<Lunch> CREATOR = new Creator<Lunch>() {
		public Lunch createFromParcel(Parcel in) {
			return new Lunch(in);
		}

		public Lunch[] newArray(int size) {
			return new Lunch[size];
		}
	};
}

	

