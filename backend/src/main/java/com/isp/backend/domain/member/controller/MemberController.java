package com.isp.backend.domain.member.controller;

import com.isp.backend.domain.member.dto.AuthRecreateRequest;
import com.isp.backend.domain.member.dto.GoogleLoginRequest;
import com.isp.backend.domain.member.dto.MemberDetailResponse;
import com.isp.backend.domain.member.dto.SignUpRequest;
import com.isp.backend.domain.member.service.MemberService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     *  test API
     */
    @GetMapping("/test")
    public ResponseEntity<String> privateEndpoint() {
//        System.out.println("이메일 출력 = " + customUserDetails.getUsername());
//        String member = customUserDetails.getUsername();
        return ResponseEntity.ok("hi");
    }



    /**
    *   로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<String> memberLogin(@RequestBody GoogleLoginRequest request) {
        return memberService.memberLogin(request);
    }


    /**
     *   회원가입 - 추가 정보 API
     */
    @PutMapping("/signup")
    public ResponseEntity<Void> signUp (@RequestBody SignUpRequest request,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String memberUid = customUserDetails.getUsername();
        memberService.signUp(request, memberUid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    /**
     *  토큰 재발행
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> authRecreate (@RequestBody AuthRecreateRequest authRecreateRequest) {
        return memberService.authRecreate(authRecreateRequest);
    }


    /**
     *  멤버 정보 조회
     */
    @GetMapping("/info")
    public ResponseEntity<MemberDetailResponse> getMemberInfo (@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(memberService.getMemberInfo(customUserDetails.getUsername()));
    }







}
