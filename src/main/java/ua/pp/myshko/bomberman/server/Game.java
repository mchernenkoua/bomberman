package ua.pp.myshko.bomberman.server;

import ua.pp.myshko.bomberman.app.GameEventListener;
import ua.pp.myshko.bomberman.server.objects.*;
import ua.pp.myshko.bomberman.server.objects.interfaces.Dependent;
import ua.pp.myshko.bomberman.server.objects.interfaces.GameObject;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author M. Chernenko
 */
public class Game implements GameObjectsEventListener {

    private final String gameId;
    private final GameWorldParams gameWorldParams;

    private Map<String, Player> players = new ConcurrentHashMap<>();
    private Set<Point> changedPoints = ConcurrentHashMap.newKeySet();
    protected final Dashboard dashboard;
    private GameEventListener eventListener;
    private transient Timer responseTimer;

    public Game(String gameId, GameEventListener eventListener, GameWorldParams gameWorldParams) {
        this.gameId = gameId;
        this.eventListener = eventListener;
        this.gameWorldParams = gameWorldParams;
        dashboard = new Dashboard(this, gameWorldParams);
        initGame();
    }

    protected void initGame() {
        dashboard.generateMap();
        initResponseTimer();
    }

    protected void initResponseTimer() {
        if (responseTimer != null) {
            responseTimer.cancel();
        } else {
            responseTimer = new Timer();
        }
        responseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendChanges();
            }
        }, gameWorldParams.getGameResponseDelay(), gameWorldParams.getGameResponseDelay());
    }

    public boolean hasRealPlayers() {
        for (Player player: players.values()) {
            if (!(AIPlayer.class.isInstance(player))) {
                return true;
            }
        }
        return false;
    }

    public void addPlayer(String playerId) {
        Player player = new Player(playerId, this, gameWorldParams);
        addPlayer(player);
        processMessage(playerId, GameCommands.GETMAP);
    }

    public Point addPlayer(Player player) {

        players.put(player.getId(), player);

        Point point = dashboard.getEmptyCornerPoint();
        if (point != null) {
            //player.move(point);
            dashboard.addGameObject(point, player);
        }
        return point;
    }

    public void addAIPlayers() {
        for (int i = 0; i < gameWorldParams.getGameAICount(); i++) {
            String playerID = "AI" + i;
            Player player = getNewAIPlayer(playerID);
            addPlayer(player);
        }
    }

    public void playerQuit(String playerId) {
        Player player = players.get(playerId);
        if (player != null) {
            player.destroy();
            players.remove(playerId);
        }
    }

    private Player getNewAIPlayer(String playerID) {
        return new AIPlayer(playerID, this, gameWorldParams);
    }

    public void close() {
        if (responseTimer != null) {
            responseTimer.cancel();
        }
        dashboard.destroy();
    }


    public void processMessage(String playerId, GameCommands gameCommand) {

        Player player = players.get(playerId);

        synchronized (dashboard) {
            if (gameCommand.equals(GameCommands.GETMAP)) {
                eventListener.handleEvent(gameId, player.getId(), dashboard.getMap());
            } else if (player.getState().equals(GameObjectState.LIVE)) {
                switch (gameCommand) {
                    case ACT_LEFT:
                        dashboard.moveGameObject(player, -1, 0);
                        break;
                    case ACT_RIGHT:
                        dashboard.moveGameObject(player, +1, 0);
                        break;
                    case ACT_UP:
                        dashboard.moveGameObject(player, 0, -1);
                        break;
                    case ACT_DOWN:
                        dashboard.moveGameObject(player, 0, +1);
                        break;
                    case ACT_BOMB:
                        GameObject bomb = player.useWeapon();
                        if (bomb != null) {
                            dashboard.addGameObject(bomb.getPoint(), bomb);
                        }
                        break;
                }
            }
        }

    }

    @Override
    public void handleObjectStateChanging(GameObject... gameObjects) {
        synchronized (dashboard) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getState().equals(GameObjectState.DEAD)) {
                    if (gameObject instanceof Bomb) {
                        Player owner = ((Player)((Dependent) gameObject).getOwner());
                        owner.increaseBombCount();
                    }
                } else if (gameObject.getState().equals(GameObjectState.REMOVE)) {
                    dashboard.removeGameObject(gameObject);
                    if (gameObject instanceof Bomb) {
                        Point bombCoordinates = gameObject.getPoint();
                        Identifiable owner = ((Dependent) gameObject).getOwner();

                        Point newPoint = new Point(bombCoordinates.getX(), bombCoordinates.getY());
                        dashboard.addGameObject(newPoint, new Fire(newPoint, owner, this, gameWorldParams));

                        // it is for filling fire step by step and stop filling fire after Wall
                        Point[] addedPoints = {newPoint, newPoint, newPoint, newPoint};

                        for (int i = 1; i < gameWorldParams.getBombDamage(); i++) {
                            if (addedPoints[0] != null) {
                                newPoint = new Point(bombCoordinates.getX() + i, bombCoordinates.getY());
                                addedPoints[0] = dashboard.addGameObject(newPoint,
                                        new Fire(newPoint, owner, this, gameWorldParams));
                            }
                            if (addedPoints[1] != null) {
                                newPoint = new Point(bombCoordinates.getX() - i, bombCoordinates.getY());
                                addedPoints[1] = dashboard.addGameObject(newPoint,
                                        new Fire(newPoint, owner, this, gameWorldParams));
                            }
                            if (addedPoints[2] != null) {
                                newPoint = new Point(bombCoordinates.getX(), bombCoordinates.getY() + i);
                                addedPoints[2] = dashboard.addGameObject(newPoint,
                                        new Fire(newPoint, owner, this, gameWorldParams));
                            }
                            if (addedPoints[3] != null) {
                                newPoint = new Point(bombCoordinates.getX(), bombCoordinates.getY() - i);
                                addedPoints[3] = dashboard.addGameObject(newPoint,
                                        new Fire(newPoint, owner, this, gameWorldParams));
                            }
                        }
                    }
                }
                handlePointChanging(gameObject.getPoint());
            }
        }
    }

    @Override
    public void handleObjectsAdding(GameObject addedGameObject, Set<GameObject> gameObjectsOnPoint) {
        synchronized (dashboard) {
            if (gameObjectsOnPoint.size() > 0) {
                if (addedGameObject instanceof Player) {
                    for (GameObject anotherGameObjectInThisPoint : gameObjectsOnPoint) {
                        if (anotherGameObjectInThisPoint.equals(addedGameObject)) {
                            continue;
                        }
                        if (anotherGameObjectInThisPoint instanceof Player) {
                            addedGameObject.destroy();
                            handleObjectStateChanging(addedGameObject);

                            ((Player) anotherGameObjectInThisPoint).increaseScore(gameWorldParams.getScoreForDeadPlayer());
                            handleObjectStateChanging(anotherGameObjectInThisPoint);
                        } else if (anotherGameObjectInThisPoint instanceof Fire) {
                            addedGameObject.destroy();
                            handleObjectStateChanging(addedGameObject);

                            GameObject owner = ((Dependent) anotherGameObjectInThisPoint).getOwner();
                            ((Player) owner).increaseScore(gameWorldParams.getScoreForDeadPlayer());
                            handleObjectStateChanging(owner);
                        }
                    }
                } else if (addedGameObject instanceof Fire) {
                    for (GameObject anotherGameObjectInThisPoint : gameObjectsOnPoint) {
                        if (anotherGameObjectInThisPoint.equals(addedGameObject)) {
                            continue;
                        }
                        if (anotherGameObjectInThisPoint instanceof Player) {
                            anotherGameObjectInThisPoint.destroy();
                            handleObjectStateChanging(anotherGameObjectInThisPoint);

                            GameObject owner = ((Dependent) addedGameObject).getOwner();
                            ((Player) owner).increaseScore(gameWorldParams.getScoreForDeadPlayer());
                            handleObjectStateChanging(owner);
                        } else if (anotherGameObjectInThisPoint instanceof Fire) {
                            anotherGameObjectInThisPoint.destroy();
                            handleObjectStateChanging(anotherGameObjectInThisPoint);
                        } else if (anotherGameObjectInThisPoint instanceof Barrier) {
                            anotherGameObjectInThisPoint.destroy();
                            handleObjectStateChanging(anotherGameObjectInThisPoint);

                            GameObject owner = ((Dependent) addedGameObject).getOwner();
                            ((Player) owner).increaseScore(gameWorldParams.getScoreForDeadBarrier());
                            handleObjectStateChanging(owner);
                        } else if (anotherGameObjectInThisPoint instanceof Bomb) {
                            ((Dependent) anotherGameObjectInThisPoint).setOwner(((Dependent) addedGameObject).getOwner());
                            anotherGameObjectInThisPoint.destroy();
                            handleObjectStateChanging(anotherGameObjectInThisPoint);
                        }
                    }
                }
            }
            handlePointChanging(addedGameObject.getPoint());
        }
    }

    @Override
    public void handlePointChanging(Point point) {
        synchronized (dashboard) {
            changedPoints.add(point);
        }
    }

    private void sendChanges() {

        synchronized (dashboard) {
            if (changedPoints.size() > 0) {
                Point[] pointArray = new Point[changedPoints.size()];
                pointArray = changedPoints.toArray(pointArray);

                PlaceMap changedLocations = dashboard.getChangedLocations(pointArray);
                notifyAboutChanges(changedLocations);
                changedPoints.clear();
            }
        }
    }

    protected void notifyAboutChanges(PlaceMap gameObjects) {
        for (Player player: players.values()) {
            eventListener.handleEvent(gameId, player.getId(), gameObjects);
        }
    }

}
