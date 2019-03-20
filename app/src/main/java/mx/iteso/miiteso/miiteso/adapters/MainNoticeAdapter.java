package mx.iteso.miiteso.miiteso.adapters;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.conectividadWS.NetServices;
import mx.iteso.miiteso.conectividadWS.RssServices;
import mx.iteso.miiteso.conectividadWS.ServiciosWeb;
import mx.iteso.miiteso.conectividadWS.onTaskCompleted;
import mx.iteso.miiteso.conectividadWS.onTaskCompletedRss;
import mx.iteso.miiteso.miiteso.WebViewNoticeActivity;
import mx.iteso.miiteso.miiteso.model.ItemMainNotice;
import mx.iteso.miiteso.utilidades.Constantes;
import mx.iteso.miiteso.utilidades.Metodos;

/**
 * Created by rjuarez on 04/03/2018.
 */

public class MainNoticeAdapter extends ArrayAdapter<ItemMainNotice> implements onTaskCompleted, onTaskCompletedRss {

    private Activity activity;
    private ArrayList<ItemMainNotice> itemMainNotices = new ArrayList<>();
    private boolean ordered = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ServiciosWeb servicioWeb;

    String rssLink;

    public MainNoticeAdapter(@NonNull Activity activity, int resource) {
        super(activity, resource);
        this.activity = activity;
        getCourses();
    }


    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (pos >= this.getCount()) {
            return null;
        }
        final int position = pos;

        LayoutInflater inflater = activity.getLayoutInflater();

