package com.fitapp;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTurkey {

@SerializedName("utc_datetime")
@Expose
private String dateTime;

@SerializedName("day_of_week")
    @Expose
    private String dateDay;

    @SerializedName("week_number")
    @Expose
    private String dateWeek;
    @SerializedName("day_of_year")
    @Expose
    private String dateYear;

    @SerializedName("timezone")
    @Expose
    private String dateTimeZone;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateDay() {
        return dateDay;
    }

    public void setDateDay(String dateDay) {
        this.dateDay = dateDay;
    }

    public String getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek;
    }

    public String getDateYear() {
        return dateYear;
    }

    public void setDateYear(String dateYear) {
        this.dateYear = dateYear;
    }

    public String getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(String dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }
}
