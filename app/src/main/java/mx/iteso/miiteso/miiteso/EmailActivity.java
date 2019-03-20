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
import mx.iteso.miiteso.miiteso.adapters.CoordinationAdapter;
import mx.iteso.miiteso.miiteso.adapters.EmailAdapter;
import mx.iteso.miiteso.miiteso.model.ItemCoordination;
import mx.iteso.miiteso.miiteso.model.ItemEmail;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class EmailActivity extends Activity {
    ListView lst_message;
    TextView lbl_header;
    ImageView ic_arrow_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        //arrayList sera sustituido por Json
        ArrayList<ItemEmail> itemEmails = new ArrayList<>();

        String[] from = new String[]{"Gómez Luna", "Aldo Altamirano", "Andrea Velázquez", "Raúl Juárez"};
        String[] subject = new String[]{"Calificaciones", "Credencial", "Baja de materias", "Tarea de Android"};
        String[] date = new String[]{"Hoy, 09:00", "Hoy, 09:00", "Hoy, 09:00", "Hoy, 9:00"};
        String[] word = new String[]{"G", "A", "V", "R"};

        for (int index = 0; index <= from.length - 1; index++)
            itemEmails.add(new ItemEmail(from[index], date[index], subject[index], word[index]));

        lst_message.setAdapter(new EmailAdapter(EmailActivity.this, 0, itemEmails));
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
        lbl_header.setTypeface(new Metodos(EmailActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lst_message = (ListView) findViewById(R.id.lst_email);
        lbl_header = (TextView) findViewById(R.id.lbl_header_title);

        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header.setText(getString(R.string.title_email));
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }
}
