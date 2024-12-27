package com.example.weaponMaster.modules.account.repository;

import com.example.weaponMaster.modules.account.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    @Query(value = "SELECT * FROM user_info WHERE user_id = :user_id", nativeQuery = true)
    UserInfo findByUserId(
            @Param("user_id") String user_id
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_info SET last_login_date = CURRENT_TIMESTAMP WHERE user_id = :user_id", nativeQuery = true)
    void updateLastLoginDate(@Param("user_id") String user_id);
}
