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
@Table(name="member")
public class Member extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "uid", nullable = false)
    private String uid ;

    @Column(name = "name")
    private String name ;

    @Column(name = "birth")
    private String birth ;

    @Column(name = "phone_number")
    private String phoneNumber ;

    @Column(name = "login_type")
    private String loginType ;

    @Builder.Default
    @Column(name = "activated", nullable = false)
    private boolean activated = true;
}
