package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.dto.ReqLoginDto;
import com.example.weaponMaster.api.account.dto.RespLoginDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.adminToken.constant.AdminTokenType;
import com.example.weaponMaster.modules.adminToken.entity.AdminToken;
import com.example.weaponMaster.modules.adminToken.repository.AdminTokenRepository;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository   userInfoRepository;
    private final AdminTokenRepository adminTokenRepository;
    private final UserLogService       userLogService;

    @Transactional
    public ApiResponse<RespLoginDto> login(ReqLoginDto request) {
        String mode = request.getIsAdminMode() ? "관리자모드" : "일반모드";

        UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
        if (userInfo == null) {
            return ApiResponse.error(String.format("[%s 로그인 실패] 유저 정보 없음 userId: %s", mode, request.getUserId()));
        }

        if (!Objects.equals(userInfo.getUserPw(), request.getUserPw())) {
            return ApiResponse.error(String.format("[%s 로그인 실패] 비밀번호 불일치 userId: %s", mode, request.getUserId()));
        }

        RespLoginDto resp = new RespLoginDto();
        if (request.getIsAdminMode()) {
            // 관리자 권한이 없어도 관리자모드로 로그인 시도 자체는 할 수 있으므로 throw 가 아닌 에러 메시지 정상 반환 케이스 추가
            if (userInfo.getUserType() != UserType.ADMIN) {
                return ApiResponse.error(String.format("[%s 로그인 실패] 관리자 권한 없음 userId: %s", mode, request.getUserId()));
            }

            // 관리자 권한 확인되면 프론트에 토큰 전달
            AdminToken adminToken = adminTokenRepository.findByType(AdminTokenType.WEAPON_MASTER);
            if (adminToken == null) {
                throw new RuntimeException(String.format("[%s 로그인 실패] 관리자 전용 토큰 값이 조회되지 않고 있습니다. userId: %s", mode, request.getUserId()));
            }

            resp.setAdminToken(adminToken.getToken());
        }

        userInfoRepository.updateLastLoginDate(request.getUserId());
        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.WEAPON_MASTER, LogActType.LOGIN);

        return ApiResponse.success(resp);
    }

    public boolean isUserIdAvailable(String userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            return true;
        }

        return false;
    }

    @Transactional
    public ApiResponse<Void> createUserInfo(ReqJoinDto request) {
        if (!isUserIdAvailable(request.getUserId())) {
            throw new IllegalArgumentException("[회원가입 오류] 이미 존재하는 아이디로 가입 API 를 호출하였습니다. userId already exist: " + request.getUserId());
        }

        UserInfo userInfo = new UserInfo(request.getUserId(), request.getUserPw());

        userInfoRepository.save(userInfo);
        userLogService.saveLog(request.getUserId(), LogContentsType.WEAPON_MASTER, LogActType.JOIN);

        return ApiResponse.success();
    }
}
