package com.example.weaponMaster.modules.neopleAPI.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_auction_notice")
@Data
@DynamicUpdate // 변경된 필드만 업데이트
@NoArgsConstructor
public class UserAuctionNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "auction_no", nullable = false, length = 255)
    private String auctionNo;

    @Column(name = "item_info", nullable = false, columnDefinition = "json")
    private JsonNode itemInfo;

    @Column(name = "auction_state", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private int auctionState;

    @Column(name = "create_date", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    public UserAuctionNotice(String userId, JsonNode itemInfo) {
        this.userId    = userId;
        this.auctionNo = itemInfo.path("auctionNo").asText();
        this.itemInfo  = itemInfo;
    }

    // TODO 아래와 같이 예외 처리 할지 고민
    // UserAuctionNotice 생성자
    // public UserAuctionNotice(String userId, JsonNode itemInfo) {
    //     this.userId    = userId;
    //     this.auctionNo = Optional.ofNullable(itemInfo.path("auctionNo").asText())
    //             .filter(s -> !s.isEmpty())
    //             .orElseThrow(() -> new IllegalArgumentException("auctionNo 값이 없습니다."));
    //     this.itemInfo  = itemInfo;
    // }
}
