package com.example.todaydrink;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<LocalDate> dayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    String currentUser;
    int cass_bottle, terra_bottle, iseul_bottle, start_bottle;
    int cass_glass, terra_glass, iseul_glass, start_glass;
    AlcoholSum alcohol_sum;
    String alcohol;

    public CalendarAdapter(ArrayList<LocalDate> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        //날짜 변수에 담기
        LocalDate day = dayList.get(position);


        if(day == null){
            holder.dayText.setText("");
        }else{
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));
            //색바꾸기 holder.dayText.setBackgroundResource(R.drawable.circle_white);
            //현재 날짜 색상 칠하기
            //if(day.equals(CalendarUtil.selectedDate)){
            //    holder.parentView.setBackgroundColor(Color.LTGRAY);}
            /*TODO 여기 안에서 해당 날짜에 데이터베이스에 저장되어있는 혈중 농도 최고치를 이용해서
            TODO 달력에다가 투명도를 다르게 해서 얼만큼 마셨는지 보여주는 것을 나타내려고 합니다
            TODO 그래서 여다가 상태를 불러와야 합니다. (1~5)
             */
            int aYear = day.getYear();
            int aMonth = day.getMonthValue();
            int aDay = day.getDayOfMonth();

            // StatisticsActivity 에서 로그인되어 있는 ID 가져옴.
            // 이 부분은 왜인지 모르게 데이터가 안 가져와 지네요...
            /*
            reference.child("User").child(currentUser).child("날짜별 데이터").child(aYear + "년 " + aMonth + "월 " + aDay + "일").child("총 알콜 농도").child("alcohol").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue(AlcoholSum.class) != null) {
                        alcohol_sum = snapshot.getValue(AlcoholSum.class);
                        assert alcohol_sum != null;
                        alcohol = alcohol_sum.alcohol_sum;
                    }
                    else {
                        alcohol = "0";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
             */
        }

        //텍스트 색상 지정
        if( (position + 1) % 7 == 0){ //토요일 파랑
            holder.dayText.setTextColor(Color.BLUE);
        }else if( position == 0 || position % 7 == 0){ //일요일 빨강
            holder.dayText.setTextColor(Color.RED);
        }

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentUser = ((StatisticsActivity)StatisticsActivity.mContext).userId;
                
                int iYear = day.getYear();//년
                int iMonth = day.getMonthValue(); //월
                int iDay = day.getDayOfMonth(); //일

                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

                reference.child("User").child(currentUser).child("날짜별 데이터").child(yearMonDay).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 선택된 날짜 아래에 대한 데이터베이스가 존재하지 않을 경우 모두 0으로.
                        if (snapshot.child("맥주").child("카스").getValue(Drink.class) == null) {
                            cass_bottle = 0;
                            cass_glass = 0;
                            terra_bottle = 0;
                            terra_glass = 0;
                            iseul_bottle = 0;
                            iseul_glass = 0;
                            start_bottle = 0;
                            start_glass = 0;
                        }

                        // 데이터베이스가 존재한다면 모든 데이터를 가져와서 저장.
                        else {
                            Drink drink_cass = snapshot.child("맥주").child("카스").getValue(Drink.class);
                            Drink drink_terra = snapshot.child("맥주").child("테라").getValue(Drink.class);
                            Drink drink_iseul = snapshot.child("소주").child("참이슬").getValue(Drink.class);
                            Drink drink_start = snapshot.child("소주").child("처음처럼").getValue(Drink.class);

                            cass_bottle = drink_cass.bottle;
                            cass_glass = drink_cass.glass;
                            terra_bottle = drink_terra.bottle;
                            terra_glass = drink_terra.glass;
                            iseul_bottle = drink_iseul.bottle;
                            iseul_glass = drink_iseul.glass;
                            start_bottle = drink_start.bottle;
                            start_glass = drink_start.glass;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("마신 술");
                // TODO builder.setMessage(); 아니면 TextView 이용해서

                // 선택한 날의 주량을 다이얼로그에 보여줌.
                // 약간의 문제는 그 날짜를 처음 클릭하면 모두 0으로 뜨고 한 번 더 누르면 정상 작동.
                builder.setMessage("카스 : " + cass_bottle + "병 " + cass_glass + "잔\n" +
                        "테라 : " + terra_bottle + "병 " + terra_glass + "잔\n" +
                        "참이슬 : " + iseul_bottle + "병 " + iseul_glass + "잔\n" +
                        "처음처럼 : " + start_bottle + "병 " + start_glass + "잔");


                builder.setPositiveButton("확인", null);
                // TODO 여기 변수에서 년, 월, 일 뺴오고 이걸 이용해서 데이터베이스에 해당 날짜에 저장된 마신 술 모두 보여주기
                // TODO 이 함수 안이 원하는 날짜를 눌렀을때 할 행동을 적을 공간
                // TODO 아래 다이얼로그에다가 setMessage로 보여주면 될거같아요
                builder.show();

                Toast.makeText(holder.itemView.getContext(), yearMonDay, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView dayText;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayText);
            parentView = itemView.findViewById(R.id.parentView);
        }
    }
}