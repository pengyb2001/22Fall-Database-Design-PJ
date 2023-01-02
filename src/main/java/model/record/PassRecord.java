package model.record;

import model.account.Instructor;
import service.sql.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class PassRecord {
    private Integer pass_num;
    private String student_ID;
    private Date timestamp;
    private String campus_name;
    private Integer type;

    public PassRecord(Integer pass_num, String student_ID, Date timestamp, String campus_name, Integer type)
    {
        this.pass_num = pass_num;
        this.student_ID = student_ID;
        this.timestamp = timestamp;
        this.campus_name = campus_name;
        this.type = type;
    }

    public Integer getPass_num()
    {
        return this.pass_num;
    }

    public String getStudent_ID()
    {
        return this.student_ID;
    }

    public String getCampus_name()
    {
        return this.campus_name;
    }

    public Integer getType()
    {
        return this.type;
    }

    public Date getTimestamp()
    {
        return this.timestamp;
    }

    public static void addRecord(String student_ID, String campus_name, Integer type)
    {
        try
        {
            //增加进出校记录
            Connection con = SQLUtil.getConnection();
            PreparedStatement addNewPassRecord = con.prepareStatement("insert into pass_record(student_ID, timestamp, campus_name, type)" +
                    "values (?, now(), ?, ?)" );
            addNewPassRecord.setString(1, student_ID);
//            addNewDailyReport.setDate(2, (java.sql.Date) timestamp);
            addNewPassRecord.setString(2, campus_name);
            addNewPassRecord.setInt(3, type);
            addNewPassRecord.executeUpdate();
            con.close();
        }
        catch (Exception e)
        {
            SQLUtil.handleExceptions(e);
        }
    }
}
