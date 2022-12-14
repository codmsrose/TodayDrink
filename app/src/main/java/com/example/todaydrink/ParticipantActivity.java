package com.example.todaydrink;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class ParticipantActivity extends AppCompatActivity implements View.OnClickListener{

    View plusDialog;
    Button plusBtn;

    RecyclerView recyclerView;
    ParticipantAdapter ParticipantAdapter;
    ArrayList<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        list = new ArrayList<>();
        list.add("아이템 1");
        list.add("아이템 2");
        list.add("아이템 3");
        list.add("아이템 4");
        list.add("아이템 5");
        list.add("아이템 6");




        plusBtn.setOnClickListener(this);

        recyclerView = findViewById(R.id.participant_recyclerView);

        //매니저
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클뷰에 어댑터
        ParticipantAdapter = new ParticipantAdapter(list);
        recyclerView.setAdapter(ParticipantAdapter);



    }

    public void onClick(View view){
        if(view == plusBtn){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("친구 찾기");

            plusDialog = (View) View.inflate(ParticipantActivity.this, R.layout.participant_dialog, null);
            builder.setView(plusDialog);
            builder.show();
        }
    }
}