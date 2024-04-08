package com.isp.backend.domain.member.service;

import com.isp.backend.domain.member.dto.request.AuthRecreateRequest;
import com.isp.backend.domain.member.dto.request.GoogleLoginRequest;
import com.isp.backend.domain.member.dto.response.MemberDetailResponse;
import com.isp.backend.domain.member.dto.request.SignUpRequest;
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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    /** 로그인 메서드 - jwt 토큰 생성후 응답 **/
    @Transactional
    @Override
    public ResponseEntity<String> memberLogin(GoogleLoginRequest request) {
        Optional<Member> existingMemberOptional = memberRepository.findByUid(request.getUid());

        if (existingMemberOptional.isPresent()) {
            Member existingMember = existingMemberOptional.get();
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
    public ResponseEntity<String> handleExistingMemberLogin(Member existingMember) {
        String accessToken = tokenProvider.createAccessToken(existingMember.getUid());
        String refreshToken = tokenProvider.createRefreshToken(existingMember.getUid());
        return ResponseEntity.ok()
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken)
                .body("기존 회원 로그인");
    }



    /** 신규 회원의 로그인 -> DB 저장 **/
    @Override
    public ResponseEntity<String> handleNewMemberLogin(GoogleLoginRequest request) {
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



    /** 회원가입 - 신규 유저의 경우 추가 정보 저장 **/
    @Transactional
    @Override
    public void signUp(SignUpRequest signUpRequest, String memberUid) {
        Member findMember = memberRepository.findByUid(memberUid)
                .orElseThrow(() -> new MemberNotFoundException());
        signUpRequest.toEntity(findMember);
        memberRepository.save(findMember);
    }



    /** 토큰 재발행 **/
    @Transactional
    @Override
    public ResponseEntity<String> authRecreate(AuthRecreateRequest authRecreateRequest) {

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



    /** 멤버 정보 조회 **/
    @Override
    public MemberDetailResponse getMemberInfo (String uid) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);
        return(MemberDetailResponse.fromEntity(member));
    }



}
