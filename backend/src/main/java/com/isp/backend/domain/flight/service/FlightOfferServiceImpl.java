package com.isp.backend.domain.flight.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.google.gson.Gson;
import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.country.repository.CountryRepository;
import com.isp.backend.domain.flight.dto.request.FlightLikeRequest;
import com.isp.backend.domain.flight.dto.request.FlightSearchRequest;
import com.isp.backend.domain.flight.dto.request.SkyScannerRequest;
import com.isp.backend.domain.flight.dto.response.FlightLikeResponse;
import com.isp.backend.domain.flight.entity.Flight;
import com.isp.backend.domain.flight.mapper.FlightMapper;
import com.isp.backend.domain.flight.mapper.FlightOfferProcessor;
import com.isp.backend.domain.flight.repository.FlightRepository;
import com.isp.backend.domain.member.entity.Member;
import com.isp.backend.domain.schedule.service.ScheduleService;
import com.isp.backend.global.exception.flight.FlightNotFoundException;
import com.isp.backend.global.exception.flight.NotYourFlightException;
import com.isp.backend.global.exception.schedule.CountryNotFoundException;
import com.isp.backend.global.exception.schedule.IataCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FlightOfferServiceImpl implements FlightOfferService {

    private final Amadeus amadeus;
    private final CountryRepository countryRepository;
    private final ScheduleService scheduleService;
    private final FlightOfferProcessor flightOfferProcessor ;
    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;


    /** 항공편 조회 **/
    @Override
    public String getFlightOffers(FlightSearchRequest request) throws ResponseException {

        String originLocationCode = findAirportCode(request.getOriginCity());
        String destinationLocationCode = findAirportCode(request.getDestinationCity());

        Params params = Params.with("originLocationCode", originLocationCode)
                .and("destinationLocationCode", destinationLocationCode)
                .and("departureDate", request.getDepartureDate())
                .and("adults", request.getAdults())
                .and("children", request.getChildren())
                .and("max", request.getMax())
                .and("nonStop", request.isNonStop())
                .and("currencyCode", "KRW");  // 원화 설정 -> 추후 유저에게 입력받을 수 있게 남겨둠

        // returnDate가 빈 문자열이 아닌 경우에만 파라미터 추가
        if (request.getReturnDate() != null && !request.getReturnDate().isEmpty()) {
            params.and("returnDate", request.getReturnDate());
        }

        FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(params);

        Gson gson = new Gson();
        String flightOffersJson = gson.toJson(flightOffers);
        return flightOfferProcessor.processFlightOffers(flightOffersJson);
    }


    /** 항공권 선택시 스카이스캐너 사이트로 연결 **/
    @Override
    public String generateSkyscannerUrl(SkyScannerRequest request) {
        // 요청 데이터 정제
        String departureDate = request.getDepartureDate().replace("-", "").substring(2);
        String returnDate = (request.getReturnDate() != null) ? request.getReturnDate().replace("-", "").substring(2) : "";
        int departureTimeMinutes = convertToMinutes(request.getDepartureTime());

        String url = "https://www.skyscanner.co.kr/transport/flights/" +
                request.getDepartureIataCode().toLowerCase() + "/" +
                request.getArrivalIataCode().toLowerCase() + "/" +
                departureDate + "/" +
                returnDate + "/?adultsv2=" + request.getAdults() +
//                "&cabinclass=economy&childrenv2=" + request.getChildren() + "%7C" + request.getChildren() +
                "&departure-times=" + departureTimeMinutes +
                "&inboundaltsenabled=false&outboundaltsenabled=false&ref=home&rtn=" + (returnDate.isEmpty() ? "0" : "1");

        // 직항인 경우
        if (request.getTransferCount() == 0) {
            url += "&stops=!oneStop,!twoPlusStops";
        } else {
            // 직항이 아닌 경우, 경유 횟수에 따라 다른 URL을 생성
            url += "&stops=!direct";  // 첫 번째 항공편은 직항이어야 함
            for (int i = 0; i < request.getTransferCount(); i++) {
                url += ",!oneStop";  // 1회 경유 추가
            }
        }
        return url;
    }


    /** 항공권 좋아요 저장 **/
    @Override
    @Transactional
    public void addLikeFlight(String uid, FlightLikeRequest flightLikeRequest) {
        // 유저 정보 확인
        Member findMember = scheduleService.validateUserCheck(uid);

        // 출발지, 도착지 정보
        Country departureIataCode = countryRepository.findCountryByAirportCode(flightLikeRequest.getDepartureIataCode())
                .orElseThrow(()-> new IataCodeNotFoundException());
        Country arrivalIataCode = countryRepository.findCountryByAirportCode(flightLikeRequest.getArrivalIataCode())
                .orElseThrow(()-> new IataCodeNotFoundException());

        // 데이터 변환 및 저장
        Flight flight = flightMapper.toEntity(flightLikeRequest, findMember, departureIataCode, arrivalIataCode);
        flightRepository.save(flight);
    }


    /** 항공권 나의 좋아요 목록 불러오기 **/
    @Override
    public List<FlightLikeResponse> getLikedFlights(String memberUid) {
        Member findMember = scheduleService.validateUserCheck(memberUid);
        List<Flight> likedFlights = flightRepository.findByMember(findMember);

        return flightMapper.toFlightLikeRequestList(likedFlights);
    }


    /** 항공권 나의 좋아요 삭제 **/
    @Override
    @Transactional
    public void deleteLikeFlight(String memberUid, Long id) {
        Member findMember = scheduleService.validateUserCheck(memberUid);

        // 해당 항공권 id 조회
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException());

        // 해당 항공권이 유저의 것인지 확인
        if (!flight.getMember().getId().equals(findMember.getId())) {
            throw new NotYourFlightException();
        }

        flightRepository.delete(flight);
    }

    /** 공항 코드 찾기 **/
    String findAirportCode(String countryName) {
        Country findCountry = countryRepository.findAirportCodeByCity(countryName)
                .orElseThrow(() -> new CountryNotFoundException());
        return findCountry.getAirportCode();
    }


    /** 시간을 분으로 변환 **/
    static int convertToMinutes(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }


}
