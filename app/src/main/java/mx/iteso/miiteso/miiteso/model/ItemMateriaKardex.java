package mx.iteso.miiteso.miiteso.model;

import java.io.Serializable;

public class ItemMateriaKardex implements Serializable{
    private String materia;
    private String calificacion;

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public ItemMateriaKardex(String materia, String calificacion) {

        this.materia = materia;
        this.calificacion = calificacion;
    }
}
