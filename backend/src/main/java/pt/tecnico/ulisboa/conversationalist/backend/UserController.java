package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping("/login")
    public String loginUser(@RequestParam(value = "user") String user, @RequestParam(value = "pass") String pass) {

        List<User> userList = new ArrayList<>(repository.getUser());
        for (User username: userList) {
            if (username.getUsername().equals(user)) {
                if (username.getPassword().equals(pass)) {
                    return "Successfully logged in user";
                }
                return "Incorrect password";
            }
        }
        User newUser = new User(user,pass);
        repository.save(newUser);
        return "No such user, registered new account";
    }
}
