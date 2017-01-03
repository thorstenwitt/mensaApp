package de.thorstenwitt.mensaapp.views;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import de.thorstenwitt.mensaapp.R;

/**
 * Created by dev on 13.12.16.
 */

public class DateItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final ImageView image;
    final TextView text;

    public DateItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.list_item, this);
        image = (ImageView) findViewById(R.id.itemIcon);
        text = (TextView) findViewById(R.id.itemName);
    }

    @Override
    public void onCenterPosition(boolean b) {

    }

    @Override
    public void onNonCenterPosition(boolean b) {
    }
}
