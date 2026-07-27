package geostat.model.procedure;

public class ProcContext {

    private final int surveyId;
    private final int year;
    private final int quarter;
    private final int month;

    public ProcContext(int surveyId, int year, int quarter, int month) {
        this.surveyId = surveyId;
        this.year = year;
        this.quarter = quarter;
        this.month = month;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public int getYear() {
        return year;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getMonth() {
        return month;
    }
}