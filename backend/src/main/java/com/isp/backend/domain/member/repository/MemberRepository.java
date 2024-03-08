package com.isp.backend.domain.member.repository;

import com.isp.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUidAndActivatedIsTrue(String Uid);

    Member findByUid(String Uid);

    boolean existsByUid(String Uid);

}
