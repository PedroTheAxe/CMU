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

    @GetMapping("/login")
    public String loginUser(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String pass) {

        List<User> userList = new ArrayList<>(this.repository.getUser());
        System.out.println("aaaaaaaaaaaaaaaaa " + user);
        for (User username: userList) {
            if (username.getUsername().equals(user)) {
                if (username.getPassword().equals(pass)) {
                    return "Successfully logged in user";
                }
                return "Incorrect password";
            }
        }
        User newUser = new User(user,pass);
        this.repository.save(newUser);
        System.out.println("AHHAUHAFSFDShihi" + newUser.getUsername());

        System.out.println("AHHAUHAFSFDSyooo" + newUser.getUsername());
        User test = this.repository.findUserByUserName(user);
        System.out.println("fixe" + test.getUsername());
        return "No such user, registered new account";
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
