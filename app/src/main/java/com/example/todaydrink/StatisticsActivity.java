package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    LoginActivity loginActivity = new LoginActivity();
    String currentUser = loginActivity.currentUser;
    public CalendarView calendarView;
    public TextView text_my_drink, text_drink_record, textView2, textView3;

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    int bottle = 0;
    int glass = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        text_my_drink = findViewById(R.id.text_my_drink);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        text_drink_record = findViewById(R.id.text_drink_record);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                text_my_drink.setVisibility(View.VISIBLE);
                text_drink_record.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                text_my_drink.setText("나의 주량");

                takeDrink("맥주", "카스");
                takeDrink("맥주", "테라");

                int beer_bottle = bottle;
                int beer_glass = glass;
                bottle = 0;
                glass = 0;

                takeDrink("소주", "참이슬");
                takeDrink("소주", "처음처럼");

                int soju_bottle = bottle;
                int soju_glass = glass;
                bottle = 0;
                glass = 0;



                text_drink_record.setText("\n" + "맥주  " + beer_bottle + "병 " + beer_glass + "잔" +
                        "\n" + "소주  " + soju_bottle + "병 " + soju_glass + "잔");
            }
        });
    }

    public void takeDrink(String drinkKind, String brand) {
        reference.child("Users").child(currentUser).child(year + "/" + month + "/" + day).child(drinkKind).child(brand).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Drink drink = snapshot.getValue(Drink.class);

                bottle += drink.getBottle();
                glass += drink.getGlass();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}