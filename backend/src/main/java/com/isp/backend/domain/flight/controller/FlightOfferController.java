package com.isp.backend.domain.flight.controller;

import com.amadeus.exceptions.ResponseException;
import com.isp.backend.domain.flight.dto.request.FlightLikeRequest;
import com.isp.backend.domain.flight.dto.request.SkyScannerRequest;
import com.isp.backend.domain.flight.dto.response.FlightLikeResponse;
import com.isp.backend.domain.flight.service.FlightOfferServiceImpl;
import com.isp.backend.domain.flight.dto.request.FlightSearchRequest;
import com.isp.backend.global.exception.flight.FlightSearchFailedException;
import com.isp.backend.global.exception.flight.SkyScannerGenerateFailedException;
import com.isp.backend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings/flights")
@RequiredArgsConstructor
public class FlightOfferController {

    @Autowired
    private FlightOfferServiceImpl flightOfferServiceImpl;

    /** 항공권 검색 API **/
    @GetMapping("/search")
    public ResponseEntity<String> getFlightOffers(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody FlightSearchRequest request) {
        String memberUid = customUserDetails.getUsername();
        try {
            String flightOffersJson = flightOfferServiceImpl.getFlightOffers(request);
            return ResponseEntity.ok(flightOffersJson);
        } catch (ResponseException e) {
            throw new FlightSearchFailedException();
        }
    }


    /** 항공권 선택시 스카이스캐너 사이트로 연결 API **/
    @PostMapping("/skyscanner")
    public ResponseEntity<String> getFlightSearchUrl(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestBody SkyScannerRequest request) {
        String memberUid = customUserDetails.getUsername();
        try {
            String skyscannerUrl = flightOfferServiceImpl.generateSkyscannerUrl(request);
            return ResponseEntity.ok("{\"skyscannerUrl\": \"" + skyscannerUrl + "\"}");
        } catch (Exception e) {
            throw new SkyScannerGenerateFailedException();
        }
    }


    /** 항공권 좋아요 저장 API **/
    @PostMapping("/like")
    public ResponseEntity<Void> addLikeFlight(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                              @RequestBody FlightLikeRequest flightLikeRequest) {
        String memberUid = customUserDetails.getUsername();
        flightOfferServiceImpl.addLikeFlight(memberUid, flightLikeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** 항공권 나의 좋아요 목록 불러오기 API **/
    @GetMapping("/likes")
    public ResponseEntity<List<FlightLikeResponse>> getLikedFlights(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String memberUid = customUserDetails.getUsername();
        List<FlightLikeResponse> likedFlights = flightOfferServiceImpl.getLikedFlights(memberUid);
        return ResponseEntity.ok(likedFlights);
    }

    /** 항공권 나의 좋아요 삭제 API **/
    @DeleteMapping("/like/{id}")
    public ResponseEntity<Void> deleteLikeFlight(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @PathVariable Long id) {
        String memberUid = customUserDetails.getUsername();
        flightOfferServiceImpl.deleteLikeFlight(memberUid, id);
        return ResponseEntity.ok().build();
    }

}
