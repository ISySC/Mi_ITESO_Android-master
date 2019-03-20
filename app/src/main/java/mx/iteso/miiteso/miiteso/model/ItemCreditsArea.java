package mx.iteso.miiteso.miiteso.model;

public class ItemCreditsArea {

    String superArea, asignatura, calificacion, creditos;

    public ItemCreditsArea(String superArea, String asignatura, String calificacion, String creditos) {
        this.superArea = superArea;
        this.asignatura = asignatura;
        this.calificacion = calificacion;
        this.creditos = creditos;
    }

    public ItemCreditsArea(String error) {
        this.superArea = "";
        this.asignatura = "";
        this.calificacion = "";
        this.creditos = "";
    }

    public String getSuperArea() {
        return superArea;
    }

    public void setSuperArea(String superArea) {
        this.superArea = superArea;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }
}
