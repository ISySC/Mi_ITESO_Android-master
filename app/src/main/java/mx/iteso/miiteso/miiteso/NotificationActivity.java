package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.adapters.NotificationAdapter;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class NotificationActivity extends Activity {
    ListView lst_notification;
    TextView lbl_header;
    ImageView ic_arrow_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        setInitializeComponent();
        setTypography();
        setImplementListener();


        //arrayList sera sustituido por Json
        ArrayList<ItemNotification> itemNotifications = new ArrayList<>();

        String[] notification = new String[]{"Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos",
                "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos", "Nuevos horarios para taller de procesos"};
        String[] date = new String[]{"Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00"};

        for (int index = 0; index <= notification.length - 1; index++)
            //itemNotifications.add(new ItemNotification(notification[index], date[index]));

        lst_notification.setAdapter(new NotificationAdapter(NotificationActivity.this, 0, itemNotifications));
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
        lbl_header.setTypeface(new Metodos(NotificationActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lst_notification = (ListView) findViewById(R.id.lst_notification);
        lbl_header = (TextView) findViewById(R.id.lbl_header_title);

        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header.setText(getString(R.string.title_notifications));
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }
}
