package com.example.weaponMaster.modules.neopleAPI.repository;

import com.example.weaponMaster.modules.neopleAPI.entity.UserAuctionNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuctionNoticeRepository extends JpaRepository<UserAuctionNotice, Integer> {

    @Query(value = "SELECT * FROM user_auction_notice WHERE user_id = :userId ORDER BY id DESC", nativeQuery = true)
    UserAuctionNotice[] findByUserId(String userId);

    @Query(value = "SELECT * FROM user_auction_notice WHERE user_id = :userId AND auction_no = :auctionNo", nativeQuery = true)
    UserAuctionNotice findByUserIdAndNo(String userId, String auctionNo);

    @Query(value = "SELECT * FROM user_auction_notice WHERE user_id = :userId AND auction_state = :auctionState", nativeQuery = true)
    UserAuctionNotice[] findByUserIdAndState(String userId, Integer auctionState);
}
