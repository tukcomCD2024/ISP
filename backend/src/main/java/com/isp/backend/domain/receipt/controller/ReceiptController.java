package com.isp.backend.domain.receipt.controller;

import com.isp.backend.domain.receipt.dto.request.ChangeReceiptOrderRequest;
import com.isp.backend.domain.receipt.dto.request.SaveReceiptRequest;
import com.isp.backend.domain.receipt.dto.response.ReceiptResponse;
import com.isp.backend.domain.receipt.dto.response.ScheduleListWithReceiptResponse;
import com.isp.backend.domain.receipt.dto.response.ScheduleReceiptResponse;
import com.isp.backend.domain.receipt.entity.Receipt;
import com.isp.backend.domain.receipt.service.ReceiptService;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;


    /** 영수증 저장 API **/
    @PostMapping
    public ResponseEntity<Long> saveReceipt( @RequestPart("request") SaveReceiptRequest request,
                                             @RequestPart(value = "receiptImg", required = false) MultipartFile receiptImg) {
        Long receiptId = receiptService.saveReceipt(request, receiptImg);
        return ResponseEntity.status(HttpStatus.CREATED).body(receiptId);
    }


    /** 영수증 삭제 API **/
    @DeleteMapping("/{receiptId}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long receiptId) {
        receiptService.deleteReceipt(receiptId);
        return ResponseEntity.ok().build();
    }


    /** 영수증 수정 API **/
    @PutMapping("/{receiptId}")
    public ResponseEntity<Long> updateReceipt(@PathVariable Long receiptId,
                                              @RequestPart("request") SaveReceiptRequest request,
                                              @RequestPart(value = "receiptImg", required = false) MultipartFile receiptImg) {
        Long newReceiptId = receiptService.updateReceipt(receiptId, request, receiptImg);
        return ResponseEntity.status(HttpStatus.OK).body(newReceiptId);
    }


    /** 여행 별 영수증 리스트 전체 조회 API **/
    @GetMapping("/{scheduleId}/list")
    public ResponseEntity<ScheduleReceiptResponse> getReceiptList(@PathVariable Long scheduleId) {
        ScheduleReceiptResponse response = receiptService.getReceiptList(scheduleId);
        return ResponseEntity.ok(response);
    }



    /** 영수증 별 상세 내역 조회 API **/
    @GetMapping("/detail/{receiptId}/list")
    public ResponseEntity<ReceiptResponse> getReceiptDetail(@PathVariable Long receiptId) {
        ReceiptResponse receiptResponse = receiptService.getReceiptDetail(receiptId);
        return ResponseEntity.ok(receiptResponse);
    }


    /** 영수증용 일정 리스트 목록 조회 API **/
    @GetMapping("/schedules/list")
    public ResponseEntity<List<ScheduleListWithReceiptResponse>> getScheduleListWithReceipt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String memberUid = userDetails.getUsername();
        List<ScheduleListWithReceiptResponse> scheduleListWithReceipts = receiptService.getScheduleListWithReceipt(memberUid);
        return ResponseEntity.ok(scheduleListWithReceipts);
    }



    /** 영수증 순서 변경 API **/
    @PutMapping("/order/{scheduleId}")
    public ResponseEntity<Void> changeOrderReceipt(@PathVariable Long scheduleId,
                                                   @RequestBody List<ChangeReceiptOrderRequest> changeRequests) {
        receiptService.changeOrderReceipt(scheduleId, changeRequests);
        return ResponseEntity.ok().build();
    }



    /** test - 영수증 사진 저장 API **/
    @PostMapping("/{receiptId}/image")
    public ResponseEntity<Receipt> saveReceiptImg(@PathVariable Long receiptId,
                                                  @RequestParam("receiptImg") MultipartFile receiptImg) {
        receiptService.saveReceiptImg(receiptId, receiptImg);
        return ResponseEntity.ok().build();
    }


}
