package it.jgrassi.roombooking.model;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.io.Serializable;

/**
 * Created by JGrassi on 12/10/2015.
 **/

/**
 * this class represents an interval of time
 **/
public class Interval implements Serializable{

    public static final String INTERVAL_SEPARATOR = " - ";

    public static final int INTIAL_HOUR = 7;
    public static final int INTIAL_MINUTE = 0;
    public  static final int FINAL_HOUR = 19;
    public static final int FINAL_MINUTE = 0;

    public static final int TYPE_FREE = 0;
    public static final int TYPE_BOOKED = 1;
    public static final int TYPE_IGNORED = -1;

    int type;
    private LocalTime start;
    private LocalTime end;

    public Interval(int type, int startHour, int startMinute, int endHour, int endMinute) {
        this.type = type;
        this.start = new LocalTime(startHour, startMinute);
        this.end = new LocalTime(endHour, endMinute);
    }

    public Interval(String interval, int type){
        this(interval.split(INTERVAL_SEPARATOR)[0], interval.split(INTERVAL_SEPARATOR)[1], type);
    }

    public Interval(String start, String end, int type) {
        setStart(LocalTime.parse(start));
        setEnd(LocalTime.parse(end));
        this.type = type;
    }

    public Interval(LocalTime start, LocalTime end) {
        setStart(start);
        setEnd(end);
        this.type = TYPE_IGNORED;
    }

    public Interval(LocalTime start, LocalTime end, int type) {
        setStart(start);
        setEnd(end);
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interval interval = (Interval) o;

        if (type != interval.type) return false;
        if (!start.equals(interval.getStart())) return false;
        return end.equals(interval.getEnd());

    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + type;
        return result;
    }

    //check if this interval is immediatly before another one
    public boolean checkIfSubsequent(Interval prev) {
        if((end.getHourOfDay() == prev.getStart().getHourOfDay() &&
                end.getMinuteOfHour() == prev.getStart().getMinuteOfHour()) ||
                (start.getHourOfDay() == prev.getEnd().getHourOfDay() &&
                        start.getMinuteOfHour() == prev.getEnd().getMinuteOfHour()))
            return true;
        return false;
    }

    public int getSlots(){
        Minutes m = Minutes.minutesBetween(start, end);
        int minutes = m.getMinutes();
        int ret = minutes/15;
        return ret;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean overlap(Interval interval) {
        if(start.isBefore(interval.getStart()))
            if(end.isAfter(interval.getStart()))
                return true;
        if(start.isAfter(interval.getStart()))
            if(start.isBefore(interval.getEnd()))
                return true;
        return false;
    }

}
