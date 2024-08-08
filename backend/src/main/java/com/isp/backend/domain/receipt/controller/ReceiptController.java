package com.isp.backend.domain.receipt.controller;

import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;


    /** 영수증 저장 API **/
    @PostMapping("/image")
    public ResponseEntity<Long> saveReceipt(@RequestBody SaveReceiptRequest request) {
        Long receiptId = receiptService.saveReceipt(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(receiptId);
    }

    /** 영수증 사진 저장 API **/
    @PostMapping("/{receiptId}/image")
    public ResponseEntity<Receipt> saveReceiptImg(@PathVariable Long receiptId,
                                                  @RequestParam("receiptImg") MultipartFile receiptImg) {
        receiptService.saveReceiptImg(receiptId, receiptImg);
        return ResponseEntity.ok().build();
    }


    /** 영수증 삭제 API **/

    /** 영수증 수정 API **/

    /** 영수증 별 상세 내역 조회 API **/

    /** 여행 별 영수증 리스트 전체 조회 API **/

}
