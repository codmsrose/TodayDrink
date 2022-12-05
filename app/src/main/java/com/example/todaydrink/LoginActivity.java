package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    EditText edit_Id, edit_Pwd;
    public String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_Id = findViewById(R.id.edit_Id);
        edit_Pwd = findViewById(R.id.edit_Pwd);

        Button btn_login = findViewById(R.id.btn_login);

        // 로그인 버튼 클릭 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력값 받아옴.
                String strId = edit_Id.getText().toString();
                String strPwd = edit_Pwd.getText().toString();

                // 입력한 아이디와 일치하는 데이터베이스에 접근해서 값을 읽음.
                databaseReference.child("Users").child(strId).child("프로필").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 입력한 아이디에 대한 정보가 데이터베이스에 들어있는지
                        if (snapshot.getValue(UserAccount.class) != null) {
                            // 데이터베이스에서 정보를 가져옴.
                            UserAccount userAccount = snapshot.getValue(UserAccount.class);

                            assert userAccount != null;
                            if (userAccount.pwd.equals(strPwd)) {
                                // 로그인 ID 저장해놓기.
                                currentUser = userAccount.id;

                                // 메인 화면으로 이동하면서 현재 로그인된 계정의 ID 정보를 넘김.
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("currentUser", currentUser);
                                startActivity(intent);
                            }
                            // 데이터베이스에 들어있는 pwd값과 다른 경우
                            else {
                                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // 입력한 아이디가 데이터베이스에 없는 경우
                        else {
                            Toast.makeText(getApplicationContext(), "아이디가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
            }
        });
    }
}
