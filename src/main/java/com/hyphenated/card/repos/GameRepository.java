package com.hyphenated.card.repos;

import com.hyphenated.card.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nitin on 25-10-2015.
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByStartedFalse();
}
