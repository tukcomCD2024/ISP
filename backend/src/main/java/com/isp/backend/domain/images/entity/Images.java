package com.isp.backend.domain.images.entity;

import com.isp.backend.domain.schedules.entity.MySchedules;
import com.isp.backend.domain.users.entity.Users;
import com.isp.backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="images")
public class Images extends BaseEntity {

    @Id
    @Column(name="id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private MySchedules mySchedules;

    @Column(name = "image_name")
    private String imageName ;

    @Column(name = "image_url")
    private String imageUrl ;

}
