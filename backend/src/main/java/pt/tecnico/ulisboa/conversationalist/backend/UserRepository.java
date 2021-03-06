package pt.tecnico.ulisboa.conversationalist.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM account", nativeQuery = true)
    List<User> getUser();

    @Query(value = "select * from account where username = :username", nativeQuery = true)
    User findUserByUserName(@Param("username") String userName);

}