package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.adapters.CoordinationAdapter;
import mx.iteso.miiteso.miiteso.adapters.NotificationAdapter;
import mx.iteso.miiteso.miiteso.model.ItemCoordination;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class Acompanamiento extends Activity implements onTaskCompleted {
    ListView lst_message;
    TextView lbl_header,tv_message;
    ImageView ic_arrow_back;
    Integer type=0;
    String elementID="0";
    ItemNotification itemNotificationTemp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notificacion_acompanamiento);
        type = Integer.valueOf(getIntent().getExtras().getString("extra 1","0"));

        setInitializeComponent();
        setTypography();
        setImplementListener();

        getNotif();
        lst_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1){
                    elementID= Constantes.itemNotification.get(position).getId();
                    itemNotificationTemp = Constantes.itemNotification.get(position);
                }
                else if(type == 2){
                    elementID= Constantes.itemNotificationGrl.get(position).getId();
                    itemNotificationTemp = Constantes.itemNotificationGrl.get(position);
                }
                view.setBackgroundColor(getResources().getColor(android.R.color.white));
                setReaded();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ITEM", (Serializable)itemNotificationTemp);
                startActivity(new Intent(Acompanamiento.this,DetailNotifActivity.class).putExtras(bundle));
            }
        });
    }

    private void getNotif()
    {
        if (type == 1) {
            if (Constantes.itemNotification != null)
                lst_message.setAdapter(new NotificationAdapter(Acompanamiento.this, 0, Constantes.itemNotification));
        }
        else if (type == 2) {
            if (Constantes.itemNotificationGrl != null)
            lst_message.setAdapter(new NotificationAdapter(Acompanamiento.this, 0, Constantes.itemNotificationGrl));
            lbl_header.setText("Generales");
        }

        if (lst_message.getAdapter() != null && lst_message.getAdapter().getCount() != 0)
        {
            lst_message.setVisibility(View.VISIBLE);
            tv_message.setVisibility(View.GONE);
        }
        else
        {
            lst_message.setVisibility(View.GONE);
            tv_message.setVisibility(View.VISIBLE);
        }

    }
    private void setImplementListener() {
        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTypography() {
        lbl_header.setTypeface(new Metodos(Acompanamiento.this).typeface(Metodos.TypeFont.MontserratRegular));
        tv_message.setTypeface(new Metodos(Acompanamiento.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lst_message = (ListView) findViewById(R.id.lst_coordination);
        lbl_header = (TextView) findViewById(R.id.lbl_header_title);

        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header.setText(getString(R.string.title_coordination));
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);

        tv_message = findViewById(R.id.tv_message);
    }

    private void setReaded() {
        //get TOKEN_ID Firebase
        Constantes.KEY_NAME_ALT = new ArrayList<String>();
        Constantes.KEY_NAME_ALT.add("token");
        Constantes.KEY_NAME_ALT.add("idnotificacion");
        Constantes.UseapiKey=true;
        new NetServices(this,Acompanamiento.this,false).execute("post", MethodWS.URL_BASE_TEST + MethodWS.WS_READED,
                new Metodos(Acompanamiento.this).getSharedPreference(getString(R.string.preference_file_key), "fbToken"),elementID);
    }

    @Override
    protected void onResume()
    {
        getNotif();
        super.onResume();

    }
    @Override
    public void onTaskCompleted(String response) {
        if (response != "") {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("response"))
                    Toast.makeText(Acompanamiento.this, jsonObject.getString("responseMessage"), Toast.LENGTH_SHORT).show();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
