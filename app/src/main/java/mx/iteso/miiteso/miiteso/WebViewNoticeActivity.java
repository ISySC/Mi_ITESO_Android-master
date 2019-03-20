package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 26/02/2018.
 */

public class WebViewNoticeActivity extends AppCompatActivity {
    WebView webView;
    TextView lbl_header_title;
    ImageView ic_arrow_back;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);

        setInitializeComponent();
        showNotice();

        ic_arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void showNotice() {
        new Metodos(WebViewNoticeActivity.this).showWebView(Constantes.url_main_notice, webView,
                new Metodos(this).showProgressDialog("Cargando aviso"));
    }

    private void setInitializeComponent() {
        webView = (WebView) findViewById(R.id.webView);
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);

        lbl_header_title.setText(R.string.lbl_main_notice);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);
    }
}
