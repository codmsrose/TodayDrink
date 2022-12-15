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

        if(day == null){
            holder.dayText.setText("");
        }else{
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            int aYear = day.getYear();
            int aMonth = day.getMonthValue();
            int aDay = day.getDayOfMonth();

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

                        // 선택된 날짜 아래에 대한 데이터베이스가 존재하지 않을 경우 병과 잔 모두 0으로.
                        if (snapshot.child("카스").getValue(Drink.class) == null) {
                            cass_bottle = 0;
                            cass_glass = 0;
                        }
                        // 데이터베이스가 존재할 경우, 병과 잔 데이터를 가져옴.
                        else {
                            Drink drink_cass = snapshot.child("카스").getValue(Drink.class);

                            cass_bottle = drink_cass.bottle;
                            cass_glass = drink_cass.glass;
                        }

                        if (snapshot.child("테라").getValue(Drink.class) == null) {
                            terra_bottle = 0;
                            terra_glass = 0;
                        }
                        else {
                            Drink drink_terra = snapshot.child("테라").getValue(Drink.class);

                            terra_bottle = drink_terra.bottle;
                            terra_glass = drink_terra.glass;
                        }

                        if (snapshot.child("하이네켄").getValue(Drink.class) == null) {
                            heineken_bottle = 0;
                            heineken_glass = 0;
                        }
                        else {
                            Drink drink_heineken = snapshot.child("하이네켄").getValue(Drink.class);

                            heineken_bottle = drink_heineken.getBottle();
                            heineken_glass = drink_heineken.getGlass();
                        }

                        if (snapshot.child("참이슬").getValue(Drink.class) == null) {
                            iseul_bottle = 0;
                            iseul_glass = 0;
                        }
                        else {
                            Drink drink_iseul = snapshot.child("참이슬").getValue(Drink.class);

                            iseul_bottle = drink_iseul.bottle;
                            iseul_glass = drink_iseul.glass;
                        }

                        if (snapshot.child("처음처럼").getValue(Drink.class) == null) {
                            start_bottle = 0;
                            start_glass = 0;
                        }
                        else {
                            Drink drink_start = snapshot.child("처음처럼").getValue(Drink.class);

                            start_bottle = drink_start.bottle;
                            start_glass = drink_start.glass;
                        }

                        if (snapshot.child("진로").getValue(Drink.class) == null) {
                            jinro_bottle = 0;
                            jinro_glass = 0;
                        }
                        else {
                            Drink drink_jinro = snapshot.child("진로").getValue(Drink.class);

                            jinro_bottle = drink_jinro.getBottle();
                            jinro_glass = drink_jinro.getGlass();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("마신 술");
                        // TODO builder.setMessage(); 아니면 TextView 이용해서

                        // 선택한 날의 주량을 다이얼로그에 보여줌.
                        builder.setMessage("카스 : " + cass_bottle + "병 " + cass_glass + "잔\n" +
                                "테라 : " + terra_bottle + "병 " + terra_glass + "잔\n" +
                                "하이네켄 : " + heineken_bottle + "병 " + heineken_glass + "잔\n" +
                                "참이슬 : " + iseul_bottle + "병 " + iseul_glass + "잔\n" +
                                "처음처럼 : " + start_bottle + "병 " + start_glass + "잔\n" +
                                "진로 : " + jinro_bottle + "병 " + jinro_glass + "잔");


                        builder.setPositiveButton("확인", null);
                        builder.show();

                        // 데이터베이스가 존재한다면 모든 데이터를 가져와서 저장.
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