package com.example.todaydrink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.CheckViewHolder>  implements OnCheckItemClickListener{



    private Context mContext;
    private ArrayList<CheckItems> items = new ArrayList<>();
    private int position;
    OnCheckItemClickListener listener;

    public CheckAdapter(Context mContext) {
        this.mContext = mContext;
        this.items = items;
    }

    public void setOnItemClicklistener(OnCheckItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(CheckViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }





    public class CheckViewHolder extends RecyclerView.ViewHolder{

        //xml의 위젯 사용하기 위해
        ImageView drink_image;
        TextView drink_name;


        public CheckViewHolder(@NonNull View itemView) {
            super(itemView);

            drink_image= itemView.findViewById(R.id.drink_image);
            drink_name= itemView.findViewById(R.id.drink_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(CheckViewHolder.this, v, position);
                    }
                }
            });


        }

        public void setItem(CheckItems item){
            drink_image.setImageResource(item.resId);
            drink_name.setText(item.name);
        }


    }


    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //viewHolder에 itemView를 넣어서 생성하고 생성된 ViewHolder가 리턴되도록 합니다.
        View itemView=inflater.inflate(R.layout.check_recycler , parent, false);
        return new CheckViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder holder, final int position) {

        CheckItems item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public CheckItems getItem(int position){
        return items.get(position);
    }

    public void addItem(CheckItems item){
        items.add(item);
    }

    public void itemPositionChange(int newView ,int lastView){
        Collections.swap(items, newView, lastView);
    }

}