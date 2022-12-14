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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);


        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }



        plusBtn.setOnClickListener(this);


        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.participant_recyclerView) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        ParticipantAdapter adapter = new ParticipantAdapter(list) ;
        recyclerView.setAdapter(adapter) ;




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