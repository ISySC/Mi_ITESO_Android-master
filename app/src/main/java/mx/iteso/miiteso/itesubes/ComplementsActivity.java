package mx.iteso.miiteso.itesubes;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Creado por:  Raúl Emmanuel Juárez Parra
 * Creado el:   12/12/2017
 * Descripción: Subject para mostrar los diferentes complementos de la app que no requieren de una autentificacion
 * Función:     Ofrecer funcionalidad a módulos que no requieren de usuario y contraseña
 */

public class ComplementsActivity extends Activity {
    TextView lbl_header_title, lbl_itesubes_title;
    ImageView ic_arrow_back;

    RelativeLayout ln_itesubes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complement);

        setInitializeComponent();
        setTypography();

        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ln_itesubes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Metodos(ComplementsActivity.this).NavegarAPantalla(ChooseRouteActivity.class);
            }
        });
    }

    private void setTypography() {
        lbl_itesubes_title.setTypeface(new Metodos(ComplementsActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_header_title.setTypeface(new Metodos(ComplementsActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);
        lbl_itesubes_title = (TextView) findViewById(R.id.lbl_itesubes_title);
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);
        ln_itesubes = (RelativeLayout) findViewById(R.id.ln_itesubes);

        lbl_header_title.setText(R.string.extension_title);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }
}
