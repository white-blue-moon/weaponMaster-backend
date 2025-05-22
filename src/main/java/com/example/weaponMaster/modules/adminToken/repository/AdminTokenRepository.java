package com.example.weaponMaster.modules.adminToken.repository;

import com.example.weaponMaster.modules.adminToken.entity.AdminToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTokenRepository extends JpaRepository<AdminToken, Integer> {

    @Query(value = "SELECT * FROM admin_token LIMIT 1", nativeQuery = true)
    AdminToken findFirst();
}
