package de.thorstenwitt.mensaapp;



import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
	
	private Context mContext;
	ArrayList<MiddayMeal> myMenu;
	private int preistyp;
	private final int PRICE_STUDENT = 0;
	private final int PRICE_EMPLOYEE = 1;
	private final int PRICE_GUEST = 2;
	
	

	public MenuListAdapter(Context mContext, ArrayList<MiddayMeal> pMiddayMeal, int preistyp) {
		super();
		this.mContext = mContext;
		myMenu = pMiddayMeal;
		this.preistyp = preistyp;
	
	}

	@Override
	public int getCount() {
		//return myMenu.get(0).middayMealList.size();
		return myMenu.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listviewlunch, null);
        TextView tvLunchName=(TextView)view.findViewById(R.id.lunchName);
        TextView tvLunchPrice=(TextView)view.findViewById(R.id.lunchPrice);
        tvLunchName.setText(myMenu.get(position).mName);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        if(preistyp== PRICE_STUDENT) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).priceStud));
        }
        else if(preistyp== PRICE_EMPLOYEE) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).priceEmp));
        }        
        else if(preistyp== PRICE_GUEST) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).priceGuest));
        }

        return view;
	}
	
}