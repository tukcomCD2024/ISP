package com.isp.backend.domain.member.dto;

import com.isp.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpRequest {

    private String name;

    private String birth;

    private String phoneNumber;


    public void toEntity(Member member) {
        member.setName(this.name);
        member.setBirth(this.birth);
        member.setPhoneNumber(this.phoneNumber);
    }

}
