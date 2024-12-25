package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.api.account.join.dto.ReqJoinDto;
import com.example.weaponMaster.api.account.join.dto.RespJoinDto;
import com.example.weaponMaster.modules.account.dto.UserInfoDto;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    // TODO DB 에러 처리 필요
    public UserInfoDto getUserInfo(String userId) {
        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            return new UserInfoDto();
        }

        return new UserInfoDto(
                true,
                userInfo.getUserId(),
                userInfo.getUserPw(),
                userInfo.getUserType(),
                userInfo.getDfServerId(),
                userInfo.getDfCharacterName(),
                userInfo.getLastLoginDate(),
                userInfo.getJoinDate()
        );
    }

    public RespJoinDto createUserInfo(ReqJoinDto request) {
        UserInfo userInfo = userInfoRepository.findByUserId(request.getUserId());
        if (userInfo != null) {
            // TODO "userId is already exist"
            return new RespJoinDto(false);
        }

        userInfo = new UserInfo(
                request.getUserId(),
                request.getUserPw(),
                request.getDfServerId(),
                request.getDfCharacterName()
        );

        // TODO 간결한 에러 처리 방식 확인 중
        try {
            userInfoRepository.save(userInfo);
            return new RespJoinDto(true);
        } catch (Exception e) {
            System.err.println("Error saving user info: " + e.getMessage());
            return new RespJoinDto(false);
        }
    }

    public void updateUserPW(String userId, String pw) {
        // TODO
    }

    public void updateUserDfInfo(String userId, String dfServerId, String dfCharacterName) {
        // TODO
    }
}
