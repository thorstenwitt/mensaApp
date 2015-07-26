package de.stdevelopment.MensaAppDatas;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class Tagesmenues implements Parcelable{
	public String mydate;
	public ArrayList<Mittagsgericht> mittagsgerichte;

	public Tagesmenues(Parcel in) {
		mydate = in.readString();
		mittagsgerichte = in.readArrayList(Mittagsgericht.class.getClassLoader());
	}
	
	
	public Tagesmenues(String mydate, ArrayList<Mittagsgericht> mittagsgerichte) {
		this.mydate = mydate;
		this.mittagsgerichte = mittagsgerichte;
	}
	public String getMydate() {
		return mydate;
	}
	public void setMydate(String mydate) {
		this.mydate = mydate;
	}
	public ArrayList<Mittagsgericht> getMittagsgerichte() {
		return mittagsgerichte;
	}
	public void setMittagsgerichte(ArrayList<Mittagsgericht> mittagsgerichte) {
		this.mittagsgerichte = mittagsgerichte;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mydate);
		dest.writeList(mittagsgerichte);
	}

	public static final Parcelable.Creator<Tagesmenues> CREATOR = new Parcelable.Creator<Tagesmenues>() {
		public Tagesmenues createFromParcel(Parcel in) {
			return new Tagesmenues(in);
		}

		public Tagesmenues[] newArray(int size) {
			return new Tagesmenues[size];
		}
	};

}
