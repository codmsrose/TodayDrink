package com.example.todaydrink;
import android.util.Log;

import java.util.ArrayList;

public class CheckCalculate {

//C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))-ßt(경과시간)

    private static int stateNum;//상태 입력
    private static double maxAlcohol; //C 혈중 알코올 최대치

    private static double newAlcohol; //섭취한 알코올의 총량
    private static double newAmountOfAlcohol; //음주량
    private static double density;//술의 농도
    private static final double N = 0.7894;

    private static double kg = 60; //TODO : 만약에 사용자가 설정화면에서 몸무게 바꾸면 이것 바뀌어야 함
    private static double R = 0.64; //성별에 대한 계수(남자 0.86, 여자 0.64)   //TODO : 회원가입할 때 입력받기

    private static long inputTime; //시작시간
    private static long nowTime=360;

    private static double bloodAlcoholLevel; //음주운전 당시 혈중알코올 = 최고혈중알코올농도-(경과시간 × 0.015%)

    static ArrayList<bloodAlcohol> bloodAlcoholArrayList = new ArrayList<bloodAlcohol>();


    //상태입력
    public static void setStateNum(int stateNum1) {
        stateNum = stateNum1;
    }

    //섭취한 알코올의 총량 A
    public static void setNewAlcohol(double density, double newAmountOfAlcohol) {
        newAlcohol = density*newAmountOfAlcohol*N;
    }

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

    //들어온 시간
    public static void setInputTime(long inputTime1){
        inputTime = inputTime1;
    }

    //지금 시간
    public static void setNowTime(long nowTime1){
        nowTime= nowTime1;
    }

    //섭취한 알코올의 최대치 구하기 C
    private static double calculateMaxAlcohol(){
        //C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))
        maxAlcohol= (newAlcohol*0.7) / (kg*R);
        return maxAlcohol;
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

    private static double calculateBloodAlcohol(){
        bloodAlcoholLevel=0;

        for(int i=0; i<bloodAlcoholArrayList.size();i++){
            bloodAlcoholLevel+=bloodAlcoholArrayList.get(i).calculateWidMark();
        }
        return bloodAlcoholLevel;
    }

    public static double test1(){
        setNewAlcohol(10,10);
        bloodAlcohol test = new bloodAlcohol(calculateMaxAlcohol(), 300);
        bloodAlcoholArrayList.add(test);
        double a = calculateBloodAlcohol();
        return a;
    }

}