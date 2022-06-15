package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    private static ChatRepository repository = null;

    ChatController(ChatRepository repository) {
        this.repository = repository;
        Chat newChat1 = new Chat("Teste1", Chat.ChatType.PUB.toString());
        Chat newChat2 = new Chat("Teste2",Chat.ChatType.GEO.toString());
        Chat newChat3 = new Chat("Teste3",Chat.ChatType.PRI.toString());
        newChat3.setInviteLink("7YAD3SF");
        repository.save(newChat1);
        repository.save(newChat2);
        repository.save(newChat3);
    }
    @GetMapping(value = "/chats")
    public List<Chat> getChat(@RequestParam(value = "username") String userName) {
        List<Chat> chatlist = ChatController.getRepository().getChat();
        UserRepository userrepo = UserController.getRepository();
        User user = userrepo.findUserByUserName(userName);
        List<Chat> availableChats = new ArrayList<>();
        for (String chatName: user.getAvailableChats()) {
                for (Chat chat: chatlist) {
                    if (chat.getChatName().equals(chatName)) {
                        if (availableChats.contains(chat)) {

                        } else{
                            availableChats.add(chat);
                        }
                    }
                }
        }
        return availableChats;
    }

    @GetMapping(value = "/createchat")
    public void createPublicPrivateChat(@RequestParam(value = "chatname") String chatname, @RequestParam(value = "chattype") String chattype, @RequestParam(required=false,value = "invitelink") String inviteLink) {
        Chat toAddChat = null;
        String fullInviteLink = null;
        if (chattype.equals("public")) {
            toAddChat = new Chat(chatname, Chat.ChatType.PUB.toString());
        } else {
            toAddChat = new Chat(chatname, Chat.ChatType.PRI.toString());
            toAddChat.setInviteLink(inviteLink);
        }
        repository.save(toAddChat);
    }

    public static ChatRepository getRepository() {
        return repository;
    }
}
