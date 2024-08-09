package com.isp.backend.domain.receipt.mapper;

import com.isp.backend.domain.receipt.dto.request.ReceiptDetailRequest;
import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.dto.response.ReceiptDetailResponse;
import com.isp.backend.domain.receipt.dto.response.ReceiptListResponse;
import com.isp.backend.domain.receipt.dto.response.ReceiptResponse;
import com.isp.backend.domain.receipt.dto.response.ScheduleListWithReceiptResponse;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import com.isp.backend.domain.receipt.entity.StoreType;
import com.isp.backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiptMapper {

    // 영수증 저장
    public Receipt toEntity(SaveReceiptRequest request, Schedule schedule, int orderNum) {
        StoreType storeType = getStoreType(request.getStoreType());

        return Receipt.builder()
                .schedule(schedule)
                .storeName(request.getStoreName())
                .storeType(storeType)
                .totalPrice(request.getTotalPrice())
                .purchaseDate(request.getPurchaseDate())
                .orderNum(orderNum)
                .build();
    }

    public ReceiptDetail toEntity(ReceiptDetailRequest request, Receipt receipt) {
        return ReceiptDetail.builder()
                .receipt(receipt)
                .item(request.getItem())
                .count(request.getCount())
                .itemPrice(request.getItemPrice())
                .build();
    }


    // 올바른 영수증 타입을 갖는지 확인
    public StoreType getStoreType(String storeType) {
        try {
            return StoreType.valueOf(storeType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid store type: " + storeType);
        }
    }


    // 영수증 목록 조회
    public ReceiptListResponse toReceiptListResponse(Receipt receipt) {
        return new ReceiptListResponse(
                receipt.getId(),
                receipt.getStoreName(),
                receipt.getStoreType().name(),
                receipt.getTotalPrice(),
                receipt.getOrderNum(),
                receipt.getPurchaseDate()
        );
    }


    // 영수증 상세 조회
    public ReceiptResponse toReceiptResponse(Receipt receipt, List<ReceiptDetailResponse> receiptDetailResponses) {
        return new ReceiptResponse(
                receipt.getId(),
                receipt.getPurchaseDate(),
                receipt.getOrderNum(),
                receipt.getReceiptImg(),
                receipt.getTotalPrice(),
                receiptDetailResponses
        );
    }

    public ReceiptDetailResponse toReceiptDetailResponse(ReceiptDetail receiptDetail) {
        return new ReceiptDetailResponse(
                receiptDetail.getItem(),
                receiptDetail.getCount(),
                receiptDetail.getItemPrice()
        );
    }


    // 영수증용 스케줄 목록 조회
    public ScheduleListWithReceiptResponse toScheduleListWithReceiptResponseDTO(Schedule schedule, double totalReceiptsPrice, int receiptCount) {
        String currencyName = schedule.getCountry().getCurrencyName();

        return new ScheduleListWithReceiptResponse(
                schedule.getScheduleName(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                currencyName,
                totalReceiptsPrice,
                receiptCount
        );
    }



}

