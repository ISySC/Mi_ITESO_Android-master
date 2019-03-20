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
import mx.iteso.miiteso.miiteso.adapters.SettingAdapter;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.miiteso.model.ItemSetting;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class SettingActivity extends Activity {
    TextView lbl_title_header, lbl_title_show_hide_notice;
    ImageView ic_back_arrow;

    ListView lst_setting;

    ArrayList<ItemSetting> itemSettings = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        String[] settings = new String[]{"Calendario Escolar", "Moodle", "Horario Escolar", "Agenda ITESO",
                "CRUCE", "MAGIS"};

        boolean[] isCheked = new boolean[]{
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "calendario").equals("1") ? true : false),
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "moodle").equals("1") ? true : false),
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "horario").equals("1") ? true : false),
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "agenda").equals("1") ? true : false),
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "cruce").equals("1") ? true : false),
                (new Metodos(this).getSharedPreference(this.getString(R.string.preference_file_key),
                        "magis").equals("1") ? true : false)};

        String[] nameSettings = new String[]{"calendario", "moodle", "horario", "agenda",
                "cruce", "magis"};

        for (int index = 0; index <= settings.length - 1; index++)
            itemSettings.add(new ItemSetting(settings[index], isCheked[index], false, nameSettings[index]));

        lst_setting.setAdapter(new SettingAdapter(SettingActivity.this, 0, itemSettings));
    }

    private void setImplementListener() {
        ic_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setTypography() {
        lbl_title_header.setTypeface(new Metodos(SettingActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_title_show_hide_notice.setTypeface(new Metodos(SettingActivity.this).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        lbl_title_header = (TextView) findViewById(R.id.lbl_header_title);
        lbl_title_show_hide_notice = findViewById(R.id.lbl_title_show_hide_notice);
        ic_back_arrow = (ImageView) findViewById(R.id.ic_one);
        lst_setting = (ListView) findViewById(R.id.lst_setting);

        lbl_title_header.setText(getString(R.string.lbl_setting));
        ic_back_arrow.setImageResource(R.drawable.ic_back_arrow);
    }
    @Override
    public void onResume()
    {
        Constantes.mainNoticeAdapter=null;
        super.onResume();
    }
}
