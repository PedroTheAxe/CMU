package pt.tecnico.ulisboa.conversationalist.backend;

import javax.persistence.*;

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "chatName", unique = true)
    private String chatName;
    @Column(name = "chatType")
    private String chatType;

    public Chat() {

    }

    public Chat(String chatName, String chatType) {
        this.chatName = chatName;
        this.chatType = chatType;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType.toString();
    }

    public enum ChatType {

        PUB {
            @Override
            public String toString() {
                return "Public";
            }
        },
        PRI {
            @Override
            public String toString() {
                return "Private";
            }
        },
        GEO {
            @Override
            public String toString() {
                return "Geo-Fenced";
            }
        }

    }
}
