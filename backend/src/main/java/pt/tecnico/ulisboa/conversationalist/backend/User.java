package pt.tecnico.ulisboa.conversationalist.backend;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "credentials")
    private String password;
    @Column(name = "availablechats")
    @ElementCollection(targetClass = String.class)
    private List<String> availableChats;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.availableChats = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAvailableChats() {
        return availableChats;
    }

    public void addAvailableChats(String availableChats) {
        this.availableChats.add(availableChats);
    }
}
