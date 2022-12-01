package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    EditText etId, etPwd, etWeight, etDrink, etAccount;
    Button btnOverlap, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etId = findViewById(R.id.id_edit);
        etPwd = findViewById(R.id.pwd_edit);
        etWeight = findViewById(R.id.weight_edit);
        etDrink = findViewById(R.id.drink_edit);
        etAccount = findViewById(R.id.account_edit);
        btnOverlap = findViewById(R.id.overlap_btn);
        btnRegister = findViewById(R.id.register_btn);

        btnRegister.setEnabled(false);

        // 중복 확인 버튼 클릭 시
        btnOverlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId = etId.getText().toString();

                databaseReference.child("Users").child(strId).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
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

        // 회원가입 버튼 클릭 시
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력 값을 모두 String 타입으로 변환
                String strId = etId.getText().toString();
                String strPwd = etPwd.getText().toString();
                String strWeight = etWeight.getText().toString();
                String strDrink = etDrink.getText().toString();
                String strAccount = etAccount.getText().toString();
                String strProfile = "gs://todaydrink-458d8.appspot.com/profile_icon.png";

                // 입력값을 모두 저장하기 위해 addUserAccount 메소드 호출
                addUserAccount(strId, strPwd, strWeight, strDrink, strAccount, strProfile);

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // 데이터베이스에 정보 저장
    public void addUserAccount(String id, String pwd, String weight, String drink, String account, String profile) {
        UserAccount userAccount = new UserAccount(id, pwd, weight, drink, account, profile);

        databaseReference.child("Users").child(id).setValue(userAccount);
    }
}