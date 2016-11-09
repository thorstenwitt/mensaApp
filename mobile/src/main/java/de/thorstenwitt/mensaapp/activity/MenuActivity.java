package de.thorstenwitt.mensaapp.activity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import de.thorstenwitt.mensaapp.parser.LunchParser;
import de.thorstenwitt.mensaapp.R;
import de.thorstenwitt.mensaapp.businessobject.Lunch;
import de.thorstenwitt.mensaapp.businessobject.LunchOffer;

public class MenuActivity extends AppCompatActivity {
	
	public TextView lbAmount;
	public Button btReset;
	public Spinner spDate;
	public ListView lstLunch;
	public MenuListAdapter menuListAdapter;
	public float totalAmount = 0.0f;
	public ArrayList<LunchOffer> myLunchData;
	private final int PRICE_STUDENT = 0;
	private final int PRICE_EMPLOYEE = 1;
	private final int PRICE_GUEST = 2;
	int selectedDay=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		lbAmount = (TextView) findViewById(R.id.textViewBetrag);
		btReset = (Button) findViewById(R.id.buttonReset);
		spDate = (Spinner) findViewById(R.id.spinnerDay);
		spDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedDay = position;
				setListAdapter(selectedDay, PRICE_STUDENT);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		lstLunch = (ListView) findViewById(R.id.listViewLunch);
		LunchParser lp = LunchParser.getInstance();

		if(savedInstanceState!=null) {
			myLunchData = savedInstanceState.getParcelableArrayList("menues");
		}
		else {
			myLunchData = lp.getLunchDataForStralsund();
		}
		ArrayList<String> lunchDates = new ArrayList<String>();
		for(LunchOffer t:myLunchData) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date lunchOfferDate = sdf.parse(t.getMydate());
				String localizedLunchDate = DateFormat.getDateInstance().format(lunchOfferDate);
				lunchDates.add(localizedLunchDate);
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG);
			}


		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lunchDates);
		spDate.setAdapter(spinnerAdapter);


		
		
		updateAmountLabel(totalAmount);
		btReset.setText("Reset");
		btReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				totalAmount =0.0f;
				updateAmountLabel(totalAmount);
				
			}
		});
		
	}
	public void updateAmountLabel(float amount) {
		lbAmount.setText("Gesamtbetrag: "+NumberFormat.getCurrencyInstance().format(amount));
	}
	public void setListAdapter(int selectedDay, int priceCategory){
		final ArrayList<Lunch> selectedLunches = myLunchData.get(selectedDay).getLunchList();
		menuListAdapter = new MenuListAdapter(this.getApplicationContext(), selectedLunches, priceCategory);
		lstLunch.setAdapter(menuListAdapter);
		lstLunch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				totalAmount += selectedLunches.get(position).getPriceStud();
				updateAmountLabel(totalAmount);
			}
		});
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putParcelableArrayList("menues", myLunchData);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, 0, Menu.NONE,"Speisekarte aktualisieren");
		
		SubMenu submenu = menu.addSubMenu(Menu.NONE, 1, Menu.NONE,"Preiskategorie auswaehlen");
		menu.add(Menu.NONE, 2, Menu.NONE,"Beenden");
		submenu.add(Menu.NONE, 11, Menu.NONE,"Studenten");
		submenu.add(Menu.NONE, 12, Menu.NONE,"Angestellte");
		submenu.add(Menu.NONE, 13, Menu.NONE,"Gäste");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==0) {
			Intent i = new Intent(getApplicationContext(), SplashActivity.class);
			startActivity(i);
			finish();
		}
		if(item.getItemId()==2) {
			finish();
			super.onDestroy();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
		if(item.getItemId()==11) {
			setListAdapter(selectedDay, PRICE_STUDENT);
		}
		if(item.getItemId()==12) {
			setListAdapter(selectedDay, PRICE_EMPLOYEE);
		}
		if(item.getItemId()==13) {
			setListAdapter(selectedDay, PRICE_GUEST);
		}		
		return super.onOptionsItemSelected(item);
	}
}