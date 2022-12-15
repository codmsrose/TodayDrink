package com.example.todaydrink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<TimeItem> mData = null ;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);

        }

        TextView other_person_timer = itemView.findViewById(R.id.other_person_timer);
        TextView other_person_name= itemView.findViewById(R.id.other_person_name);

    }

    TimeAdapter(ArrayList<TimeItem> list){
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.time_recycler_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = mData.get(position).time ;
        String name = mData.get(position).name;
        holder. other_person_timer.setText(time) ;
        holder. other_person_name.setText(name);

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }



}