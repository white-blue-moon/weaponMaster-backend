package com.example.weaponMaster.modules.characterBanner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ref_character_banner", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"version", "character_type", "banner_order"})
})
@Getter
@NoArgsConstructor
public class CharacterBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "character_type", nullable = false)
    private Byte characterType;

    @Column(name = "character_desc", length = 255)
    private String characterDesc;

    @Column(name = "name_img_url", nullable = false, length = 255)
    private String nameImgUrl;

    @Column(name = "thumb_img_url", nullable = false, length = 255)
    private String thumbImgUrl;

    @Column(name = "banner_order", nullable = false)
    private Byte bannerOrder;

    @Column(name = "create_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;
}
