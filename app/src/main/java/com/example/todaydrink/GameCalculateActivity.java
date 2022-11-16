package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameCalculateActivity extends AppCompatActivity {

    TextView number1;
    TextView number2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_calculate);

        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);

        int n1 = (int)((Math.random()*(1000-100))+100);
        int n2 = (int)(Math.random()*10+5);
        number1.setText(String.valueOf(n1));
        number2.setText(String.valueOf(n2));

        int answer = n1 * n2;
    }
}