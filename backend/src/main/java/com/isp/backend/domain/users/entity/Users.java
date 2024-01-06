package com.isp.backend.domain.users.entity;

import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class Users extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "name", nullable = false)
    private String name ;

    @Column(name = "birth")
    private String birth ;

    @Column(name = "email", nullable = false)
    private String email ;

    @Column(name = "phone_number")
    private String phoneNumber ;

    @Column(name = "password")
    private String password ;

    @Column(name = "login_type")
    private String loginType ;

    @Column(name = "activated", nullable = false)
    private boolean activated = true;
}
