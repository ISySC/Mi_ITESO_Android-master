package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mx.iteso.miiteso.miiteso.Acompanamiento;
import mx.iteso.miiteso.miiteso.adapters.NotificationAdapter;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 04/03/2019.
 */

public class DetailNotifActivity extends Activity {

    TextView lbl_subject,lbl_from,date,lbl_message_detail,lbl_header_title;
    ImageView ic_arrow_back;
    ItemNotification itemNotification;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notif_detail);
        itemNotification = (ItemNotification) getIntent().getExtras().getSerializable("ITEM");

        setInitializeComponent();
        setTypography();
        setImplementListener();
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
        lbl_header_title.setTypeface(new Metodos(DetailNotifActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_subject.setTypeface(new Metodos(DetailNotifActivity.this).typeface(Metodos.TypeFont.MontserratMedium));
        lbl_from.setTypeface(new Metodos(DetailNotifActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        date.setTypeface(new Metodos(DetailNotifActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_message_detail.setTypeface(new Metodos(DetailNotifActivity.this).typeface(Metodos.TypeFont.MontserratRegular));

    }

    private void setInitializeComponent() {
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);

        lbl_header_title = findViewById(R.id.lbl_header_title);

        lbl_subject =  findViewById(R.id.lbl_subject);
        lbl_from =  findViewById(R.id.lbl_from);
        date =  findViewById(R.id.date);
        lbl_message_detail =  findViewById(R.id.lbl_message_detail);


        lbl_subject.setText(itemNotification.getEvento());
        lbl_from.setText(itemNotification.getRemitente());
        date.setText(itemNotification.getFechaEnvio() + " hrs");
        lbl_message_detail.setText(itemNotification.getMensaje());
        lbl_header_title.setText("Detalle");
    }
}
