package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;



public class GameNumberActivity extends AppCompatActivity {

    TextView numberCount;
    TextView finishText;
    Button[] numberBtn = new Button[25];

    int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_number);

        numberCount = findViewById(R.id.countNumber);
        finishText = findViewById(R.id.finishText);

        randomNumber();
    }

    public void clickBtn(View view) {
        Button btn = (Button) view;
        String s = btn.getText().toString();
        int n = Integer.parseInt(s);
        if (number == n) {
            btn.setBackgroundColor(getResources().getColor(R.color.green));
            number++;
            if (number > 25) {
                finishText.setText("축하합니다 끝났습니다.");
            } else {
                numberCount.setText(number + "");
            }
        }
    }

    public void randomNumber(){
        Random rnd = new Random();

        int[] arr = new int[25];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rnd.nextInt(25) + 1;
            for (int k = 0; k < i; k++) {
                if (arr[i] == arr[k]) {
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < numberBtn.length; i++) {
            numberBtn[i] = findViewById(R.id.btn01 + i);
            numberBtn[i].setText(arr[i] + "");
            numberBtn[i].setTextColor(getResources().getColor(R.color.black));
            numberBtn[i].setBackgroundColor(getResources().getColor(R.color.light_gray));
        }
    }
}