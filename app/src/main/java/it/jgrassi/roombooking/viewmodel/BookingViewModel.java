package it.jgrassi.roombooking.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import it.jgrassi.roombooking.BR;
import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.RoomBookingApplication;
import it.jgrassi.roombooking.data.FactoryUtils;
import it.jgrassi.roombooking.data.RoomFactory;
import it.jgrassi.roombooking.data.RoomService;
import it.jgrassi.roombooking.model.BookRequest;
import it.jgrassi.roombooking.model.Booking;
import it.jgrassi.roombooking.model.BookingResult;
import it.jgrassi.roombooking.model.Interval;
import it.jgrassi.roombooking.model.Person;

/**
 * Created by jacop on 01/11/2017.
 */

public class BookingViewModel extends BaseObservable {
    private final Context context;
    private final Interval interval;
    private final LocalDate date;
    private final String roomName;

    private int fromHour;
    private int fromMinute;
    private int toHour;
    private int toMinute;

    public String title;
    public String description;

    private String name;
    private String number;
    private String email;
    private List<Person> invitationList;

    public ObservableInt error;
    public ObservableInt success;
    public ObservableInt cover;

    LocalTime from;
    LocalTime to;
    private CompositeDisposable compositeDisposable;


    public BookingViewModel(Context context, Interval interval, LocalDate date, String roomName) {
        this.context = context;
        this.interval = interval;
        from = interval.getStart();
        to = interval.getEnd();
        fromHour = from.getHourOfDay()-interval.getStart().getHourOfDay();
        fromMinute = from.getMinuteOfHour()/15;
        toHour = to.getHourOfDay()-interval.getStart().getHourOfDay();
        toMinute = to.getMinuteOfHour()/15;
        invitationList = new ArrayList<>();
        this.date = date;
        this.roomName = roomName;
        compositeDisposable = new CompositeDisposable();
        error = new ObservableInt();
        error.set(View.GONE);
        success = new ObservableInt();
        success.set(View.GONE);
        cover = new ObservableInt();
        cover.set(View.GONE);
    }

    public int getMaxValueHour() {
        int value = interval.getEnd().getHourOfDay() - interval.getStart().getHourOfDay();
        return value;
    }

    @Bindable
    public boolean getBookingEnabled(){
        boolean isFromInInterval = from.isEqual(interval.getStart()) || (from.isAfter(interval.getStart()) && from.isBefore(interval.getEnd()));
        boolean isToInInterval = to.isEqual(interval.getEnd()) || to.isAfter(interval.getStart()) && to.isBefore(interval.getEnd());
        boolean isFromBeforeTo = from.isBefore(to);

        return isFromBeforeTo && isFromInInterval && isToInInterval;
    }

    @Bindable
    public boolean getAddEnabled(){
        boolean isNameValid = (name != null && !name.equals(""));
        boolean isEmailValid = (email != null && !email.equals("")) && email.matches("^[a-z,A-Z0-9._%+-]+@[a-z,A-Z0-9.-]+\\.[a-z,A-Z]{2,}$");
        boolean isNumberValid = (number != null && !number.equals("")) && number.matches("^\\+?[0-9, ]{3,16}$");

        return isNameValid && isEmailValid && isNumberValid;
        //return true;
    }


    @Bindable
    public String getFrom(){
        return "From " + from.toString("HH:mm");
    }

    @Bindable
    public String getTo(){
        return "To " + to.toString("HH:mm");
    }

    private void updateIntervals() {
       from = interval.getStart().plusHours(fromHour).minuteOfHour().setCopy(fromMinute*15);
       to = interval.getStart().plusHours(toHour).minuteOfHour().setCopy(toMinute*15);
    }

    public int getFromMinute(){
        return fromMinute;
    }

    public void setFromMinute(int fromMinute){
        this.fromMinute = fromMinute;
        updateIntervals();
        notifyPropertyChanged(BR.from);
        notifyPropertyChanged(BR.bookingEnabled);
        return;
    }

    @Bindable
    public String getInvitationsText(){
        String text = "";
        for(Person p : invitationList){
            text += p.toString();
        }
        return text;
    }

    public void onAddInvite(View view) {
        invitationList.add(new Person(name, number, email));
        notifyPropertyChanged(BR.invitationsText);
        name = number = email = "";
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.number);
    }

    public void onBook(View view) {
        RoomBookingApplication application = RoomBookingApplication.createApp(context);
        RoomService service = application.getRoomService();

        Booking booking = new Booking(date.toDateTime(new LocalTime(0,0)).getMillis(), from.toDateTimeToday().getMillis(), to.toDateTimeToday().getMillis(),title, description, roomName);

        BookRequest requestBody = new BookRequest(booking, invitationList.toArray(new Person[invitationList.size()]));

        Disposable disposable = service.book(FactoryUtils.BASE_URL + RoomFactory.BOOK_URL, requestBody)
                .subscribeOn(application.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookingResult>() {
                    @Override
                    public void accept(BookingResult result)  {
                        cover.set(View.VISIBLE);
                        if(result.result)
                            success.set(View.VISIBLE);
                        else
                            error.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                        Toast.makeText(context, String.format(context.getResources().getString(R.string.loading_error), "Rooms"), Toast.LENGTH_LONG).show();
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.addEnabled);
    }

    @Bindable
    public String getNumber() {
        return number;
    }

    @Bindable
    public void setNumber(String number) {
        this.number = number;
        notifyPropertyChanged(BR.addEnabled);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.addEnabled);
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
        updateIntervals();
        notifyPropertyChanged(BR.from);
        notifyPropertyChanged(BR.bookingEnabled);
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
        updateIntervals();
        notifyPropertyChanged(BR.to);
        notifyPropertyChanged(BR.bookingEnabled);
    }

    public int getToMinute() {
        return toMinute;
    }

    public void setToMinute(int toMinute) {
        this.toMinute = toMinute;
        updateIntervals();
        notifyPropertyChanged(BR.to);
        notifyPropertyChanged(BR.bookingEnabled);
    }
}
