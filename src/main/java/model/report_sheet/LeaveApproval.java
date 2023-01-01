package model.report_sheet;

import java.util.Date;

public class LeaveApproval {
    private Integer form_num;
    private String student_ID;
    private Date timestamp;
    private String destination;
    private String reason;
    private Date leave_date;
    private Date entry_date;
    private Integer status;
    private String refuse_reason;

    public LeaveApproval(Integer form_num, String student_ID, Date timestamp, String reason, String destination, Date leave_date,
                         Date entry_date, Integer status, String refuse_reason)
    {
        this.form_num = form_num;
        this.student_ID = student_ID;
        this.timestamp = timestamp;
        this.reason = reason;
        this.destination = destination;
        this.leave_date = leave_date;
        this.entry_date = entry_date;
        this.status = status;
        this.refuse_reason = refuse_reason;
    }

    public Integer getForm_num()
    {
        return this.form_num;
    }
    public String getStudent_ID()
    {
        return this.student_ID;
    }
    public Date getTimestamp()
    {
        return this.timestamp;
    }
    public String getReason()
    {
        return this.reason;
    }
    public String getDestination()
    {
        return this.destination;
    }
    public Date getLeave_date()
    {
        return this.leave_date;
    }
    public Date getEntry_date()
    {
        return this.entry_date;
    }
    public Integer getStatus()
    {
        return this.status;
    }
    public String getRefuse_reason()
    {
        return this.refuse_reason;
    }
    public void setStatus(Integer new_status)
    {
        this.status = new_status;
    }
    public void setReason(String new_reason)
    {
        this.reason = new_reason;
    }
    public void setDestination(String destination)
    {
        this.destination = destination;
    }
    public void setLeave_date(java.sql.Date date)
    {
        this.leave_date = date;
    }
    public void setEntry_date(java.sql.Date date)
    {
        this.entry_date = date;
    }
    public void setRefuse_reason(String refuse_reason)
    {
        this.refuse_reason = refuse_reason;
    }



}
