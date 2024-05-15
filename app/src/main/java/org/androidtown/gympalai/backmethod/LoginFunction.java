package org.androidtown.gympalai.backmethod;

public class LoginFunction { //로그인 한 유저의 아이디로 설정하기 위한 코드
    private static String myId;

    public LoginFunction(String myId){
        this.myId = myId;
    }
    public LoginFunction(){}

    public String getMYId(){
        return myId;
    }
}
