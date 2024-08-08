package com.isp.backend.domain.receipt.service;

import com.isp.backend.domain.receipt.dto.request.ReceiptDetailRequest;
import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import com.isp.backend.domain.receipt.mapper.ReceiptMapper;
import com.isp.backend.domain.receipt.repository.ReceiptDetailRepository;
import com.isp.backend.domain.receipt.repository.ReceiptRepository;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.global.exception.Image.ImageAlreadyExistingException;
import com.isp.backend.global.exception.receipt.ReceiptNotFoundException;
import com.isp.backend.global.exception.schedule.ScheduleNotFoundException;
import com.isp.backend.global.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptDetailRepository receiptDetailRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReceiptMapper receiptMapper;

    private final S3ImageService s3ImageService;


    /**
     * 영수증 저장 API
     **/
    @Transactional
    public Long saveReceipt(SaveReceiptRequest request) {
        // 일정 정보 확인
        Schedule findSchedule = validateSchedule(request.getScheduleId());

        // 데이터 변환 및 저장
        Receipt receipt = receiptMapper.toEntity(request, findSchedule);
        receiptRepository.save(receipt);

        for (ReceiptDetailRequest detailRequest : request.getReceiptDetails()) {
            ReceiptDetail detail = receiptMapper.toEntity(detailRequest, receipt);
            receiptDetailRepository.save(detail);
        }

        return receipt.getId();
    }


    /** 영수증 이미지 저장 API **/
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


    // 유효한 일정 확인 메소드
    private Schedule validateSchedule(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findByIdAndActivatedIsTrue(scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);
        return findSchedule;
    }

    // 유효한 영수증 확인 메서드
    private Receipt validateReceipt(Long receiptId){
        Receipt findReceipt = receiptRepository.findById(receiptId)
                .orElseThrow(ReceiptNotFoundException::new);
        return findReceipt;
    }


}
