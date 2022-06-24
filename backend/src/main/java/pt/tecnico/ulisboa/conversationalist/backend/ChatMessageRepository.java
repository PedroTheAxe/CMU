package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    @Query(value = "select * from chat_message where chatmessagechatroom = :chatname", nativeQuery = true)
    List<ChatMessage> findMessagesByChatRoomName(@Param("chatname") String chatRoomName);

    @Query(value = "select * from chat_message where id = :messageid", nativeQuery = true)
    ChatMessage findMessageById(@Param("messageid") long messageId);

    @Query(value = "select * from chat_message where chatmessagechatroom = :chatname order by id", nativeQuery = true)
    List<ChatMessage> findOrderedMessagesByChatRoomName(@Param("chatname") String chatRoomName);
}