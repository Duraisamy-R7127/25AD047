package com.FestPass.Repository;

import com.FestPass.Models.FestEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestEventRepository extends JpaRepository<FestEvent, Long> {

}