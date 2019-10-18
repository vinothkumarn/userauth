package cobalt.authservice.persistence;

import cobalt.authservice.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional findById(Long id);

    @Query(value = "SELECT * FROM users u WHERE u.email = :email",
            nativeQuery = true)
    User findUserByEmail(
            @Param("email") String username);
}
