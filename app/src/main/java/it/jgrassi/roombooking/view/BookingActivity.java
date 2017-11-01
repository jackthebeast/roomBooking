package it.jgrassi.roombooking.view;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.joda.time.LocalDate;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.databinding.BookingActivityBinding;
import it.jgrassi.roombooking.model.Interval;
import it.jgrassi.roombooking.viewmodel.BookingViewModel;


public class BookingActivity extends AppCompatActivity {

    private static final String PARAM_DATE = "PARAM_DATE";
    private static String PARAM_INTERVAL = "PARAM_INTERVAL";
    private static final String PARAM_ROOM = "PARAM_ROOM";
    private String roomName;
    private Interval interval;
    private BookingViewModel bookingViewModel;
    private BookingActivityBinding binding;
    private LocalDate date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_activity);

        getIntentExtra();
        initDataBinding();
        setTitle(roomName + "   " + date.toString("dd/MM/yyyy") + "  " + interval.getStart().toString("HH:mm") + "-" + interval.getEnd().toString("HH:mm"));
    }

    private void initDataBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.booking_activity);
        bookingViewModel = new BookingViewModel(this, interval, date, roomName);
        binding.setBookingViewModel(bookingViewModel);
    }

    private void getIntentExtra() {
        roomName = getIntent().getStringExtra(PARAM_ROOM);
        interval = (Interval) getIntent().getSerializableExtra(PARAM_INTERVAL);
        date = (LocalDate) getIntent().getSerializableExtra(PARAM_DATE);
    }

    public static void launchBooking(Context context, String name, Interval interval, LocalDate date) {
        Intent intent = new Intent(context, BookingActivity.class);
        intent.putExtra(PARAM_ROOM, name);
        intent.putExtra(PARAM_INTERVAL, interval);
        intent.putExtra(PARAM_DATE, date);

        context.startActivity(intent);
    }
}
