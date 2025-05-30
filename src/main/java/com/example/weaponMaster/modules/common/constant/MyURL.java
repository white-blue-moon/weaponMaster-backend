package com.example.weaponMaster.modules.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyURL {

    public static String WEAPON_MASTER;

    // application.yml 값 참조
    public MyURL(@Value("${weapon-master.url}") String url) {
        WEAPON_MASTER = url;
    }
}
