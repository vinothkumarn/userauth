package cobalt.authservice.persistence;

import cobalt.authservice.entity.ResetToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetTokenRepository extends CrudRepository<ResetToken, Long> {

    Optional findById(Long id);

    @Query(value = "SELECT * FROM resettoken r WHERE r.emailresettoken = :emailresettoken",
            nativeQuery = true)
    ResetToken findPasswordResetToken(
            @Param("emailresettoken") String emailresettoken);
}
