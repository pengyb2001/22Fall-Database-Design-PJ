package model.report_sheet;

import java.util.Date;

public class DailyReport {
    private Integer report_num;
    private String student_ID;
    private Date timestamp;
    private String location;
    private Integer is_healthy;

    public DailyReport(Integer report_num, String student_ID, Date timestamp, String location, Integer is_healthy)
    {
        this.report_num = report_num;
        this.student_ID = student_ID;
        this.timestamp = timestamp;
        this.location = location;
        this.is_healthy = is_healthy;
    }

    public Integer getReport_num()
    {
        return this.report_num;
    }
    public String getStudent_ID()
    {
        return this.student_ID;
    }
    public Date getTimestamp()
    {
        return this.timestamp;
    }
    public String getLocation()
    {
        return this.location;
    }
    public Integer getIs_healthy()
    {
        return this.is_healthy;
    }
}
