package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "User.findByUserName",query = "SELECT u FROM UserEntity u WHERE u.userName LIKE ?1")
@Table(name = "user", schema = "public", catalog = "login")
public class UserEntity {
    private int userId;
    private String userName;
    private Timestamp lastlyBlocked;
    private String secretKey;
    private String gmailAdress;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_name", nullable = false, length = -1)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "lastly_blocked", nullable = true)
    public Timestamp getLastlyBlocked() {
        return lastlyBlocked;
    }

    public void setLastlyBlocked(Timestamp lastlyBlocked) {
        this.lastlyBlocked = lastlyBlocked;
    }

    @Basic
    @Column(name = "secret_key", nullable = true, length = -1)
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Basic
    @Column(name = "gmail_adress", nullable = true, length = -1)
    public String getGmailAdress() {
        return gmailAdress;
    }

    public void setGmailAdress(String gmailAdress) {
        this.gmailAdress = gmailAdress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (lastlyBlocked != null ? !lastlyBlocked.equals(that.lastlyBlocked) : that.lastlyBlocked != null)
            return false;
        if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
        if (gmailAdress != null ? !gmailAdress.equals(that.gmailAdress) : that.gmailAdress != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (lastlyBlocked != null ? lastlyBlocked.hashCode() : 0);
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        result = 31 * result + (gmailAdress != null ? gmailAdress.hashCode() : 0);
        return result;
    }
}
