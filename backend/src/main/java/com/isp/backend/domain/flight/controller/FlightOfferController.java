package com.isp.backend.domain.flight.controller;

import com.amadeus.exceptions.ResponseException;
import com.isp.backend.domain.flight.dto.request.FlightLikeRequest;
import com.isp.backend.domain.flight.dto.request.SkyScannerRequest;
import com.isp.backend.domain.flight.dto.response.FlightLikeResponse;
import com.isp.backend.domain.flight.service.FlightOfferService;
import com.isp.backend.domain.flight.dto.request.FlightSearchRequest;
import com.isp.backend.global.exception.flight.AmadeusSearchFailedException;
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
    private FlightOfferService flightOfferService;

    /** 항공권 검색 API **/
    @PostMapping("/search")
    public ResponseEntity<String> getFlightOffers(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                  @RequestBody FlightSearchRequest request) {
        String memberUid = customUserDetails.getUsername();
        try {
            String flightOffersJson = flightOfferService.getFlightOffers(request);
            return ResponseEntity.ok(flightOffersJson);
        } catch (ResponseException e) {
            throw new AmadeusSearchFailedException();
        }
    }


    /** 항공권 선택시 스카이스캐너 사이트로 연결 API **/
    @PostMapping("/connect")
    public ResponseEntity<String> getFlightSearchUrl(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestBody SkyScannerRequest request) {
        String memberUid = customUserDetails.getUsername();
        try {
            String skyscannerUrl = flightOfferService.generateSkyscannerUrl(request);
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
        flightOfferService.addLikeFlight(memberUid, flightLikeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** 항공권 나의 좋아요 목록 불러오기 API **/
    @GetMapping("/likes")
    public ResponseEntity<List<FlightLikeResponse>> getLikedFlights(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String memberUid = customUserDetails.getUsername();
        List<FlightLikeResponse> likedFlights = flightOfferService.getLikedFlights(memberUid);
        return ResponseEntity.ok(likedFlights);
    }

    /** 항공권 나의 좋아요 삭제 API **/
    @DeleteMapping("/like/{id}")
    public ResponseEntity<Void> deleteLikeFlight(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @PathVariable Long id) {
        String memberUid = customUserDetails.getUsername();
        flightOfferService.deleteLikeFlight(memberUid, id);
        return ResponseEntity.ok().build();
    }

}
