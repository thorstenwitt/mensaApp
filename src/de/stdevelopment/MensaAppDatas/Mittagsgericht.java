package de.stdevelopment.MensaAppDatas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Mittagsgericht implements Parcelable {
	String mName;
	float preisStud;
	float preisMit;
	float preisGast;
	ArrayList<Mittagsgericht> m = new ArrayList<Mittagsgericht>();

	 
	
	public Mittagsgericht() {
		
	}

	public Mittagsgericht(Parcel in) {
		mName = in.readString();
		preisStud = in.readFloat();
		preisMit = in.readFloat();
		preisGast = in.readFloat();

	}
	public Mittagsgericht(String lName,float lpreisStud,float lpreisMit, float lpreisGast) {
		
		mName = lName;
		preisStud = lpreisStud;
		preisMit = lpreisMit;
		preisGast = lpreisGast;
	}
	
	public void addMittagsgericht(String lName,float lpreisStud,float lpreisMit, float lpreisGast) {
		/*mName = lName;
		preisStud = lpreisStud;
		preisMit = lpreisMit;
		preisGast = lpreisGast;
		*/
		m.add(new Mittagsgericht(lName,lpreisStud,lpreisMit,lpreisGast));
		
	}
	
	public ArrayList<Mittagsgericht> returnGerichte () {
		
		return m;
	}
	// 1=Student, 2=Mitarbeiter, 3=Gast

	public ArrayList<String> returnGerichtsnamen () {
		ArrayList<String> gn = new ArrayList<String>();
		for (int i=0; i<m.size(); i++) {
			if(m.get(i).mName.equals("")) {
				gn.add("<Unbenanntes Mittagsgericht>");
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
		dest.writeFloat(preisGast);
		dest.writeFloat(preisMit);
		dest.writeFloat(preisStud);
	}

	public static final Parcelable.Creator<Mittagsgericht> CREATOR = new Parcelable.Creator<Mittagsgericht>() {
		public Mittagsgericht createFromParcel(Parcel in) {
			return new Mittagsgericht(in);
		}

		public Mittagsgericht[] newArray(int size) {
			return new Mittagsgericht[size];
		}
	};
}

	

