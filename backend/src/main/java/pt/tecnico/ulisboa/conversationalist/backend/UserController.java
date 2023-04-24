package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private static UserRepository repository = null;

    UserController(UserRepository repository) {
        UserController.repository = repository;
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping("/register")
    public void registerUser(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String pass) {
        User newUser = new User(user,pass);
        this.repository.save(newUser);
        return;
    }

    @GetMapping("/login")
    public String loginUser(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String pass) {

        List<User> userList = new ArrayList<>(this.repository.getUser());
        for (User username: userList) {
            if (username.getUsername().equals(user)) {
                if (username.getPassword().equals(pass)) {
                    System.out.println("Logged in");
                    return "Successfully logged in user";
                }
                System.out.println("Incorrect password");
                return "Incorrect password";
            }
        }

        return "No such user, register new account";
    }

    @GetMapping("/joinprivatechat")
    public void joinPrivateChat(@RequestParam(value = "username") String userName, @RequestParam(value = "invitelink") String inviteLink) {
        User user = repository.findUserByUserName(userName);
        ChatRepository chatRepository = ChatController.getRepository();
        List<Chat> chats = chatRepository.getChat();
        for(Chat chat: chats) {
            if (chat.getInviteLink() == null) {
                continue;
            }
            if (chat.getInviteLink().equals(inviteLink)) {
                user.addAvailableChats(chat.getChatName());
                this.repository.save(user);
            }
        }
    }

    public static UserRepository getRepository() {
        return UserController.repository;
    }
}
