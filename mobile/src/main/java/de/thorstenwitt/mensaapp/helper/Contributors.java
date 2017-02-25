package de.thorstenwitt.mensaapp.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import de.thorstenwitt.mensaapp.R;
//import android.preference.PreferenceManager;


/**
 * Created by karst.christopher on 03.01.17.
 */

public class Contributors {

    private Activity mActivity;

    public Contributors(Activity context) {
        mActivity = context;
    }

    public void show() {
        String message = "<b>Team</b>" +
                        "<p>" +
                        "Thorsten Witt, Robert Freese, Christopher Karst, Sascha Schwarz, Steven Kannewischer" +
                        "<p>" +
                        "<b>Urheberrecht</b>" +
                        "<p>" +
                        "Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen Seiten unterliegen dem deutschen Urheberrecht. Die Vervielfältigung, Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der Grenzen des Urheberrechtes bedürfen der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers. Downloads und Kopien dieser Seite sind nur für den privaten, nicht kommerziellen Gebrauch gestattet." +
                        "<p>" +
                        "Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden, werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte Dritter als solche gekennzeichnet. Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden, bitten wir um einen entsprechenden Hinweis. Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Inhalte umgehend entfernen." +
                        "<p>" +
                        "<b>Studentenwerk Greifswald</b>" +
                        "<p>" +
                        "<a href=http://studwerk.fh-stralsund.de> Studentenwerk </a> ";

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("Disclaimer");

                LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.contributor_dialog, null);

                TextView text = (TextView) layout.findViewById(R.id.contributors);
                text.setMovementMethod(LinkMovementMethod.getInstance());
                if (Build.VERSION.SDK_INT >= 24) {
                    text.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
                }else {
                    text.setText(Html.fromHtml(message));
                }
                builder.setView(layout);
                builder.setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.finish();
                    }

                });
        builder.create().show();
        //}
    }


}