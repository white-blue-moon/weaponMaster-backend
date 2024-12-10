package com.example.weaponMaster.repository;

import com.example.weaponMaster.entity.BannerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerInfoRepository extends JpaRepository<BannerInfo, Integer> {

    List<BannerInfo> findByVersion(Integer version);

    // Native Query 를 사용한 메서드
    @Query(value = "SELECT * FROM ref_banner_info WHERE version = :version AND banner_type = :bannerType ORDER BY img_order", nativeQuery = true)
    List<BannerInfo> findByVersionAndBannerTypeOrderByImgOrder(
            @Param("version") Integer version,
            @Param("bannerType") Integer bannerType
    );
}
