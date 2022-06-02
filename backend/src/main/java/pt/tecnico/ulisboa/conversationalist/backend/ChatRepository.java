package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface ChatRepository extends JpaRepository<Chat, String> {

    @Query(value = "SELECT * FROM chat", nativeQuery = true)
    public List<Chat> getChat();

}