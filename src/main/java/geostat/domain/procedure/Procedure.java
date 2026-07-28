package geostat.domain.procedure;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "questionnaire_procedure_call_history", schema = "dbo", catalog = "KD")
public class Procedure {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "year")
    private Integer year;

    @Column(name = "quarter")
    private Integer quarter;

    @Column(name = "month")
    private Integer month;

    @Column(name = "survey_id", nullable = false)
    private Long surveyId;

    @Column(name = "error_message")
    private String errorMessage;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started_at")
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finished_at")
    private Date finishedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "result")
    private Integer result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}