package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 26/02/2018.
 */

public class NoticePrivacyActivity extends Activity {
    WebView webView;
    TextView lbl_header_title;
    ImageView ic_arrow_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);

        setInitializeComponent();
        showNoticeAndPrivacy();

        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showNoticeAndPrivacy() {
        new Metodos(NoticePrivacyActivity.this).showWebView(MethodWS.URL_NOTICE_PRIVACY, webView,
                new Metodos(this).showProgressDialog("Cargando aviso de privacidad"));
    }

    private void setInitializeComponent() {
        webView = (WebView) findViewById(R.id.webView);
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header_title.setText(R.string.lbl_notice_privacy_string);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);

        lbl_header_title.setTypeface(new Metodos(NoticePrivacyActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }
}
