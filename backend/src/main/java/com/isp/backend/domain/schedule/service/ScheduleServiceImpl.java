package com.isp.backend.domain.schedule.service;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.member.repository.MemberRepository;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.dto.request.ScheduleSaveRequest;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.mapper.ScheduleMapper;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.scheduleDetail.entity.ScheduleDetail;
import com.isp.backend.domain.scheduleDetail.repository.ScheduleDetailRepository;
import com.isp.backend.global.exception.member.MemberNotFoundException;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import com.isp.backend.global.exception.schedule.NotYourScheduleException;
import com.isp.backend.global.exception.schedule.ScheduleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDetailRepository scheduleDetailRepository;
    private final CountryRepository countryRepository;
    private final MemberRepository memberRepository;
    private final ScheduleMapper scheduleMapper;
    
    /**
     * 여행 일정 저장 API
     */
    @Transactional
    @Override
    public void saveSchedule(String uid, ScheduleSaveRequest scheduleSaveRequest) {
        Member findMember = validateUserCheck(uid);
        Country findCountry = validateCountry(scheduleSaveRequest.getCountry());
        // 여행 일정 저장
        Schedule schedule = scheduleMapper.toScheduleEntity(scheduleSaveRequest, findMember, findCountry);
        // 여행 전체 일정 총 경비 저장
        calculateTotalPrice(schedule);

        scheduleRepository.save(schedule);
    }


    /**
     * 여행 일정 목록 조회 API
     */
    @Override
    public List<ScheduleListResponse> getScheduleList(String uid) {
        Member findMember = validateUserCheck(uid);
        // 내가 쓴 일정 불러오기
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByMember(findMember);


        return scheduleList.stream()
                .map(scheduleMapper::toScheduleListResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * 여행 일정 상세 조회 API
     */
    @Override
    public ScheduleSaveRequest getScheduleDetail(String uid, Long scheduleId) {
        Member findMember = validateUserCheck(uid);
        Schedule findSchedule = validateSchedule(scheduleId);

        return scheduleMapper.toScheduleResponseDTO(findSchedule);
    }


    /**
     * 여행 일정 삭제 API
     */
    @Transactional
    @Override
    public void deleteSchedule(String uid, Long scheduleId) {
        Member findMember = validateUserCheck(uid);
        Schedule findSchedule = validateSchedule(scheduleId);

        // 자신의 여행 일정인지 확인
        if (!findSchedule.getMember().equals(findMember)) {
            throw new NotYourScheduleException();
        }

        // ScheduleDetail 테이블 데이터 삭제
        scheduleDetailRepository.deleteBySchedule(findSchedule);
        findSchedule.setActivated(false);

        scheduleRepository.save(findSchedule);
    }


    /**
     * 여행 일정 수정 API
     */
    @Transactional
    @Override
    public ScheduleSaveRequest updateSchedule(String uid, Long scheduleId, ScheduleSaveRequest updateRequestDTO) {
        Member findMember = validateUserCheck(uid);
        Country findCountry = validateCountry(updateRequestDTO.getCountry());
        Schedule findSchedule = validateSchedule(scheduleId);

        // 자신의 여행 일정인지 확인
        if (!findSchedule.getMember().equals(findMember)) {
            throw new NotYourScheduleException();
        }

        scheduleDetailRepository.deleteBySchedule(findSchedule);

        // 기존의 일정 엔티티를 수정
        findSchedule.setScheduleName(updateRequestDTO.getScheduleName());
        findSchedule.setCountry(findCountry);
        findSchedule.setStartDate(updateRequestDTO.getStartDate());
        findSchedule.setEndDate(updateRequestDTO.getEndDate());

        // 새로운 일정 세부 정보 추가
        List<ScheduleDetail> scheduleDetails = scheduleMapper.updateSchedulesEntity(updateRequestDTO, findSchedule);
        findSchedule.setScheduleDetails(scheduleDetails);

        // 총경비 재 계산 후 저장
        calculateTotalPrice(findSchedule);
        scheduleRepository.save(findSchedule);

        // 수정된 일정을 반환 -- 없애도 됨 public void fh
        return updateRequestDTO;
    }


    /**  여행 일정 총 경비 계산 **/
    @Override
    public void calculateTotalPrice(Schedule schedule) {
        double totalPrice = schedule.getScheduleDetails().stream()
                .mapToDouble(ScheduleDetail::getBudget)
                .sum();
        schedule.setTotalPrice(totalPrice);
    }


    /**  유저 정보 확인 **/
    @Override
    public Member validateUserCheck(String uid) {
        Member findMember = memberRepository.findByUid(uid);
        if (findMember == null) {
            throw new MemberNotFoundException();
        }
        return findMember;
    }


    /**  여행 일정 검증 **/
    @Override
    public Schedule validateSchedule(Long scheduleId) {
        // 여행 일정 찾기
        Schedule findSchedule = scheduleRepository.findByIdAndActivatedIsTrue(scheduleId);
        if (findSchedule == null) {
            throw new ScheduleNotFoundException();
        }
        return findSchedule;
    }


    /** 여행 국가 검증 **/
    @Override
    public Country validateCountry(String countryName) {
        Country findCountry = countryRepository.findIdByCity(countryName);
        if (findCountry == null) {
            throw new CountryNotFoundException();
        }
        return findCountry;
    }




}
