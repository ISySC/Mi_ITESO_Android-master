package mx.iteso.miiteso.miiteso.model;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class ItemCoordination {
    private String lbl_message;
    private String lbl_date;

    public String getLbl_message() {
        return lbl_message;
    }

    public void setLbl_notification(String lbl_message) {
        this.lbl_message = lbl_message;
    }

    public String getLbl_date() {
        return lbl_date;
    }

    public void setLbl_date(String lbl_date) {
        this.lbl_date = lbl_date;
    }

    public ItemCoordination(String lbl_message, String lbl_date) {
        this.lbl_message = lbl_message;
        this.lbl_date = lbl_date;
    }
}
