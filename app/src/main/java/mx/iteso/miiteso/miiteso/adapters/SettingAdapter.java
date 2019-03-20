package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.model.ItemMainNotice;
import mx.iteso.miiteso.miiteso.model.ItemSetting;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class SettingAdapter extends ArrayAdapter<ItemSetting> implements onTaskCompleted {
    private Activity activity;
    private ArrayList<ItemSetting> itemSettings;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public SettingAdapter(@NonNull Activity activity, int resource, ArrayList<ItemSetting> itemSettings) {
        super(activity, resource, itemSettings);

        this.activity = activity;
        this.itemSettings = itemSettings;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View item = inflater.inflate(R.layout.item_setting, parent, false);

        TextView lbl_setting = (TextView) item.findViewById(R.id.lbl_setting);
        Switch sw_off_on = (Switch) item.findViewById(R.id.sw_off_on);

        lbl_setting.setText(itemSettings.get(position).getLbl_setting());
        sw_off_on.setChecked(itemSettings.get(position).isSw_off_on());

        if (itemSettings.get(position).getLbl_setting().equals("Moodle"))
            sw_off_on.setEnabled(true);

        if (itemSettings.get(position).getLbl_setting().equals("Horario Escolar"))
            sw_off_on.setEnabled(true);

        if (itemSettings.get(position).getLbl_setting().equals("Calendario Escolar"))
            sw_off_on.setEnabled(false);

        lbl_setting.setTypeface(new Metodos(getContext()).typeface(Metodos.TypeFont.MontserratRegular));

        sw_off_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked)
                    new Metodos(getContext()).setSharedPreference(getContext().getString(R.string.preference_file_key),
                            itemSettings.get(position).getNameSetting(), "1");
                else
                    new Metodos(getContext()).setSharedPreference(getContext().getString(R.string.preference_file_key),
                            itemSettings.get(position).getNameSetting(), "0");

            }
        });

        Constantes.isChangedSettings = true;
        return item;
    }

    public void getCourses() {
        try {
            this.clear();
            itemSettings.clear();
            Constantes.KEY_NAME = null;
            String service = "";
            //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
            service = MethodWS.WS_MOODLE_COURSES + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");

            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        } finally {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onTaskCompleted(String response) {
        {
            ItemSetting itemSetting;
            try {
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {


                        itemSetting =
                                new ItemSetting(jsonArray.getJSONObject(i).getString("category")
                                        , jsonArray.getJSONObject(i).getBoolean("category")
                                        , jsonArray.getJSONObject(i).getBoolean("obligatorio"),
                                        "");
                        this.itemSettings.add(itemSetting);

                    }
                    this.addAll(this.itemSettings);
                    notifyDataSetChanged();
                } else {
                    this.itemSettings.add(new ItemSetting("vacio"));
                    this.addAll(this.itemSettings);
                    notifyDataSetChanged();
                }

            } catch (JSONException ex) {
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show();
                this.clear();
                this.itemSettings.clear();
                this.itemSettings.add(new ItemSetting("error"));
                this.addAll(this.itemSettings);
                notifyDataSetChanged();

            } finally {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
