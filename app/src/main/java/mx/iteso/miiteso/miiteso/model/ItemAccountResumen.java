package mx.iteso.miiteso.miiteso.model;

import mx.iteso.miiteso.conectividadWS.ServiciosWeb;

/**
 * Created by rjuarez on 04/03/2018.
 */

public class ItemAccountResumen {
    private String date;
    private String amount;
    private String decription;
    private String amountMX;
    private String exchangeType;
    private String badge;
    private String id;
    private String periodoEscolar;
    private String academicLevel;
    private ServiciosWeb serviciosWeb;

    public ItemAccountResumen(ServiciosWeb serviciosWeb, String date, String amount, String decription, String amountMX, String exchangeType
    , String badge, String id) {
        this.serviciosWeb = serviciosWeb;
        this.date = date;
        this.amount = amount;
        this.decription = decription;
        this.amountMX=amountMX;
        this.exchangeType=exchangeType;
        this.badge=badge;
        this.id=id;
    }

    public ItemAccountResumen(ServiciosWeb serviciosWeb, String date, String amount, String decription, String amountMX, String exchangeType
            ,String badge, String id, String periodoEscolar,String academicLevel) {
        this.serviciosWeb = serviciosWeb;
        this.date = date;
        this.amount = amount;
        this.decription = decription;
        this.amountMX=amountMX;
        this.exchangeType=exchangeType;
        this.badge=badge;
        this.id=id;
        this.periodoEscolar = periodoEscolar;
        this.academicLevel = academicLevel;
    }

    public ItemAccountResumen(ServiciosWeb serviciosWeb, String date, String amount, String decription, String amountMX, String exchangeType
            ,String badge, String id, String periodoEscolar) {
        this.serviciosWeb = serviciosWeb;
        this.date = date;
        this.amount = amount;
        this.decription = decription;
        this.amountMX=amountMX;
        this.exchangeType=exchangeType;
        this.badge=badge;
        this.id=id;
        this.periodoEscolar = periodoEscolar;
    }


    public ServiciosWeb getServiciosWeb() {
        return serviciosWeb;
    }

    public String getAcademicLevel() {
        return academicLevel;
    }

    public String getAmountMX() {
        return amountMX;
    }

    public String getBadge() {
        return badge;
    }

    public String getPeriodoEscolar() {
        return periodoEscolar;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public String getId() {
        return id;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
