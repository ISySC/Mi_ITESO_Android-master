package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.MateriaKardexDetalleActivity;
import mx.iteso.miiteso.miiteso.MateriaPorAreaDetalleActivity;
import mx.iteso.miiteso.miiteso.model.ItemArea;
import mx.iteso.miiteso.miiteso.model.ItemCreditsArea;
import mx.iteso.miiteso.miiteso.model.ItemMateriaKardex;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

public class CreditAreaAdapter extends ArrayAdapter<ItemArea> implements onTaskCompleted {

    private Activity activity;
    boolean[] animationStates;
    ArrayList<ItemArea> itemAreas = new ArrayList<>();

    ServiciosWeb servicioWeb;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String service = "";

    JSONArray jsonArrayHistoriaAcademica = null;
    JSONArray jsonArrayRuta = null;

    int servicio = 0;

    public CreditAreaAdapter(@NonNull Activity activity, int resource, ServiciosWeb servicioWeb, SwipeRefreshLayout mSwipeRefreshLayout) {
        super(activity, resource);
        this.activity = activity;
        this.servicioWeb = servicioWeb;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setRefreshing(true);
        getAreas();
    }

    public void getCredits() {
        try {
            this.clear();
            servicio = 1;
            Constantes.KEY_NAME = null;
            service = MethodWS.WS_HISTORIAL_ACADEMICA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");
            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }


    public void getAreas() {
        try {
            this.clear();
            servicio = 0;
            Constantes.KEY_NAME = null;
            service = MethodWS.WS_CREDITOS_AREA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");
            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position >= this.getCount())
            position = this.getCount() - 1;
        if (!this.getItem(0).getSuperArea().equals("error") &&
                !this.getItem(0).getSuperArea().equals("vacio")) {

            View item = inflater.inflate(R.layout.item_route, parent, false);

            RelativeLayout rl_materia = item.findViewById(R.id.rl_materia);
            final String cicloNombre = "Ciclo " + String.valueOf(position); // itemKardexGeneral.get(position).getArea();
            TextView tvCiclo = (TextView) item.findViewById(R.id.tv_ciclo);
            TextView tvPorcentaje = (TextView) item.findViewById(R.id.tv_porcentaje);
            ProgressBar progressBar = (ProgressBar) item.findViewById(R.id.progressBar);
            ImageView imgMore = (ImageView) item.findViewById(R.id.img_more);

            tvCiclo.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            tvPorcentaje.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));

            tvCiclo.setText(this.getItem(position).getSuperArea());
            tvPorcentaje.setText(String.valueOf(this.getItem(position).getCreditosActuales()) + "/" + String.valueOf(this.getItem(position).getCreditosTotales()));

            if (this.getItem(position).getCreditosTotales() == 0) {
                progressBar.setMax(1);
                progressBar.setProgress(1);
            } else {
                progressBar.setMax(this.getItem(position).getCreditosTotales());
                progressBar.setProgress(this.getItem(position).getCreditosActuales());
            }


            final int finalPosition = position;
            rl_materia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MateriaKardexDetalleActivity.class);
                    intent.putExtra("nombreCiclo", CreditAreaAdapter.this.getItem(finalPosition).getSuperArea());
                    intent.putExtra("materias", (Serializable) CreditAreaAdapter.this.getItem(finalPosition).getItems());
                    getContext().startActivity(intent);
                }
            });

            return item;
        } else {
            View item = inflater.inflate(R.layout.layout_no_data, parent, false);
            if (this.getItem(0).getSuperArea() == "vacio") {
                ((TextView) item.findViewById(R.id.tv_message)).setText("No hay información para mostrar");
                //((ImageView) item.findViewById(R.id.img_error)).setImageResource(R.drawable.ic_not_info);
                if (!animationStates[position])
                    anim(position, item);
            }
            return item;
        }

    }

    private void anim(int position, View item) {
        Log.e("TAG", "Animating item no: " + position);
        animationStates[position] = true;
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        animation.setStartOffset(position * 500);
        item.startAnimation(animation);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public void onTaskCompleted(String response) {
        if (servicio == 1) {
            try {
                ItemArea item = null;
                ItemArea itemAreatemp = null;
                boolean existe = false;
                if (response != null) {

                    jsonArrayRuta = new JSONArray(response);

                    for (int index = 0; index <= jsonArrayRuta.length() - 1; index++) {
                        existe = false;
                        if (jsonArrayRuta.getJSONObject(index).has("superArea")) {
                            item = new ItemArea(jsonArrayRuta.getJSONObject(index).getString("superArea"));

                            for (ItemArea itemArea : itemAreas) {
                                if (itemArea.getSuperArea().equals(item.getSuperArea())) {
                                    existe = true;
                                    itemAreatemp = itemArea;
                                }

                            }
                            if (existe) {
                                itemAreatemp.addItem(new ItemMateriaKardex(
                                        jsonArrayRuta.getJSONObject(index).getString("asignatura"),
                                        jsonArrayRuta.getJSONObject(index).getString("calificacion")));
                            }
                        }
                    }
                    Log.i("ITEMS_CREDITS", "La cantidad de Areas Encontradas es de " + String.valueOf(itemAreas.size()));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                this.addAll(itemAreas);
                this.notifyDataSetChanged();
                this.mSwipeRefreshLayout.setRefreshing(false);

            }
        }

        if (servicio == 0) {
            itemAreas.clear();
            String[] area = null;
            try {
                if (response != null) {

                    jsonArrayRuta = new JSONArray(response);

                    for (int index = 0; index < jsonArrayRuta.length(); index++) {
                        area = jsonArrayRuta.getJSONObject(index).getString("area").split("-");

                        if (area.length == 2) {
                            itemAreas.add(new ItemArea(area[0].trim(),
                                    Integer.valueOf(jsonArrayRuta.getJSONObject(index).getString("creditosRequeridos")),
                                    Integer.valueOf(jsonArrayRuta.getJSONObject(index).getString("credAprobados"))));
                        }
                    }
                    Log.i("ITEMS_CREDITS", "La cantidad de Areas Encontradas es de " + String.valueOf(itemAreas.size()));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                getCredits();
            }
        }

    }
}
