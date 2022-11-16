package com.example.todaydrink;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StateActivity extends AppCompatActivity implements View.OnClickListener {
    Button modifyBtn;
    Button state1Btn;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        modifyBtn = (Button)findViewById(R.id.state_modify);
        state1Btn = (Button)findViewById(R.id.state1);
        state1Btn.setOnClickListener(this);
        modifyBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == modifyBtn && check == 0) {
            modifyBtn.setText("수정 내용 저장하기");
            check = 1;
        }
        if (view == state1Btn && check == 1) {
            final EditText state = new EditText(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("상태 바꾸기");
            builder.setMessage("자신의 하는 행동을 적어주세요.");
            builder.setView(state);
            builder.setNegativeButton("취소", null);
            builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String modifyState = state.getText().toString();
                    state1Btn.setText("1단계 " + modifyState);
                }
            });
            builder.show();
        }
    }
}
