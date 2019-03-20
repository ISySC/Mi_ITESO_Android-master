package mx.iteso.miiteso.conectividadWS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.json.XML.*;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/11/2016.
 */

public class NetServices extends AsyncTask<String, Void, String> {

    private onTaskCompleted taskCompleted = null;
    private Activity context;
    private boolean showDialog;
    ArrayList<String> responses = new ArrayList<>();

    private ProgressDialog dialog;

    public NetServices(onTaskCompleted taskContext, Activity context, Boolean showDialog) {
        this.taskCompleted = taskContext;
        this.context = context;
        this.showDialog = showDialog;

        if (!Constantes.esNotificacion) {
            dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }

    }

    protected String doInBackground(String... values) {
        String response = null;
        JSONArray re = new JSONArray();
        JSONObject jso = new JSONObject();
        switch (values[0]) {
            case "post":
                response = new RequestPost(context).requestPost(values);
                break;
            case "get":
                response = new RequestGet(context).requestGet(values);
                break;
            case "put":
                break;
            case "del":
                break;
            case "notif":
                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "moodle").equals("1")) {
                    try {

                        String[] valM = {"", values[1]};
                        response = new RequestPost(context).requestPost(valM);
                        jso.put("Moodle", new JSONArray(response));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "horario").equals("1")) {
                    try {
                        String[] valC = {"", values[2]};
                        response = new RequestPost(context).requestPost(valC);
                        jso.put("Horario1", new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "horario").equals("1")) {
                    try {
                        String[] valC = {"", values[3]};
                        response = new RequestPost(context).requestPost(valC);
                        jso.put("Horario2", new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "horario").equals("1")) {

                    try {
                        String[] valC = {"", values[4]};
                        response = new RequestPost(context).requestPost(valC);
                        jso.put("Horario3", new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "horario").equals("1")) {
                    try {
                        String[] valC = {"", values[5]};
                        response = new RequestPost(context).requestPost(valC);
                        jso.put("Horario4", new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "horario").equals("1")) {

                    try {
                        String[] valC = {"", values[6]};
                        response = new RequestPost(context).requestPost(valC);
                        jso.put("Horario5", new JSONArray(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Agend ITESO
                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "agenda").equals("1")) {
                    try {
                        response = new RequestGet(context).requestGet(values[8]);
                        jso.put("agenda", new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Calendario Escolar
                if (new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key),
                        "calendario").equals("1")) {

                    try {
                        response = new RequestGet(context).requestGet(values[7]);
                        jso.put("calendario", new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                response = jso.toString();
                break;
        }
        //este objeto lo cacha el método onPostExecute
        return response;
    }




    //se ejecuta una vez terminado el hilo de doInBackground....
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (taskCompleted != null)
            if (response != "")
                taskCompleted.onTaskCompleted(response);
            else
                new Metodos(context).MensajeAUsuario(Constantes.MSG_WEB_SERVICES_ERROR);
        if (!Constantes.esNotificacion) {
            if ( dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        Constantes.esNotificacion = false;
    }


    protected void onPreExecute() {
        if (!Constantes.esNotificacion) {
            dialog.setMessage("Cargando información. Por favor espere.");
            dialog.show();
        }


    }
}
