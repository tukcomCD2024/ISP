package com.isp.backend.domain.receipt.service;

import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.member.repository.MemberRepository;
import com.isp.backend.domain.receipt.dto.request.ChangeReceiptOrderRequest;
import com.isp.backend.domain.receipt.dto.request.ReceiptDetailRequest;
import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.dto.response.*;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import com.isp.backend.domain.receipt.mapper.ReceiptMapper;
import com.isp.backend.domain.receipt.repository.ReceiptDetailRepository;
import com.isp.backend.domain.receipt.repository.ReceiptRepository;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.global.exception.Image.ImageAlreadyExistingException;
import com.isp.backend.global.exception.common.MemberNotFoundException;
import com.isp.backend.global.exception.receipt.ReceiptNotFoundException;
import com.isp.backend.global.exception.schedule.ScheduleNotFoundException;
import com.isp.backend.global.s3.constant.S3BucketDirectory;
import com.isp.backend.global.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final MemberRepository memberRepository ;
    private final ScheduleRepository scheduleRepository;
    private final ReceiptMapper receiptMapper;
    private final S3ImageService s3ImageService;


    /** 영수증 저장 API **/
    @Transactional
    public Long saveReceipt(SaveReceiptRequest request,MultipartFile receiptImg) {
        // 일정 정보 확인
        Schedule findSchedule = validateSchedule(request.getScheduleId());

        // 동일한 날짜의 가장 큰 orderNum 조회
        int maxOrderNum = receiptRepository.findMaxOrderNumByScheduleIdAndPurchaseDate(
                request.getScheduleId(), request.getPurchaseDate()
        );

        // orderNum 설정 (해당 날짜에 영수증이 없으면 1로 설정, 있으면 maxOrderNum + 1)
        int orderNum = maxOrderNum + 1;

        // 데이터 변환 및 저장
        Receipt receipt = receiptMapper.toEntity(request, findSchedule, orderNum);

        // 영수증 사진 저장
        if (receiptImg != null && !receiptImg.isEmpty()) {
            String receiptImgUrl = s3ImageService.create(receiptImg, "RECEIPT" );
            receipt.setReceiptImg(receiptImgUrl);
        }

        receiptRepository.save(receipt);

        for (ReceiptDetailRequest detailRequest : request.getReceiptDetails()) {
            ReceiptDetail detail = receiptMapper.toEntity(detailRequest, receipt);
            receiptDetailRepository.save(detail);
        }

        return receipt.getId();
    }



    /** 영수증 삭제 메소드 **/
    @Transactional
    public void deleteReceipt(Long receiptId) {
        // 유효한 영수증 확인
        Receipt receipt = validateReceipt(receiptId);

        // 영수증에 연결된 세부 내역 삭제
        receiptDetailRepository.deleteAllByReceipt(receipt);

        // 영수증 삭제
        receiptRepository.delete(receipt);
    }



    /** 영수증 수정 메소드 **/
    @Transactional
    public Long updateReceipt(Long receiptId, SaveReceiptRequest request, MultipartFile receiptImg) {
        // 유효한 영수증 확인
        Receipt receipt = validateReceipt(receiptId);

        // 영수증 정보 업데이트
        receipt.setStoreName(request.getStoreName());
        receipt.setStoreType(receiptMapper.getStoreType(request.getStoreType()));
        receipt.setTotalPrice(request.getTotalPrice());
        receipt.setPurchaseDate(request.getPurchaseDate());

        // 영수증 이미지가 있을 경우 업데이트
        if (receiptImg != null && !receiptImg.isEmpty()) {
            String receiptImgUrl = s3ImageService.create(receiptImg, "RECEIPT");
            receipt.setReceiptImg(receiptImgUrl);
        }

        // 기존 영수증 세부 내역 삭제
        receiptDetailRepository.deleteAllByReceipt(receipt);

        // 새 세부 내역 추가
        for (ReceiptDetailRequest detailRequest : request.getReceiptDetails()) {
            ReceiptDetail detail = receiptMapper.toEntity(detailRequest, receipt);
            receiptDetailRepository.save(detail);
        }

        // 수정된 영수증 정보 저장
        receiptRepository.save(receipt);
        return receipt.getId();
    }



    /** 여행 별 영수증 리스트 전체 조회 메소드 **/
    @Transactional(readOnly = true)
    public ScheduleReceiptResponse getReceiptList(Long scheduleId) {
        // 스케줄 유효성 검사
        Schedule schedule = validateSchedule(scheduleId);
        List<Receipt> receipts = receiptRepository.findByScheduleIdOrderByPurchaseDateAscOrderNumAsc(schedule.getId());

        // 영수증의 합계 구하기
        double totalReceiptsPrice = receipts.stream()
                .mapToDouble(Receipt::getTotalPrice)
                .sum();

        // 영수증들 매핑
        List<ReceiptListResponse> receiptList = receipts.stream()
                .map(receiptMapper::toReceiptListResponse)
                .collect(Collectors.toList());

        return new ScheduleReceiptResponse(
                schedule.getScheduleName(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getCountry().getCurrencyName(),
                totalReceiptsPrice,
                receiptList
        );
    }



    /** 영수증 별 상세 내역 조회 메소드 **/
    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptDetail(Long receiptId) {
        Receipt receipt = validateReceipt(receiptId);

        // 영수증에 연관된 상세 내역 리스트를 조회
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByReceiptId(receiptId);

        // ReceiptDetail을 ReceiptDetailResponse로 변환
        List<ReceiptDetailResponse> receiptDetailResponses = receiptDetails.stream()
                .map(receiptMapper::toReceiptDetailResponse)
                .collect(Collectors.toList());

        // ReceiptResponse DTO 생성 및 반환
        return receiptMapper.toReceiptResponse(receipt, receiptDetailResponses);

    }



    /** 영수증용 일정 리스트 목록 조회 API **/
    @Transactional(readOnly = true)
    public List<ScheduleListWithReceiptResponse> getScheduleListWithReceipt(String uid) {
        // 유저 정보 확인
        Member findMember = validateUserCheck(uid);

        // 해당 유저의 모든 일정 불러오기
        List<Schedule> scheduleList = scheduleRepository.findSchedulesByMember(findMember);

        // 일정 리스트를 DTO로 변환
        return scheduleList.stream()
                .map(schedule -> {
                    // sumTotalPriceByScheduleId가 null을 반환하면 0.0을 기본값으로 설정
                    double totalReceiptsPrice = Optional.ofNullable(receiptRepository.sumTotalPriceByScheduleId(schedule.getId()))
                            .orElse(0.0);
                    int receiptCount = receiptRepository.countByScheduleId(schedule.getId());
                    return receiptMapper.toScheduleListWithReceiptResponseDTO(schedule, totalReceiptsPrice, receiptCount);
                })
                .collect(Collectors.toList());

    }



    // 영수증 순서 변경 메서드 수정 예정
    /** 예외처리 및 정확한 로직 분석 필요 **/
    @Transactional
    public void changeOrderReceipt(Long scheduleId, List<ChangeReceiptOrderRequest> changeRequests) {
        // 해당 스케줄에 존재하는 영수증 전체 목록 조회
        List<Receipt> existingReceipts = receiptRepository.findByScheduleId(scheduleId);

        // 클라이언트가 제공한 정보로 이루어진 Map 생성 (key: purchaseDate, receiptId, value: orderNum)
        Map<String, Map<Long, Integer>> providedReceiptsMap = changeRequests.stream()
                .collect(Collectors.groupingBy(
                        ChangeReceiptOrderRequest::getPurchaseDate,
                        Collectors.toMap(ChangeReceiptOrderRequest::getReceiptId, ChangeReceiptOrderRequest::getOrderNum)
                ));

        // 제공된 모든 receiptId와 날짜가 해당 스케줄의 영수증에 일치하는지 확인
        for (Receipt receipt : existingReceipts) {
            Map<Long, Integer> dateSpecificReceipts = providedReceiptsMap.get(receipt.getPurchaseDate());
            if (dateSpecificReceipts == null || !dateSpecificReceipts.containsKey(receipt.getId())) {
                throw new IllegalArgumentException("모든 영수증 ID와 날짜를 제공해야 합니다.");
            }
        }

        // 날짜별로 orderNum 값이 중복되지 않는지 확인
        for (Map<Long, Integer> receiptMap : providedReceiptsMap.values()) {
            Set<Integer> orderNums = new HashSet<>(receiptMap.values());
            if (orderNums.size() != receiptMap.size()) {
                throw new IllegalArgumentException("날짜별 orderNum 값이 중복됩니다.");
            }
        }

        // orderNum 업데이트
        for (Receipt receipt : existingReceipts) {
            int newOrderNum = providedReceiptsMap.get(receipt.getPurchaseDate()).get(receipt.getId());
            receipt.setOrderNum(newOrderNum);
            receiptRepository.save(receipt);
        }
    }


    /** 유효한 유저 정보 확인 **/
    private Member validateUserCheck(String uid) {
        return memberRepository.findByUid(uid)
                .orElseThrow(MemberNotFoundException::new);
    }



    /** 유효한 일정 확인 메소드 **/
    private Schedule validateSchedule(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findByIdAndActivatedIsTrue(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);
        return findSchedule;
    }


    /** 유효한 영수증 확인 메서드**/
    private Receipt validateReceipt(Long receiptId){
        Receipt findReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(ReceiptNotFoundException::new);
        return findReceipt;
    }


    /** test - 영수증 이미지 저장 API **/
    @Transactional
    public void saveReceiptImg(Long receiptId, MultipartFile receiptImg) {
        // 영수증 정보 확인
        Receipt receipt = validateReceipt(receiptId);

        // 영수증 사진 url이 이미 db에 있는지 여부 확인
        if (receipt.getReceiptImg() != null && !receipt.getReceiptImg().isEmpty()) {
            throw new ImageAlreadyExistingException();
        }

        // 영수증 사진 저장
        if (receiptImg != null && !receiptImg.isEmpty()) {
            String receiptImgUrl = s3ImageService.create(receiptImg, "RECEIPT" );
            receipt.setReceiptImg(receiptImgUrl);
            receiptRepository.save(receipt);
        }

    }


}
