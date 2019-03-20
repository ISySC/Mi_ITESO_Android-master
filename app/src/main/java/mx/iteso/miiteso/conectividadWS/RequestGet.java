package mx.iteso.miiteso.conectividadWS;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rjuarez on 03/11/2016.
 */

public class RequestGet {
    private URL url = null;
    private Context context = null;
    private HttpURLConnection httpURLConnection = null;
    private InputStream inputStream = null;

    String result = "";

    public RequestGet(Context context) {
        this.context = context;
    }

    public String requestGet(String[] values) {

        try {
            //TinyDB tinyDB = new TinyDB(context);

            url = new URL(values[1]);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);

            httpURLConnection.setRequestProperty("apikey", "ssadddewsalkdfjjcmdbeuikshdi33323ssd&%L");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(false);



            if (httpURLConnection.getResponseCode() != 200) //Por alguna razon el response nos da un error
                inputStream = httpURLConnection.getErrorStream();
            else if (httpURLConnection.getResponseCode() == 200)  //el response es correcto (200)
                inputStream = httpURLConnection.getInputStream();




            result = convertStreamToString(inputStream);

            inputStream.close();
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    public String requestGet(String values) {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            url = new URL(values);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);

            //new SetHeaders().setToken(httpURLConnection, tinyDB.getString("TOKEN"));

            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=Unicode");
            httpURLConnection.setDoOutput(false);

            if (httpURLConnection.getResponseCode() != 200) //Por alguna razon el response nos da un error
                inputStream = httpURLConnection.getErrorStream();
            else if (httpURLConnection.getResponseCode() == 200)  //el response es correcto (200)
                inputStream = httpURLConnection.getInputStream();

            result = convertStreamToString(inputStream);

            inputStream.close();
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        } finally {
            if (httpURLConnection != null)
            httpURLConnection.disconnect();
        }
        return result;
    }



    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("respuesta", sb.toString());
        return sb.toString();
    }
}
