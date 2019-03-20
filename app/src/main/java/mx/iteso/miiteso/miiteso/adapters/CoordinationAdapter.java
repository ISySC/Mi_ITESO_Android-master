package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.model.ItemCoordination;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class CoordinationAdapter extends ArrayAdapter<ItemCoordination> {

    private List<ItemCoordination> itemCoordinations;
    private Activity activity;

    public CoordinationAdapter(@NonNull Activity activity, int resource, ArrayList<ItemCoordination> itemCoordinations) {
        super(activity, resource, itemCoordinations);

        this.activity = activity;
        this.itemCoordinations = itemCoordinations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.item_coordination, parent, false);

        TextView lbl_message = (TextView) itemView.findViewById(R.id.lbl_message);
        TextView lbl_date = (TextView) itemView.findViewById(R.id.date);

        lbl_message.setText(itemCoordinations.get(position).getLbl_message());
        lbl_date.setText(itemCoordinations.get(position).getLbl_date());

        lbl_message.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratMedium));
        lbl_date.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));

        return itemView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
