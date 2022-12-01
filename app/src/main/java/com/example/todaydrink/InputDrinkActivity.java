package com.example.todaydrink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class InputDrinkActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    Button btn_cass, btn_terra, btn_iseul, btn_start, btn_check;
    int cass_bottle, terra_bottle, iseul_bottle, start_bottle = 0;
    int cass_glass, terra_glass, iseul_glass, start_glass = 0;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_drink);

        btn_cass = findViewById(R.id.btn_cass);
        btn_terra = findViewById(R.id.btn_terra);
        btn_iseul = findViewById(R.id.btn_iseul);
        btn_start = findViewById(R.id.btn_start);
        btn_check = findViewById(R.id.btn_check);

        btn_cass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cass_glass++;
                if (cass_glass % 5 == 0) {
                    cass_bottle++;
                    cass_glass = 0;
                }
                int cass_ml = (cass_bottle * 1000) + (cass_glass * 200);
                addDrink("맥주", "카스", cass_bottle, cass_glass, cass_ml);
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
                int terra_ml = (terra_bottle * 1000) + (terra_glass * 200);
                addDrink("맥주", "테라", terra_bottle, terra_glass, terra_ml);
            }
        });

        btn_iseul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iseul_glass++;
                if (iseul_glass % 5 == 0) {
                    iseul_bottle++;
                    iseul_glass = 0;
                }
                int iseul_ml = (iseul_bottle * 1000) + (iseul_glass * 200);
                addDrink("소주", "참이슬", iseul_bottle, iseul_glass, iseul_ml);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_glass++;
                if (start_glass % 5 == 0) {
                    start_bottle++;
                    start_glass = 0;
                }
                int start_ml = (start_bottle * 1000) + (start_glass * 200);
                addDrink("소주", "처음처럼", start_bottle, start_glass, start_ml);
            }
        });
    }

    public void addDrink(String drinkKind, String brand, int bottle, int glass, int ml) {
        Drink drink = new Drink(bottle, glass, ml);

        reference.child("Users").child("abc123").child(year + "" + (month + 1) + "" + day).child(drinkKind).child(brand).setValue(drink);
    }
}