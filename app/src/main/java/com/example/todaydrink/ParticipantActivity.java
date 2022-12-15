package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParticipantActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    View plusDialog;
    Button okBtn;

    RecyclerView recyclerView;
    ParticipantAdapter ParticipantAdapter;

    String currentUser = "";
    int groupNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);
        okBtn = findViewById(R.id.Participant_host_save);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 0);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();
        /*
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }
         */

        reference.child("방").child("방" + groupNumber).child("참가자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember member = dataSnapshot.getValue(GroupMember.class);

                    list.add(member.name);
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.participant_recyclerView) ;
                recyclerView.setLayoutManager(new GridLayoutManager(ParticipantActivity.this,3)) ;

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                ParticipantAdapter adapter = new ParticipantAdapter(list) ;
                recyclerView.setAdapter(adapter) ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClick(View view){
        if(view == okBtn){
            Intent intent = new Intent(ParticipantActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }
}