package model.account;

import java.util.Date;

public class ContinuousStudent{
    private String student_ID;
    private int day_count;
    private Date start_day;

    public ContinuousStudent(String student_ID, int day_count, Date start_day)
    {
        this.student_ID = student_ID;
        this.day_count = day_count;
        this.start_day = start_day;
    }

    public String getStudent_ID(){
        return this.student_ID;
    }

    public int getDay_count(){
        return this.day_count;
    }

    public Date getStart_day(){
        return this.start_day;
    }


}
