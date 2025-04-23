package com.example.weaponMaster.modules.slack.repository;

import com.example.weaponMaster.modules.slack.entity.SlackBot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackBotRepository extends JpaRepository<SlackBot, Integer> {

    @Query(value = "SELECT * FROM slack_bot WHERE type = :botType", nativeQuery = true)
    SlackBot findByType(Byte botType);
}