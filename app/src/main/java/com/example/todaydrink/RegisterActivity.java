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

    private int beforeLength, afterLength = 0;

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

        // 계좌 번호 입력 시 1111-111-... 으로 형식을 지정.
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                afterLength = s.length();

                if (beforeLength > afterLength) {
                    if (s.toString().endsWith("-")) {
                        etAccount.setText(s.toString().substring(0, s.length() - 1));
                    }
                }

                else if (beforeLength < afterLength) {
                    // 5번째 문자를 입력할 차례가 되면 "-" 을 자동으로 넣어줌.
                    if (afterLength == 5 && s.toString().indexOf("-") < 0) {
                        etAccount.setText(s.toString().subSequence(0, 4) + "-" + s.toString().substring(4, s.length()));
                    }
                    // 9번째 문자를 입력할 차례가 되면 "-" 을 자동으로 넣어줌.
                    else if (afterLength == 9) {
                        etAccount.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));
                    }
                }
                etAccount.setSelection(etAccount.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 회원가입 버튼 클릭 시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력 값을 모두 변환하여 데이터베이스에 저장.
                String strName = etName.getText().toString();
                String strId = etId.getText().toString();
                String strPwd = etPwd.getText().toString();
                int intGender = gender_num;
                String intWeight = etWeight.getText().toString();
                String strDrink = etDrink.getText().toString();
                String strAccount = etAccount.getText().toString();
                String strProfile = "gs://todaydrink-458d8.appspot.com/profile_icon.png";

                // 입력값을 모두 저장하기 위해 addUserAccount 메소드 호출
                addUserAccount(strName, strId, strPwd, intGender, intWeight, strDrink, strAccount, strProfile);

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