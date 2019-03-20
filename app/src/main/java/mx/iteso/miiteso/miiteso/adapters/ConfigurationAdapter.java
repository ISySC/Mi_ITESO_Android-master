package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.AboutActivity;
import mx.iteso.miiteso.miiteso.ConfigurationActivity;
import mx.iteso.miiteso.miiteso.LoginActivity;
import mx.iteso.miiteso.miiteso.SettingActivity;
import mx.iteso.miiteso.miiteso.model.ItemConfiguration;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 20/02/2018.
 */

public class ConfigurationAdapter extends ArrayAdapter<ItemConfiguration> {
    private List<ItemConfiguration> itemConfigurations;
    private Activity activity;

    public ConfigurationAdapter(Activity activity, int resource, ArrayList<ItemConfiguration> itemConfigurations) {
        super(activity, resource, itemConfigurations);

        this.activity = activity;
        this.itemConfigurations = itemConfigurations;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_configuration, parent, false);

        TextView lbl_option = (TextView) item.findViewById(R.id.lbl_option);
        ImageView ic_option = (ImageView) item.findViewById(R.id.ic_option);
        RelativeLayout rl_option = (RelativeLayout) item.findViewById(R.id.rl_option);

        rl_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        setting();
                        break;
                    case 1:
                        logoutSession();
                        break;
                    case 2:
                        about();
                        break;
                }
            }
        });

        lbl_option.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));

        lbl_option.setText(itemConfigurations.get(position).getLbl_option());
        ic_option.setImageResource(itemConfigurations.get(position).getIc_option());

        return item;
    }

    private void about() {
        new Metodos(getContext()).NavegarAPantalla(AboutActivity.class);
    }

    private void logoutSession() {
        new Metodos(activity).alertDialog(activity.getString(R.string.set_message_title_logout),
                activity.getString(R.string.set_message_sing_off))
                .setPositiveButton(activity.getString(R.string.set_message_title_logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Metodos(activity).setSharedPreference(activity.getString(R.string.preference_file_key),
                                "sessionActive", "0");
                        new Metodos(activity).NavegarAPantalla(LoginActivity.class);
                    }
                }).setNegativeButton(activity.getString(R.string.set_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    private void setting() {
        new Metodos(activity).NavegarAPantalla(SettingActivity.class);
    }

}
