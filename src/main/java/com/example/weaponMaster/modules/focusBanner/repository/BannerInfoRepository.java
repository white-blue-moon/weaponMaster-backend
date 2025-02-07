package com.example.weaponMaster.modules.focusBanner.repository;

import com.example.weaponMaster.modules.focusBanner.entity.BannerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerInfoRepository extends JpaRepository<BannerInfo, Integer> {

    @Query(value = "SELECT * FROM ref_focus_banner_info WHERE version = :version AND banner_type = :bannerType ORDER BY img_order", nativeQuery = true)
    BannerInfo[] findByVersionAndType(Integer version, Integer bannerType);
}
