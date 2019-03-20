package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.itesubes.ComplementsActivity;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;


public class LoginActivity extends Activity implements onTaskCompleted {
    Button btn_login;
    EditText txt_account, txt_password;
    CheckBox chk_remember_data;
    TextView lbl_problems_title, lbl_privacity, lbl_footer_title, lbl_header_title;
    RelativeLayout ln_footer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        configNotices();
        Constantes.mainNoticeAdapter = null;

        setInitializeComponent();
        setTypography();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        lbl_problems_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(LoginActivity.this).alertDialog("",
                        getString(R.string.lbl_problems_messaage_string))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        lbl_privacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(LoginActivity.this).NavegarAPantalla(NoticePrivacyActivity.class);
            }
        });

        ln_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComplements();
            }
        });
    }

    private void showComplements() {
        new Metodos(LoginActivity.this).NavegarAPantalla(ComplementsActivity.class);
    }

    private void login() {

        Constantes.MSG_PROGRESO = "Iniciando sesión. Espere un momento";

        String usuario = txt_account.getText().toString();
        String contrasena = txt_password.getText().toString();

        try {
            if (!new Metodos(LoginActivity.this).isFieldEmpty(txt_account, txt_password)) {
                Constantes.KEY_NAME = new ArrayList<String>();
                Constantes.KEY_NAME.add("usuario");
                Constantes.KEY_NAME.add("contrasena");

                new NetServices(LoginActivity.this, this, true).execute("post", MethodWS.WS_INICIAR_SESION, usuario, contrasena);
            } else {
                new Metodos(LoginActivity.this).MensajeAUsuario(Constantes.MSG_EMPTY_FIELD_ERROR);
            }
        } catch (Exception ex) {
            Log.d("errorIngresar", ex.toString());
        }
    }

    private void setTypography() {
        txt_password.setTransformationMethod(new PasswordTransformationMethod());

        txt_password.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        txt_account.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_problems_title.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratLight));
        lbl_privacity.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratLight));
        lbl_footer_title.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratLight));
        btn_login.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        chk_remember_data.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratLight));
        lbl_header_title.setTypeface(new Metodos(LoginActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        btn_login = (Button) findViewById(R.id.btn_login);
        txt_account = (EditText) findViewById(R.id.txt_account);
        txt_password = (EditText) findViewById(R.id.txt_password);
        lbl_problems_title = (TextView) findViewById(R.id.lbl_problems);
        lbl_privacity = (TextView) findViewById(R.id.lbl_privacity);
        lbl_footer_title = (TextView) findViewById(R.id.lbl_footer_title);
        chk_remember_data = (CheckBox) findViewById(R.id.chk_remember_data);
        ln_footer = (RelativeLayout) findViewById(R.id.ly_footer);
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);

        lbl_footer_title.setText(getString(R.string.lbl_complements));
        lbl_header_title.setText("Mi ITESO");

        isSessionActive();
    }

    private void isSessionActive() {
        try {

            if (new Metodos(LoginActivity.this).
                    getSharedPreference(getString(R.string.preference_file_key),
                            "sessionActive").equals("1")) {
                new Metodos(this).NavegarAPantalla(MainActivity.class);
            }

        } catch (Exception e) {
            new Metodos(LoginActivity.this).setSharedPreference(getString(R.string.preference_file_key), "sessionActive", "0");
        }
    }

    @Override
    public void onTaskCompleted(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("token").isEmpty())
                new Metodos(LoginActivity.this).MensajeAUsuario("Usuario y/o contraseña incorrecta");
            else {
                if (chk_remember_data.isChecked())
                    new Metodos(LoginActivity.this).setSharedPreference(getString(R.string.preference_file_key),
                            "sessionActive", "1");

                new Metodos(LoginActivity.this).setSharedPreference(getString(R.string.preference_file_key),
                        "token", jsonObject.getString("token"));
                new Metodos(LoginActivity.this).setSharedPreference(getString(R.string.preference_file_key),
                        "expAlumno", txt_account.getText().toString());

                txt_password.setText("");
                txt_account.setText("");

                Constantes.mainNoticeAdapter = null;

                new Metodos(LoginActivity.this).NavegarAPantalla(MainActivity.class);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void configNotices() {

        //validar si es primera vez para preconfigurar los ajustes
        if (new Metodos(this).getSharedPreference(getString(R.string.preference_file_key), "token").equals("0")) {

            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "calendario", "1");
            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "moodle", "1");
            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "horario", "1");
            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "agenda", "1");
            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "cruce", "1");
            new Metodos(this).setSharedPreference(getString(R.string.preference_file_key),
                    "magis", "1");
        }
    }

}
