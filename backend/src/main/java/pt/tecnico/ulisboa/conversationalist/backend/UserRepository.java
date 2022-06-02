package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM account", nativeQuery = true)
    public List<User> getUser();

}