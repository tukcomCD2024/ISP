package com.isp.backend.domain.travelPlace.repository;

import com.isp.backend.domain.travelPlace.entity.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPlaceRepository extends JpaRepository<TravelPlace, String> {
}
