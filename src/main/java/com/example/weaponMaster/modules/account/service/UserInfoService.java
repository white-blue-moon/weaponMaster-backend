package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.dto.ReqJoinDto;
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
    public ApiResponse<Void> checkLogin(String userId, String userPw) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            throw new IllegalArgumentException("userId doesn't exist: " + userId);
        }

        if (!Objects.equals(userInfo.getUserPw(), userPw)) {
            throw new IllegalArgumentException("wrong password: " + userId);
        }

        userInfoRepository.updateLastLoginDate(userId);
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
