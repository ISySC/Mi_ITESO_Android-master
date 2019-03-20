package mx.iteso.miiteso.miiteso;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.itesubes.ChooseRouteActivity;
import mx.iteso.miiteso.miiteso.adapters.MateriaKardexDetalleAdapter;
import mx.iteso.miiteso.miiteso.model.ItemMateriaKardex;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

public class MateriaKardexDetalleActivity extends AppCompatActivity implements onTaskCompleted {
    private String nombreCiclo;
    private String numeroCiclo;
    private TextView lbl_header_title;
    private ImageView ic_arrow_back;

    private ServiciosWeb servicioWeb;
    private ArrayList<ItemMateriaKardex> itemMateriaKardexes = null;
    private MateriaKardexDetalleAdapter materiaKardexDetalleAdapter = null;

    private ListView lst_materias;

    JSONArray jsonArrayRutaCurricular = null;
    JSONArray jsonArrayhistorialAcademico = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_materia_kardex_detalle);

        setInitUI();
        setTypography();

        itemMateriaKardexes = (ArrayList<ItemMateriaKardex>) getIntent().getExtras().getSerializable("materias");

        if (itemMateriaKardexes == null) {
            getDetalleCiclo();

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                nombreCiclo = bundle.getString("nombreCiclo");
                numeroCiclo = bundle.getString("numeroCiclo");
                lbl_header_title.setText(nombreCiclo + "       " + bundle.getString("porcentajeAvance") + "%");
            }
        } else {
            lbl_header_title.setText(getIntent().getExtras().getString("nombreCiclo"));
            materiaKardexDetalleAdapter = new MateriaKardexDetalleAdapter(this, 0, itemMateriaKardexes);
            lst_materias.setAdapter(materiaKardexDetalleAdapter);
        }
    }

    private void getDetalleCiclo() {

        //Constantes.progressDialog = new Metodos(this).showProgressDialog("Cargando información");

        servicioWeb = ServiciosWeb.RutaCurricular;

        String service = "";
        //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
        service = MethodWS.WS_RUTA_CURRICULAR + new Metodos(this).getSharedPreference(getString(R.string.preference_file_key), "token");
        new NetServices(this, this, true).execute("post", service);
    }

    private void setInitUI() {
        lbl_header_title = findViewById(R.id.lbl_header_title);
        ic_arrow_back = findViewById(R.id.ic_one);
        lst_materias = findViewById(R.id.lst_materias);

        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTypography() {
        lbl_header_title.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
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
            if (Constantes.progressDialog != null)
                Constantes.progressDialog.dismiss();
        }

    }

    private void getHistorialAcademico(String response) {
        Log.i("response", response);

        try {
            jsonArrayhistorialAcademico = new JSONArray(response);

            if (jsonArrayRutaCurricular.length() > 0 && jsonArrayhistorialAcademico.length() > 0) {
                itemMateriaKardexes = new ArrayList<ItemMateriaKardex>();

                String[] materiasRuta = new String[15];
                String[] materiasHistorial = new String[15];
                String[] calificacionAsignatura = new String[15];

                int i = 0;

                for (int index = 0; index <= jsonArrayRutaCurricular.length() - 1; index++) {
                    if (jsonArrayRutaCurricular.getJSONObject(index).getString("ciclo").equals(nombreCiclo)) {
                        materiasRuta[i] = jsonArrayRutaCurricular.getJSONObject(index).getString("materia");
                        i++;
                    }
                }

                i = 0;
                for (int index = 0; index <= jsonArrayhistorialAcademico.length() - 1; index++) {
                    if (jsonArrayhistorialAcademico.getJSONObject(index).getString("cicloSugerido").equals(numeroCiclo)) {
                        materiasHistorial[i] = jsonArrayhistorialAcademico.getJSONObject(index).getString("asignatura");
                        calificacionAsignatura[i] = jsonArrayhistorialAcademico.getJSONObject(index).getString("calificacion");
                        i++;
                    }
                }

                boolean existeMateria = false;
                for (int index = 0; index <= materiasRuta.length - 1; index++) {
                    for (int index1 = 0; index1 <= materiasHistorial.length - 1; index1++) {
                        if (materiasRuta[index] != null) {
                            if (materiasRuta[index].equals(materiasHistorial[index1])) {
                                itemMateriaKardexes.add(new ItemMateriaKardex(materiasHistorial[index1],
                                        calificacionAsignatura[index1]));
                                existeMateria = true;
                                break;
                            }
                        }
                    }


                    if (existeMateria == false)
                        if (materiasRuta[index] != null)
                            itemMateriaKardexes.add(new ItemMateriaKardex(materiasRuta[index],
                                    "Sin Cursar"));

                    existeMateria = false;
                }

                materiaKardexDetalleAdapter = new MateriaKardexDetalleAdapter(this, 0, itemMateriaKardexes);
                lst_materias.setAdapter(materiaKardexDetalleAdapter);
                Constantes.progressDialog.dismiss();
            }

        } catch (JSONException ex) {
            Log.e("errorHistorialAc", ex.toString());
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
                service = MethodWS.WS_HISTORIAL_ACADEMICA + new Metodos(this).getSharedPreference(getString(R.string.preference_file_key), "token");
                ;
                new NetServices(this, this, false).execute("post", service);
            }

        } catch (JSONException ex) {
            Log.e("errorRutaCurricular", ex.toString());
        }
    }
}
