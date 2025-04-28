package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.modules.account.constant.UserType;
import com.example.weaponMaster.modules.account.entity.UserInfo;
import com.example.weaponMaster.modules.account.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserInfoRepository userInfoRepository;

    public Boolean isAdminAuthorized(Boolean isAdminMode, String userId) {
        if (!isAdminMode) {
            return false;
        }

        UserInfo userInfo = userInfoRepository.findByUserId(userId);
        if (userInfo == null) {
            throw new IllegalArgumentException(String.format("[권한 확인 에러] 유저의 정보를 확인할 수 없습니다. userId: %s", userId));
        }

        if (userInfo.getUserType() != UserType.ADMIN) {
            throw new IllegalArgumentException(String.format("[권한 확인 에러] 관리자 권한이 없으나 관리자모드에서 동작 요청하였습니다. userId: %s, userType: %d", userInfo.getUserId(), userInfo.getUserType()));
        }

        return true;
    }

}
