package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameCalculateActivity extends AppCompatActivity {

    TextView number1;
    TextView number2;
    EditText answer;
    Button mark;
    boolean correct = false;
    int correctAnswer = 0;
    String userAnswer = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_calculate);

        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);
        answer = (EditText)findViewById(R.id.answer);
        mark = (Button)findViewById(R.id.mark);

        int n1 = (int)((Math.random()*(1000-100))+100);
        int n2 = (int)(Math.random()*10);
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
                    Intent intent = new Intent(GameCalculateActivity.this, GameResultActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(GameCalculateActivity.this, "다시 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
