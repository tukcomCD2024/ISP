package com.isp.backend.domain.member.service;

import com.isp.backend.domain.member.dto.AuthRecreateRequest;
import com.isp.backend.domain.member.dto.GoogleLoginRequest;
import com.isp.backend.domain.member.dto.MemberDetailResponse;
import com.isp.backend.domain.member.dto.SignUpRequest;
import com.isp.backend.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    String memberLogin(GoogleLoginRequest request);

    void signUp(SignUpRequest signUpRequest, String memberUid);

    String handleExistingMemberLogin(Member existingMember);

    String handleNewMemberLogin(GoogleLoginRequest request);

    ResponseEntity<String> authRecreate(AuthRecreateRequest authRecreateRequest);

    MemberDetailResponse getMemberInfo(String uid);
}
