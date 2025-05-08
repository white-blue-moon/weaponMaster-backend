package com.example.weaponMaster.modules.accessGate.repository;

import com.example.weaponMaster.modules.accessGate.entity.AccessGatePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessGatePasswordRepository extends JpaRepository<AccessGatePassword, Integer> {

    @Query(value = "SELECT * FROM access_gate_password WHERE type = :type", nativeQuery = true)
    AccessGatePassword findByType(Integer type);
}