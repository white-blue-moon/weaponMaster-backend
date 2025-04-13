package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.dto.ReqLoginDto;
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

    @Transactional
    public ApiResponse<Void> loginNormal(ReqLoginDto request) {
        UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
        if (userInfo == null) {
            throw new IllegalArgumentException("userId doesn't exist: " + request.getUserId());
        }

        if (!Objects.equals(userInfo.getUserPw(), request.getUserPw())) {
            throw new IllegalArgumentException("wrong password: " + request.getUserId());
        }

        userInfoRepository.updateLastLoginDate(request.getUserId());
        return ApiResponse.success();
    }

    @Transactional
    public ApiResponse<Void> loginAdmin(ReqLoginDto request) {
        UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
        if (userInfo == null) {
            throw new IllegalArgumentException(String.format("[관리자모드 로그인 실패] 유저 정보 조회 불가 userId: %s", request.getUserId()));
        }

        if (!Objects.equals(userInfo.getUserPw(), request.getUserPw())) {
            throw new IllegalArgumentException(String.format("[관리자모드 로그인 실패] 비밀번호 불일치 userId: %s", request.getUserId()));
        }

        if (userInfo.getUserType() != UserType.ADMIN) {
            throw new IllegalArgumentException(String.format("[관리자모드 로그인 실패] 관리자 권한이 없으나 관리자모드 로그인 시도 userId: %s", request.getUserId()));
        }

        userInfoRepository.updateLastLoginDate(request.getUserId());
        return ApiResponse.success();
    }

    // TODO 함수명 그대로 쓸지 고민
    public UserInfo getUserInfoEntity(String userId) {
        return userInfoRepository.findByUserId(userId);
    }

    public boolean isUserIdAvailable(String userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        return userInfo == null;
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
