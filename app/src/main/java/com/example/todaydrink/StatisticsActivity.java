package com.example.todaydrink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;
import java.util.ArrayList;
import org.threeten.bp.LocalDate;

public class StatisticsActivity extends AppCompatActivity{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public String userId;

    TextView monthYearText; //년월 텍스트뷰

    RecyclerView recyclerView;

    static TextView textMyDrink;
    static double soju = 16.5 * 0.05;
    static double beer = 4.5 *0.5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        AndroidThreeTen.init(this);

        // 로그인된 ID를 가져옴.
        Intent get_Intent = getIntent();
        userId = get_Intent.getStringExtra("currentUser");

        // userId를 사용하기 위해 이 액티비티를 CalendarAdapter에 전달.
        mContext = this;

        //초기화
        monthYearText = findViewById(R.id.monthYearText);
        ImageButton preBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.check_recyclerView);
        textMyDrink = findViewById(R.id.text_myDrink);

        //현재 날짜
        CalendarUtil.selectedDate = LocalDate.now();

        //화면 설정
        setMonthView();

        //이전 달 버튼 이벤트
        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //-1한 월을 넣어준다. (2월 -> 1월)
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        //다음 달 버튼 이벤트
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //+1한 월을 넣어준다.(2월 -> 3월)
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        reference.child("User").child(userId).child("상태").addValueEventListener(new ValueEventListener() {
            int i = 0;
            String[] myDrink = new String[5];
            double[] myDrinkDensity = new double[5];

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    State state = dataSnapshot.getValue(State.class);

                    if (i != 5) {
                        myDrink[i] = (i + 1) + "단계 " + state.state + " : " + state.density;
                        myDrinkDensity[i] = state.density;
                        i++;
                    }
                }

                textMyDrink.setText(myDrink[0] + "\n" + myDrink[1] + "\n" + myDrink[2] + "\n" + myDrink[3] + "\n" + myDrink[4]);
                CheckCalculate.setStateArray(myDrinkDensity);
                update(myDrinkDensity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }//onCreate

    //날짜 타입 설정(4월 2020)
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM");
        return date.format(formatter);
    }

    //화면 설정
    private void setMonthView(){

        //년월 텍스트뷰 셋팅
        monthYearText.setText(monthYearFromDate(CalendarUtil.selectedDate));

        //해당 월 날짜 가져오기
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);

        //어뎁터 데이터 적용
        CalendarAdapter adapter = new CalendarAdapter(dayList);

        //레이아웃 설정(열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

        //레이아웃 적용
        recyclerView.setLayoutManager(manager);

        //어뎁터 적용
        recyclerView.setAdapter(adapter);
    }

    //날짜 생성
    private ArrayList<LocalDate> daysInMonthArray(LocalDate date){

        ArrayList<LocalDate> dayList = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);

        //해당 월 마지막 날짜 가져오기(예 28, 30, 31)
        int lastDay = yearMonth.lengthOfMonth();

        //해당 월의 첫 번째 날 가져오기(예 4월1일)
        LocalDate firstDay = CalendarUtil.selectedDate.withDayOfMonth(1);

        //첫 번째 날 요일 가져오기(월:1 , 일:7)
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        //날짜 생성
        for(int i = 1; i < 42; i++){

            if( i <= dayOfWeek || i > lastDay + dayOfWeek){

                dayList.add(null);
            }else{
                dayList.add(LocalDate.of(CalendarUtil.selectedDate.getYear(), CalendarUtil.selectedDate.getMonth(),
                        i - dayOfWeek));
            }
        }

        return dayList;
    }
    public static void update(double[] state){

        String show =" ";
        String temp="";


        for(int i=0; i<5; i++)
        {
            if(state[i]/soju>0){
                temp=String.format("%10.1f",state[i]/soju)+"병" ;

            }
            show+= +(i+1)+"단계"+" 소주 :"+temp+"\n";
        }

        textMyDrink.setText(show);


    }






}