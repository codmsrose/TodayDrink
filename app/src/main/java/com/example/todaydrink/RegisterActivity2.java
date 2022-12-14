package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity2 extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    EditText state1_amount, state2_amount, state3_amount, state4_amount, state5_amount;
    String amount1, amount2, amount3, amount4, amount5;
    int state1, state2, state3, state4, state5;
    Button register_btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Intent intent = getIntent();
        String strId = intent.getStringExtra("registerId");

        state1_amount = findViewById(R.id.state1_edit);
        state2_amount = findViewById(R.id.state2_edit);
        state3_amount = findViewById(R.id.state3_edit);
        state4_amount = findViewById(R.id.state4_edit);
        state5_amount = findViewById(R.id.state5_edit);

        register_btn = findViewById(R.id.register_btn);

        RadioGroup drink1_group = findViewById(R.id.drink1_group);
        drink1_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                amount1 = state1_amount.getText().toString();
                switch (checkedId) {
                    case R.id.bottle1_radio:
                        state1 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount1) * 360);
                        break;
                    case R.id.glass1_radio:
                        state1 = CheckCalculate.calculate(strId, 1, Integer.parseInt(amount1) * 50);
                        break;
                }
            }
        });

        RadioGroup drink2_group = findViewById(R.id.drink2_group);
        drink2_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                amount2 = state2_amount.getText().toString();
                RadioButton radioButton = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.bottle2_radio:
                        state2 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount2) * 360);
                        break;
                    case R.id.glass2_radio:
                        state2 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount2) * 50);
                        break;
                }
            }
        });

        RadioGroup drink3_group = findViewById(R.id.drink3_group);
        drink3_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                amount3 = state3_amount.getText().toString();
                RadioButton radioButton = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.bottle3_radio:
                        state3 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount3) * 360);
                        break;
                    case R.id.glass3_radio:
                        state3 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount3) * 50);
                        break;
                }
            }
        });

        RadioGroup drink4_group = findViewById(R.id.drink4_group);
        drink4_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                amount4 = state4_amount.getText().toString();
                RadioButton radioButton = group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.bottle4_radio:
                        state4 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount4) * 360);
                        break;
                    case R.id.glass4_radio:
                        state4 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount4) * 50);
                        break;
                }
            }
        });

        RadioGroup drink5_group = findViewById(R.id.drink5_group);
        drink5_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                amount5 = state5_amount.getText().toString();
                switch (checkedId) {
                    case R.id.bottle5_radio:
                        state5 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount5) * 360);
                        break;
                    case R.id.glass5_radio:
                        state5 = CheckCalculate.calculate(strId, 16.9, Integer.parseInt(amount5) * 50);
                        break;
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                State state1_ = new State(state1, "멀쩡해요");
                reference.child("User").child(strId).child("상태").child("1").setValue(state1_);
                State state2_ = new State(state2, "기분이 UP");
                reference.child("User").child(strId).child("상태").child("2").setValue(state2_);
                State state3_ = new State(state3, "물건을 떨어뜨림");
                reference.child("User").child(strId).child("상태").child("3").setValue(state3_);
                State state4_ = new State(state4, "비틀비틀");
                reference.child("User").child(strId).child("상태").child("4").setValue(state4_);
                State state5_ = new State(state5, "필름 끊김");
                reference.child("User").child(strId).child("상태").child("5").setValue(state5_);

                Intent intent1 = new Intent(RegisterActivity2.this, LoginActivity.class);
                startActivity(intent1);
            }
        });
    }
}