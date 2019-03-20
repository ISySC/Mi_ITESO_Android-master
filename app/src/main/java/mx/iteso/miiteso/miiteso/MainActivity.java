package mx.iteso.miiteso.miiteso;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 20/02/2018.
 */

public class MainActivity extends AppCompatActivity implements onTaskCompleted {
    ImageView ic_agenda, ic_finance, ic_kardex, ic_schedule, ic_complements;
    RelativeLayout rl_agenda, rl_finance, rl_kardex, rl_schedule, rl_complements;
    ObjectAnimator rotate;
    TextView lbl_title_header_main;
    ImageView ic_one, /*ic_two,*/
            ic_three, ic_four;
    boolean firsTime = true;
    String fireBaseToken;
    ImageView ic_oval_notification_people /*, ic_oval_notification_graduation */, ic_oval_notification_email;

    TextView lbl_count_notification_people /*, lbl_count_notification_graduation,*/, lbl_count_notification_email;

    int servicio=0;

    boolean isNotices = true;
    private static AudioManager audioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);


        setContentView(R.layout.activity_main);
        setInitializeComponent();
        setImplementListener();


    }

    private void setImplementListener() {


        rl_agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeThinImageViews(ic_agenda);
                lbl_title_header_main.setText(R.string.title_header_main);
                setNoticicationVisibility(true);
                Constantes.selectedPosition.set(14);
                setNoticesVisibility(1);
                isNotices = true;
                new Metodos(MainActivity.this).addFragment(new FragmentNotice(), MainActivity.this);
            }
        });

        rl_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new NetServices(MainActivity.this, MainActivity.this).execute("post", MethodWS.WS_EDO_CUENTA_RESUMEN +
                //        new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key),
                //                 "token"));
                changeThinImageViews(ic_finance);
                lbl_title_header_main.setText(R.string.account_status);
                setNoticicationVisibility(false);
                setNoticesVisibility(0);
                isNotices = false;
                new Metodos(MainActivity.this).addFragment(new FragmentAccountStatus(), MainActivity.this);
            }
        });

        rl_kardex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNoticesVisibility(0);
                changeThinImageViews(ic_kardex);
                lbl_title_header_main.setText(R.string.kardex);
                setNoticicationVisibility(false);
                isNotices = false;
                new Metodos(MainActivity.this).addFragment(new FragmentKardex(), MainActivity.this);

            }
        });

        rl_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeThinImageViews(ic_schedule);
                lbl_title_header_main.setText(R.string.schedule_title);
                setNoticicationVisibility(false);
                Constantes.selectedPosition.set(14);
                setNoticesVisibility(0);
                isNotices = false;
                new Metodos(MainActivity.this).addFragment(new FragmentSchedule(), MainActivity.this);
            }
        });

        rl_complements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeThinImageViews(ic_complements);
                lbl_title_header_main.setText(R.string.extension_title);
                setNoticicationVisibility(false);
                setNoticesVisibility(0);
                isNotices = false;
                new Metodos(MainActivity.this).addFragment(new FragmentComplements(), MainActivity.this);
            }
        });

        ic_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(MainActivity.this).NavegarAPantalla(ConfigurationActivity.class);
            }
        });

        /*ic_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(getActivity()).NavegarAPantalla(NotificationActivity.class);
            }
        });*/

        findViewById(R.id.fml_acom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Metodos(MainActivity.this).NavegarAPantallaExtras(Acompanamiento.class,"2");

            }
        });

        findViewById(R.id.fml_generales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Metodos(MainActivity.this).NavegarAPantallaExtras(Acompanamiento.class,"1");

            }
        });

        /*ic_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(MainActivity.this).NavegarAPantalla(Acompanamiento.class);
            }
        });*/

    }

    private void setNoticesVisibility(int visible)
    {
        findViewById(R.id.fml_acom).setVisibility(visible == 0? View.GONE : View.VISIBLE);
        findViewById(R.id.fml_generales).setVisibility(visible == 0? View.GONE : View.VISIBLE);
    }
    private void setInitializeComponent() {


        ic_agenda = (ImageView) findViewById(R.id.ic_agenda);
        ic_finance = (ImageView) findViewById(R.id.ic_finance);
        ic_kardex = (ImageView) findViewById(R.id.ic_kardex);
        ic_schedule = (ImageView) findViewById(R.id.ic_schedule);
        ic_complements = (ImageView) findViewById(R.id.ic_complements);

        rl_agenda = (RelativeLayout) findViewById(R.id.rl_agenda);
        rl_finance = (RelativeLayout) findViewById(R.id.rl_finance);
        rl_kardex = (RelativeLayout) findViewById(R.id.rl_kardex);
        rl_schedule = (RelativeLayout) findViewById(R.id.rl_schedule);
        rl_complements = (RelativeLayout) findViewById(R.id.rl_complements);

        lbl_title_header_main = (TextView) findViewById(R.id.lbl_header_title);
        lbl_title_header_main.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_title_header_main.setText(R.string.title_header_main);

        ic_one = (ImageView) findViewById(R.id.ic_one);
        //ic_two = (ImageView) view.findViewById(R.id.ic_two);
        ic_three = (ImageView) findViewById(R.id.ic_three);
        ic_four = (ImageView) findViewById(R.id.ic_four);

        ic_one.setImageResource(R.drawable.ic_more_ver);
        //ic_two.setImageResource(R.drawable.ic_people);
        ic_three.setImageResource(R.drawable.ic_people);
        ic_four.setImageResource(R.drawable.ic_notifications_none_black_24dp);

        lbl_count_notification_people = (TextView) findViewById(R.id.lbl_count_notification_people);
        //lbl_count_notification_graduation = (TextView) findViewById(R.id.lbl_count_notification_graduation);
        lbl_count_notification_email = (TextView) findViewById(R.id.lbl_count_notification_email);

        ic_oval_notification_people = (ImageView) findViewById(R.id.ic_oval_notification_people);
        //ic_oval_notification_graduation = (ImageView) findViewById(R.id.ic_oval_notification_graduation);
        ic_oval_notification_email = (ImageView) findViewById(R.id.ic_oval_notification_email);


        //lbl_count_notification_people.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratBold));
        //lbl_count_notification_graduation.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratBold));
        lbl_count_notification_email.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratBold));



    }

    private void setNoticicationVisibility(Boolean val) {
        int visibility = 0;
        if (val)
            visibility = View.VISIBLE;
        else
            visibility = View.GONE;

        ic_one.setVisibility(visibility);
        //ic_two.setVisibility(visibility);
        ic_three.setVisibility(visibility);
        ic_four.setVisibility(visibility);
        //lbl_count_notification_people.setVisibility(visibility);
        //lbl_count_notification_graduation.setVisibility(visibility);
        lbl_count_notification_email.setVisibility(visibility);

        //ic_oval_notification_people.setVisibility(visibility);
//        ic_oval_notification_graduation.setVisibility(visibility);
        ic_oval_notification_email.setVisibility(visibility);

    }

    @Override
    public void onTaskCompleted(String response) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (response != "") {
            if (servicio == 1)
                try {

                    if ((new JSONObject(response).getBoolean("response"))) {
                        JSONArray jsonArray = new JSONArray(new JSONObject(response).getString("data"));

                        int acompanamiento = 0;
                        int generales = 0;
                        Constantes.itemNotification = new ArrayList<ItemNotification>();
                        Constantes.itemNotificationGrl = new ArrayList<ItemNotification>();

                        for (int index = 0; index <= jsonArray.length() - 1; index++) {
                            if (!jsonArray.getJSONObject(index).has("fechaLectura") &&
                                    jsonArray.getJSONObject(index).getString("tipo").equals("Acompañamiento"))
                                acompanamiento += 1;
                            if (!jsonArray.getJSONObject(index).has("fechaLectura") &&
                                    jsonArray.getJSONObject(index).getString("tipo").equals("Generales"))
                                generales += 1;

                            if (jsonArray.getJSONObject(index).getString("tipo").equals("Acompañamiento"))
                                Constantes.itemNotification.add(new ItemNotification(jsonArray.getJSONObject(index).getString("evento"),
                                        //jsonArray.getJSONObject(index).getString("fechaEnvio"),
                                        dateFormat.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(jsonArray.getJSONObject(index).getString("fechaEnvio"))),
                                        (jsonArray.getJSONObject(index).has("fechaLectura") ? jsonArray.getJSONObject(index).getString("fechaLectura") : ""),
                                        jsonArray.getJSONObject(index).getString("id"),
                                        jsonArray.getJSONObject(index).getString("mensaje"),
                                        jsonArray.getJSONObject(index).getString("remitente"),
                                        jsonArray.getJSONObject(index).getString("tipo")));

                            else if (jsonArray.getJSONObject(index).getString("tipo").equals("Generales"))
                                Constantes.itemNotificationGrl.add(new ItemNotification(jsonArray.getJSONObject(index).getString("evento"),
                                        dateFormat.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(jsonArray.getJSONObject(index).getString("fechaEnvio"))),
                                        (jsonArray.getJSONObject(index).has("fechaLectura") ? jsonArray.getJSONObject(index).getString("fechaLectura") : ""),
                                        jsonArray.getJSONObject(index).getString("id"),
                                        jsonArray.getJSONObject(index).getString("mensaje"),
                                        jsonArray.getJSONObject(index).getString("remitente"),
                                        jsonArray.getJSONObject(index).getString("tipo")));
                        }


                        if (acompanamiento > 0) {
                            new Metodos(this).showOvalNotification(ic_oval_notification_people, 0,
                                    lbl_count_notification_people, String.valueOf(acompanamiento));
                        }
                        if (generales > 0) {
                            new Metodos(this).showOvalNotification(ic_oval_notification_email, 0,
                                    lbl_count_notification_email, String.valueOf(generales));
                        }
                    } //else
                      //  Toast.makeText(MainActivity.this, (new JSONObject(response).getString("responseMessage")), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("ParseEx", e.toString());
                } finally {
                    new Metodos(MainActivity.this).addFragment(new FragmentNotice(), MainActivity.this);
                    changeThinImageViews(ic_agenda);
                    lbl_title_header_main.setText(R.string.title_header_main);
                    isNotices = true;
                    //if (firsTime) {
                    if (new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key), "fbToken").equals("0")) {
                        getTokenFireBase();
                    } else
                        Log.i("FTOKEN", new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key), "fbToken"));

                    //}
                }

            else if (servicio == 2)
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("response")) {
                        Toast.makeText(MainActivity.this, jsonObject.getString("responseMessage"), Toast.LENGTH_SHORT).show();
                    } else {
                        new Metodos(MainActivity.this).setSharedPreference(getString(R.string.preference_file_key),
                                "fbToken", fireBaseToken);
                        servicio = 1;
                        new NetServices(this, MainActivity.this, false).execute("get", MethodWS.URL_NOTIFICATIONS + new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key), "fbToken"));

                    }
                } catch (JSONException ex) {
                    Log.i("errorPostFBToken", "Insert token error");
                    ex.printStackTrace();
                }
        }
    }

    public void changeThinImageViews(ImageView imageView) {
        audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);
        rotate = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 20f, 0f, -20f, 0f); // rotate o degree then 20 degree and so on for one loop of rotation.
        rotate.setRepeatCount(5);
        rotate.setDuration(20);
        rotate.start();

        new Metodos(MainActivity.this).changeThinImageView(ic_agenda, 114, 114, 114);
        new Metodos(MainActivity.this).changeThinImageView(ic_kardex, 114, 114, 114);
        new Metodos(MainActivity.this).changeThinImageView(ic_finance, 114, 114, 114);
        new Metodos(MainActivity.this).changeThinImageView(ic_schedule, 114, 114, 114);
        new Metodos(MainActivity.this).changeThinImageView(ic_complements, 114, 114, 114);

        new Metodos(MainActivity.this).changeThinImageView(imageView, 27, 117, 187);
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }

    private void getTokenFireBase() {
        //get TOKEN_ID Firebase
        fireBaseToken = FirebaseInstanceId.getInstance().getToken();
        Constantes.KEY_NAME_ALT = new ArrayList<String>();
        Constantes.KEY_NAME_ALT.add("token");
        Constantes.KEY_NAME_ALT.add("plataforma");
        Constantes.KEY_NAME_ALT.add("expAlumno");
        Log.i("FCTOKEN", fireBaseToken);
        servicio=2;
        Constantes.UseapiKey=true;
        new NetServices(this,MainActivity.this,false).execute("post", MethodWS.URL_BASE_TEST + MethodWS.WS_SUBSCRIBE,fireBaseToken,"Android",
                new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key), "expAlumno").substring(2));

    }

    @Override
    public void onResume(){
        if (isNotices) {
            servicio = 1;
            Constantes.esNotificacion = true;
            new NetServices(this, MainActivity.this, false).execute("get", MethodWS.URL_NOTIFICATIONS + new Metodos(MainActivity.this).getSharedPreference(getString(R.string.preference_file_key), "fbToken") );
            //new NetServices(this, MainActivity.this, false).execute("get", MethodWS.URL_NOTIFICATIONS + "nzkrrs6MKMJsq8USWpWC");
        }
        super.onResume();

    }
}
