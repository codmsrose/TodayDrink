package com.example.todaydrink;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>  implements OnGroupItemClickListener{

    private Context mContext;
    private ArrayList<GroupItems> items = new ArrayList<>();
    private int position;
    OnGroupItemClickListener listener;

    GroupAdapter(Context mContext) {
        this.mContext = mContext;
        this.items = items;
    }

    public void setOnItemClickListener(OnGroupItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(GroupViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        Button group_button;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            group_button = itemView.findViewById(R.id.group_button);
        }

        public void setItem(GroupItems item) {
            group_button.setText("방" + item.groupNumber);
        }
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.group_recycler, parent, false);

        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {

        int groupNumber = AddGroupActivity.groupNumber;

        holder.group_button.setText("방" + groupNumber);

        holder.group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGroupActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public GroupItems getItem(int position){
        return items.get(position);
    }

    public void addItem(GroupItems item){
        items.add(item);
    }

    public void itemPositionChange(int newView ,int lastView){
        Collections.swap(items, newView, lastView);
    }

}
