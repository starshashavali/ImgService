package com.tcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
