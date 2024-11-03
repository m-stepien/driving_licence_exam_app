package com.exam.license.exam.repository;

import com.exam.license.exam.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
