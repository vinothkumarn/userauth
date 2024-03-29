package cobalt.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(
        name = "users"
)
@NoArgsConstructor()
@AllArgsConstructor()
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public void setPassword(String password) {
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = encryptedPassword;
    }
}
