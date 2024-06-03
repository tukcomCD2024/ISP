package com.isp.backend.domain.scheduleDetail.dto.response;

import com.isp.backend.domain.scheduleDetail.entity.CheckList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckListResponse {
    private Long id;
    private String todo;
    private boolean check;

    public CheckListResponse(CheckList checkList) {
        this.id = checkList.getId();
        this.todo = checkList.getTodo();
        this.check = checkList.isChecked();
    }
}

