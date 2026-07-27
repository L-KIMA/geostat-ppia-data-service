package geostat.model.procedure;

public class ProcCall {

    private final String sql;
    private final Integer[] params;
    private final boolean hasOutParam;

    public ProcCall(String sql, Integer[] params, boolean hasOutParam) {
        this.sql = sql;
        this.params = params;
        this.hasOutParam = hasOutParam;
    }

    public String getSql() { return sql; }
    public Integer[] getParams() { return params; }
    public boolean isHasOutParam() { return hasOutParam; }
}