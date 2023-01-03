package model.record;

import model.account.Student;

public class AvgOutTime implements Comparable {
    private String student_ID;
    private Long avgOutTime;

    public AvgOutTime(String student_ID, Long avgOutTime)
    {
        this.avgOutTime = avgOutTime;
        this.student_ID = student_ID;
    }
    public String getStudent_ID(){return this.student_ID;}
    public Long getAvgOutTime(){return this.avgOutTime;}
    public void setAvgOutTime() {this.avgOutTime = avgOutTime;}
    public void setStudent_ID() {this.student_ID = student_ID;}

    @Override
    public int compareTo(Object o) {
        AvgOutTime s = (AvgOutTime) o;
        return (int)(s.avgOutTime - this.avgOutTime);
    }
}
