package mx.iteso.miiteso.miiteso;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.miiteso.adapters.SectionsPagerAdapter;
import mx.iteso.miiteso.AccountStatusFragmtents.GetAccountResumen;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 26/04/2018.
 */

public class FragmentAccountStatus extends Fragment  {
    FragmentStatePagerAdapter fragmentStatePagerAdapter;
    static TextView last_update;
    TabLayout tabLayout;
    ListView fl_contenedor;
    ViewPager viewPager;
    View view;
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_status, container, false);
        //View v = new Metodos(getContext()).checkInstance(view);
        //if (v != null)
        //    return v;
        view = inflater.inflate(R.layout.fragment_account_status, container, false);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        updateDate();
        return view;
    }

    private void setImplementListener() {
    }


    private void setTypography() {
        last_update.setTypeface(new Metodos(getActivity()).typeface(Metodos.TypeFont.MontserratLight));
    }

    private void setInitializeComponent() {

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        last_update = view.findViewById(R.id.last_update);
        fl_contenedor = view.findViewById(R.id.fl_contenedor);

         viewPager = (ViewPager) view.findViewById(R.id.viewpager);
         viewPager.setAdapter(null);
         tabLayout.setupWithViewPager(viewPager);
         setupViewPager(viewPager);

        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);



        /*tabLayout.addTab(tabLayout.newTab().setText("Resumen"));
        tabLayout.addTab(tabLayout.newTab().setText("Corto"));
        tabLayout.addTab(tabLayout.newTab().setText("Largo"));*/

        new Metodos(getActivity()).changeTabsFont(tabLayout);
    }

    private void setupViewPager(ViewPager viewPager) {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(),getActivity());

        Bundle bundleResumen = new Bundle();
        bundleResumen.putSerializable("WEB_SERVICES", ServiciosWeb.AccountResumen);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleResumen, "Resumen",0);

        Bundle bundleShort = new Bundle();
        bundleShort.putSerializable("WEB_SERVICES", ServiciosWeb.AccountShort);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleShort, "Corto",1);

        Bundle bundleLong = new Bundle();
        bundleLong.putSerializable("WEB_SERVICES", ServiciosWeb.AccountLong);
        mSectionsPagerAdapter.addFragment(new GetAccountResumen(), bundleLong, "Largo",2);

            viewPager.setAdapter(mSectionsPagerAdapter);
    }
    @Override
    public void onDetach () {
        super.onDetach();
        Constantes.fragmentsInstances.add(view);

    }


    public static void updateDate()
    {
        last_update.setText("Fecha de actualización: " + Metodos.getCurrenDateTimeFormat() + " hrs");

    }

}
