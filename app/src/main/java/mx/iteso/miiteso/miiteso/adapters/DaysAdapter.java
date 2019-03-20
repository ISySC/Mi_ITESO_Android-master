package mx.iteso.miiteso.miiteso.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.miiteso.model.ItemDay;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 22/08/2018.
 */

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {



    private static List<ItemDay> itemDays;
    private Context activity;
    boolean firstLoad= true;
    private RecyclerView recyclerView;
    private ArrayList<ItemDay> deletePosition = new ArrayList<>();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvDay,tvDayNumber;
        public LinearLayout lyRoot;
        public MyViewHolder(View ItemView) {
            super(ItemView);
            tvDay = ItemView.findViewById(R.id.tv_day);
            tvDayNumber = ItemView.findViewById(R.id.tv_day_numb);
            lyRoot = ItemView.findViewById(R.id.ly_root);
        }
    }

    public DaysAdapter(@NonNull Context activity, RecyclerView mRecyclerView ,int resource, ArrayList<ItemDay> itemDays
    ) {
        DateFormat dateFormatName = new SimpleDateFormat("EEE, MMM d, ''yy");
        String tomorrowNameAsString = dateFormatName.format(Constantes.currentWorkingDate);
        Date result = null;
        try {
            result = dateFormatName.parse(tomorrowNameAsString);
            Constantes.currentWorkingDate = result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.activity = activity;
        this.itemDays = itemDays;
        this.recyclerView = mRecyclerView;



        for (int i =0;i<itemDays.size();i++) {
            if (itemDays.get(i).getCurrentDate().compareTo(Constantes.currentWorkingDate) == 0) {
                Constantes.selectedPosition.set(i);
                break;
            }
        }
        for (int i =0;i<itemDays.size();i++) {
        if (i < Constantes.selectedPosition.get()-14 || i > Constantes.selectedPosition.get()+14)
        {deletePosition.add(itemDays.get(i)); }
        }
        itemDays.removeAll(deletePosition);

        notifyDataSetChanged();
        for (int i =0;i<itemDays.size();i++) {
            if (itemDays.get(i).getCurrentDate().compareTo(Constantes.currentWorkingDate)==0) {
                Constantes.selectedPosition.set(i);
                //tvDate.setText(itemDays.get(i).getDcompleteDAyName() + " " + itemDays.get(i).getDayNumber() );
            }
        }

        Constantes.firstShowingDay = itemDays.get(0).getCurrentDate();
        Constantes.lastShowingDay = itemDays.get(itemDays.size()-1).getCurrentDate();



    }





    @Override
    public DaysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvDay.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
        holder.tvDayNumber.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
        Date date = new Date();
        if (DateUtils.isToday(itemDays.get(position).getCurrentDate().getTime()))
        {
            itemDays.get(position).setDayName("HOY");

            holder.tvDayNumber.setVisibility(View.GONE);

            //if(itemDays.get(position).getDayName().equals("HOY"))
                holder.tvDay.setTextColor(ContextCompat.getColor(activity, R.color.color_current_day));

        }
        if(Constantes.selectedPosition.get()==position) {
            holder.tvDay.setTextColor(ContextCompat.getColor(activity, R.color.color_white));
            holder.tvDayNumber.setTextColor(ContextCompat.getColor(activity, R.color.color_white));
            Constantes.currentWorkingDate = itemDays.get(position).getCurrentDate();
            //setSelectedItem(position);
            Constantes.currentWorkingDay = itemDays.get(position).getDcompleteDAyName();
            int colorFrom = ContextCompat.getColor(activity, R.color.color_white);
            int colorTo = ContextCompat.getColor(activity, R.color.nice_blue);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(2000); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    itemDays.get(position).setTag("blue");
                    holder.lyRoot.getBackground().setColorFilter((int) animator.getAnimatedValue(),PorterDuff.Mode.SRC_ATOP); // ((int) animator.getAnimatedValue());

                }

            });
            colorAnimation.start();

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String tag = itemDays.get(position).getTag();
                if ( tag.equals("blue") ) {
                    holder.tvDay.setTextColor(ContextCompat.getColor(activity, R.color.greyish_brown));
                    int  colorTo = ContextCompat.getColor(activity, R.color.color_white);
                    int colorFrom = ContextCompat.getColor(activity, R.color.nice_blue);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(2000); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            itemDays.get(position).setTag("white");
                            holder.lyRoot.getBackground().setColorFilter((int) animator.getAnimatedValue(),PorterDuff.Mode.SRC_ATOP); // ((int) animator.getAnimatedValue());

                        }

                    });
                    colorAnimation.start();
                }
            }
            holder.tvDayNumber.setTextColor(ContextCompat.getColor(activity, R.color.greyish_brown));
            holder.lyRoot.setBackgroundColor(ContextCompat.getColor(activity, R.color.color_transparent));

        }

        holder.itemView.getLayoutParams().width = (int) (getScreenWidth() / 5); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS


        holder.tvDay.setText(itemDays.get(position).getDayName());
        holder.tvDayNumber.setText(String.valueOf( itemDays.get(position).getDayNumber()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(position);
                //tvDate.setText(itemDays.get(position).getDcompleteDAyName() + " " + itemDays.get(position).getDayNumber() );
                notifyDataSetChanged();

            }
        });

    }



    public static void setSelectedItem(int position)
    {
        Constantes.currentWorkingDate = itemDays.get(position).getCurrentDate();
        Constantes.selectedPosition.set(position);
    }

    public static  ItemDay getItem(int position)
    {
       return itemDays.get(position);
    }

    public static int getCount()
    {
            return itemDays.size();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemDays.size();
    }
}
