package it.jgrassi.roombooking.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.model.RoomsRequest;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomListViewModel extends Observable implements android.databinding.Observable{

    public ObservableInt roomsProgress;
    public ObservableInt roomsRecycler;
//    private ObservableField<LocalDate> day;
    private String searchKey;
    private boolean availableNextHour;
    private LocalDate day;

    private List<Room> roomList;
    private Context context;
    private CompositeDisposable compositeDisposable;
    private List<Room> unfilteredRooms;

    public RoomListViewModel(Context context){
        this.context = context;
        this.roomList = new ArrayList<>();
        roomsRecycler = new ObservableInt(View.GONE);
        roomsProgress = new ObservableInt(View.GONE);
//        day = new ObservableField<>();
        compositeDisposable = new CompositeDisposable();

        initializeViews();
        fetchRoomList();

    }

    private  void initializeViews(){
        roomsRecycler.set(View.GONE);
        roomsProgress.set(View.VISIBLE);
        day = new LocalDate();
        searchKey = "";
    }

    private void setRoomsData(List<Room> rooms){
        roomList.addAll(rooms);
        this.unfilteredRooms = rooms;
        setChanged();
        notifyObservers();
    }

    private void filterRooms() {
        List<Room> filteredRooms = new ArrayList<>();
        for(Room room : unfilteredRooms){
            if(room.name.contains(searchKey)) {
                if(availableNextHour) {
                    if (room.isAvailableNextHour())
                        filteredRooms.add(room);
                }else{
                    filteredRooms.add(room);
                }
            }
        }
        this.roomList = new ArrayList<>();
        roomList.addAll(filteredRooms);
        setChanged();
        notifyObservers();
    }

    public List<Room> getRoomList(){
        return roomList;
    }

    private void fetchRoomList() {

        RoomBookingApplication application = RoomBookingApplication.createApp(context);
        RoomService service = application.getRoomService();

        RoomsRequest requestBody = new RoomsRequest(RoomsRequest.NOW);

        Disposable disposable = service.fetchRooms(FactoryUtils.BASE_URL + RoomFactory.ROOM_URL, requestBody)
                .subscribeOn(application.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Room>>() {
                    @Override
                    public void accept(List<Room> rooms) throws Exception {
                        setRoomsData(rooms);
                        roomsProgress.set(View.GONE);
                        roomsRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, String.format(context.getResources().getString(R.string.loading_error), "Rooms"), Toast.LENGTH_LONG).show();
                        roomsProgress.set(View.GONE);
                        roomsRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }
    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void setSearchKey(String newKey){
        searchKey = newKey;
        filterRooms();
    }

    public String getSearchKey(){
        return searchKey;
    }

    public boolean isAvailableNextHour() {
        return availableNextHour;
    }

    public void setAvailableNextHour(boolean availableNextHour) {
        this.availableNextHour = availableNextHour;
        filterRooms();
    }

    @Bindable
    public String getDay(){
        return day.toString("dd/MM/yyyy");
    }

    public void previousDay() {
        day = day.plusDays(1);
        setChanged();
        notifyObservers();
    }

    public void nextDay() {
        day = day.minusDays(1);
        //notifyPropertyChanged(BR.day);

    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {

    }
}
