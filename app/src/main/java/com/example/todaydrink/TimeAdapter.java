package com.example.todaydrink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class TimeAdapter   extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView other_person_timer = itemView.findViewById(R.id.other_person_timer);

        ViewHolder(View itemView) {
            super(itemView);

        }
    }

    TimeAdapter(ArrayList<String> list){
        mData = list;
    }

    @NonNull
    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.time_recycler_item, parent, false) ;
        TimeAdapter.ViewHolder vh = new TimeAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder. other_person_timer.setText(text) ;

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void addItemTime(String time){
        mData.add(time);
    }


}

