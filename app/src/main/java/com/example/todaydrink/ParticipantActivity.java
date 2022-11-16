package com.example.todaydrink;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ParticipantActivity extends AppCompatActivity implements View.OnClickListener{

    View plusDialog;
    Button plusBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);

        plusBtn = findViewById(R.id.Participant_plus);
        plusBtn.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view == plusBtn){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("친구 찾기");

            plusDialog = (View) View.inflate(ParticipantActivity.this, R.layout.participant_dialog, null);
            builder.setView(plusDialog);
            builder.show();
        }
    }
}