        if (this.getItem(0).getTitle() != "error" &&
                this.getItem(0).getTitle() != "vacio") {


            View item = inflater.inflate(R.layout.item_main_notice, parent, false);

            TextView lbl_notice_description = (TextView) item.findViewById(R.id.lbl_notice_description);
            TextView lbl_sub_notice_description = (TextView) item.findViewById(R.id.lbl_sub_notice_description);
            TextView lbl_date = (TextView) item.findViewById(R.id.date);
            TextView lbl_type_notice = (TextView) item.findViewById(R.id.lbl_type_notice);
            TextView tv_url = item.findViewById(R.id.tv_url);

            ImageView ic_notice = (ImageView) item.findViewById(R.id.ic_notice);
            ImageView ic_next = item.findViewById(R.id.ic_next);

            LinearLayout ln_bar = (LinearLayout) item.findViewById(R.id.ln_bar);
            RelativeLayout rl_item_notice = (RelativeLayout) item.findViewById(R.id.rl_item_notice);

            lbl_notice_description.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratMedium));
            lbl_sub_notice_description.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            lbl_date.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratRegular));
            lbl_type_notice.setTypeface(new Metodos(activity).typeface(Metodos.TypeFont.MontserratLight));
            ;

            lbl_notice_description.setText(this.getItem(position).getTitle().replace("&#8220", "“").
                    replace("&#8221", "”"));

            if (!getItem(position).getUrl().equals(""))
                tv_url.setText(this.getItem(position).getUrl());
            else
                ic_next.setVisibility(View.INVISIBLE);


            switch (this.getItem(position).getTypeNotice()) {
                case 1:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText("Hora limite " + this.getItem(position).getStarDate().substring(11, 16) + " hrs");
                    lbl_type_notice.setText("Moodle");
                    break;
                case 2:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText(this.getItem(position).getStartHour() + " - " + this.getItem(position).getEndHour() + " hrs");
                    lbl_type_notice.setText("Horario clase");
                    break;
                case 3:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText(this.getItem(position).getStartHour() + " - " + this.getItem(position).getEndHour() + " hrs");
                    lbl_type_notice.setText("Calendario Escolar");
                    break;
                case 4:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText(this.getItem(position).getStartHour() + " - " + this.getItem(position).getEndHour() + " hrs");
                    lbl_type_notice.setText("Agenda ITESO");
                    break;
                case 5:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText(this.getItem(position).getStarDate().substring(0, 11));
                    lbl_type_notice.setText("MAGIS");
                    break;
                case 6:
                    lbl_sub_notice_description.setVisibility(View.INVISIBLE);
                    lbl_date.setText(this.getItem(position).getStartHour());
                    lbl_type_notice.setText("CRUCE");
                    break;
                case -1:
                    lbl_notice_description.setTextSize(20);
                    break;
                default:
                    //lbl_date.setText(this.getItem(position).getStarDate());
                    //lbl_type_notice.setText(this.getItem(position).getEndDate());

                    //lbl_sub_notice_description.setVisibility(View.GONE);
                    break;
            }

            if (!getItem(position).getUrl().equals("")) {
                ic_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Constantes.url_main_notice = getItem(position).getUrl();
                        new Metodos(activity).NavegarAPantalla(WebViewNoticeActivity.class);
                    }
                });
            }
            rl_item_notice.setFocusable(false);
            rl_item_notice.setClickable(false);

            ic_notice.setImageResource(this.getItem(position).getIcon());
            ln_bar.setBackgroundColor(this.getItem(position).getColor());
            return item;

        } else {


            View item = inflater.inflate(R.layout.layout_no_data, parent, false);
            if (this.getItem(0).getTitle().equalsIgnoreCase("vacio")) {

                TextView tv_no_data = item.findViewById(R.id.tv_message);
                tv_no_data.setTypeface(new Metodos(getContext()).typeface(Metodos.TypeFont.MontserratRegular));
                tv_no_data.setText("No hay avisos para mostrar");

                ViewGroup.LayoutParams params = item.getLayoutParams();

                // Set the height of the Item View
                params.height = parent.getHeight();

                item.setLayoutParams(params);

            }
            return item;
        }

    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void getCourses() {
        try {
            this.clear();

            Calendar c = Calendar.getInstance();
            c.setTime(Constantes.currentWorkingDate);
            c.getActualMaximum(Calendar.WEEK_OF_YEAR);
            int totalWeek;

            itemMainNotices.clear();
            servicioWeb = ServiciosWeb.NoticeMoodle;
            Constantes.KEY_NAME = null;

            //String token = "94cdf4f4bbdaf17c6fb9040292b3b0176c353db9768790e81eb4dfb515bb447217debfec95a67587d573827ae214bd797988909a3dd793ae0211f5d07600f603";
            String serviceMoodle = MethodWS.WS_MOODLE_COURSES + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token");


            c.add(Calendar.WEEK_OF_YEAR,-2);
            String serviceClass1 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(c.get(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", c));
            c.setTime(Constantes.currentWorkingDate);

            c.add(Calendar.WEEK_OF_YEAR,-1);
            String serviceClass2 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(c.get(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", c));
            c.setTime(Constantes.currentWorkingDate);

            c.add(Calendar.WEEK_OF_YEAR,+0);
            String serviceClass3 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(c.get(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", c));
            c.setTime(Constantes.currentWorkingDate);


            c.add(Calendar.WEEK_OF_YEAR,+1);
            String serviceClass4 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(c.get(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", c));
            c.setTime(Constantes.currentWorkingDate);


            c.add(Calendar.WEEK_OF_YEAR,+2);
            String serviceClass5 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(c.get(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", c));
            c.setTime(Constantes.currentWorkingDate);

            /*String serviceClass1 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(Constantes.currentWorkingWeek - 2 > 0 ? Constantes.currentWorkingWeek - 2 : 1) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", Constantes.currentWorkingDate));



            String serviceClass2 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(Constantes.currentWorkingWeek - 1 > 0 ? Constantes.currentWorkingWeek - 1 : 1) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", Constantes.currentWorkingDate));



            String serviceClass3 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(Constantes.currentWorkingWeek) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", Constantes.currentWorkingDate));



            String serviceClass4 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(Constantes.currentWorkingWeek + 1 <= c.getActualMaximum(Calendar.WEEK_OF_YEAR) ? Constantes.currentWorkingWeek + 1 : c.getActualMaximum(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", Constantes.currentWorkingDate));



            String serviceClass5 =
                    MethodWS.WS_HORARIO + new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key), "token") + "/"
                            + String.valueOf(Constantes.currentWorkingWeek + 2 <= c.getActualMaximum(Calendar.WEEK_OF_YEAR) ? Constantes.currentWorkingWeek + 2 : c.getActualMaximum(Calendar.WEEK_OF_YEAR)) + "/" + String.valueOf(android.text.format.DateFormat.format("yyyy", Constantes.currentWorkingDate));

            */
            String agendaITESO = "https://agenda.iteso.mx/wp-json/tribe/events/v1/events";
            String calendarioEscolar = "https://blogs.iteso.mx/calendarioescolar/wp-json/tribe/events/v1/events?start_date=" +
                    String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", Constantes.firstShowingDay)) +
                    "&end_date=" + String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", Constantes.lastShowingDay)) + "&per_page=10";
            String serviceCruse = "https://cruce.iteso.mx/feed/";
            String serviceMagis = "https://www.magis.iteso.mx/feed";
            ArrayList<String> serviciosRss = new ArrayList<String>();

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "cruce").equals("1"))
                serviciosRss.add(serviceCruse);

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "magis").equals("1"))
                serviciosRss.add(serviceMagis);

            if (serviciosRss.size() > 0)
                new RssServices(getContext(), this, serviciosRss, serviciosRss.size()).execute((Void) null);

            new NetServices(this, activity, false).execute("notif", serviceMoodle, serviceClass1, serviceClass2, serviceClass3, serviceClass4, serviceClass5, calendarioEscolar, agendaITESO);
        } catch (Exception ex) {
            Log.d("error", ex.toString());
        }
    }

    public void showTodayNotice() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date starDate, endDate;
        this.clear();


        final ArrayList<ItemMainNotice> itemsOrdered = new ArrayList<>();

        try {
            for (ItemMainNotice item : itemMainNotices) {
                starDate = sdf.parse(item.getStarDate());
                endDate = sdf.parse(item.getEndDate());
                if (Constantes.currentWorkingDate.compareTo(starDate) >= 0 && Constantes.currentWorkingDate.compareTo(endDate) <= 0) {
                    Log.i("NoticeDates", "Today: " + Constantes.currentWorkingDate.toString() + "  Item Start Date " +
                            starDate.toString() + "  Item end Date " + endDate.toString() + "\n\tItem: " + item.getTitle() + " is between dates");
                    itemsOrdered.add(item);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {


            CountDownTimer timer = new CountDownTimer(100, 100) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Collections.sort(itemsOrdered);
                    MainNoticeAdapter.this.addAll(itemsOrdered);
                    MainNoticeAdapter.this.notifyDataSetChanged();
                    if (MainNoticeAdapter.this.getCount() == 0) {
                        MainNoticeAdapter.this.clear();
                        MainNoticeAdapter.this.add(new ItemMainNotice("vacio"));
                    }
                    MainNoticeAdapter.this.notifyDataSetChanged();
                }
            };
            timer.start();

        }
    }

    //Calendario Escolar ///////////////////////////////////////////////////////
    private void getCalendarioEscolar(String response) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ItemMainNotice itemMainNotices;
        try {
            //Traer informacion del JSON y crear nuevos itemas para agregar al adapter. Servicio Web Agenda
            JSONArray jsonArrayCalendarioEscolar = new JSONObject(response).getJSONArray("events");
            if (jsonArrayCalendarioEscolar.length() > 0) {

                for (int i = 0; i < jsonArrayCalendarioEscolar.length(); i++) {
                    String[] startHour = jsonArrayCalendarioEscolar.getJSONObject(i).getString("start_date").split(" ");
                    String[] endHour = jsonArrayCalendarioEscolar.getJSONObject(i).getString("end_date").split(" ");

                    itemMainNotices =
                            new ItemMainNotice(
                                    jsonArrayCalendarioEscolar.getJSONObject(i).getString("title"),
                                    dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayCalendarioEscolar.getJSONObject(i).getString("start_date"))),
                                    dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayCalendarioEscolar.getJSONObject(i).getString("end_date"))),
                                    jsonArrayCalendarioEscolar.getJSONObject(i).getString("url"), 3,
                                    startHour[1].substring(0, 5),
                                    endHour[1].substring(0, 5));

                    if (jsonArrayCalendarioEscolar.getJSONObject(i).getString("status").equals("publish"))
                        this.itemMainNotices.add(itemMainNotices);
                }
            }
        } catch (JSONException ex) {
            Log.e("JSONException", ex.toString() + "Error");
            Log.e("JSON response", response);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }//////////////////////////////////////////////////////////////////

    // Moodle ///////////////////////////////////////////////////////////////
    private void getMoodle(String response) {
        ItemMainNotice itemMainNotices;
        try {

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    for (int a = 0; a < jsonArray.getJSONObject(i).getJSONArray("assignments").length(); a++) {
                        itemMainNotices =
                                new ItemMainNotice(
                                        jsonArray.getJSONObject(i).getString("shortname") + " - " + jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("name"),
                                        Metodos.getDateFromEpoch(jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("duedate")),
                                        Metodos.getDateFromEpoch(jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("cutoffdate")),
                                        "", 1, "", "");
                        this.itemMainNotices.add(itemMainNotices);
                    }
                }
                notifyDataSetChanged();
            }


        } catch (JSONException ex) {
            Log.e("JSONException", ex.toString() + "Error");
        }
    }
    //////////////////////////////////////////////////////////////////////////////

    // Horario Escolar //////////////////////////////////////////////////////////
    private void getHorario(String response) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        ItemMainNotice itemMainNotices;
        try {

            JSONArray jsonArrayHorario = new JSONArray(response);
            if (jsonArrayHorario.length() > 0) {

                for (int i = 0; i < jsonArrayHorario.length(); i++) {

                    // String formato = Metodos.getDateFromEpoch(Long.parseLong(jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("duedate"))).substring(0, 10);

                    String[] startHour = jsonArrayHorario.getJSONObject(i).getString("horaInicio").split(" ");
                    String[] endHour = jsonArrayHorario.getJSONObject(i).getString("horaFin").split(" ");


                    //if (Constantes.currentDate.equals(formato)) {
                    itemMainNotices =
                            new ItemMainNotice(
                                    jsonArrayHorario.getJSONObject(i).getString("asignatura"),
                                    dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayHorario.getJSONObject(i).getString("horaInicio"))),
                                    dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayHorario.getJSONObject(i).getString("horaFin"))),
                                    "", 2, startHour[1].substring(0, 5),
                                    endHour[1].substring(0, 5));
                    this.itemMainNotices.add(itemMainNotices);
                    //}
                }

            }


        } catch (JSONException ex) {
            Log.e("JSONException", ex.toString() + "Error");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }////////////////////////////////////////////////////////////////////////////////

    //Agenda ITESO ////////////////////////////////////////////////////////
    private void getAgendaITESO(String response) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ItemMainNotice itemMainNotices;

        try {
            JSONArray jsonArrayAgendaITESO = new JSONObject(response).getJSONArray("events");

            if (jsonArrayAgendaITESO.length() > 0) {
                for (int i = 0; i < jsonArrayAgendaITESO.length(); i++) {
                    if (jsonArrayAgendaITESO.getJSONObject(i).getString("status").equals("publish")) {
                        String[] startHour = jsonArrayAgendaITESO.getJSONObject(i).getString("start_date").split(" ");
                        String[] endHour = jsonArrayAgendaITESO.getJSONObject(i).getString("end_date").split(" ");

                        itemMainNotices =
                                new ItemMainNotice(
                                        jsonArrayAgendaITESO.getJSONObject(i).getString("title"),
                                        dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayAgendaITESO.getJSONObject(i).getString("start_date"))),
                                        dateFormat.format(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(jsonArrayAgendaITESO.getJSONObject(i).getString("end_date"))),
                                        jsonArrayAgendaITESO.getJSONObject(i).getString("url"), 4,
                                        startHour[1].substring(0, 5),
                                        endHour[1].substring(0, 5)
                                );

                        this.itemMainNotices.add(itemMainNotices);
                    }
                }

            }
        } catch (JSONException ex) {
            Log.e("ErrorAgendaITESO", ex.toString());
        } catch (ParseException e) {
            Log.e("ParseException", e.toString());
        }
    }
    ///////////////////////////////////////////////////////////

   /* private void  getCruceItems(String response) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ItemMainNotice itemMainNotices;
        try {

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    for (int a = 0; a < jsonArray.getJSONObject(i).getJSONArray("assignments").length(); a++) {
                        // String formato = Metodos.getDateFromEpoch(Long.parseLong(jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("duedate"))).substring(0, 10);

                        //if (Constantes.currentDate.equals(formato)) {
                        itemMainNotices =
                                new ItemMainNotice(
                                        jsonArray.getJSONObject(i).getString("shortname") + " - " + jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("name"),
                                        Metodos.getDateFromEpoch( jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("duedate")),
                                        Metodos.getDateFromEpoch( jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("cutoffdate")),
                                        "", 1);
                        this.itemMainNotices.add(itemMainNotices);
                        //}
                    }
                }
                //this.addAll(this.itemMainNotices);
                notifyDataSetChanged();
            }


        } catch (JSONException ex) {
            Log.e("JSONException", ex.toString() + "Error");
            Log.e("JSON response" ,response);

        } finally {
            if (servicioWeb == ServiciosWeb.NoticeMoodle) {
            }

        }
    }

    private void  getMagisItems(String response) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ItemMainNotice itemMainNotices;
        try {

            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    for (int a = 0; a < jsonArray.getJSONObject(i).getJSONArray("assignments").length(); a++) {
                        // String formato = Metodos.getDateFromEpoch(Long.parseLong(jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("duedate"))).substring(0, 10);

                        //if (Constantes.currentDate.equals(formato)) {
                        itemMainNotices =
                                new ItemMainNotice(
                                        jsonArray.getJSONObject(i).getString("shortname") + " - " + jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getString("name"),
                                        Metodos.getDateFromEpoch( jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("duedate")),
                                        Metodos.getDateFromEpoch( jsonArray.getJSONObject(i).getJSONArray("assignments").getJSONObject(a).getLong("cutoffdate")),
                                        "", 1);
                        this.itemMainNotices.add(itemMainNotices);
                        //}
                    }
                }
                //this.addAll(this.itemMainNotices);
                notifyDataSetChanged();
            }


        } catch (JSONException ex) {
            Log.e("JSONException", ex.toString() + "Error");
            Log.e("JSON response" ,response);

        } finally {
            if (servicioWeb == ServiciosWeb.NoticeMoodle) {
            }

        }
    }
*/


    @Override
    public void onTaskCompleted(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "calendario").equals("1")) {
                try {
                    getCalendarioEscolar(jsonObject.getJSONObject("calendario").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "moodle").equals("1")) {
                try {

                    getMoodle(jsonObject.getJSONArray("Moodle").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "horario").equals("1")) {
                try {

                    getHorario(jsonObject.getJSONArray("Horario1").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "horario").equals("1")) {
                try {
                    getHorario(jsonObject.getJSONArray("Horario2").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "horario").equals("1")) {
                try {
                    getHorario(jsonObject.getJSONArray("Horario3").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "horario").equals("1")) {
                try {
                    getHorario(jsonObject.getJSONArray("Horario4").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "horario").equals("1")) {
                try {
                    getHorario(jsonObject.getJSONArray("Horario5").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (new Metodos(getContext()).getSharedPreference(getContext().getString(R.string.preference_file_key),
                    "agenda").equals("1")) {
                try {
                    getAgendaITESO(jsonObject.getJSONObject("agenda").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /*try {
                getMagisItems(jsonObject.getJSONObject("Magis").toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            try {
                getCruceItems(jsonObject.getJSONObject("Cruce").toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            showTodayNotice();

        }
    }

    @Override
    public void onTaskCompleted(List<ItemMainNotice> items) {
        this.itemMainNotices.addAll(items);
        //showTodayNotice();
    }
}
