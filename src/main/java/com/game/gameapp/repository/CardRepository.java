package com.game.gameapp.repository;

import com.game.gameapp.model.CustomCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CustomCard, Long> {
    CustomCard findByText(String text);
}
