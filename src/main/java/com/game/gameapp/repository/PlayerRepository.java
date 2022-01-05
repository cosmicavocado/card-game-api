package com.game.gameapp.repository;

import com.game.gameapp.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByIdAndName(Long playerId, String name);
}
