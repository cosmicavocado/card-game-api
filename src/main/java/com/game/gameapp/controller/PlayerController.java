package com.game.gameapp.controller;

import com.game.gameapp.model.Player;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api")
public class PlayerController {
    private static final Logger LOGGER = Logger.getLogger(PlayerController.class.getName());
    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    // http://localhost:9092/api/player
    @GetMapping("/player")
    public List<Player> getPlayers() {
        LOGGER.info("Calling getPlayers method from controller.");
        return playerService.getPlayers();
    }

    // http://localhost:9092/api/player/{playerId}
    @GetMapping(path = "/player/{playerId}")
    public ResponseEntity<Object> getPlayer(@PathVariable Long playerId) {
        LOGGER.info("Calling getPlayer method from controller.");
        return new ResponseEntity<>(playerService.getPlayer(playerId), HttpStatus.OK);
    }


    // http://localhost:9092/api/player
    @PostMapping(path = "/player")
    public Player createPlayer(@RequestBody Player playerObject) {
        LOGGER.info("Calling createPlayer method from controller.");
        return playerService.createPlayer(playerObject);
    }

    // http://localhost:9092/api/player/{playerId}
}
