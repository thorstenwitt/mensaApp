package de.thorstenwitt.mensaapp.activity;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

import com.google.android.gms.wearable.PutDataMapRequest;

import de.thorstenwitt.mensaapp.common.DataMapParcelableUtils;
import de.thorstenwitt.mensaapp.common.businessobject.Mensa;
import de.thorstenwitt.mensaapp.common.businessobject.Properties;
import de.thorstenwitt.mensaapp.helper.Contributors;
import de.thorstenwitt.mensaapp.helper.DataSync;
import de.thorstenwitt.mensaapp.parser.LunchParser;
import de.thorstenwitt.mensaapp.R;
import de.thorstenwitt.mensaapp.common.businessobject.Lunch;
import de.thorstenwitt.mensaapp.common.businessobject.LunchOffer;

public class MenuActivity extends AppCompatActivity {

	public TextView lbAmount;
	public Button btReset;
	public Spinner spDate;
	public ListView lstLunch;
	public MenuListAdapter menuListAdapter;
	public float totalAmount = 0.0f;
	public ArrayList<LunchOffer> myLunchData;
	int selectedDay=0;
	private Properties properties;
	private DataSync ds;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		ds = new DataSync(this);
		lbAmount = (TextView) findViewById(R.id.textViewBetrag);
		btReset = (Button) findViewById(R.id.buttonReset);
		spDate = (Spinner) findViewById(R.id.spinnerDay);
		spDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedDay = position;
				setListAdapter(selectedDay);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		lstLunch = (ListView) findViewById(R.id.listViewLunch);

		if(savedInstanceState!=null) {
			myLunchData = savedInstanceState.getParcelableArrayList("menues");
			properties = savedInstanceState.getParcelable("properties");
		}
		else {
			properties = new Properties(Lunch.MENSA_STRALSUND, Lunch.PRICE_STUDENT);
			getLunchOffersfromSelectedMensa(LunchParser.getInstance(), properties.getSelectedMensa());
		}
		ArrayList<String> lunchDates = new ArrayList<String>();
		for(LunchOffer t:myLunchData) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date lunchOfferDate = sdf.parse(t.getMydate());
				String localizedLunchDate = DateFormat.getDateInstance(DateFormat.FULL).format(lunchOfferDate);
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

		sendMensaDataToWatch();
	}

	public void sendMensaDataToWatch() {
		if (LunchParser.getInstance().getLunchDataForAllMensas().size()>=properties.getSelectedMensa()) {
			ds.sendMensa(LunchParser.getInstance().getLunchDataForAllMensas().get(properties.getSelectedMensa()));
		}
		ds.sendProperties(properties);
	}

	public void updateAmountLabel(float amount) {
		lbAmount.setText("Gesamtbetrag: "+NumberFormat.getCurrencyInstance(Locale.GERMANY).format(amount));
	}
	public void setListAdapter(int selectedDay){
		final ArrayList<Lunch> selectedLunches = myLunchData.get(selectedDay).getLunchList();
		menuListAdapter = new MenuListAdapter(this.getApplicationContext(), selectedLunches, properties.getSelectedPriceCategory());
		lstLunch.setAdapter(menuListAdapter);
		lstLunch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				if (properties.getSelectedPriceCategory() == Lunch.PRICE_STUDENT	){
					totalAmount += selectedLunches.get(position).getPriceStud();
					updateAmountLabel(totalAmount);
				}else if (properties.getSelectedPriceCategory() == Lunch.PRICE_EMPLOYEE){
					totalAmount += selectedLunches.get(position).getPriceEmp();
					updateAmountLabel(totalAmount);
				}else{
					totalAmount += selectedLunches.get(position).getPriceGuest();
					updateAmountLabel(totalAmount);
				}
			}
		});
		sendMensaDataToWatch();
	}
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		bundle.putParcelableArrayList("menues", myLunchData);
		bundle.putParcelable("properties",properties);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, 0, Menu.NONE,"Speisekarte aktualisieren");
		SubMenu submenuPreis = menu.addSubMenu(Menu.NONE, 1, Menu.NONE,"Preiskategorie auswaehlen");
		SubMenu submenuMensa = menu.addSubMenu(Menu.NONE, 2, Menu.NONE,"Mensa auswaehlen");
		menu.add(Menu.NONE, 3, Menu.NONE,"Infos");

		submenuPreis.add(Menu.NONE, 11, Menu.NONE,"Studenten");
		submenuPreis.add(Menu.NONE, 12, Menu.NONE,"Angestellte");
		submenuPreis.add(Menu.NONE, 13, Menu.NONE,"Gäste");

		submenuMensa.add(Menu.NONE, 21, Menu.NONE,"Mensa am Wall");
		submenuMensa.add(Menu.NONE, 22, Menu.NONE,"Mensa Neubrandenburg");
		submenuMensa.add(Menu.NONE, 23, Menu.NONE,"Mensa Stralsund");
		submenuMensa.add(Menu.NONE, 24, Menu.NONE,"Mensa Berthold-Beitz-Platz");

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==0) {
			Intent i = new Intent(getApplicationContext(), SplashActivity.class);
			startActivity(i);
			finish();
		}
		if(item.getItemId()==3) {
			new Contributors(this).show();
		}
		if(item.getItemId()==11) {
			properties.setSelectedPriceCategory(Lunch.PRICE_STUDENT);
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==12) {
			properties.setSelectedPriceCategory(Lunch.PRICE_EMPLOYEE);
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==13) {
			properties.setSelectedPriceCategory(Lunch.PRICE_GUEST);
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==21) {
			properties.setSelectedMensa(Lunch.MENSA_AM_WALL);
			getLunchOffersfromSelectedMensa(LunchParser.getInstance(), properties.getSelectedMensa());
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==22) {
			properties.setSelectedMensa(Lunch.MENSA_NEUBRANDENBURG);
			getLunchOffersfromSelectedMensa(LunchParser.getInstance(), properties.getSelectedMensa());
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==23) {
			properties.setSelectedMensa(Lunch.MENSA_STRALSUND);
			getLunchOffersfromSelectedMensa(LunchParser.getInstance(), properties.getSelectedMensa());
			setListAdapter(selectedDay);
		}
		if(item.getItemId()==24) {
			properties.setSelectedMensa(Lunch.MENSA_BERTHOLD_BEITZ_PLATZ);
			getLunchOffersfromSelectedMensa(LunchParser.getInstance(), properties.getSelectedMensa());
			setListAdapter(selectedDay);
		}

		return super.onOptionsItemSelected(item);
	}
	private void getLunchOffersfromSelectedMensa (LunchParser lp, int selectedMensa) {
		myLunchData = lp.getLunchData(selectedMensa);
	}

}
