package de.thorstenwitt.mensaapp.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    RelativeLayout rectLayout;
    RelativeLayout roundLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa_wear);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                listView = (WearableListView) findViewById(R.id.wearable_list);
                loadAdapter();
            }
        });
    }

    private  void loadAdapter() {

        listView.setAdapter(new LunchListAdapterWear(this, lunches));
        listView.setClickListener(this);
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
