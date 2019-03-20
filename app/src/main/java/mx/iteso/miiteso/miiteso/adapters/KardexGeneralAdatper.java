package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.common.internal.StringResourceValueReader;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.MateriaKardexDetalleActivity;
import mx.iteso.miiteso.miiteso.model.ItemKardexGeneral;
import mx.iteso.miiteso.miiteso.onPromedioKardex;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 24/10/2018.
 */

public class KardexGeneralAdatper extends ArrayAdapter<ItemKardexGeneral> implements onTaskCompleted {

    private Activity activity;
    private ArrayList<ItemKardexGeneral> itemKardexGeneral = new ArrayList<>();
    boolean[] animationStates;
    ServiciosWeb servicioWeb;
    SwipeRefreshLayout mSwipeRefreshLayout;

    JSONArray jsonArrayRutaCurricular = null;
    JSONArray jsonArrayhistorialAcademico = null;

    private int TOTAL_CICLOS_CURSADOS = 0;
    private int PORCENTAJE_AVANCE = 0;
    private double PROMEDIO_GENERAL = 0;
    private int TOTAL_MATERIAS = 0;


    public KardexGeneralAdatper(Activity activity, int resource, SwipeRefreshLayout mSwipeRefreshLayout, ServiciosWeb servicioWeb) {
        super(activity, resource);
        this.activity = activity;
        this.servicioWeb = servicioWeb;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setRefreshing(true);
        getSubjects();
    }

    public void getSubjects() {
        try {
            this.clear();
            itemKardexGeneral.clear();
            Constantes.KEY_NAME = null;
            String service = "";
            servicioWeb = ServiciosWeb.RutaCurricular;
            service = MethodWS.WS_RUTA_CURRICULAR + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");

            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final LayoutInflater inflater = activity.getLayoutInflater();
        if (position >= itemKardexGeneral.size())
            position = itemKardexGeneral.size() - 1;

        View item = inflater.inflate(R.layout.item_route, parent, false);

        RelativeLayout rl_materia = item.findViewById(R.id.rl_materia);
        final String cicloNombre = "Ciclo " + String.valueOf(position); // itemKardexGeneral.get(position).getArea();
        TextView tvCiclo = (TextView) item.findViewById(R.id.tv_ciclo);
        TextView tvPorcentaje = (TextView) item.findViewById(R.id.tv_porcentaje);
        ProgressBar progressBar = (ProgressBar) item.findViewById(R.id.progressBar);
        ImageView imgMore = (ImageView) item.findViewById(R.id.img_more);

        tvCiclo.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
        tvPorcentaje.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));

        tvCiclo.setText(itemKardexGeneral.get(position).getNombreCiclo());
        tvPorcentaje.setText(String.valueOf(itemKardexGeneral.get(position).getPorcentajeAvance()) + "%");

        progressBar.setProgress(itemKardexGeneral.get(position).getPorcentajeAvance());

        if (!animationStates[position]) {
            anim(position, item);
        }

        final int finalPosition = position;

