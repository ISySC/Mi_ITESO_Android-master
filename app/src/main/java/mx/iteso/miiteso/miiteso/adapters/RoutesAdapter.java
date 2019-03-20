package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.model.ItemRoutes;
import mx.iteso.miiteso.utilidades.Metodos;

public class RoutesAdapter extends ArrayAdapter<ItemRoutes> {
    private ArrayList<ItemRoutes> itemRoutes;
    private Activity activity;
    private View item;

    private LayoutInflater inflater;

    private TextView tvRouteID;

    public RoutesAdapter(@NonNull Activity activity, int resource, ArrayList<ItemRoutes> itemRoutes) {
        super(activity, resource, itemRoutes);

        this.activity = activity;
        this.itemRoutes = itemRoutes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        inflater = activity.getLayoutInflater();
        item = inflater.inflate(R.layout.item_routes, parent, false);

        setInitUI();
        setValues(position);
        setTypeFont();

        return item;
    }

    private void setValues(int position) {
        tvRouteID.setText(itemRoutes.get(position).getNombreRuta());
    }

    private void setTypeFont() {
        tvRouteID.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitUI() {
        tvRouteID = item.findViewById(R.id.tvRouteID);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
