package it.jgrassi.roombooking.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacop on 29/10/2017.
 */

public class Room implements Serializable{

    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public String location;

    @SerializedName("equipment")
    public String[] equipment;

    @SerializedName("size")
    public String size;

    @SerializedName("capacity")
    public String capacity;

    @SerializedName("avail")
    public String[] avail;

    @SerializedName("images")
    public String[] images;

    public transient List<Interval> availInterval;

    public void computeIntervals(){
        availInterval = new ArrayList<>();

        for(int i=0; i<avail.length; i++){
            Interval interval = new Interval(avail[i], Interval.TYPE_FREE);

            if(i==0){
                //if it's the first interval
                if(!(interval.getStart().getHourOfDay() == Interval.INTIAL_HOUR && interval.getStart().getMinuteOfHour() == Interval.INTIAL_MINUTE)) {
                    //if the first interval is not at the start of the day, fill with a free interval
                    Interval toAdd = new Interval(Interval.TYPE_BOOKED, Interval.INTIAL_HOUR, Interval.INTIAL_MINUTE, interval.getStart().getHourOfDay(), interval.getStart().getMinuteOfHour());
                    availInterval.add(toAdd);
                }
            }else {
                //fill with free interval if the previous is not immediatly before this one
                Interval prev = new Interval(avail[i-1],Interval.TYPE_IGNORED);
                if(!prev.checkIfSubsequent(interval)) {
                    Interval toAdd = new Interval(Interval.TYPE_BOOKED, prev.getEnd().getHourOfDay(), prev.getEnd().getMinuteOfHour(), interval.getStart().getHourOfDay(), interval.getStart().getMinuteOfHour());
                    availInterval.add(toAdd);
                }
            }
            availInterval.add(interval);

            //if it's the last interval
            if(i==avail.length-1){
                //if it's not at the end of the day, fill the rest of the day with a free interval
                if(!(interval.getEnd().getHourOfDay() == Interval.FINAL_HOUR && interval.getEnd().getMinuteOfHour() == Interval.FINAL_MINUTE)) {
                    Interval toAdd = new Interval(Interval.TYPE_BOOKED, interval.getEnd().getHourOfDay(), interval.getEnd().getMinuteOfHour(), Interval.FINAL_HOUR, Interval.FINAL_MINUTE);
                    availInterval.add(toAdd);
                }
            }
        }
    }

    public boolean isAvailableNextHour() {
        LocalTime now = new LocalTime();
        Interval nextHour = new Interval(now, now.plusHours(1));

        if(availInterval == null)
            computeIntervals();

        for(Interval interval : availInterval){
            if(nextHour.overlap(interval))
                return false;
        }
        return true;
    }
}
