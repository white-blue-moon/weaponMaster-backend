package com.example.weaponMaster.modules.siteSetting.repository;

import com.example.weaponMaster.modules.siteSetting.constant.ActiveState;
import com.example.weaponMaster.modules.siteSetting.entity.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteSettingRepository extends JpaRepository<SiteSetting, Integer> {

    @Query(value = "SELECT * FROM site_setting WHERE active_state = :activeState ORDER BY id DESC LIMIT 1", nativeQuery = true)
    SiteSetting findLatestActiveSetting(Integer activeState);

    // 활성화된 최신 설정값 조회
    default SiteSetting findLatestActiveSetting() {
        return findLatestActiveSetting(ActiveState.ACTIVE);
    }
}
