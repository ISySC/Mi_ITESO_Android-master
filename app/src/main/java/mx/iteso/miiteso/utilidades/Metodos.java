package mx.iteso.miiteso.utilidades;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.iteso.miiteso.R;
import mx.iteso.miiteso.conectividadWS.MethodWS;
import mx.iteso.miiteso.miiteso.LoginActivity;
import mx.iteso.miiteso.miiteso.adapters.DaysAdapter;
import mx.iteso.miiteso.miiteso.classes.OnIntegerChangeListener;
import mx.iteso.miiteso.miiteso.model.ItemDay;

/**
 * Creado por:  Raúl Emmanuel Juárez Parra
 * Creado el:   29/11/2017
 * Descripción: Procesar información referente a la aplicación
 * Función:     Proporcionar y procesar información necesaria para la aplicación.
 */

public class Metodos {
    private static final int WIDTH_INDEX = 0, HEIGHT_INDEX = 0;
    private Context context;
    private SharedPreferences sessionActive;
    private SharedPreferences.Editor editor;
    public static SnapHelper snapHelper = new LinearSnapHelper();
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static int cont = 0;
    public static DaysAdapter daysAdapter = null;

    public enum TypeFont {
        MontserratRegular,
        MontserratLight,
        MontserratMedium,
        MontserratBold
    }

    public Metodos(Context context) {
        this.context = context;
    }

    public void showToast(String msg) {
        if (Constantes.DEBUG_MODE)
            Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show();
    }

    //establecer tipografia
    public Typeface typeface(TypeFont typeFont) {

        String font_path = "font/";

        switch (typeFont) {
            case MontserratRegular:
                font_path += "Montserrat-Regular.ttf";
                break;
            case MontserratLight:
                font_path += "Montserrat-Light.ttf";
                break;
            case MontserratMedium:
                font_path += "Montserrat-Medium.ttf";
                break;
            case MontserratBold:
                font_path += "Montserrat-Bold.ttf";
                break;
        }

        return Typeface.createFromAsset(context.getAssets(), font_path);
    }

    //Método para validar correo electrónico
    public boolean esCorreoValido(String correo) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = correo;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //Método para validar las cadenas de texto nulos
    public boolean isFieldEmpty(EditText... fields) {
        boolean isEmpty = false;

        for (EditText editText : fields) {
            if (editText.getText().toString().isEmpty()) {
                isEmpty = true;
                break;
            } else
                continue;
        }

        return isEmpty;
    }

