package com.example.weaponMaster.modules.slack.repository;

import com.example.weaponMaster.modules.slack.entity.AdminSlackNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSlackNoticeRepository extends JpaRepository<AdminSlackNotice, Integer> {

    @Query(value = "SELECT * FROM admin_slack_notice WHERE channel_type = :channelType", nativeQuery = true)
    AdminSlackNotice findByType(Integer channelType);
}
