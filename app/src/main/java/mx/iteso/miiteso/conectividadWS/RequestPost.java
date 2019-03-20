package mx.iteso.miiteso.conectividadWS;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 03/11/2016.
 */

public class RequestPost {
    private Context context = null;
    private URL url = null;
    private HttpURLConnection httpURLConnection = null;
    private OutputStreamWriter writer = null;
    private URLConnection urlConnection = null;
    private JSONObject jsonObject = null;
    private InputStream inputStream = null;

    String parameters = "";
    String result = "";

    public RequestPost(Context context) {
        this.context = context;
    }

    public String requestPost(String[] values) {
        try {
            if (Constantes.UseapiKey)
                url = new URL(values[1]);
            else
                url = new URL(MethodWS.URL_BASE.concat(values[1]));

            urlConnection = url.openConnection();
            httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);

            if (Constantes.UseapiKey) {
                httpURLConnection.setRequestProperty("apikey", "ssadddewsalkdfjjcmdbeuikshdi33323ssd&%L");
                Constantes.KEY_NAME = Constantes.KEY_NAME_ALT;
                Constantes.UseapiKey = false;
            }

            httpURLConnection.connect();

            writer = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");

            jsonObject = new JSONObject();

            for (int index = 2; index <= values.length - 1; index++) {
                jsonObject.put(Constantes.KEY_NAME.get(index - 2), values[index]);
            }

            parameters = jsonObject.toString();
            writer.write(parameters);
            writer.flush();

            if (httpURLConnection.getResponseCode() != 200)
                inputStream = httpURLConnection.getErrorStream();
            else if (httpURLConnection.getResponseCode() == 200)
                inputStream = httpURLConnection.getInputStream();

            Constantes.CODIGO_RESPUESTA = httpURLConnection.getResponseCode();
            result = new Metodos(context).ConvertirStreamAString(inputStream);

            inputStream.close();
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return result;
    }
}
