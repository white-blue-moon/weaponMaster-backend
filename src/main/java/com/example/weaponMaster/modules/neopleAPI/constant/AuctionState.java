package com.example.weaponMaster.modules.neopleAPI.constant;

public class AuctionState {

    public static final Integer MONITOR_ERROR  = -100; // 경매 추적 중 에러 발생
    public static final Integer SERVER_INSPECT = -53;  // 경매 추적 중 던전앤파이터 시스템(서버) 점검
    public static final Integer NONE           = -1;   // 아무런 상태도 할당되지 않음을 의미

    public static final Integer SELLING  = 0;
    public static final Integer SOLD_OUT = 1;
    public static final Integer EXPIRED  = 2;
}
