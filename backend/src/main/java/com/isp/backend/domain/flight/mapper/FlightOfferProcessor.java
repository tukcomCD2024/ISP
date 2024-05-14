package com.isp.backend.domain.flight.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FlightOfferProcessor {

    /** 추출한 정보 json으로 반환 **/
    public String processFlightOffers(String flightOffersJson) {
        JsonArray flightOffersArray = new Gson().fromJson(flightOffersJson, JsonArray.class);

        JsonArray filteredFlightOffers = new JsonArray();
        for (JsonElement flightOfferElement : flightOffersArray) {
            JsonObject flightOffer = flightOfferElement.getAsJsonObject();
            JsonObject filteredFlightOffer = filterFlightOffer(flightOffer);
            filteredFlightOffers.add(filteredFlightOffer);
        }
        return filteredFlightOffers.toString();
    }

    /** 항공 정보에서 필요한 정보 추출 **/
    public JsonObject filterFlightOffer(JsonObject flightOffer) {
        JsonObject filteredFlightOffer = new JsonObject();

        // 필요한 정보 추출
        String id = flightOffer.get("id").getAsString();
        String carrierCode = flightOffer.getAsJsonArray("itineraries").get(0).getAsJsonObject()
                .getAsJsonArray("segments").get(0).getAsJsonObject().get("carrierCode").getAsString();
        Double price = flightOffer.getAsJsonObject("price").get("total").getAsDouble();

        // 왕복-편도 여행 여부 확인 (itineraries 배열 길이 확인)
        boolean isRoundTrip = flightOffer.getAsJsonArray("itineraries").size() == 2;

        // 출국 정보 추출
        JsonObject abroadItinerary = flightOffer.getAsJsonArray("itineraries").get(0).getAsJsonObject();
        String abroadDuration = abroadItinerary.get("duration").getAsString();
        String abroadDepartureTime = abroadItinerary.getAsJsonArray("segments").get(0).getAsJsonObject().getAsJsonObject("departure").get("at").getAsString();
        String abroadArrivalTime = abroadItinerary.getAsJsonArray("segments").get(0).getAsJsonObject().getAsJsonObject("arrival").get("at").getAsString();

        // 출발지 및 도착지 정보 추출
        JsonArray segments = flightOffer.getAsJsonArray("itineraries").get(0).getAsJsonObject().getAsJsonArray("segments");
        JsonObject lastSegment = segments.get(segments.size() - 1).getAsJsonObject();
        String departureIataCode = segments.get(0).getAsJsonObject().getAsJsonObject("departure").get("iataCode").getAsString();
        String arrivalIataCode = lastSegment.getAsJsonObject("arrival").get("iataCode").getAsString();

        // 경유 횟수 확인
        int transferCount = countTransfers(segments);

        // 시간 변환 및 저장
        String filteredAbroadDepartureTime = formatTime(abroadDepartureTime);
        String filteredAbroadArrivalTime = formatTime(abroadArrivalTime);

        // duration 변환 및 저장
        String filteredAbroadDuration = formatDuration(abroadDuration);

        // 경유 및 직항 여부 추출
        boolean nonstop = segments.size() == 1; // 직항일 경우 segments 배열의 크기는 1이 됨

        filteredFlightOffer.addProperty("id", id);
        filteredFlightOffer.addProperty("carrierCode", carrierCode);
        filteredFlightOffer.addProperty("totalPrice", price);
        filteredFlightOffer.addProperty("departureIataCode", departureIataCode);
        filteredFlightOffer.addProperty("arrivalIataCode", arrivalIataCode);
        filteredFlightOffer.addProperty("nonstop", nonstop);
        filteredFlightOffer.addProperty("transferCount", transferCount);

        filteredFlightOffer.addProperty("abroadDuration", filteredAbroadDuration);
        filteredFlightOffer.addProperty("abroadDepartureTime", filteredAbroadDepartureTime);
        filteredFlightOffer.addProperty("abroadArrivalTime", filteredAbroadArrivalTime);


        // 왕복 여행의 경우
        if (isRoundTrip) {
            // 입국 여행 정보 추출
            JsonObject homeItinerary = flightOffer.getAsJsonArray("itineraries").get(1).getAsJsonObject();
            String homeDuration = homeItinerary.get("duration").getAsString();
            String homeDepartureTime = homeItinerary.getAsJsonArray("segments").get(0).getAsJsonObject().getAsJsonObject("departure").get("at").getAsString();
            String homeArrivalTime = homeItinerary.getAsJsonArray("segments").get(1).getAsJsonObject().getAsJsonObject("arrival").get("at").getAsString();
            String filteredHomeDepartureTime = formatTime(homeDepartureTime);
            String filteredHomeArrivalTime = formatTime(homeArrivalTime);
            String filteredHomeDuration = formatDuration(homeDuration);

            // 응답 값에 추가
            filteredFlightOffer.addProperty("homeDuration", filteredHomeDuration);
            filteredFlightOffer.addProperty("homeDepartureTime", filteredHomeDepartureTime);
            filteredFlightOffer.addProperty("homeArrivalTime", filteredHomeArrivalTime);
        }
        return filteredFlightOffer;
    }


    /** 경유 횟수 확인 **/
    private int countTransfers(JsonArray segments) {
        // 직항일 경우 경유 없음
        if (segments.size() == 1) {
            return 0;
        }
        // 경유 횟수는 항공편 세그먼트 개수 - 1
        return segments.size() - 1;
    }


    /** 시간 변환 **/
    private String formatTime(String timeStr) {
        LocalDateTime time = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME);
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    /** duration 비행 시간 변환 **/
    private String formatDuration(String durationStr) {
        Duration duration = Duration.parse(durationStr);

        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%d:%02d", hours, minutes);
    }


}
