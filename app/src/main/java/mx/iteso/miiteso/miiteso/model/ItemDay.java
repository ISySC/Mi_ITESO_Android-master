package mx.iteso.miiteso.miiteso.model;

import java.util.Date;

/**
 * Created by PC on 22/08/2018.
 */

public class ItemDay {
    private Date currentDate;
    private String dayName;
    private String completeDayName;
    private String dayNumber;
    private String tag = "white";

    public ItemDay(String dayName, String completeDayName, String dayNumber, Date currentDate)
    {
        this.dayName=dayName;
        this.completeDayName = completeDayName;
        this.dayNumber = dayNumber;
        this.currentDate = currentDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public String getDayName() {
        return dayName;
    }
    public String getDcompleteDAyName() {
        return completeDayName;
    }


    public void setCompleteDayName(String completeDayName) {
        this.completeDayName = completeDayName;
    }
    public void setDayName(String dayName) {
        this.dayName = dayName;
    }


    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
