package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "Attempt.threeLastEntries",query = "select a from AttemptEntity a where a.userId = ?1 and a.allowed = false order by a.date desc")
@Table(name = "attempt", schema = "public", catalog = "login")
public class AttemptEntity {
    private int attemptId;
    private int userId;
    private boolean allowed;
    private Timestamp date;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "attempt_id", nullable = false)
    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "allowed", nullable = false)
    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttemptEntity that = (AttemptEntity) o;

        if (attemptId != that.attemptId) return false;
        if (userId != that.userId) return false;
        if (allowed != that.allowed) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = attemptId;
        result = 31 * result + userId;
        result = 31 * result + (allowed ? 1 : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
