package com.isp.backend.domain.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;

public interface ImageRepository extends JpaRepository<Image, String> {
}
