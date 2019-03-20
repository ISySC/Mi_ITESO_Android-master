package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 26/04/2018.
 */

public class EmailDetailsActivity extends Activity {
    TextView lbl_header_title, lbl_subject, lbl_from, lbl_to, lbl_date, lbl_message_detail, lbl_word;
    ImageView ic_arrow_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_details);

        setInitializeComponent();
        setTypography();

        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTypography() {
        lbl_header_title.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_subject.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratMedium));
        lbl_from.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_to.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratLight));
        lbl_date.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratLight));
        lbl_message_detail.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_word.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lbl_header_title = findViewById(R.id.lbl_header_title);
        lbl_subject = findViewById(R.id.lbl_subject);
        lbl_from = findViewById(R.id.lbl_from);
        lbl_to = findViewById(R.id.lbl_to);
        lbl_date = findViewById(R.id.date);
        lbl_message_detail = findViewById(R.id.lbl_message_detail);
        lbl_word = findViewById(R.id.lbl_word);
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header_title.setText(getString(R.string.title_email));
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }
}
