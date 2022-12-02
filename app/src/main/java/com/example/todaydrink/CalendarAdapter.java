package com.example.todaydrink;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<LocalDate> dayList;

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
            TODO 그래서 여기다가 혈중 농도 최고치 데이터를 불러와야할 것 같아요.
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

                int iYear = day.getYear();//년
                int iMonth = day.getMonthValue(); //월
                int iDay = day.getDayOfMonth(); //일

                String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";
                // TODO 여기 변수에서 년, 월, 일 뺴오고 이걸 이용해서 데이터베이스에 해당 날짜에 저장된 마신 술 모두 보여주기
                // TODO 이 함수 안이 원하는 날짜를 눌렀을때 할 행동을 적을 공간
                // TODO 아래 다이얼로그에다가 setMessage로 보여주면 될거같아요
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("마신 술");
                // TODO builder.setMessage(); 아니면 TextView 이용해서
                builder.setPositiveButton("확인", null);
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