package com.isp.backend.domain.member.repository;

import com.isp.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmailAndActivatedIsTrue(String email);

}
