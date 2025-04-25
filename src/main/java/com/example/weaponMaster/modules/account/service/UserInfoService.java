package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.dto.ReqLoginDto;
import com.example.weaponMaster.modules.account.constant.LogActType;
import com.example.weaponMaster.modules.account.constant.LogContentsType;
import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import com.example.weaponMaster.modules.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserLogService     userLogService;

    @Transactional
    public ApiResponse<Void> login(ReqLoginDto request) {
        String mode = request.getIsAdminMode() ? "관리자모드" : "일반모드";

        UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
        if (userInfo == null) {
            throw new IllegalArgumentException(String.format("[%s 로그인 실패] 유저 정보 없음 userId: %s", mode, request.getUserId()));
        }

        if (!Objects.equals(userInfo.getUserPw(), request.getUserPw())) {
            throw new IllegalArgumentException(String.format("[%s 로그인 실패] 비밀번호 불일치 userId: %s", mode, request.getUserId()));
        }

        if (request.getIsAdminMode() && userInfo.getUserType() != UserType.ADMIN) {
            throw new IllegalArgumentException(String.format("[%s 로그인 실패] 관리자 권한 없음 userId: %s", mode, request.getUserId()));
        }

        userInfoRepository.updateLastLoginDate(request.getUserId());
        userLogService.saveLog(request.getUserId(), request.getIsAdminMode(), LogContentsType.WEAPON_MASTER, LogActType.LOGIN);

        return ApiResponse.success();
    }

    // TODO 함수명 그대로 쓸지 고민
    public UserInfo getUserInfoEntity(String userId) {
        return userInfoRepository.findByUserId(userId);
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
            throw new IllegalArgumentException("userId already exist: " + request.getUserId());
        }

        UserInfo userInfo = new UserInfo(
                request.getUserId(),
                request.getUserPw(),
                request.getDfServerId(),
                request.getDfCharacterName()
        );

        userInfoRepository.save(userInfo);
        userLogService.saveLog(request.getUserId(), LogContentsType.WEAPON_MASTER, LogActType.JOIN);

        return ApiResponse.success();
    }

    // TODO 비밀번호 업데이트
    @Transactional
    public ApiResponse<Void> updateUserPW(String userId, String newPassword) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            throw new IllegalArgumentException("can't find user info: " + userId);
        }

        userInfo.setUserPw(newPassword);
        userInfoRepository.save(userInfo);
        return ApiResponse.success();
    }

    // TODO DF 정보 업데이트
    @Transactional
    public ApiResponse<Void> updateUserDfInfo(String userId, String dfServerId, String dfCharacterName) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            throw new IllegalArgumentException("can't find user info: " + userId);
        }

        userInfo.setDfServerId(dfServerId);
        userInfo.setDfCharacterName(dfCharacterName);
        userInfoRepository.save(userInfo);
        return ApiResponse.success();
    }
}
