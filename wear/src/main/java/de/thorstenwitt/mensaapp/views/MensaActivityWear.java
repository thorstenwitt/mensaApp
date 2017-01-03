package de.thorstenwitt.mensaapp.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import de.thorstenwitt.mensaapp.R;

/**
 * Created by dev on 02.12.16.
 */


public class MensaActivityWear extends Activity implements WearableListView.ClickListener{

    private String[] lunches = {"Geschnitzeltes nach Gyros Art", "Rindersauerbraten mit Sauce", "Tellerrösti mediterrainer Art", "Hähnbrust natur mit Letschosauce"};
    WearableListView listView;
    LinearLayout linearLayout;
    ImageButton dateButton;
    RelativeLayout rectLayout;
    RelativeLayout roundLayout;
    private static final int PICK_DATE_FROM_ACTIVITY=1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_wear);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);


        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                listView = (WearableListView) findViewById(R.id.wearable_list);
                linearLayout = (LinearLayout) findViewById(R.id.frame_layout);
                dateButton = (ImageButton) findViewById(R.id.date_button);

                dateButton.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                      @Override
                      public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                          int chin = windowInsets.getSystemWindowInsetBottom();
                          linearLayout.setPadding(0,0,0,chin);
                          return windowInsets;
                      }
                  });


                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentDate = new Intent(getApplicationContext(), ChooseDateActivityWear.class);
                        startActivityForResult(intentDate, PICK_DATE_FROM_ACTIVITY);


                    }
                });

                loadAdapter(lunches);
            }
        });
    }

    private  void loadAdapter(String[] pLunches) {

        listView.setAdapter(new LunchListAdapterWear(this, pLunches));
        listView.setClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_DATE_FROM_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String[] mylunches = {"Schnitzel mit Champignons", "Senfeier mit Kartoffeln"};
                String test=data.getStringExtra("DATE");
                loadAdapter(mylunches);
            }
        }
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {

        String s = lunches[viewHolder.getAdapterPosition()];
        Toast.makeText(this, 2.55+viewHolder.getAdapterPosition()+" €",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTopEmptyRegionClick() {

    }

    public class LunchListAdapterWear extends WearableListView.Adapter {

        private final Context context;
        private final String[] lunches;

        public LunchListAdapterWear (Context context, String[] lunches) {
            System.out.println("teleeel");
            this.context = context;
            this.lunches = lunches;

        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(new LunchItemView(context));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
            LunchItemView lunchItemView = (LunchItemView) viewHolder.itemView;

            TextView textView = (TextView) lunchItemView.findViewById(R.id.itemName);
            textView.setText(lunches[position]);

            ImageView imageView = (ImageView) lunchItemView.findViewById(R.id.itemIcon);
            imageView.setImageResource(R.drawable.tag_purple);
;
        }


        @Override
        public int getItemCount() {
            return lunches.length;
        }
    }

}
