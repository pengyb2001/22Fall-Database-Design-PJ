package model.report_sheet;

import java.util.Date;

public class LeaveApproval {
    private Integer form_num;
    private String student_ID;
    private Date timestamp;
    private String reason;
    private Date leave_date;
    private Date entry_date;
    private Integer status;
    private String refuse_reason;

    public LeaveApproval(Integer form_num, String student_ID, Date timestamp, String reason, Date leave_date,
                         Date entry_date, Integer status)
    {
        this.form_num = form_num;
        this.student_ID = student_ID;
        this.timestamp = timestamp;

    }
}
