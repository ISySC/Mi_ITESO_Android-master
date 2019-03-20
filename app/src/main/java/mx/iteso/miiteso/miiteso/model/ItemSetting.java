package mx.iteso.miiteso.miiteso.model;

/**
 * Created by rjuarez on 02/03/2018.
 */

public class ItemSetting {
    private  String nameSetting;
    private String lbl_setting;
    private boolean sw_off_on;
    private boolean sw_disable;

    public String getLbl_setting() {
        return lbl_setting;
    }

    public String getNameSetting() {
        return nameSetting;
    }

    public void setNameSetting(String nameSetting) {
        this.nameSetting = nameSetting;
    }

    public void setSw_disable(boolean sw_disable) {
        this.sw_disable = sw_disable;
    }

    public void setLbl_setting(String lbl_setting) {
        this.lbl_setting = lbl_setting;
    }

    public boolean isSw_off_on() {
        return sw_off_on;
    }

    public void setSw_off_on(boolean sw_off_on) {
        this.sw_off_on = sw_off_on;
    }

    public boolean isSw_disable() {
        return sw_disable;
    }

    public ItemSetting(String lbl_setting, boolean sw_off_on, boolean sw_disable, String nameSetting) {

        this.lbl_setting = lbl_setting;
        this.sw_off_on = sw_off_on;
        this.sw_disable = sw_disable;
        this.nameSetting = nameSetting;

    }

    public ItemSetting(String lbl_setting) {

        this.lbl_setting = lbl_setting;
    }
}
