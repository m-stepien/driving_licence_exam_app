package com.exam.license.exam.repository;

import com.exam.license.exam.models.Media;
import com.exam.license.exam.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
