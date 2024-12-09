package com.example.weaponMaster.repository;

import com.example.weaponMaster.entity.BannerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerInfoRepository extends JpaRepository<BannerInfo, Integer> {
    List<BannerInfo> findByVersion(Integer version);
}
