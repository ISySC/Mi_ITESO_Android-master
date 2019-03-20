package mx.iteso.miiteso.miiteso.model;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rjuarez on 20/02/2018.
 */

public class ItemConfiguration {
    public int ic_option;
    public String lbl_option;

    public ItemConfiguration() {

    }

    public ItemConfiguration(int ic_option, String lbl_option) {
        this.ic_option = ic_option;
        this.lbl_option = lbl_option;
    }

    public int getIc_option() {
        return ic_option;
    }

    public void setIc_option(int ic_option) {
        this.ic_option = ic_option;
    }

    public String getLbl_option() {
        return lbl_option;
    }

    public void setLbl_option(String lbl_option) {
        this.lbl_option = lbl_option;
    }
}
