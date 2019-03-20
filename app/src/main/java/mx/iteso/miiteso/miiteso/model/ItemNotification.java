package mx.iteso.miiteso.miiteso.model;

import java.io.Serializable;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class ItemNotification implements Serializable {
    private String evento;
    private String fechaEnvio;
    private String id;
    private String mensaje;
    private String remitente;
    private String tipo;
    private String fechaLectura;

    public ItemNotification(String evento, String fechaEnvio, String fechaLectura, String id, String mensaje, String remitente, String tipo) {
        this.evento = evento;
        this.fechaEnvio = fechaEnvio;
        this.id = id;
        this.mensaje = mensaje;
        this.remitente = remitente;
        this.tipo = tipo;
        this.fechaLectura = fechaLectura;
    }

    public String getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura() {
        this.fechaLectura = fechaLectura;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }
}
