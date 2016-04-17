package ua.pp.myshko.bomberman.app;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.pp.myshko.bomberman.server.GameCommands;
import ua.pp.myshko.bomberman.server.GameServer;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 * @author M. Chernenko
 */
public class GameServerEndpointTest {

    public static final String SESSION_ID = "S_ID100";
    Session session;
    RemoteEndpoint.Basic basicRemote;
    GameServerEndpoint gameServerEndpoint;
    GameServer gameServer;

    @Before
    public void setUp() throws Exception {

        basicRemote = Mockito.mock(RemoteEndpoint.Basic.class);
        //when(basicRemote.sendText(anyString())).thenCallRealMethod()
        gameServer = Mockito.mock(GameServer.class);

        session = Mockito.mock(Session.class);
        Mockito.when(session.getId()).thenReturn(SESSION_ID);
        Mockito.when(session.getBasicRemote()).thenReturn(basicRemote);

    }

    @Test
    public void shouldRegisterSession() throws Exception {
        gameServerEndpoint = new GameServerEndpoint();
        gameServerEndpoint.onOpen(session);
    }

    @Test
    public void shouldSendMessageToClient() throws Exception {

        gameServerEndpoint = new GameServerEndpoint() {
            @Override
            public void handleEvent(String gameId, String playerId, Object msg) {
                sendToClient(session,  gameId, playerId, msg);
            }
        };
        gameServerEndpoint.onOpen(session);
        gameServerEndpoint.handleEvent(SESSION_ID, SESSION_ID, "msg");

        Mockito.verify(basicRemote).sendText("{\n" +
                "  \"gameId\": \"S_ID100\",\n" +
                "  \"playerId\": \"S_ID100\",\n" +
                "  \"message\": \"msg\"\n" +
                "}");
    }

    @Test
    public void shouldTransferMessageToGameServer() throws Exception {
        gameServerEndpoint = new GameServerEndpoint();
        gameServerEndpoint.setGameServer(gameServer);
        gameServerEndpoint.onOpen(session);
        gameServerEndpoint.onMessage("{\"gameId\":\"S_ID100\"," +
                                        "\"playerId\":\"S_ID100\"," +
                                        "\"command\":\"START\"," +
                                        "\"message\":\"msg\"}",
                session);

        GameMessage gameMessage = new GameMessage(SESSION_ID, SESSION_ID);
        gameMessage.setCommand(GameCommands.START);
        gameMessage.setMessage("msg");

        try {
            Mockito.verify(gameServer).handlePlayerMessage(gameMessage);
        } catch (BombermanException e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void shouldQuitGameOnCloseSession() throws Exception {
        gameServerEndpoint = new GameServerEndpoint();
        gameServerEndpoint.setGameServer(gameServer);
        gameServerEndpoint.onOpen(session);
        gameServerEndpoint.onClose(session, null);

        Mockito.verify(gameServer).handleQuit(SESSION_ID);
    }

}