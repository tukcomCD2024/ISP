package com.isp.backend.domain.country.repository;

import com.isp.backend.domain.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findIdByCity(String city); // 여행나라 이름으로 id 찾기

    Country findCountryById(Long countryId); //
    Country findAirportCodeByCity(String countryName); // 나라 이름으로 공항 코드 찾기

}
