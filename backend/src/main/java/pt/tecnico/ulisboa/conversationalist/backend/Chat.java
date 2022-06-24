package pt.tecnico.ulisboa.conversationalist.backend;

import javax.persistence.*;
import java.util.List;

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
    @Column(name = "invitelink")
    private String inviteLink;
    @Column(name = "radius")
    private String chatRadius;
    @Column(name = "lat")
    private String chatLat;
    @Column(name = "lon")
    private String chatLon;
    public Chat() {

    }

    public Chat(String chatName, String chatType
    ) {
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

    public String getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    public String getChatRadius() {
        return chatRadius;
    }

    public String getChatLat() {
        return chatLat;
    }

    public String getChatLon() {
        return chatLon;
    }

    public void setChatRadius(String chatRadius) {
        this.chatRadius = chatRadius;
    }

    public void setChatLat(String chatLat) {
        this.chatLat = chatLat;
    }

    public void setChatLon(String chatLon) {
        this.chatLon = chatLon;
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
