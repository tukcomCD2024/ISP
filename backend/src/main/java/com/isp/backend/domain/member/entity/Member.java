package com.isp.backend.domain.member.entity;

import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uid;

    private String name;

    private String birth;

    private String phoneNumber;

    private String loginType;

    @Builder.Default
    @Column(nullable = false)
    private boolean activated = true;
}
