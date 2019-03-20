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
import mx.iteso.miiteso.miiteso.LoginActivity;
import mx.iteso.miiteso.miiteso.SettingActivity;
import mx.iteso.miiteso.miiteso.model.ItemConfiguration;
import mx.iteso.miiteso.miiteso.model.ItemKardexGeneral;
import mx.iteso.miiteso.miiteso.model.ItemMateriaKardex;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 20/02/2018.
 */

public class MateriaKardexDetalleAdapter extends ArrayAdapter<ItemMateriaKardex> {
    private List<ItemMateriaKardex> itemMateriaKardexes;
    private Activity activity;

    public MateriaKardexDetalleAdapter(Activity activity, int resource, ArrayList<ItemMateriaKardex> itemMateriaKardexes) {
        super(activity, resource, itemMateriaKardexes);

        this.activity = activity;
        this.itemMateriaKardexes = itemMateriaKardexes;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.item_materia_detalle_ciclo, parent, false);

        TextView tv_materia = (TextView) item.findViewById(R.id.tv_materia);
        TextView tv_calificacion = (TextView) item.findViewById(R.id.tv_calificacion);


        tv_materia.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
        tv_calificacion.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));

        tv_materia.setText(itemMateriaKardexes.get(position).getMateria());
        tv_calificacion.setText(itemMateriaKardexes.get(position).getCalificacion());

        return item;
    }
}
