package com.example.weaponMaster.modules.slack.repository;

import com.example.weaponMaster.modules.slack.entity.SlackBotToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackBotTokenRepository extends JpaRepository<SlackBotToken, Integer> {

    @Query(value = "SELECT * FROM slack_bot_token WHERE type = :slackBotType", nativeQuery = true)
    SlackBotToken findByType(Integer slackBotType);
}