    public void findViews(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    //you can recursively call this method
                    findViews(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratRegular));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Método encargado de enviar los mensajes a los usuarios en la pantalla solicitada
    public void MensajeAUsuario(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void NavegarAPantalla(Class<?> destino) {
        Intent intent = new Intent(context, destino).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public void NavegarAPantallaExtras(Class<?> destino, String... extras) {
        Intent intent = new Intent(context, destino).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int cont = 0;
        for (String string : extras) {
            cont++;
            intent.putExtra("extra " + String.valueOf(cont), string);
        }
        context.startActivity(intent);
    }

    public static String ConvertirStreamAString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public AlertDialog.Builder alertDialog(String setTitle, String setMessage) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(setTitle);
        alert.setMessage(setMessage);
        alert.setCancelable(false);

        return alert;
    }

    public AlertDialog.Builder alertDialogUI() {

        View view = View.inflate(context, R.layout.layout_info_itesubes, null);

        ((TextView) (view.findViewById(R.id.tv_detenido))).setTypeface(typeface(TypeFont.MontserratRegular));
        ((TextView) (view.findViewById(R.id.tv_parada))).setTypeface(typeface(TypeFont.MontserratRegular));
        ((TextView) (view.findViewById(R.id.tv_lento))).setTypeface(typeface(TypeFont.MontserratRegular));
        ((TextView) (view.findViewById(R.id.tv_movimiento))).setTypeface(typeface(TypeFont.MontserratRegular));

        return new AlertDialog.Builder(context).setView(view);
    }

    //Método para mostrar mensaje de cargando
    public ProgressDialog showProgressDialog(String message) {
        Constantes.progressDialog = new ProgressDialog(this.context);
        Constantes.progressDialog.setMessage(message);
        Constantes.progressDialog.setCanceledOnTouchOutside(false);

        Constantes.progressDialog.show();

        return Constantes.progressDialog;
    }

    public ProgressDialog newProgressDialog(String message) {
        ProgressDialog progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();
        return progressDialog;
    }

    public void showOvalNotification(ImageView imageView, int setVisibilityOval, TextView textView, String number) {
        imageView.setImageResource(R.drawable.ic_oval_notification);
        imageView.setVisibility(setVisibilityOval);

        textView.setVisibility(setVisibilityOval);
        textView.setText(number);
        textView.setTypeface(this.typeface(TypeFont.MontserratBold));
    }

    public void changeThinImageView(ImageView imageView, int red, int green, int blue) {
        imageView.setColorFilter(Color.argb(255, red, green, blue));
    }

    public void setSharedPreference(String keyPreference, String key, String value) {
        sessionActive = context.getSharedPreferences(keyPreference, Context.MODE_PRIVATE);

        editor = sessionActive.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedPreference(String keyPreference, String key) {
        sessionActive = context.getSharedPreferences(keyPreference, Context.MODE_PRIVATE);
        return (sessionActive.getString(key, "0"));
    }

    public void showWebView(String urlLoad, WebView webView, final ProgressDialog progressDialog) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl(urlLoad);

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void addFragment(Fragment fragment, AppCompatActivity activity) {

        this.fragment = fragment;
        fragmentManager = activity.getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_seccion, this.fragment);
        fragmentTransaction.commit();
    }


    public static int[] getScreenSize(Context context) {
        int[] widthHeight = new int[2];
        widthHeight[WIDTH_INDEX] = 0;
        widthHeight[HEIGHT_INDEX] = 0;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        widthHeight[WIDTH_INDEX] = size.x;
        widthHeight[HEIGHT_INDEX] = size.y;

        if (!isScreenSizeRetrieved(widthHeight)) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            widthHeight[0] = metrics.widthPixels;
            widthHeight[1] = metrics.heightPixels;
        }

        if (!isScreenSizeRetrieved(widthHeight)) {
            widthHeight[0] = display.getWidth(); // deprecated
            widthHeight[1] = display.getHeight(); // deprecated
        }

        return widthHeight;
    }

    private static boolean isScreenSizeRetrieved(int[] widthHeight) {
        return widthHeight[WIDTH_INDEX] != 0 && widthHeight[HEIGHT_INDEX] != 0;
    }

    public static Bitmap blur(AppCompatActivity activity) {
        Bitmap bitmap = takeScreenShot(activity);

        RenderScript renderScript = RenderScript.create(activity);

        // This will blur the bitmapOriginal with a radius of 16 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(renderScript, bitmap); // Use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setRadius(16f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();


        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public void changeTabsFont(TabLayout tabLayout) {
        ViewGroup childTabLayout = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < childTabLayout.getChildCount(); i++) {
            ViewGroup viewTab = (ViewGroup) childTabLayout.getChildAt(i);
            for (int j = 0; j < viewTab.getChildCount(); j++) {
                View tabTextView = viewTab.getChildAt(j);
                if (tabTextView instanceof TextView) {
                    ((TextView) tabTextView).setTypeface(new Metodos(context).typeface(Metodos.TypeFont.MontserratMedium));
                }
            }
        }
    }

