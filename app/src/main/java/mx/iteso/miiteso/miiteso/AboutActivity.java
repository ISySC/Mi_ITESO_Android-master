package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Metodos;

public class AboutActivity extends Activity implements View.OnClickListener {

    TextView lbl_header_title;
    ImageView ic_arrow_back;
    TextView tv_about, tv_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        setInitializeUI();
        setListener();
        setTypeface();
    }

    private void setInitializeUI() {
        lbl_header_title = findViewById(R.id.lbl_header_title);
        tv_about = findViewById(R.id.tv_about);
        ic_arrow_back = findViewById(R.id.ic_one);
        tv_mail = findViewById(R.id.tv_mail);

        lbl_header_title.setText(R.string.about);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }

    private void setListener() {
        ic_arrow_back.setOnClickListener(this);
    }

    private void setTypeface() {
        lbl_header_title.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        tv_about.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
        tv_mail.setTypeface(new Metodos(this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_one:
                finish();
                break;
        }
    }
}
