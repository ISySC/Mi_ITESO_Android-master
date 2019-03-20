package mx.iteso.miiteso.miiteso;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.adapters.DaysAdapter;
import mx.iteso.miiteso.miiteso.adapters.MainNoticeAdapter;
import mx.iteso.miiteso.miiteso.classes.OnIntegerChangeListener;
import mx.iteso.miiteso.miiteso.classes.OnSwipeTouchListener;
import mx.iteso.miiteso.miiteso.model.ItemMainNotice;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

import static mx.iteso.miiteso.utilidades.Constantes.mainNoticeAdapter;

/**
 * Created by rjuarez on 05/03/2018.
 */

public class FragmentNotice extends Fragment {
    ListView lst_notice;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<ItemMainNotice> itemMainNotices = new ArrayList<>();

    RecyclerView rvDays;
    ImageView imgNextDate;
    ImageView imgPreviousDate;
    TextView tvDate;


    ViewGroup container;

    public static FragmentNotice newInstance() {
        FragmentNotice fragment = new FragmentNotice();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        this.container = container;
        setInitializeComponent();
        setTypography();


        setNotice();

        return view;
    }


    private void setTypography() {
    }

    private void setInitializeComponent() {

        rvDays = (RecyclerView) view.findViewById(R.id.rv_day);
        imgNextDate = (ImageView) view.findViewById(R.id.img_next_date);
        imgPreviousDate = (ImageView) view.findViewById(R.id.img_previous_date);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        swipeRefreshLayout = view.findViewById(R.id.refresh);



        TextView tv_accurency_date = view.findViewById(R.id.tv_date);

        Constantes.currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()).toUpperCase();
        tv_accurency_date.setText("HOY " + new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault()).format(new Date()).toUpperCase().replace(".", ""));

        lst_notice = (ListView) view.findViewById(R.id.lst_notice);


        new Metodos(getContext()).showCalendar(getContext(), rvDays, tvDate, Constantes.calender.get(Calendar.WEEK_OF_YEAR));

        //new Metodos(getActivity()).showOvalNotification(ic_oval_notification_people,
        //        0, lbl_count_notification_people, "3");

        //rvDays.smoothScrollToPosition(Constantes.selectedPosition.get());

    }


    private void setNotice() {

        getInfo();

        lst_notice.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeLeft() {
                int newPosition = Constantes.selectedPosition.get() - 1;
                //Constantes.selectedPosition.set(newPosition);

                DaysAdapter.setSelectedItem(newPosition);

                return true;
            }

            @Override
            public boolean onSwipeRight() {
                int newPosition = Constantes.selectedPosition.get() + 1;
                //Constantes.selectedPosition.set(newPosition);
                DaysAdapter.setSelectedItem(newPosition);
                return true;
            }
        });


        try {
            Constantes.selectedPosition.setOnIntegerChangeListener(new OnIntegerChangeListener() {
                @Override
                public void onIntegerChanged(int newValue) {
                    if (Constantes.selectedPosition.get() >= 0 && Constantes.selectedPosition.getCount() > 1
                            && Constantes.selectedPosition.getPrevoius() != Constantes.selectedPosition.get()) {
                        if (mainNoticeAdapter != null)
                            if (!mainNoticeAdapter.isEmpty() && getFragmentManager() != null) {
                                //mainNoticeAdapter.showTodayNotice();
                                if (Constantes.selectedPosition.getPrevoius() <= Constantes.selectedPosition.get()) {
                                    Metodos.replaceFragmentWithAnimationLeft(FragmentNotice.newInstance(), getFragmentManager().beginTransaction(), container.getId(), "");
                                } else {
                                    Metodos.replaceFragmentWithAnimationRight(FragmentNotice.newInstance(), getFragmentManager().beginTransaction(), container.getId(), "");
                                }
                            }
                    }
                }
            });
        } catch (Exception ex) {
            Log.i("errorSetting", ex.toString());
            //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }

         swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainNoticeAdapter = new MainNoticeAdapter(getActivity(), 0);
                swipeRefreshLayout.setRefreshing(false);
                getInfo();
            }
        });

    }


    private void getInfo()
    {
        if (mainNoticeAdapter == null) {
            mainNoticeAdapter = new MainNoticeAdapter(getActivity(), 0);
        }
            lst_notice.setAdapter(mainNoticeAdapter);
            mainNoticeAdapter.showTodayNotice();


    }

    @Override
    public void onResume() {
        super.onResume();
        //setNotice();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
