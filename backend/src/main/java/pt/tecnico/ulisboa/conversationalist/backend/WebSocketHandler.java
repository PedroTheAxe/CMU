package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WebSocketHandler extends AbstractWebSocketHandler {

    private static List<WebSocketSession> clientSessions = new ArrayList<>();
    //private static HashMap<String,List<String>> openClientChats = new HashMap<>();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.setTextMessageSizeLimit(999999);
        session.setBinaryMessageSizeLimit(999999);
        if (!clientSessions.contains(session)) {
            clientSessions.add(session);
        }
        if (message.getPayload().equals("Session start")) {
            return;
        }
        String[] msg = String.valueOf(message.getPayload()).split("/");
        if (msg[0].equals("Session start")) {
            return;
        }
        String[] imageSplit = String.valueOf(message.getPayload()).split("/",4);
        if (imageSplit[0].equals("image")) {
            String imageData = imageSplit[3];
            String chatId = ChatMessageController.registerWebSocketMessage(msg[1],msg[2],null,"image",imageData);

            for (WebSocketSession ses: clientSessions) {
                try {
                    ses.sendMessage(new TextMessage("imgfileidreq/"+chatId));
                } catch (Exception e) {
                    ses.close();
                }
            }
            return;
        }
        String timestamp = ChatMessageController.registerWebSocketMessage(msg[0],msg[1],msg[2],null,null);
        String jsonString = "[{\"chatMessageSender\":" + "\"" + msg[1] + "\"" + ", \"chatMessageContent\":" + "\"" + msg[2] + "\"" + ", \"chatMessageTimestamp\":" + "\"" + timestamp + "\"" + "}]";
        for (WebSocketSession ses: clientSessions) {
            try {
                ses.sendMessage(new TextMessage(jsonString));
            } catch (Exception e) {
                ses.close();
            }
        }
    }
}