package com.example.weaponMaster.modules.slack.repository;

import com.example.weaponMaster.modules.slack.entity.UserSlackNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSlackNoticeRepository extends JpaRepository<UserSlackNotice, Integer> {

    @Query(value = "SELECT * FROM user_slack_notice WHERE user_id = :userId AND notice_type = :noticeType", nativeQuery = true)
    UserSlackNotice findByUserIdAndType(String userId, Byte noticeType);
}
