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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.miiteso.model.ItemInscribedSubject;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by PC on 24/10/2018.
 */

public class InscribedSubjects extends ArrayAdapter<ItemInscribedSubject> implements onTaskCompleted {

    private Activity activity;
    private ArrayList<ItemInscribedSubject> itemInscribedSubjects = new ArrayList<>();
    private String mesageNullAdapter;
    boolean[] animationStates;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public InscribedSubjects(@NonNull Activity activity, int resource, SwipeRefreshLayout mSwipeRefreshLayout) {
        super(activity, resource);
        this.activity = activity;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setRefreshing(true);
        getInscribedSubjects();
    }

    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //-----------Falta Acomodar servicio web adecudo-----------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    //---------------------------------------------------------//
    public void getInscribedSubjects() {
        try {
            this.clear();
            itemInscribedSubjects.clear();
            Constantes.KEY_NAME = null;
            String service = "";
            //String key = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
            service = MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/" + Constantes.currentWorkingWeek + "/" + Calendar.getInstance().get(Calendar.YEAR);

            new NetServices(this, activity, false).execute("post", service);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position >= itemInscribedSubjects.size())
            position = itemInscribedSubjects.size() - 1;
        if (!itemInscribedSubjects.get(0).getName().equals("error") &&
                !itemInscribedSubjects.get(0).getName().equals("vacio")) {
            View item = inflater.inflate(R.layout.item_subject, parent, false);

            TextView tvName = (TextView) item.findViewById(R.id.tv_ciclo);
            TextView tvRoomRes = (TextView) item.findViewById(R.id.tv_room_res);
            TextView tvTeacherRes = (TextView) item.findViewById(R.id.tv_teacher_res);
            TextView tvLnguajeRes = (TextView) item.findViewById(R.id.tv_lenguaje_res);

            tvName.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            tvRoomRes.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            tvTeacherRes.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            tvLnguajeRes.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));


            tvName.setText(itemInscribedSubjects.get(position).getName());
            tvRoomRes.setText(itemInscribedSubjects.get(position).getClassRoom());
            tvTeacherRes.setText(itemInscribedSubjects.get(position).getTeacher());
            tvLnguajeRes.setText(itemInscribedSubjects.get(position).getLenguaje());

            if (!animationStates[position]) {
                anim(position, item);
            }
            return item;
        } else {
            View item = inflater.inflate(R.layout.layout_no_data, parent, false);
            if (itemInscribedSubjects.get(0).getName() == "vacio") {
                ((TextView) item.findViewById(R.id.tv_message)).setText("No hay información para mostrar");
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
            ItemInscribedSubject itemInscribedSubject;

            try {
                JSONArray jsonArray = new JSONArray(response);

                itemInscribedSubject =
                        new ItemInscribedSubject(jsonArray.getJSONObject(0).getString("asignatura"), "Profesor: " + jsonArray.getJSONObject(0).getString("nombreProfesor"),
                                "Idioma: " + jsonArray.getJSONObject(0).getString("idioma"), "Para cursar esta materia es necesario contar con una MAC."
                                , jsonArray.getJSONObject(0).getString("salon"), 8);
                this.itemInscribedSubjects.add(itemInscribedSubject);

                if (jsonArray.length() > 0) {
                    for (int index = 1; index <= jsonArray.length() - 1; index++) {

                        if (!uniqueSubject(itemInscribedSubject, jsonArray.getJSONObject(index).getString("asignatura"))) {
                            itemInscribedSubject =
                                    new ItemInscribedSubject(jsonArray.getJSONObject(index).getString("asignatura"), "Profesor: " + jsonArray.getJSONObject(index).getString("nombreProfesor"),
                                            "Idioma: " + jsonArray.getJSONObject(index).getString("idioma"), "Para cursar esta materia es necesario contar con una MAC."
                                            , jsonArray.getJSONObject(index).getString("salon"), 8);
                            this.itemInscribedSubjects.add(itemInscribedSubject);
                        }

                    }
                }
            } catch (Exception e) {
                Log.d("errorMaterias", e.toString());
            }

            this.addAll(this.itemInscribedSubjects);
            notifyDataSetChanged();

            animationStates = new boolean[this.itemInscribedSubjects.size()];
            this.mSwipeRefreshLayout.setRefreshing(false);

        }
    }

    public boolean uniqueSubject(ItemInscribedSubject itemInscribedSubject, String subject) {
        boolean existsSubject = false;

        for (ItemInscribedSubject item : itemInscribedSubjects)
            if (item.getName().equals(subject)) {
                existsSubject = true;
                break;
            }
            else
                existsSubject = false;

        return existsSubject;
    }
}