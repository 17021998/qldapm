package com.example.measurehearthrate.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.measurehearthrate.Filter;
import com.example.measurehearthrate.Database.DatabaseHelper;
import com.example.measurehearthrate.Model.ItemHistory;
import com.example.measurehearthrate.MyToast;
import com.example.measurehearthrate.R;

public class ListHistories extends ArrayAdapter<ItemHistory> {
    Context context;
    int resource;
    List<ItemHistory> list;
    Filter filter;
    List<ItemHistory> listbyFilter;
    DatabaseHelper mData;
    Handler isRefreshChart;
    public ListHistories(Context context, int resource, List<ItemHistory> list, DatabaseHelper mData, Handler isRefreshChart){
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
        this.listbyFilter = new ArrayList<>();
        this.mData = mData;
        this.isRefreshChart = isRefreshChart;
    }

    public List<ItemHistory> getListbyFilter(Filter filter){
        this.filter = filter;
        List<ItemHistory> result = new ArrayList<>();
        for(ItemHistory i:this.list){
            if(i.isState(filter.getState()))
                result.add(i);
        }
        listbyFilter = result;
        return result;
    }

    private List<ItemHistory> getListbyFilter(){
        if(isEmpty(this.listbyFilter))
            return this.list;
        return this.listbyFilter;
    }
    public ArrayList<String> getStateLabel(){
        List<ItemHistory> list = this.getList();
        ArrayList<String> result = new ArrayList<>();
        Set<String> listLabel = new HashSet<String>();
        for(ItemHistory item:list){
            listLabel.add(item.getState());
        }
        result.add("All");
        for(String item:listLabel){
            result.add(item);
        }
        return result;
    };

    public List<ItemHistory> getList(){
        return this.list;
    }

    public void setList(List<ItemHistory> list){
        this.list = list;
    }

    public void addItemHistory(ItemHistory item){
        this.list.add(0, item);
    }

    public boolean isEmpty(){
        return this.list.isEmpty();
    }

    public boolean isEmpty(List<ItemHistory> list){
        return list.isEmpty();
    }

    public ItemHistory getMaximumHeartRate(){
        if(isEmpty(this.getListbyFilter())) return null;
        return Collections.max(this.getListbyFilter(), Comparator.comparing(ItemHistory::getResultByInt));
    }

    public ItemHistory getMinimumHeartRate(){
        if(isEmpty(this.getListbyFilter())) return null;
        return Collections.min(this.getListbyFilter(), Comparator.comparing(ItemHistory::getResultByInt));
    }

    public int getAvgHeartRate(){
        if(isEmpty(this.getListbyFilter())) return 0;
        return (int) this.getListbyFilter().stream().mapToInt(val -> val.getResultByInt()).average().orElse(0.0);
    }

    public float getAvgHeartRate(int idate){
        int result = 0;
        List<ItemHistory> ilist = new ArrayList<>();
        this.list.forEach(value->{
            int day = value.getDay();
            if(day != 0 && day - 8 == idate){
                ilist.add(value);
            }
        });
        result = (int) ilist.stream().mapToInt(value -> value.getResultByInt()).average().orElse(0.0);
        if(result > 0)
            return (float)((result-40)*1.0/10);
        return 0;
    }

    public void refresh(){
        List<ItemHistory> list = new ArrayList<>();
        this.list.forEach(item->{
            if(item.getRemove() == 1)
                list.add(item);
        });
        this.setList(list);
    }

    public void remove(int position){
        this.list.remove(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resource, null);
        TextView _date = view.findViewById(R.id.item_date);
        TextView _result = view.findViewById(R.id.item_result);
        ImageView _logo = view.findViewById(R.id.item_logo);
        ItemHistory itemHistory = list.get(position);
        _date.setText(itemHistory.getDate());
        _result.setText(itemHistory.getResult());
        _logo.setImageDrawable(context.getResources().getDrawable(itemHistory.getImage(), null));

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want delete?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int x = mData.detele(itemHistory);
                        if (x == 1) {
                            itemHistory.setRemove(0);
                            isRefreshChart.sendEmptyMessage(position);
                            MyToast.makeText(context, String.valueOf("Removed"), MyToast.LONG,  MyToast.ERROR, false).show();
                        }
                    }
                }).setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }

            ;
        });
        return view;
    }
}
