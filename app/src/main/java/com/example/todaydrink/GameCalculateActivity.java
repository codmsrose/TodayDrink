package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameCalculateActivity extends AppCompatActivity {

    String currentUser;

    TextView number1;
    TextView number2;
    EditText answer;
    Button mark;

    boolean correct = false;
    int correctAnswer = 0;
    String userAnswer = "";

    Chronometer chronometer;
    boolean running;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_calculate);

        Intent get_Intent = getIntent();
        currentUser = get_Intent.getStringExtra("currentUser");

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
        running = true;

        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);
        answer = (EditText)findViewById(R.id.answer);
        mark = (Button)findViewById(R.id.mark);

        int n1 = (int)((Math.random()*(1000-100))+100);
        int n2 = (int)((Math.random()*(5-1))+5);
        number1.setText(String.valueOf(n1));
        number2.setText(String.valueOf(n2));

        correctAnswer = n1 * n2;

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswer = answer.getText().toString();
                if(userAnswer.equals(String.valueOf(correctAnswer))){
                    correct = true;
                }
                if (correct) {
                    if(running){
                        chronometer.stop();
                        time = chronometer.getText().toString();
                        running = false;
                    }
                    Intent intent = new Intent(GameCalculateActivity.this, GameResultActivity.class);
                    intent.putExtra("time_calculate", time);
                    intent.putExtra("currentUser", currentUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(GameCalculateActivity.this, "다시 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
