package mx.iteso.miiteso.miiteso.model;

public class ItemRoutes {
    private String nombreRuta;
    private String idRuta;
    private Double Lat;
    private Double Lon;

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public String getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(String idRuta) {
        this.idRuta = idRuta;
    }

    public ItemRoutes(String nombreRuta, String idRuta, Double Lat, Double Lon) {
        this.nombreRuta = nombreRuta;
        this.idRuta = idRuta;
        this.Lon = Lon;
        this.Lat = Lat;
    }
}
