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

import java.util.Optional;

//import java.util.Optional;

@RequiredArgsConstructor
@Service

public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    /**
     *   로그인 메서드 - jwt 토큰 생성후 응답
     */
    @Transactional
    public ResponseEntity<String> memberLogin(GoogleLoginRequest request) {
        Optional<Member> existingMember = memberRepository.findByUid(request.getUid());

        if (existingMember.isPresent()) {

            Member activatedMember = existingMember
                    .filter(Member::isActivated)
                    .orElseThrow(MemberNotActivatedException::new);

            // 기존 회원의 로그인
            String accessToken = tokenProvider.createAccessToken(activatedMember.getUid());
            String refreshToken = tokenProvider.createRefreshToken(activatedMember.getUid());

            return ResponseEntity.ok()
                    .header("Access-Token", accessToken)
                    .header("Refresh-Token", refreshToken)
                    .body("기존 회원 로그인");
        } else {
            // 신규 회원의 로그인 - db 저장
            Member newMember = Member.builder()
                    .uid(request.getUid())
                    .loginType("google")
                    .build();
            memberRepository.save(newMember);

            String accessToken = tokenProvider.createAccessToken(newMember.getUid());
            String refreshToken = tokenProvider.createRefreshToken(newMember.getUid());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Access-Token", accessToken)
                    .header("Refresh-Token", refreshToken)
                    .body("신규 회원 로그인");
            }

    }



    /**
     *  회원가입 - 신규 유저의 경우 추가 정보 저장
     */
    @Transactional
    public void signUp(SignUpRequest signUpRequest, String memberUid) {
        Member member = memberRepository.findByUid(memberUid)
                .orElseThrow(() -> new MemberNotFoundException());

        signUpRequest.toEntity(member);
        memberRepository.save(member);
    }



    /**
     *  토큰 재발행
     */
    @Transactional
    public ResponseEntity<String> authRecreate (AuthRecreateRequest authRecreateRequest) {

        if (!tokenProvider.validateRefreshToken(authRecreateRequest.getRefreshToken())) {
            throw new AuthenticationFailedException();
        }
        String uid = tokenProvider.getUid(authRecreateRequest.getRefreshToken());
        Member member = memberRepository.findByUidAndActivatedIsTrue(uid)
                .orElseThrow(MemberNotFoundException::new);

        String accessToken = tokenProvider.createAccessToken(member.getUid());
        String refreshToken = tokenProvider.createRefreshToken(member.getUid());

        return ResponseEntity.ok()
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken)
                .body(null);
    }


    /**
     *  멤버 정보 조회
     */
    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberInfo (String uid) {

        Member member = memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);

        return(MemberDetailResponse.fromEntity(member));
    }



}
