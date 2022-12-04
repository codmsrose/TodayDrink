package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class InputDrinkActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    Button btn_cass, btn_terra, btn_iseul, btn_start, btn_check;
    TextView text_cass, text_terra, text_iseul, text_start;
    int cass_bottle, terra_bottle, iseul_bottle, start_bottle = 0;
    int cass_glass, terra_glass, iseul_glass, start_glass = 0;
    int cass_ml, terra_ml, iseul_ml, start_ml = 0;
    double alcohol_sum = 0;

    // 데이터베이스에 오늘 날짜에 맞게 저장하기 위해
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_drink);

        // 로그인한 ID를 Intent로 가져옴.
        Intent get_Intent = getIntent();
        String currentUser = get_Intent.getStringExtra("currentUser");

        btn_cass = findViewById(R.id.btn_cass);
        btn_terra = findViewById(R.id.btn_terra);
        btn_iseul = findViewById(R.id.btn_iseul);
        btn_start = findViewById(R.id.btn_start);
        btn_check = findViewById(R.id.btn_check);
        text_cass = findViewById(R.id.text_cass);
        text_terra = findViewById(R.id.text_terra);
        text_iseul = findViewById(R.id.text_iseul);
        text_start = findViewById(R.id.text_start);

        // 이 화면에 처음 들어왔을 때는 현재 주량이 안보임.
        text_cass.setVisibility(View.INVISIBLE);
        text_terra.setVisibility(View.INVISIBLE);
        text_iseul.setVisibility(View.INVISIBLE);
        text_start.setVisibility(View.INVISIBLE);

        // 카스 버튼 클릭 시
        btn_cass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 잔 1잔 추가.
                cass_glass++;
                // 5잔이 된 경우 1병을 추가하고 잔을 초기화.
                if (cass_glass % 5 == 0) {
                    cass_bottle++;
                    cass_glass = 0;
                }
                cass_ml = (cass_bottle * 1000) + (cass_glass * 200);

                // 현재 마신 병 수와 잔 수를 보여줌.
                text_cass.setText(cass_bottle + "병\n" + cass_glass + "잔");
                text_cass.setVisibility(View.VISIBLE);
            }
        });

        btn_terra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terra_glass++;
                if (terra_glass % 5 == 0) {
                    terra_bottle++;
                    terra_glass = 0;
                }
                terra_ml = (terra_bottle * 1000) + (terra_glass * 200);

                text_terra.setText(terra_bottle + "병\n" + terra_glass + "잔");
                text_terra.setVisibility(View.VISIBLE);
            }
        });

        btn_iseul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iseul_glass++;
                if (iseul_glass % 7 == 0) {
                    iseul_bottle++;
                    iseul_glass = 0;
                }
                iseul_ml = (iseul_bottle * 350) + (iseul_glass * 50);

                text_iseul.setText(iseul_bottle + "병\n" + iseul_glass + "잔");
                text_iseul.setVisibility(View.VISIBLE);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_glass++;
                if (start_glass % 7 == 0) {
                    start_bottle++;
                    start_glass = 0;
                }
                start_ml = (start_bottle * 350) + (start_glass * 50);

                text_start.setText(start_bottle + "병\n" + start_glass + "잔");
                text_start.setVisibility(View.VISIBLE);
            }
        });

        // 확인 버튼 클릭 시
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 데이터베이스에 마신 주량 저장
                addDrink(currentUser, "맥주", "카스", cass_bottle, cass_glass, cass_ml);
                addDrink(currentUser, "맥주", "테라", terra_bottle, terra_glass, terra_ml);
                addDrink(currentUser, "소주", "참이슬", iseul_bottle, iseul_glass, iseul_ml);
                addDrink(currentUser, "소주", "처음처럼", start_bottle, start_glass, start_ml);

                // (각 술들을 마신 용량 X 도수) / (총 마신 용량) 이라는 간단한 식으로 총 알콜 농도 저장.
                // TODO 생각해 놓으신 식이 있으면 그 식 기준으로 바꾸시면 될 듯 합니다.
                alcohol_sum = ((cass_ml * 4) + (terra_ml * 4.6) + (iseul_ml * 16.5) + (start_ml * 16.5)) / (cass_ml + terra_ml + iseul_ml + start_ml);
                AlcoholSum alcoholSum = new AlcoholSum(String.valueOf(alcohol_sum));
                reference.child("Users").child(currentUser).child(year + "년 " + (month + 1) + "월 " + day + "일").child("총 알콜 농도").child("alcohol").setValue(alcoholSum);

                // 메인 화면으로 돌아감
                Intent intent = new Intent(InputDrinkActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void addDrink(String currentUser, String drinkKind, String brand, int bottle, int glass, int ml) {
        Drink drink = new Drink(bottle, glass, ml);

        reference.child("Users").child(currentUser).child(year + "년 " + (month + 1) + "월 " + day + "일").child(drinkKind).child(brand).setValue(drink);
    }
}