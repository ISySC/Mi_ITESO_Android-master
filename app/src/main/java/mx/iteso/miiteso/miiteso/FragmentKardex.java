package mx.iteso.miiteso.miiteso;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.iteso.miiteso.AccountStatusFragmtents.GetAccountResumen;
import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.miiteso.adapters.SectionsPagerAdapter;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 26/04/2018.
 */

public class FragmentKardex extends Fragment {
    TextView last_update, lbl_curses, lbl_curses_number, lbl_porcentaje, lbl_porcentaje_number, lbl_average, lbl_average_number;
    TabLayout tabLayout;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kardex, container, false);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        return view;
    }

    private void setImplementListener() {

    }

    private void setTypography() {
        last_update.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratLight));
        lbl_curses.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_curses_number.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_porcentaje.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_porcentaje_number.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_average.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
        lbl_average_number.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratRegular));
    }

    private void setInitializeComponent() {
        tabLayout = view.findViewById(R.id.tabLayout);
        last_update = view.findViewById(R.id.last_update);
        lbl_curses = view.findViewById(R.id.lbl_curses);
        lbl_curses_number = view.findViewById(R.id.lbl_curses_number);
        lbl_porcentaje = view.findViewById(R.id.lbl_porcentaje);
        lbl_porcentaje_number = view.findViewById(R.id.lbl_porcentaje_number);
        lbl_average = view.findViewById(R.id.lbl_average);
        lbl_average_number = view.findViewById(R.id.lbl_average_number);


        //tabLayout.addTab(tabLayout.newTab().setText("Plan general"));
        //tabLayout.addTab(tabLayout.newTab().setText("Créditos por área"));


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(null);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);


        last_update.setText("Fecha de actualización: " + Metodos.getCurrenDateTimeFormat());

        new Metodos(getActivity()).changeTabsFont(tabLayout);
    }


    private void setupViewPager(ViewPager viewPager) {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), getActivity());

        Bundle bundleResumen = new Bundle();
        bundleResumen.putSerializable("WEB_SERVICES", ServiciosWeb.HistorialAcademica);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleResumen, "Historia Académica", 0);

        Bundle bundleShort = new Bundle();
        bundleShort.putSerializable("WEB_SERVICES", ServiciosWeb.CreditsArea);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleShort, "Créditos por área", 1);

        Bundle bundleLong = new Bundle();
        bundleLong.putSerializable("WEB_SERVICES", ServiciosWeb.InscribedSubject);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleLong, "Materias inscritas", 2);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mSectionsPagerAdapter);
    }
}
