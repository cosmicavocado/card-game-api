package com.game.gameapp.controller;

import com.game.gameapp.model.Player;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<Player> getPlayer(@PathVariable Long playerId) {
        LOGGER.info("Calling getPlayer method from controller.");
        return playerService.getPlayer(playerId);
    }


    // http://localhost:9092/api/player
    @PostMapping(path = "/player")
    public Player createPlayer(@RequestBody Player playerObject) {
        LOGGER.info("Calling createPlayer method from controller.");
        return playerService.createPlayer(playerObject);
    }

    // http://localhost:9092/api/player/{playerId}
    @PutMapping(path ="/player/{playerId}")
    public Player updatePlayer(@PathVariable Long playerId, @RequestBody Player playerObject) {
        LOGGER.info("Calling updatePlayer method from controller.");
        return playerService.updatePlayer(playerId, playerObject);
    }

    // http://localhost:9092/api/player/{playerId}
    @DeleteMapping(path ="/player/{playerId}")
    public String deleteCategory(@PathVariable Long playerId) {
        LOGGER.info("Calling deletePlayer method from controller.");
        return playerService.deletePlayer(playerId);
    }
}
