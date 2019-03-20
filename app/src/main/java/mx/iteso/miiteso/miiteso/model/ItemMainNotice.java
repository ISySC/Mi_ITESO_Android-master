package mx.iteso.miiteso.miiteso.model;

import android.graphics.Color;
import android.support.annotation.NonNull;

import mx.iteso.miiteso.R;

/**
 * Created by rjuarez on 04/03/2018.
 */

public class ItemMainNotice implements Comparable {
    private String url;
    private String title;
    private String starDate;
    private String endDate;
    private String startHour;

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    private String endHour;

    private int typeNotice;
    private int icon;
    private int color;
    private int pos;

    public String getUrl() {
        return url;
    }

    public int getColor() {
        return color;
    }

    public int getIcon() {
        return icon;
    }

    public int getTypeNotice() {
        return typeNotice;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStarDate() {
        return starDate;
    }

    public String getTitle() {
        return title;
    }

    //////============  Type Notice: 1.-Curso moodle 2.-Horario  3.-Calendario  4.-Agenda 5.-Magis 6.-Cruce
    public ItemMainNotice(String title, String startDate, String endDate, String url, int typeNotice, String starHour, String endHour) {
        this.title = title;
        this.starDate = startDate;
        this.endDate = endDate;
        this.url = url;
        this.typeNotice = typeNotice;
        this.startHour = starHour;
        this.endHour = endHour;

        switch (typeNotice) {
            case 1:
                this.icon = R.drawable.ic_pencil_simple_line_icons;
                this.color = Color.parseColor("#4388cc");
                this.pos = 2;
                break;
            case 2:
                this.icon = R.drawable.ic_bulb_simple_line_icons;
                this.color = Color.parseColor("#339999");
                this.pos = 3;
                break;
            case 3:
                this.icon = R.drawable.ic_calendar_simple_line_icons;
                this.color = Color.parseColor("#b50000");
                this.pos = 1;
                break;
            case 4:
                this.icon = R.drawable.ic_book_open_simple_line_icons;
                this.color =  Color.parseColor("#6600cc");
                this.pos = 4;
                break;
            case 5:
                this.icon = R.drawable.ic_book_open_simple_line_icons;
                this.color =  Color.parseColor("#6600cc");
                this.pos = 4;
                break;
            case 6:
                this.icon = R.drawable.ic_book_open_simple_line_icons;
                this.color =  Color.parseColor("#6600cc");
                this.pos = 4;
                break;
        }
    }

    public ItemMainNotice(String lbl_notice_description) {
        this.title = lbl_notice_description;
        this.starDate = "";
        this.endDate = "";
        this.url = "";
        this.typeNotice = -1;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int compareType = ((ItemMainNotice) o).getPos();
        /* For Ascending order*/
        return this.pos - compareType;
    }
}
