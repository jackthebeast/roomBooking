package it.jgrassi.roombooking.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.databinding.ItemRoomBinding;
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.viewmodel.ItemRoomViewModel;

/**
 * Created by jacop on 29/10/2017.
 */

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomAdapterViewHolder>{


    private final LocalDate date;
    private List<Room> list;

    public RoomsAdapter(LocalDate date){
        this.list = new ArrayList<>();
        this.date = date;
    }


    public static class RoomAdapterViewHolder extends RecyclerView.ViewHolder{
        private final LocalDate date;
        ItemRoomBinding itemBinding;

        public RoomAdapterViewHolder(ItemRoomBinding itemBinding, LocalDate date){
            super(itemBinding.itemRoom);
            this.itemBinding = itemBinding;
            this.date = date;
        }

        void bindRoom(Room room){
            if(itemBinding.getRoomViewModel() == null){
                itemBinding.setRoomViewModel(new ItemRoomViewModel(itemView.getContext(), room , date));
            }else{
                itemBinding.getRoomViewModel().setRoom(room);
            }
        }
    }

    @Override
    public void onBindViewHolder(RoomAdapterViewHolder holder, int position){
        holder.bindRoom(list.get(position));
    }

    @Override
    public RoomAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRoomBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_room, parent, false);
        return new RoomAdapterViewHolder(itemBinding, date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setRoomList(List<Room> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
