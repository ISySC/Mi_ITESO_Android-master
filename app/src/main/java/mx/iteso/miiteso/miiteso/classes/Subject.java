package mx.iteso.miiteso.miiteso.classes;

/**
 * Created by PC on 22/08/2018.
 */

public class Subject {
    private int  horaInicio, horaFin,claseID;
    private String grupo,salon,asignatura,profesor,nombreDia, horaInicioFin;
    private boolean visible = true;

    public String getHoraInicioFin() {
        return horaInicioFin;
    }

    public void setHoraInicioFin(String horaInicioFin) {
        this.horaInicioFin = horaInicioFin;
    }

    public Subject(int horaInicio, int horaFin, int claseID, String grupo, String salon, String asignatura, String nombreDia, String horaInicioFin)
    {
        this.horaInicio=horaInicio;
        this.horaFin=horaFin;
        this.claseID=claseID;
        this.grupo=grupo;
        this.salon=salon;
        this.asignatura=asignatura;
        this.nombreDia=nombreDia;
        this.horaInicioFin = horaInicioFin;

    }
    public void setNombreDia(String nombreDia) {
        this.nombreDia = nombreDia;
    }

    public String getNombreDia() {
        return nombreDia;
    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Subject()
    {
        this.asignatura ="vacion";
    }

    public void setClaseID(int claseID) {
        this.claseID = claseID;
    }

    public int getClaseID() {
        return claseID;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public String getAsignatura() {
        return asignatura;
    }


    public String getGrupo() {
        return grupo;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getSalon() {
        return salon;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }
}
