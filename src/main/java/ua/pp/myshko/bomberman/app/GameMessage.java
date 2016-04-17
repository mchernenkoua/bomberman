package ua.pp.myshko.bomberman.app;

import ua.pp.myshko.bomberman.server.GameCommands;

/**
 * @author M. Chernenko
 */
public class GameMessage {

    private String gameId;
    private String playerId;
    private GameCommands command;
    private String message;

    public GameMessage() {
    }

    public GameMessage(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GameCommands getCommand() {
        return command;
    }

    public void setCommand(GameCommands command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameMessage that = (GameMessage) o;

        if (gameId != null ? !gameId.equals(that.gameId) : that.gameId != null) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (command != that.command) return false;
        return !(message != null ? !message.equals(that.message) : that.message != null);

    }

    @Override
    public int hashCode() {
        int result = gameId != null ? gameId.hashCode() : 0;
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (command != null ? command.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
