package it.jgrassi.roombooking;

import android.app.Application;
import android.content.Context;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by jacop on 19/10/2017.
 */

public class RoomBookingApplication extends Application{

    private Scheduler scheduler;

    public static RoomBookingApplication getApp(Context context){
        return (RoomBookingApplication) context.getApplicationContext();
    }


    public static RoomBookingApplication createApp(Context context) {
        return RoomBookingApplication.getApp(context);
    }
    public Scheduler subscribeScheduler(){
        if(scheduler == null)
            scheduler = Schedulers.io();
        return scheduler;
    }



}
