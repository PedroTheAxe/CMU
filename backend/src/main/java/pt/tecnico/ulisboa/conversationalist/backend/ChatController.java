package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    private final ChatRepository repository;

    ChatController(ChatRepository repository) {
        this.repository = repository;
        Chat newChat1 = new Chat("Teste1", Chat.ChatType.PUB.toString());
        Chat newChat2 = new Chat("Teste2",Chat.ChatType.GEO.toString());
        repository.save(newChat1);
        repository.save(newChat2);
    }
    //idk if this produces does anything
    @GetMapping(value = "/chats", produces = "application/json")
    public List<Chat> getUsers() {
        return repository.getChat();
    }

}
