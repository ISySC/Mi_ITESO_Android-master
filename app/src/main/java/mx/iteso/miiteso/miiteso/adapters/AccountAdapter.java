package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.model.ItemAccountResumen;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 04/03/2018.
 */

public class AccountAdapter extends ArrayAdapter<ItemAccountResumen> implements onTaskCompleted {

    private Activity activity;
    private ArrayList<ItemAccountResumen> itemAccountResumen = new ArrayList<>();
    private String mesageNullAdapter;
    boolean[] animationStates;
    ServiciosWeb servicioWeb;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public AccountAdapter(@NonNull Activity activity, int resource, ServiciosWeb servicioWeb, SwipeRefreshLayout mSwipeRefreshLayout) {
        super(activity, resource /*,itemAccountResumen*/);
        this.activity = activity;
        this.servicioWeb = servicioWeb;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setRefreshing(true);
        getResumen();
        //this.itemAccountResumen = itemAccountResumen;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position >= itemAccountResumen.size())
            position = itemAccountResumen.size() - 1;
            if (itemAccountResumen.get(0).getDecription() != "error" &&
                    itemAccountResumen.get(0).getDecription() != "vacio") {
            View item = inflater.inflate(R.layout.item_account_status, parent, false);

            TextView lbl_title = (TextView) item.findViewById(R.id.lbl_title);
            TextView lbl_sub_title = (TextView) item.findViewById(R.id.lbl_sub_title);
            TextView lbl_date = (TextView) item.findViewById(R.id.date);
            TextView lbl_total = (TextView) item.findViewById(R.id.lbl_total);
            TextView lbl_mount = (TextView) item.findViewById(R.id.amount);

            lbl_title.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            lbl_sub_title.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            lbl_date.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratMedium));
            lbl_total.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            lbl_mount.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratMedium));

            lbl_title.setText(itemAccountResumen.get(position).getDecription());

            NumberFormat format = NumberFormat.getCurrencyInstance();
            lbl_mount.setText(new Metodos(getContext()).format(itemAccountResumen.get(position).getAmount()));

            //lbl_mount.setText(format.format(Double.valueOf(itemAccountResumen.get(position).getAmount())));
            lbl_date.setText(itemAccountResumen.get(position).getDate());

            if (!animationStates[position]) {
                Log.e("TAG", "Animating item no: " + position);
                animationStates[position] = true;
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
                animation.setStartOffset(position * 500);
                item.startAnimation(animation);
            }


            return item;
        } else {
            View item = inflater.inflate(R.layout.layout_no_data, parent, false);
            if (itemAccountResumen.get(0).getDecription() == "vacio") {
                ((TextView) item.findViewById(R.id.tv_message)).setText("No hay información para mostrar");
                //((ImageView) item.findViewById(R.id.img_error)).setImageResource(R.drawable.ic_not_info);
                if (!animationStates[position]) {
                    Log.e("TAG", "Animating item no: " + position);
                    animationStates[position] = true;
                    Animation animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
                    animation.setStartOffset(position * 500);
                    item.startAnimation(animation);
                }
            }
            return item;
        }

    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void getResumen() {
        try {
            MethodWS.ES_ESTADO=true;
            this.clear();
            itemAccountResumen.clear();
            Constantes.KEY_NAME = null;
            String service="";
            //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
            switch (servicioWeb)
            {
                case AccountLong:
                    service = MethodWS.WS_EDO_CUENTA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/S";
                    break;
                case AccountShort:
                    service = MethodWS.WS_EDO_CUENTA + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/N";
                    break;
                case AccountResumen:
                    service= MethodWS.WS_EDO_CUENTA_RESUMEN  + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");
                    break;
            }
            new NetServices(this, activity,false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }

    @Override
    public void onTaskCompleted(String response) {
        ItemAccountResumen itemAccountResumen;

        Date date;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate="";
        try {

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    switch (this.servicioWeb)
                    {
                        case AccountResumen:

                            itemAccountResumen =
                                    new ItemAccountResumen( this.servicioWeb,
                                            jsonArray.getJSONObject(i).getString("fechaVencimiento"),jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("tipo"),jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("tipoCambio"),jsonArray.getJSONObject(i).getString("moneda")
                                            ,jsonArray.getJSONObject(i).getString("id"));
                            this.itemAccountResumen.add(itemAccountResumen);

                            break;
                        case AccountShort:
                            date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArray.getJSONObject(i).getString("fechaVence"));
                            strDate = dateFormat.format(date);
                            itemAccountResumen =
                                    new ItemAccountResumen( this.servicioWeb,
                                            strDate ,jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("nombreConcepto"),jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("tipoCambio"),jsonArray.getJSONObject(i).getString("moneda")
                                            ,jsonArray.getJSONObject(i).getString("id"),jsonArray.getJSONObject(i).getString("periodoEscolar")
                                    );//,jsonArray.getJSONObject(i).getString("periodoEscolar"));
                            this.itemAccountResumen.add(itemAccountResumen);

                            break;
                        case AccountLong:
                            date=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArray.getJSONObject(i).getString("fechaVence"));
                            strDate = dateFormat.format(date);

                            itemAccountResumen =
                                    new ItemAccountResumen( this.servicioWeb,
                                            strDate,jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("nombreConcepto"),jsonArray.getJSONObject(i).getString("importeMx")
                                            ,jsonArray.getJSONObject(i).getString("tipoCambio"),jsonArray.getJSONObject(i).getString("moneda")
                                            ,jsonArray.getJSONObject(i).getString("id"),jsonArray.getJSONObject(i).getString("periodoEscolar")
                                    );//,jsonArray.getJSONObject(i).getString("nivelAcademico"));
                            this.itemAccountResumen.add(itemAccountResumen);

                            break;
                    }
                }
                this.addAll(this.itemAccountResumen);
                notifyDataSetChanged();
            } else {
                this.itemAccountResumen.add(new ItemAccountResumen(this.servicioWeb,"","","vacio","","", "", ""));
                this.addAll(this.itemAccountResumen);
                notifyDataSetChanged();

            }

        } catch (JSONException ex) {
            Toast.makeText(activity,ex.toString(),Toast.LENGTH_LONG).show();
            this.itemAccountResumen.add(new ItemAccountResumen(this.servicioWeb,"","","error","","", "", ""));

            this.addAll(this.itemAccountResumen);
            notifyDataSetChanged();

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            animationStates = new boolean[this.itemAccountResumen.size()];
            this.mSwipeRefreshLayout.setRefreshing(false);
        }


    }

}
