package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@NamedQuery(name = "Result.getLeaders",query = "SELECT r.userId, SUM(r.pointsEarned) AS totalPoints FROM ResultEntity r " +
        "GROUP BY r.userId" +
        " ORDER BY totalPoints DESC")
@NamedQuery(name = "Result.getTotalPointsById", query = "SELECT SUM(r.pointsEarned) as totalPoints FROM ResultEntity r" +
        " WHERE r.userId = ?1")
@Table(name = "result", schema = "public", catalog = "login")
public class ResultEntity {
    private int resultId;
    private Timestamp date;
    private int userId;
    private int pointsEarned;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "result_id", nullable = false)
    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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
    @Column(name = "points_earned", nullable = false)
    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultEntity that = (ResultEntity) o;

        if (resultId != that.resultId) return false;
        if (userId != that.userId) return false;
        if (pointsEarned != that.pointsEarned) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = resultId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + pointsEarned;
        return result;
    }
}
