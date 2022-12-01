package com.example.todaydrink;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView numberText;
    TextView weightText;
    Button numberBtn;
    Button weightBtn;
    String modifyNumberText;
    String modifyWeightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        numberText = (TextView)findViewById(R.id.myNumberText);
        weightText = (TextView)findViewById(R.id.myWeightText);
        numberBtn = (Button)findViewById(R.id.numberBtn);
        weightBtn = (Button)findViewById(R.id.weightBtn);

        numberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText numberEditText = new EditText(ProfileActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("내용 수정하기");
                builder.setView(numberEditText);
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        modifyNumberText = numberEditText.getText().toString();
                        numberText.setText(modifyNumberText);
                    }
                });
                builder.show();
            }
        });

        weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText weightEditText = new EditText(ProfileActivity.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("내용 수정하기");
                builder.setView(weightEditText);
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        modifyWeightText = weightEditText.getText().toString();
                        weightText.setText(modifyWeightText);
                    }
                });
                builder.show();
            }
        });

    }
    // TODO 회원가입할떄 저장했던 정보들을 프로필에다가 보여줘야함.
    // TODO 계좌번호나 몸무게는 수정하면 수정한 내용이 데이터베이스에 들어가게(수정한 내용이 들어간 변수->modifyWeightText, modifyNumberText)
    // TODO 회원가입시에 받아오는 성별을 데이터베이스에 회원정보에만 넣어놓고 프로필에는 안넣어도 될거같아요..
}