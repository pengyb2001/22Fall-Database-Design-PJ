package model.record;

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
