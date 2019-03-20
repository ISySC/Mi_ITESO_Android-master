package mx.iteso.miiteso.miiteso;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.adapters.ConfigurationAdapter;
import mx.iteso.miiteso.miiteso.model.ItemConfiguration;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 20/02/2018.
 */

public class ConfigurationActivity extends Activity {
    TextView lbl_header_title;
    ImageView ic_arrow_back;
    ListView lst_option;

    ConfigurationAdapter configurationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);

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
        lbl_header_title.setTypeface(new Metodos(ConfigurationActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lbl_header_title = (TextView) findViewById(R.id.lbl_header_title);
        ic_arrow_back = (ImageView) findViewById(R.id.ic_one);
        lst_option = (ListView) findViewById(R.id.lst_options);

        lbl_header_title.setText(R.string.lbl_title_configuration);
        ic_arrow_back.setImageResource(R.drawable.ic_back_arrow);

        configurationAdapter = new ConfigurationAdapter(this, 0, ConfigurationData.configurationData());

        lst_option.setAdapter(configurationAdapter);

    }

    static class ConfigurationData {
        public static ArrayList<ItemConfiguration> configurationData() {
            ItemConfiguration itemConfiguration;

            ArrayList<ItemConfiguration> itemsConfiguration = new ArrayList<>();

            int[] ic_option = {R.drawable.ic_settings, R.drawable.ic_logout, R.drawable.ic_about};

            String[] lbl_option = {"Ajustes", "Cerrar sesión", "Acerca de"};

            for (int index = 0; index <= ic_option.length - 1; index++) {
                itemConfiguration = new ItemConfiguration();

                itemConfiguration.lbl_option = lbl_option[index];
                itemConfiguration.ic_option = ic_option[index];

                itemsConfiguration.add(itemConfiguration);
            }

            return itemsConfiguration;
        }
    }
}
