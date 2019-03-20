package mx.iteso.miiteso.utilidades;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.View;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.iteso.miiteso.miiteso.adapters.MainNoticeAdapter;
import mx.iteso.miiteso.miiteso.classes.ObservableInteger;
import mx.iteso.miiteso.miiteso.model.ItemNotification;
import mx.iteso.miiteso.miiteso.model.ItemRoutes;

/**
 * Creado por:  Raúl Emmanuel Juárez Parra
 * Creado el:   29/11/2017
 * Descripción: Subject para declarar las constantes para la aplicacion
 * Función:     Proporcionar la información solicitada desde cualquier activity
 */

public class Constantes {

    public static final String MSG_EMPTY_FIELD_ERROR = "Para iniciar sesión, es necesario ingresar cuenta y contraseña";
    public static final String MSG_WEB_SERVICES_ERROR = "Error al procesar la información. Favor de verificar tu conexión a Internet.";
    public static String url_main_notice = "";
    public static ArrayList<View> fragmentsInstances = new ArrayList<View>();
    public static String MSG_PROGRESO = "";

    public static boolean DEBUG_MODE = true;

    public static int CODIGO_RESPUESTA = 0;
    public static String TOKEN = "token";
    public static List<String> KEY_NAME;
    public static List<String> KEY_NAME_ALT;
    public static ObservableInteger selectedPosition = new ObservableInteger(-1);
    public static ProgressDialog progressDialog;
    public static Date today;

    public static Date currentWorkingDate = new Date();
    public static int currentWorkingWeek = 0;
    public static String currentWorkingDay = "";
    public static final Calendar calender = GregorianCalendar.getInstance();


    public static Date firstShowingDay;
    public static Date lastShowingDay;

    public static String routeID = "";
    public static String currentDate = "";
    public static MainNoticeAdapter mainNoticeAdapter = null;

    public static boolean isChangedSettings = true;
    public static String route = "";

    public static Double LatBusStop;
    public static Double LonBusStop;

    public static ArrayList<ItemNotification> itemNotification;
    public static ArrayList<ItemNotification> itemNotificationGrl;

    public static ArrayList<ItemRoutes> itemRoutes = new ArrayList<>();
    public static boolean UseapiKey = false;
    public static boolean esNotificacion = true;
}
