package com.example.todaydrink;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> datas;

    public PostAdapter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position);
        holder.id.setText("작성자: " + data.getId());
        holder.title.setText(data.getTitle());
        holder.contents.setText(data.getContents());
        holder.time.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView title;
        private TextView contents;
        private TextView time;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.item_post_id);
            title = itemView.findViewById(R.id.item_post_title);
            contents = itemView.findViewById(R.id.item_post_contents);
            time = itemView.findViewById(R.id.item_post_time);
        }
    }
}
