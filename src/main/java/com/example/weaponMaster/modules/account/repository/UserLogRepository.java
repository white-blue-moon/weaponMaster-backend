package com.example.weaponMaster.modules.account.repository;

import com.example.weaponMaster.modules.account.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, String> {

}
