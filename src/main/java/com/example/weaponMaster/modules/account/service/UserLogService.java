package com.example.weaponMaster.modules.account.service;

import com.example.weaponMaster.modules.account.entity.UserLog;
import com.example.weaponMaster.modules.account.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepo;

    // 일반/관리자 구분 없이 기본 로그 저장 (일반으로 저장됨)
    public void saveLog(String userId, Short contentsType, Short actType) {
        UserLog log = new UserLog(userId, contentsType, actType);
        userLogRepo.save(log);
    }

    // 기본 로그 저장
    public void saveLog(String userId, Boolean isAdminMode, Short contentsType, Short actType) {
        UserLog log = new UserLog(userId, isAdminMode, contentsType, actType);
        userLogRepo.save(log);
    }

    // refValue 포함 로그 저장
    public void saveLog(String userId, Boolean isAdminMode, Short contentsType, Short actType, Short refValue) {
        UserLog log = new UserLog(userId, isAdminMode, contentsType, actType, refValue);
        userLogRepo.save(log);
    }

    // refValue, extraRefValue 포함 로그 저장
    public void saveLog(String userId, Boolean isAdminMode, Short contentsType, Short actType, Short refValue, Short extraRefValue) {
        UserLog log = new UserLog(userId, isAdminMode, contentsType, actType, refValue, extraRefValue);
        userLogRepo.save(log);
    }
}