    public String format(String value) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(Double.valueOf(value));
    }

    public void showCalendar(Context context, RecyclerView rvDays, TextView tvDate
            , final int weekOfTheYear) {

        final Date date = new Date();
        ArrayList<ItemDay> itemDays = new ArrayList<>();
        List<String> day_name = new ArrayList<>();
        List<String> complete_day_name = new ArrayList<>();
        List<String> day_number = new ArrayList<>();
        List<Date> current_date = new ArrayList<>();
        final Context conte = context;


        Calendar mCalendar = new GregorianCalendar();
        mCalendar.set(Calendar.YEAR, Constantes.calender.get(Calendar.YEAR)); // Set only year
        mCalendar.set(Calendar.MONTH, Calendar.DECEMBER); // Don't change
        mCalendar.set(Calendar.DAY_OF_MONTH, 31); // Don't change
        int totalWeeks = mCalendar.get(Calendar.WEEK_OF_YEAR);


        Calendar c = new GregorianCalendar(Locale.getDefault());

        for (int a = weekOfTheYear - 2; a <= weekOfTheYear + 2; a++) {

            c.set(Calendar.WEEK_OF_YEAR, a);
            c.set(Calendar.YEAR, Constantes.calender.get(Calendar.YEAR));


            int firstDayOfWeek = c.getFirstDayOfWeek();
            for (int i = firstDayOfWeek; i < firstDayOfWeek + 7; i++) {
                c.set(Calendar.DAY_OF_WEEK, i);
                Date tomorrow = c.getTime();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String tomorrowAsString = dateFormat.format(tomorrow);

                DateFormat dateFormatName = new SimpleDateFormat("EEE, MMM d, ''yy");
                String tomorrowNameAsString = dateFormatName.format(tomorrow);

                DateFormat dateFormatFullName = new SimpleDateFormat("EEEE");
                String tomorrowFullNameAsString = dateFormatFullName.format(tomorrow);

                Date result = null;
                try {
                    result = dateFormatName.parse(tomorrowNameAsString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //if (i == firstDayOfWeek)
                //    tvDate.setText(tomorrowNameAsString.substring(5, 9).toUpperCase() + " " + tomorrowAsString.substring(8, 10));
                //else if (i == firstDayOfWeek + 6)
                //    tvDate.setText(tvDate.getText() + " -" + tomorrowNameAsString.substring(5, 9).toUpperCase() + " " + tomorrowAsString.substring(8, 10));


                day_number.add(tomorrowAsString.substring(8, 10));
                day_name.add(tomorrowNameAsString.substring(0, 3).toUpperCase());
                complete_day_name.add(tomorrowFullNameAsString);
                current_date.add(result);
            }

        }
        for (int cont = 0; cont < day_number.size(); cont++)
            itemDays.add(new ItemDay(day_name.get(cont), complete_day_name.get(cont), day_number.get(cont), current_date.get(cont)));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(conte, LinearLayoutManager.HORIZONTAL, false);
        if (daysAdapter == null)
            daysAdapter = new DaysAdapter((Activity) conte, rvDays, 0, itemDays);


        rvDays.getRecycledViewPool().setMaxRecycledViews(0, 0);
        snapHelper.attachToRecyclerView(rvDays);
        rvDays.setLayoutManager(mLayoutManager);
        rvDays.setAdapter(daysAdapter);


        tvDate.setText(String.valueOf(android.text.format.DateFormat.format("dd/MMM/yyyy", Constantes.currentWorkingDate)).replace(".", "").toUpperCase());
        tvDate.setTypeface(new Metodos(conte).typeface(Metodos.TypeFont.MontserratRegular));

        //rvDays.smoothScrollToPosition(Constantes.selectedPosition.get());

        int targetPosition = Constantes.selectedPosition.get();
        int PreviousPosition = Constantes.selectedPosition.getPrevoius();
        int itemWhidth = getScreenWidth() / 5;

        int previousTotal = (PreviousPosition * itemWhidth) - itemWhidth * 2;
        int total = (targetPosition * itemWhidth) - itemWhidth * 2;
        int totalL = (targetPosition * itemWhidth) - itemWhidth - getScreenWidth() * 3;
        if (Constantes.selectedPosition.getPrevoius() > Constantes.selectedPosition.get()) {
            //rvDays.scrollToPosition(0);
            //rvDays.scrollTo(total/*-previousTotal*/, 0);
            rvDays.scrollToPosition(Constantes.selectedPosition.get() - 2);

        } else {
            //rvDays.scrollToPosition(14);
            //rvDays.smoothScrollBy(totalL/*-previousTotal*/, 0);
            rvDays.scrollToPosition(Constantes.selectedPosition.get() - 2);

        }

        Constantes.currentWorkingWeek = weekOfTheYear;


    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    public static String getDateFromEpoch(long epoch) {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(epoch));
    }

    public View checkInstance(View view) {
        for (View v : Constantes.fragmentsInstances) {
            if (v.getContext().getClass() == view.getContext().getClass()) {
                Toast.makeText(context, "Previa Instancia", Toast.LENGTH_SHORT).show();
                return v;
            }
        }
        return null;
    }


    public static void replaceFragmentWithAnimationRight(android.support.v4.app.Fragment fragment, FragmentTransaction transaction, int view, String tag) {
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(view, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static void replaceFragmentWithAnimationLeft(android.support.v4.app.Fragment fragment, FragmentTransaction transaction, int view, String tag) {
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(view, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static String getCurrenDateTimeFormat() {
        Date date = Calendar.getInstance().getTime();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(date);
    }

}
