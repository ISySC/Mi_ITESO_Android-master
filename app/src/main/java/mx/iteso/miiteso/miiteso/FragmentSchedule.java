package mx.iteso.miiteso.miiteso;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.adapters.DaysAdapter;
import mx.iteso.miiteso.miiteso.adapters.ScheduleAdapter;
import mx.iteso.miiteso.miiteso.classes.OnIntegerChangeListener;
import mx.iteso.miiteso.miiteso.classes.OnSwipeTouchListener;
import mx.iteso.miiteso.miiteso.classes.ScheduleDay;
import mx.iteso.miiteso.miiteso.classes.Subject;
import mx.iteso.miiteso.miiteso.model.ItemDay;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 05/03/2018.
 */

public class FragmentSchedule extends Fragment {
    RecyclerView rvHours;
    TextView tv_message;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ScheduleAdapter scheduleAdapter;
    View view;
    private RecyclerView.LayoutManager mLayoutManager;

    ViewGroup container;

    public static FragmentSchedule newInstance() {
        FragmentSchedule fragment = new FragmentSchedule();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        this.container = container;
        setInitializeComponent();
        setTypography();

        return view;
    }

    private void setTypography() {

    }

    private void setInitializeComponent() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        tv_message = view.findViewById(R.id.tv_message);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.nice_blue);
        mSwipeRefreshLayout.setEnabled(false);


        RecyclerView rvDays = (RecyclerView) view.findViewById(R.id.rv_day);
        ImageView imgNextDate = (ImageView) view.findViewById(R.id.img_next_date);
        ImageView imgPreviousDate = (ImageView) view.findViewById(R.id.img_previous_date);
        TextView tvDate = (TextView) view.findViewById(R.id.tv_date);

        Calendar calender = GregorianCalendar.getInstance();
        new Metodos(getContext()).showCalendar(getContext(),rvDays,tvDate,calender.get(Calendar.WEEK_OF_YEAR));
        rvHours =  (RecyclerView) view.findViewById(R.id.rv_hours);
        scheduleAdapter = new ScheduleAdapter(getActivity(),mSwipeRefreshLayout,rvHours,tv_message);
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        rvHours.setLayoutManager(mLayoutManager);

        rvHours.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeLeft() {
                int newPosition=Constantes.selectedPosition.get() -1;
                //Constantes.selectedPosition.set(newPosition);
                DaysAdapter.setSelectedItem(newPosition);

                return true;
            }

            @Override
            public boolean onSwipeRight() {

                int newPosition=Constantes.selectedPosition.get() +1;
                //Constantes.selectedPosition.set(newPosition);
                DaysAdapter.setSelectedItem(newPosition);

                return true;
            }});

        tv_message.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeLeft() {
                int newPosition=Constantes.selectedPosition.get() -1;
                //Constantes.selectedPosition.set(newPosition);
                DaysAdapter.setSelectedItem(newPosition);

                return true;
            }

            @Override
            public boolean onSwipeRight() {

                int newPosition=Constantes.selectedPosition.get() +1;
                //Constantes.selectedPosition.set(newPosition);
                DaysAdapter.setSelectedItem(newPosition);

                return true;
            }});
        imgNextDate = (ImageView) view.findViewById(R.id.img_next_date);
        imgPreviousDate = (ImageView) view.findViewById(R.id.img_previous_date);

        imgNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Constantes.selectedPosition.get() + 5;
                if (value < DaysAdapter.getCount()-1)
                    Constantes.selectedPosition.set(value);
                else
                    Constantes.selectedPosition.set(DaysAdapter.getCount()-1);

            }
        });

        imgPreviousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Constantes.selectedPosition.get() - 5;
                if (value > 0)
                    Constantes.selectedPosition.set(value);
                else
                    Constantes.selectedPosition.set(0);
            }
        });
       try {
           Constantes.selectedPosition.setOnIntegerChangeListener(new OnIntegerChangeListener() {
               @Override
               public void onIntegerChanged(int newValue) {
                   if (Constantes.selectedPosition.get() >= 0 && Constantes.selectedPosition.getCount()>1) {
                       if (scheduleAdapter != null && getFragmentManager() != null) {
                           rvHours.setAdapter(scheduleAdapter);
                           if (getFragmentManager().beginTransaction() != null)
                           if (Constantes.selectedPosition.getPrevoius() <= Constantes.selectedPosition.get()) {
                               Metodos.replaceFragmentWithAnimationLeft(FragmentSchedule.newInstance(), getFragmentManager().beginTransaction(), container.getId(), "");
                           } else {
                               Metodos.replaceFragmentWithAnimationRight(FragmentSchedule.newInstance(), getFragmentManager().beginTransaction(), container.getId(), "");
                           }
                           scheduleAdapter.getItemCount();
                       }
                   }
               }
           });
       }catch (Exception ex){
           //Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
       }


        rvHours.setAdapter(scheduleAdapter);
        /*if (scheduleAdapter.getItemCount() == 0) {
            rvHours.setVisibility(View.GONE);
            tv_message.setText("No hay informacion para este dia");
            tv_message.setVisibility(View.VISIBLE);
        }
        else{
            rvHours.setVisibility(View.VISIBLE);
            tv_message.setVisibility(View.GONE);
        }
        */
    }



}
