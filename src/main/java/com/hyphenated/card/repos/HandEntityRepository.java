package com.hyphenated.card.repos;

import com.hyphenated.card.domain.HandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nitin on 05-11-2015.
 */
public interface HandEntityRepository extends JpaRepository<HandEntity, Long> {


}