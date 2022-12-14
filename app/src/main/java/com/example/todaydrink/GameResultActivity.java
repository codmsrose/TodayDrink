package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameResultActivity extends AppCompatActivity {

    String currentUser;

    String getTime_calculate = null;
    String getTime_number = null;
    int min, sec;

    TextView textView;
    ImageView stateImage;
    TextView drinkLevel;
    Button goSelectState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        Intent get_Intent = getIntent();
        currentUser = get_Intent.getStringExtra("currentUser");

        textView = (TextView)findViewById(R.id.text);
        stateImage = (ImageView)findViewById(R.id.stateImage);
        drinkLevel = (TextView)findViewById(R.id.drinkLevel);
        goSelectState = (Button)findViewById(R.id.goSelectState);

        Intent intent = getIntent();
        getTime_calculate = intent.getStringExtra("time_calculate");
        getTime_number = intent.getStringExtra("time_number");
        if(getTime_calculate != null) {
            splitTime(getTime_calculate);
        }
        else if(getTime_number != null) {
            splitTime(getTime_number);
        }
        gameResult();

        goSelectState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameResultActivity.this, StateActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            }
        });
    }

    public void splitTime(String time){
        min = Integer.parseInt(time.split(":")[0]);
        sec = Integer.parseInt(time.split(":")[1]);
    }

    public void gameResult(){
        if(min>0){
            textView.setText(min+"??? "+sec+"??? ???????????????");
            stateImage.setImageResource(R.drawable.state5);
            drinkLevel.setText("?????? ?????? ???????????????");
        }
        else{
            if(sec>50){
                textView.setText(sec+"??? ???????????????");
                stateImage.setImageResource(R.drawable.state4);
                drinkLevel.setText("????????? ?????? ???????????????");
            }
            else if(sec>40){
                textView.setText(sec+"??? ???????????????");
                stateImage.setImageResource(R.drawable.state3);
                drinkLevel.setText("????????? ???????????????");
            }
            else if(sec>30){
                textView.setText(sec+"??? ???????????????");
                stateImage.setImageResource(R.drawable.state2);
                drinkLevel.setText("????????? ???????????? ????????????");
            }
            else{
                textView.setText(sec+"??? ???????????????");
                stateImage.setImageResource(R.drawable.state1);
                drinkLevel.setText("?????? ???????????????");
            }
        }
        Toast.makeText(GameResultActivity.this, "????????? ???????????? ?????????!", Toast.LENGTH_SHORT).show();
    }
}