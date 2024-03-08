package com.isp.backend.domain.schedule.mapper;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.dto.DailyScheduleDTO;
import com.isp.backend.domain.schedule.dto.ScheduleDetailDTO;
import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ScheduleMapper {

    /**
     * 일정 저장
     **/
    // 여행 일정 요청 DTO -> 엔티티로 변환
    public Schedule toScheduleEntity(ScheduleSaveRequestDTO scheduleSaveRequestDTO, Member member, Country country) {
        // 여행 일정 엔티티 생성
        Schedule schedule = Schedule.builder()
                .scheduleName(scheduleSaveRequestDTO.getScheduleName())
                .country(country)
                .startDate(scheduleSaveRequestDTO.getStartDate())
                .endDate(scheduleSaveRequestDTO.getEndDate())
                .member(member)
                .build();

        // 여행 일정 세부 항목 리스트 생성
        List<ScheduleDetail> scheduleDetails = scheduleSaveRequestDTO.getDailySchedules().stream()
                .flatMap(dailyScheduleDTO -> {
                    AtomicInteger num = new AtomicInteger(1); // 날짜별 일정 순서 카운터
                    return dailyScheduleDTO.getSchedules().stream()
                            .map(scheduleDetailDTO -> toScheduleDetailEntity(scheduleDetailDTO, dailyScheduleDTO, schedule, num.getAndIncrement()));
                })
                .collect(Collectors.toList());

        // 일정 세부를 일정 엔티티에 설정
        schedule.setScheduleDetails(scheduleDetails);

        return schedule;
    }

    // 일정 세부 DTO를 엔티티로 변환
    private ScheduleDetail toScheduleDetailEntity(ScheduleDetailDTO scheduleDetailDTO, DailyScheduleDTO dailyScheduleDTO, Schedule schedule, int num) {
        ScheduleDetail scheduleDetail = ScheduleDetail.builder()
                .todo(scheduleDetailDTO.getTodo())
                .place(scheduleDetailDTO.getPlace())
                .budget(scheduleDetailDTO.getBudget())
                .latitude(scheduleDetailDTO.getLatitude())
                .longitude(scheduleDetailDTO.getLongitude())
                .date(dailyScheduleDTO.getDate())
                .num(num) // 일정 순서 저장
                .schedule(schedule)
                .build();

        // ScheduleDetailDTO에서 직접 ScheduleType을 가져와서 설정
        scheduleDetail.setScheduleType(scheduleDetailDTO.getType());

        return scheduleDetail;
    }


    /**
     * 일정 전체 조회
     **/
    public ScheduleListResponseDTO toScheduleListResponseDTO(Schedule schedule) {
        return new ScheduleListResponseDTO(
                schedule.getId(),
                schedule.getScheduleName(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getTotalPrice(),
                schedule.getCountry().getImageUrl(),
                schedule.getCountry().getCity(),
                schedule.getCountry().getLatitude(),
                schedule.getCountry().getLongitude()
        );
    }


    /**
     * 일정 상세 조회
     **/
    // 엔티티 -> ScheduleSaveRequestDTO 로 변환
    public ScheduleSaveRequestDTO toScheduleResponseDTO(Schedule schedule) {
        // 일정 세부를 날짜별로 그룹화하고, 날짜를 기준으로 정렬
        List<DailyScheduleDTO> sortedDailySchedules = schedule.getScheduleDetails().stream()
                .sorted(Comparator.comparing(ScheduleDetail::getDate))
                .collect(Collectors.groupingBy(ScheduleDetail::getDate))
                .entrySet().stream()
                .map(entry -> new DailyScheduleDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .sorted(Comparator.comparingInt(ScheduleDetail::getNum))
                                .map(this::toScheduleDetailDTO)
                                .collect(Collectors.toList())
                ))
                .sorted(Comparator.comparing(DailyScheduleDTO::getDate))
                .collect(Collectors.toList());

        return new ScheduleSaveRequestDTO(
                schedule.getScheduleName(),
                schedule.getCountry().getCity(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                sortedDailySchedules
        );
    }

    // ScheduleDetail 엔티티를 ScheduleDetailDTO로 변환하는 메서드
    private ScheduleDetailDTO toScheduleDetailDTO(ScheduleDetail scheduleDetail) {
        return new ScheduleDetailDTO(
                scheduleDetail.getTodo(),
                scheduleDetail.getPlace(),
                scheduleDetail.getScheduleType(),
                scheduleDetail.getBudget(),
                scheduleDetail.getLatitude(),
                scheduleDetail.getLongitude()
        );
    }


    /**
     * 일정 수정
     **/
    // ScheduleDetailDTO 목록을 ScheduleDetail 엔티티 목록으로 변환하는 메서드
    public List<ScheduleDetail> updateSchedulesEntity(ScheduleSaveRequestDTO scheduleSaveRequestDTO, Schedule schedule) {
        return scheduleSaveRequestDTO.getDailySchedules().stream()
                .flatMap(dailyScheduleDTO -> {
                    AtomicInteger num = new AtomicInteger(1); // 날짜별 일정 순서 카운터
                    return dailyScheduleDTO.getSchedules().stream()
                            .map(scheduleDetailDTO -> toScheduleDetailEntity(scheduleDetailDTO, dailyScheduleDTO, schedule, num.getAndIncrement()));
                })
                .collect(Collectors.toList());
    }


}


