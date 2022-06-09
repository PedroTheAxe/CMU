package pt.tecnico.ulisboa.conversationalist.backend;

import javax.persistence.*;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", updatable = false, nullable = false, unique = true)
    private Long id;
    @Column(name = "chatmessagechatroom")
    private String chatMessageChatRoom;
    @Column(name = "chatMessageSender")
    private String chatMessageSender;
    @Column(name = "chatMessageTimestamp")
    private String chatMessageTimestamp;
    @Column(name = "chatMessageContent")
    private String chatMessageContent;

    public ChatMessage() {

    }

    public ChatMessage(String chatMessageChatRoom, String chatMessageSender, String chatMessageTimestamp, String chatMessageContent) {
        this.chatMessageChatRoom = chatMessageChatRoom;
        this.chatMessageSender = chatMessageSender;
        this.chatMessageTimestamp = chatMessageTimestamp;
        this.chatMessageContent = chatMessageContent;
    }

    public String getChatMessageSender() {
        return chatMessageSender;
    }

    public void setChatMessageSender(String chatMessageSender) {
        this.chatMessageSender = chatMessageSender;
    }

    public String getChatMessageTimestamp() {
        return chatMessageTimestamp;
    }

    public void setChatMessageTimestamp(String chatMessageTimestamp) {
        this.chatMessageTimestamp = chatMessageTimestamp;
    }

    public String getChatMessageContent() {
        return chatMessageContent;
    }

    public void setChatMessageContent(String chatMessageContent) {
        this.chatMessageContent = chatMessageContent;
    }

    public String getChatMessageChatRoom() {
        return chatMessageChatRoom;
    }

    public void setChatMessageChatRoom(String chatMessageChatRoom) {
        this.chatMessageChatRoom = chatMessageChatRoom;
    }
}

