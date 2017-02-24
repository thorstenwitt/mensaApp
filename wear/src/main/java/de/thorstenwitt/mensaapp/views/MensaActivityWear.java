package de.thorstenwitt.mensaapp.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.thorstenwitt.mensaapp.DataLayerListenerService;
import de.thorstenwitt.mensaapp.R;
import de.thorstenwitt.mensaapp.common.businessobject.Lunch;
import de.thorstenwitt.mensaapp.common.businessobject.LunchOffer;
import de.thorstenwitt.mensaapp.common.businessobject.Mensa;

/**
 * Created by dev on 02.12.16.
 */


public class MensaActivityWear extends Activity implements WearableListView.ClickListener{


    WearableListView listView;
    LinearLayout linearLayout;
    LinearLayout progressBarLayout;
    ImageButton dateButton;
    private static final int PICK_DATE_FROM_ACTIVITY=1;
    private Mensa mensaData;
    private int selectedDate;
    private DataLayerListenerService dls;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_wear);
        ArrayList<Lunch> lunchlist = new ArrayList<>();
        lunchlist.add(new Lunch("Smartphone App -> Speisekarte aktualisieren",1,1,1,false));
        LunchOffer lo = new LunchOffer("Keine Daten", lunchlist);
        ArrayList<LunchOffer> lolist = new ArrayList<>();
        dls = new DataLayerListenerService(this);
        dls.getGoogleApiClient().connect();

        lolist.add(lo);
        Mensa mensa = new Mensa("Mensa1", lolist);
        mensaData = mensa;

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);


        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {


                progressBarLayout = (LinearLayout) findViewById(R.id.progressbar_layout);
                listView = (WearableListView) findViewById(R.id.wearable_list);
                linearLayout = (LinearLayout) findViewById(R.id.frame_layout);
                dateButton = (ImageButton) findViewById(R.id.date_button);

                dateButton.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                      @Override
                      public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                          int chin = windowInsets.getSystemWindowInsetBottom();
                          stub.setPadding(0,0,0,chin);
                          return windowInsets;
                      }
                  });


                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] availableDates = new String[mensaData.getLunchOffers().size()];
                        for(int i=0; i<mensaData.getLunchOffers().size(); i++) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date lunchOfferDate = sdf.parse(mensaData.getLunchOffers().get(i).getMydate());
                                SimpleDateFormat targetSdf = new SimpleDateFormat("EEEE, dd.MM.yyyy");
                                String localizedLunchDate = targetSdf.format(lunchOfferDate);
                                        //DateFormat.getDateInstance(DateFormat.FULL).format(lunchOfferDate);
                                availableDates[i] = localizedLunchDate;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        Intent intentDate = new Intent(getApplicationContext(), ChooseDateActivityWear.class);
                        intentDate.putExtra("DATES", availableDates);

                        startActivityForResult(intentDate, PICK_DATE_FROM_ACTIVITY);


                    }
                });

                loadAdapter();
            }
        });
    }

    public void notifyAboutNewMensaData(Mensa mensa) {
        this.mensaData = mensa;
        this.progressBarLayout.setVisibility(View.INVISIBLE);
        this.linearLayout.setVisibility(View.VISIBLE);
        loadAdapter();
    }

    private  void loadAdapter() {
        listView.setAdapter(new LunchListAdapterWear(this, mensaData));
        listView.setClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_DATE_FROM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                selectedDate = data.getIntExtra("SELECTEDDATE",0);
                loadAdapter();
            }
        }
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        float price = mensaData.getLunchOffers().get(selectedDate).getLunchList().get(viewHolder.getAdapterPosition()).getPriceStud();
        String s = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(price);
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    public class LunchListAdapterWear extends WearableListView.Adapter {

        private final Context context;
        private final Mensa mensaData;

        public LunchListAdapterWear (Context context, Mensa mensa) {
            this.context = context;
            this.mensaData = mensa;

        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(new LunchItemView(context));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
            LunchItemView lunchItemView = (LunchItemView) viewHolder.itemView;

            TextView textView = (TextView) lunchItemView.findViewById(R.id.itemName);
            textView.setText(mensaData.getLunchOffers().get(selectedDate).getLunchList().get(position).getmName());

            ImageView imageView = (ImageView) lunchItemView.findViewById(R.id.itemIcon);
            if(mensaData.getLunchOffers().get(selectedDate).getLunchList().get(position).getIsMensaVital()) {
                imageView.setImageResource(R.drawable.tag_green);
                textView.setTextColor(Color.rgb(0,134,61));
            }
            else {
                imageView.setImageResource(R.drawable.tag_purple);
            }


        }


        @Override
        public int getItemCount() {
            return mensaData.getLunchOffers().get(selectedDate).getLunchList().size();
        }
    }

}
