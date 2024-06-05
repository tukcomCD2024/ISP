package com.isp.backend.domain.hotel.controller;

import com.amadeus.exceptions.ResponseException;
import com.isp.backend.domain.hotel.dto.request.SearchGeocodeRequest;
import com.isp.backend.domain.hotel.service.HotelService;
import com.isp.backend.global.exception.openApi.AmadeusSearchFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService ;

    /** 좌표 주변 호텔 리스트 API **/
    @PostMapping("/search")
    public ResponseEntity<String> searchHotelsByGeocode(@RequestBody SearchGeocodeRequest request) {
        try {
            String hotelsJson = hotelService.searchHotelsByGeocode(request);
            return ResponseEntity.ok(hotelsJson);
        } catch (ResponseException e) {
            throw new AmadeusSearchFailedException();
        }
    }

    /** 호텔 선택시 스카이스캐너 사이트로 연결 API **/


    /** 호텔 좋아요 저장 API **/


    /** 호텔 좋아요 목록 불러오기 API **/


    /** 호텔 좋아요 삭제하기 API **/




}
