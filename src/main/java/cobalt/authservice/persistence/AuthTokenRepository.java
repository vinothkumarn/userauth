package cobalt.authservice.persistence;

import cobalt.authservice.entity.AuthToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {

    Optional findById(Long id);
}
