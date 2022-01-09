package com.game.gameapp.controller;

import com.game.gameapp.model.Player;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
        LOGGER.info("Calling getPlayer method from player controller.");
        return playerService.getPlayer(playerId);
    }

    // http://localhost:9092/api/player
    @PostMapping(path = "/player")
    public Player createPlayer(@RequestBody Player playerObject) {
        LOGGER.info("Calling createPlayer method from player controller.");
        return playerService.createPlayer(playerObject);
    }

    // http://localhost:9092/api/player/{playerId}
    @PutMapping(path ="/player/{playerId}")
    public Player updatePlayer(@PathVariable Long playerId, @RequestBody Player playerObject) {
        LOGGER.info("Calling updatePlayer method from player controller.");
        return playerService.updatePlayer(playerId, playerObject);
    }

    // http://localhost:9092/api/player/{playerId}
    @DeleteMapping(path ="/player/{playerId}")
    public String deleteCategory(@PathVariable Long playerId) {
        LOGGER.info("Calling deletePlayer method from player controller.");
        return playerService.deletePlayer(playerId);
    }

    // http://localhost:9092/api/player/{playerId}/draw
    @GetMapping(path="/player/{playerId}/draw")
    public void drawUpToTen(@PathVariable Long playerId) {
        LOGGER.info("Calling drawUpToTen method from player controller.");
        playerService.drawUpToTen(playerId);
    }

    // http://localhost:9092/api/player/{playerIds}/play
    @PostMapping(path="/player/play")
    public void playGame(@RequestBody LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling playGame method from player controller.");
        playerService.playGame(players);
    }

    // http://localhost:9092/api/player/{playerId}/card/{cardId}
}
