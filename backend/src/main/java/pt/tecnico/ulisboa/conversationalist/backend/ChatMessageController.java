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
        List<ChatMessage> messagesFromChat = repository.findMessagesByChatRoomName(chatroomname);
        if (messagesFromChat.size() < 8) {
            return repository.findMessagesByChatRoomName(chatroomname).subList(0,messagesFromChat.size());
        } else {
            return repository.findMessagesByChatRoomName(chatroomname).subList(0,8);
        }
    }

    @GetMapping("/fetchmessages")
    public List<ChatMessage> fetchChatMessages(@RequestParam(value = "chatroomname") String chatroomname, @RequestParam(value = "from") String totalCount) {
        List<ChatMessage> chatMsgQueue = repository.findMessagesByChatRoomName(chatroomname);
        int queueSize = chatMsgQueue.size();
        if (queueSize > Integer.valueOf(totalCount)+3) {
            return repository.findOrderedMessagesByChatRoomName(chatroomname).subList(Integer.valueOf(totalCount),Integer.valueOf(totalCount)+3);
        } else {
            return repository.findOrderedMessagesByChatRoomName(chatroomname).subList(Integer.valueOf(totalCount),Integer.valueOf(totalCount)+(queueSize-Integer.valueOf(totalCount)));
        }

    }

    @GetMapping("/sendmessage")
    public void registerNewMessage(@RequestParam(value = "chatroom") String chatRoom, @RequestParam(value = "username") String userName, @RequestParam(value = "content") String content) {
        String timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(Calendar.getInstance().getTime());
        ChatMessage chatMessage = new ChatMessage(chatRoom, userName, timeStamp, content);
        repository.save(chatMessage);
    }

    @GetMapping("/getimage")
    public ChatMessage getImageFromId(@RequestParam(value="id") String messageId) {
        return repository.findMessageById(Long.parseLong(messageId));

    }

    public static String registerWebSocketMessage(String chatRoom, String userName, String content, String chatType, String chatBitmap) {
        String timeStamp = new SimpleDateFormat("dd/MM HH:mm").format(Calendar.getInstance().getTime());
        if (chatType != null) {
            System.out.println(chatBitmap);
            ChatMessage chatImage = new ChatMessage(chatRoom, userName, timeStamp, chatType, chatBitmap);
            System.out.println(chatImage.getChatMessageType());
            repository.save(chatImage);
            return String.valueOf(chatImage.getId());
        }
        ChatMessage chatMessage = new ChatMessage(chatRoom, userName, timeStamp, content);
        repository.save(chatMessage);
        return timeStamp;
    }

    public ChatMessageRepository getRepository() {
        return repository;
    }
}
