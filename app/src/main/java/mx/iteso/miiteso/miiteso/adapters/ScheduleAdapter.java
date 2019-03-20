package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.classes.OnIntegerChangeListener;
import mx.iteso.miiteso.miiteso.classes.ScheduleDay;
import mx.iteso.miiteso.miiteso.classes.Subject;
import mx.iteso.miiteso.miiteso.model.ItemAccountResumen;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 17/08/2018.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> implements onTaskCompleted {
    private Activity context;
    private ArrayList<Subject> data = new ArrayList<>();
    private LayoutInflater inflater;
    private int previousPosition = 0;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView; TextView tvMessage;
    ScheduleDay day = new ScheduleDay("");


    public ScheduleAdapter(final Activity context, SwipeRefreshLayout mSwipeRefreshLayout,RecyclerView recyclerView, TextView tvMessage) {
        try {
            this.context = context;
            this.mSwipeRefreshLayout = mSwipeRefreshLayout;
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setRefreshing(true);
            getClases();
            this.recyclerView = recyclerView;
            this.tvMessage = tvMessage;
            inflater = LayoutInflater.from(context);
        } catch (Exception ex) {
        }
        ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_horario, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder myViewHolder, final int position) {



        String color = "#" + Integer.toHexString(data.get(position).getClaseID());

        String[] horaInicioFin = data.get(position).getHoraInicioFin().split("-");

        myViewHolder.lbl_description.setText(data.get(position).getAsignatura());
        myViewHolder.lbl_sub_description.setText(horaInicioFin[0].substring(0,5) + " - " + horaInicioFin[1].substring(0,6) + " hrs");
        myViewHolder.tv_hora_inicio.setText(String.valueOf(data.get(position).getHoraInicio()));
        myViewHolder.lbl_salon.setText(data.get(position).getSalon().toUpperCase());

        myViewHolder.lbl_description.setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratRegular));
        myViewHolder.lbl_sub_description.setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratRegular));
        myViewHolder.tv_hora_inicio.setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratRegular));
        myViewHolder.lbl_salon.setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratRegular));

        //myViewHolder.tv_horaFin.setText( String.valueOf(data.get(position).getHoraFin()));
        myViewHolder.lyColor.setBackgroundColor(data.get(position).getClaseID() == 0 ? Color.WHITE : Color.parseColor(color));
        int inicio = data.get(position).getHoraInicio() + 1;
        while (inicio < data.get(position).getHoraFin()) {
            TableRow tableRow = new TableRow(context);
            TextView textview = new TextView(context);
            textview.setText(String.valueOf(inicio));
            textview.setLayoutParams(new TableRow.LayoutParams(30, 150));
            textview.setGravity(Gravity.CENTER);
            textview.setTextColor(Color.parseColor("#053756"));
            tableRow.addView(textview);
            textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            myViewHolder.tableLayout.addView(tableRow);
            inicio++;
        }

    }

    @Override
    public int getItemCount() {

        if (data.size() == 0)
            Toast.makeText(context,"No hay información para mostrar",Toast.LENGTH_SHORT);
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_description, lbl_sub_description, tv_hora_inicio, lbl_salon, lbl_idioma;
        LinearLayout lyColor, lyItem;
        TableLayout tableLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_hora_inicio = (TextView) itemView.findViewById(R.id.tv_hora_inicio);
            lbl_description = (TextView) itemView.findViewById(R.id.lbl_description);
            lbl_sub_description = (TextView) itemView.findViewById(R.id.lbl_sub_description);
            lbl_salon = (TextView) itemView.findViewById(R.id.lbl_salon);

            lyColor = (LinearLayout) itemView.findViewById(R.id.ln_bar);
            lyItem = (LinearLayout) itemView.findViewById(R.id.ly_item);
            tableLayout = (TableLayout) itemView.findViewById(R.id.table_layout);

        }
    }

    private void removeItem(int currPosition) {

        data.remove(currPosition);
        notifyItemRemoved(currPosition);
    }

    // This method adds(duplicates) a Object (item ) to our Data set as well as Recycler View.
    private void addItem(int position, Subject infoData) {

        data.add(position, infoData);
        notifyItemInserted(position);
    }

    public void getClases() {
        try {
            Constantes.KEY_NAME = null;
            String service = "";
            MethodWS.ES_HORARIO = true;
            //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
            service = MethodWS.WS_HORARIO + new Metodos(context).getSharedPreference(context.getString(R.string.preference_file_key), "token") + "/" + String.valueOf(Constantes.currentWorkingWeek) + "/" + String.valueOf(DateFormat.format("yyyy", Constantes.currentWorkingDate));
            new NetServices(this, context, true).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }
    }


    @Override
    public void onTaskCompleted(String response) {

        ScheduleDay data = new ScheduleDay("");
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getJSONObject(i).getString("nombreDia").toLowerCase().equals(Constantes.currentWorkingDay.toLowerCase())) {
                        data.addSubject(new Subject(
                                Integer.valueOf(jsonArray.getJSONObject(i).getString("horaInicioFin").substring(0, 2))
                                , Integer.valueOf(jsonArray.getJSONObject(i).getString("horaInicioFin").substring(11, 13))
                                , Integer.valueOf(jsonArray.getJSONObject(i).getString("id"))
                                , jsonArray.getJSONObject(i).getString("grupo")
                                , jsonArray.getJSONObject(i).getString("salon")
                                , jsonArray.getJSONObject(i).getString("asignatura")
                                , jsonArray.getJSONObject(i).getString("nombreDia")
                                , jsonArray.getJSONObject(i).getString("horaInicioFin")
                        ));
                    }
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        } catch (JSONException ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
            notifyDataSetChanged();

        }

        try {
            int position = 0, cont = 1;
            boolean checkUp = false, checkDown = false, remove = false;
            while (position < data.getclases().size()) {
                while (!checkUp) {
                    if (position - cont >= 0) {
                        if (data.getclases().get(position - cont).getClaseID() == data.getclases().get(position).getClaseID()) {
                            data.getclases().get(position).setHoraInicio(data.getclases().get(position - cont).getHoraInicio());
                            data.getclases().remove(position - cont);
                            cont++;
                            remove = true;
                        } else checkUp = true;
                    } else
                        checkUp = true;

                }

                cont = 1;
                while (!checkDown) {
                    if (position + cont < data.getclases().size()) {
                        if (data.getclases().get(position + cont).getClaseID() == data.getclases().get(position).getClaseID()) {
                            if (data.getclases().get(position).getHoraFin() < data.getclases().get(position + cont).getHoraFin())
                                data.getclases().get(position).setHoraFin(data.getclases().get(position + cont).getHoraFin());
                            data.getclases().remove(position + cont);
                            cont++;
                            remove = true;
                        } else checkDown = true;
                    } else
                        checkDown = true;
                }
                if (!remove)
                    position++;
                else position = 0;
                checkDown = false;
                checkUp = false;
                remove = false;
            }
            for (int i = 0; i < data.getclases().size(); i++)
                if (data.getclases().get(i).getClaseID() == 0)
                    data.getclases().remove(i);

            if (data.getclases().size() > 0) {
                this.data = data.getclases();
                notifyDataSetChanged();
            }
            //else this.data.add(new Subject(0,0,0,"","","Vacio","",""));
        } catch (Exception ex) {
            Log.e("errorHorario", ex.toString());
            //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        } finally {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(false);

            if (this.data.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvMessage.setText("No hay información para este dia");
                tvMessage.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
            }
        }
    }
}