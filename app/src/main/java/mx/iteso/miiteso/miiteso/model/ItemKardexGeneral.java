package mx.iteso.miiteso.miiteso.model;

/**
 * Created by PC on 24/10/2018.
 */

public class ItemKardexGeneral {
    private int porcentajeAvance;
    private String nombreCiclo;

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getNombreCiclo() {
        return nombreCiclo;
    }

    public void setNombreCiclo(String nombreCiclo) {
        this.nombreCiclo = nombreCiclo;
    }

    public ItemKardexGeneral(int porcentajeAvance, String nombreCiclo) {

        this.porcentajeAvance = porcentajeAvance;
        this.nombreCiclo = nombreCiclo;
    }
}
