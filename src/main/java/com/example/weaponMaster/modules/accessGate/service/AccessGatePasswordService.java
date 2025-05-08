package com.example.weaponMaster.modules.accessGate.service;

import com.example.weaponMaster.modules.accessGate.constant.AccessGateType;
import com.example.weaponMaster.modules.accessGate.entity.AccessGatePassword;
import com.example.weaponMaster.modules.accessGate.repository.AccessGatePasswordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessGatePasswordService {

    private final AccessGatePasswordRepository accessGatePasswordRepo;

    public Boolean isPasswordValid(String[] password) {
        AccessGatePassword gatePassword   = accessGatePasswordRepo.findByType(AccessGateType.WEAPON_MASTER);
        String             passwordString = String.join("", password);

        if (passwordString.equals(gatePassword.getPassword())) {
            return true;
        }

        return false;
    }

}