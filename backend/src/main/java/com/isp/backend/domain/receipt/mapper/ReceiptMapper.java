package com.isp.backend.domain.receipt.mapper;

import com.isp.backend.domain.receipt.dto.request.ReceiptDetailRequest;
import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.entity.ReceiptDetail;
import com.isp.backend.domain.receipt.entity.StoreType;
import com.isp.backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptMapper {

    // 영수증 저장
    public Receipt toEntity(SaveReceiptRequest request, Schedule schedule) {
        StoreType storeType = getStoreType(request.getStoreType());

        return Receipt.builder()
                .schedule(schedule)
                .storeName(request.getStoreName())
                .storeType(storeType)
                .totalPrice(request.getTotalPrice())
                .purchaseDate(request.getPurchaseDate())
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

    private StoreType getStoreType(String storeType) {
        try {
            return StoreType.valueOf(storeType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid store type: " + storeType);
        }
    }

}

