package com.example.hearthrate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearthrate.MyToast;
import com.example.hearthrate.R;

public class ListAge extends RecyclerView.Adapter<ListAge.ViewHolder> {

    private ArrayList<String> list_age = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_age, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_age.setText(list_age.get(position));
        holder.parent_item_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.makeText(mContext, String.valueOf("Lets put your finger"), MyToast.LONG,  MyToast.CONFUSING, false).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_age.size();
    }

    public ListAge(Context context, ArrayList<String> list){
        list_age = list;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_age;
        RelativeLayout parent_item_age;
        public ViewHolder(View itemView){
            super(itemView);
            item_age = itemView.findViewById(R.id.item_age);
            parent_item_age = itemView.findViewById(R.id.parent_item_age);
        }
    }
}
