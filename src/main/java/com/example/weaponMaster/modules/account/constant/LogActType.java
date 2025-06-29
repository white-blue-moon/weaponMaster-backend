package com.example.weaponMaster.modules.account.constant;

public class LogActType {

    public static final Short LOGIN_ACCESS_GATE  = 0; // 페이지 접근 제한 해제

    public static final Short LOGIN  = 1; // 로그인
    public static final Short LOGOUT = 2; // 로그아웃
    public static final Short JOIN   = 3; // 회원가입

    public static final Short CREATE  = 10; // 생성
    public static final Short READ    = 11; // 읽기
    public static final Short UPDATE  = 12; // 수정
    public static final Short DELETE  = 13; // 삭제

    public static final Short CREATE_COMMENT = 20; // 댓글 작성
    public static final Short DELETE_COMMENT = 21; // 댓글 삭제

    public static final Short TEST = 30; // 테스트

    public static final Short TOGGLE_ARTICLE_PIN = 40; // 게시물 상단 고정

    public static final Short RESUME = 50; // 다시 재게
}
