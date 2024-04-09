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

        // 필요한 정보만 객체로 변환
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
        filteredFlightOffer.addProperty("id", flightOffer.get("id").getAsString());

        // 항공사 코드 정보 추가
        JsonArray itinerarySegments = flightOffer.getAsJsonArray("itineraries").get(0).getAsJsonObject().getAsJsonArray("segments");
        String carrierCode = itinerarySegments.get(0).getAsJsonObject().get("carrierCode").getAsString();
        filteredFlightOffer.addProperty("carrierCode", carrierCode);



        // 여행 일정 - itineraries
        JsonArray itineraries = flightOffer.getAsJsonArray("itineraries");
        JsonArray filteredItineraries = new JsonArray();
        for (JsonElement itineraryElement : itineraries) {
            JsonObject itinerary = itineraryElement.getAsJsonObject();
            JsonObject filteredItinerary = new JsonObject();

            // duration 추출
            formatDuration(itinerary, filteredItinerary);

            // segments 추출
            JsonArray segments = itinerary.getAsJsonArray("segments");
            JsonArray filteredSegments = new JsonArray();
            for (JsonElement segmentElement : segments) {
                JsonObject segment = segmentElement.getAsJsonObject();
                JsonObject filteredSegment = new JsonObject();

                // 출발편
                filteredSegment.addProperty("departureIataCode", segment.getAsJsonObject("departure").get("iataCode").getAsString());
                formatTime(segment.getAsJsonObject("departure").get("at").getAsString(), filteredSegment, "departureTime");
                // 도착편
                filteredSegment.addProperty("arrivalIataCode", segment.getAsJsonObject("arrival").get("iataCode").getAsString());
                formatTime(segment.getAsJsonObject("arrival").get("at").getAsString(), filteredSegment, "arrivalTime");

                filteredSegments.add(filteredSegment);
            }
            filteredItinerary.add("segments", filteredSegments);
            filteredItineraries.add(filteredItinerary);
        }
        filteredFlightOffer.add("itineraries", filteredItineraries);

        // 가격 정보 추출
        JsonObject price = flightOffer.getAsJsonObject("price");
        filteredFlightOffer.addProperty("totalPrice", price.get("total").getAsString());

        return filteredFlightOffer;
    }


    /** 시간 변환 **/
    private void formatTime(String timeStr, JsonObject filteredSegment, String propertyName) {
        LocalDateTime time = LocalDateTime.parse(timeStr, DateTimeFormatter.ISO_DATE_TIME);
        String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        filteredSegment.addProperty(propertyName, formattedTime);
    }

    /** duration 비행 시간 변환 **/
    private void formatDuration(JsonObject itinerary, JsonObject filteredItinerary) {
        String durationString = itinerary.get("duration").getAsString();
        Duration duration = Duration.parse(durationString);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        String formattedDuration = String.format("%d:%02d", hours, minutes);
        filteredItinerary.addProperty("duration", formattedDuration);
    }


}

