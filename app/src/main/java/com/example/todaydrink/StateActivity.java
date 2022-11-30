package com.example.todaydrink;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StateActivity extends AppCompatActivity{

    Button modifyBtn;

    char numberState;
    String modifyState;
    String saveState;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        modifyBtn = (Button)findViewById(R.id.state_modify);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 0) {
                    modifyBtn.setText("수정 내용 저장하기");
                    check = 1;
                }
                else if (check == 1){
                    modifyBtn.setText("수정");
                    check = 0;
                }
            }
        });

        modifyState();
    }

    public void modifyState(){
        Button[] buttonNum = new Button[5];
        for(int i=0;i<buttonNum.length;i++){
            String buttonId = "state"+(i+1);
            buttonNum[i] = findViewById(getResources().getIdentifier(buttonId, "id", getPackageName()));
        }
        for(Button buttonId : buttonNum) {
            buttonId.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (check == 1) {
                        final EditText state = new EditText(StateActivity.this);

                        AlertDialog.Builder builder = new AlertDialog.Builder(StateActivity.this);
                        builder.setTitle("상태 바꾸기");
                        builder.setMessage("자신의 하는 행동을 적어주세요.");
                        builder.setView(state);
                        builder.setNegativeButton("취소", null);
                        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                numberState = buttonId.getText().toString().charAt(0);
                                modifyState = state.getText().toString();
                                saveState = numberState+"단계 " + modifyState;
                                buttonId.setText(saveState);
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }
}
