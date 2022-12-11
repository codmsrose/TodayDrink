package com.example.todaydrink;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class StateActivity extends AppCompatActivity{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    private static final String TAG = "Jaewan";

    Button modifyBtn;

    char numberState;
    char saveStateNumber;
    String modifyState;
    String saveState;

    int check = 0;
    static int num = 1;

    String currentUser;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");

        modifyBtn = (Button)findViewById(R.id.state_modify);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check == 0) { // 수정 안하고 있는 상태
                    modifyBtn.setText("수정 내용 저장하기");
                    check = 1;
                }
                else if (check == 1){ //수정 하고 있는 상태
                    modifyBtn.setText("수정");
                    check = 0;
                }
            }
        });

        modifyState();

        Button[] buttonNum = new Button[5];
        for(int i=0;i<buttonNum.length;i++){
            String buttonId = "state"+(i+1);
            buttonNum[i] = findViewById(getResources().getIdentifier(buttonId, "id", getPackageName()));
        }

        reference.child("User").child(currentUser).child("상태").addValueEventListener(new ValueEventListener() {
            State state;
            int i = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    state = dataSnapshot.getValue(State.class);

                    if (i != 5) {
                        buttonNum[i].setText((i + 1) + "단계 " + state.getState());
                        i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    /* TODO 수정하기 전에 먼저 버튼에 디폴트값으로 설정되어있는 문자열을 데이터베이스에 저장해놔야함
       TODO 수정을 했다면 버튼 눌렀을 떄 onClick에서 다이얼로그 띄워서 받아온 문자열을 다시 해당 단계에 저장해야함
       TODO numberState에는 단계 숫자 "1", modifyState에는 행동만 "비틀거림", saveState는 "1단계 비틀거림" 이런식으로 저장되어있음.
     */

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

                                reference.child("User").child(currentUser).child("상태").child(String.valueOf(numberState)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue(State.class) != null) {
                                            State state1 = snapshot.getValue(State.class);
                                            state1.state = modifyState;

                                            reference.child("User").child(currentUser).child("상태").child(String.valueOf(numberState)).setValue(state1);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        builder.show();
                    }
                    else if(check == 0){
                        saveStateNumber = buttonId.getText().toString().charAt(0);
                        SelectedState selectedState = new SelectedState(String.valueOf(saveStateNumber));

                        reference.child("User").child(currentUser).child("날짜별 데이터").child(year + "년 " + (month + 1) + "월 " + day + "일").child("선택한 상태").setValue(selectedState);
                        // TODO saveStateNumber는 char형으로 나오는 내가 선택한 단계 숫자
                        // TODO 현재 내 상태를 눌렀을 때 나중에 해당 날짜에 지금 마신 술 양이랑 같이 이 숫자가 저장되어야함.
                    }
                }
            });
        }
    }
}
