package com.FestPass.Repository;

import com.FestPass.Models.FestEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestEventRepository extends JpaRepository<FestEvent, Long> {
}