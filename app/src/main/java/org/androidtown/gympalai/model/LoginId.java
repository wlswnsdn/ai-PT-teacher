package org.androidtown.gympalai.model;

public class LoginId {
    // 테스트용 하드 코딩, 로그인 메서드 구현 후 private static String loginId; 으로 변경할 것
    private static String loginId="jin";

    public LoginId() {
    }
    // 로그인하고나서 로그인한 id 여기다가 넣음
    public LoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }

}
