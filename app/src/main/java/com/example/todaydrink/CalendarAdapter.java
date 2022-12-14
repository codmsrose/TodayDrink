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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<LocalDate> dayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    String currentUser;
    int cass_bottle, terra_bottle, heineken_bottle, iseul_bottle, start_bottle, jinro_bottle;
    int cass_glass, terra_glass, heineken_glass, iseul_glass, start_glass, jinro_glass;
    String selectedState;
    int intState;
    TextView text_myDrink;
    String message = "";

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

        currentUser = ((StatisticsActivity)StatisticsActivity.mContext).userId;

        text_myDrink = ((StatisticsActivity)StatisticsActivity.mContext).findViewById(R.id.text_myDrink);

        reference.child("User").child(currentUser).child("상태").addValueEventListener(new ValueEventListener() {
            int i = 0;
            String[] myDrink = new String[5];

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    State state = dataSnapshot.getValue(State.class);

                    if (i != 5) {
                        myDrink[i] = (i + 1) + "단계 " + state.state + " : " + state.density;
                        i++;
                    }
                }

                text_myDrink.setText(myDrink[0] + "\n" + myDrink[1] + "\n" + myDrink[2] + "\n" + myDrink[3] + "\n" + myDrink[4]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(day == null){
            holder.dayText.setText("");
        }else{
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            int aYear = day.getYear();
            int aMonth = day.getMonthValue();
            int aDay = day.getDayOfMonth();

            holder.dayText.setBackgroundResource(R.drawable.circle_white);

            if(day.equals(CalendarUtil.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }

            reference.child("User").child(currentUser).child("날짜별 데이터").child(aYear + "년 " + aMonth + "월 " + aDay + "일")
                    .child("선택한 상태").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue(SelectedState.class) != null) {
                                SelectedState state = snapshot.getValue(SelectedState.class);

                                // 상태 불러옴.
                                selectedState = state.selectedState;
                                // 상태 int 형으로.
                                intState = Integer.parseInt(selectedState);

                                switch(intState){
                                    case 1 :
                                        holder.dayText.setBackgroundResource(R.drawable.circle1);
                                        break;
                                    case 2 :
                                        holder.dayText.setBackgroundResource(R.drawable.circle2);
                                        break;
                                    case 3 :
                                        holder.dayText.setBackgroundResource(R.drawable.circle3);
                                        break;
                                    case 4 :
                                        holder.dayText.setBackgroundResource(R.drawable.circle4);
                                        break;
                                    case 5 :
                                        holder.dayText.setBackgroundResource(R.drawable.circle5);
                                        break;
                                    default :
                                        holder.dayText.setBackgroundResource(R.drawable.circle_white);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

                reference.child("User").child(currentUser).child("날짜별 데이터").child(yearMonDay).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // 선택된 날짜 아래에 대한 데이터베이스가 존재하지 않을 경우 병과 잔 모두 0으로.
                            if (dataSnapshot.getValue(Drink.class) != null) {
                                Drink drink = dataSnapshot.getValue(Drink.class);
                                if (!(Objects.equals(dataSnapshot.getKey(), "선택한 상태")) && !(Objects.equals(dataSnapshot.getKey(), "총 알콜 농도"))) {
                                    if (drink.bottle != 0 || drink.glass != 0) {
                                        message = message + dataSnapshot.getKey() + ": " + drink.bottle + "병" + drink.glass + "잔\n";
                                    }

                                }
                            }



                            // 데이터베이스가 존재한다면 모든 데이터를 가져와서 저장.
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("마신 술");
                        // TODO builder.setMessage(); 아니면 TextView 이용해서

                        // 선택한 날의 주량을 다이얼로그에 보여줌.
                        builder.setMessage(message);

                        builder.setPositiveButton("확인", null);
                        builder.show();
                        message = "";
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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