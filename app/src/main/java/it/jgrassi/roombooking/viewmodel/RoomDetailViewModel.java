package it.jgrassi.roombooking.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Observable;

import it.jgrassi.roombooking.R;
import it.jgrassi.roombooking.data.FactoryUtils;
import it.jgrassi.roombooking.model.Interval;
import it.jgrassi.roombooking.model.Room;
import it.jgrassi.roombooking.view.RoomListActivity;

/**
 * Created by jacop on 30/10/2017.
 */

public class RoomDetailViewModel extends Observable {

    public Room room;

    private Context context;
    private BarData barData;

    public RoomDetailViewModel(Context context, Room room){
        this.context = context;
        this.room = room;
    }

    @BindingAdapter({"photoUrlDetail"})
    public static void loadPhotoDetail(ImageView imageView, String url) {
        if(url != null) {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }

    public String getPhotoUrl1() {
        if(room.images[0] != null)
            return FactoryUtils.BASE_URL + room.images[0];
        return null;
    }

    public String getPhotoUrl2() {
        if(room.images[1] != null)
            return FactoryUtils.BASE_URL + room.images[1];
        return null;
    }

    public String getPhotoUrl3() {
        if(room.images[2] != null)
            return FactoryUtils.BASE_URL + room.images[2];
        return null;
    }

    public String getEquipment(){
        if(room.equipment == null || room.equipment.length == 0){
            return "No equipment available";
        }
        String ret = "Available equipment: ";
        for(int i = 0; i<room.equipment.length; i++){
            ret += room.equipment[i];
            if(i != room.equipment.length-1)
                ret += ", ";
        }
        return ret;
    }

    private void buildBookingLayout(Interval interval) {

    }

    @BindingAdapter({"availDataDetail"})
    public static void buildChart(HorizontalBarChart chart, final RoomDetailViewModel model) {
        //number of "pieces" in the chart = number of slots in a day
        chart.setMaxVisibleValueCount(48);

        Description description = new Description();
        description.setText("");
        chart.setDescription(description);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight selected) {
                 model.buildBookingLayout(model.room.availInterval.get(selected.getStackIndex()));
            }



            @Override
            public void onNothingSelected() {

            }
        });

        chart.animateY(0);

        chart.setDrawValueAboveBar(false);

        YAxis yLabels = chart.getAxisLeft();
        yLabels.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value == 0)
                    return "07:00";
                if(value <= 4)
                    return "08:00";
                if(value <= 8)
                    return "09:00";
                if(value <= 12)
                    return "10:00";
                if(value <= 16)
                    return "11:00";
                if(value <= 20)
                    return "12:00";
                if(value <= 24)
                    return "13:00";
                if(value <= 28)
                    return "14:00";
                if(value <= 32)
                    return "15:00";
                if(value <= 36)
                    return "16:00";
                if(value <= 40)
                    return "17:00";
                if(value <= 44)
                    return "18:00";
                if(value <= 48)
                    return "19:00";
                return "";
            }
        });
        yLabels.setDrawGridLines(false);
        yLabels.setDrawAxisLine(false);
        yLabels.setAxisMaximum(48);
        yLabels.setAxisMinimum(0);
        yLabels.setLabelCount(12, true);

        chart.getAxisRight().setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setEnabled(false);



        Legend l = chart.getLegend();
        l.setEnabled(false);


        chart.setData(model.barData);
    }

    public RoomDetailViewModel getAvailData() {
        room.computeIntervals();

        float[] intervals = new float[room.availInterval.size()];
        int[] colors = new int[room.availInterval.size()];



        for (int i = 0; i<room.availInterval.size();i++) {
            Interval interval = room.availInterval.get(i);
            int slots = interval.getSlots();

            intervals[i] = Float.valueOf(slots);
            if(interval.getType() == Interval.TYPE_FREE)
                colors[i] = context.getResources().getColor(R.color.free);
            else
                colors[i] = context.getResources().getColor(R.color.booked);

        }

        ArrayList<BarEntry> yVals = new ArrayList<>();
        yVals.add(new BarEntry(0.0F, intervals));
        BarDataSet yset = new BarDataSet(yVals, "");

        yset.setColors(colors);                     //adding colors after converting them to an array
        yset.setDrawValues(false);


        barData = new BarData( yset);
        return this;
    }

    public void reset() {
        context = null;
    }

}
