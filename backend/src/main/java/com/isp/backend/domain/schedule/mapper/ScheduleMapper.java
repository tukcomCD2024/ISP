package com.isp.backend.domain.schedule.mapper;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.dto.request.DailySchedule;
import com.isp.backend.domain.schedule.dto.request.ScheduleDetailRequest;
import com.isp.backend.domain.schedule.dto.response.FastestScheduleResponse;
import com.isp.backend.domain.schedule.dto.response.LatestCreateResponse;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.dto.request.ScheduleSaveRequest;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public Schedule toScheduleEntity(ScheduleSaveRequest scheduleSaveRequest, Member member, Country country) {
        // 여행 일정 엔티티 생성
        Schedule schedule = Schedule.builder()
                .scheduleName(scheduleSaveRequest.getScheduleName())
                .country(country)
                .startDate(scheduleSaveRequest.getStartDate())
                .endDate(scheduleSaveRequest.getEndDate())
                .member(member)
                .build();

        // 여행 일정 세부 항목 리스트 생성
        List<ScheduleDetail> scheduleDetails = scheduleSaveRequest.getDailySchedules().stream()
                .flatMap(dailySchedule -> {
                    AtomicInteger num = new AtomicInteger(1); // 날짜별 일정 순서 카운터
                    return dailySchedule.getSchedules().stream()
                            .map(scheduleDetailDTO -> toScheduleDetailEntity(scheduleDetailDTO, dailySchedule, schedule, num.getAndIncrement()));
                })
                .collect(Collectors.toList());

        // 일정 세부를 일정 엔티티에 설정
        schedule.setScheduleDetails(scheduleDetails);

        return schedule;
    }

    // 일정 세부 DTO를 엔티티로 변환
    private ScheduleDetail toScheduleDetailEntity(ScheduleDetailRequest scheduleDetailDTO, DailySchedule dailySchedule, Schedule schedule, int num) {
        ScheduleDetail scheduleDetail = ScheduleDetail.builder()
                .todo(scheduleDetailDTO.getTodo())
                .place(scheduleDetailDTO.getPlace())
                .budget(scheduleDetailDTO.getBudget())
                .latitude(scheduleDetailDTO.getLatitude())
                .longitude(scheduleDetailDTO.getLongitude())
                .date(dailySchedule.getDate())
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
    public ScheduleListResponse toScheduleListResponseDTO(Schedule schedule) {
        return new ScheduleListResponse(
                schedule.getId(),
                schedule.getScheduleName(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getTotalPrice(),
                schedule.getCountry().getImageUrl(),
                schedule.getCountry().getCity(),
                schedule.getCountry().getLatitude(),
                schedule.getCountry().getLongitude(),
                schedule.getCountry().getCurrencyName()
        );
    }


    /**
     * 일정 상세 조회
     **/
    // 엔티티 -> ScheduleSaveRequestDTO 로 변환
    public ScheduleSaveRequest toScheduleResponseDTO(Schedule schedule) {
        // 일정 세부를 날짜별로 그룹화하고, 날짜를 기준으로 정렬
        List<DailySchedule> sortedDailySchedules = schedule.getScheduleDetails().stream()
                .sorted(Comparator.comparing(ScheduleDetail::getDate))
                .collect(Collectors.groupingBy(ScheduleDetail::getDate))
                .entrySet().stream()
                .map(entry -> new DailySchedule(
                        entry.getKey(),
                        entry.getValue().stream()
                                .sorted(Comparator.comparingInt(ScheduleDetail::getNum))
                                .map(this::toScheduleDetailDTO)
                                .collect(Collectors.toList())
                ))
                .sorted(Comparator.comparing(DailySchedule::getDate))
                .collect(Collectors.toList());

        return new ScheduleSaveRequest(
                schedule.getScheduleName(),
                schedule.getCountry().getCity(), // 국가에서 도시 가져오기로 수정
                schedule.getStartDate(),
                schedule.getEndDate(),
                sortedDailySchedules
        );
    }

    // ScheduleDetail 엔티티를 ScheduleDetailDTO로 변환하는 메서드
    private ScheduleDetailRequest toScheduleDetailDTO(ScheduleDetail scheduleDetail) {
        ScheduleDetailRequest scheduleDetailDTO = new ScheduleDetailRequest(
                scheduleDetail.getTodo(),
                scheduleDetail.getPlace(),
                scheduleDetail.getScheduleType(),
                scheduleDetail.getBudget(),
                scheduleDetail.getLatitude(),
                scheduleDetail.getLongitude()
        );
        return scheduleDetailDTO;
    }


    /**
     * 일정 수정
     **/
    // ScheduleDetailDTO 목록을 ScheduleDetail 엔티티 목록으로 변환하는 메서드
    public List<ScheduleDetail> updateSchedulesEntity(ScheduleSaveRequest scheduleSaveRequest, Schedule schedule) {
        return scheduleSaveRequest.getDailySchedules().stream()
                .flatMap(dailySchedule -> {
                    AtomicInteger num = new AtomicInteger(1); // 날짜별 일정 순서 카운터
                    return dailySchedule.getSchedules().stream()
                            .map(scheduleDetail -> toScheduleDetailEntity(scheduleDetail, dailySchedule, schedule, num.getAndIncrement()));
                })
                .collect(Collectors.toList());
    }


    /**
     *  D-day
     **/
    public FastestScheduleResponse toFastestScheduleDTO(Schedule schedule) {
        FastestScheduleResponse response = new FastestScheduleResponse();
        response.setId(schedule.getId());
        response.setScheduleName(schedule.getScheduleName());
        response.setDday(calculateDday(schedule.getStartDate()));
        response.setCountry(schedule.getCountry().getCity());
        response.setImageUrl(schedule.getCountry().getImageUrl());

        return response;
    }


    /**
     * D-day 계산 메서드
     **/
    private String calculateDday(String startDateString) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.parse(startDateString);
        long dday = ChronoUnit.DAYS.between(today, startDate) + 1; // 오늘-시작일까지의 날짜 수 계산
        return Long.toString(dday);
    }



    /**
     * 최근에 생성한 여행 일정 top 5
     **/
    public LatestCreateResponse toLatestCreateResponse(Schedule schedule, int limit) {
        List<String> limitedPlan = schedule.getScheduleDetails().stream()
                .sorted(Comparator.comparing(ScheduleDetail::getDate)
                        .thenComparing(ScheduleDetail::getNum))
                .map(ScheduleDetail::getTodo)
                .limit(limit) // 최대 개수로 제한
                .collect(Collectors.toList());

        return new LatestCreateResponse(
                schedule.getId(),
                schedule.getScheduleName(),
                schedule.getCountry().getCity(), // 국가에서 도시 가져오기로 수정
                schedule.getCountry().getImageUrl(), // 이미지 URL 추가 (엔티티에 이 필드가 있다고 가정)
                limitedPlan
        );
    }



}


