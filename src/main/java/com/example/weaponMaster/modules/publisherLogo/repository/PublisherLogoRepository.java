package com.example.weaponMaster.modules.publisherLogo.repository;

import com.example.weaponMaster.modules.publisherLogo.entity.PublisherLogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherLogoRepository extends JpaRepository<PublisherLogo, Integer> {

    @Query(value = "SELECT * FROM ref_publisher_logo WHERE version = :version", nativeQuery = true)
    PublisherLogo findByVersion(Integer version);
}
