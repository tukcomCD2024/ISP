package com.isp.backend.domain.member.service;

import com.isp.backend.domain.member.dto.AuthRecreateRequest;
import com.isp.backend.domain.member.dto.GoogleLoginRequest;
import com.isp.backend.domain.member.dto.MemberDetailResponse;
import com.isp.backend.domain.member.dto.SignUpRequest;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.member.repository.MemberRepository;
import com.isp.backend.global.exception.member.AuthenticationFailedException;
import com.isp.backend.global.exception.member.MemberNotActivatedException;
import com.isp.backend.global.exception.member.MemberNotFoundException;
import com.isp.backend.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     * 로그인 메서드 - jwt 토큰 생성후 응답
     */
    @Transactional
    @Override
    public String memberLogin(GoogleLoginRequest request) {
        Member existingMember = memberRepository.findByUid(request.getUid());

        if (existingMember != null) {
            if (existingMember.isActivated()) {
                return handleExistingMemberLogin(existingMember);
            } else {
                throw new MemberNotActivatedException();
            }
        } else {
            return handleNewMemberLogin(request);
        }
    }


    /** 기존 회원의 로그인 **/
    @Override
    public String handleExistingMemberLogin(Member existingMember) {
        String accessToken = tokenProvider.createAccessToken(existingMember.getUid());
        String refreshToken = tokenProvider.createRefreshToken(existingMember.getUid());
        return "기존 회원 로그인";
    }



    /** 신규 회원의 로그인 -> DB 저장 **/
    @Override
    public String handleNewMemberLogin(GoogleLoginRequest request) {
        Member newMember = Member.builder()
                .uid(request.getUid())
                .loginType("google")
                .build();
        memberRepository.save(newMember);

        String accessToken = tokenProvider.createAccessToken(newMember.getUid());
        String refreshToken = tokenProvider.createRefreshToken(newMember.getUid());

        return "신규 회원 로그인";
    }


    /**
     * 회원가입 - 신규 유저의 경우 추가 정보 저장
     */
    @Transactional
    @Override
    public void signUp(SignUpRequest signUpRequest, String memberUid) {
        Member findMember = memberRepository.findByUid(memberUid);
        if (findMember == null) {
            throw new MemberNotFoundException();
        }

        signUpRequest.toEntity(findMember);
        memberRepository.save(findMember);
    }


    /**
     * 토큰 재발행
     */
    @Transactional
    @Override
    public ResponseEntity<String> authRecreate(AuthRecreateRequest authRecreateRequest) {

        if (!tokenProvider.validateRefreshToken(authRecreateRequest.getRefreshToken())) {
            throw new AuthenticationFailedException();
        }
        String uid = tokenProvider.getUid(authRecreateRequest.getRefreshToken());
        Member member = memberRepository.findByUidAndActivatedIsTrue(uid);
        if (member == null) {
            throw new MemberNotFoundException();
        }
        String accessToken = tokenProvider.createAccessToken(member.getUid());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid());

        return ResponseEntity.ok()
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken)
                .body(null);
    }


    /**
     * 멤버 정보 조회
     */
    @Override
    public MemberDetailResponse getMemberInfo(String uid) {

        Member findMember = memberRepository.findByUid(uid);
        if (findMember == null) {
            throw new MemberNotFoundException();
        }

        return (MemberDetailResponse.fromEntity(findMember));
    }


}
