package de.thorstenwitt.mensaapp;



import java.text.NumberFormat;
import java.util.ArrayList;

import de.thorstenwitt.mensaapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
	
	private Context mContext;
	ArrayList<Mittagsgericht> myMenu;
	private int preistyp;
	private final int PREIS_STUDENT = 0;
	private final int PREIS_ANGESTELLTER = 1;
	private final int PREIS_GAST = 2;
	
	

	public MenuListAdapter(Context mContext,ArrayList<Mittagsgericht> pMittagsgericht, int preistyp) {
		super();
		this.mContext = mContext;
		myMenu = pMittagsgericht;
		this.preistyp = preistyp; 
	
	}

	@Override
	public int getCount() {
		//return myMenu.get(0).mittagsgerichte.size();
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
        if(preistyp==PREIS_STUDENT) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).preisStud));	
        }
        else if(preistyp==PREIS_ANGESTELLTER) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).preisMit));	
        }        
        else if(preistyp==PREIS_GAST) {
        	tvLunchPrice.setText(nf.format(myMenu.get(position).preisGast));	
        }

        return view;
	}
	
}