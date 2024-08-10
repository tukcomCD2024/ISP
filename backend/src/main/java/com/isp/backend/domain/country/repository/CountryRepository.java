package com.isp.backend.domain.country.repository;

import com.isp.backend.domain.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findIdByCity(String city); // 여행나라 이름으로 id 찾기
    Country findCountryById(Long countryId);
    Optional<Country> findAirportCodeByCity(String countryName); // 나라 이름으로 공항 코드 찾기
    Optional<Country> findCountryByAirportCode(String airportCode);

}
