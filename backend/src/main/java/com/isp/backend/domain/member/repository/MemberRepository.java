package com.isp.backend.domain.member.repository;

import com.isp.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUidAndActivatedIsTrue(String Uid);

    Optional<Member> findByUid(String Uid);

}
