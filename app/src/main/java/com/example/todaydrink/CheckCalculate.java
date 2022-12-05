package com.example.todaydrink;

public class CheckCalculate {

//C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))-ßt(경과시간)

    private int stateNum;//상태 입력
    private double maxAlcohol; //C 혈중 알코올 최대치

    private double newAlcohol; //섭취한 알코올의 총량
    private double newAmountOfAlcohol; //음주량
    private double density;//술의 농도
    private final double N = 0.7894;

    private double kg; //TODO : 만약에 사용자가 설정화면에서 몸무게 바꾸면 이것 바뀌어야 함
    private double R; //성별에 대한 계수(남자 0.86, 여자 0.64)   //TODO : 회원가입할 때 입력받기

    private long startTime; //시작시간
    private long nowTime;

    private int bloodAlcoholLevel; //음주운전 당시 혈중알코올 = 최고혈중알코올농도-(경과시간 × 0.015%)

    //상태입력
    public void setStateNum(int stateNum) {
        this.stateNum = stateNum;
    }
    //섭취한 알코올의 총량
    public void setNewAlcohol(double density, double newAmountOfAlcohol) {
        newAlcohol = density*newAmountOfAlcohol*N;
    }
    //몸무게 입력
    public void setWeight(double kg) {
        this.kg = kg;
    }

    //성별계수
    public void setR (int i) {

        if(i==0){
            this.R= 0.86;}
        else{
            this.R=0.64;}
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void setNowTime(long nowTime){
        this.nowTime= nowTime;
    }

    //섭취한 알코올의 최대치 구하기
    private void calculateMaxAlcohol(){
        //C(최고치)= A(섭취한 알코올 총량 = 음주량x술의농도xN)×0.7(체내흡수율)/(P(체중)×R(성별에 대한 계수))
        maxAlcohol= (newAlcohol*0.7) / (kg*R);
    }
}
