package com.example.weaponMaster.modules.characterBanner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ref_character_banner_detail", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"version", "character_type", "character_detail_type"})
})
@Getter
@NoArgsConstructor
public class CharacterBannerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "character_type", nullable = false)
    private Byte characterType;

    @Column(name = "character_detail_type", nullable = false)
    private Byte characterDetailType;

    @Column(name = "character_name", nullable = false, length = 100)
    private String characterName;

    @Column(name = "character_intro", nullable = false, length = 255)
    private String characterIntro;

    @Column(name = "img_url", nullable = false, length = 255)
    private String imgUrl;

    @Column(name = "homepage_link_url", length = 255)
    private String homepageLinkUrl;

    @Column(name = "create_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;
}
