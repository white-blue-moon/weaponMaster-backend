package com.example.weaponMaster.modules.characterBanner.repository;

import com.example.weaponMaster.modules.characterBanner.entity.CharacterBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterBannerRepository extends JpaRepository<CharacterBanner, Integer> {

    @Query(value = "SELECT * FROM ref_character_banner WHERE version = :version ORDER BY banner_order", nativeQuery = true)
    CharacterBanner[] findByVersion(Integer version);
}
