package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    Button btn_addgroup, btn_group1, btn_group2;
    ImageView imageView_profile;
    ImageButton ib_home, ib_measure, ib_statistics, ib_board;
    static int groupNumber = 0;
    public static int REQUEST_CODE = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent get_Intent = getIntent();
        String currentUser = get_Intent.getStringExtra("currentUser");

        btn_addgroup = findViewById(R.id.btn_addgroup);
        ib_home = (ImageButton) findViewById(R.id.ib_home);
        ib_measure = (ImageButton) findViewById(R.id.ib_measure);
        ib_statistics = (ImageButton) findViewById(R.id.ib_statistics);
        ib_board = (ImageButton) findViewById(R.id.ib_board);
        imageView_profile = findViewById(R.id.imageView_profile);
        btn_group1 = findViewById(R.id.btn_group1);
        btn_group2 = findViewById(R.id.btn_group2);

        btn_group1.setVisibility(View.GONE);
        btn_group2.setVisibility(View.GONE);

        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
                intent.putExtra("currentUser", currentUser);
                intent.putExtra("groupNumber", groupNumber);
                REQUEST_CODE++;
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ib_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputDrinkActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        ib_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ib_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
            }
        });


        ib_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        imageView_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });

        btn_group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("currentUser", currentUser);
                groupNumber = 1;
                intent.putExtra("groupNumber", groupNumber);
                startActivity(intent);
            }
        });

        btn_group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("currentUser", currentUser);
                groupNumber = 2;
                intent.putExtra("groupNumber", groupNumber);
                startActivity(intent);
            }
        });

        reference.child("방").child("방1").child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(GroupMember.class) != null) {
                        btn_group1.setVisibility(View.VISIBLE);
                        groupNumber = 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("방").child("방2").child("주선자").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue(GroupMember.class) != null) {
                        btn_group2.setVisibility(View.VISIBLE);
                        groupNumber = 2;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}