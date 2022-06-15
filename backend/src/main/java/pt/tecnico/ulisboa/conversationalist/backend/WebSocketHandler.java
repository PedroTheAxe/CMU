package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WebSocketHandler extends AbstractWebSocketHandler {

    private static List<WebSocketSession> clientSessions = new ArrayList<>();
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.setTextMessageSizeLimit(999999);
        session.setBinaryMessageSizeLimit(999999);
        System.out.println("estou aqui");
        if (!clientSessions.contains(session)) {
            clientSessions.add(session);
        }
        System.out.println(message.getPayload());
        if (message.getPayload().equals("Session start")) {
            return;
        }
        String[] msg = String.valueOf(message.getPayload()).split("/");
        String[] imageSplit = String.valueOf(message.getPayload()).split("/",3);
        if (imageSplit[0].equals("imagem")) {
            String imageData = imageSplit[2];
            System.out.println("RECEBI UMA IMAGEM");
            String timestamp = new SimpleDateFormat("dd/MM HH:mm").format(Calendar.getInstance().getTime());
            String jsonString = "[{\"chatMessageSender\":" + "\"" + imageSplit[1] + "\"" + ", \"chatMessageContent\":" + "\"" + imageData + "\"" + ", \"chatMessageTimestamp\":" + "\"" + timestamp + "\"" + "}]";
            System.out.println(jsonString);
            for (WebSocketSession ses: clientSessions) {
                ses.sendMessage(new TextMessage(jsonString));
            }
            return;
        }
        if (msg.length < 3) {
            System.out.println("Type 2");
            return;
        }
        String timestamp = ChatMessageController.registerWebSocketMessage(msg[0],msg[1],msg[2]);
        String jsonString = "[{\"chatMessageSender\":" + "\"" + msg[1] + "\"" + ", \"chatMessageContent\":" + "\"" + msg[2] + "\"" + ", \"chatMessageTimestamp\":" + "\"" + timestamp + "\"" + "}]";
        System.out.println(jsonString);
        for (WebSocketSession ses: clientSessions) {
            try {
                ses.sendMessage(new TextMessage(jsonString));
            } catch (Exception e) {
                ses.close();
            }
        }
    }
}