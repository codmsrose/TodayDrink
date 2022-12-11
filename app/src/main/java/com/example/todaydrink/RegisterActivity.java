package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    EditText etName, etId, etPwd, etWeight, etDrink, etAccount;
    Button btnOverlap, btnRegister;
    RadioButton man_radio, woman_radio;
    int gender_num;

    String strName;
    String strId;
    String strPwd;
    int intGender;
    String strWeight;
    String strDrink;
    String strAccount;
    String strProfile = "gs://todaydrink-458d8.appspot.com/profile_icon.png";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.name_edit);
        etId = findViewById(R.id.id_edit);
        etPwd = findViewById(R.id.pwd_edit);
        etWeight = findViewById(R.id.weight_edit);
        etDrink = findViewById(R.id.drink_edit);
        etAccount = findViewById(R.id.account_edit);
        btnOverlap = findViewById(R.id.overlap_btn);
        btnRegister = findViewById(R.id.register_btn);
        man_radio = findViewById(R.id.man_radio);
        woman_radio = findViewById(R.id.woman_radio);

        btnRegister.setEnabled(false);

        // 중복 확인 버튼 클릭 시
        btnOverlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId = etId.getText().toString();

                databaseReference.child("User").child(strId).child("프로필").child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);

                        // 데이터베이스 안에 사용자가 입력한 id가 존재한다면 토스트를 띄우고 가입 버튼 비활성화
                        if(value!=null){
                            Toast.makeText(getApplicationContext(),"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();//토스메세지 출력
                            btnRegister.setEnabled(false);
                        }
                        // id가 없다면 가입 버튼 활성화
                        else{
                            Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                            btnRegister.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("RegisterActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });
            }
        });

        // 성별을 받기 위한 버튼
        RadioGroup gender_radio = findViewById(R.id.gender_group);
        gender_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton genderButton = group.findViewById(checkedId);
                switch (checkedId) {
                    // 남성을 눌렀다면 0으로 저장.
                    case R.id.man_radio:
                        gender_num = 0;
                        break;
                    // 여성을 눌렀다면 1로 저장.
                    case R.id.woman_radio:
                        gender_num = 1;
                        break;
                }
            }
        });

        // 회원가입 버튼 클릭 시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력 값을 모두 변환하여 데이터베이스에 저장.
                strName = etName.getText().toString();
                strId = etId.getText().toString();
                strPwd = etPwd.getText().toString();
                intGender = gender_num;
                strWeight = etWeight.getText().toString();
                strWeight = etWeight.getText().toString();
                strAccount = etAccount.getText().toString();
                strProfile = "gs://todaydrink-458d8.appspot.com/profile_icon.png";

                State state1 = new State("1", "멀쩡해요");
                databaseReference.child("User").child(strId).child("상태").child("1").setValue(state1);
                State state2 = new State("2", "기분이 UP");
                databaseReference.child("User").child(strId).child("상태").child("2").setValue(state2);
                State state3 = new State("3", "물건을 떨어뜨림");
                databaseReference.child("User").child(strId).child("상태").child("3").setValue(state3);
                State state4 = new State("4", "비틀비틀");
                databaseReference.child("User").child(strId).child("상태").child("4").setValue(state4);
                State state5 = new State("5", "필름 끊김");
                databaseReference.child("User").child(strId).child("상태").child("5").setValue(state5);



                addUserAccount(strName, strId, strPwd, intGender, strWeight, strDrink, strAccount, strProfile);

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // 데이터베이스에 정보 저장
    public void addUserAccount(String name, String id, String pwd, int gender, String weight, String drink, String account, String profile) {
        UserAccount userAccount = new UserAccount(name, id, pwd, gender, weight, drink, account, profile);

        databaseReference.child("User").child(id).child("프로필").setValue(userAccount);
    }
}