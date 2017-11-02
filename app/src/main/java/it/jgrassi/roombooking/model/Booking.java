package it.jgrassi.roombooking.model;

/**
 * Created by jacop on 01/11/2017.
 */

public class Booking {
    public String date;

    public String time_start;

    public String time_end;

    public String title;

    public String description;

    public String room;

    public Booking(long date, long time_start, long time_end, String title, String description, String room) {
        this.date = Long.toString(date);
        this.time_start = Long.toString(time_start);
        this.time_end = Long.toString(time_end);
        this.title = title;
        this.description = description;
        this.room = room;
    }
}
