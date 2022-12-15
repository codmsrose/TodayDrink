package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParticipantActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    View plusDialog, hostChangeDialog;
    Button plusBtn, participant_host_change, participant_host_save;
    String[] memberName_list = new String[3];
    String[] memberId_list = new String[3];
    int i = 0;
    String original_LeaderName, original_LeaderID;
    String changed_LeaderName, changed_LeaderID;

    RecyclerView recyclerView;
    ParticipantAdapter ParticipantAdapter;

    String currentUser = "";
    int groupNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        groupNumber = intent.getIntExtra("groupNumber", 0);

        participant_host_change = findViewById(R.id.Participant_host_change);
        participant_host_save = findViewById(R.id.Participant_host_save);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();

        // 주선자 이름 리스트에 넣기.
        reference.child("방").child("방" + groupNumber).child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember member = dataSnapshot.getValue(GroupMember.class);

                    original_LeaderID = member.id;
                    original_LeaderName = member.name;

                    list.add(member.name);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 참가자 이름 리스트에 넣고 리사이클러뷰 적용.
        reference.child("방").child("방" + groupNumber).child("참가자").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember member = dataSnapshot.getValue(GroupMember.class);

                    list.add(member.name);
                    memberName_list = new String[i];
                    memberId_list = new String[i];
                    i++;
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.participant_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(ParticipantActivity.this));

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                ParticipantAdapter adapter = new ParticipantAdapter(list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child("방").child("방" + groupNumber).child("참가자").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GroupMember member = dataSnapshot.getValue(GroupMember.class);

                    memberName_list[i] = member.name;
                    memberId_list[i] = member.id;
                    i++;
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = findViewById(R.id.participant_recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(ParticipantActivity.this));

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                ParticipantAdapter adapter = new ParticipantAdapter(list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //plusBtn.setOnClickListener(this);
        participant_host_change.setOnClickListener(this);

    }

    public void onClick(View view) {
        if (view == plusBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("친구 찾기");

            plusDialog = (View) View.inflate(ParticipantActivity.this, R.layout.participant_dialog, null);
            builder.setView(plusDialog);
            builder.show();
        }

        if (view == participant_host_change) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("주선자 변경");

            builder.setSingleChoiceItems(memberName_list, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 선택한 사람의 이름과 아이디를 가져옴.
                    changed_LeaderName = memberName_list[which];
                    changed_LeaderID = memberId_list[which];
                }
            });

            builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reference.child("방").child("방" + groupNumber).child("주선자").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // 기존의 주선자에 해당하던 사람의 데이터를 날림.
                            reference.child("방").child("방" + groupNumber).child("주선자").child(original_LeaderID).removeValue();

                            // 주선자로 뽑힌 사람의 데이터를 참가자에서 날림.
                            reference.child("방").child("방" + groupNumber).child("참가자").child(changed_LeaderID).removeValue();

                            // 주선자 데이터에 새롭게 주선자로 뽑힌 사람의 데이터를 넣음.
                            GroupMember leader = new GroupMember(changed_LeaderID, changed_LeaderName);
                            reference.child("방").child("방" + groupNumber).child("주선자").child(changed_LeaderID).setValue(leader);

                            // 기존의 주선자에 해당하던 사람의 데이터를 참가자에 넣음.
                            GroupMember newMember = new GroupMember(original_LeaderID, original_LeaderName);
                            reference.child("방").child("방" + groupNumber).child("참가자").child(original_LeaderID).setValue(newMember);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            builder.setNegativeButton("취소", null);
            builder.show();
        }
    }
}