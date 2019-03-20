package mx.iteso.miiteso.itesubes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.adapters.RoutesAdapter;
import mx.iteso.miiteso.miiteso.model.ItemRoutes;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 28/08/2018.
 */

public class ChooseRouteActivity extends Activity implements onTaskCompleted {

    TextView lbl_header_title;
    ImageView ic_arrow_back;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_route);

        setInitializeComponent();
        setTypography();

        getRoutes();
    }

    private void getRoutes() {
        new NetServices(ChooseRouteActivity.this, this, false).execute("get", MethodWS.URL_BASE_ITESUBES + MethodWS.WS_GET_ROUTES);
    }

    private void setInitializeComponent() {
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);
        lbl_header_title.setText("Rutas");
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
        listView = findViewById(R.id.lv_routs);

        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTypography() {
        lbl_header_title.setTypeface(new Metodos(ChooseRouteActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    @Override
    public void onTaskCompleted(String response) {
        if (response != null) {

            final ArrayList<ItemRoutes> itemRoutes = new ArrayList<ItemRoutes>();
            RoutesAdapter routesAdapter = null;
            JSONArray jsonArrayRoutes = null;

            try {
                jsonArrayRoutes = new JSONArray(new JSONObject(response).getString("data"));

                for (int index = 0; index <= jsonArrayRoutes.length() - 1; index++)
                    itemRoutes.add(new ItemRoutes(jsonArrayRoutes.getJSONObject(index).getString("Nombre"),
                            jsonArrayRoutes.getJSONObject(index).getString("IDRuta"), Double.parseDouble(jsonArrayRoutes.getJSONObject(index).getString("Latitud")),
                            Double.parseDouble(jsonArrayRoutes.getJSONObject(index).getString("Longitud"))));

                routesAdapter = new RoutesAdapter(ChooseRouteActivity.this, 0, itemRoutes);
                listView.setAdapter(routesAdapter);



                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Constantes.LatBusStop = itemRoutes.get(position).getLat();
                        Constantes.LonBusStop = itemRoutes.get(position).getLon();
                        Constantes.routeID = itemRoutes.get(position).getIdRuta();
                        Constantes.route = itemRoutes.get(position).getNombreRuta();

                        new Metodos(ChooseRouteActivity.this).NavegarAPantalla(ItesubesMapActivity.class);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
