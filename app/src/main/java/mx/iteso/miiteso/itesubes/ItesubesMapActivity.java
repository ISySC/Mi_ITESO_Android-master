package mx.iteso.miiteso.itesubes;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.RequestGet;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.model.ItemRoutes;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

public class ItesubesMapActivity extends Activity implements View.OnClickListener, OnMapReadyCallback, onTaskCompleted {

    GoogleMap googleMap;
    MapFragment mapFragment;

    TextView lbl_header_title;
    ImageView ic_arrow_back, ic_info;

    Handler handlerMap = new Handler();

    boolean isFirstTime = true;
    LatLng bus, busStop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        setInitializeUI();
        setTypeFaceUI();
        setListener();
    }

    private void setListener() {
        ic_arrow_back.setOnClickListener(this);
        ic_info.setOnClickListener(this);
    }

    private void setTypeFaceUI() {
        lbl_header_title.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeUI() {
        lbl_header_title = findViewById(R.id.lbl_header_title);
        ic_arrow_back = findViewById(R.id.ic_one);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        ic_info = findViewById(R.id.ic_info);

        lbl_header_title.setText("Rutas");

        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_one:
                finish();
                break;
            case R.id.ic_info:
                popUpInfo();
                break;
        }
    }

    private void popUpInfo() {
        new Metodos(this).alertDialogUI().create().show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Constantes.MSG_PROGRESO = "Obteniendo rutas...";
        setBusStop();
        getBusPositions(true);

        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void setBusStop() {

        for (ItemRoutes itemRoute : Constantes.itemRoutes) {
            busStop = new LatLng(itemRoute.getLat(), itemRoute.getLon());
            googleMap.addMarker(new MarkerOptions()
                    .position(busStop)
                    .title("Parada ITESO")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_camion))).showInfoWindow();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getBusPositions(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        removeHandleMaps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //remove handler maps
        removeHandleMaps();
    }

    private void removeHandleMaps() {
        //remove handler maps
        handlerMap.removeCallbacksAndMessages(null);
    }

    @Override
    public void onTaskCompleted(String response) {
        String titleEstatusVehiculo = "";

        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.get("response").toString() == "true") {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                googleMap.clear();

                setBusStop();

                for (int index = 0; index <= jsonArray.length() - 1; index++) {

                    titleEstatusVehiculo = jsonArray.getJSONObject(index).getString("Estatus").equals("1") ?
                            "Camino a ITESO" : "Camino a recolección";

                    bus = new LatLng(Double.parseDouble(jsonArray.getJSONObject(index).getString("Latitud")),
                            Double.parseDouble(jsonArray.getJSONObject(index).getString("Longitud")));

                    switch (jsonArray.getJSONObject(index).getInt("EstatusVehiculo")) {
                        //---------Apagado-------------
                        case 0:
                            googleMap.addMarker(new MarkerOptions()
                                    .rotation(jsonArray.getJSONObject(index).getInt("Orientacion") - 180f)
                                    .position(bus)
                                    .title("Apagado")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carro_red))).showInfoWindow();

                            break;
                        //---------Parado--------------
                        case 1:
                            googleMap.addMarker(new MarkerOptions()
                                    .rotation(jsonArray.getJSONObject(index).getInt("Orientacion"))
                                    .position(bus)
                                    .title("Parado")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carro_orange))).showInfoWindow();

                            break;
                        //---------En movimiento-------
                        case 2:
                            googleMap.addMarker(new MarkerOptions()
                                    .rotation(jsonArray.getJSONObject(index).getInt("Orientacion"))
                                    .position(bus)
                                    .title("Circulando")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_carro_green))).showInfoWindow();

                            break;
                    }


                    Log.i("busPositions", jsonArray.getJSONObject(index).getString("Latitud") + "    " + jsonArray.getJSONObject(index).getString("Longitud")
                            + " Estatus:" + jsonArray.getJSONObject(index).getString("Estatus"));
                }


                if (isFirstTime) {
                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(bus)
                            .zoom(15)
                            .build();

                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    isFirstTime = false;
                }
            }


        } catch (JSONException ex) {
            Log.e("error", ex.toString());
        }
    }


    private void updateRoute(final boolean showDialog) {
        handlerMap.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBusPositions(showDialog);
            }
        }, 10000);
    }

    private void getBusPositions(boolean showDialog) {
        new getPosition(ItesubesMapActivity.this, this, showDialog).execute("get", MethodWS.URL_BASE_ITESUBES + MethodWS.WS_GET_VEHICLES);
    }

    public class getPosition extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;
        private onTaskCompleted taskCompleted = null;
        private Activity context = null;
        private boolean showDialog;

        public getPosition(onTaskCompleted taskContext, Activity context, Boolean showDialog) {
            this.taskCompleted = taskContext;
            this.context = context;
            this.showDialog = showDialog;
        }

        protected String doInBackground(String... values) {
            String response = null;

            switch (values[0]) {
                case "get":
                    response = new RequestGet(context).requestGet(values);
                    break;
            }

            updateRoute(false);
            //este objeto lo cacha el método onPostExecute
            return response;
        }

        //se ejecuta una vez terminado el hilo de doInBackground....
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (showDialog)
                progressDialog.dismiss();

            if (response != "") {
                taskCompleted.onTaskCompleted(response);
            } else
                new Metodos(context).MensajeAUsuario(Constantes.MSG_WEB_SERVICES_ERROR);
        }

        protected void onPreExecute() {
            if (showDialog)
                progressDialog = new Metodos(context).showProgressDialog(Constantes.MSG_PROGRESO);
        }
    }
}
