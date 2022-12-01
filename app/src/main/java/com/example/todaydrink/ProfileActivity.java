package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    Button numberBtn;
    Button weightBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        numberBtn = (Button)findViewById(R.id.numberBtn);
        weightBtn = (Button)findViewById(R.id.weightBtn);


    }
    // TODO 회원가입할떄 저장했던 정보들을 프로필에다가 보여줘야함.
}