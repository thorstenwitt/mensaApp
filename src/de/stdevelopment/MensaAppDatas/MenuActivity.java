package de.stdevelopment.MensaAppDatas;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import de.stdevelopment.MensaApp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MenuActivity extends Activity {
	
	public TextView lbBetrag;
	public Button btReset;
	public Spinner spDate;
	public ListView lstLunch;
	public MenuListAdapter menuListAdapter;
	public float gesamtBetrag = 0.0f;
	public ArrayList<Tagesmenues> myLunchData;
	private final int PREIS_STUDENT = 0;
	private final int PREIS_ANGESTELLTER = 1;
	private final int PREIS_GAST = 2;
	int selectedDay=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		lbBetrag = (TextView) findViewById(R.id.textViewBetrag);
		btReset = (Button) findViewById(R.id.buttonReset);
		spDate = (Spinner) findViewById(R.id.spinnerDay);
		spDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedDay = position;
				setListAdapter(selectedDay,PREIS_STUDENT);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lstLunch = (ListView) findViewById(R.id.listViewLunch);
		LunchParser lp = LunchParser.getInstance();
		myLunchData = lp.getLunchData();
		
		
		ArrayList<String> lunchDates = new ArrayList<String>();
		for(Tagesmenues t:myLunchData) {
			DateFormat df = DateFormat.getDateInstance();
			String myDate = t.getMydate(); 
			lunchDates.add(myDate);
		}
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lunchDates);
		spDate.setAdapter(spinnerAdapter);


		
		
		updateBetragsLabel(gesamtBetrag);
		btReset.setText("Reset");
		btReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gesamtBetrag=0.0f;
				updateBetragsLabel(gesamtBetrag);
				
			}
		});
		
	}
	public void updateBetragsLabel(float betrag) {
		lbBetrag.setText("Gesamtbetrag: "+NumberFormat.getCurrencyInstance().format(betrag));
	}
	public void setListAdapter(int selectedDay, int preistyp){
		final ArrayList<Mittagsgericht> selectedMittagsgerichte = myLunchData.get(selectedDay).getMittagsgerichte();
		menuListAdapter = new MenuListAdapter(this.getApplicationContext(),selectedMittagsgerichte,preistyp);
		lstLunch.setAdapter(menuListAdapter);
		lstLunch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				gesamtBetrag += selectedMittagsgerichte.get(position).preisStud;
				updateBetragsLabel(gesamtBetrag);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, 0, Menu.NONE,"Speisekarte aktualisieren");
		
		SubMenu submenu = menu.addSubMenu(Menu.NONE, 1, Menu.NONE,"Preiskategorie auswählen");
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
			setListAdapter(selectedDay, PREIS_STUDENT);
		}
		if(item.getItemId()==12) {
			setListAdapter(selectedDay, PREIS_ANGESTELLTER);
		}
		if(item.getItemId()==13) {
			setListAdapter(selectedDay, PREIS_GAST);
		}		
		return super.onOptionsItemSelected(item);	
	}
}
