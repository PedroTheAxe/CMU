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
    @Column(name = "chatImageBitmap", length = 99999)
    private String chatImageBitmap;
    @Column(name = "chatMessageType")
    private String chatMessageType;

    public ChatMessage() {

    }

    public ChatMessage(String chatMessageChatRoom, String chatMessageSender, String chatMessageTimestamp, String chatMessageContent) {
        this.chatMessageChatRoom = chatMessageChatRoom;
        this.chatMessageSender = chatMessageSender;
        this.chatMessageTimestamp = chatMessageTimestamp;
        this.chatMessageContent = chatMessageContent;
    }

    public ChatMessage(String chatMessageChatRoom, String chatMessageSender, String chatMessageTimestamp, String chatMessageType, String chatImageBitmap) {
        this.chatMessageChatRoom = chatMessageChatRoom;
        this.chatMessageSender = chatMessageSender;
        this.chatMessageTimestamp = chatMessageTimestamp;
        this.chatImageBitmap = chatImageBitmap;
        this.chatMessageType = chatMessageType;
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

    public String getChatImageBitmap() {
        return chatImageBitmap;
    }

    public String getChatMessageType() {
        return chatMessageType;
    }

    public Long getId() {
        return id;
    }
}

