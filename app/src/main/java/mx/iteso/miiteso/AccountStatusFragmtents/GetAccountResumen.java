package mx.iteso.miiteso.AccountStatusFragmtents;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.miiteso.FragmentAccountStatus;
import mx.iteso.miiteso.miiteso.adapters.AccountAdapter;
import mx.iteso.miiteso.miiteso.adapters.CreditAreaAdapter;
import mx.iteso.miiteso.miiteso.adapters.InscribedSubjects;
import mx.iteso.miiteso.miiteso.adapters.KardexGeneralAdatper;
import mx.iteso.miiteso.miiteso.onPromedioKardex;
import mx.iteso.miiteso.utilidades.Constantes;

/**
 * Created by rjuarez on 26/04/2018.
 */

public class GetAccountResumen extends Fragment {
    View view;
    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ServiciosWeb mParam;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_list, container, false);

        setInitializeComponent();
        setTypography();
        setImplementListener();

        return view;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = (ServiciosWeb) getArguments().getSerializable("WEB_SERVICES");
        }
    }

    private void setImplementListener() {
    }

    private void setTypography() {
    }

    private void setInitializeComponent() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.nice_blue);
        listView = view.findViewById(R.id.fl_contenedor);

        final CreditAreaAdapter creditAreaAdapter;
        final InscribedSubjects inscribedSubjects;
        final KardexGeneralAdatper kardexGeneralAdatper;

        switch (mParam) {
            case AccountLong:
                final AccountAdapter accountAdapterLong = new AccountAdapter(getActivity(), 0, ServiciosWeb.AccountLong, mSwipeRefreshLayout);
                listView.setAdapter(accountAdapterLong);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        accountAdapterLong.getResumen();
                        FragmentAccountStatus.updateDate();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                break;
            case AccountResumen:

                final AccountAdapter accountAdapterResumen = new AccountAdapter(getActivity(), 0, ServiciosWeb.AccountResumen, mSwipeRefreshLayout);
                listView.setAdapter(accountAdapterResumen);


                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        accountAdapterResumen.getResumen();
                        FragmentAccountStatus.updateDate();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

                break;
            case AccountShort:

                final AccountAdapter accountAdapterShort = new AccountAdapter(getActivity(), 0, ServiciosWeb.AccountShort, mSwipeRefreshLayout);
                listView.setAdapter(accountAdapterShort);


                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        accountAdapterShort.getResumen();
                        FragmentAccountStatus.updateDate();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


                break;
            case CreditsArea:
                creditAreaAdapter = new CreditAreaAdapter(getActivity(), 0, ServiciosWeb.CreditsArea, mSwipeRefreshLayout);
                listView.setAdapter(creditAreaAdapter);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        creditAreaAdapter.getAreas();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                break;

            case InscribedSubject:
                inscribedSubjects = new InscribedSubjects(getActivity(), 0, mSwipeRefreshLayout);
                listView.setAdapter(inscribedSubjects);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        inscribedSubjects.getInscribedSubjects();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
                break;
            case HistorialAcademica:
                kardexGeneralAdatper = new KardexGeneralAdatper(getActivity(), 0, mSwipeRefreshLayout, ServiciosWeb.RutaCurricular);
                listView.setAdapter(kardexGeneralAdatper);

                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        kardexGeneralAdatper.getSubjects();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });

                break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Constantes.fragmentsInstances.add(view);
    }
}
