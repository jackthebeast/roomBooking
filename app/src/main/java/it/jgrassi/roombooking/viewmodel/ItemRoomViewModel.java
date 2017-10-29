package it.jgrassi.roombooking.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import it.jgrassi.roombooking.data.FactoryUtils;
import it.jgrassi.roombooking.data.RoomFactory;
import it.jgrassi.roombooking.model.Room;

/**
 * Created by jacop on 29/10/2017.
 */

public class ItemRoomViewModel extends BaseObservable{

    private Room room;
    private Context context;

    public ItemRoomViewModel(Context context, Room room){
        this.context = context;
        this.room = room;
    }

    public void setRoom(Room room){
        this.room = room;
        notifyChange();
    }

    public void onItemClick(View view){
        //TODO
    }

    public String getName(){
        return room.name;
    }

    public String getLocation(){
        return room.location;
    }

    public String getSize(){
        return room.size;
    }

    public String getCapacity(){
        return Integer.toString(room.capacity);
    }

    @BindingAdapter({"photoUrl"})
    public static void loadPhoto(ImageView imageView, String url) {
        if(url != null)
            Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public String getPhotoUrl() {
        if(room.images[0] != null)
            return FactoryUtils.BASE_URL + room.images[0];
        return null;
    }

}
