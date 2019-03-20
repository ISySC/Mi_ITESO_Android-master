package mx.iteso.miiteso.miiteso;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.adapters.MateriaKardexDetalleAdapter;
import mx.iteso.miiteso.miiteso.model.ItemMateriaKardex;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

public class MateriaPorAreaDetalleActivity extends AppCompatActivity implements onTaskCompleted {
    private String nombreArea;
    private TextView lbl_header_title;
    private ImageView ic_arrow_back;

    private ServiciosWeb servicioWeb;
    private ArrayList<ItemMateriaKardex> itemMateriaKardexes;
    private MateriaKardexDetalleAdapter materiaKardexDetalleAdapter = null;

    private ListView lst_materias;

    JSONArray jsonArrayhistorialAcademico = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_materia_kardex_detalle);

        setInitUI();
        setTypography();

        getHistorialAcademico();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            nombreArea = bundle.getString("nombreArea");
            lbl_header_title.setText(nombreArea);
        }
    }

    private void getHistorialAcademico() {

        Constantes.progressDialog = new Metodos(this).showProgressDialog("Cargando información");

        String service = "";
        //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
        service = MethodWS.WS_HISTORIAL_ACADEMICA + new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key), "token");
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
        Log.i("response", response);

        try {
            jsonArrayhistorialAcademico = new JSONArray(response);

            if (jsonArrayhistorialAcademico.length() > 0) {
                itemMateriaKardexes = new ArrayList<ItemMateriaKardex>();

                for (int index = 0; index <= jsonArrayhistorialAcademico.length() - 1; index++) {
                    if(!jsonArrayhistorialAcademico.getJSONObject(index).getString("cicloSugerido").equals("-1")) {
                        if (jsonArrayhistorialAcademico.getJSONObject(index).getString("superArea").equals(nombreArea)) {
                            itemMateriaKardexes.add(new ItemMateriaKardex(jsonArrayhistorialAcademico.getJSONObject(index).getString("asignatura"),
                                    jsonArrayhistorialAcademico.getJSONObject(index).getString("calificacion")));
                        }
                    }
                }

                materiaKardexDetalleAdapter = new MateriaKardexDetalleAdapter(this, 0, itemMateriaKardexes);
                lst_materias.setAdapter(materiaKardexDetalleAdapter);
                Constantes.progressDialog.dismiss();
            }

        } catch (JSONException ex) {
            Log.e("errorHistorialAc", ex.toString());
        }

    }
}
