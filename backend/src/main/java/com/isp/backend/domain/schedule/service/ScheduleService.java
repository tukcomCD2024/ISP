package com.isp.backend.domain.schedule.service;

import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import lombok.extern.slf4j.Slf4j;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.member.repository.MemberRepository;
import com.isp.backend.domain.schedule.dto.ScheduleListResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleRequestDTO;
import com.isp.backend.domain.schedule.dto.ScheduleResponseDTO;
import com.isp.backend.domain.schedule.dto.ScheduleSaveRequestDTO;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.mapper.ScheduleMapper;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.domain.scheduleDetail.repository.ScheduleDetailRepository;
import com.isp.backend.global.exception.member.MemberNotFoundException;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository ;
    private final CountryRepository countryRepository;
    private final MemberRepository memberRepository ;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleResponseDTO createSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        // 프롬프트 생성하고
        // schedule 일정 생성하기 openai 써서
        return null;
    }

    /**
     * 여행 일정 저장 API
     */
    @Transactional
    public void saveSchedule(String uid, ScheduleSaveRequestDTO scheduleSaveRequestDTO) {
        // 유저 정보 확인
        Member findmember = memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);

        // 여행할 국가 확인
        Country findCountry = countryRepository.findIdByCity(scheduleSaveRequestDTO.getCountry());
        if(findCountry == null) {
            throw new CountryNotFoundException();
        }

        // 여행 일정 저장
        Schedule schedule = scheduleMapper.toSchedulesEntity(scheduleSaveRequestDTO, findmember, findCountry);

        // 여행 전체 일정 총 경비 저장
        calculateTotalPrice(schedule);

        scheduleRepository.save(schedule);
    }


    /**
     * 여행 일정 목록 조회 API
     */
    @Transactional(readOnly = true)
    public List<ScheduleListResponseDTO> getScheduleList(String uid) {
        // 유저 정보 확인
        Member findmember = memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);
        // 내가 쓴 일정 불러오기
        List<Schedule> scheduleList = scheduleRepository.findByMember(findmember);

        return scheduleList.stream()
                .map(scheduleMapper::toScheduleListResponseDTO)
                .collect(Collectors.toList());
    }


    /*
     * 여행 일정 총 경비 계산
     */
    private void calculateTotalPrice(Schedule schedule) {
        double totalPrice = schedule.getScheduleDetails().stream()
                .mapToDouble(ScheduleDetail::getBudget)
                .sum();
        schedule.setTotalPrice(totalPrice);
    }
}
