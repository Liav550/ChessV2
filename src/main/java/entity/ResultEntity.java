package entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "result", schema = "public", catalog = "login")
public class ResultEntity {
    private int resultId;
    private int userId;
    private int result;
    private Timestamp date;

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
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "result", nullable = false)
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

        ResultEntity that = (ResultEntity) o;

        if (resultId != that.resultId) return false;
        if (userId != that.userId) return false;
        if (result != that.result) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = resultId;
        result1 = 31 * result1 + userId;
        result1 = 31 * result1 + result;
        result1 = 31 * result1 + (date != null ? date.hashCode() : 0);
        return result1;
    }
}
