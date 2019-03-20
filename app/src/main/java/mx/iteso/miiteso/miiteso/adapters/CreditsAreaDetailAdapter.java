package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.itesubes.ItesubesMapActivity;
import mx.iteso.miiteso.miiteso.model.ItemSubject;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

public class CreditsAreaDetailAdapter extends ArrayAdapter<ItemSubject> implements onTaskCompleted {

    private Activity activity;
    private ArrayList<ItemSubject> itemsSubject = new ArrayList<>();
    private String mesageNullAdapter;
    boolean[] animationStates;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public CreditsAreaDetailAdapter(@NonNull Activity activity, int resource, SwipeRefreshLayout mSwipeRefreshLayout) {
        super(activity, resource);
        this.activity = activity;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setRefreshing(true);
        getCredits();
    }

    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //-----------Falta Acomodar servicio web adecudo-----------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    public void getCredits() {
        try {
            this.clear();
            itemsSubject.clear();
            Constantes.KEY_NAME = null;
            String service = "";
            service = MethodWS.WS_CREDITOS_AREA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");

            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position >= itemsSubject.size())
            position = itemsSubject.size() - 1;
        if (!itemsSubject.get(0).getName().equals("error") &&
                !itemsSubject.get(0).getName().equals("vacio")) {
            View item = inflater.inflate(R.layout.item_route_detail, parent, false);

            TextView tvName = (TextView) item.findViewById(R.id.tv_name);
            TextView tvCredits = (TextView) item.findViewById(R.id.tv_credits);


            tvName.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            tvCredits.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));

            tvName.setText(itemsSubject.get(position).getName());
            tvCredits.setText( String.valueOf( itemsSubject.get(position).getCredits()));


            ((View)tvName.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity,ItesubesMapActivity.class).
                            putExtra("WEB_SERVICES",ServiciosWeb.DetailItemSubject).putExtra("TITLE","").putExtra("SUB_TITLE",""));
                }
            });
            if (!animationStates[position]) {
                anim(position, item);
            }
            return item;
        } else {
            View item = inflater.inflate(R.layout.layout_no_data, parent, false);
            if (itemsSubject.get(0).getName() == "vacio") {
                ((TextView) item.findViewById(R.id.tv_message)).setText("No hay informacion para mostrar");
                //((ImageView) item.findViewById(R.id.img_error)).setImageResource(R.drawable.ic_not_info);
                if (!animationStates[position])
                    anim(position, item);
            }
            return item;
        }

    }

    private void anim(int position, View item) {
        Log.e("TAG", "Animating item no: " + position);
        animationStates[position] = true;
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        animation.setStartOffset(position * 500);
        item.startAnimation(animation);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public void onTaskCompleted(String response) {
        {
            ItemSubject itemCreditArea;

            //try {
                //JSONArray jsonArray = new JSONArray(response);
                //if (jsonArray.length() > 0) {
                    for (int i = 0; i < 15/*jsonArray.length()*/; i++) {

                        itemCreditArea =
                                new ItemSubject("Dibujo de la forma","Cyndi Nero",
                                        "Español","Para cursar esta materia es necesario contar con una MAC."
                                        ,8);
                        this.itemsSubject.add(itemCreditArea);

                    }
                    this.addAll(this.itemsSubject);
                    notifyDataSetChanged();
              /*  } else {
                    this.itemsSubject.add(new ItemSubject("vacio","","",""));
                    this.addAll(this.itemsSubject);
                    notifyDataSetChanged();

                }

            } catch (JSONException ex) {
                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show();
                this.itemsSubject.add(new ItemSubject("vacio","","",""));
                this.addAll(this.itemsSubject);
                notifyDataSetChanged();

            } finally {
                animationStates = new boolean[this.itemsSubject.size()];
                this.mSwipeRefreshLayout.setRefreshing(false);
            }*/
            animationStates = new boolean[this.itemsSubject.size()];
            this.mSwipeRefreshLayout.setRefreshing(false);

        }
    }
}
