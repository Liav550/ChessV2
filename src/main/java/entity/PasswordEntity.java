package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "Password.mostRecent",
        query = "select e from PasswordEntity e where e.creationDate = " +
                "(SELECT max(p.creationDate) FROM PasswordEntity p WHERE p.userId = ?1) and e.userId = ?1")
@Table(name = "password", schema = "public", catalog = "login")
public class PasswordEntity {
    private int passwordId;
    private String passwordText;
    private Timestamp creationDate;
    private int userId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "password_id", nullable = false)
    public int getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(int passwordId) {
        this.passwordId = passwordId;
    }

    @Basic
    @Column(name = "password_text", nullable = false, length = -1)
    public String getPasswordText() {
        return passwordText;
    }

    public void setPasswordText(String passwordText) {
        this.passwordText = passwordText;
    }

    @Basic
    @Column(name = "creation_date", nullable = false)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordEntity that = (PasswordEntity) o;

        if (passwordId != that.passwordId) return false;
        if (userId != that.userId) return false;
        if (passwordText != null ? !passwordText.equals(that.passwordText) : that.passwordText != null) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = passwordId;
        result = 31 * result + (passwordText != null ? passwordText.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + userId;
        return result;
    }
}
