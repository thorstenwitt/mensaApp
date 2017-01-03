package de.thorstenwitt.mensaapp.activity;



import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.thorstenwitt.mensaapp.R;
import de.thorstenwitt.mensaapp.common.businessobject.Lunch;

public class MenuListAdapter extends BaseAdapter {
	
	private Context mContext;
	ArrayList<Lunch> myMenu;
	private int preistyp;
	
	

	public MenuListAdapter(Context mContext, ArrayList<Lunch> pLunch, int preistyp) {
		super();
		this.mContext = mContext;
		myMenu = pLunch;
		this.preistyp = preistyp;
	
	}

	@Override
	public int getCount() {
		//return myMenu.get(0).lunchList.size();
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
        tvLunchName.setText(myMenu.get(position).getmName());
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        if(preistyp==Lunch.PRICE_STUDENT) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).getPriceStud()));
        }
        else if(preistyp==Lunch.PRICE_EMPLOYEE) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).getPriceEmp()));
        }        
        else if(preistyp==Lunch.PRICE_GUEST) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).getPriceGuest()));
        }
		if (myMenu.get(position).getIsMensaVital()) {
			tvLunchName.setTextColor(Color.rgb(0,134,61));
			tvLunchPrice.setTextColor(Color.rgb(0,134,61));
		}

        return view;
	}
	
}