package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    Button dutchPayBtn;
    Button participantBtn;
    Button timeBtn;

    String leader;
    String currentUser;
    int groupNumber;
    int memberNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 0);
        // 참가자 명 수
        memberNumber = intent.getIntExtra("memberNumber", 0);
        dutchPayBtn = (Button)findViewById(R.id.menu_pay);
        participantBtn = (Button)findViewById(R.id.menu_participant);
        timeBtn = (Button)findViewById(R.id.menu_time);
        dutchPayBtn.setOnClickListener(this);
        participantBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);

        // TODO 더치페이 누르면 주선자일때 바로 사진 찍는걸로 안나와서 위치 바꿈.
        reference.child("방").child("방" + groupNumber).child("주선자").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(GroupMember.class) != null) {
                        GroupMember groupMember = dataSnapshot.getValue(GroupMember.class);

                        leader = groupMember.id;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onClick(View view){
        if(view == dutchPayBtn){
            if (currentUser.equals(leader)) {
                Intent intent = new Intent(this, DutchPayPictureActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("groupNumber", groupNumber);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, DutchPayAmountActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("groupNumber", groupNumber);
                startActivity(intent);
            }
        }
        else if(view == timeBtn){
            Intent intent = new Intent(this, TimeActivity.class);
            intent.putExtra("currentUser", currentUser);
            intent.putExtra("groupNumber", groupNumber);
            startActivity(intent);
        }
    }
}