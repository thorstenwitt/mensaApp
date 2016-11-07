package de.thorstenwitt.mensaapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class FoodSelection implements Parcelable{
	public String mydate;
	public ArrayList<MiddayMeal> middayMealList;

	public FoodSelection(Parcel in) {
		mydate = in.readString();
		middayMealList = in.readArrayList(MiddayMeal.class.getClassLoader());
	}
	
	
	public FoodSelection(String mydate, ArrayList<MiddayMeal> middayMealList) {
		this.mydate = mydate;
		this.middayMealList = middayMealList;
	}
	public String getMydate() {
		return mydate;
	}

	public void setMydate(String mydate) {
		this.mydate = mydate;
	}

	public ArrayList<MiddayMeal> getMiddayMealList() {
		return middayMealList;
	}

	public void setMiddayMealList(ArrayList<MiddayMeal> middayMealList) {
		this.middayMealList = middayMealList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mydate);
		dest.writeList(middayMealList);
	}

	public static final Creator<FoodSelection> CREATOR = new Creator<FoodSelection>() {
		public FoodSelection createFromParcel(Parcel in) {
			return new FoodSelection(in);
		}

		public FoodSelection[] newArray(int size) {
			return new FoodSelection[size];
		}
	};

}
