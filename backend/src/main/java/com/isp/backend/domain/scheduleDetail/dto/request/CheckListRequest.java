package com.isp.backend.domain.scheduleDetail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckListRequest {

    private String todo;

    private boolean check;

}
