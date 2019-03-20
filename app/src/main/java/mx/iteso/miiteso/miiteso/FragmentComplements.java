package mx.iteso.miiteso.miiteso;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.itesubes.ChooseRouteActivity;
import mx.iteso.miiteso.itesubes.ItesubesMapActivity;
import mx.iteso.miiteso.miiteso.adapters.RoutesAdapter;
import mx.iteso.miiteso.miiteso.model.ItemRoutes;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 28/08/2018.
 */

public class FragmentComplements extends Fragment implements onTaskCompleted {
    LinearLayout ly_Itesubes;
    TextView tvItesubes;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complements, container, false);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        return view;
    }

    private void setImplementListener() {

        ly_Itesubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(),ChooseRouteActivity.class));
                getRoutes();
            }
        });

    }

    private void setTypography() {
        tvItesubes.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));

    }

    private void setInitializeComponent() {
        ly_Itesubes = view.findViewById(R.id.ly_itesubes);
        tvItesubes = view.findViewById(R.id.tv_itesubes);

    }

    private void getRoutes() {
        new NetServices(this, getActivity(), false).execute("get", MethodWS.URL_BASE_ITESUBES + MethodWS.WS_GET_ROUTES);
    }

    @Override
    public void onTaskCompleted(String response) {
        if (response != null) {

            JSONArray jsonArrayRoutes = null;

            try {
                jsonArrayRoutes = new JSONArray(new JSONObject(response).getString("data"));

                for (int index = 0; index <= jsonArrayRoutes.length() - 1; index++)
                    Constantes.itemRoutes.add(new ItemRoutes(jsonArrayRoutes.getJSONObject(index).getString("Nombre"),
                            jsonArrayRoutes.getJSONObject(index).getString("IDRuta"), Double.parseDouble(jsonArrayRoutes.getJSONObject(index).getString("Latitud")),
                            Double.parseDouble(jsonArrayRoutes.getJSONObject(index).getString("Longitud"))));

                new Metodos(getContext()).NavegarAPantalla(ItesubesMapActivity.class);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}