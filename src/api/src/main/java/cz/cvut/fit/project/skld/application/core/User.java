package cz.cvut.fit.project.skld.application.core;

import javax.persistence.*;
import java.security.Principal;
import java.time.Instant;
import java.util.Objects;

/**
 * Reprezentuje uzivatele z domenoveho modelu.
 */
@Entity
@Table(
        name="users",
        indexes = @Index(
                name = "idx_user_pin",
                columnList = "PIN",
                unique = true
        )
)
@NamedQueries({
        @NamedQuery(
                name = "User.findByPIN",
                query = "SELECT u FROM User u WHERE u.pin = :pin AND u.blockedAt IS NULL"
        )
})
public class User implements Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @Column(name="blocked_at")
    private Instant blockedAt;

    @Column(name="PIN")
    private String pin;

    @Column(name="is_admin", nullable = false)
    private boolean isAdmin;

    /**
     * Konstruktor.
     */
    public User() {}

    /**
     * Konstruktor.
     * @param name Jmeno uzivatele
     * @param PIN PIN kod, kterym se uzivatel prihlasuje
     */
    public User(String name, String PIN) {
        setName(name);
        setPin(PIN);
    }

    /**
     * Overi, zda zadany PIN odpovida uzivatelove PINu.
     * @param comparedPIN Porovnavany PIN
     * @return True zadany PIN odpovida uzivatelove PINu
     */
    public boolean comparePIN(String comparedPIN) {
        return pin.equals(comparedPIN);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Instant blockedAt) {
        this.blockedAt = blockedAt;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                isAdmin == user.isAdmin &&
                Objects.equals(name, user.name) &&
                Objects.equals(createdAt, user.createdAt) &&
                Objects.equals(blockedAt, user.blockedAt) &&
                Objects.equals(pin, user.pin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, createdAt, blockedAt, pin, isAdmin);
    }
}
