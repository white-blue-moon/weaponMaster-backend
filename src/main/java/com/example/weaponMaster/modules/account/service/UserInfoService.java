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
}
