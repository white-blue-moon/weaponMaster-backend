package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.join.dto.ReqJoinDto;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    // TODO 조회하는 중복 로직 묶기
    public UserInfo getUserInfoEntity(String userId) {
        UserInfo userInfo;

        try {
            userInfo = userInfoRepository.findByUserId(userId);
        } catch (Exception e) {
            System.err.println("Error get user info: " + e.getMessage());
            return null;
        }

        return userInfo;
    }

    // TODO DB 에러 처리 필요
    public boolean checkLogin(String userId, String userPw) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            return false;
        }

        if (Objects.equals(userInfo.getUserId(), userId)) {
            if (Objects.equals(userInfo.getUserPw(), userPw)) {
                userInfoRepository.updateLastLoginDate(userId);
                return true;
            }
        }

        return false;
    }

    // TODO DB 에러 처리 필요
    public boolean isUserIdExist(String userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            return false;
        }

        return true;
    }

    public boolean createUserInfo(ReqJoinDto request) {
        if (isUserIdExist(request.getUserId())) {
            return false;
        }

        UserInfo userInfo = new UserInfo(
                request.getUserId(),
                request.getUserPw(),
                request.getDfServerId(),
                request.getDfCharacterName()
        );

        // TODO 간결한 에러 처리 방식 확인 중
        try {
            userInfoRepository.save(userInfo);
            return true;
        } catch (Exception e) {
            System.err.println("Error saving user info: " + e.getMessage());
            return false;
        }
    }

    public void updateUserPW(String userId, String pw) {
        // TODO
    }

    public void updateUserDfInfo(String userId, String dfServerId, String dfCharacterName) {
        // TODO
    }
}