        rl_materia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MateriaKardexDetalleActivity.class);
                intent.putExtra("nombreCiclo", itemKardexGeneral.get(finalPosition).getNombreCiclo());
                intent.putExtra("numeroCiclo", String.valueOf(finalPosition));
                intent.putExtra("porcentajeAvance", String.valueOf(itemKardexGeneral.get(finalPosition).getPorcentajeAvance()));
                getContext().startActivity(intent);
            }
        });

        return item;
    }


    private void anim(int position, View item) {
        Log.e("TAG", "Animating item no: " + position);
        animationStates[position] = true;
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        animation.setStartOffset(position * 500);
        item.startAnimation(animation);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public void onTaskCompleted(String response) {
        try {
            switch (servicioWeb) {
                case RutaCurricular:
                    getRutaCurricular(response);
                    break;
                case HistorialAcademica:
                    getHistorialAcademico(response);
                    break;
            }


        } catch (Exception ex) {
            Log.e("errorRuta", ex.toString());
            //Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show();
            //this.itemKardexGeneral.add(new ItemHistorialAcademico("error"));
            //this.addAll(this.itemKardexGeneral);
            //notifyDataSetChanged();

        } finally {
            animationStates = new boolean[this.itemKardexGeneral.size()];
            this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getHistorialAcademico(String response) {
        Log.i("response", response);

        try {
            jsonArrayhistorialAcademico = new JSONArray(response);

            if (jsonArrayRutaCurricular.length() > 0 && jsonArrayhistorialAcademico.length() > 0)
                setPlanGeneral();

        } catch (JSONException ex) {
            Log.e("errorHistorialAc", ex.toString());
        }


    }

    private void setPlanGeneral() {
        TOTAL_CICLOS_CURSADOS = 0;
        PROMEDIO_GENERAL = 0;
        PORCENTAJE_AVANCE = 0;
        TOTAL_MATERIAS = 0;

        String nombreCiclo = "";

        int porcentajeAvance = 0;
        int creditosRuta = 0;
        int creditosHistorial = 0;

        try {
            nombreCiclo = jsonArrayRutaCurricular.getJSONObject(0).getString("ciclo");
            creditosRuta = Integer.parseInt(jsonArrayRutaCurricular.getJSONObject(0).getString("creditos"));

            for (int indexRuta = 0; indexRuta <= jsonArrayRutaCurricular.length() - 1; indexRuta++) {
                if (!nombreCiclo.equals(jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("ciclo"))) {

                    if (creditosHistorial > 0)
                        porcentajeAvance = ((creditosHistorial * 100) / creditosRuta);

                    Log.i("ciclo" + nombreCiclo, String.valueOf(creditosHistorial) + "ruta:" +  String.valueOf(creditosRuta));

                    if (porcentajeAvance > 0) {
                        PORCENTAJE_AVANCE += porcentajeAvance;
                        TOTAL_CICLOS_CURSADOS += 1;
                    }

                    itemKardexGeneral.add(new ItemKardexGeneral(porcentajeAvance, nombreCiclo));

                    porcentajeAvance = 0;
                    creditosHistorial = 0;
                    creditosRuta = 0;

                    nombreCiclo = jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("ciclo");
                    creditosRuta = Integer.parseInt(jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("creditos"));

                    for (int indexHistorial = 0; indexHistorial <= jsonArrayhistorialAcademico.length() - 1; indexHistorial++) {

                        if (jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("materia").equals(
                                jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("asignatura"))
                                && !jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("calificacion").equals("Inscrita")) {
                            creditosHistorial += Integer.parseInt(jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("creditos"));
                            PROMEDIO_GENERAL += Integer.parseInt(jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("calificacion"));
                            TOTAL_MATERIAS += 1;
                        }
                    }

                } else {
                    creditosRuta += Integer.parseInt(jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("creditos"));

                    for (int indexHistorial = 0; indexHistorial <= jsonArrayhistorialAcademico.length() - 1; indexHistorial++) {

                        if (jsonArrayRutaCurricular.getJSONObject(indexRuta).getString("materia").equals(
                                jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("asignatura"))
                                && !jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("calificacion").equals("Inscrita")) {
                            creditosHistorial += Integer.parseInt(jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("creditos"));
                            PROMEDIO_GENERAL += Integer.parseInt(jsonArrayhistorialAcademico.getJSONObject(indexHistorial).getString("calificacion"));
                            TOTAL_MATERIAS += 1;
                        }
                    }
                }
            }

            itemKardexGeneral.add(new ItemKardexGeneral(porcentajeAvance, nombreCiclo));

            ((TextView) activity.findViewById(R.id.lbl_curses_number)).setText(String.valueOf(TOTAL_CICLOS_CURSADOS));


            DecimalFormat formato = new DecimalFormat("#.00");

            if (PORCENTAJE_AVANCE > 0)
                ((TextView) activity.findViewById(R.id.lbl_porcentaje_number)).setText(String.valueOf(((PORCENTAJE_AVANCE * 100) / (itemKardexGeneral.size() * 100))) + "%");

            if (PROMEDIO_GENERAL > 0)
                ((TextView) activity.findViewById(R.id.lbl_average_number)).setText(String.valueOf(formato.format(PROMEDIO_GENERAL / TOTAL_MATERIAS)));

            Log.i("TOTAL_MATERIAS", String.valueOf(TOTAL_MATERIAS));

            //onPromedioKardexTotales.onPromedioKardex(String.valueOf(Constantes.TOTAL_CICLOS_CURSADOS));
            this.addAll(this.itemKardexGeneral);
            notifyDataSetChanged();

            animationStates = new boolean[this.itemKardexGeneral.size()];
            this.mSwipeRefreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            Log.e("errorAdapter", e.toString());
        }

    }

    private void getRutaCurricular(String response) {
        Log.i("response", response);

        try {
            jsonArrayRutaCurricular = new JSONArray(response);

            if (jsonArrayRutaCurricular.length() > 0) {

                this.servicioWeb = ServiciosWeb.HistorialAcademica;

                String service = "";
                //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
                service = MethodWS.WS_HISTORIAL_ACADEMICA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");
                new NetServices(this, activity, false).execute("post", service);
            }

        } catch (JSONException ex) {
            Log.e("errorRutaCurricular", ex.toString());
        }
    }

}