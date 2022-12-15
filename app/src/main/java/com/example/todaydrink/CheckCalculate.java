package com.example.todaydrink;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckCalculate {

//C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))-ßt(경과시간)

    private static double maxAlcohol; //C 혈중 알코올 최대치

    private static double newAlcohol; //섭취한 알코올의 총량
    private static double newAmountOfAlcohol; //음주량
    private static double density;//술의 농도
    private static final double N = 0.7894;

    private static double kg = 60; //TODO : 만약에 사용자가 설정화면에서 몸무게 바꾸면 이것 바뀌어야 함
    private static double R = 0.64; //성별에 대한 계수(남자 0.86, 여자 0.64)   //TODO : 회원가입할 때 입력받기

    private static long inputTime = 1000*60*60*17; //시작시간
    private static long nowTime;

    private static double bloodAlcoholLevel=0; //음주운전 당시 혈중알코올 = 최고혈중알코올농도-(경과시간 × 0.015%)

    static ArrayList<bloodAlcohol> bloodAlcoholArrayList = new ArrayList<bloodAlcohol>();


    //상태의 기준이 되는 알콜농도
    static private double[] stateWithAlcohol = new double[5];


    public static void setMaxState() {
        stateWithAlcohol[0] = 16.5 * 0.05;
        stateWithAlcohol[1] = 16.5 * 0.1;
        stateWithAlcohol[2] = 16.5 * 0.15;
        stateWithAlcohol[3] = 16.5 * 0.20;
        stateWithAlcohol[4] = 16.5 * 0.25;

    }

    //

    private static int maxState;


    //몸무게 입력
    public static void setWeight(double kg1) {
        kg = kg1;
    }



    //성별계수
    public static void setR (int i) {

        if(i==0){
            R= 0.86;}
        else{
            R=0.64;}
    }

    public static void setStateArray(double[] stateArray){
        stateWithAlcohol = stateArray;
    }

    //배열 원소
    private static class bloodAlcohol{

        double maxAlcohol;
        long inputTime;
        double time;

        bloodAlcohol(double maxAlcohol, long time){
            this.maxAlcohol = maxAlcohol;
            this.inputTime = time;
        }

        double calculateWidMark(){
            time = (double)(nowTime-inputTime /*-90분*/ ) * 0.015 ;
            if(time <0)
            {
                time=0;
            }
            return maxAlcohol-(double)time;
        }

    }


    //들어온 시간
    public static void setInputTime(long inputTime1){
        inputTime = inputTime1;
    }

    //지금 시간
    public static long setNowTime(){
        nowTime = System.currentTimeMillis();
        return nowTime;
    }



     //////////////////////////
    //혈중알콜농도 계산


    /////////////////////////////////////



    //TODO: 데이터베이스에 새로 넣어야 함


    //상태 기준 업데이트
    public static void updateStateStandard(int statePick) {

        switch (statePick-1){

            case (0):
                stateWithAlcohol[0]=(stateWithAlcohol[0]+bloodAlcoholLevel)/2;
                break;

            case (1):
                stateWithAlcohol[1]=(stateWithAlcohol[1]+bloodAlcoholLevel)/2;
                break;

            case (2):
                stateWithAlcohol[2]=(stateWithAlcohol[2]+bloodAlcoholLevel)/2;
                break;

            case (3):
                stateWithAlcohol[3]=(stateWithAlcohol[3]+bloodAlcoholLevel)/2;
                break;

            case (4):
                stateWithAlcohol[4]=(stateWithAlcohol[4]+bloodAlcoholLevel)/2;
                break;

        }
        StatisticsActivity.update(stateWithAlcohol);


    }



    public static int calculate(String currentUser, double density1, double newAmountOfAlcohol) {
        double density = density1/1000;

        newAlcohol = density*newAmountOfAlcohol*N;  //방금 섭취한 알코올
        calculateMaxAlcohol(currentUser);
        bloodAlcoholArrayList.add(new bloodAlcohol(maxAlcohol, setNowTime()));

        calculateBloodAlcohol();
        return updateState(bloodAlcoholLevel);


    }



    //섭취한 알코올의 최대치 구하기 C
    private static double calculateMaxAlcohol(String currentUser){
        //C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        reference.child("User").child(currentUser).child("프로필").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(UserAccount.class) != null) {
                    UserAccount user = snapshot.getValue(UserAccount.class);

                    setR(user.gender);
                    setWeight(Double.parseDouble(user.getWeight()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        maxAlcohol= (newAlcohol*0.7) / (kg*R);
        return maxAlcohol;
    }


    //현재 혈중 알콜농도
    private static void calculateBloodAlcohol(){

        for(int i=0; i<bloodAlcoholArrayList.size();i++){
            bloodAlcoholLevel+=bloodAlcoholArrayList.get(i).calculateWidMark();
        }
    }

     // 만약 지금 혈중알콜농도가 각 상태별로 정한 알콜 농도를 넘어가면 화면에서 변화를 주기 위해서
    public static int updateState(double bloodAlcoholLevel){

        if(bloodAlcoholLevel<stateWithAlcohol[0])
        {
            maxState =0;
        }
            else if(stateWithAlcohol[0]<=bloodAlcoholLevel  && bloodAlcoholLevel<stateWithAlcohol[1])
        {
            maxState =1;
        }
        else if(stateWithAlcohol[1]<=bloodAlcoholLevel && bloodAlcoholLevel<stateWithAlcohol[2])
        {
            maxState=2;
        }
        else if(stateWithAlcohol[2]<=bloodAlcoholLevel && bloodAlcoholLevel <stateWithAlcohol[3])
        {
            maxState=3;
        }
        else
        {
            maxState=4;
        }


        return maxState;

    }

    public static int GetMaxState(){
        return maxState;
    }

    //상태에 update

    public static class StateCalculate {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        private final String currentUser = ((InputDrinkActivity)InputDrinkActivity.mContext).currentUser;

        public void setState(int num, double Alcohol){

            // stateWithAlcohol이랑 데이터베이스의 각 상태의 혈중알콜농도랑 값이 똑같아요.

            //TODO: 왜 num-1 하시는지?

            reference.child("User").child(currentUser).child("상태").child(String.valueOf(num)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    State state = snapshot.getValue(State.class);
                    state.density = Integer.parseInt(String.valueOf(stateWithAlcohol[num - 1]));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        public double getState(int num){
            //코드에서 state에 해당하는 혈중알콜농도 알고 싶을 때 사용
            return stateWithAlcohol[num-1];

        }
    }





}
