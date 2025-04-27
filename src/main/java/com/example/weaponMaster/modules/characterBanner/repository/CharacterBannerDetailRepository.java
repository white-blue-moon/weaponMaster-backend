package com.example.weaponMaster.modules.characterBanner.repository;

import com.example.weaponMaster.modules.characterBanner.entity.CharacterBannerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterBannerDetailRepository extends JpaRepository<CharacterBannerDetail, Integer> {

    @Query(value = "SELECT * FROM ref_character_banner_detail WHERE version = :version ORDER BY character_type, character_detail_type", nativeQuery = true)
    CharacterBannerDetail[] findByVersion(Integer version);
}
