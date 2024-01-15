package com.isp.backend.domain.member.dto;

import com.isp.backend.domain.member.entity.Member;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDetailResponse {

    private final String uid ;

    private final String name ;

    private final String birth ;

    private final String phoneNumber ;


    public static MemberDetailResponse fromEntity(Member member){
        return new MemberDetailResponse(
                member.getUid(),
                member.getName(),
                member.getBirth(),
                member.getPhoneNumber());
    }

}
