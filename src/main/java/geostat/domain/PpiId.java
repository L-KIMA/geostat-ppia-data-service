package geostat.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PpiId implements Serializable {

    @Column(name = "SID")
    private String sid;

    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    public PpiId() {
    }

    public PpiId(String sid, Long surveyId, Integer year, Integer month) {
        this.sid = sid;
        this.surveyId = surveyId;
        this.year = year;
        this.month = month;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PpiId)) return false;
        PpiId that = (PpiId) o;
        return sid.equals(that.sid) &&
                surveyId.equals(that.surveyId) &&
                year.equals(that.year) &&
                month.equals(that.month);
    }

    @Override
    public int hashCode() {
        int result = sid.hashCode();
        result = 31 * result + surveyId.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + month.hashCode();
        return result;
    }
}