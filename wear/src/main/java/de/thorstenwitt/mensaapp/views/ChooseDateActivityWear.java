package de.thorstenwitt.mensaapp.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.thorstenwitt.mensaapp.R;

/**
 * Created by dev on 03.01.17.
 */

public class ChooseDateActivityWear extends Activity implements WearableListView.ClickListener {

    public String[] dates={"Keine Daten"};
    WearableListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_date_activity_mensa_wear);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            dates = bundle.getStringArray("DATES");
        }


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                listView = (WearableListView) findViewById(R.id.wearable_date_list);
                loadAdapter();
            }

        });

    }
    private void loadAdapter() {

        listView.setAdapter(new ChooseDateActivityWear.DateListAdapterWear(this, dates));
        listView.setClickListener(this);
    }

    public class DateListAdapterWear extends WearableListView.Adapter {

        private final Context context;
        private final String[] dates;

        public DateListAdapterWear (Context context, String[] dates) {
            this.context = context;
            this.dates = dates;

        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(new DateItemView(context));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
            DateItemView dateItemView = (DateItemView) viewHolder.itemView;

            TextView textView = (TextView) dateItemView.findViewById(R.id.itemName);
            textView.setText(dates[position]);

            ImageView imageView = (ImageView) dateItemView.findViewById(R.id.itemIcon);
            imageView.setImageResource(R.drawable.calendar_view_day);

        }


        @Override
        public int getItemCount() {
            return dates.length;
        }
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Intent intent = new Intent();
        intent.putExtra("SELECTEDDATE",viewHolder.getAdapterPosition());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
