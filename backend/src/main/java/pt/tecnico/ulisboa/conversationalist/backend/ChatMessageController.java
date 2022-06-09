package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
public class ChatMessageController {

    private static ChatMessageRepository repository = null;

    public ChatMessageController(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/chatrooms")
    public List<ChatMessage> getChatMessages(@RequestParam(value = "chatroomname") String chatroomname) {

        return repository.findMessagesByChatRoomName(chatroomname);
    }

    @GetMapping("/sendmessage")
    public void registerNewMessage(@RequestParam(value = "chatroom") String chatRoom, @RequestParam(value = "username") String userName, @RequestParam(value = "content") String content) {
        String timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(Calendar.getInstance().getTime());
        ChatMessage chatMessage = new ChatMessage(chatRoom, userName, timeStamp, content);
        repository.save(chatMessage);
    }

    public static String registerWebSocketMessage(String chatRoom, String userName, String content) {
        String timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(Calendar.getInstance().getTime());
        ChatMessage chatMessage = new ChatMessage(chatRoom, userName, timeStamp, content);
        repository.save(chatMessage);
        return timeStamp;
    }

    public ChatMessageRepository getRepository() {
        return repository;
    }
}
