package mx.iteso.miiteso.conectividadWS;

/**
 * Creado por:  Raúl Emmanuel Juárez Parra
 * Creado el:   29/11/2017
 * Descripción: Subject para declarar los nombres de los servicios web a consumir
 * Función:     Proporcionar los métodos de los servicios web para que este disponible en toda la aplicación
 */

public class MethodWS {
    //URIs base
    public static final String URL_BASE_TEST = "https://webtest.iteso.mx/";
    public static final String URL_BASE = "https://apps.iteso.mx/";
    public static final String URL_BASE_ITESUBES = "https://itesubes.ulab.mx/api/";
    public static final String URL_NOTIFICATIONS = "https://webtest.iteso.mx/Notificaciones-web/api/notificaciones/";

    public static final String URL_NOTICE_PRIVACY = "https://datospersonales.iteso.mx/";
    public static boolean ES_HORARIO = false;
    public static boolean ES_ESTADO = false;


    //Métodos POST
    public static final String WS_INICIAR_SESION = "ItesoMovil-web/WebServices/tokenWS/private/accesorest/";
    public static final String WS_EDO_CUENTA_RESUMEN = "ItesoMovil-web/WebServices/WebServiceREST/estadocuentaresumen/";
    public static final String WS_EDO_CUENTA = "ItesoMovil-web/WebServices/WebServiceREST/estadocuenta/";
    public static final String WS_HORARIO = "ItesoMovil-web/WebServices/WebServiceREST/horarioescolar/";
    public static final String WS_CREDITOS_AREA = "ItesoMovil-web/WebServices/WebServiceREST/avanceplea/";
    public static final String WS_MOODLE_COURSES= "ItesoMovil-web/WebServices/WebServiceREST/cursosMoodle/";
    public static final String WS_HISTORIAL_ACADEMICA= "ItesoMovil-web/WebServices/WebServiceREST/historiaacademica/";
    public static final String WS_RUTA_CURRICULAR ="ItesoMovil-web/WebServices/WebServiceREST/rutacurricular/";
    public static final String WS_GET_ROUTES= "rutas";
    public static final String WS_GET_VEHICLES= "vehiculos";
    public static final String WS_SUBSCRIBE= "Notificaciones-web/api/notificaciones/subscribe";
    public static final String WS_READED = "Notificaciones-web/api/notificaciones";
}
