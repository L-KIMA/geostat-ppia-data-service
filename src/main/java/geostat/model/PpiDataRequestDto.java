package geostat.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class PpiDataRequestDto {

    @NotNull
    private Long surveyId;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotEmpty
    private Set<String> sidCodes;

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

    public Set<String> getSidCodes() {
        return sidCodes;
    }

    public void setSidCodes(Set<String> sidCodes) {
        this.sidCodes = sidCodes;
    }
}
