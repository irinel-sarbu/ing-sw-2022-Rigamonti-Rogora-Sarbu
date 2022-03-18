package model.player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private final List<Player> playerList;

    public Team() {
        playerList = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (playerList.size() >= 2)
            throw new MaxPlayersPerTeamException();
        else
            playerList.add(player);
    }

    public void remove(String playerName) {
        boolean removed = playerList.removeIf(p -> p.getName().equals(playerName));
        if(!removed) {
            throw new PlayerNotFoundException();
        }
    }
}
