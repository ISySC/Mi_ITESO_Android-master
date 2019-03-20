package mx.iteso.miiteso.miiteso.model;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class ItemEmail {
    private String lbl_from;
    private String lbl_date;
    private String lbl_subject;
    private String lbl_word;

    public String getLbl_from() {
        return lbl_from;
    }

    public void setLbl_from(String lbl_from) {
        this.lbl_from = lbl_from;
    }

    public String getLbl_date() {
        return lbl_date;
    }

    public void setLbl_date(String lbl_date) {
        this.lbl_date = lbl_date;
    }

    public String getLbl_subject() {
        return lbl_subject;
    }

    public void setLbl_subject(String lbl_subject) {
        this.lbl_subject = lbl_subject;
    }

    public String getLbl_word() {
        return lbl_word;
    }

    public void setLbl_word(String lbl_word) {
        this.lbl_word = lbl_word;
    }

    public ItemEmail(String lbl_from, String lbl_date, String lbl_subject, String lbl_word) {

        this.lbl_from = lbl_from;
        this.lbl_date = lbl_date;
        this.lbl_subject = lbl_subject;
        this.lbl_word = lbl_word;
    }
}
