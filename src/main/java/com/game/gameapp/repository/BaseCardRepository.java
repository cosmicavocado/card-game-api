package com.game.gameapp.repository;

import com.game.gameapp.model.BaseCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCardRepository extends JpaRepository<BaseCard, Long> {
}
