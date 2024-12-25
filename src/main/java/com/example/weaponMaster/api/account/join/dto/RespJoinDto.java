package com.example.weaponMaster.api.account.join.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // save 함수 호출할 때 필요
@AllArgsConstructor
public class RespJoinDto {
    private boolean isSuccess; // 클라에서 전달받을 때는 is 가 빠진 채로 받음
}
