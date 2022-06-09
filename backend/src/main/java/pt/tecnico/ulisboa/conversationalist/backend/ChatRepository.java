package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface ChatRepository extends JpaRepository<Chat, String> {

    @Query(value = "SELECT * FROM chat", nativeQuery = true)
    List<Chat> getChat();
}